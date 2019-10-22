package part01;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Block {
    private String hash;
    private String prevHash;
    private String data;
    private long timeStamp;
    private int nonce;
    private int difficulty;

    public Block(String data, String prevHash) {
        this.data = data;
        this.prevHash = prevHash;
        this.timeStamp = System.currentTimeMillis();
        this.hash = calculateHash();
    }

    public String calculateHash() {
        return BlockUtil.applySha256(prevHash + data + String.valueOf(nonce) + String.valueOf(timeStamp));
    }

    public void mineBlock(int difficulty) {
        String target = BlockUtil.getDifficultyString(difficulty);

        while (!hash.substring(0, difficulty).equals(target)) {
            nonce++;
            hash = calculateHash();
        }
    }
}