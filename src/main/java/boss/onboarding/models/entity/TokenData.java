package boss.onboarding.models.entity;

import java.util.UUID;

public class TokenData {
    private double data;
    private String dataId;

    public TokenData(double data) {
        this.data = data;
        this.dataId = UUID.randomUUID().toString();
    }

    public double getData() {
        return data;
    }

    public String getDataId() {
        return dataId;
    }

    public String toString() {
        return "Data{" +
                "data=" + data +
                ", dataId='" + dataId + '\'' +
                '}';
    }
}
