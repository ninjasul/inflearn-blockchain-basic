package part02;

import lombok.Getter;
import lombok.Setter;

import java.security.PublicKey;
import java.util.Optional;

@Getter
@Setter
public class TransOutput {
    private String id;
    private PublicKey recipient;
    private float value;
    private String parentTransId;

    public TransOutput(PublicKey recipient, float value, String parentTransId) {
        this.recipient = recipient;
        this.value = value;
        this.parentTransId = parentTransId;
        this.id = generateId(recipient, value, parentTransId);
    }

    private String generateId(PublicKey recipient, float value, String parentTransId) {
        return BlockUtil.applySha256(BlockUtil.getStringFromKey(recipient) + value + parentTransId);
    }

    public boolean mined(PublicKey publicKey) {
        return Optional.ofNullable(publicKey)
                .filter(key -> key.equals(recipient))
                .isPresent();
    }
}