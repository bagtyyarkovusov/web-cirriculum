from __future__ import annotations

from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
SEED_PATH = ROOT / "src" / "main" / "resources" / "seed.sql"

HEADER = """-- Online Bookstore — seed data. Run AFTER schema.sql:
--   mysql -uroot < schema.sql && mysql -uroot < seed.sql
SET NAMES utf8mb4;
USE bookstore;
"""

CATEGORIES = [
    (1, '计算机', None),
    (2, '文学艺术', None),
    (3, '科技', None),
    (4, '外语', None),
    (5, '考试辅导', None),
    (6, '历史', None),
    (7, '经济管理', None),
    (8, '童书', None),
]

BOOKS = [
    # Existing rows 1-9 (kept)
    (1, 'Java核心技术 卷I（原书第12版）', 'Cay S. Horstmann', '机械工业出版社', '9787111709473', 149.00, 50, 1, None, 'Java入门与进阶的经典教程。', 'ON'),
    (2, '深入理解Java虚拟机（第3版）', '周志明', '机械工业出版社', '9787111641247', 129.00, 30, 1, None, 'JVM 原理与实战。', 'ON'),
    (3, '算法导论（原书第3版）', 'Thomas H. Cormen', '机械工业出版社', '9787111407010', 128.00, 20, 1, None, '算法领域的权威著作。', 'ON'),
    (4, '活着', '余华', '作家出版社', '9787506365437', 45.00, 80, 2, None, '一部关于生命与苦难的小说。', 'ON'),
    (5, '三体（全三册）', '刘慈欣', '重庆出版社', '9787229137939', 168.00, 60, 2, None, '中国科幻里程碑之作。', 'ON'),
    (6, '时间简史', '史蒂芬·霍金', '湖南科学技术出版社', '9787535732309', 45.00, 40, 3, None, '探索宇宙与时间的本质。', 'ON'),
    (7, '新概念英语（2）', '亚历山大、何其莘', '外语教学与研究出版社', '9787560022086', 38.00, 100, 4, None, '经典英语学习教材。', 'ON'),
    (8, '考研数学复习全书', '李永乐', '中国农业出版社', '9787109267701', 89.00, 25, 5, None, '考研数学系统复习用书。', 'ON'),
    (9, '已下架示例图书', '示例', '示例出版社', '0000000000000', 1.00, 0, 1, None, '该书已下架，用户端不可见。', 'OFF'),
    # New rows 10-50
    (10, '代码大全（第2版）', 'Steve McConnell', '电子工业出版社', '9787121026003', 128.00, 22, 1, None, '软件构建实践经典。', 'ON'),
    (11, '重构（第2版）', 'Martin Fowler', '人民邮电出版社', '9787115508645', 99.00, 5, 1, None, '改善既有代码设计。', 'ON'),
    (12, '设计模式', 'Erich Gamma 等', '机械工业出版社', '9787111075752', 89.00, 18, 1, None, '可复用面向对象软件的基础。', 'ON'),
    (13, 'Spring实战（第6版）', 'Craig Walls', '人民邮电出版社', '9787115564756', 119.00, 14, 1, None, 'Spring 框架实战指南。', 'ON'),
    (14, 'Clean Code', 'Robert C. Martin', 'Prentice Hall', '9780132350884', 139.00, 10, 1, None, '代码整洁之道。', 'ON'),
    (15, 'Effective Java（第3版）', 'Joshua Bloch', 'Addison-Wesley', '9780134685991', 149.00, 8, 1, None, 'Java 编程最佳实践。', 'ON'),
    (16, 'Python编程：从入门到实践', 'Eric Matthes', '人民邮电出版社', '9787115428028', 89.00, 35, 1, None, 'Python 入门经典。', 'ON'),
    (17, '黑客与画家', 'Paul Graham', '人民邮电出版社', '9787115249494', 59.00, 3, 1, None, '硅谷创业教父文集。', 'ON'),
    (18, '百年孤独', '加西亚·马尔克斯', '南海出版公司', '9787544253994', 55.00, 45, 2, None, '魔幻现实主义文学代表作。', 'ON'),
    (19, '围城', '钱钟书', '人民文学出版社', '9787020090006', 39.00, 55, 2, None, '中国现代文学经典。', 'ON'),
    (20, '红楼梦', '曹雪芹', '人民文学出版社', '9787020002207', 68.00, 40, 2, None, '中国古典小说巅峰。', 'ON'),
    (21, '平凡的世界', '路遥', '北京十月文艺出版社', '9787530216781', 108.00, 28, 2, None, '展现中国城乡社会变迁。', 'ON'),
    (22, '白夜行', '东野圭吾', '南海出版公司', '9787544255899', 49.00, 33, 2, None, '悬疑小说代表作。', 'ON'),
    (23, ' Norse Mythology', 'Neil Gaiman', 'W. W. Norton', '9780393356182', 75.00, 12, 2, None, '北欧神话新编。', 'ON'),
    (24, '自私的基因', 'Richard Dawkins', '中信出版社', '9787508647357', 68.00, 22, 3, None, '进化生物学经典。', 'ON'),
    (25, '万物简史', '比尔·布莱森', '接力出版社', '9787544831488', 58.00, 17, 3, None, '科学史通俗读物。', 'ON'),
    (26, '量子力学是什么', 'Sean Carroll', '中信出版社', '9787521730001', 72.00, 9, 3, None, '量子力学入门。', 'ON'),
    (27, '宇宙', '卡尔·萨根', '广西科学技术出版社', '9787555109521', 88.00, 11, 3, None, '探索宇宙奥秘。', 'ON'),
    (28, '人类简史', '尤瓦尔·赫拉利', '中信出版社', '9787508647358', 68.00, 26, 3, None, '人类发展历程。', 'ON'),
    (29, '牛津高阶英汉双解词典（第10版）', 'A. S. Hornby', '商务印书馆', '9787100200572', 198.00, 15, 4, None, '权威英语学习词典。', 'ON'),
    (30, '剑桥雅思真题18', 'Cambridge', '剑桥大学出版社', '9781009498916', 128.00, 20, 4, None, '雅思官方真题集。', 'ON'),
    (31, 'Word Power Made Easy', 'Norman Lewis', 'Anchor', '9781101873854', 45.00, 18, 4, None, '词汇学习经典。', 'ON'),
    (32, '考研英语词汇红宝书', '俞敏洪', '西安交通大学出版社', '9787560542386', 58.00, 7, 5, None, '考研英语核心词汇。', 'ON'),
    (33, '肖秀荣考研政治1000题', '肖秀荣', '国家开放大学出版社', '9787304098765', 62.00, 19, 5, None, '考研政治练习题集。', 'ON'),
    (34, '注册会计师教材：会计', '中国注册会计师协会', '中国财政经济出版社', '9787522301234', 89.00, 4, 5, None, 'CPA 会计科目教材。', 'ON'),
    (35, '史记', '司马迁', '中华书局', '9787101003048', 128.00, 16, 6, None, '中国第一部纪传体通史。', 'ON'),
    (36, '万历十五年', '黄仁宇', '生活·读书·新知三联书店', '9787108004676', 42.00, 30, 6, None, '大历史观代表作。', 'ON'),
    (37, '枪炮、病菌与钢铁', '贾雷德·戴蒙德', '上海译文出版社', '9787532744985', 68.00, 21, 6, None, '人类社会发展史。', 'ON'),
    (38, '明朝那些事儿', '当年明月', '中国友谊出版公司', '9787505722460', 199.00, 13, 6, None, '通俗历史读物。', 'ON'),
    (39, '耶路撒冷三千年', 'Simon Sebag Montefiore', '民主与建设出版社', '9787513903291', 118.00, 6, 6, None, '耶路撒冷城市史。', 'ON'),
    (40, '罗马帝国衰亡史', '爱德华·吉本', '商务印书馆', '9787100018654', 168.00, 2, 6, None, '西方史学巨著。', 'OFF'),
    (41, '经济学原理（第8版）', 'N. Gregory Mankiw', '北京大学出版社', '9787301316180', 128.00, 20, 7, None, '经济学入门教材。', 'ON'),
    (42, '穷查理宝典', '查理·芒格', '中信出版社', '9787508685168', 168.00, 11, 7, None, '芒格智慧箴言录。', 'ON'),
    (43, '从0到1', 'Peter Thiel', '中信出版社', '9787508647356', 52.00, 25, 7, None, '创业与商业思维。', 'ON'),
    (44, '巴菲特致股东的信', '沃伦·巴菲特', '机械工业出版社', '9787111563006', 79.00, 14, 7, None, '投资智慧合集。', 'ON'),
    (45, '财务报表分析', '马丁·弗里德森', '中国人民大学出版社', '9787300234569', 98.00, 8, 7, None, '财务分析实务。', 'ON'),
    (46, '创新者的窘境', 'Clayton Christensen', '中信出版社', '9787508683737', 69.00, 5, 7, None, '商业创新经典。', 'OFF'),
    (47, '哈利·波特与魔法石', 'J. K. Rowling', '人民文学出版社', '9787020033430', 59.00, 40, 8, None, '魔法世界开启之作。', 'ON'),
    (48, '小王子', '圣埃克苏佩里', '天津人民出版社', '9787201072627', 32.00, 60, 8, None, '献给大人的童话。', 'ON'),
    (49, '夏洛的网', 'E. B. White', '上海译文出版社', '9787532761339', 35.00, 28, 8, None, '关于友谊的童书经典。', 'ON'),
    (50, '窗边的小豆豆', '黑柳彻子', '南海出版公司', '9787544241984', 45.00, 15, 8, None, '日本儿童文学经典。', 'ON'),
]

