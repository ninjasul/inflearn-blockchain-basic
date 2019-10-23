package part02;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransInput {
    private String transOutputId;
    private TransOutput transOutput;

    public TransInput(String transOutputId, TransOutput transOutput) {
        this.transOutputId = transOutputId;
        this.transOutput = transOutput;
    }

    public float getOutputValue() {
        return transOutput.getValue();
    }
}