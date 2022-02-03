/**
 * 
 */
package com.amazinggaming.wallet.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.amazinggaming.wallet.domain.Player;
import com.amazinggaming.wallet.domain.Profile;
import com.amazinggaming.wallet.domain.Transaction;
import com.amazinggaming.wallet.domain.TransactionType;
import com.amazinggaming.wallet.domain.Wallet;
import com.amazinggaming.wallet.domain.WalletStatus;
import com.amazinggaming.wallet.domain.WalletType;

/**
 * @author dereshharry
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TransactionRepositoryTest {
	
	private static final String EMAIL = "transaction08@gmail.com";
	private static final String WALLET_NUMBER = "800000101";
	private static final String TRANS_TYPE_CODE = "CGPAY";
	
	
	@Autowired
	private PlayerRepository playerRepository;
	
	@Autowired
	private WalletRepository walletRepository;
	
	@Autowired
	private TransactionTypeRepository transactionTypeRepository;
	
	@Autowired
	private TransactionRepository transactionRepository;
	
	
	@Test
	@Order(1)
	public void should_store_player_for_test() {
		Player player = new Player();
		player.setAddress("Hal Gharhur");
		player.setEmail(EMAIL);
		player.setIdNumber("0129129A");
		player.setLastName("John");
		player.setName("DOe");
		player.setUsername("John");
		player.setProfile(Profile.PLAYER);
		player.setPassword("12345Kebab!");
		playerRepository.save(player);
		assertThat(player.getId()).isNotNull();
	}
	
	@Test
	@Order(2)
	void should_store_transaction_type_for_test() {
		TransactionType transactionType = new TransactionType();
		transactionType.setCode(TRANS_TYPE_CODE);
		transactionType.setName("Google Pay");
		transactionType.setDescription("Deposit Google Pay");
		transactionType.setType(TransactionType.CREDIT);
		transactionTypeRepository.save(transactionType);
	}
	
	
	@Test
	@Order(3)
	public void should_store_wallet_for_test() {
		Wallet wallet = new Wallet();
		wallet.setBalance(BigDecimal.ZERO);
		wallet.setWalletNumber(WALLET_NUMBER);
		wallet.setWalletStatus(WalletStatus.ACTIVE);
		wallet.setWalletType(WalletType.PLAYER_WALLET);
		Player player = playerRepository.findByEmail(EMAIL);
		wallet.setUser(player);
		walletRepository.save(wallet);
		assertThat(wallet.getId()).isNotNull();
	}
	
	@Test
	@Order(4)
	public void should_find_no_transaction_if_repository_is_empty() {
		List<Transaction> transactions = transactionRepository.findAll();
		assertThat(transactions).isEmpty();
	}
	
	@Test
	@Order(5)
	public void should_store_transaction() {
		Transaction transaction = new Transaction();
		transaction.setAmount(BigDecimal.TEN);
		TransactionType transactionType = transactionTypeRepository.findByCode(TRANS_TYPE_CODE);
		transaction.setTransactionType(transactionType);
		transaction.setDescription(transactionType.getDescription());
		Wallet wallet = walletRepository.findByWalletNumber(WALLET_NUMBER);
		transaction.setWallet(wallet);
		transactionRepository.save(transaction);
		assertThat(transaction.getId()).isNotNull();
	}
	
	@Test
	@Order(6)
	public void should_find_all_transaction() {
		List<Transaction> transactions = transactionRepository.findAll();
		assertThat(transactions).isNotEmpty();
	}
	
	@Test
	@Order(7)
	public void should_find_all_transaction_in_wallet() {
		Wallet wallet = walletRepository.findByWalletNumber(WALLET_NUMBER);
		List<Transaction> transactions = transactionRepository.findByWalletIn(Arrays.asList(wallet));
		assertThat(transactions).isNotEmpty();
	}

}
