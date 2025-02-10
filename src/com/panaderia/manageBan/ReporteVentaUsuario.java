package com.panaderia.manageBan;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

import com.panaderia.modelo.bo.VentaBO;
import com.panaderia.modelo.negocio.PanXSucursal;
import com.panaderia.modelo.negocio.RegistroVenta;
import com.panaderia.seguridad.UserSession;
import com.panaderia.servicio.IBriocheServicio;
import com.panaderia.util.PrinterService;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;


@ManagedBean(name = "reporteUsuario")
@ViewScoped
public class ReporteVentaUsuario implements Serializable{

	private static final long serialVersionUID = -2622248929053798986L;

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
	BigDecimal ingreso=BigDecimal.ZERO,egreso=BigDecimal.ZERO,venta=BigDecimal.ZERO;
	
	private DateFormat fecEnvioFormato = new SimpleDateFormat("dd-MM-yyyy");
	private DateFormat fecFormatoHora = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	
	BigDecimal total;
	
	//por precio
	private List<PanXSucursal> panSucursal;
	private int turno;//1 mañana 2 tarde
	private List<RegistroVenta> listaFilterPrecio;
	
	
	public ReporteVentaUsuario() {

	}

	@PostConstruct
	public void inicio() {
		
		///private int idUsuario=1;//userSession.getUsuario().getId();
		System.out.println("ddddeeee"+userSession.getUsuario().getId());
		
		turno=userSession.getUsuario().getTurno();
		regVenta = new RegistroVenta();
		lista = getiBriocheServicio().obtenerRegVentaXUsuario(fechaActual,userSession.getUsuario().getId());
		
		ingreso=getiBriocheServicio().obtenerIngresoXDiaXTipo(fechaActual,1,userSession.getUsuario().getCatUbicacion().getId(),userSession.getUsuario().getId());
		egreso=getiBriocheServicio().obtenerIngresoXDiaXTipo(fechaActual,2,userSession.getUsuario().getCatUbicacion().getId(),userSession.getUsuario().getId());
		
		//BigDecimal venta=getiBriocheServicio().obtenerTotalVentaXDiaXTipo(fechaActual,userSession.getUsuario().getCatUbicacion().getId());
		
		List<RegistroVenta>venta=getiBriocheServicio().obtenerTotalVentaXDiaXSucursal(fechaActual,userSession.getUsuario().getCatUbicacion().getId());
			
		List<String> folioExiste = new ArrayList<String>();
		total= new BigDecimal(0);
		for (RegistroVenta rv : lista) {
			if(!folioExiste.contains(rv.getId().getFolio())) {
				folioExiste.add(rv.getId().getFolio());
				total=total.add(rv.getTotal());
			}
		}
		
		System.out.println("que ondaaddddd"+ingreso+"   "+egreso+"   "+total+" sucursal"+userSession.getUsuario().getCatUbicacion().getId());
		
		
		panSucursal=new ArrayList<>();
		panSucursal=getiBriocheServicio().obtenerPanXSucursalesXFecha(fechaActual, 3, userSession.getUsuario().getCatUbicacion().getId());
		
		System.out.println("ucuandos tenogoo"+panSucursal.size());
	} 
	
