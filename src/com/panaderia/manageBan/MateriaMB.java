package com.panaderia.manageBan;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import com.panaderia.modelo.negocio.CatMateriaPrima;
import com.panaderia.modelo.negocio.CatProveedor;
import com.panaderia.modelo.negocio.CatUbicacion;
import com.panaderia.modelo.negocio.CatUnidadMedida;
import com.panaderia.seguridad.UserSession;
import com.panaderia.servicio.IBriocheServicio;
import com.panaderia.util.MsgUtil;


@ManagedBean(name = "materiaMB")
@ViewScoped
public class MateriaMB implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6119635364189931220L;
	@ManagedProperty(value = "#{BriocheServicio}")
	IBriocheServicio iBriocheServicio;
	@ManagedProperty(value = "#{userSession}")
	UserSession userSession;

	
	private CatMateriaPrima materia;
	private List<CatMateriaPrima> lista;
	private List<CatMateriaPrima> listaFilter;
	private CatMateriaPrima slcmateria;
	private boolean estatus=true;
	
	//para autocomplementable
	private CatUnidadMedida catUnidadMedida;
	private CatProveedor catProveedor;
	private CatUbicacion catUbicacion;

	public MateriaMB() {

	}

	@PostConstruct
	public void inicio() {
		materia = new CatMateriaPrima();

		lista = getiBriocheServicio().obtenerMaterias();
	}

	public void guardar() {

		try {

			
			if (materia.getId() == 0) {
				materia.setCatUbicacion(catUbicacion);
				materia.setCatProveedor(catProveedor);
				materia.setCatUnidadMedida(catUnidadMedida);
				materia.setEstatus(estatus);
				getiBriocheServicio().guardarMateria(materia);

				lista.add(materia);
				
				MsgUtil.msgInfo("Exito!", "Registro Guardado.");
			} else {
				
				getiBriocheServicio().actualizarMateria(materia);
				MsgUtil.msgInfo("Exito!", "Registro Actualizado.");
				
			}

			materia = new CatMateriaPrima();
			cancelar();
		} catch (Exception e) {
			e.printStackTrace();
			MsgUtil.msgError("Error!", "Registro no procesado.");
		}

	}

	public void editar() {
		materia = slcmateria;
		estatus=slcmateria.isEstatus();
		catProveedor=slcmateria.getCatProveedor();
		catUbicacion=slcmateria.getCatUbicacion();
		catUnidadMedida=slcmateria.getCatUnidadMedida();
	}

	public void eliminar() {
		try {
			getiBriocheServicio().eliminarMateria(slcmateria);
			lista.remove(slcmateria);
			materia = new CatMateriaPrima();
			MsgUtil.msgInfo("Exito!", "Registro Eliminado.");
		} catch (Exception e) {
			e.printStackTrace();
			MsgUtil.msgError("Error!", "Registro no eliminado.");
		}
	}

	public void cancelar() {
		materia = new CatMateriaPrima();
		slcmateria = null;
		estatus=false;
		catUnidadMedida=null;
		catProveedor=null;
		catUbicacion=null;
	}
	
	
	//obtener unidades medida complete
		public List<CatUnidadMedida> completeUnidades(String query) {
			List<CatUnidadMedida> articulos = new ArrayList<CatUnidadMedida>();
			articulos=getiBriocheServicio().obtenerUnidades();
			
			List<CatUnidadMedida> filtrados = new ArrayList<CatUnidadMedida>();

			for (int i = 0; i < articulos.size(); i++) {
				CatUnidadMedida c = articulos.get(i);
				if (c.getDescripcion() != null) {
					if (c.getDescripcion().toUpperCase().contains(query.toUpperCase())) {
						filtrados.add(c);
					}
				}
			}
			return filtrados;
		}

		
		
		//obtener proveedores complete
		public List<CatProveedor> completeProveedores(String query) {
			List<CatProveedor> provee = new ArrayList<CatProveedor>();
			provee=getiBriocheServicio().obtenerProveedores();
			
			List<CatProveedor> filtrados = new ArrayList<CatProveedor>();

			for (int i = 0; i < provee.size(); i++) {
				CatProveedor c = provee.get(i);
				if (c.getDescripcion() != null) {
					if (c.getDescripcion().toUpperCase().contains(query.toUpperCase())) {
						filtrados.add(c);
					}
				}
			}
			return filtrados;
		}
		
		
		//obtener proveedores complete
		public List<CatUbicacion> completeUbicaciones(String query) {
			List<CatUbicacion> ubica = new ArrayList<CatUbicacion>();
			ubica=getiBriocheServicio().obtenerUbicaciones();
			
			List<CatUbicacion> filtrados = new ArrayList<CatUbicacion>();

			for (int i = 0; i < ubica.size(); i++) {
				CatUbicacion c = ubica.get(i);
				if (c.getDescripcion() != null) {
					if (c.getDescripcion().toUpperCase().contains(query.toUpperCase())) {
						filtrados.add(c);
					}
				}
			}
			return filtrados;
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

	

	public List<CatMateriaPrima> getLista() {
		return lista;
	}

	public void setLista(List<CatMateriaPrima> lista) {
		this.lista = lista;
	}

	public List<CatMateriaPrima> getListaFilter() {
		return listaFilter;
	}

	public void setListaFilter(List<CatMateriaPrima> listaFilter) {
		this.listaFilter = listaFilter;
	}

	public CatMateriaPrima getSlcmateria() {
		return slcmateria;
	}

	public void setSlcmateria(CatMateriaPrima slcmateria) {
		this.slcmateria = slcmateria;
	}

	public CatMateriaPrima getmateria() {
		return materia;
	}

	public void setmateria(CatMateriaPrima materia) {
		this.materia = materia;
	}

	public boolean isEstatus() {
		return estatus;
	}

	public void setEstatus(boolean estatus) {
		this.estatus = estatus;
	}

	public CatMateriaPrima getMateria() {
		return materia;
	}

	public void setMateria(CatMateriaPrima materia) {
		this.materia = materia;
	}

	public CatUnidadMedida getCatUnidadMedida() {
		return catUnidadMedida;
	}

	public void setCatUnidadMedida(CatUnidadMedida catUnidadMedida) {
		this.catUnidadMedida = catUnidadMedida;
	}

	public CatProveedor getCatProveedor() {
		return catProveedor;
	}

	public void setCatProveedor(CatProveedor catProveedor) {
		this.catProveedor = catProveedor;
	}

	public CatUbicacion getCatUbicacion() {
		return catUbicacion;
	}

	public void setCatUbicacion(CatUbicacion catUbicacion) {
		this.catUbicacion = catUbicacion;
	}
	

}
