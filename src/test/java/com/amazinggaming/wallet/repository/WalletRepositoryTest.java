/**
 * 
 */
package com.amazinggaming.wallet.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
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
public class WalletRepositoryTest {
	
	
	@Autowired
	private PlayerRepository playerRepository;
	
	@Autowired 
	private WalletRepository walletRepository;
	
	private static final String EMAIL = "playerwallet08@gmail.com";
	private static final String WALLET_NUMBER = "300000101";
	
	@Test
	@Order(1)
	public void should_store_player_for_test() {
		Player player = new Player();
		player.setAddress("Hal Gharhur");
		player.setEmail(EMAIL);
		player.setIdNumber("0129129A");
		player.setLastName("John");
		player.setName("DOe");
		player.setUsername("John08");
		player.setProfile(Profile.PLAYER);
		player.setPassword("12345Kebab!");
		playerRepository.save(player);
		assertThat(player.getId()).isNotNull();
	}
	@Test
	@Order(2)
	public void should_find_no_wallet_if_repository_is_empty() {
		List<Wallet> wallets = walletRepository.findAll();
		assertThat(wallets).isEmpty();
	}
	@Test
	@Order(3)
	public void should_store_wallet() {
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
	public void should_find_all_wallets() {
		List<Wallet> wallets = walletRepository.findAll();
		assertThat(wallets).isNotEmpty();
	}
	@Test
	@Order(5)
	public void should_find_wallets_by_user() {
		Player player = playerRepository.findByEmail(EMAIL);
		List<Wallet> wallets = walletRepository.findByUser(player);	
		assertThat(wallets).isNotEmpty();
	}
	@Test
	@Order(6)
	public void should_find_wallet_by_wallet_number() {
		Wallet wallet = walletRepository.findByWalletNumber(WALLET_NUMBER);
		assertThat(wallet.getWalletNumber()).isEqualTo(WALLET_NUMBER);
	}
	@Test
	@Order(7)
	public void should_find_wallet_by_user_and_product() {
		Player player = playerRepository.findByEmail(EMAIL);
		Wallet wallet = walletRepository.findByUserAndWalletType(player, WalletType.PLAYER_WALLET);
		assertThat(wallet.getUser().getUsername()).isEqualTo(player.getUsername());
		assertThat(wallet.getWalletType()).isEqualTo(WalletType.PLAYER_WALLET);
		
	}
	@Test
	@Order(8)
	public void should_update_wallet_balance() {
		Wallet wallet = walletRepository.findByWalletNumber(WALLET_NUMBER);
		wallet.setBalance(new BigDecimal("200"));
		assertThat(wallet.getBalance()).isEqualTo(new BigDecimal(200));
	}
}
