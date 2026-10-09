package com.crypto.binancearb.ingestor.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record MarketTicker(
        @JsonProperty("u") long updateId,
        @JsonProperty("s")String symbol,
        @JsonProperty("b")BigDecimal bidPrice,
        @JsonProperty("B")BigDecimal bidQty,
        @JsonProperty("a")BigDecimal askPrice,
        @JsonProperty("A")BigDecimal askQty
) {
}
