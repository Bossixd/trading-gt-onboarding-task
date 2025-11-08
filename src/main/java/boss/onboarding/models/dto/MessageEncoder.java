package boss.onboarding.models.dto;

import jakarta.websocket.*;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MessageEncoder implements Encoder.Text<Message> {
    private static ObjectMapper mapper = new ObjectMapper();

    @Override
    public String encode(Message message) throws EncodeException {
        try {
            return mapper.writeValueAsString(message);
        } catch (Exception e) {
            throw new EncodeException(message, "Encoding failed", e);
        }
    }

    @Override public void init(EndpointConfig config) {}
    @Override public void destroy() {}
}