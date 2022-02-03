/**
 * 
 */
package com.amazinggaming.wallet.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amazinggaming.wallet.domain.Player;

/**
 * @author dereshharry
 * 
 * Repository for Player CRUD and additional findBy
 *
 */
public interface PlayerRepository extends JpaRepository<Player, Long>{
	
	Player findByUsername(String username);
	
	Player findByEmail(String email);

}
