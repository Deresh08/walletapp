/**
 * 
 */
package com.amazinggaming.wallet.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author dereshharry
 *
 */
@Entity
@Table(name = "charge")
public class Charge {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	/**
	 * CREDIT OR DEBIT
	 */
	@Column(name = "type")
	private String type;

	/**
	 * charge will debit/credit fixed amount i.e. 5 EUR but it can be 0 EUR
	 */
	@Column(name = "fixed_charge")
	private BigDecimal fixedCharge;

	/**
	 * charge will debit/credit percent of amount i.e. 10% of amount
	 */
	@Column(name = "percent_charge")
	private BigDecimal percentCharge;

	/**
	 * if charge is percentage i.e. 10% of amount but we set min charge as 1 eur if
	 * 10% less than 1 Eur
	 */
	@Column(name = "min_charge")
	private BigDecimal minCharge;

	/**
	 * if charge is percentage i.e. 10% of amount but we set max charge as 1000 eur if
	 * 10% more than 1000 Eur
	 */
	@Column(name = "max_charge")
	private BigDecimal maxCharge;

	/**
	 * Charge applies to amount with minimum of 20 euros
	 */
	@Column(name = "min_amount")
	private BigDecimal minAmount;

	/**
	 * Charge applies to amount with max of 30 euros
	 * this allows us to apply different charges in a range of min & max amounts
	 */
	@Column(name = "max_amount")
	private BigDecimal maxAmount;

	/**
	 * User-Friendly name i.e. Christmas Deposit Bonus / New Years Withdrawl Charge
	 */	
	@Column(name = "name")
	private String name;
	
	/**
	 * Transaction Type the charge is applied to
	 */
	@ManyToOne
	@JoinColumn(name = "transaction_type", foreignKey = @ForeignKey(name="charge_trx_type_fk"))
	private TransactionType transactionType;
	
	/**
	 * Wallet Type where charge is applied
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "wallet_type")
	private WalletType applyTowalletType;

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the fixedCharge
	 */
	public BigDecimal getFixedCharge() {
		return fixedCharge;
	}

	/**
	 * value will debit/credit fixed amount
	 * @param fixedCharge the fixedCharge to set
	 */
	public void setFixedCharge(BigDecimal fixedCharge) {
		this.fixedCharge = fixedCharge;
	}

	/**
	 * @return the percentCharge
	 */
	public BigDecimal getPercentCharge() {
		return percentCharge;
	}

	/**
	 * value will debit/credit percent of amount i.e. 10% of amount
	 * @param percentCharge the percentCharge to set
	 */
	public void setPercentCharge(BigDecimal percentCharge) {
		this.percentCharge = percentCharge;
	}

	/**
	 * @return the minCharge
	 */
	public BigDecimal getMinCharge() {
		return minCharge;
	}

	/**
	 * if charge is percentage i.e. 10% of amount but we set min charge as 1 eur if
	 * 10% less than 1 Eur
	 * @param minCharge the minCharge to set
	 */
	public void setMinCharge(BigDecimal minCharge) {
		this.minCharge = minCharge;
	}

	/**
	 * @return the maxCharge
	 */
	public BigDecimal getMaxCharge() {
		return maxCharge;
	}

	/**
	 * if charge is percentage i.e. 10% of amount but we set max charge as 1000 eur if
	 * 10% more than 1000 Eur
	 * @param maxCharge the maxCharge to set
	 */
	public void setMaxCharge(BigDecimal maxCharge) {
		this.maxCharge = maxCharge;
	}

	/**
	 * @return the minAmount
	 */
	public BigDecimal getMinAmount() {
		return minAmount;
	}

	/**
	 * Charge applies to amount i.e. applies from minimum of 20 euros
	 * @param minAmount the minAmount to set
	 */
	public void setMinAmount(BigDecimal minAmount) {
		this.minAmount = minAmount;
	}

	/**
	 * @return the maxAmount
	 */
	public BigDecimal getMaxAmount() {
		return maxAmount;
	}

	/**
	 *  Charge applies to amount i.e. applies from maximum of 2000 euros
	 * @param maxAmount the maxAmount to set
	 */
	public void setMaxAmount(BigDecimal maxAmount) {
		this.maxAmount = maxAmount;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Nmae of the charge
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return the transactionType
	 */
	public TransactionType getTransactionType() {
		return transactionType;
	}

	/**
	 * @param transactionType the transactionType to set
	 */
	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}

	/**
	 * @return the applyTowalletType
	 */
	public WalletType getApplyTowalletType() {
		return applyTowalletType;
	}

	/**
	 * @param applyTowalletType the applyTowalletType to set
	 */
	public void setApplyTowalletType(WalletType applyTowalletType) {
		this.applyTowalletType = applyTowalletType;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Charge [id=");
		builder.append(id);
		builder.append(", type=");
		builder.append(type);
		builder.append(", fixedCharge=");
		builder.append(fixedCharge);
		builder.append(", percentCharge=");
		builder.append(percentCharge);
		builder.append(", minCharge=");
		builder.append(minCharge);
		builder.append(", maxCharge=");
		builder.append(maxCharge);
		builder.append(", minAmount=");
		builder.append(minAmount);
		builder.append(", maxAmount=");
		builder.append(maxAmount);
		builder.append(", name=");
		builder.append(name);
		builder.append("]");
		return builder.toString();
	}
}
