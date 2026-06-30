from __future__ import annotations

import base64
import io
import re
from pathlib import Path

from docx import Document
from docx.enum.text import WD_ALIGN_PARAGRAPH
from docx.oxml.ns import qn
from docx.table import Table
from docx.text.paragraph import Paragraph as DocxParagraph

from reportlab.lib import colors
from reportlab.lib.pagesizes import A4
from reportlab.lib.styles import ParagraphStyle, getSampleStyleSheet
from reportlab.lib.units import cm
from reportlab.pdfbase import pdfmetrics
from reportlab.pdfbase.ttfonts import TTFont
from reportlab.platypus import (
    Image,
    ListFlowable,
    ListItem,
    PageBreak,
    Paragraph as RLParagraph,
    SimpleDocTemplate,
    Spacer,
    Table as RLTable,
    TableStyle,
)

ROOT = Path(__file__).resolve().parents[1]
REPORT_DIR = ROOT / "deliverables" / "reports"

FONT_PATHS = [
    "/System/Library/Fonts/Supplemental/Arial Unicode.ttf",
    "/System/Library/Fonts/PingFang.ttc",
]
CHINESE_FONT = None
for fp in FONT_PATHS:
    if Path(fp).exists():
        try:
            name = Path(fp).stem.replace(" ", "")
            pdfmetrics.registerFont(TTFont(name, fp))
            CHINESE_FONT = name
            break
        except Exception:
            pass

if CHINESE_FONT is None:
    raise RuntimeError("No Chinese font found")


def register_styles():
    styles = getSampleStyleSheet()
    base = ParagraphStyle(
        "Base",
        parent=styles["Normal"],
        fontName=CHINESE_FONT,
        fontSize=11,
        leading=20,
        spaceAfter=10,
    )
    styles.add(base)
    styles.add(ParagraphStyle("CN-Title", parent=base, fontSize=24, alignment=1, spaceAfter=24, leading=32))
    styles.add(ParagraphStyle("CN-Subtitle", parent=base, fontSize=16, alignment=1, spaceAfter=80, leading=24))
    styles.add(ParagraphStyle("CN-Heading1", parent=base, fontSize=18, spaceBefore=24, spaceAfter=14, leading=26))
    styles.add(ParagraphStyle("CN-Heading2", parent=base, fontSize=15, spaceBefore=18, spaceAfter=10, leading=22))
    styles.add(ParagraphStyle("CN-Heading3", parent=base, fontSize=13, spaceBefore=14, spaceAfter=8, leading=20))
    styles.add(ParagraphStyle("CN-Body", parent=base, firstLineIndent=22, alignment=4))
    styles.add(ParagraphStyle("CN-NoIndent", parent=base, firstLineIndent=0))
    styles.add(ParagraphStyle("CN-Bullet", parent=base, leftIndent=28, bulletIndent=10, bulletFontName=CHINESE_FONT))
    styles.add(ParagraphStyle("CN-Number", parent=base, leftIndent=28, bulletIndent=10, bulletFontName=CHINESE_FONT))
    styles.add(ParagraphStyle("CN-Caption", parent=base, fontSize=10, alignment=1, spaceAfter=14, leading=16))
    styles.add(ParagraphStyle("CN-CoverInfo", parent=base, fontSize=12, alignment=1, spaceAfter=8, leading=18))
    return styles


def clean_text(text: str) -> str:
    if not text:
        return ""
    text = text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
    text = text.replace("\n", "<br/>")
    return text


def extract_inline_images(paragraph: DocxParagraph):
    """Return list of (image_bytes, ext) for inline images in paragraph."""
    images = []
    for run in paragraph.runs:
        drawing = run._element.find(qn("w:drawing"))
        if drawing is None:
            continue
        blip = drawing.find(".//" + qn("a:blip"))
        if blip is None:
            continue
        embed = blip.get(qn("r:embed"))
        if embed:
            part = paragraph.part.related_parts.get(embed)
            if part:
                ext = part.content_type.split("/")[-1]
                images.append((part.blob, ext))
    return images


def iter_block_items(doc):
    for child in doc.element.body.iterchildren():
        if child.tag == qn("w:p"):
            yield DocxParagraph(child, doc)
        elif child.tag == qn("w:tbl"):
            yield Table(child, doc)


def is_list_paragraph(p: DocxParagraph) -> bool:
    numPr = p._element.find(".//" + qn("w:numPr"))
    return numPr is not None


def is_list_bullet(p: DocxParagraph) -> bool:
    if not is_list_paragraph(p):
        return False
    # The DOCX generator uses explicit "List Bullet" / "List Number" styles.
    return p.style.name.startswith("List Bullet")


