/**
 * 
 */
package com.amazinggaming.wallet.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.amazinggaming.wallet.domain.Bet;
import com.amazinggaming.wallet.domain.Player;
import com.amazinggaming.wallet.domain.Profile;
import com.amazinggaming.wallet.domain.TransactionType;
import com.amazinggaming.wallet.domain.Wallet;
import com.amazinggaming.wallet.domain.WalletStatus;
import com.amazinggaming.wallet.domain.WalletType;
import com.amazinggaming.wallet.dto.BetDto;
import com.amazinggaming.wallet.dto.WalletDto;
import com.amazinggaming.wallet.exception.ApplicationException;
import com.amazinggaming.wallet.repository.BetRepository;
import com.amazinggaming.wallet.repository.PlayerRepository;
import com.amazinggaming.wallet.repository.TransactionTypeRepository;
import com.amazinggaming.wallet.repository.WalletRepository;

/**
 * @author dereshharry
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BetServiceTest {

	@Autowired
	private PlayerRepository playerRepository;

	@Autowired
	private WalletRepository walletRepository;

	@Autowired
	private TransactionTypeRepository transactionTypeRepository;
	
	@Autowired
	private BetService betService;
	
	@Autowired
	private BetRepository betRepository;
	
	@Autowired
	private WalletService walletService;
	
	private final static String DEPOSIT_TRX_CODE = "CGPAY";
	private final static String DEPOSIT_TRX_ID = "7e3396be-f129-40ea-ab43-e2933810b11999";
	private final static String BET_TRX_ID = "7e3396be-f129-40ea-ab43-e2933810b11e";
	private final static String BET_TRX_ID_ = "7e3396be-f129-4as0ea-ab43-e2933810b11e";
	private final static String WIN_BET_TRX_CODE = "CBET";
	private final static String PLACE_BET_TRX_CODE = "DBET";
	private final static String WIN_TRX_ID = "9ef5d727-065d-4384-96a1-0243ac2c5aca";
	private final static String USERNAME = "JOHN08";
	private long betId = 0;
	
	@Test
	@Order(1)
	public void should_place_bet() throws ApplicationException {
		createPlayer();
		createWallets();
		createTransactionTypeBetDebit();
		BetDto betDto = new BetDto();
		betDto.setAmount(new BigDecimal(120.00));
		betDto.setTransactionId(BET_TRX_ID);
		betDto.setTransactionCode(PLACE_BET_TRX_CODE);
		betDto.setUsername(USERNAME);
		betDto.setOdds(new BigDecimal(11.00));
		betDto.setEvent("Liverpool vs Chelsea - Home win");
		BetDto betResult = betService.bet(betDto);
		betId = betResult.getBetId();
		Bet bet = betRepository.findById(betId).get();
		assertThat(bet.getCashAmount()).isEqualByComparingTo(new BigDecimal(100.00));
		assertThat(bet.getBonusAmount()).isEqualByComparingTo(new BigDecimal(20.00));
		assertThat(bet).isNotNull();
		assertThat(betId).isNotEqualTo(0);
	}
	
	@Test
	@Order(2)
	public void should_place_win() throws Exception {
		createTransactionTypeBetCredit();
		depositMoney();
		BetDto placeBetResult = placeBetForWin();
		BetDto betDto = new BetDto();
		betDto.setTransactionId(WIN_TRX_ID);
		betDto.setTransactionCode(WIN_BET_TRX_CODE);
		betDto.setUsername(USERNAME);
		if(placeBetResult.getBetId() == 0) {
			throw new Exception("BET ID IS 0");
		}
		betDto.setBetId(placeBetResult.getBetId());
		BetDto betResult = betService.win(betDto);
		assertThat(betResult.getCashBalance()).isEqualByComparingTo(new BigDecimal(1000));
		assertThat(betResult.getBonusBalance()).isEqualByComparingTo(new BigDecimal(210));
	}
	
	private void createTransactionTypeBetCredit() {
		TransactionType depositTransactionType = new TransactionType();
		depositTransactionType.setCode(WIN_BET_TRX_CODE);
		depositTransactionType.setName("Bet Win");
		depositTransactionType.setDescription("Winning bet");
		depositTransactionType.setType(TransactionType.CREDIT);
		transactionTypeRepository.save(depositTransactionType);
	}
	
	private void createTransactionTypeBetDebit() {
		TransactionType depositTransactionType = new TransactionType();
		depositTransactionType.setCode(PLACE_BET_TRX_CODE);
		depositTransactionType.setName("Bet Placed");
		depositTransactionType.setDescription("Bet Placed");
		depositTransactionType.setType(TransactionType.DEBIT);
		transactionTypeRepository.save(depositTransactionType);
	}
	
	private BetDto placeBetForWin() throws ApplicationException {
		BetDto placeBetRequest = new BetDto();
		placeBetRequest.setAmount(new BigDecimal(120.00));
		placeBetRequest.setTransactionId(BET_TRX_ID_);
		placeBetRequest.setTransactionCode(PLACE_BET_TRX_CODE);
		placeBetRequest.setUsername(USERNAME);
		placeBetRequest.setOdds(new BigDecimal(11.00));
		placeBetRequest.setEvent("Liverpool vs Chelsea - Home win");
		return betService.bet(placeBetRequest);
	}
	
	private void depositMoney() throws ApplicationException {
		createTransactionTypeDeposit();
		WalletDto commonDto = new WalletDto();
		commonDto.setTransactionId(DEPOSIT_TRX_ID);
		commonDto.setUsername(USERNAME);
		commonDto.setAmount(new BigDecimal(100.0));
		commonDto.setTransactionCode(DEPOSIT_TRX_CODE);
		
		walletService.deposit(commonDto);
		
	}

	private Player createPlayer() {
		Player player = new Player();
		player.setAddress("Hal Gharhur");
		player.setEmail("testWalletService@gmail.com");
		player.setIdNumber("0129129A");
		player.setLastName("John");
		player.setName("DOe");
		player.setUsername(USERNAME);
		player.setProfile(Profile.PLAYER);
		player.setPassword("12345Kebab!");
		playerRepository.save(player);
		assertThat(player.getId()).isNotNull();
		return player;
	}

	private void createWallets() {

		Player player = playerRepository.findByUsername(USERNAME);
		Wallet cashWallet = new Wallet();
		cashWallet.setBalance(new BigDecimal(100.00));
		cashWallet.setWalletNumber("30001");
		cashWallet.setWalletStatus(WalletStatus.ACTIVE);
		cashWallet.setWalletType(WalletType.PLAYER_WALLET);
		cashWallet.setUser(player);
		walletRepository.save(cashWallet);
		assertThat(cashWallet.getId()).isNotNull();
		Wallet bonusWallet = new Wallet();
		bonusWallet.setBalance(new BigDecimal(50.00));
		bonusWallet.setWalletNumber("30002");
		bonusWallet.setWalletStatus(WalletStatus.ACTIVE);
		bonusWallet.setWalletType(WalletType.PLAYER_BONUS_WALLET);
		bonusWallet.setUser(player);
		walletRepository.save(bonusWallet);
		assertThat(bonusWallet.getId()).isNotNull();

	}
	
	
	private void createTransactionTypeDeposit() {
		TransactionType depositTransactionType = new TransactionType();
		depositTransactionType.setCode(DEPOSIT_TRX_CODE);
		depositTransactionType.setName("Google Pay");
		depositTransactionType.setDescription("Deposit Google Pay");
		depositTransactionType.setType(TransactionType.CREDIT);
		transactionTypeRepository.save(depositTransactionType);
	}
}
