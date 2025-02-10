package com.panaderia.modelo.bo;

import java.io.Serializable;
import java.math.BigDecimal;

public class VentaBO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String folio;
	private BigDecimal total;

	public VentaBO() {

	}

	public String getFolio() {
		return folio;
	}

	public void setFolio(String folio) {
		this.folio = folio;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

}
