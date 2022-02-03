/**
 * 
 */
package com.amazinggaming.wallet.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazinggaming.wallet.domain.Bet;
import com.amazinggaming.wallet.domain.BetStatus;
import com.amazinggaming.wallet.domain.Player;
import com.amazinggaming.wallet.domain.TransactionType;
import com.amazinggaming.wallet.domain.Wallet;
import com.amazinggaming.wallet.domain.WalletType;
import com.amazinggaming.wallet.dto.BaseDto;
import com.amazinggaming.wallet.dto.BetDto;
import com.amazinggaming.wallet.exception.ApplicationException;
import com.amazinggaming.wallet.repository.BetRepository;
import com.amazinggaming.wallet.repository.PlayerRepository;
import com.amazinggaming.wallet.repository.TransactionTypeRepository;
import com.amazinggaming.wallet.repository.WalletRepository;

/**
 * @author dereshharry
 * 
 * this class handles placing bet and payouts
 *
 */
@Service
public class BetService {

	@Autowired
	private PlayerRepository playerRepository;

	@Autowired
	private WalletRepository walletRepository;

	@Autowired
	private TransactionTypeRepository transactionTypeRepository;

	@Autowired
	private TransactionService transactionService;
	
	@Autowired
	private BetRepository betRepository;

	/**
	 * 
	 * Registers a bet, stake is split proportionally with bonus wallet
	 * 
	 * 
	 * @param betRequest
	 * @return
	 * @throws ApplicationException
	 */
	public BetDto bet(BetDto betRequest) throws ApplicationException {
		TransactionType transactionType = transactionTypeRepository.findByCode(betRequest.getTransactionCode());
		Player player = playerRepository.findByUsername(betRequest.getUsername());
		Bet bet = new Bet();
		bet.setBetStatus(BetStatus.PENDING);
		bet.setEvent(betRequest.getEvent());
		bet.setOdds(betRequest.getOdds());
		bet.setCreatedBy(player);
	
		List<Wallet> playerWallets = walletRepository.findByUser(player);
		Wallet cashWallet = playerWallets.stream().filter(wallet -> wallet.getWalletType() == WalletType.PLAYER_WALLET)
				.findAny().get();
		Optional<Wallet> bonusWallet = playerWallets.stream()
				.filter(wallet -> wallet.getWalletType() == WalletType.PLAYER_BONUS_WALLET).findAny();
		BigDecimal calulatedBonusBet = BigDecimal.ZERO;
		BigDecimal calulatedCashBet = BigDecimal.ZERO;
		if (cashWallet.getBalance().compareTo(betRequest.getAmount()) == -1) {
			if(!bonusWallet.isPresent()) {
				throw new ApplicationException("Not Enough Funds Available");
			}
			calulatedBonusBet = betRequest.getAmount().subtract(cashWallet.getBalance());
			calulatedCashBet = betRequest.getAmount().subtract(calulatedBonusBet);
			if(bonusWallet.get().getBalance().compareTo(calulatedBonusBet) == -1) {
				throw new ApplicationException("Not Enough Funds Available");
			}else {
				BaseDto cashTransaction = transactionService.createTransactionAndApplyCharges(calulatedCashBet, cashWallet, transactionType,
						betRequest.getTransactionId(), player);
				bet.setCashAmount(calulatedCashBet);
				
				transactionService.createTransactionAndApplyCharges(calulatedBonusBet, bonusWallet.get(), transactionType,
						betRequest.getTransactionId()+cashTransaction.getTransactionId(), player, cashTransaction.getTransactionId());
				bet.setBonusAmount(calulatedBonusBet);
				betRepository.save(bet);
				return prepareReturnBetDto(cashTransaction, player, bet);
			}
		}else {
			BaseDto cashTransaction = transactionService.createTransactionAndApplyCharges(betRequest.getAmount(), cashWallet, transactionType,
					betRequest.getTransactionId(), player);
			bet.setCashAmount(betRequest.getAmount());
			bet.setBonusAmount(BigDecimal.ZERO);
			betRepository.save(bet);
			return prepareReturnBetDto(cashTransaction, player, bet);
		}
	}
	
	
	/**
	 * 
	 * This method 
	 * 
	 * @param betDto
	 * @return
	 * @throws ApplicationException
	 */
	public BetDto win(BetDto betDto) throws ApplicationException{
		Optional<Bet> betOptional = betRepository.findById(betDto.getBetId());
		TransactionType transactionType = transactionTypeRepository.findByCode(betDto.getTransactionCode());
		if(betOptional.isPresent()) {
			Player player = playerRepository.findByUsername(betDto.getUsername());
			Bet bet = betOptional.get();
			
			if(bet.getBetStatus() == BetStatus.WINNING) {
				throw new ApplicationException("Bet already winning");
			}
			
			BigDecimal cashPayout = bet.getCashAmount().multiply(bet.getOdds()).subtract(bet.getCashAmount()).setScale(2);
			
			BigDecimal bonusPayout = bet.getBonusAmount().multiply(bet.getOdds()).subtract(bet.getBonusAmount()).setScale(2);
			
			List<Wallet> playerWallets = walletRepository.findByUser(player);
			Wallet cashWallet = playerWallets.stream().filter(wallet -> wallet.getWalletType() == WalletType.PLAYER_WALLET)
					.findAny().get();
			Optional<Wallet> bonusWallet = playerWallets.stream()
					.filter(wallet -> wallet.getWalletType() == WalletType.PLAYER_BONUS_WALLET).findAny();
			
			
			BaseDto cashTransaction = transactionService.createTransactionAndApplyCharges(cashPayout, cashWallet, transactionType,
					betDto.getTransactionId(), player);
			
			if(bonusPayout.compareTo(BigDecimal.ZERO) > 0 && bonusWallet.isPresent()) {
				transactionService.createTransactionAndApplyCharges(bonusPayout, bonusWallet.get(), transactionType,
						betDto.getTransactionId()+cashTransaction.getExternalTransactionId(), player, cashTransaction.getExternalTransactionId());
			}
			
			 bet.setBetStatus(BetStatus.WINNING);
			 betRepository.save(bet);
			return prepareReturnBetDto(cashTransaction, player, bet);
		}else {
			throw new ApplicationException("Bet Does Not Exist");
		}
	}
	

	/**
	 * Prepares return object
	 * @param baseDto
	 * @param player
	 * @param bet
	 * @return
	 */
	private BetDto prepareReturnBetDto(BaseDto baseDto, Player player, Bet bet) {
		List<Wallet> playerWallets = walletRepository.findByUser(player);
		Wallet cashWallet = playerWallets.stream().filter(wallet -> wallet.getWalletType() == WalletType.PLAYER_WALLET)
				.findAny().get();
		Optional<Wallet> bonusWallet = playerWallets.stream()
				.filter(wallet -> wallet.getWalletType() == WalletType.PLAYER_BONUS_WALLET).findAny();
		
		BetDto betResp = new BetDto();
		betResp.setTransactionId(baseDto.getTransactionId());
		betResp.setExternalTransactionId(baseDto.getExternalTransactionId());
		betResp.setCashBalance(cashWallet.getBalance());
		betResp.setBonusBalance(bonusWallet.isPresent() ? bonusWallet.get().getBalance() : BigDecimal.ZERO);
		betResp.setBetId(bet.getId());
		return betResp;
	}
}
