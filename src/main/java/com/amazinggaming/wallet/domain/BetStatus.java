/**
 * 
 */
package com.amazinggaming.wallet.domain;

/**
 * @author dereshharry
 * 
 * {@link BetStatus#WINNING}
 * {@link BetStatus#LOSING}
 * {@link BetStatus#NO_RESULT}
 *
 */
public enum BetStatus {
	
	/**
	 * Winning bet
	 */
	WINNING, 
	
	/**
	 * Losing bet
	 */
	LOSING, 
	
	/**
	 * cancelled event i.e. cancelled football match
	 */
	NO_RESULT,
	
	/**
	 * pending event 
	 */
	PENDING}
