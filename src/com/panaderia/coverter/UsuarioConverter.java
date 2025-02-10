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

import com.panaderia.modelo.negocio.CatUsuario;
import com.panaderia.servicio.IBriocheServicio;

@ManagedBean(name = "usuarioConverter")
@RequestScoped
public class UsuarioConverter implements Converter {

	@ManagedProperty(value = "#{BriocheServicio}")
	IBriocheServicio iBriocheServicio;

	private static final Logger logger = Logger.getLogger(UsuarioConverter.class);

	@Override
	public Object getAsObject(FacesContext fc, UIComponent component, String value) {
		try {
			if (value != null && value.trim().length() > 0) {
				return getiBriocheServicio().obtenerUsuarioXId(Integer.valueOf(value));
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
			return String.valueOf(((CatUsuario) value).getId());
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
