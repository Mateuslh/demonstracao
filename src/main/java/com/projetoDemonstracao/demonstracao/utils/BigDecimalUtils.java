package com.projetoDemonstracao.demonstracao.utils;

import java.math.BigDecimal;

public class BigDecimalUtils {
    public static BigDecimal nullToZero(BigDecimal value) {
        return value != null ? value : BigDecimal.ZERO;
    }
}