DEMO_PASSWORD_SM3 = 'da4886d49f5694d5d252baf1d90d8bc02030469eba613ff499d0eb1b6e13ad93'
DEMO_SALT = '74ae25abc1b9080dcc90715881962b30'

USERS = [
    # Existing rows 1-4
    (1, 'customer', DEMO_PASSWORD_SM3, DEMO_SALT, '张三', 'M', 'CMrkDR2GIUqkqPDuFHU02RkvAMXLthXv3AA0gPzcP2c=', None, 'CUSTOMER', 'ACTIVE'),
    (2, 'operator', '71128fa23502a7d52ecad197b7bfad87312b292632f9870bd111cae62d49afcb', '3c8d7cfeface30b4bd4d4b99a2975f4d', '运营小李', 'F', 'VsxRrmLzs69DA+f70F2/8MpaIhgPZsNItagkx+858g0=', None, 'OPERATOR_ADMIN', 'ACTIVE'),
    (3, 'sysadmin', '17d736a9a3c8088994a46b40bab10d376e4e5a0420e36c0af1e0f7beb974f224', '9e495e1d9d34d27642d0f1db8e9862e7', '系统管理员', 'M', 'gt42I0mTS2nJaJa1x8syntYwLBguetCbMwVQ82kmjnA=', None, 'SYSTEM_ADMIN', 'ACTIVE'),
    (4, 'auditor', '57bea300cff3f3f5c048c8ea1d3c72c66c9ad114cffadf985488d8a72e2f2712', 'f0bea610b392a9be65b6657e87f88557', '审计员', 'F', '42BHvTuMi4xNcKZgNrVruLtMV2oOWmSzcRuk6s3wqi8=', None, 'AUDIT_ADMIN', 'ACTIVE'),
    # New customers 5-9
    (5, 'wangfang', DEMO_PASSWORD_SM3, DEMO_SALT, '王芳', 'F', None, None, 'CUSTOMER', 'ACTIVE'),
    (6, 'lihua', DEMO_PASSWORD_SM3, DEMO_SALT, '李华', 'M', None, None, 'CUSTOMER', 'ACTIVE'),
    (7, 'zhaolei', DEMO_PASSWORD_SM3, DEMO_SALT, '赵磊', 'M', None, None, 'CUSTOMER', 'ACTIVE'),
    (8, 'sunli', DEMO_PASSWORD_SM3, DEMO_SALT, '孙丽', 'F', None, None, 'CUSTOMER', 'ACTIVE'),
    (9, 'zhouyu', DEMO_PASSWORD_SM3, DEMO_SALT, '周宇', 'M', None, None, 'CUSTOMER', 'ACTIVE'),
]

