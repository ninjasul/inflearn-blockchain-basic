package blockchain;

public class BlockChainTest {
    public static void main(String[] args) {
        Block block = new Block(1);
        block.setNext(new Block(2));
        block.getNext().setNext(new Block(3));
        block.getNext().getNext().setNext(new Block(4));

        printBlocks(block);
    }

    private static void printBlocks(Block block) {
        while(block != null) {
            System.out.println(block.getValue());
            block = block.getNext();
        }
    }
}