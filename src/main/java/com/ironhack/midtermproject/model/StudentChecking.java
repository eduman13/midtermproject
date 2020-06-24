package com.ironhack.midtermproject.model;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@DynamicUpdate
@PrimaryKeyJoinColumn(name="studentCheckingId")
public class StudentChecking extends Checking {

    public StudentChecking() {
    }

    public StudentChecking(Checking checking) {
        super(checking.getBalance(), checking.getSecretKey(), checking.getStatus());
        this.creationDate = LocalDate.now();
    }

    public static StudentChecking checkingToStudentChecking(Checking checking) {
        return new StudentChecking(checking);
    }
}