ORDERS = [
    # (id, order_no, user_id, total, status, receiver, tracking, created_at, paid_at, shipped_at, completed_at)
    (1, 'O2026063008010000001001', 1, 149.00, 'COMPLETED', '张三 / 北京', 'YT202606300001', '2026-06-20 08:01:00', '2026-06-20 08:05:00', '2026-06-21 09:00:00', '2026-06-25 16:00:00'),
    (2, 'O2026061509120000002001', 5, 128.00, 'PENDING_PAY', '王芳 / 北京', None, '2026-06-15 09:12:00', None, None, None),
    (3, 'O2026061814030000003001', 6, 200.00, 'PENDING_SHIP', '李华 / 上海', None, '2026-06-18 14:03:00', '2026-06-18 14:05:00', None, None),
    (4, 'O2026062010450000004001', 7, 168.00, 'SHIPPED', '赵磊 / 广州', 'YT202606200001', '2026-06-20 10:45:00', '2026-06-20 10:46:00', '2026-06-21 09:00:00', None),
    (5, 'O2026062511300000005001', 8, 89.00, 'COMPLETED', '孙丽 / 成都', 'YT202606250002', '2026-06-25 11:30:00', '2026-06-25 11:31:00', '2026-06-26 08:30:00', '2026-06-28 16:00:00'),
    (6, 'O2026062812150000006001', 9, 45.00, 'CANCELLED', '周宇 / 武汉', None, '2026-06-28 12:15:00', None, None, None),
    (7, 'O2026061018300000007001', 1, 219.00, 'COMPLETED', '张三 / 北京', 'YT202606100003', '2026-06-10 18:30:00', '2026-06-10 18:32:00', '2026-06-11 10:00:00', '2026-06-14 14:00:00'),
    (8, 'O2026062211000000008001', 5, 77.00, 'PENDING_SHIP', '王芳 / 北京', None, '2026-06-22 11:00:00', '2026-06-22 11:02:00', None, None),
    (9, 'O2026062316200000009001', 6, 149.00, 'SHIPPED', '李华 / 上海', 'YT202606230004', '2026-06-23 16:20:00', '2026-06-23 16:21:00', '2026-06-24 09:30:00', None),
    (10, 'O2026062610000000010001', 7, 77.00, 'COMPLETED', '赵磊 / 广州', 'YT202606260005', '2026-06-26 10:00:00', '2026-06-26 10:01:00', '2026-06-27 08:00:00', '2026-06-29 10:00:00'),
    (11, 'O2026062909150000011001', 8, 128.00, 'PENDING_PAY', '孙丽 / 成都', None, '2026-06-29 09:15:00', None, None, None),
    (12, 'O2026061414000000012001', 9, 99.00, 'CANCELLED', '周宇 / 武汉', None, '2026-06-14 14:00:00', None, None, None),
    (13, 'O2026061709300000013001', 1, 89.00, 'PENDING_SHIP', '张三 / 北京', None, '2026-06-17 09:30:00', '2026-06-17 09:31:00', None, None),
    (14, 'O2026061908000000014001', 5, 168.00, 'SHIPPED', '王芳 / 北京', 'YT202606190006', '2026-06-19 08:00:00', '2026-06-19 08:01:00', '2026-06-20 09:00:00', None),
    (15, 'O2026062409300000015001', 6, 45.00, 'COMPLETED', '李华 / 上海', 'YT202606240007', '2026-06-24 09:30:00', '2026-06-24 09:31:00', '2026-06-25 08:30:00', '2026-06-27 12:00:00'),
    (16, 'O2026062107000000016001', 7, 128.00, 'PENDING_PAY', '赵磊 / 广州', None, '2026-06-21 07:00:00', None, None, None),
    (17, 'O2026061309450000017001', 8, 198.00, 'CANCELLED', '孙丽 / 成都', None, '2026-06-13 09:45:00', None, None, None),
    (18, 'O2026061611300000018001', 9, 68.00, 'SHIPPED', '周宇 / 武汉', 'YT202606160008', '2026-06-16 11:30:00', '2026-06-16 11:31:00', '2026-06-17 09:00:00', None),
    (19, 'O2026062718000000019001', 1, 99.00, 'PENDING_PAY', '张三 / 北京', None, '2026-06-27 18:00:00', None, None, None),
]

