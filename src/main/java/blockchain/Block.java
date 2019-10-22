package blockchain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Block {
    private int value;
    private Block next;

    public Block(int value) {
        this.value = value;
    }

    public void mineBlock(int difficulty) {

    }
}