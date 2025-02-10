package com.panaderia.manageBan;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import com.panaderia.modelo.negocio.CatPerfil;
import com.panaderia.modelo.negocio.CatUbicacion;
import com.panaderia.modelo.negocio.CatUsuario;
import com.panaderia.seguridad.UserSession;
import com.panaderia.servicio.IBriocheServicio;
import com.panaderia.util.MD5Util;
import com.panaderia.util.MsgUtil;



@ManagedBean(name = "usuarioMB")
@ViewScoped
public class UsuarioMB implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 959107711113516850L;
	@ManagedProperty(value = "#{BriocheServicio}")
	IBriocheServicio iBriocheServicio;
	@ManagedProperty(value = "#{userSession}")
	UserSession userSession;

	
	private CatUsuario usuario;
	private List<CatUsuario> lista;
	private List<CatUsuario> listaFilter;
	private CatUsuario slcUsuario;
	
	
	private String doPerfil;
	List<CatPerfil> listaPerfiles;
	private String doUbicacion;

	public UsuarioMB() {

	}

	@PostConstruct
	public void inicio() {
		usuario = new CatUsuario();

		lista = getiBriocheServicio().obtenerUsuarios();
	}

	public void guardar() {

		try {

			if (usuario.getId() == 0) {
				usuario.setContrasenia(MD5Util.encriptaMD5(usuario.getContrasenia()));
				usuario.setCatPerfil(getiBriocheServicio().obtenerPerfilXId(Integer.parseInt(doPerfil)));
				usuario.setCatUbicacion(getiBriocheServicio().obtenerUbicacionXId(Integer.parseInt(doUbicacion)));
				getiBriocheServicio().guardarUsuario(usuario);

				lista.add(usuario);

				MsgUtil.msgInfo("Exito!", "Registro Guardado.");
			} else {
				usuario.setContrasenia(MD5Util.encriptaMD5(usuario.getContrasenia()));
				usuario.setCatPerfil(getiBriocheServicio().obtenerPerfilXId(Integer.parseInt(doPerfil)));
				usuario.setCatUbicacion(getiBriocheServicio().obtenerUbicacionXId(Integer.parseInt(doUbicacion)));
				getiBriocheServicio().actualizarUsuario(usuario);
				MsgUtil.msgInfo("Exito!", "Registro Actualizado.");
				
			}

			usuario = new CatUsuario();

		} catch (Exception e) {
			e.printStackTrace();
			MsgUtil.msgError("Error!", "Registro no procesado.");
		}

	}

	public void editar() {
		usuario = slcUsuario;
		String cadena = Integer.toString(usuario.getCatPerfil().getId());
		doPerfil=cadena;
	}

	public void eliminar() {
		try {
			getiBriocheServicio().eliminarUsuario(slcUsuario);
			lista.remove(slcUsuario);
			usuario = new CatUsuario();
			MsgUtil.msgInfo("Exito!", "Registro Eliminado.");
		} catch (Exception e) {
			e.printStackTrace();
			MsgUtil.msgError("Error!", "Registro no eliminado.");
		}
	}

	public void cancelar() {
		usuario = new CatUsuario();
		slcUsuario = null;
	}
	
	public List<CatPerfil> getlistaPerfil() {
		return getiBriocheServicio().obtenerPerfiles();
	}
	
	public List<CatUbicacion> getlistaUbicacion() {
		return getiBriocheServicio().obtenerUbicaciones();
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

	public CatUsuario getUsuario() {
		return usuario;
	}

	public void setUsuario(CatUsuario usuario) {
		this.usuario = usuario;
	}

	public List<CatUsuario> getLista() {
		return lista;
	}

	public void setLista(List<CatUsuario> lista) {
		this.lista = lista;
	}

	public List<CatUsuario> getListaFilter() {
		return listaFilter;
	}

	public void setListaFilter(List<CatUsuario> listaFilter) {
		this.listaFilter = listaFilter;
	}

	public CatUsuario getSlcUsuario() {
		return slcUsuario;
	}

	public void setSlcUsuario(CatUsuario slcUsuario) {
		this.slcUsuario = slcUsuario;
	}

	public String getDoPerfil() {
		return doPerfil;
	}

	public void setDoPerfil(String doPerfil) {
		this.doPerfil = doPerfil;
	}

	public List<CatPerfil> getListaPerfiles() {
		return listaPerfiles;
	}

	public void setListaPerfiles(List<CatPerfil> listaPerfiles) {
		this.listaPerfiles = listaPerfiles;
	}

	public String getDoUbicacion() {
		return doUbicacion;
	}

	public void setDoUbicacion(String doUbicacion) {
		this.doUbicacion = doUbicacion;
	}

	

}
