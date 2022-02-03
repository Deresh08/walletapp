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

import com.amazinggaming.wallet.domain.Bet;
import com.amazinggaming.wallet.domain.BetStatus;
import com.amazinggaming.wallet.domain.Player;
import com.amazinggaming.wallet.domain.Profile;

/**
 * @author dereshharry
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BetRepositoryTest {
	
	
	private static final String EMAIL = "player08@gmail.com";
	
	@Autowired
	private BetRepository betRepository;
	
	@Autowired
	private PlayerRepository playerRepository;

	
	
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
	public void should_find_no_bets_if_repository_is_empty() {
		List<Bet> bets = betRepository.findAll();
		assertThat(bets).isEmpty();
	}
	
	@Test
	@Order(3)
	public void should_store_bet() {
		Player player = playerRepository.findByEmail(EMAIL);
		Bet bet = new Bet();
		bet.setBetStatus(BetStatus.PENDING);
		bet.setBonusAmount(BigDecimal.TEN);
		bet.setCashAmount(BigDecimal.ONE);
		bet.setCreatedBy(player);
		bet.setEvent("Liverpool vs Man Utd");
//		bet.setOdds(3);
		betRepository.save(bet);
		assertThat(bet.getId()).isNotNull();
	}
	
	@Test
	@Order(4)
	public void should_find_all_bets() {
		List<Bet> bets = betRepository.findAll();
		assertThat(bets).isNotEmpty();
	}
	
	@Test
	@Order(5)
	public void should_find_bets_by_user() {
		Player player = playerRepository.findByEmail(EMAIL);
		List<Bet> bets = betRepository.findBycreatedBy(player);
		assertThat(bets).isNotEmpty();
	}
	
	
	
	
	
	

}