ORDER_ITEMS = [
    # Each tuple: (order_id, book_id, title_snapshot, price_snapshot, qty)
    (1, 1, 'Java核心技术 卷I（原书第12版）', 149.00, 1),
    (2, 10, '代码大全（第2版）', 128.00, 1),
    (3, 5, '三体（全三册）', 168.00, 1), (3, 48, '小王子', 32.00, 1),
    (4, 5, '三体（全三册）', 168.00, 1),
    (5, 8, '考研数学复习全书', 89.00, 1),
    (6, 6, '时间简史', 45.00, 1),
    (7, 2, '深入理解Java虚拟机（第3版）', 129.00, 1), (7, 4, '活着', 45.00, 1), (7, 6, '时间简史', 45.00, 1),
    (8, 7, '新概念英语（2）', 38.00, 1), (8, 19, '围城', 39.00, 1),
    (9, 1, 'Java核心技术 卷I（原书第12版）', 149.00, 1),
    (10, 4, '活着', 45.00, 1), (10, 48, '小王子', 32.00, 1),
    (11, 10, '代码大全（第2版）', 128.00, 1),
    (12, 11, '重构（第2版）', 99.00, 1),
    (13, 8, '考研数学复习全书', 89.00, 1),
    (14, 5, '三体（全三册）', 168.00, 1),
    (15, 6, '时间简史', 45.00, 1),
    (16, 10, '代码大全（第2版）', 128.00, 1),
    (17, 29, '牛津高阶英汉双解词典（第10版）', 198.00, 1),
    (18, 24, '自私的基因', 68.00, 1),
    (19, 11, '重构（第2版）', 99.00, 1),
]

