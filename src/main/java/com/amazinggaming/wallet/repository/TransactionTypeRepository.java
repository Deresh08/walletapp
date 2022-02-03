/**
 * 
 */
package com.amazinggaming.wallet.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amazinggaming.wallet.domain.TransactionType;

/**
 * @author dereshharry
 * 
 * Repository for TransactionType CRUD and additional findBy
 *
 */
public interface TransactionTypeRepository extends JpaRepository<TransactionType, Long> {
	
	TransactionType findByCode(String code);

}
