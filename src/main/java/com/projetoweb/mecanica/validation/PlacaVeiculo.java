package com.projetoweb.mecanica.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PlacaVeiculoValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface PlacaVeiculo {
    
    String message() default "Placa de veículo inválida. Use o formato ABC-1234 ou ABC1D23 (Mercosul)";
    
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
}
