package com.crypto.binancearb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class BinanceArbitrageApplication {

    public static void main(String[] args) {
        SpringApplication.run(BinanceArbitrageApplication.class, args);
    }

}
