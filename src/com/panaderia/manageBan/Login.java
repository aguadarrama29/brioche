package com.panaderia.manageBan;

import java.io.IOException;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.panaderia.modelo.negocio.CatUsuario;
import com.panaderia.servicio.IBriocheServicio;
import com.panaderia.util.MsgUtil;
import com.panaderia.util.ResultTypeUtil;

@ManagedBean(name = "login")
@SessionScoped
public class Login extends SpringBeanAutowiringSupport implements Serializable {

	@Autowired
	IBriocheServicio iBriocheServicio;

	private static final long serialVersionUID = 1L;
	private CatUsuario usuario;

	public Login() {
		this.initObjects();
	}

	private void initObjects() {
		usuario = new CatUsuario();
	}

	public String doLogin() throws IOException {
		usuario = iBriocheServicio.login(usuario);
		String resultado = null;

		if (usuario != null) {
			if (usuario.getCatPerfil().getId() == 2) {
				resultado = ResultTypeUtil.ADMINISTRADOR;
			}

			if (usuario.getCatPerfil().getId() == 1) {
				resultado = ResultTypeUtil.USUARIO;
			}
			FacesContext context = FacesContext.getCurrentInstance();
			context.getExternalContext().getSessionMap().put("usuario", usuario);
		} else {
			MsgUtil.msgError("Error!", "El usuario y/o contraseña son incorrectos");
			this.initObjects();
		}

		return resultado;
	}

	public CatUsuario getUsuario() {
		return usuario;
	}

	public void setUsuario(CatUsuario usuario) {
		this.usuario = usuario;
	}

}