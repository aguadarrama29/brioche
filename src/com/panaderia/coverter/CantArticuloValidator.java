package com.panaderia.coverter;


import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("cantArticuloValidator")
public class CantArticuloValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if (value == null) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                "Cantidad mínima 1", "Debe ingresar al menos 1"));
        }

        Number cantidad;
        try {
            cantidad = (Number) value; // Puede ser Float, Double, BigDecimal
        } catch (ClassCastException e) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                "Valor inválido", "Debe ingresar un número válido"));
        }

        if (cantidad.floatValue() < 1.0f) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                "Cantidad mínima 1", "Debe ingresar al menos 1"));
        }
    }
}