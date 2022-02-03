/**
 * 
 */
package com.amazinggaming.wallet.domain;

/**
 * @author dereshharry
 * 
 * @apiNote This class defines profiles. Roles can be added i.e. BET,
 *          CREATE_CHARGE. Roles and Profiles can be mapped to add or limit usage
 *          by profile
 *          {@link Profile#PLAYER}
 * 			{@link Profile#OPERATOR}
 *
 */
public enum Profile {
	/**
	 * Permissions deposit, withdraw, place/view bet, view balance
	 */
	PLAYER, 
	/**
	 * Permissions carryout win call, read all transactions, view/create/update charges
	 */
	OPERATOR
}
