/**
 * 
 */
package com.amazinggaming.wallet.service;


import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazinggaming.wallet.domain.Player;
import com.amazinggaming.wallet.domain.TransactionType;
import com.amazinggaming.wallet.domain.Wallet;
import com.amazinggaming.wallet.domain.WalletType;
import com.amazinggaming.wallet.dto.BaseDto;
import com.amazinggaming.wallet.dto.WalletDto;
import com.amazinggaming.wallet.exception.ApplicationException;
import com.amazinggaming.wallet.repository.PlayerRepository;
import com.amazinggaming.wallet.repository.TransactionTypeRepository;
import com.amazinggaming.wallet.repository.WalletRepository;

/**
 * @author dereshharry
 * 
 *         This class will handle wallet related features withdraw depoit
 *         balance
 *
 */
@Service
public class WalletService {

	@Autowired
	private PlayerRepository playerRepository;

	@Autowired
	private WalletRepository walletRepository;

	@Autowired
	private TransactionTypeRepository transactionTypeRepository;

	@Autowired
	private TransactionService transactionService;
	
	/**
	 * 
	 * This method allows players to deposit funds
	 * 
	 * @param deposit
	 * @return WalletDto
	 * @throws ApplicationException
	 */
	public WalletDto deposit(WalletDto deposit) throws ApplicationException {
		WalletDto response = new WalletDto();
		TransactionType transactionType = transactionTypeRepository.findByCode(deposit.getTransactionCode());
		Player player = playerRepository.findByUsername(deposit.getUsername());
		Wallet playersWallet = walletRepository.findByUserAndWalletType(player, WalletType.PLAYER_WALLET);
		Wallet playersBonusWallet = walletRepository.findByUserAndWalletType(player, WalletType.PLAYER_BONUS_WALLET);
		BaseDto transactionResult = transactionService.createTransactionAndApplyCharges(deposit.getAmount(), playersWallet, transactionType,
				deposit.getTransactionId(), player);
		response.setTransactionId(transactionResult.getTransactionId());
		response.setExternalTransactionId(transactionResult.getExternalTransactionId());
		response.setCashBalance(playersWallet.getBalance());
		response.setBonusBalance( playersBonusWallet != null ? playersBonusWallet.getBalance() : BigDecimal.ZERO);
		return response;
	}
	
	/**
	 * This method allows players to withdraw funds
	 * @param withdraw
	 * @return WalletDto
	 * @throws ApplicationException
	 */
	public WalletDto withdrawl(WalletDto withdraw) throws ApplicationException {
		WalletDto response = new WalletDto();
		TransactionType transactionType = transactionTypeRepository.findByCode(withdraw.getTransactionCode());
		Player player = playerRepository.findByUsername(withdraw.getUsername());
		Wallet cashWallet = walletRepository.findByUserAndWalletType(player, WalletType.PLAYER_WALLET);
		Wallet playersBonusWallet = walletRepository.findByUserAndWalletType(player, WalletType.PLAYER_BONUS_WALLET);
		BaseDto transactionResult = transactionService.createTransactionAndApplyCharges(withdraw.getAmount(), cashWallet, transactionType,
				withdraw.getTransactionId(), player);
		response.setTransactionId(transactionResult.getTransactionId());
		response.setExternalTransactionId(transactionResult.getExternalTransactionId());
		response.setCashBalance(cashWallet.getBalance());
		response.setBonusBalance( playersBonusWallet != null ? playersBonusWallet.getBalance() : BigDecimal.ZERO);
		return response;
	}
	
	/**
	 * This method allows players to do a balance enquiry
	 * @param withdraw
	 * @return WalletDto
	 * @throws ApplicationException
	 */
	public WalletDto balance(String username) {
		Player player = playerRepository.findByUsername(username);
		Wallet playersWallet = walletRepository.findByUserAndWalletType(player, WalletType.PLAYER_WALLET);
		Wallet playersBonusWallet = walletRepository.findByUserAndWalletType(player, WalletType.PLAYER_BONUS_WALLET);
		WalletDto walletDto = new WalletDto();
		walletDto.setCashBalance(playersWallet.getBalance());
		walletDto.setBonusBalance(playersBonusWallet.getBalance());
		return walletDto;
	}

}
