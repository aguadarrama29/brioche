package com.panaderia.manageBan;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
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

import com.panaderia.modelo.negocio.CatTipoPan;
import com.panaderia.seguridad.UserSession;
import com.panaderia.servicio.IBriocheServicio;
import com.panaderia.util.MsgUtil;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;



@ManagedBean(name = "tipoPanMB")
@ViewScoped
public class TipoPanMB implements Serializable{	
	
	private static final long serialVersionUID = -6107738923951347880L;
	
	private static final Logger logger = Logger.getLogger(TipoPanMB.class);
	
	@ManagedProperty(value = "#{BriocheServicio}")
	IBriocheServicio iBriocheServicio;
	@ManagedProperty(value = "#{userSession}")
	UserSession userSession;

	
	private CatTipoPan tipoPan;
	private List<CatTipoPan> lista;
	private List<CatTipoPan> listaFilter;
	private CatTipoPan slctipoPan;
	private boolean estatus;
	 
	private int accion=1; //para guardar 2 para actualizar
	
	public TipoPanMB() {

	}

	@PostConstruct
	public void inicio() {
		tipoPan = new CatTipoPan();
		lista = getiBriocheServicio().obtenerTiposPan();
	}
	
	public void agregar() {
		accion=1;	
	}

	public void guardar() {
		//System.out.println("voy a guardar"+accion);
		try {
			tipoPan.setCodigo(tipoPan.getCodigo().toUpperCase());
			tipoPan.setDescripcion(tipoPan.getDescripcion().toUpperCase());
			
			if (accion==1) {
				tipoPan.setImagen("general.jpg");
				tipoPan.setEstatus(true);
				//System.out.println("voy a guardar"+accion);
				getiBriocheServicio().guardarTipoPan(tipoPan);

				lista.add(tipoPan);

				MsgUtil.msgInfo("Exito!", "Registro Guardado.");
			} else {
				
				getiBriocheServicio().actualizarTipoPan(tipoPan);
				MsgUtil.msgInfo("Exito!", "Registro Actualizado.");
				
			}

			this.limpiar();
			//this.inicio();

		} catch (Exception e) {
			e.printStackTrace();
			MsgUtil.msgError("Error!", "Registro no procesado.");
		}

	}

	public void editar(CatTipoPan catTipoPan) {
		tipoPan = catTipoPan;
		accion=2;
	}

	public void eliminar() {
		try {
			getiBriocheServicio().eliminarTipoPan(tipoPan);
			lista.remove(tipoPan);
			tipoPan = new CatTipoPan();
			MsgUtil.msgInfo("Exito!", "Registro Eliminado.");
			
			this.inicio();
			
		} catch (Exception e) {
			e.printStackTrace();
			MsgUtil.msgError("Error!", "Registro no eliminado.");
		}
	}

	public void cancelar() {
		tipoPan = new CatTipoPan();
		slctipoPan = null;
		estatus=false;
	}
	
	public void limpiar() throws IOException {
		FacesContext.getCurrentInstance().getExternalContext().redirect("tipoPan.xhtml");
	}
	
	
	public void reporteTipoPan() {

		ExternalContext context;
		FacesContext facesContext = FacesContext.getCurrentInstance();
		context = facesContext.getExternalContext();
		ExternalContext externalContext = facesContext.getExternalContext();
		HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
		String absoluteDiskPath = context.getRealPath("/resources/");
		String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/reportes/reporteConcertacion.jasper");

		JasperPrint jasperPrint;
		HashMap<String, Object> parametros = new HashMap<String, Object>();
		
		
		JRBeanCollectionDataSource beanCollectionDataSource;
		if(lista==null)
			beanCollectionDataSource = new JRBeanCollectionDataSource(listaFilter);else 
				beanCollectionDataSource = new JRBeanCollectionDataSource(lista);

		parametros.put("patch", absoluteDiskPath);
		//parametros.put("expCajaTodo",expCajaTodo );//cajasFiltro

		try {
			jasperPrint = JasperFillManager.fillReport(reportPath, parametros, beanCollectionDataSource);
			byte[] pdf = JasperExportManager.exportReportToPdf(jasperPrint);
			response.reset();
			response.setHeader("Content-Type", "application/pdf");
			response.setHeader("Content-Disposition", "inline; filename=\"" + "reporteConcertacion.pdf" + "\"");
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
	
	
	double cantArticulo;
	public void agrega(CatTipoPan c) {
		System.out.println("que llega"+c.getCodigo());
		cantArticulo=0.0;
	}

	public double getCantArticulo() {
		return cantArticulo;
	}

	public void setCantArticulo(double cantArticulo) {
		this.cantArticulo = cantArticulo;
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

	public CatTipoPan getTipoPan() {
		return tipoPan;
	}

	public void setTipoPan(CatTipoPan tipoPan) {
		this.tipoPan = tipoPan;
	}

	public List<CatTipoPan> getLista() {
		return lista;
	}

	public void setLista(List<CatTipoPan> lista) {
		this.lista = lista;
	}

	public List<CatTipoPan> getListaFilter() {
		return listaFilter;
	}

	public void setListaFilter(List<CatTipoPan> listaFilter) {
		this.listaFilter = listaFilter;
	}

	public CatTipoPan getSlctipoPan() {
		return slctipoPan;
	}

	public void setSlctipoPan(CatTipoPan slctipoPan) {
		this.slctipoPan = slctipoPan;
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
