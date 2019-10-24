package part02;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransInput {
    private String transOutputId;
    private TransOutput transOutput;

    public TransInput(String transOutputId) {
        this.transOutputId = transOutputId;
    }

    public float getOutputValue() {
        return transOutput.getValue();
    }
}