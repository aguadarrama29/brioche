package com.panaderia.coverter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.math.BigDecimal;

@FacesConverter("zeroToEmptyConverter")
public class ZeroToEmptyConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.trim().isEmpty()) {
            return BigDecimal.ZERO; // Si está vacío, lo tratamos como 0
        }
        try {
            return new BigDecimal(value.trim());
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return "";
        }
        BigDecimal val = (BigDecimal) value;
        if (val.compareTo(BigDecimal.ZERO) == 0) {
            return ""; // Mostrar vacío si es 0
        }
        return val.toPlainString();
    }
}
