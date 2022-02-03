/**
 * 
 */
package com.amazinggaming.wallet.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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

/**
 * @author Deresh Harry
 * @apiNote Holds all account balances and links to User.
 */
@Entity
@Table(name = "wallet")
public class Wallet {

	/**
	 * The primary key
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	/**
	 * Unique walletNumber
	 */
	@Column(name = "wallet_number")
	private String walletNumber;
	/**
	 * User the wallet belongs to
	 */
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	/**
	 * Balance of wallet
	 */
	@Column(name = "balance")
	private BigDecimal balance;

	/**
	 * Status of wallet
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "wallet_status")
	private WalletStatus walletStatus;
	
	/**
	 * Type of wallet
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "wallet_type")
	private WalletType walletType;
	
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
	 * @return the walletNumber
	 */
	public String getWalletNumber() {
		return walletNumber;
	}

	/**
	 * @param walletNumber the walletNumber to set
	 */
	public void setWalletNumber(String walletNumber) {
		this.walletNumber = walletNumber;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return the balance
	 */
	public BigDecimal getBalance() {
		return balance;
	}

	/**
	 * @param balance the balance to set
	 */
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	/**
	 * @return the walletStatus
	 */
	public WalletStatus getWalletStatus() {
		return walletStatus;
	}

	/**
	 * @param walletStatus the walletStatus to set
	 */
	public void setWalletStatus(WalletStatus walletStatus) {
		this.walletStatus = walletStatus;
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
	 * @return the walletType
	 */
	public WalletType getWalletType() {
		return walletType;
	}

	/**
	 * @param walletType the walletType to set
	 */
	public void setWalletType(WalletType walletType) {
		this.walletType = walletType;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Wallet [id=");
		builder.append(id);
		builder.append(", walletNumber=");
		builder.append(walletNumber);
		builder.append(", user=");
		builder.append(user);
		builder.append(", balance=");
		builder.append(balance);
		builder.append(", walletStatus=");
		builder.append(walletStatus);
		builder.append(", walletType=");
		builder.append(walletType);
		builder.append(", createDate=");
		builder.append(createDate);
		builder.append(", modifyDate=");
		builder.append(modifyDate);
		builder.append("]");
		return builder.toString();
	}

}
