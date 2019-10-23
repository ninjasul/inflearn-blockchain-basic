package part02;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static part02.Constants.DEFAULT_DIFFICULTY;
import static part02.Constants.MIN_TRANSACTION_UNIT;

public class BlockChain {
    private static List<Block> blockChain = new ArrayList<>();
    private static Map<String, TransOutput> transOutputs = new HashMap<>();

    public static Transaction createInitialTransaction(Wallet sender, Wallet recipient, float value) {
        Transaction transaction = new Transaction(sender, recipient, value);
        TransOutput output = new TransOutput(transaction.getRecipient(), transaction.getValue(), transaction.getTransId());
        transaction.addOutput(output);

        transOutputs.put(transaction.getTransId(), output);
        return transaction;
    }

    public static void addBlock(Block block) {
        block.mineBlock(DEFAULT_DIFFICULTY);
        blockChain.add(block);
    }

    public static TransOutput getTransOutput(String id) {
        return transOutputs.get(id);
    }

    public static void putTransOutput(String id, TransOutput transOutput) {
        transOutputs.put(id, transOutput);
    }

    public static void removeTransOutput(String id) {
        transOutputs.remove(id);
    }

    // 잔고 확인 기능
    public static float getBalance(Wallet wallet) {
        return (float)getMinedTransOutput(wallet.getPublicKey())
                .stream()
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
        return getMinedTransOutput(sender.getPublicKey())
                .stream()
                .map(BlockChain::createNewTransInput)
                .collect(Collectors.toList());
    }

    private static List<TransOutput> getMinedTransOutput(PublicKey publicKey) {
        return transOutputs.values()
                            .stream()
                            .filter(output -> output.mined(publicKey))
                            .collect(Collectors.toList());
    }

    private static TransInput createNewTransInput(TransOutput output) {
        return new TransInput(output.getId(), transOutputs.get(output.getId()));
    }
}