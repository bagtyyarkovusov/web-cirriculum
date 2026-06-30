package com.bookstore.security;

import java.util.Locale;

public final class MaskUtil {

    private static final String FIXED_MASK = "****";

    private MaskUtil() {
    }

    public static String mask(String type, Object value) {
        String text = normalize(value);
        if (text.isEmpty()) {
            return "";
        }

        String normalizedType = type == null ? "" : type.trim().toLowerCase(Locale.ROOT);
        return switch (normalizedType) {
            case "phone", "mobile", "tel" -> maskPhone(text);
            case "name" -> maskName(text);
            case "idcard", "id_card", "identity" -> maskIdCard(text);
            default -> maskGeneric(text);
        };
    }

    public static String maskPhone(String value) {
        String text = normalize(value);
        if (codePointCount(text) < 7) {
            return maskGeneric(text);
        }
        return first(text, 3) + FIXED_MASK + last(text, 4);
    }

    public static String maskName(String value) {
        String text = normalize(value);
        int length = codePointCount(text);
        if (length == 0) {
            return "";
        }
        if (length == 1) {
            return "*";
        }
        if (length == 2) {
            return first(text, 1) + "*";
        }
        return first(text, 1) + "*" + last(text, 1);
    }

    public static String maskIdCard(String value) {
        String text = normalize(value);
        int length = codePointCount(text);
        if (length <= 10) {
            return maskGeneric(text);
        }
        return first(text, 6) + repeat("*", length - 10) + last(text, 4);
    }

    private static String maskGeneric(String text) {
        int length = codePointCount(text);
        if (length == 0) {
            return "";
        }
        if (length == 1) {
            return "*";
        }
        if (length == 2) {
            return first(text, 1) + "*";
        }
        if (length <= 6) {
            return first(text, 1) + repeat("*", length - 2) + last(text, 1);
        }
        return first(text, 2) + FIXED_MASK + last(text, 2);
    }

    private static String normalize(Object value) {
        return value == null ? "" : value.toString().trim();
    }

    private static int codePointCount(String text) {
        return text.codePointCount(0, text.length());
    }

    private static String first(String text, int count) {
        int end = text.offsetByCodePoints(0, Math.min(count, codePointCount(text)));
        return text.substring(0, end);
    }

    private static String last(String text, int count) {
        int length = codePointCount(text);
        int start = text.offsetByCodePoints(0, Math.max(0, length - count));
        return text.substring(start);
    }

    private static String repeat(String value, int count) {
        return value.repeat(Math.max(0, count));
    }
}