	/*public void registroXDia() {
		lista= new ArrayList<>();
		lista = getiBriocheServicio().obtenerRegVentaXDiaConsulta(fechaActual);
	}*/
	
	
	public void limpiar() throws IOException {
		FacesContext.getCurrentInstance().getExternalContext().redirect("ventaXDia.xhtml");
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
	
	public void imprimirVentaUsu() {

		ExternalContext context;
		FacesContext facesContext = FacesContext.getCurrentInstance();
		context = facesContext.getExternalContext();
		ExternalContext externalContext = facesContext.getExternalContext();
		HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
		String absoluteDiskPath = context.getRealPath("/resources/");
		String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/reportes/reporteXUsuFecha.jasper");

		
		JasperPrint jasperPrint;
		HashMap<String, Object> parametros = new HashMap<String, Object>();
		
		JRBeanCollectionDataSource beanCollectionDataSource;
		if (listaFilter != null) {
			beanCollectionDataSource = new JRBeanCollectionDataSource(listaFilter);
		} else {
			beanCollectionDataSource = new JRBeanCollectionDataSource(lista);
		}

		parametros.put("patch", absoluteDiskPath);
		parametros.put("fecha", fechaActual);
		parametros.put("entrega", userSession.getUsuario().getDescripcion());
		
		System.out.println("que ondaa"+ingreso+"   "+egreso+"   "+venta);
		
		parametros.put("ingreso", ingreso);
		parametros.put("egreso", egreso);
		parametros.put("venta", venta);
		

		try {
			jasperPrint = JasperFillManager.fillReport(reportPath, parametros, beanCollectionDataSource);
			byte[] pdf = JasperExportManager.exportReportToPdf(jasperPrint);
			response.reset();
			response.setHeader("Content-Type", "application/pdf");
			response.setHeader("Content-Disposition", "inline; filename=\"" + "Reporte Venta por Usuario: "+fechaActual + "\"");
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
	
	
	public void ticketXTurno() {
		String fechaActual = fecFormatoHora.format(new Date());
		try {
			PrinterService printerService = new PrinterService();
			String strContenido = "\t PANADERIA LA BRIOCHE  \n";
			strContenido += "\t SUCURSAL  "+ userSession.getUsuario().getCatUbicacion().getDescripcion()+"\n";
			strContenido += "Telefono(s): 728 28 7 53 80 \n\n";
			strContenido += "Vendedor:"+userSession.getUsuario().getDescripcion()+" \n\n";
		    strContenido += " Descrip\t  Monto\n";

			BigDecimal totalticket = BigDecimal.ZERO;
			BigDecimal sumIngreso = BigDecimal.ZERO;
			
			strContenido += "Venta: " + "\t\t   " + total+  "\n";
			strContenido += "Ingresos: " + "\t    " + ingreso + "\n";
			strContenido += "Gastos: " + "\t    " + egreso + "\n";
			
			sumIngreso=sumIngreso.add(total);
			sumIngreso=sumIngreso.add(ingreso);
			
			totalticket=totalticket.add(sumIngreso);
			totalticket=totalticket.subtract(egreso);
			//totalticket=ingreso.subtract(egreso);
			strContenido +="TOTAL:  \t"+totalticket+  " ";
			//strContenido +="\t EFECTIVO: "+efectivo+"\n";
			//strContenido +="\t CAMBIO:   "+efectivo.subtract(total)+"\n";
			//strContenido +="\t Total Art.:   "+totArti;
			strContenido += "\n \n"
					+ "Corte de Turno!  \n"
					+ "Fecha: "+fechaActual
					+ " \n\n\n\n\n\n\n\n\n\n";
			
			//System.out.println(strContenido);
			printerService.printString("EPSON-T88V", strContenido);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public void registroExistencia() {
		int total;
		
		for (PanXSucursal ps : panSucursal) {
			total=0;
			
			//hacerlo por turno 1 matutino---2 vespertino
			if(turno==1) {
				total=ps.getCantidad()-ps.getCorteMatutino();
				ps.setCorteMatutino(ps.getCorteMatutino());
			}else {
				total=ps.getCantidad()-ps.getCorteVespertino();
				ps.setCorteVespertino(ps.getCorteVespertino());
			}
			
			ps.setCantidad(total); //en cantidad va lo que se mueve segun las ventas o el corte
			
			getiBriocheServicio().actualizarPanXSucursal(ps);
		}
	
		
		
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

	public BigDecimal getIngreso() {
		return ingreso;
	}

	public void setIngreso(BigDecimal ingreso) {
		this.ingreso = ingreso;
	}

	public BigDecimal getEgreso() {
		return egreso;
	}

	public void setEgreso(BigDecimal egreso) {
		this.egreso = egreso;
	}

	public BigDecimal getVenta() {
		return venta;
	}

	public void setVenta(BigDecimal venta) {
		this.venta = venta;
	}

	public List<PanXSucursal> getPanSucursal() {
		return panSucursal;
	}

	public void setPanSucursal(List<PanXSucursal> panSucursal) {
		this.panSucursal = panSucursal;
	}

	public int getTurno() {
		return turno;
	}

	public void setTurno(int turno) {
		this.turno = turno;
	}

	public List<RegistroVenta> getListaFilterPrecio() {
		return listaFilterPrecio;
	}

	public void setListaFilterPrecio(List<RegistroVenta> listaFilterPrecio) {
		this.listaFilterPrecio = listaFilterPrecio;
	}

	

	

}