CART_ITEMS = [
    (5, 11, 1), (5, 3, 2),
    (6, 4, 1),
    (7, 7, 3),
    (1, 12, 1),
]


def fmt_book(row):
    return f" ({row[0]}, '{row[1]}', '{row[2]}', '{row[3]}', '{row[4]}', {row[5]:.2f}, {row[6]}, {row[7]}, NULL, '{row[9]}', '{row[10]}')"


def fmt_user(row):
    phone = 'NULL' if row[6] is None else f"'{row[6]}'"
    return f" ({row[0]}, '{row[1]}', '{row[2]}', '{row[3]}', '{row[4]}', '{row[5]}', {phone}, NULL, '{row[8]}', '{row[9]}', NOW())"


def fmt_order(row):
    def ts(v):
        return 'NULL' if v is None else f"'{v}'"
    tracking = 'NULL' if row[6] is None else f"'{row[6]}'"
    return f" ({row[0]}, '{row[1]}', {row[2]}, {row[3]:.2f}, '{row[4]}', '{row[5]}', {tracking}, {ts(row[7])}, {ts(row[8])}, {ts(row[9])}, {ts(row[10])})"


def main():
    lines = [HEADER]

    lines.append("-- Categories (flat) ---------------------------------------------------------")
    lines.append("INSERT INTO category (id, name, parent_id) VALUES")
    lines.append(",\n".join(f" ({c[0]}, '{c[1]}', {('NULL' if c[2] is None else c[2])})" for c in CATEGORIES) + ";")
    lines.append("")

    lines.append("-- Books (status ON = on sale; OFF rows prove the user-facing filter) --------")
    lines.append("INSERT INTO book (id, title, author, publisher, isbn, price, stock, category_id, cover_path, intro, status) VALUES")
    lines.append(",\n".join(fmt_book(b) for b in BOOKS) + ";")
    lines.append("")

    lines.append("-- Accounts: one per role plus demo customers. Dev login password = Bookstore@123.")
    lines.append("-- password_sm3 = SM3(salt + password); phone_enc = SM4(phone).")
    lines.append("INSERT INTO `user`")
    lines.append(" (id, username, password_sm3, salt, real_name, gender, phone_enc, address_enc, role, status, pwd_changed_at) VALUES")
    lines.append(",\n".join(fmt_user(u) for u in USERS) + ";")
    lines.append("")

    lines.append("-- Orders across all states (snapshot receiver + tracking) -------------------")
    lines.append("INSERT INTO orders (id, order_no, user_id, total, status, receiver_snapshot, tracking_no, created_at, paid_at, shipped_at, completed_at) VALUES")
    lines.append(",\n".join(fmt_order(o) for o in ORDERS) + ";")
    lines.append("")

    lines.append("-- Order items (price snapshots match order totals) --------------------------")
    lines.append("INSERT INTO order_item (order_id, book_id, title_snapshot, price_snapshot, qty) VALUES")
    lines.append(",\n".join(f" ({i[0]}, {i[1]}, '{i[2]}', {i[3]:.2f}, {i[4]})" for i in ORDER_ITEMS) + ";")
    lines.append("")

    lines.append("-- Sample carts (non-empty) --------------------------------------------------")
    lines.append("INSERT INTO cart_item (user_id, book_id, qty) VALUES")
    lines.append(",\n".join(f" ({c[0]}, {c[1]}, {c[2]})" for c in CART_ITEMS) + ";")
    lines.append("")

    SEED_PATH.write_text("\n".join(lines), encoding="utf-8")
    print(SEED_PATH)


if __name__ == "__main__":
    main()
