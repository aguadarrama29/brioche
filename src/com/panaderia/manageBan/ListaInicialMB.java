package com.panaderia.manageBan;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
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

import com.panaderia.modelo.negocio.CatListaInicial;
import com.panaderia.modelo.negocio.CatListaInicialId;
import com.panaderia.modelo.negocio.CatTipoPan;
import com.panaderia.modelo.negocio.CatUbicacion;
import com.panaderia.seguridad.UserSession;
import com.panaderia.servicio.IBriocheServicio;
import com.panaderia.util.MsgUtil;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@ManagedBean(name = "listaInicialMB")
@ViewScoped
public class ListaInicialMB implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3043291667961707061L;

	private static final Logger logger = Logger.getLogger(ListaInicialMB.class);

	@ManagedProperty(value = "#{BriocheServicio}")
	IBriocheServicio iBriocheServicio;
	@ManagedProperty(value = "#{userSession}")
	UserSession userSession;

	private CatListaInicial tipoPan;
	private List<CatListaInicial> lista;
	private List<CatListaInicial> listaFilter;
	private CatListaInicial slctipoPan;
	private boolean estatus;

	private int accion = 1; // para guardar 2 para actualizar
	
	private CatTipoPan pan;
	private CatUbicacion catUbicacion;
	private int idSucursal;

	public ListaInicialMB() {

	}

	@PostConstruct
	public void inicio() {
		tipoPan = new CatListaInicial();
		tipoPan.setId(new CatListaInicialId());
		//lista = getiBriocheServicio().listaInicialXSucursal(1);
		lista= new ArrayList<>();
		idSucursal=0;
		System.out.println("inicioo"+idSucursal);
		pan= new CatTipoPan();
	}

	public void agregar() {
		accion = 1;
		//limpiar info
		pan= new CatTipoPan();
		tipoPan= new CatListaInicial();
		tipoPan.setId(new CatListaInicialId());
	}

	public void guardar() {
		 System.out.println("voy a guardar"+accion+"  "+pan.getCodigo());
		try {
			
			tipoPan.getId().setCodigopan(pan.getCodigo());
			tipoPan.getId().setIdUbicacion(idSucursal);
			
			if (accion == 1) {
				tipoPan.setEstatus(1);
				getiBriocheServicio().guardarCatListaInicial(tipoPan);

				lista.add(tipoPan);
				MsgUtil.msgInfo("Exito!", "Registro Guardado.");
			} else {

				getiBriocheServicio().actualizarCatListaInicial(tipoPan);
				MsgUtil.msgInfo("Exito!", "Registro Actualizado.");
			}
            this.sucursal();
			//this.limpiar();

		} catch (Exception e) {
			e.printStackTrace();
			MsgUtil.msgError("Error!", "Registro no procesado.");
		}

	}

	public void editar(CatListaInicial catTipoPan) {
		System.out.println("editar"+catTipoPan.getCantidad());
		tipoPan = catTipoPan;
		
		pan=getiBriocheServicio().obtenerTipoPanXCodigo(catTipoPan.getId().getCodigopan());
		accion = 2;
	}
	
	public void elimina(CatListaInicial catTipoPan) {
		System.out.println("elimina"+catTipoPan.getCantidad());
		tipoPan = catTipoPan;		
		accion = 3;
	}

	public void eliminar() {
		try {
			
			getiBriocheServicio().eliminarCatListaInicial(tipoPan);
			this.limpiar();
			MsgUtil.msgInfo("Exito!", "Registro Eliminado.");

		} catch (Exception e) {
			e.printStackTrace();
			MsgUtil.msgError("Error!", "Registro no eliminado.");
		}
	}

	public void cancelar() {
		tipoPan = new CatListaInicial();
		slctipoPan = null;
		estatus = false;
	}

	public void limpiar() throws IOException {
		FacesContext.getCurrentInstance().getExternalContext().redirect("listaInicial.xhtml");
	}

	public void reporteListainicial() {

		ExternalContext context;
		FacesContext facesContext = FacesContext.getCurrentInstance();
		context = facesContext.getExternalContext();
		ExternalContext externalContext = facesContext.getExternalContext();
		HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
		String absoluteDiskPath = context.getRealPath("/resources/");
		String reportPath = FacesContext.getCurrentInstance().getExternalContext()
				.getRealPath("/resources/reportes/reporteListaInicio.jasper");

		JasperPrint jasperPrint;
		HashMap<String, Object> parametros = new HashMap<String, Object>();

		JRBeanCollectionDataSource beanCollectionDataSource;
		if (lista == null)
			beanCollectionDataSource = new JRBeanCollectionDataSource(listaFilter);
		else
			beanCollectionDataSource = new JRBeanCollectionDataSource(lista);

		parametros.put("patch", absoluteDiskPath);
		// parametros.put("expCajaTodo",expCajaTodo );//cajasFiltro

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
		System.out.println("que llega" + c.getCodigo());
		cantArticulo = 0.0;
	}

	// obtener los articulos por tipo
	public List<CatTipoPan> completeArticulosDes(String query) {
		List<CatTipoPan> articulos = new ArrayList<CatTipoPan>();
		articulos = getiBriocheServicio().obtenerTiposPan();

		List<CatTipoPan> filtrados = new ArrayList<CatTipoPan>();

		for (int i = 0; i < articulos.size(); i++) {
			CatTipoPan c = articulos.get(i);
			if (c.getDescripcion() != null) {
				if (c.getDescripcion().toUpperCase().contains(query.toUpperCase())) {
					filtrados.add(c);
				}
			}
		}
		return filtrados;
	}
	
	public void sucursal() {
		idSucursal = catUbicacion.getId();
		System.out.println(idSucursal);
		lista = getiBriocheServicio().listaInicialXSucursal(idSucursal);
	}
	
	public List<CatUbicacion> getlistaSucursales() {
		return getiBriocheServicio().obtenerUbicaciones();
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

	public CatListaInicial getTipoPan() {
		return tipoPan;
	}

	public void setTipoPan(CatListaInicial tipoPan) {
		this.tipoPan = tipoPan;
	}

	public List<CatListaInicial> getLista() {
		return lista;
	}

	public void setLista(List<CatListaInicial> lista) {
		this.lista = lista;
	}

	public List<CatListaInicial> getListaFilter() {
		return listaFilter;
	}

	public void setListaFilter(List<CatListaInicial> listaFilter) {
		this.listaFilter = listaFilter;
	}

	public CatListaInicial getSlctipoPan() {
		return slctipoPan;
	}

	public void setSlctipoPan(CatListaInicial slctipoPan) {
		this.slctipoPan = slctipoPan;
	}

	public CatTipoPan getPan() {
		return pan;
	}

	public void setPan(CatTipoPan pan) {
		this.pan = pan;
	}

	public CatUbicacion getCatUbicacion() {
		return catUbicacion;
	}

	public void setCatUbicacion(CatUbicacion catUbicacion) {
		this.catUbicacion = catUbicacion;
	}

	public int getIdSucursal() {
		return idSucursal;
	}

	public void setIdSucursal(int idSucursal) {
		this.idSucursal = idSucursal;
	}

}
