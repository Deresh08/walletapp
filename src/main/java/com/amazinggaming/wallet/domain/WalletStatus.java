/**
 * 
 */
package com.amazinggaming.wallet.domain;

/**
 * @author dereshharry
 *
 * @apiNote This class could be an entity inside the database for 
 *			the purpose of this test i have decided to use ENUM. 
 * 			
 * 			{@link WalletStatus#ACTIVE}
 * 			{@link WalletStatus#NOT_ACTIVE}
 * 			{@link WalletStatus#BLOCKED}
 */
public enum WalletStatus {
	/**
	 * Verified Wallet
	 */
	ACTIVE,
	/**
	 * Unverified Wallet
	 */
	NOT_ACTIVE,
	/**
	 * Blocked Wallet
	 */
	BLOCKED}
