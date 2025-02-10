package com.panaderia.manageBan;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import com.panaderia.modelo.negocio.CatMarca;
import com.panaderia.modelo.negocio.CatPerfil;
import com.panaderia.modelo.negocio.CatProveedor;
import com.panaderia.seguridad.UserSession;
import com.panaderia.servicio.IBriocheServicio;
import com.panaderia.util.MsgUtil;



@ManagedBean(name = "proveedorMB")
@ViewScoped
public class ProveedorMB implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 959107711113516850L;
	@ManagedProperty(value = "#{BriocheServicio}")
	IBriocheServicio iBriocheServicio;
	@ManagedProperty(value = "#{userSession}")
	UserSession userSession;

	
	private CatProveedor proveedor;
	private List<CatProveedor> lista;
	private List<CatProveedor> listaFilter;
	private CatProveedor slcproveedor;
	
	boolean estatus=true;
	private String doMarca;
	List<CatMarca> listaMarcas;
	
	private int accion=1; //para guardar 2 para actualizar

	public ProveedorMB() {

	}

	@PostConstruct
	public void inicio() {
		proveedor = new CatProveedor();

		lista = getiBriocheServicio().obtenerProveedores();
	}

	public void guardar() {

		try {

			if (proveedor.getId() == 0) {
				
				proveedor.setCatMarca(getiBriocheServicio().obtenerMarcaXId(Integer.parseInt(doMarca)));
				getiBriocheServicio().guardarProveedor(proveedor);

				lista.add(proveedor);

				MsgUtil.msgInfo("Exito!", "Registro Guardado.");
			} else {
				proveedor.setCatMarca(getiBriocheServicio().obtenerMarcaXId(Integer.parseInt(doMarca)));
				getiBriocheServicio().actualizarProveedor(proveedor);
				MsgUtil.msgInfo("Exito!", "Registro Actualizado.");
				
			}

			proveedor = new CatProveedor();

		} catch (Exception e) {
			e.printStackTrace();
			MsgUtil.msgError("Error!", "Registro no procesado.");
		}

	}

	public void editar() {
		proveedor = slcproveedor;
		String cadena = Integer.toString(proveedor.getCatMarca().getId());
		doMarca=cadena;
	}

	public void eliminar() {
		try {
			getiBriocheServicio().eliminarProveedor(slcproveedor);
			lista.remove(slcproveedor);
			proveedor = new CatProveedor();
			MsgUtil.msgInfo("Exito!", "Registro Eliminado.");
		} catch (Exception e) {
			e.printStackTrace();
			MsgUtil.msgError("Error!", "Registro no eliminado.");
		}
	}

	public void cancelar() {
		proveedor = new CatProveedor();
		slcproveedor = null;
		estatus=false;
	}
	
	public List<CatMarca> getlistaMarca() {
		return getiBriocheServicio().obtenerMarcas();
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

	public CatProveedor getproveedor() {
		return proveedor;
	}

	public void setproveedor(CatProveedor proveedor) {
		this.proveedor = proveedor;
	}

	public List<CatProveedor> getLista() {
		return lista;
	}

	public void setLista(List<CatProveedor> lista) {
		this.lista = lista;
	}

	public List<CatProveedor> getListaFilter() {
		return listaFilter;
	}

	public void setListaFilter(List<CatProveedor> listaFilter) {
		this.listaFilter = listaFilter;
	}

	public CatProveedor getSlcproveedor() {
		return slcproveedor;
	}

	public void setSlcproveedor(CatProveedor slcproveedor) {
		this.slcproveedor = slcproveedor;
	}

	public String getDoMarca() {
		return doMarca;
	}

	public void setDoMarca(String doMarca) {
		this.doMarca = doMarca;
	}

	public List<CatMarca> getListaMarcas() {
		return listaMarcas;
	}

	public void setListaMarcas(List<CatMarca> listaMarcas) {
		this.listaMarcas = listaMarcas;
	}

	public CatProveedor getProveedor() {
		return proveedor;
	}

	public void setProveedor(CatProveedor proveedor) {
		this.proveedor = proveedor;
	}

	public boolean isEstatus() {
		return estatus;
	}

	public void setEstatus(boolean estatus) {
		this.estatus = estatus;
	}

	public int getAccion() {
		return accion;
	}

	public void setAccion(int accion) {
		this.accion = accion;
	}

	

	

}
