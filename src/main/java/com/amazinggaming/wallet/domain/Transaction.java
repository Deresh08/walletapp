/**
 * 
 */
package com.amazinggaming.wallet.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author dereshharry
 */
@Entity
@Table(name = "transaction")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class Transaction {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	/**
	 * Description of transaction i.e cash winnings for LFC vs MUFC
	 */
	@Column(name = "description")
	private String description;
	
	/**
	 * Credit for deposit/bet winning/bonus charge
	 * Debit for withdrawl/withdrawl chage 
	 */
	@Column(name = "amount")
	private BigDecimal amount;
	
	/**
	 * Credit for deposit/bet winning/bonus charge
	 * Debit for withdrawl/withdrawl chage 
	 */
	@Column(name = "external_id", unique = true)
	private String externalId;
	
	/**
	 * Debit/Credit for wallet(BONUS or CASH ACCOUNT)
	 */
	@ManyToOne(targetEntity = Wallet.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "wallet_id", nullable = false, foreignKey = @ForeignKey(name="trx_wallet_fk"))
	private Wallet wallet;
	
	/**
	 * Credit Apple Pay(deposit)/ Debit Apple Pay(Withdrawl)
	 */
	@ManyToOne
	@JoinColumn(name="transaction_type", nullable= false, foreignKey = @ForeignKey(name="trx_type_fk"))
	private TransactionType transactionType;
	
	
	@ManyToOne(targetEntity = Transaction.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_transaction_id", foreignKey = @ForeignKey(name="parent_trx_fk"), nullable = true)
	private Transaction parentTransaction;
	
	/**
	 * Creation of wallet
	 */
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_date")
	private Date createDate;

	/**
	 * Modified of wallet
	 */
	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modify_date")
	private Date modifyDate;

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
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the wallet
	 */
	public Wallet getWallet() {
		return wallet;
	}

	/**
	 * @param wallet the wallet to set
	 */
	public void setWallet(Wallet wallet) {
		this.wallet = wallet;
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
	 * @return the amount
	 */
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	/**
	 * @return the createDate
	 */
	public Date getCreateDate() {
		return createDate;
	}

	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	/**
	 * @return the modifyDate
	 */
	public Date getModifyDate() {
		return modifyDate;
	}

	/**
	 * @param modifyDate the modifyDate to set
	 */
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
	
	/**
	 * @return the parentTransaction
	 */
	public Transaction getParentTransaction() {
		return parentTransaction;
	}

	/**
	 * @param parentTransaction the parentTransaction to set
	 */
	public void setParentTransaction(Transaction parentTransaction) {
		this.parentTransaction = parentTransaction;
	}
	
	/**
	 * @return the externalId
	 */
	public String getExternalId() {
		return externalId;
	}

	/**
	 * @param externalId the externalId to set
	 */
	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Transaction [id=");
		builder.append(id);
		builder.append(", description=");
		builder.append(description);
		builder.append(", amount=");
		builder.append(amount);
		builder.append(", externalId=");
		builder.append(externalId);
		builder.append(", wallet=");
		builder.append(wallet);
		builder.append(", transactionType=");
		builder.append(transactionType);
		builder.append(", parentTransaction=");
		builder.append(parentTransaction);
		builder.append(", createDate=");
		builder.append(createDate);
		builder.append(", modifyDate=");
		builder.append(modifyDate);
		builder.append("]");
		return builder.toString();
	}

		
}
