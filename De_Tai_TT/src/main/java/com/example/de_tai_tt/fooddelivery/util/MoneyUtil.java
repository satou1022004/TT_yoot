package com.example.de_tai_tt.fooddelivery.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class MoneyUtil {

    private MoneyUtil() {
    }

    public static BigDecimal money(BigDecimal amount) {
        return amount.setScale(2, RoundingMode.HALF_UP);
    }
}
