/**
 * 
 */
package com.amazinggaming.wallet.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.amazinggaming.wallet.domain.Transaction;
import com.amazinggaming.wallet.domain.Wallet;

/**
 * @author dereshharry
 * 
 * Repository for Transaction CRUD and additional findBy
 *
 */
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
	
	List<Transaction> findByWalletIn(List<Wallet> wallet);
	
	Transaction findByExternalId(String externalId);
	
	Transaction findByParentTransaction(Transaction transaction);
	
	Page<Transaction> findByDescriptionContaining(String description, Pageable pageable);

}
