package com.panaderia.modelo.negocio;

public class CatListaInicialId implements java.io.Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -40995445619720015L;
	private String codigopan;
	private int idUbicacion;

	public CatListaInicialId() {
	}

	public String getCodigopan() {
		return this.codigopan;
	}

	public void setCodigopan(String codigopan) {
		this.codigopan = codigopan;
	}

	public int getIdUbicacion() {
		return this.idUbicacion;
	}

	public void setIdUbicacion(int idUbicacion) {
		this.idUbicacion = idUbicacion;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigopan == null) ? 0 : codigopan.hashCode());
		result = prime * result + idUbicacion;
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
		CatListaInicialId other = (CatListaInicialId) obj;
		if (codigopan == null) {
			if (other.codigopan != null)
				return false;
		} else if (!codigopan.equals(other.codigopan))
			return false;
		if (idUbicacion != other.idUbicacion)
			return false;
		return true;
	}
	
	
}
