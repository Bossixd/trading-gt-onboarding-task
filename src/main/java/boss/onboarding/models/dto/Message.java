package boss.onboarding.models.dto;

public class Message {
    private Double value;

    public Message() {}

    public Message(Double value) {
        this.value = value;
    }

    public Double getValue() { return value; }
    public void setValue(Double value) { this.value = value; }
}