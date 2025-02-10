package com.panaderia.manageBan;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.panaderia.modelo.negocio.CatUsuario;
import com.panaderia.modelo.negocio.RegistroEfectivo;
import com.panaderia.seguridad.UserSession;
import com.panaderia.servicio.IBriocheServicio;
import com.panaderia.util.MsgUtil;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@ManagedBean(name = "registroMovEfec")
@ViewScoped
public class RegistroEfectivoController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7688938966310133147L;
	
	private static final Logger logger = Logger.getLogger(RegistroEfectivoController.class);
	
	@ManagedProperty(value = "#{BriocheServicio}")
	IBriocheServicio iBriocheServicio;
	@ManagedProperty(value = "#{userSession}")
	UserSession userSession;
	
	
	
	private RegistroEfectivo regEfectivo;
	private List<RegistroEfectivo> lista;
	private List<RegistroEfectivo> listaFilter;
	private RegistroEfectivo slcRegistroEfectivo;
	
	private int accion=1; //1 para guardar 2 para actualizar
	
	private Date fechaActual = new Date();	
	
	private int perfil=0;
	
	public RegistroEfectivoController() {

	}

	@PostConstruct
	public void inicio() {
		perfil=userSession.getUsuario().getCatPerfil().getId();
		regEfectivo = new RegistroEfectivo();
		//lista = getiBriocheServicio().obtenerRegistrosEfectivo(); //3 todos //1 registrados //2 cancelados obtenerRegistrosXEstatusXFecha(3);
		lista = getiBriocheServicio().obtenerRegistrosXEstatusXFecha(1,fechaActual,userSession.getUsuario().getCatUbicacion().getId());
	}
	
	public void registroXDia() {
		lista= new ArrayList<>();
		lista = getiBriocheServicio().obtenerRegistrosXEstatusXFecha(1,fechaActual,userSession.getUsuario().getCatUbicacion().getId());
	}
	
	public void agregar() {
		accion=1;	
	}

	public void guardar() {
		//System.out.println("voy a guardar"+accion);
		
		regEfectivo.setCatUsuario(userSession.getUsuario());
		try {
			regEfectivo.setDescripcion(regEfectivo.getDescripcion().toUpperCase());
			if (accion==1) {
				regEfectivo.setEstatus(1); //1 registro 2 cancelar
				regEfectivo.setIdSucursal(userSession.getUsuario().getCatUbicacion().getId());
				regEfectivo.setFecha(new Date());
				getiBriocheServicio().guardarRegistroEfectivo(regEfectivo);

				lista.add(regEfectivo);

				MsgUtil.msgInfo("Exito!", "Registro Guardado.");
			} else {
				
				getiBriocheServicio().actualizarRegistroEfectivo(regEfectivo);
				MsgUtil.msgInfo("Exito!", "Registro Actualizado.");
				
			}
			this.limpiar();
		} catch (Exception e) {
			e.printStackTrace();
			MsgUtil.msgError("Error!", "Registro no procesado.");
		}

	}

	public void editar(RegistroEfectivo regEfec) {
		regEfectivo = regEfec;
		accion=2;
	}

	public void eliminar() {
		try {
			getiBriocheServicio().eliminarRegistroEfectivo(regEfectivo);
			lista.remove(regEfectivo);
			regEfectivo = new RegistroEfectivo();
			MsgUtil.msgInfo("Exito!", "Registro Eliminado.");
			
			this.inicio();
			
		} catch (Exception e) {
			e.printStackTrace();
			MsgUtil.msgError("Error!", "Registro no eliminado.");
		}
	}

	public void cancelar() {
		regEfectivo = new RegistroEfectivo();
		slcRegistroEfectivo = null;
	}
	
	public void limpiar() throws IOException {
		FacesContext.getCurrentInstance().getExternalContext().redirect("registroDinero.xhtml");
	}
	
	
	public void reporteTipoPan() {

		ExternalContext context;
		FacesContext facesContext = FacesContext.getCurrentInstance();
		context = facesContext.getExternalContext();
		ExternalContext externalContext = facesContext.getExternalContext();
		HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
		String absoluteDiskPath = context.getRealPath("/resources/");
		String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/reportes/movEfectivo.jasper");

		JasperPrint jasperPrint;
		HashMap<String, Object> parametros = new HashMap<String, Object>();
		
		
		JRBeanCollectionDataSource beanCollectionDataSource;
		if(lista==null)
			beanCollectionDataSource = new JRBeanCollectionDataSource(listaFilter);else 
				beanCollectionDataSource = new JRBeanCollectionDataSource(lista);

		parametros.put("patch", absoluteDiskPath);
		parametros.put("fecha", fechaActual);
		//parametros.put("expCajaTodo",expCajaTodo );//cajasFiltro

		try {
			jasperPrint = JasperFillManager.fillReport(reportPath, parametros, beanCollectionDataSource);
			byte[] pdf = JasperExportManager.exportReportToPdf(jasperPrint);
			response.reset();
			response.setHeader("Content-Type", "application/pdf");
			response.setHeader("Content-Disposition", "inline; filename=\"" + "reporteMovEfectivo.pdf" + "\"");
			response.setContentLength(pdf.length);
			response.getOutputStream().write(pdf, 0, pdf.length);
			response.getOutputStream().flush();
			response.getOutputStream().close();
		} catch (JRException ex) {
			StringWriter stack = new StringWriter();
			ex.printStackTrace(new PrintWriter(stack));
			logger.error(stack.toString());
		} catch (IOException ioex) {
			StringWriter stack = new StringWriter();
			ioex.printStackTrace(new PrintWriter(stack));
			logger.error(stack.toString());
		}
		facesContext.responseComplete();
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

	

	public List<RegistroEfectivo> getLista() {
		return lista;
	}

	public void setLista(List<RegistroEfectivo> lista) {
		this.lista = lista;
	}

	public List<RegistroEfectivo> getListaFilter() {
		return listaFilter;
	}

	public void setListaFilter(List<RegistroEfectivo> listaFilter) {
		this.listaFilter = listaFilter;
	}

	public RegistroEfectivo getSlcRegistroEfectivo() {
		return slcRegistroEfectivo;
	}

	public void setSlcRegistroEfectivo(RegistroEfectivo slcRegistroEfectivo) {
		this.slcRegistroEfectivo = slcRegistroEfectivo;
	}

	public int getAccion() {
		return accion;
	}

	public void setAccion(int accion) {
		this.accion = accion;
	}

	public RegistroEfectivo getRegEfectivo() {
		return regEfectivo;
	}

	public void setRegEfectivo(RegistroEfectivo regEfectivo) {
		this.regEfectivo = regEfectivo;
	}

	public Date getFechaActual() {
		return fechaActual;
	}

	public void setFechaActual(Date fechaActual) {
		this.fechaActual = fechaActual;
	}

	public int getPerfil() {
		return perfil;
	}

	public void setPerfil(int perfil) {
		this.perfil = perfil;
	}
	
	

}
