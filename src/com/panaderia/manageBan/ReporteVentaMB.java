package com.panaderia.manageBan;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.panaderia.modelo.negocio.RegistroVenta;
import com.panaderia.seguridad.UserSession;
import com.panaderia.servicio.IBriocheServicio;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;


@ManagedBean(name = "reporteVentaMB")
@ViewScoped
public class ReporteVentaMB implements Serializable{

	private static final long serialVersionUID = 8369149234906850781L;
	private static final Logger logger = Logger.getLogger(ReporteVentaMB.class);
	
	@ManagedProperty(value = "#{BriocheServicio}")
	IBriocheServicio iBriocheServicio;
	@ManagedProperty(value = "#{userSession}")
	UserSession userSession;

	
	private RegistroVenta regVenta;
	private List<RegistroVenta> lista;
	private List<RegistroVenta> listaFilter;
	private RegistroVenta slcregVenta;
	private boolean estatus=true;
	
	private Date fechaActual = new Date();	
	private int idUsuario=userSession.getUsuario().getId();
	

	public ReporteVentaMB() {

	}

	@PostConstruct
	public void inicio() {
		//si es administrador se mnanda el 1 se busca la venta total sin sucursal
		int idUbicacion=1;
		if(userSession.getUsuario().getCatPerfil().getId()!=2)
			idUbicacion=userSession.getUsuario().getCatUbicacion().getId();
				
		regVenta = new RegistroVenta();
		lista= new ArrayList<>();
		lista = getiBriocheServicio().obtenerRegVentaXDia(fechaActual);
		
		ingreso=getiBriocheServicio().obtenerIngresoXDiaXTipo(fechaActual,1,idUbicacion,userSession.getUsuario().getId());
		egreso=getiBriocheServicio().obtenerIngresoXDiaXTipo(fechaActual,2,idUbicacion,userSession.getUsuario().getId());
		
		//venta=getiBriocheServicio().obtenerTotalVentaXDiaXTipo(fechaActual,idUbicacion);
	}
	
	
	BigDecimal ingreso=BigDecimal.ZERO,egreso= BigDecimal.ZERO,venta= BigDecimal.ZERO;
	public void registroXDia() {
		
		//si es administrador se mnanda el 1 se busca la venta total sin sucursal
		int idUbicacion=1;
		if(userSession.getUsuario().getCatPerfil().getId()!=2)
			idUbicacion=userSession.getUsuario().getCatUbicacion().getId();
				
		lista= new ArrayList<>();
		lista = getiBriocheServicio().obtenerRegVentaXDiaConsulta(fechaActual,idUbicacion);
		ingreso=getiBriocheServicio().obtenerIngresoXDiaXTipo(fechaActual,1,idUbicacion,userSession.getUsuario().getId());
		egreso=getiBriocheServicio().obtenerIngresoXDiaXTipo(fechaActual,2,idUbicacion,userSession.getUsuario().getId());
		
		
		
		//venta=getiBriocheServicio().obtenerTotalVentaXDiaXTipo(fechaActual,idUbicacion);
		//System.out.println(idUbicacion+"  que ondaa"+ingreso+"   "+egreso+"   "+venta);
	}
	
	
	public void limpiar() throws IOException {
		FacesContext.getCurrentInstance().getExternalContext().redirect("ventaXDia.xhtml");
	}
	
	
	
