package com.ironhack.midtermproject.model;

import com.ironhack.midtermproject.model.enums.Status;
import com.ironhack.midtermproject.model.users.AccountHolder;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Entity
@DynamicUpdate
@Inheritance(strategy=InheritanceType.JOINED)
public class Checking {

    static final String DEFAULT_MINIMUM_BALANCE = "250";
    static final String DEFAULT_MONTHLY_MAINTENANCE_FEE = "12";
    static final String DEFAULT_PENALTY_FEE = "40";

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    protected Integer id;

    protected BigDecimal balance;

    protected String secretKey;

    protected String moneyType;

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="primary_owner_id")
    protected AccountHolder primaryOwner;

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="secondary_owner_id")
    protected AccountHolder secondaryOwner;

    private BigDecimal minimumBalance;

    private BigDecimal penaltyFee;

    private BigDecimal monthlyMaintenanceFee;

    @Enumerated(EnumType.STRING)
    protected Status status;

    protected LocalDate creationDate;

    public Checking() {
    }

    public Checking(Checking checking) {
        this.balance = checking.getBalance();
        this.secretKey = UUID.randomUUID().toString().replace("-", "");
        this.status = checking.getStatus();
        this.monthlyMaintenanceFee = new BigDecimal(DEFAULT_MONTHLY_MAINTENANCE_FEE);
        this.penaltyFee = new BigDecimal(DEFAULT_PENALTY_FEE);
        Optional.ofNullable(checking.getMinimumBalance()).ifPresentOrElse(this::setMinimumBalance, () -> this.minimumBalance = new BigDecimal(DEFAULT_MINIMUM_BALANCE));
        Optional.ofNullable(checking.getMoneyType()).ifPresentOrElse(this::setMoneyType, () -> this.moneyType = "USD");
        this.creationDate = LocalDate.now();
    }

    public Checking(BigDecimal balance, String secretKey, Status status) {
        this.balance = balance;
        this.secretKey = UUID.randomUUID().toString().replace("-", "");
        this.status = status;
        this.penaltyFee = new BigDecimal("40");
        this.moneyType = "USD";
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

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public AccountHolder getPrimaryOwner() {
        return primaryOwner;
    }

    public void setPrimaryOwner(AccountHolder primaryOwner) {
        this.primaryOwner = primaryOwner;
    }

    public AccountHolder getSecondaryOwner() {
        return secondaryOwner;
    }

    public void setSecondaryOwner(AccountHolder secondaryOwner) {
        this.secondaryOwner = secondaryOwner;
    }

    public BigDecimal getMinimumBalance() {
        return minimumBalance;
    }

    public void setMinimumBalance(BigDecimal minimumBalance) {
        this.minimumBalance = minimumBalance;
    }

    public BigDecimal getMonthlyMaintenanceFee() {
        return monthlyMaintenanceFee;
    }

    public void setMonthlyMaintenanceFee(BigDecimal monthlyMaintenanceFee) {
        this.monthlyMaintenanceFee = monthlyMaintenanceFee;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
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
}
