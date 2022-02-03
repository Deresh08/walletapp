/**
 * 
 */
package com.amazinggaming.wallet.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Ignore;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import com.amazinggaming.wallet.domain.TransactionType;

/**
 * @author dereshharry
 *
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TransactionTypeRepositoryTest {

	@Autowired
	TransactionTypeRepository transactionTypeRepository;

	@Test
	@Order(1)
	@Ignore
	public void should_find_no_transaction_types_if_repository_is_empty() {
		List<TransactionType> transactionTypes = transactionTypeRepository.findAll();
		assertThat(transactionTypes).isEmpty();
	}

	@Test
	@Order(2)
	void should_store_transaction_type() {
		TransactionType transactionType = new TransactionType();
		transactionType.setCode("CGPAY");
		transactionType.setName("Google Pay");
		transactionType.setDescription("Deposit Google Pay");
		transactionType.setType(TransactionType.CREDIT);
		transactionTypeRepository.save(transactionType);
		
		TransactionType transactionTypeApple = new TransactionType();
		transactionTypeApple.setCode("CAPAY");
		transactionTypeApple.setName("Apple Pay");
		transactionTypeApple.setDescription("Deposit Apple Pay");
		transactionTypeApple.setType(TransactionType.CREDIT);
		transactionTypeRepository.save(transactionTypeApple);
		assertThat(transactionTypeApple.getId()).isNotNull();
	}
	
	@Test
	@Order(3)
	@Ignore
	void should_find_all_transaction_types() {
		List<TransactionType> transactionTypes = transactionTypeRepository.findAll();
		assertThat(transactionTypes).hasSize(2);
	}
	
	@Test
	@Order(4)
	void should_find_transaction_type_by_code() {
		TransactionType transactionType = transactionTypeRepository.findByCode("CAPAY");
		assertThat(transactionType.getCode()).containsIgnoringCase("CAPAY");
	}
}
