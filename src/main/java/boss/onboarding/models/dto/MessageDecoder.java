package boss.onboarding.models.dto;

import jakarta.websocket.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

public class MessageDecoder implements Decoder.Text<Message> {
    private static ObjectMapper mapper = new ObjectMapper();

    @Override
    public Message decode(String text) throws DecodeException {
        try {
            JsonNode node = mapper.readTree(text);
            
            if (!node.has("value")) {
                throw new DecodeException(text, "Missing 'value' field");
            }
            
            if (!node.get("value").isNumber()) {
                throw new DecodeException(text, "'value' must be a number");
            }
            
            return mapper.treeToValue(node, Message.class);
            
        } catch (Exception e) {
            throw new DecodeException(text, "Invalid number format", e);
        }
    }

    @Override
    public boolean willDecode(String text) {
        try {
            JsonNode node = mapper.readTree(text);
            return node.has("value") && node.get("value").isNumber();
        } catch (Exception e) {
            return false;
        }
    }

    @Override public void init(EndpointConfig config) {}
    @Override public void destroy() {}
}
