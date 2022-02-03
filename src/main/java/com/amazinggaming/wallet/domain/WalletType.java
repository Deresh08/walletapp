/**
 * 
 */
package com.amazinggaming.wallet.domain;

/**
 * @author Deresh Harry
 * 
 * @apiNote This class could be an entity inside the database for the purpose of
 *          this test i have decided to use ENUM, we could add attributes like
 *          daily limit, monthly limit, yearly limit, continent (where wallet
 *          type applies) i.e. (AF, AN, AS, EU).
 *			{@link WalletType#PLAYER_WALLET}
 * 			{@link WalletType#PLAYER_BONUS_WALLET}
 */
public enum WalletType {
	/**
	 * Regular Player Wallet
	 */
	PLAYER_WALLET,
	/**
	 * Regular Player Bonus Wallet
	 */
	PLAYER_BONUS_WALLET
}
