package com.crypto.binancearb.client.dto;

import java.math.BigDecimal;

public record MarketTicker(
        long updateId,
        String symbol,
        BigDecimal bidPrice,
        BigDecimal bidQty,
        BigDecimal askPrice,
        BigDecimal askQty
) {
}