def render_docx(docx_path: Path, pdf_path: Path):
    doc = Document(docx_path)
    styles = register_styles()
    story = []
    in_cover = True

    for item in iter_block_items(doc):
        if isinstance(item, Table):
            data = []
            for row in item.rows:
                cells = [clean_text(cell.text) for cell in row.cells]
                data.append(cells)
            if data:
                n_cols = len(data[0])
                col_widths = [16 * cm / n_cols] * n_cols
                if n_cols == 2:
                    col_widths = [5 * cm, 11 * cm]
                rl_table = RLTable(data, colWidths=col_widths, repeatRows=1)
                rl_table.setStyle(
                    TableStyle([
                        ("GRID", (0, 0), (-1, -1), 0.5, colors.grey),
                        ("VALIGN", (0, 0), (-1, -1), "MIDDLE"),
                        ("FONTNAME", (0, 0), (-1, -1), CHINESE_FONT),
                        ("FONTSIZE", (0, 0), (-1, -1), 11),
                        ("LEFTPADDING", (0, 0), (-1, -1), 8),
                        ("RIGHTPADDING", (0, 0), (-1, -1), 8),
                        ("TOPPADDING", (0, 0), (-1, -1), 6),
                        ("BOTTOMPADDING", (0, 0), (-1, -1), 6),
                    ])
                )
                story.append(rl_table)
                story.append(Spacer(1, 16))
                in_cover = False
            continue

        p = item
        text = p.text
        if not text:
            # Could still have images
            pass

        style_name = "CN-Body"
        is_center = p.alignment == WD_ALIGN_PARAGRAPH.CENTER
        is_heading = p.style.name.startswith("Heading")

        if is_heading:
            if p.style.name.startswith("Heading 1"):
                style_name = "CN-Heading1"
            elif p.style.name.startswith("Heading 2"):
                style_name = "CN-Heading2"
            elif p.style.name.startswith("Heading 3"):
                style_name = "CN-Heading3"
            story.append(PageBreak())
            in_cover = False
        elif is_center and in_cover:
            if "报告" in text or "说明书" in text:
                style_name = "CN-Subtitle"
            elif len(text) < 20:
                style_name = "CN-Title"
            else:
                style_name = "CN-CoverInfo"
        elif is_list_paragraph(p):
            style_name = "CN-Bullet"
        elif re.match(r"^图\s*\d+", text):
            style_name = "CN-Caption"
        elif re.match(r"^表\s*\d+", text):
            style_name = "CN-Caption"
        else:
            style_name = "CN-Body"
            in_cover = False

        # Add images in paragraph
        imgs = extract_inline_images(p)
        if imgs:
            story.append(Spacer(1, 8))
        for blob, ext in imgs:
            try:
                img = Image(io.BytesIO(blob), width=15 * cm, height=15 * cm)
                # preserve aspect ratio
                img._restrictSize(15 * cm, 20 * cm)
                story.append(img)
                story.append(Spacer(1, 6))
            except Exception:
                pass

        if text.strip():
            story.append(RLParagraph(clean_text(text), styles[style_name]))

    # Extract footer text from the DOCX section so it appears on every PDF page.
    footer_text = ""
    for section in doc.sections:
        for paragraph in section.footer.paragraphs:
            if paragraph.text.strip():
                footer_text = paragraph.text.strip()
                break
        if footer_text:
            break

    def draw_footer(canvas, doc):
        if not footer_text:
            return
        canvas.saveState()
        canvas.setFont(CHINESE_FONT, 9)
        canvas.drawCentredString(A4[0] / 2, 1 * cm, footer_text)
        canvas.restoreState()

    doc_template = SimpleDocTemplate(
        str(pdf_path),
        pagesize=A4,
        rightMargin=2 * cm,
        leftMargin=2 * cm,
        topMargin=2.5 * cm,
        bottomMargin=2 * cm,
    )
    doc_template.build(story, onFirstPage=draw_footer, onLaterPages=draw_footer)
    print(pdf_path)


def main():
    mappings = [
        ("网上书店-课程设计报告.docx", "网上书店-课程设计报告.pdf"),
        ("网上书店-系统使用说明书.docx", "网上书店-系统使用说明书.pdf"),
        ("网上书店-技术报告.docx", "网上书店-技术报告.pdf"),
    ]
    for docx_name, pdf_name in mappings:
        render_docx(REPORT_DIR / docx_name, REPORT_DIR / pdf_name)


if __name__ == "__main__":
    main()
