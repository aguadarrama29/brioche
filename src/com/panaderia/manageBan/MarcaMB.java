package com.panaderia.manageBan;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import com.panaderia.modelo.negocio.CatMarca;
import com.panaderia.seguridad.UserSession;
import com.panaderia.servicio.IBriocheServicio;
import com.panaderia.util.MsgUtil;



@ManagedBean(name = "marcaMB")
@ViewScoped
public class MarcaMB implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2395072936818096911L;
	@ManagedProperty(value = "#{BriocheServicio}")
	IBriocheServicio iBriocheServicio;
	@ManagedProperty(value = "#{userSession}")
	UserSession userSession;

	
	private CatMarca marca;
	private List<CatMarca> lista;
	private List<CatMarca> listaFilter;
	private CatMarca slcmarca;
	private boolean estatus;
	

	public MarcaMB() {

	}

	@PostConstruct
	public void inicio() {
		marca = new CatMarca();

		lista = getiBriocheServicio().obtenerMarcas();
	}

	public void guardar() {

		try {

			if (marca.getId() == 0) {
				marca.setEstatus(estatus);
				getiBriocheServicio().guardarMarca(marca);

				lista.add(marca);

				MsgUtil.msgInfo("Exito!", "Registro Guardado.");
			} else {
				
				getiBriocheServicio().actualizarMarca(marca);
				MsgUtil.msgInfo("Exito!", "Registro Actualizado.");
				
			}

			marca = new CatMarca();

		} catch (Exception e) {
			e.printStackTrace();
			MsgUtil.msgError("Error!", "Registro no procesado.");
		}

	}

	public void editar() {
		marca = slcmarca;
	}

	public void eliminar() {
		try {
			getiBriocheServicio().eliminarMarca(slcmarca);
			lista.remove(slcmarca);
			marca = new CatMarca();
			MsgUtil.msgInfo("Exito!", "Registro Eliminado.");
		} catch (Exception e) {
			e.printStackTrace();
			MsgUtil.msgError("Error!", "Registro no eliminado.");
		}
	}

	public void cancelar() {
		marca = new CatMarca();
		slcmarca = null;
		estatus=false;
	}

	public IBriocheServicio getiBriocheServicio() {
		return iBriocheServicio;
	}

	public void setiBriocheServicio(IBriocheServicio iBriocheServicio) {
		this.iBriocheServicio = iBriocheServicio;
	}

	public UserSession getUserSession() {
		return userSession;
	}

	public void setUserSession(UserSession userSession) {
		this.userSession = userSession;
	}

	public CatMarca getmarca() {
		return marca;
	}

	public void setmarca(CatMarca marca) {
		this.marca = marca;
	}

	public List<CatMarca> getLista() {
		return lista;
	}

	public void setLista(List<CatMarca> lista) {
		this.lista = lista;
	}

	public List<CatMarca> getListaFilter() {
		return listaFilter;
	}

	public void setListaFilter(List<CatMarca> listaFilter) {
		this.listaFilter = listaFilter;
	}

	public CatMarca getSlcmarca() {
		return slcmarca;
	}

	public void setSlcmarca(CatMarca slcmarca) {
		this.slcmarca = slcmarca;
	}

	public CatMarca getMarca() {
		return marca;
	}

	public void setMarca(CatMarca marca) {
		this.marca = marca;
	}

	public boolean isEstatus() {
		return estatus;
	}

	public void setEstatus(boolean estatus) {
		this.estatus = estatus;
	}
	

}
