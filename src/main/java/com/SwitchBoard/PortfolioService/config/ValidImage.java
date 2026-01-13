package com.SwitchBoard.PortfolioService.config;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ImageTypeValidator.class)
@Documented
public @interface ValidImage {

    String message() default "Invalid image type. Allowed types are JPG, JPEG, and PNG.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

