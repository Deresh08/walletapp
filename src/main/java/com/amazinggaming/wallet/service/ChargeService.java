/**
 * 
 */
package com.amazinggaming.wallet.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazinggaming.wallet.domain.Charge;
import com.amazinggaming.wallet.domain.Player;
import com.amazinggaming.wallet.domain.Transaction;
import com.amazinggaming.wallet.domain.TransactionType;
import com.amazinggaming.wallet.domain.Wallet;
import com.amazinggaming.wallet.domain.WalletType;
import com.amazinggaming.wallet.repository.ChargeRepository;
import com.amazinggaming.wallet.repository.TransactionRepository;
import com.amazinggaming.wallet.repository.WalletRepository;

/**
 * @author dereshharry
 * 
 * This class handles all charges
 *
 */
@Service
public class ChargeService {
	
	@Autowired
	private ChargeRepository chargeRepository;
	
	@Autowired
	private TransactionRepository transactionRepository;
	
	@Autowired
	private WalletRepository walletRepository;
	
	
	
	/**
	 * 
	 * This method returns the total charges that apply to WalletType and TransactionType
	 * 
	 * @param wallet
	 * @param transactionType
	 * @param amount
	 * @return
	 */
	public BigDecimal calculateTotalCharge(Wallet wallet, TransactionType transactionType, BigDecimal amount) {
		BigDecimal credit = BigDecimal.ZERO;
		BigDecimal debit = BigDecimal.ZERO;
		List<Charge> applicableCharges = getApplicableCharges(transactionType, wallet.getWalletType());
		for (Charge charge : applicableCharges) {
			BigDecimal calulatedCharge = calculateCharge(amount, charge);
			if (charge.getType().equals(TransactionType.CREDIT)) {
				credit = credit.add(calulatedCharge);
			} else {
				debit = debit.add(calulatedCharge);
			}
		}
		credit = credit.subtract(debit);
		return credit;

	}

	/**
	 * 
	 * This method calculates charges and applies them
	 * 
	 * @param player
	 * @param transactionType
	 * @param amount
	 * @param parentTransaction
	 */
	public void calculateChargeAndCreateTransaction(Player player, TransactionType transactionType, BigDecimal amount,
			Transaction parentTransaction) {
		List<Wallet> wallets = walletRepository.findByUser(player);
		for (Wallet wallet : wallets) {
			List<Charge> applicableCharges = getApplicableCharges(transactionType, wallet.getWalletType());
			for (Charge charge : applicableCharges) {
				BigDecimal calulatedCharge = calculateCharge(amount, charge);
				if (charge.getType().equals(TransactionType.CREDIT)) {
					saveChargeTransaction(wallet, calulatedCharge, charge, parentTransaction);
				} else {
					saveChargeTransaction(wallet, calulatedCharge.negate(), charge, parentTransaction);
				}
			}
		}
	}
	
	
	/**
	 * General method to fetch applicable charges
	 * @param transactionType
	 * @param walletType
	 * @return
	 */
	private List<Charge> getApplicableCharges(TransactionType transactionType, WalletType walletType){
		List<Charge> charges = chargeRepository.findByTransactionType(transactionType);
		return charges.stream()
				.filter(charge -> charge.getApplyTowalletType() == walletType).toList();
	}
	
	
	
	/**
	 * 
	 * Calculates charge and returns amount
	 * @param amount
	 * @param charge
	 * @return
	 */
	private BigDecimal calculateCharge(BigDecimal amount, Charge charge) {
		BigDecimal chargeTotal = BigDecimal.ZERO;
		if (amount.compareTo(charge.getMinAmount()) == -1) {
			return chargeTotal;
		}
		if (amount.compareTo(charge.getMaxAmount()) == 1) {
			return chargeTotal;
		}
		chargeTotal = chargeTotal.add(charge.getFixedCharge());
		BigDecimal percentDecimal = charge.getPercentCharge().divide(new BigDecimal(100.0), 2, RoundingMode.FLOOR);
		BigDecimal calculatedAmount = amount.multiply(percentDecimal).setScale(2, RoundingMode.FLOOR);
		chargeTotal = chargeTotal.add(calculatedAmount);
		
		if (chargeTotal.compareTo(charge.getMinCharge()) == -1) {
			return charge.getMinAmount();
		} else if (chargeTotal.compareTo(charge.getMaxCharge()) == 1) {
			return charge.getMaxAmount();
		} else {
			
			return chargeTotal;
		}

	}
	
	
	/**
	 * 
	 * Method saves transaction
	 * 
	 * @param wallet
	 * @param amount
	 * @param charge
	 * @param parentTransaction
	 */
	private void saveChargeTransaction(Wallet wallet, BigDecimal amount, Charge charge, Transaction parentTransaction) {
		Transaction transaction = new Transaction();
		transaction.setAmount(amount);
		transaction.setTransactionType(charge.getTransactionType());
		transaction.setDescription(charge.getName());
		if (parentTransaction != null) {
			transaction.setParentTransaction(parentTransaction);
		}
		transaction.setWallet(wallet);
		transactionRepository.save(transaction);
		wallet.setBalance(wallet.getBalance().add(amount));
		walletRepository.save(wallet);

	}
	

}
