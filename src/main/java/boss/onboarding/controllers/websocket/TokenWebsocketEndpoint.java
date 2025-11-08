package boss.onboarding.controllers.websocket;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.springframework.stereotype.Component;

import boss.onboarding.models.dto.Message;
import boss.onboarding.models.dto.MessageDecoder;
import boss.onboarding.models.dto.MessageEncoder;
import boss.onboarding.models.repository.TokenDataRepository;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

@Component
@ServerEndpoint(value="/token/{token}", encoders=MessageEncoder.class, decoders=MessageDecoder.class)
public class TokenWebsocketEndpoint {

    private static Set<TokenWebsocketEndpoint> endpoints = new CopyOnWriteArraySet<>();
    private static HashMap<String, String> tokens = new HashMap<>();
    private static TokenDataRepository data = TokenDataRepository.getDataSingleton();

    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) throws IOException {
        System.out.println("WebSocket CONNECTED - token: " + token + ", Session: " + session.getId());
        
        endpoints.add(this);
        tokens.put(session.getId(), token);

        session.getBasicRemote().sendText(token + " connected!");
    }

    @OnMessage
    public void onMessage(Session session, Message message) throws IOException {
        Double value = message.getValue();
        String token = tokens.get(session.getId());
        data.write(token, value);

        session.getBasicRemote().sendText("Data Received!");
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        endpoints.remove(this);
        String token = tokens.get(session.getId());
        System.out.println("WebSocket CLOSED - Username: " + token);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.out.println("WebSocket ERROR: " + throwable.getMessage());
        throwable.printStackTrace();
    }
}