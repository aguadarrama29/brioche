package com.panaderia.manageBan;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import com.panaderia.modelo.negocio.CatUbicacion;
import com.panaderia.seguridad.UserSession;
import com.panaderia.servicio.IBriocheServicio;
import com.panaderia.util.MsgUtil;



@ManagedBean(name = "ubicacionMB")
@ViewScoped
public class UbicacionMB implements Serializable{

	
	private static final long serialVersionUID = -1349929870950879266L;
	@ManagedProperty(value = "#{BriocheServicio}")
	IBriocheServicio iBriocheServicio;
	@ManagedProperty(value = "#{userSession}")
	UserSession userSession;

	
	private CatUbicacion ubicacion;
	private List<CatUbicacion> lista;
	private List<CatUbicacion> listaFilter;
	private CatUbicacion slcubicacion;
	private boolean estatus=true;
	

	public UbicacionMB() {

	}

	@PostConstruct
	public void inicio() {
		ubicacion = new CatUbicacion();

		lista = getiBriocheServicio().obtenerUbicaciones();
	}

	public void guardar() {

		try {
			ubicacion.setDescripcion(ubicacion.getDescripcion().toUpperCase());
			if (ubicacion.getId() == 0) {
				ubicacion.setEstatus(estatus);
				getiBriocheServicio().guardarUbicacion(ubicacion);

				lista.add(ubicacion);

				MsgUtil.msgInfo("Exito!", "Registro Guardado.");
			} else {
				
				getiBriocheServicio().actualizarUbicacion(ubicacion);
				MsgUtil.msgInfo("Exito!", "Registro Actualizado.");
				
			}

			ubicacion = new CatUbicacion();

		} catch (Exception e) {
			e.printStackTrace();
			MsgUtil.msgError("Error!", "Registro no procesado.");
		}

	}

	public void editar() {
		ubicacion = slcubicacion;
		estatus=slcubicacion.getEstatus();
	}

	public void eliminar() {
		try {
			getiBriocheServicio().eliminarUbicacion(slcubicacion);
			lista.remove(slcubicacion);
			ubicacion = new CatUbicacion();
			MsgUtil.msgInfo("Exito!", "Registro Eliminado.");
		} catch (Exception e) {
			e.printStackTrace();
			MsgUtil.msgError("Error!", "Registro no eliminado.");
		}
	}

	public void cancelar() {
		ubicacion = new CatUbicacion();
		slcubicacion = null;
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

	

	public List<CatUbicacion> getLista() {
		return lista;
	}

	public void setLista(List<CatUbicacion> lista) {
		this.lista = lista;
	}

	public List<CatUbicacion> getListaFilter() {
		return listaFilter;
	}

	public void setListaFilter(List<CatUbicacion> listaFilter) {
		this.listaFilter = listaFilter;
	}

	public CatUbicacion getSlcubicacion() {
		return slcubicacion;
	}

	public void setSlcubicacion(CatUbicacion slcubicacion) {
		this.slcubicacion = slcubicacion;
	}

	public CatUbicacion getubicacion() {
		return ubicacion;
	}

	public void setubicacion(CatUbicacion ubicacion) {
		this.ubicacion = ubicacion;
	}

	public boolean isEstatus() {
		return estatus;
	}

	public void setEstatus(boolean estatus) {
		this.estatus = estatus;
	}
	

}
