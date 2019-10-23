package part02;

import lombok.Getter;
import lombok.Setter;

import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static part02.Constants.*;

@Getter
@Setter
public class Wallet {
    private PrivateKey privateKey;
    private PublicKey publicKey;

    //private Map<String, TransOutput> transOutputs = new HashMap<>();

    public Wallet() {
        generateKeyPair();
    }

    private void generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ECDSA, BC);
            SecureRandom random = SecureRandom.getInstance(SHA1PRNG);
            ECGenParameterSpec ecSpec = new ECGenParameterSpec(PRIME192V1_STANDARD_NAME);

            keyGen.initialize(ecSpec, random);
            KeyPair keyPair = keyGen.generateKeyPair();

            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
/*
    public void putTransOutput(String id, TransOutput transOutput) {
        transOutputs.put(id, transOutput);
    }

    public Collection<TransOutput> getTransOutputs() {
        return transOutputs.values();
    }

    public void removeTransOutputsByTransInputs(List<TransInput> inputs) {
        inputs.stream()
            .map(TransInput::getTransOutputId)
            .filter(transOutputs::containsKey)
            .forEach(transOutputs::remove);
    }*/
}