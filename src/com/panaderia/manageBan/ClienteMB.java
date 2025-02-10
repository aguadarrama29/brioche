package com.panaderia.manageBan;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import com.panaderia.modelo.negocio.CatCliente;
import com.panaderia.seguridad.UserSession;
import com.panaderia.servicio.IBriocheServicio;
import com.panaderia.util.MsgUtil;

@ManagedBean(name = "clienteMB")
@ViewScoped
public class ClienteMB implements Serializable{

	
	private static final long serialVersionUID = 1427362174578528086L;
	@ManagedProperty(value = "#{BriocheServicio}")
	IBriocheServicio iBriocheServicio;
	@ManagedProperty(value = "#{userSession}")
	UserSession userSession;
	
	private CatCliente cliente;
	private List<CatCliente> lista;
	private List<CatCliente> listaFilter;
	private CatCliente slcCliente;

	public ClienteMB() {

	}

	@PostConstruct
	public void inicio() {
		cliente = new CatCliente();
		lista = new ArrayList<CatCliente>();
		
		lista=getiBriocheServicio().obtenerClientes();

		/*for (Cliente c : getiBriocheServicio().obtenerClientes()) {
			ClienteBO cBO = new ClienteBO();
			cBO.setCliente(c);
			cBO.setTipo(true);
			lista.add(cBO);
		}*/
	}

	public void guardar() {

		try {
			if (cliente.getId()==0) {
				getiBriocheServicio().guardarCliente(cliente);

				lista.add(cliente);

				MsgUtil.msgInfo("Exito!", "Registro Guardado.");
			} else {
				getiBriocheServicio().actualizarCliente(cliente);
				MsgUtil.msgInfo("Exito!", "Registro Actualizado.");
			}

			cliente = new CatCliente();

		} catch (Exception e) {
			e.printStackTrace();
			MsgUtil.msgError("Error!", "Registro no procesado.");
		}

	}

	public void editar() {
		cliente = slcCliente;
	}

	public void eliminar() {
		try {
			getiBriocheServicio().eliminarCliente(slcCliente);
			lista.remove(slcCliente);
			MsgUtil.msgInfo("Exito!", "Registro Eliminado.");
		} catch (Exception e) {
			e.printStackTrace();
			MsgUtil.msgError("Error!", "Registro no eliminado.");
		}
	}

	public void cancelar() {
		cliente = new CatCliente();
		slcCliente = null;
	}

	

	public UserSession getUserSession() {
		return userSession;
	}

	public void setUserSession(UserSession userSession) {
		this.userSession = userSession;
	}

	

	public IBriocheServicio getiBriocheServicio() {
		return iBriocheServicio;
	}

	public void setiBriocheServicio(IBriocheServicio iBriocheServicio) {
		this.iBriocheServicio = iBriocheServicio;
	}

	public CatCliente getCliente() {
		return cliente;
	}

	public void setCliente(CatCliente cliente) {
		this.cliente = cliente;
	}

	public List<CatCliente> getLista() {
		return lista;
	}

	public void setLista(List<CatCliente> lista) {
		this.lista = lista;
	}

	public List<CatCliente> getListaFilter() {
		return listaFilter;
	}

	public void setListaFilter(List<CatCliente> listaFilter) {
		this.listaFilter = listaFilter;
	}

	public CatCliente getSlcCliente() {
		return slcCliente;
	}

	public void setSlcCliente(CatCliente slcCliente) {
		this.slcCliente = slcCliente;
	}
	
	

}
