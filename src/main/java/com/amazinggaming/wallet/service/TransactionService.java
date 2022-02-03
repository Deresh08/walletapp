package com.amazinggaming.wallet.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.amazinggaming.wallet.domain.Player;
import com.amazinggaming.wallet.domain.Transaction;
import com.amazinggaming.wallet.domain.TransactionType;
import com.amazinggaming.wallet.domain.Wallet;
import com.amazinggaming.wallet.dto.BaseDto;
import com.amazinggaming.wallet.exception.ApplicationException;
import com.amazinggaming.wallet.repository.TransactionRepository;
import com.amazinggaming.wallet.repository.WalletRepository;

/**
 * @author dereshharry
 *
 *	This class handles creating transactions
 *
 */
@Service
public class TransactionService {


	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private ChargeService chargeService;

	@Autowired
	private WalletRepository walletRepository;

	/**
	 * 
	 * Method creates transactions and applies fees
	 * 
	 * @param originalAmount
	 * @param wallet
	 * @param transactionType
	 * @param externalTransactionId
	 * @param player
	 * @return
	 * @throws ApplicationException
	 */
	public BaseDto createTransactionAndApplyCharges(BigDecimal originalAmount, Wallet wallet,
			TransactionType transactionType, String externalTransactionId, Player player) throws ApplicationException {
		BaseDto response = new BaseDto();
		
		Transaction transaction = new Transaction();
		BigDecimal amount = originalAmount;

		if (transactionType.getType().equals(TransactionType.DEBIT)) {
			amount = originalAmount.negate();
		}
		BigDecimal balance = wallet.getBalance();
		BigDecimal walletCharge = chargeService.calculateTotalCharge(wallet, transactionType, originalAmount);
		balance = balance.add(amount).add(walletCharge);
		if (balance.compareTo(BigDecimal.ZERO) == -1) {
			throw new ApplicationException("Insufficient Funds");
		}
		wallet.setBalance(wallet.getBalance().add(amount));
		transaction.setExternalId(externalTransactionId);
		transaction.setAmount(amount);
		transaction.setTransactionType(transactionType);
		transaction.setDescription(transactionType.getDescription());
		transaction.setWallet(wallet);
		walletRepository.save(wallet);

		transactionRepository.save(transaction);
		chargeService.calculateChargeAndCreateTransaction(player, transactionType, originalAmount, transaction);
		response.setTransactionId(transaction.getExternalId());
		response.setAmount( transaction.getAmount());
		response.setExternalTransactionId(String.valueOf(transaction.getId()));
		return response;
	}

	/**
	 * 
	 * Method creates transactions and applies fees in addition it accepts a parent transaction
	 * 
	 * @param originalAmount
	 * @param wallet
	 * @param transactionType
	 * @param externalTransactionId
	 * @param player
	 * @param parentTransaction
	 * @return
	 * @throws ApplicationException
	 */
	public BaseDto createTransactionAndApplyCharges(BigDecimal originalAmount, Wallet wallet,
			TransactionType transactionType, String externalTransactionId, Player player, String parentTransaction)
			throws ApplicationException {
		BaseDto response = new BaseDto();
		Transaction transaction = new Transaction();
		BigDecimal amount = originalAmount;

		if (transactionType.getType().equals(TransactionType.DEBIT)) {
			amount = originalAmount.negate();
		}
		BigDecimal balance = wallet.getBalance();
		BigDecimal walletCharge = chargeService.calculateTotalCharge(wallet, transactionType, originalAmount);
		balance = balance.add(amount).add(walletCharge);
		if (balance.compareTo(BigDecimal.ZERO) == -1) {
			throw new ApplicationException("Insufficient Funds");
		}
		wallet.setBalance(wallet.getBalance().add(amount));
		transaction.setExternalId(externalTransactionId);
		transaction.setAmount(amount);
		transaction.setTransactionType(transactionType);
		transaction.setDescription(transactionType.getDescription());
		Transaction initialTransaction = transactionRepository.findByExternalId(externalTransactionId);
		transaction.setParentTransaction(initialTransaction);
		transaction.setWallet(wallet);
		walletRepository.save(wallet);

		transactionRepository.save(transaction);
		chargeService.calculateChargeAndCreateTransaction(player, transactionType, originalAmount, transaction);
		
		response.setTransactionId(transaction.getExternalId());
		response.setAmount( transaction.getAmount());
		response.setExternalTransactionId(String.valueOf(transaction.getId()));
		return response;
	}
	
	  public Map<String, Object> getAllTransactions(String description, int page, int size) {
	      List<Transaction> transactions = new ArrayList<Transaction>();
	      Pageable paging = PageRequest.of(page, size);
	      Page<Transaction> pageTransactions;
	      if (description == null)
	    	  pageTransactions = transactionRepository.findAll(paging);
	      else
	    	  pageTransactions = transactionRepository.findByDescriptionContaining(description, paging);
	      transactions = pageTransactions.getContent();
	      Map<String, Object> response = new HashMap<>();
	      response.put("transactions", transactions);
	      response.put("currentPage", pageTransactions.getNumber());
	      response.put("totalItems", pageTransactions.getTotalElements());
	      response.put("totalPages", pageTransactions.getTotalPages());
	      return response;
	  }

}
