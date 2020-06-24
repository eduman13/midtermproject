package com.ironhack.midtermproject.model;

import com.ironhack.midtermproject.validation.Between;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.validation.constraints.DecimalMax;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Entity
@DynamicUpdate
@PrimaryKeyJoinColumn(name="savingId")
public class Saving extends Checking {

    static final String DEFAULT_INTEREST_RATE = "0.0025";
    static final String DEFAULT_MINIMUM_BALANCE = "1000";

    @DecimalMax(value="0.5")
    private BigDecimal interestRate;

    @Between(from="100", to="1000", message="minimumBalance not in the required range")
    private BigDecimal minimumBalance;

    public Saving() {
    }

    public Saving(Saving saving) {
        super(saving.getBalance(), saving.getSecretKey(), saving.getStatus());
        Optional.ofNullable(saving.getInterestRate()).ifPresentOrElse(this::setInterestRate, () -> setInterestRate(new BigDecimal(DEFAULT_INTEREST_RATE)));
        Optional.ofNullable(saving.getMinimumBalance()).ifPresentOrElse(this::setMinimumBalance, () -> setMinimumBalance(new BigDecimal(DEFAULT_MINIMUM_BALANCE)));
        this.creationDate = LocalDate.now();
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    @Override
    public BigDecimal getMinimumBalance() {
        return minimumBalance;
    }

    @Override
    public void setMinimumBalance(BigDecimal minimumBalance) {
        this.minimumBalance = minimumBalance;
    }
}
