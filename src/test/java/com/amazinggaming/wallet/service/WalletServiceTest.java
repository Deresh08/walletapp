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

import com.amazinggaming.wallet.domain.Charge;
import com.amazinggaming.wallet.domain.Player;
import com.amazinggaming.wallet.domain.Profile;
import com.amazinggaming.wallet.domain.Transaction;
import com.amazinggaming.wallet.domain.TransactionType;
import com.amazinggaming.wallet.domain.Wallet;
import com.amazinggaming.wallet.domain.WalletStatus;
import com.amazinggaming.wallet.domain.WalletType;
import com.amazinggaming.wallet.dto.WalletDto;
import com.amazinggaming.wallet.exception.ApplicationException;
import com.amazinggaming.wallet.repository.ChargeRepository;
import com.amazinggaming.wallet.repository.PlayerRepository;
import com.amazinggaming.wallet.repository.TransactionRepository;
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
public class WalletServiceTest {

	@Autowired
	private PlayerRepository playerRepository;

	@Autowired
	private WalletRepository walletRepository;

	@Autowired
	private TransactionTypeRepository transactionTypeRepository;

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private ChargeRepository chargeRepository;

	@Autowired
	private WalletService walletService;

	private final static String DEPOSIT_TRX_ID = "7e3396be-f129-40ea-ab43-e2933810b11e";
	private final static String DEPOSIT_TRX_CODE = "CGPAY";
	private final static String WITHDRAWL_TRX_CODE = "DGPAY";
	private final static String WITHDRAWL_TRX_ID = "9ef5d727-065d-4384-96a1-0243ac2c5aca";
	private final static String USERNAME = "JOHN08";

	private BigDecimal amount = new BigDecimal(100.00).setScale(2);

	@Test
	@Order(1)
	public void should_deposit_bonus_cash() {
		WalletDto commonDto = new WalletDto();
		commonDto.setTransactionId(DEPOSIT_TRX_ID);
		commonDto.setUsername(USERNAME);
		commonDto.setAmount(amount);
		commonDto.setTransactionCode(DEPOSIT_TRX_CODE);
		try {
			Player player = createPlayer();
			createWallets();
			createTransactionTypeDeposit();
			createBonusDepositCharge();
			walletService.deposit(commonDto);
			Transaction transaction = transactionRepository.findByExternalId(commonDto.getTransactionId());
			Transaction childTransaction = transactionRepository.findByParentTransaction(transaction);
			Wallet bonusWallet = walletRepository.findByUserAndWalletType(player, WalletType.PLAYER_BONUS_WALLET);
			Wallet cashWallet = walletRepository.findByUserAndWalletType(player, WalletType.PLAYER_WALLET);
			assertThat(transaction).isNotNull();
			assertThat(transaction.getAmount()).isEqualTo(amount);
			assertThat(childTransaction).isNotNull();
			assertThat(childTransaction.getAmount()).isEqualTo(amount);
			assertThat(bonusWallet.getBalance()).isEqualTo(amount);
			assertThat(cashWallet.getBalance()).isEqualTo(amount);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	@Order(2)
	public void should_withdraw_cash() {
		WalletDto withdraw = new WalletDto();
		withdraw.setTransactionId(WITHDRAWL_TRX_ID);
		withdraw.setUsername(USERNAME);
		withdraw.setAmount(amount.subtract(new BigDecimal(50.00).setScale(2)));
		withdraw.setTransactionCode(WITHDRAWL_TRX_CODE);
		try {
			Player player = playerRepository.findByUsername(USERNAME);
			createTransactionTypeWithdrawl();
			walletService.withdrawl(withdraw);
			Wallet cashWallet = walletRepository.findByUserAndWalletType(player, WalletType.PLAYER_WALLET);
			Wallet bonusWallet = walletRepository.findByUserAndWalletType(player, WalletType.PLAYER_BONUS_WALLET);
			Transaction transactionWithdraw = transactionRepository.findByExternalId(withdraw.getTransactionId());
			assertThat(transactionWithdraw).isNotNull();
			assertThat(transactionWithdraw.getAmount()).isEqualTo(amount.subtract(new BigDecimal(50.00).setScale(2)).negate());
			assertThat(bonusWallet.getBalance()).isEqualTo(amount);
			assertThat(cashWallet.getBalance()).isEqualTo(amount.subtract(new BigDecimal(50.00).setScale(2)));
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void createBonusDepositCharge() {
		Charge charge = new Charge();
		charge.setType(TransactionType.CREDIT);
		charge.setName("Deposit Bonus");
		charge.setFixedCharge(BigDecimal.ZERO);
		charge.setMinAmount(BigDecimal.ZERO);
		charge.setMaxAmount(new BigDecimal(1000000));
		charge.setMinCharge(BigDecimal.ZERO);
		charge.setMaxCharge(new BigDecimal(1000000));
		charge.setPercentCharge(new BigDecimal(100));
		charge.setTransactionType(transactionTypeRepository.findByCode(DEPOSIT_TRX_CODE));
		charge.setApplyTowalletType(WalletType.PLAYER_BONUS_WALLET);
		chargeRepository.save(charge);
		assertThat(charge.getId()).isNotNull();
	}

	private void createTransactionTypeWithdrawl() {
		TransactionType withdrawlTransactionType = new TransactionType();
		withdrawlTransactionType.setCode("DGPAY");
		withdrawlTransactionType.setName("Google Pay");
		withdrawlTransactionType.setDescription("Withdrawl Google Pay");
		withdrawlTransactionType.setType(TransactionType.DEBIT);
		transactionTypeRepository.save(withdrawlTransactionType);
		assertThat(withdrawlTransactionType.getId()).isNotNull();
	}

	private void createTransactionTypeDeposit() {
		TransactionType depositTransactionType = new TransactionType();
		depositTransactionType.setCode(DEPOSIT_TRX_CODE);
		depositTransactionType.setName("Google Pay");
		depositTransactionType.setDescription("Deposit Google Pay");
		depositTransactionType.setType(TransactionType.CREDIT);
		transactionTypeRepository.save(depositTransactionType);
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
		cashWallet.setBalance(BigDecimal.ZERO);
		cashWallet.setWalletNumber("30001");
		cashWallet.setWalletStatus(WalletStatus.ACTIVE);
		cashWallet.setWalletType(WalletType.PLAYER_WALLET);
		cashWallet.setUser(player);
		walletRepository.save(cashWallet);
		assertThat(cashWallet.getId()).isNotNull();
		Wallet bonusWallet = new Wallet();
		bonusWallet.setBalance(BigDecimal.ZERO);
		bonusWallet.setWalletNumber("30002");
		bonusWallet.setWalletStatus(WalletStatus.ACTIVE);
		bonusWallet.setWalletType(WalletType.PLAYER_BONUS_WALLET);
		bonusWallet.setUser(player);
		walletRepository.save(bonusWallet);
		assertThat(bonusWallet.getId()).isNotNull();

	}

}
