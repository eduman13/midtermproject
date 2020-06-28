package com.ironhack.midtermproject.model.users;

import com.ironhack.midtermproject.model.Checking;
import com.ironhack.midtermproject.model.CreditCard;
import com.ironhack.midtermproject.model.security.User;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class AccountHolder extends User {

    private String name;

    private LocalDate birthday;

    private String primaryAddress;

    private String mailingAddress;

    @OneToOne(mappedBy="primaryOwner", fetch=FetchType.LAZY, optional=false)
    private Checking primaryChecking;

    @OneToOne(mappedBy="secondaryOwner", fetch=FetchType.LAZY)
    private Checking secondaryChecking;

    @OneToOne(mappedBy ="primaryOwner", fetch=FetchType.LAZY, optional=false)
    private CreditCard primarycreditCard;

    @OneToOne(mappedBy ="secondaryOwner", fetch=FetchType.LAZY)
    private CreditCard secondcreditCard;

    public AccountHolder() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthDay(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getPrimaryAddress() {
        return primaryAddress;
    }

    public void setPrimaryAddress(String primaryAddress) {
        this.primaryAddress = primaryAddress;
    }

    public String getMailingAddress() {
        return mailingAddress;
    }

    public void setMailingAddress(String mailingAddress) {
        this.mailingAddress = mailingAddress;
    }

    public Checking getPrimaryChecking() {
        return primaryChecking;
    }

    public void setPrimaryChecking(Checking primaryChecking) {
        this.primaryChecking = primaryChecking;
    }

    public Checking getSecondaryChecking() {
        return secondaryChecking;
    }

    public void setSecondaryChecking(Checking secondaryChecking) {
        this.secondaryChecking = secondaryChecking;
    }

    public CreditCard getPrimarycreditCard() {
        return primarycreditCard;
    }

    public void setPrimarycreditCard(CreditCard primarycreditCard) {
        this.primarycreditCard = primarycreditCard;
    }

    public CreditCard getSecondcreditCard() {
        return secondcreditCard;
    }

    public void setSecondcreditCard(CreditCard secondcreditCard) {
        this.secondcreditCard = secondcreditCard;
    }
}
