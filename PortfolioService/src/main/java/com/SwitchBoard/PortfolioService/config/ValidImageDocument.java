package com.SwitchBoard.PortfolioService.config;

import jakarta.validation.Payload;

import jakarta.validation.Constraint;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ImageDocumenttypeValidator.class)
@Documented
public @interface ValidImageDocument {

    String message() default "Invalid image type. Allowed types are JPG, JPEG, PNG and PDF.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