	public void imprimirBandeja() {

		ExternalContext context;
		FacesContext facesContext = FacesContext.getCurrentInstance();
		context = facesContext.getExternalContext();
		ExternalContext externalContext = facesContext.getExternalContext();
		HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
		String absoluteDiskPath = context.getRealPath("/resources/");
		String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/reportes/reporteXDia.jasper");

		
			
		
		JasperPrint jasperPrint;
		HashMap<String, Object> parametros = new HashMap<String, Object>();
		
		JRBeanCollectionDataSource beanCollectionDataSource;
		if (listaFilter != null) {
			beanCollectionDataSource = new JRBeanCollectionDataSource(listaFilter);
		} else {
			beanCollectionDataSource = new JRBeanCollectionDataSource(lista);
		}
		System.out.println("que ondaaddddd"+ingreso+"   "+egreso+"   "+venta);
		parametros.put("patch", absoluteDiskPath);
		parametros.put("fecha", fechaActual);
		
		parametros.put("ingreso", ingreso);
		parametros.put("egreso", egreso);
		parametros.put("venta", venta);
		

		try {
			jasperPrint = JasperFillManager.fillReport(reportPath, parametros, beanCollectionDataSource);
			byte[] pdf = JasperExportManager.exportReportToPdf(jasperPrint);
			response.reset();
			response.setHeader("Content-Type", "application/pdf");
			response.setHeader("Content-Disposition", "inline; filename=\"" + "Reporte Venta del dia: "+fechaActual + "\"");
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
	
	
	public void exportarBandeja() {
		ExternalContext context;
		FacesContext facesContext = FacesContext.getCurrentInstance();
		context = facesContext.getExternalContext();
		ExternalContext externalContext = facesContext.getExternalContext();
		HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
		String absoluteDiskPath = context.getRealPath("/resources/");
		String reportPath = FacesContext.getCurrentInstance().getExternalContext()
				.getRealPath("/resources/reportes/reporteDIExcel.jasper");

		// JasperPrint jasperPrint;
		HashMap<String, Object> parametros = new HashMap<String, Object>();

		parametros.put("patch", absoluteDiskPath);

		// cambio para amndarle lo que trae la tabla
		JRBeanCollectionDataSource beanCollectionDataSource;
		if (listaFilter != null) {
			beanCollectionDataSource = new JRBeanCollectionDataSource(listaFilter);
		} else {
			beanCollectionDataSource = new JRBeanCollectionDataSource(lista);
		}
		try {
			JasperPrint jp = JasperFillManager.fillReport(reportPath, parametros, beanCollectionDataSource);
			JExcelApiExporter xls = new JExcelApiExporter();
			// exportando a excel xls
			xls.setParameter(JRXlsExporterParameter.JASPER_PRINT, jp);
			xls.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, true);
			xls.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, false);
			xls.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, true);
			xls.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, true);
			xls.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, response.getOutputStream());

			response.setContentType("application/xls");
			response.setHeader("Content-Disposition", "inline;filename=Reporte Venta del dia: "+fechaActual+".xls");

			xls.exportReport();
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
	
	public void reporteXUsuarioCaja() throws Exception {
		ExternalContext context;
		FacesContext facesContext = FacesContext.getCurrentInstance();
		context = facesContext.getExternalContext();
		ExternalContext externalContext = facesContext.getExternalContext();
		HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
		String absoluteDiskPath = context.getRealPath("/resources/");
		String reportPath = "";

		Calendar cal = Calendar.getInstance();
		 cal.setTime(fechaActual);
		String anio = String.valueOf(cal.get(Calendar.YEAR));
		String mes = String.valueOf(cal.get(Calendar.MONTH) + 1);
		String dia = String.valueOf(cal.get(Calendar.DATE));
		
		String consul="fecha::text LIKE '"+anio+"-"+mes+"-"+dia+"%'";
		System.out.println("que saleee"+consul);
		
		Integer idUsuario=userSession.getUsuario().getId();
		
		reportPath = FacesContext.getCurrentInstance().getExternalContext()
				.getRealPath("/resources/reportes/reporteXUsuario.jasper");

		JasperPrint jasperPrint;
		HashMap<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("patch", absoluteDiskPath);
		parametros.put("consul", consul);
		parametros.put("idUsuario", idUsuario);

		ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext()
				.getContext();
		ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);

		DataSource ds = (DataSource) ctx.getBean("DataSource");

		Connection c = ds.getConnection();
		try {
			jasperPrint = JasperFillManager.fillReport(reportPath, parametros, c);
			byte[] pdf = JasperExportManager.exportReportToPdf(jasperPrint);
			response.reset();
			response.setHeader("Content-Type", "application/pdf");
			response.setHeader("Content-Disposition", "inline; filename=\"" + "GuiaSimple" + "\"");
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

	public RegistroVenta getRegVenta() {
		return regVenta;
	}

	public void setRegVenta(RegistroVenta regVenta) {
		this.regVenta = regVenta;
	}

	public List<RegistroVenta> getLista() {
		return lista;
	}

	public void setLista(List<RegistroVenta> lista) {
		this.lista = lista;
	}

	public List<RegistroVenta> getListaFilter() {
		return listaFilter;
	}

	public void setListaFilter(List<RegistroVenta> listaFilter) {
		this.listaFilter = listaFilter;
	}

	public RegistroVenta getSlcregVenta() {
		return slcregVenta;
	}

	public void setSlcregVenta(RegistroVenta slcregVenta) {
		this.slcregVenta = slcregVenta;
	}

	public boolean isEstatus() {
		return estatus;
	}

	public void setEstatus(boolean estatus) {
		this.estatus = estatus;
	}

	public Date getFechaActual() {
		return fechaActual;
	}

	public void setFechaActual(Date fechaActual) {
		this.fechaActual = fechaActual;
	}

	

	

}
