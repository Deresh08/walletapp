package com.amazinggaming.wallet.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author dereshharry
 * 
 *         Used for Bet related methods
 *
 */
@JsonInclude(value = Include.NON_NULL)
public class BetDto extends BaseDto {

	private long betId;

	private BigDecimal odds;

	private String event;

	/**
	 * event description
	 * 
	 * @return the event
	 */
	public String getEvent() {
		return event;
	}

	/**
	 * event description
	 * 
	 * @param event the event to set
	 */
	public void setEvent(String event) {
		this.event = event;
	}

	/**
	 * 
	 * id of the placed bet
	 * 
	 * @return the betId
	 */
	public long getBetId() {
		return betId;
	}

	/**
	 * id of the placed bet
	 * 
	 * @param betId the betId to set
	 */
	public void setBetId(long betId) {
		this.betId = betId;
	}

	/**
	 * betting odds
	 * 
	 * @return the odds
	 */
	public BigDecimal getOdds() {
		return odds;
	}

	/**
	 * betting odds
	 * 
	 * @param odds the odds to set
	 */
	public void setOdds(BigDecimal odds) {
		this.odds = odds;
	}
}
