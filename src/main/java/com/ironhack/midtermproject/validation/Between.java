package com.ironhack.midtermproject.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy=BetweenValidation.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Between {

    String message() default "Value not in the required range";

    String from();

    String to();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
