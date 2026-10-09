package com.crypto.binancearb.ingestor.store;

import com.crypto.binancearb.ingestor.model.MarketTicker;
import lombok.AllArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class OrderBookCache {

    private final CacheManager cache;

    private Cache getCache() {
        return cache.getCache("tickers");
    }

    public void update(MarketTicker ticker) {
        getCache().put(ticker.symbol(), ticker);
    }
}
