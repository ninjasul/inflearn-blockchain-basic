package part02;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.security.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import static part02.Constants.*;

public class BlockUtil {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    //Applies Sha256 to a string and returns the result.
    public static String applySha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance(SHA_256);

            byte[] hash = digest.digest(input.getBytes(UTF_8));
            StringBuffer hexString = new StringBuffer(); // This will contain hash as hexidecimal

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);

                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //Applies ECDSA Signature and returns the result ( as bytes ).
    public static byte[] applyECDSASig(PrivateKey privateKey, String input) {
        Signature dsa;
        byte[] output = new byte[0];
        try {
            dsa = Signature.getInstance(ECDSA, BC);
            dsa.initSign(privateKey);
            byte[] strByte = input.getBytes();
            dsa.update(strByte);
            byte[] realSig = dsa.sign();
            output = realSig;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return output;
    }

    //Verifies a String signature
    public static boolean verifyECDSASig(PublicKey publicKey, String data, byte[] signature) {
        try {
            Signature ecdsaVerify = Signature.getInstance(ECDSA, BC);
            ecdsaVerify.initVerify(publicKey);
            ecdsaVerify.update(data.getBytes());
            return ecdsaVerify.verify(signature);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //Short hand helper to turn Object into a json string
    public static String getJson(Object o) {
        return gson.toJson(o);
    }

    //Returns difficulty string target, to compare to hash. eg difficulty of 5 will return "00000"
    public static String getDifficultyString(int difficulty) {
        return new String(new char[difficulty]).replace('\0', '0');
    }

    public static String getStringFromKey(Key key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    public static String getMerkleRoot(List<Transaction> transactions) {
        List<String> prevTreeLayer = getTransIds(transactions);

        List<String> treeLayer = prevTreeLayer;

        int count = transactions.size();

        while (count > 1) {
            treeLayer = new ArrayList<>();

            for (int i = 1; i < prevTreeLayer.size(); i += 2) {
                treeLayer.add(applySha256(prevTreeLayer.get(i - 1) + prevTreeLayer.get(i)));
            }
            count = treeLayer.size();
            prevTreeLayer = treeLayer;
        }

        return (treeLayer.size() == 1) ? treeLayer.get(0) : "";
    }

    private static List<String> getTransIds(List<Transaction> transactions) {
        return transactions.stream()
                .map(Transaction::getTransId)
                .collect(Collectors.toList());
    }
}