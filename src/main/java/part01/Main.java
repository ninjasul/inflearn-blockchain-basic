package part01;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static List<Block> blockChain = new ArrayList<>();
    public static int difficulty = 2;

    public static void main(String[] args) {
        Block block = new Block("1", "0");

        addBlock(block);
        addBlock(new Block("2", blockChain.get(blockChain.size()-1).getHash()));
        addBlock(new Block("3", blockChain.get(blockChain.size()-1).getHash()));
        addBlock(new Block("4", blockChain.get(blockChain.size()-1).getHash()));

        System.out.println(BlockUtil.getJson(blockChain));
    }

    private static void addBlock(Block block) {
        block.mineBlock(difficulty);
        blockChain.add(block);
    }
}