package com.bookstore.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MaskUtilTest {

    @Test
    void masksPhoneByKeepingFirstThreeAndLastFourDigits() {
        assertEquals("138****1234", MaskUtil.mask("phone", "13800001234"));
    }

    @Test
    void masksTwoCharacterChineseNameByKeepingOnlyFamilyName() {
        assertEquals("张*", MaskUtil.mask("name", "张三"));
    }

    @Test
    void masksLongerChineseNameByKeepingFirstAndLastCharacter() {
        assertEquals("张*丰", MaskUtil.mask("name", "张三丰"));
    }

    @Test
    void masksIdCardByKeepingFirstSixAndLastFourCharacters() {
        assertEquals("110101********1234", MaskUtil.mask("idcard", "110101199001011234"));
    }

    @Test
    void masksUnknownTypeWithGenericFallback() {
        assertEquals("se****ve", MaskUtil.mask("unknown", "sensitive"));
    }

    @Test
    void blankAndNullValuesRenderAsEmptyText() {
        assertEquals("", MaskUtil.mask("phone", null));
        assertEquals("", MaskUtil.mask("name", "   "));
    }
}
