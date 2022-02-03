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

import com.amazinggaming.wallet.domain.Charge;
import com.amazinggaming.wallet.domain.TransactionType;
import com.amazinggaming.wallet.domain.WalletType;

/**
 * @author dereshharry
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ChargeRepositoryTest {
	
	@Autowired
	private TransactionTypeRepository transactionTypeRepository;
	
	@Autowired
	private ChargeRepository chargeRepository;
	
	private static final String TRANSACTION_TYPE_CODE = "CPAYPAYL";
	
	private static final String CHARGE_NAME = "Deposit Bonus";

	@Test
	@Order(1)
	public void should_store_transaction_type_for_test() {
		TransactionType transactionType = new TransactionType();
		transactionType.setCode(TRANSACTION_TYPE_CODE);
		transactionType.setName("Pay Pal");
		transactionType.setDescription("Deposit Pay Pal");
		transactionType.setType(TransactionType.CREDIT);
		transactionTypeRepository.save(transactionType);
		assertThat(transactionType.getId()).isNotNull();
	}
	
	@Test
	@Order(2)
	public void should_find_no_charges_if_repository_is_empty() {
		List<Charge> charges = chargeRepository.findAll();
		assertThat(charges).isEmpty();
	}

	@Test
	@Order(3)
	public void should_store_charge() {
		TransactionType transactionTp = transactionTypeRepository.findByCode(TRANSACTION_TYPE_CODE);
		Charge charge = new Charge();
		charge.setType(TransactionType.CREDIT);
		charge.setName("Deposit Bonus");
		charge.setFixedCharge(BigDecimal.ZERO);
		charge.setMinAmount(BigDecimal.ZERO);
		charge.setMaxAmount(new BigDecimal(1000000));
		charge.setMinCharge(BigDecimal.ZERO);
		charge.setMaxCharge(new BigDecimal(1000000));
		charge.setPercentCharge(new BigDecimal(100));
		charge.setTransactionType(transactionTp);
		charge.setApplyTowalletType(WalletType.PLAYER_BONUS_WALLET);
		chargeRepository.save(charge);
		assertThat(charge.getId()).isNotNull();
	}
	

	@Test
	@Order(4)
	public void should_find_all_charges() {
		List<Charge> charges = chargeRepository.findAll();
		assertThat(charges).hasSize(1);
		
	}
	
	@Test
	@Order(5)
	public void should_find_all_charges_by_transaction_type() {
		TransactionType transactionTp = transactionTypeRepository.findByCode(TRANSACTION_TYPE_CODE);
		List<Charge> charges = chargeRepository.findByTransactionType(transactionTp);
		assertThat(charges).hasSize(1);
		
	}
	
	@Test
	@Order(5)
	public void should_update_charge() {
		Charge charge = chargeRepository.findByName(CHARGE_NAME);
		charge.setPercentCharge(new BigDecimal(100));
		chargeRepository.save(charge);
		Charge charges = chargeRepository.findByName(CHARGE_NAME);
		assertThat(charges.getPercentCharge()).isEqualTo(50);
	}
	
	
	
	

}
