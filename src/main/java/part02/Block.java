package part02;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Block {
    private String hash;
    private String prevHash;
    private String data;
    private long timeStamp;
    private int nonce;
    private String merkleRoot;

    private List<Transaction> transactions = new ArrayList<>();

    public Block(String prevHash) {
        //this.data = data;
        this.prevHash = prevHash;
        this.timeStamp = System.currentTimeMillis();
        this.hash = calculateHash();
    }

    private String calculateHash() {
        return BlockUtil.applySha256(prevHash + timeStamp + nonce + merkleRoot);
    }

    public void mineBlock(int difficulty) {
        merkleRoot = BlockUtil.getMerkleRoot(transactions);

        // 2이면 앞자리가 00
        String target = BlockUtil.getDifficultyString(difficulty);

        while(!hash.substring(0,difficulty).equals(target)) {
            nonce++;
            hash = calculateHash();
    	  	//System.out.println("target: "+target+" hash: "+hash+" nonce: "+nonce);
        }
        System.out.println("===Block Mined!!! : " + hash);
    }

    public boolean addTransaction(Transaction transaction) {
        if(transaction == null) {
            System.out.println("===Transaction is null");
            return false;
        }

        if(!isEmptyHash(prevHash) && !transaction.proceed()) {
            System.out.println("===Transaction couldn't be added to Block");
            return false;
        }

        transactions.add(transaction);
        System.out.println("===Transaction added to Block");
        return true;
    }

    private boolean isEmptyHash(String hash) {
        return "0".equals(hash);
    }
}