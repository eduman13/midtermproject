package com.ironhack.midtermproject.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;
import java.util.Optional;

public class BetweenValidation implements ConstraintValidator<Between, BigDecimal> {

    private String from;
    private String to;

    @Override
    public void initialize(Between constraintAnnotation) {
        this.from = constraintAnnotation.from();
        this.to = constraintAnnotation.to();
    }

    @Override
    public boolean isValid(BigDecimal bigDecimal, ConstraintValidatorContext constraintValidatorContext) {
        if (Optional.ofNullable(bigDecimal).isEmpty()) {
            return true;
        } else {
            return bigDecimal.compareTo(new BigDecimal(from)) < 0 || bigDecimal.compareTo(new BigDecimal(to)) > 0 ? false : true;
        }
    }
}
