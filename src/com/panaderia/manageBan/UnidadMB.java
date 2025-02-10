package com.panaderia.manageBan;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import com.panaderia.modelo.negocio.CatUnidadMedida;
import com.panaderia.seguridad.UserSession;
import com.panaderia.servicio.IBriocheServicio;
import com.panaderia.util.MsgUtil;



@ManagedBean(name = "unidadMB")
@ViewScoped
public class UnidadMB implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6861957799242792537L;
	@ManagedProperty(value = "#{BriocheServicio}")
	IBriocheServicio iBriocheServicio;
	@ManagedProperty(value = "#{userSession}")
	UserSession userSession;

	
	private CatUnidadMedida unidad;
	private List<CatUnidadMedida> lista;
	private List<CatUnidadMedida> listaFilter;
	private CatUnidadMedida slcunidad;
	private boolean estatus=true;
	

	public UnidadMB() {

	}

	@PostConstruct
	public void inicio() {
		unidad = new CatUnidadMedida();

		lista = getiBriocheServicio().obtenerUnidades();
	}

	public void guardar() {

		try {

			if (unidad.getId() == 0) {
				unidad.setEstatus(estatus);
				getiBriocheServicio().guardarUnidad(unidad);

				lista.add(unidad);

				MsgUtil.msgInfo("Exito!", "Registro Guardado.");
			} else {
				
				getiBriocheServicio().actualizarUnidad(unidad);
				MsgUtil.msgInfo("Exito!", "Registro Actualizado.");
				
			}

			unidad = new CatUnidadMedida();

		} catch (Exception e) {
			e.printStackTrace();
			MsgUtil.msgError("Error!", "Registro no procesado.");
		}

	}

	public void editar() {
		unidad = slcunidad;
		estatus=slcunidad.isEstatus();
	}

	public void eliminar() {
		try {
			getiBriocheServicio().eliminarUnidad(slcunidad);
			lista.remove(slcunidad);
			unidad = new CatUnidadMedida();
			MsgUtil.msgInfo("Exito!", "Registro Eliminado.");
		} catch (Exception e) {
			e.printStackTrace();
			MsgUtil.msgError("Error!", "Registro no eliminado.");
		}
	}

	public void cancelar() {
		unidad = new CatUnidadMedida();
		slcunidad = null;
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

	

	public List<CatUnidadMedida> getLista() {
		return lista;
	}

	public void setLista(List<CatUnidadMedida> lista) {
		this.lista = lista;
	}

	public List<CatUnidadMedida> getListaFilter() {
		return listaFilter;
	}

	public void setListaFilter(List<CatUnidadMedida> listaFilter) {
		this.listaFilter = listaFilter;
	}

	public CatUnidadMedida getSlcunidad() {
		return slcunidad;
	}

	public void setSlcunidad(CatUnidadMedida slcunidad) {
		this.slcunidad = slcunidad;
	}

	public CatUnidadMedida getunidad() {
		return unidad;
	}

	public void setunidad(CatUnidadMedida unidad) {
		this.unidad = unidad;
	}

	public boolean isEstatus() {
		return estatus;
	}

	public void setEstatus(boolean estatus) {
		this.estatus = estatus;
	}
	

}
