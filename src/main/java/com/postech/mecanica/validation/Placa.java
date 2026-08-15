package com.postech.mecanica.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PlacaValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Placa {

    String message() default "Placa inválida. Use o formato antigo (ABC1234) ou Mercosul (ABC1D23)";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}