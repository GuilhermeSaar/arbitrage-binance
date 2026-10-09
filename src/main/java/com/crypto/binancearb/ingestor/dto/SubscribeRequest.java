package com.crypto.binancearb.ingestor.dto;

import java.util.List;

public record SubscribeRequest(String method, List<String> params, int id) {
}
