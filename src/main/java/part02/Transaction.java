package part02;

import lombok.Getter;
import lombok.Setter;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class Transaction {
    private static int sequence = 0;
    private String transId;
    private PublicKey sender;
    private PublicKey recipient;
    private float value;
    private byte[] signature;

    private List<TransInput> inputs;

    private List<TransOutput> outputs = new ArrayList<>();

    public Transaction(PublicKey sender, PrivateKey privateKey, PublicKey recipient, float value, List<TransInput> inputs) {
        this.sender = sender;
        this.recipient = recipient;
        this.value = value;
        this.signature = generateSignature(privateKey);
        this.transId = calculateHash();
        this.inputs = inputs;
    }

    public Transaction(Wallet sender, Wallet recipient, float value) {
        this(sender.getPublicKey(), sender.getPrivateKey(), recipient.getPublicKey(), value, new ArrayList<>());
    }

    public Transaction(Wallet sender, Wallet recipient, float value, List<TransInput> inputs) {
        this(sender.getPublicKey(), sender.getPrivateKey(), recipient.getPublicKey(), value, inputs);
    }

    public boolean proceed() {

        if (!canProceed()) {
            return false;
        }

        float leftOver = getInputValueSum() - value;

        // 40코인 송신
        addOutput(new TransOutput(recipient, value, transId));

        // 나머지 60
        addOutput(new TransOutput(sender, leftOver, transId));

        for (TransOutput output : outputs) {
            BlockChain.putUnspentTransOutput(output.getId(), output);
        }

        for (TransInput input : inputs) {
            if (input.getTransOutput() == null) {
                continue;
            }

            BlockChain.removeUnspentTransOutput(input.getTransOutput().getId());
        }

        return true;
    }

    private boolean canProceed() {
        // 1. verifySignature()
        if (!verifySignature()) {
            return false;
        }

        return true;
    }

    private String calculateHash() {
        sequence++;
        return BlockUtil.applySha256(
                BlockUtil.getStringFromKey(sender) +
                        BlockUtil.getStringFromKey(recipient) +
                        value + sequence);
    }

    private float getInputValueSum() {
        return (float)inputs.stream()
                .filter(input -> !Objects.isNull(input.getTransOutput()))
                .mapToDouble(TransInput::getOutputValue)
                .sum();
    }

    private boolean verifySignature() {
        String data = BlockUtil.getStringFromKey(sender) + BlockUtil.getStringFromKey(recipient) + value;
        return BlockUtil.verifyECDSASig(sender, data, signature);
    }

    public byte[] generateSignature(PrivateKey privateKey) {
        String data = BlockUtil.getStringFromKey(sender) + BlockUtil.getStringFromKey(recipient) + value;
        return BlockUtil.applyECDSASig(privateKey, data);
    }

    public void addOutput(TransOutput output) {
        outputs.add(output);
    }
}