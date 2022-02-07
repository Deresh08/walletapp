/**
 * 
 */
package com.amazinggaming.wallet.service;


import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazinggaming.wallet.domain.Player;
import com.amazinggaming.wallet.domain.Profile;
import com.amazinggaming.wallet.domain.TransactionType;
import com.amazinggaming.wallet.domain.Wallet;
import com.amazinggaming.wallet.domain.WalletStatus;
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
	
	
	private static final int WALLET_NO_SIZE = 15;
	
	/**
	 * 
	 * This method allows players to deposit funds
	 * 
	 * @param deposit
	 * @return WalletDto
	 * @throws ApplicationException
	 */
	public WalletDto deposit(WalletDto deposit) throws ApplicationException {
		boolean returnPassword = false;
		WalletDto response = new WalletDto();
		TransactionType transactionType = transactionTypeRepository.findByCode(deposit.getTransactionCode());
		Player player = playerRepository.findByUsername(deposit.getUsername());
		if(player == null) {
			createPlayer(deposit.getUsername());
			returnPassword = true;
		}
		player = playerRepository.findByUsername(deposit.getUsername());
		Wallet playersWallet = walletRepository.findByUserAndWalletType(player, WalletType.PLAYER_WALLET);
		Wallet playersBonusWallet = walletRepository.findByUserAndWalletType(player, WalletType.PLAYER_BONUS_WALLET);
		BaseDto transactionResult = transactionService.createTransactionAndApplyCharges(deposit.getAmount(), playersWallet, transactionType,
				deposit.getTransactionId(), player);
		if(returnPassword) {
			response.setPassword(player.getPassword());
		}
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
	
	
	
	private void createPlayer(String username) {
		Player player = new Player();
		player.setUsername(username);
		player.setPassword(generateRandomPassword(WALLET_NO_SIZE));
		player.setProfile(Profile.PLAYER);
		playerRepository.save(player);
		createPlayerWallets(player);
	}
	
	private void createPlayerWallets(Player player) {
		Wallet cashWallet = new Wallet();
		Wallet bonusWallet = new Wallet();
		cashWallet.setBalance(BigDecimal.ZERO);
		cashWallet.setWalletNumber(generateRandomWalletNumber(WALLET_NO_SIZE));
		cashWallet.setWalletStatus(WalletStatus.ACTIVE);
		cashWallet.setUser(player);
		cashWallet.setWalletType(WalletType.PLAYER_WALLET);
		bonusWallet.setBalance(BigDecimal.ZERO);
		bonusWallet.setWalletNumber(generateRandomWalletNumber(WALLET_NO_SIZE));
		bonusWallet.setWalletStatus(WalletStatus.ACTIVE);
		bonusWallet.setUser(player);
		bonusWallet.setWalletType(WalletType.PLAYER_BONUS_WALLET);
		walletRepository.save(cashWallet);
		walletRepository.save(bonusWallet);
	}
	
	
	private String generateRandomWalletNumber(int length) {
	    Random random = new Random();
	    char[] digits = new char[length];
	    digits[0] = (char) (random.nextInt(9) + '1');
	    for (int i = 1; i < length; i++) {
	        digits[i] = (char) (random.nextInt(10) + '0');
	    }
	    return new String(digits);
	}
	
	public static String generateRandomPassword(int len)
    {
        // ASCII range – alphanumeric (0-9, a-z, A-Z)
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
 
        SecureRandom random = new SecureRandom();
 
        // each iteration of the loop randomly chooses a character from the given
        // ASCII range and appends it to the `StringBuilder` instance
        return IntStream.range(0, len)
                .map(i -> random.nextInt(chars.length()))
                .mapToObj(randomIndex -> String.valueOf(chars.charAt(randomIndex)))
                .collect(Collectors.joining());
    }
}
