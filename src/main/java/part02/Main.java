package part02;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;

public class Main {

    public static void main(String[] args) {
        // 해당 서비스 프로바이를 추가하지 않으면 KeyPairGenerator 의 인스턴스를 가져올 수 없음.
        Security.addProvider(new BouncyCastleProvider());

        // 지갑 생성 - 지갑은 PublicKey(계좌번호), PrivateKey(계좌비밀번호) 로 구성되어 있음.
        Wallet coinBase = new Wallet();
        Wallet walletA = new Wallet();
        Wallet walletB = new Wallet();

        // 제너시스 트랜잭션 생성
        Transaction genTransaction = BlockChain.createInitialTransaction(coinBase, walletA, 100f);

        // 제너시스 블록 생성
        Block genesisBlock = new Block("0");
        genesisBlock.addTransaction(genTransaction);
        BlockChain.addBlock(genesisBlock);
        System.out.println();

        // 다음 블록(Block1) 생성
        Block block1 = new Block(genesisBlock.getHash());
        System.out.println("1. walletA.getBalance(): " + BlockChain.getBalance(walletA));
        System.out.println("2. block1 add 전 " + BlockChain.getBalance(walletA));

        block1.addTransaction(BlockChain.sendFunds(walletA, walletB, 40f));
        System.out.println("3. walletA.getBalance(): " + BlockChain.getBalance(walletA));
        System.out.println("4. walletB.getBalance(): " + BlockChain.getBalance(walletB));

        Wallet walletC = new Wallet();
        block1.addTransaction(BlockChain.sendFunds(walletB, walletC, 20f));
        BlockChain.addBlock(block1);
        System.out.println("5. walletA.getBalance(): " + BlockChain.getBalance(walletA));
        System.out.println("6. walletB.getBalance(): " + BlockChain.getBalance(walletB));
        System.out.println("7. walletC.getBalance(): " + BlockChain.getBalance(walletC));
    }
}