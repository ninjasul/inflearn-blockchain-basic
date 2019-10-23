package part02;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;

public class Main {

    public static void main(String[] args) {
        Security.addProvider(new BouncyCastleProvider());

        // 1. 지갑 생성
        Wallet walletA = new Wallet();
        Wallet walletB = new Wallet();
        Wallet coinBase = new Wallet();

        // 2. 제너시스 트랜잭션 생성
        Transaction genTransaction = BlockChain.createInitialTransaction(coinBase, walletA, 100f);

        // 4. genesis 생성
        Block genesisBlock = new Block("0");
        genesisBlock.addTransaction(genTransaction);
        BlockChain.addBlock(genesisBlock);
        System.out.println();

        // 5.다음 블록(Block1) 생성
        Block block1 = new Block(genesisBlock.getHash());
        System.out.println("1. walletA.getBalance(): " + BlockChain.getBalance(walletA));
        System.out.println("2. block1 add 전 " + BlockChain.getBalance(walletA));

        block1.addTransaction(BlockChain.sendFunds(walletA, walletB, 40f));
        BlockChain.addBlock(block1);
        System.out.println("3. walletA.getBalance(): " + BlockChain.getBalance(walletA));
        System.out.println("4. walletB.getBalance(): " + BlockChain.getBalance(walletB));
    }
}