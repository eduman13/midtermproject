package com.ironhack.midtermproject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ironhack.midtermproject.model.users.AccountHolder;
import com.ironhack.midtermproject.validation.Between;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Entity
@DynamicUpdate
public class CreditCard {

    static final String DEFAULT_CREDIT_LIMIT = "100";
    static final String DEFAULT_INTEREST_RATE = "0.2";
    static final String DEFAULT_PENALTY_FEE = "40";

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    private BigDecimal balance;

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="primary_owner_id")
    private AccountHolder primaryOwner;

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="secondary_owner_id")
    private AccountHolder secondaryOwner;

    @Between(from="100", to="100000", message="creditLimit not in the required range")
    private BigDecimal creditLimit;

    @Between(from="0.1", to="0.2", message="interestRate not in the required range")
    private BigDecimal interestRate;

    private BigDecimal penaltyFee;

    private String moneyType;

    private LocalDate creationDate;

    private String secretKey;

    public CreditCard() {
    }

    public CreditCard(CreditCard creditCard) {
        this.balance = creditCard.getBalance();
        this.penaltyFee = new BigDecimal(DEFAULT_PENALTY_FEE);
        Optional.ofNullable(creditCard.getCreditLimit()).ifPresentOrElse(this::setCreditLimit, () -> this.creditLimit = new BigDecimal(DEFAULT_CREDIT_LIMIT));
        Optional.ofNullable(creditCard.getInterestRate()).ifPresentOrElse(this::setInterestRate, () -> this.interestRate = new BigDecimal(DEFAULT_INTEREST_RATE));
        Optional.ofNullable(creditCard.getMoneyType()).ifPresentOrElse(this::setMoneyType, () -> this.moneyType = "USD");
        this.secretKey = UUID.randomUUID().toString().replace("-", "");
        this.creationDate = LocalDate.now();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @JsonIgnore
    public AccountHolder getPrimaryOwner() {
        return primaryOwner;
    }

    public void setPrimaryOwner(AccountHolder primaryOwner) {
        this.primaryOwner = primaryOwner;
    }

    @JsonIgnore
    public AccountHolder getSecondaryHolder() {
        return secondaryOwner;
    }

    public void setSecondaryHolder(AccountHolder secondaryHolder) {
        this.secondaryOwner = secondaryHolder;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public BigDecimal getPenaltyFee() {
        return penaltyFee;
    }

    public void setPenaltyFee(BigDecimal penaltyFee) {
        this.penaltyFee = penaltyFee;
    }

    public String getMoneyType() {
        return moneyType;
    }

    public void setMoneyType(String moneyType) {
        this.moneyType = moneyType;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public AccountHolder getSecondaryOwner() {
        return secondaryOwner;
    }

    public void setSecondaryOwner(AccountHolder secondaryOwner) {
        this.secondaryOwner = secondaryOwner;
    }
}
