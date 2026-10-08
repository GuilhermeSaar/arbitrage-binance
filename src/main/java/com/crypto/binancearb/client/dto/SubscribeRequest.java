package com.crypto.binancearb.client.dto;

import java.util.List;

public record SubscribeRequest(String method, List<String> params, int id) {
}
