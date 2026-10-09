package com.crypto.binancearb.ingestor.client;

import com.crypto.binancearb.ingestor.dto.SubscribeRequest;
import com.crypto.binancearb.ingestor.model.MarketTicker;
import com.crypto.binancearb.ingestor.store.OrderBookCache;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.concurrent.TimeUnit;


@RequiredArgsConstructor
@Component
public class MarketDataWebSocketClient {

    private final String BINANCE_WS_URL = "wss://stream.binance.com:9443/ws";
    private final ObjectMapper mapper;
    private OkHttpClient client;
    private WebSocket webSocket;
    private final OrderBookCache cache;

    @PostConstruct
    public void init() {

        this.client = new OkHttpClient.Builder().
                readTimeout(0, TimeUnit.MILLISECONDS)
                .pingInterval(3, TimeUnit.SECONDS)
                .build();

        connect();
    }

    private void connect() {
        Request request = new Request.Builder()
                .url(BINANCE_WS_URL)
                .build();

        BinanceListener listener = new BinanceListener();
        this.webSocket = client.newWebSocket(request, listener);
    }

    @PreDestroy
    public void cleanup() {

        if (webSocket != null) {
            webSocket.close(1000, "Spring desativado");
        }

        if (client != null) {
            client.dispatcher().executorService().shutdown();
        }
    }

    private class BinanceListener extends WebSocketListener {

        @Override
        public void onOpen(WebSocket webSocket, Response response) {

            System.out.println("conectado com sucesso");

            var request = new SubscribeRequest(
                    "SUBSCRIBE",
                    List.of(
                            "btcusdt@bookTicker",
                            "avaxusdt@bookTicker",
                            "avaxbtc@bookTicker"
                    ), 1
            );

            try {
                String json = mapper.writeValueAsString(request);
                webSocket.send(json);
                System.out.println("Subscribe enviado " + json);
            }catch (Exception e){
                throw new RuntimeException("Erro ao serializar subscribe request", e);
            }
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {

            try {
                if (text.contains("\"result\":null")) {
                    return;
                }

                var ticker = mapper.readValue(text, MarketTicker.class);
                cache.update(ticker);

            } catch (Exception e) {
                System.err.println("Erro ao processar mensagem: " + text);
                e.printStackTrace();
            }
            System.out.println("[Binance Dados] " + text);
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            System.out.println("Fechando conexao com o servidor " + code + " " + reason);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {

            System.out.println("Tentando reconectar");
        }
    }
}
