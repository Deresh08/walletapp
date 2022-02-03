/**
 * 
 */
package com.amazinggaming.wallet.repository;

import static org.assertj.core.api.Assertions.assertThat;

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

/**
 * @author dereshharry
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PlayerRepositoryTest {

	private static final String EMAIL = "player08@gmail.com";
	private static final String USERNAME = "NotJohn08";

	@Autowired
	private PlayerRepository playerRepository;

	@Test
	@Order(1)
	public void should_find_no_players_if_repository_is_empty() {
		List<Player> players = playerRepository.findAll();
		assertThat(players).isEmpty();
	}

	@Test
	@Order(2)
	public void should_store_player() {
		Player player = new Player();
		player.setAddress("Hal Gharhur");
		player.setEmail(EMAIL);
		player.setIdNumber("0129129A");
		player.setLastName("John");
		player.setName("DOe");
		player.setUsername(USERNAME);
		player.setProfile(Profile.PLAYER);
		player.setPassword("12345Kebab!");
		playerRepository.save(player);
		assertThat(player.getId()).isNotNull();
	}

	@Test
	@Order(3)
	public void should_find_all_players() {
		List<Player> players = playerRepository.findAll();
		assertThat(players).isNotEmpty();
	}

	@Test
	@Order(4)
	public void should_find_player_by_username() {
		Player player = playerRepository.findByUsername(USERNAME);
		assertThat(player.getUsername()).isEqualTo(USERNAME);
	}

	@Test
	@Order(5)
	public void should_find_player_by_email() {
		Player player = playerRepository.findByEmail(EMAIL);
		assertThat(player.getEmail()).isEqualTo(EMAIL);
	}

}
