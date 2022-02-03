/**
 * 
 */
package com.amazinggaming.wallet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amazinggaming.wallet.domain.Bet;
import com.amazinggaming.wallet.domain.User;

/**
 * @author dereshharry
 * 
 * Repository for Bet CRUD and additional findBy
 *
 */
public interface BetRepository extends JpaRepository<Bet, Long>{
	
	List<Bet> findBycreatedBy(User createdBy);

}
