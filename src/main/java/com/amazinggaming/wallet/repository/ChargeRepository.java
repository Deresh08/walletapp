/**
 * 
 */
package com.amazinggaming.wallet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amazinggaming.wallet.domain.Charge;
import com.amazinggaming.wallet.domain.TransactionType;

/**
 * @author dereshharry
 * 
 * Repository for Charge CRUD and additional findBy
 *
 */
public interface ChargeRepository extends JpaRepository<Charge, Long>{
	
	List<Charge> findByTransactionType(TransactionType transactionType);
	
	Charge findByName(String name);

}
