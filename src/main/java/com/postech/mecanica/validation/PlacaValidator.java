package com.postech.mecanica.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class PlacaValidator implements ConstraintValidator<Placa, String> {

    // Padrão antigo: ABC1234
    private static final Pattern PADRAO_ANTIGO = Pattern.compile("^[A-Z]{3}[0-9]{4}$");

    // Padrão Mercosul: ABC1D23
    private static final Pattern PADRAO_MERCOSUL = Pattern.compile("^[A-Z]{3}[0-9][A-Z][0-9]{2}$");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return false;
        }

        String placa = value.trim().toUpperCase().replace("-", "");

        return PADRAO_ANTIGO.matcher(placa).matches()
                || PADRAO_MERCOSUL.matcher(placa).matches();
    }
}