package com.panaderia.modelo.bo;

import java.io.Serializable;

import com.panaderia.modelo.negocio.PanXSucursal;

public class VentaPanBO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PanXSucursal pan;
	public int cantidad;
	public int cantidadTest;
	public String codigo;

	public VentaPanBO() {

	}

	public PanXSucursal getPan() {
		return pan;
	}

	public void setPan(PanXSucursal pan) {
		this.pan = pan;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + cantidad;
		result = prime * result + ((pan == null) ? 0 : pan.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VentaPanBO other = (VentaPanBO) obj;
		if (cantidad != other.cantidad)
			return false;
		if (pan == null) {
			if (other.pan != null)
				return false;
		} else if (!pan.equals(other.pan))
			return false;
		return true;
	}

	public int getCantidadTest() {
		return cantidadTest;
	}

	public void setCantidadTest(int cantidadTest) {
		this.cantidadTest = cantidadTest;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

}
