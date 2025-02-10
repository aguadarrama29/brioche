package com.panaderia.coverter;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.apache.log4j.Logger;

import com.panaderia.modelo.negocio.CatProveedor;
import com.panaderia.servicio.IBriocheServicio;

@ManagedBean(name = "proveedorConverter")
@RequestScoped
public class ProveedoresConverter implements Converter {

	@ManagedProperty(value = "#{BriocheServicio}")
	IBriocheServicio iBriocheServicio;

	private static final Logger logger = Logger.getLogger(ProveedoresConverter.class);

	@Override
	public Object getAsObject(FacesContext fc, UIComponent component, String value) {
		try {
			if (value != null && value.trim().length() > 0) {
				return getiBriocheServicio().obtenerProveedorXId(Integer.valueOf(value));
			} else {
				return null;
			}
		} catch (Exception e) {
			StringWriter stack = new StringWriter();
			e.printStackTrace(new PrintWriter(stack));
			logger.error(stack.toString());
			return null;
		}
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			return String.valueOf(((CatProveedor) value).getId());
		} else {
			return null;
		}
	}

	public IBriocheServicio getiBriocheServicio() {
		return iBriocheServicio;
	}

	public void setiBriocheServicio(IBriocheServicio iBriocheServicio) {
		this.iBriocheServicio = iBriocheServicio;
	}

	

}
