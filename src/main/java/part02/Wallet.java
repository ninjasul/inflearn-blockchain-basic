package part02;

import lombok.Getter;
import lombok.Setter;

import java.security.*;
import java.security.spec.ECGenParameterSpec;

import static part02.Constants.*;

@Getter
@Setter
public class Wallet {
    private PrivateKey privateKey;
    private PublicKey publicKey;

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
}