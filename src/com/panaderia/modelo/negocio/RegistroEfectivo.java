package com.panaderia.modelo.negocio;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class RegistroEfectivo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2282525139050923024L;
	
	private int id;
	private String descripcion;
	private BigDecimal monto;
	private CatUsuario catUsuario;
	private int estatus;
	private int tipo;
	private Date fecha;
	private int idSucursal;
	
	
	
	public RegistroEfectivo() {
		
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public BigDecimal getMonto() {
		return monto;
	}
	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}
	public CatUsuario getCatUsuario() {
		return catUsuario;
	}
	public void setCatUsuario(CatUsuario catUsuario) {
		this.catUsuario = catUsuario;
	}
	public int getEstatus() {
		return estatus;
	}
	public void setEstatus(int estatus) {
		this.estatus = estatus;
	}
	public int getTipo() {
		return tipo;
	}
	public void setTipo(int tipo) {
		this.tipo = tipo;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		RegistroEfectivo other = (RegistroEfectivo) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public int getIdSucursal() {
		return idSucursal;
	}

	public void setIdSucursal(int idSucursal) {
		this.idSucursal = idSucursal;
	}

}
