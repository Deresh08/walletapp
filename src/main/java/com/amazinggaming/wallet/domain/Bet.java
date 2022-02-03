/**
 * 
 */
package com.amazinggaming.wallet.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.AbstractAuditable;

/**
 * @author dereshharry
 * 
 * @apiNote When a bet is created it will be stored here and amount will be
 *          blocked from the user account split between cashAmount and
 *          bonusAmount this will allow us to evenly distribute winning based on
 *          stored odds at the time the bet was taken
 */
@Entity
@Table(name = "bet")
public class Bet extends AbstractAuditable<User,Long>{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	
	/**
	 * event bet on i.e. LFC outright win EPL
	 */
	@Column(name = "event")
	private String event;

	/**
	 * Status of bet
	 * {@link BetStatus}
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "bet_status")
	private BetStatus betStatus;

	@Column(name = "cash_amount")
	private BigDecimal cashAmount;

	@Column(name = "bonus_amount")
	private BigDecimal bonusAmount;

	@Column(name = "odds")
	private BigDecimal odds;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the event
	 */
	public String getEvent() {
		return event;
	}

	/**
	 * @param event the event to set
	 */
	public void setEvent(String event) {
		this.event = event;
	}

	/**
	 * @return the betStatus
	 */
	public BetStatus getBetStatus() {
		return betStatus;
	}

	/**
	 * @param betStatus the betStatus to set
	 */
	public void setBetStatus(BetStatus betStatus) {
		this.betStatus = betStatus;
	}

	/**
	 * @return the cashAmount
	 */
	public BigDecimal getCashAmount() {
		return cashAmount;
	}

	/**
	 * @param cashAmount the cashAmount to set
	 */
	public void setCashAmount(BigDecimal cashAmount) {
		this.cashAmount = cashAmount;
	}

	/**
	 * @return the odds
	 */
	public BigDecimal getOdds() {
		return odds;
	}

	/**
	 * @param odds the odds to set
	 */
	public void setOdds(BigDecimal odds) {
		this.odds = odds;
	}
	
	/**
	 * @return the bonusAmount
	 */
	public BigDecimal getBonusAmount() {
		return bonusAmount;
	}

	/**
	 * @param bonusAmount the bonusAmount to set
	 */
	public void setBonusAmount(BigDecimal bonusAmount) {
		this.bonusAmount = bonusAmount;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Bet [id=");
		builder.append(id);
		builder.append(", event=");
		builder.append(event);
		builder.append(", betStatus=");
		builder.append(betStatus);
		builder.append(", cashAmount=");
		builder.append(cashAmount);
		builder.append(", bonusAmount=");
		builder.append(bonusAmount);
		builder.append(", odds=");
		builder.append(odds);
		builder.append("]");
		return builder.toString();
	}

	
}
