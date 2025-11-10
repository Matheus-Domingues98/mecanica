package com.projetoweb.mecanica.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class PlacaVeiculoValidator implements ConstraintValidator<PlacaVeiculo, String> {
    
    // Padrão antigo: ABC-1234 ou ABC1234
    private static final Pattern PLACA_ANTIGA = Pattern.compile("^[A-Z]{3}-?[0-9]{4}$");
    
    // Padrão Mercosul: ABC1D23 ou ABC-1D23
    private static final Pattern PLACA_MERCOSUL = Pattern.compile("^[A-Z]{3}-?[0-9][A-Z][0-9]{2}$");
    
    @Override
    public void initialize(PlacaVeiculo constraintAnnotation) {
    }
    
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) {
            return false;
        }
        
        String upperValue = value.toUpperCase().trim();
        
        return PLACA_ANTIGA.matcher(upperValue).matches() || 
               PLACA_MERCOSUL.matcher(upperValue).matches();
    }
}
