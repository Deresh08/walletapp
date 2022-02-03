/**
 * 
 */
package com.amazinggaming.wallet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amazinggaming.wallet.domain.User;
import com.amazinggaming.wallet.domain.Wallet;
import com.amazinggaming.wallet.domain.WalletType;

/**
 * @author dereshharry
 * 
 * Repository for Wallet CRUD and additional findBy
 *
 */
public interface WalletRepository extends JpaRepository<Wallet, Long> {
	
	 public List<Wallet> findByUser(User user);
	 
	 public Wallet findByWalletNumber(String walletNumber);
	 
	 public Wallet findByUserAndWalletType(User user, WalletType walletType);

}
