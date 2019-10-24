package part02;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static part02.Constants.DEFAULT_DIFFICULTY;
import static part02.Constants.MIN_TRANSACTION_UNIT;

public class BlockChain {
    private static List<Block> blockChain = new ArrayList<>();
    private static Map<String, TransOutput> unspentTransOutputs = new HashMap<>();

    public static Transaction createInitialTransaction(Wallet sender, Wallet recipient, float value) {
        Transaction transaction = new Transaction(sender, recipient, value);
        TransOutput output = new TransOutput(transaction.getRecipient(), transaction.getValue(), transaction.getTransId());
        transaction.addOutput(output);

        putUnspentTransOutput(transaction.getTransId(), output);
        return transaction;
    }

    public static void addBlock(Block block) {
        block.mineBlock(DEFAULT_DIFFICULTY);
        blockChain.add(block);
    }

    public static TransOutput getUnspentTransOutput(String id) {
        return unspentTransOutputs.get(id);
    }

    public static void putUnspentTransOutput(String id, TransOutput unspentTransOutput) {
        unspentTransOutputs.put(id, unspentTransOutput);
    }

    public static void removeUnspentTransOutput(String id) {
        unspentTransOutputs.remove(id);
    }

    // 잔고 확인 기능
    public static float getBalance(Wallet wallet) {
        return (float) unspentTransOutputs.values()
                .stream()
                .filter(isMinedTransOutput(wallet.getPublicKey()))
                .mapToDouble(TransOutput::getValue)
                .sum();
    }

    // 송신기능
    public static Transaction sendFunds(Wallet sender, Wallet recipient, float value) {
        if (!canSend(sender, value)) {
            System.out.println("Not Enough~~!");
            return null;
        }

        return new Transaction(sender, recipient, value, generateTransInputs(sender));
    }

    private static boolean canSend(Wallet sender, float value) {
        return value > MIN_TRANSACTION_UNIT && getBalance(sender) >= value;
    }

    private static List<TransInput> generateTransInputs(Wallet sender) {
        return unspentTransOutputs.values()
                .stream()
                .filter(isMinedTransOutput(sender.getPublicKey()))
                .map(BlockChain::createNewTransInput)
                .collect(Collectors.toList());
    }

    private static Predicate<TransOutput> isMinedTransOutput(PublicKey publicKey) {
        return output -> output.mined(publicKey);
    }

    private static TransInput createNewTransInput(TransOutput output) {
        return new TransInput(output.getId());
    }
}