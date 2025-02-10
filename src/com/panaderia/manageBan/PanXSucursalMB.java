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
import org.primefaces.event.TabChangeEvent;

import com.panaderia.modelo.negocio.CatTipoPan;
import com.panaderia.modelo.negocio.CatUbicacion;
import com.panaderia.modelo.negocio.PanXSucursal;
import com.panaderia.modelo.negocio.PanXSucursalId;
import com.panaderia.modelo.negocio.PrecioCantidadSucursal;
import com.panaderia.seguridad.UserSession;
import com.panaderia.servicio.IBriocheServicio;
import com.panaderia.util.MsgUtil;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;



@ManagedBean(name = "panXSucursalMB")
@ViewScoped
public class PanXSucursalMB implements Serializable{	
	
	
	private static final long serialVersionUID = 569973467870665247L;

	private static final Logger logger = Logger.getLogger(PanXSucursalMB.class);
	
	@ManagedProperty(value = "#{BriocheServicio}")
	IBriocheServicio iBriocheServicio;
	@ManagedProperty(value = "#{userSession}")
	UserSession userSession;

	private PanXSucursal panXSucursal;	
	private List<PanXSucursal> listaPrevio;
	private Boolean bandera=false;
	private CatTipoPan catTipoPan;
	private CatUbicacion catUbicacion;
	private String nombreER; 
	private int index = 0;
	private Date fechaConsulta;
	
	private List<PanXSucursal> lista;
	private List<PanXSucursal> listaFilter;
	private PanXSucursal slctipoPan;
	
	 Date fechaActual = new Date();
	 
	 //precio
	 private List<PrecioCantidadSucursal> listaPrevioPrecio;

	public PanXSucursalMB() {

	}

	@PostConstruct
	public void inicio() {
		panXSucursal = new PanXSucursal();
		panXSucursal.setCatTipoPan(new CatTipoPan());
		panXSucursal.setCatUbicacion(new CatUbicacion());
		catTipoPan= new CatTipoPan();
		catUbicacion= new CatUbicacion();
		nombreER="";
		lista=new ArrayList<>();
		listaPrevio=new ArrayList<>();
		
		listaPrevio = getiBriocheServicio().obtenerPanXSucursalesXFecha(fechaActual,1,userSession.getUsuario().getCatUbicacion().getId());
		
		//para obtener por precio
		listaPrevioPrecio= new ArrayList<>();
		listaPrevioPrecio=getiBriocheServicio().obtenerPrecioXSucursalesXFecha(fechaActual, 1, userSession.getUsuario().getCatUbicacion().getId());
		
	}
	
	 public void onTabChange(TabChangeEvent event) {
		// System.out.println("llegoo"+event.getTab().getTitle());
		 System.out.println("llegoo"+index);
		 if(index==1) {
			lista = getiBriocheServicio().obtenerPanXSucursalesXFecha(fechaActual,3,userSession.getUsuario().getCatUbicacion().getId());
		 }
	       
	 }
	 
	 //actualizar el pan enviado (recepcion)
	 public void guardarSucrusal() {
			try {	
				for (PanXSucursal pan : listaPrevio) {
					pan.setEstatus(2);
					pan.setRecibe(nombreER);
					
					getiBriocheServicio().actualizarPanXSucursal(pan);
				}
				
				//actualziar estatus por Precio				
				for (PrecioCantidadSucursal pre : listaPrevioPrecio) {
					pre.setEstatus(2); //se puede vender y se muestra en venta
					getiBriocheServicio().actualizarPrecioCantidadSucursal(pre);
				}

				MsgUtil.msgInfo("Exito!", "Resgistros  actualizados con éxito.");
				this.inicio();
			} catch (Exception e) {
				e.printStackTrace();
				MsgUtil.msgError("Error!", "Registros no procesados.");
			}

	}
	 
	 public void consultarXFecha() {
		// lista= new ArrayList<>();
		 System.out.println("que tengo"+userSession.getUsuario().getCatUbicacion().getId());
		 lista = getiBriocheServicio().obtenerPanXSucursalesXFecha(fechaConsulta,3,userSession.getUsuario().getCatUbicacion().getId());
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

		JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(listaFilter);

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

	
	//obtener tipo pan complete
	public List<CatTipoPan> completeTipoPan(String query) {
		List<CatTipoPan> tipopan = new ArrayList<CatTipoPan>();
		tipopan=getiBriocheServicio().obtenerTiposPan();
		
		List<CatTipoPan> filtrados = new ArrayList<CatTipoPan>();

		for (int i = 0; i < tipopan.size(); i++) {
			CatTipoPan c = tipopan.get(i);
			if (c.getDescripcion() != null) {
				if (c.getDescripcion().toUpperCase().contains(query.toUpperCase())) {
					filtrados.add(c);
				}
			}
		}
		return filtrados;
	}
	
	//obtener sucursales complete
	public List<CatUbicacion> completeUbicacion(String query) {
		List<CatUbicacion> tipopan = new ArrayList<CatUbicacion>();
		tipopan=getiBriocheServicio().obtenerUbicaciones();
		
		List<CatUbicacion> filtrados = new ArrayList<CatUbicacion>();

		for (int i = 0; i < tipopan.size(); i++) {
			CatUbicacion c = tipopan.get(i);
			if (c.getDescripcion() != null) {
				if (c.getDescripcion().toUpperCase().contains(query.toUpperCase())) {
					filtrados.add(c);
				}
			}
		}
		return filtrados;
	}

	
	public void limpiar() throws IOException {
		FacesContext.getCurrentInstance().getExternalContext().redirect("ERPanSucursal.xhtml");
	}
	
	public void agregarArticulo() {
		Date fecha = new Date();
		
		if(listaPrevio==null) {
			listaPrevio= new ArrayList<>();
		}
		panXSucursal.setId(new PanXSucursalId());
		//usuario1 admin2    tipo1 es origen tipo2 destino
		if(userSession.getUsuario().getCatPerfil().getId()==1) {
			panXSucursal.getId().setTipo(2);
			panXSucursal.setRecibe(nombreER);
		}else {
			panXSucursal.getId().setTipo(1);
			panXSucursal.setEntrega(nombreER);
		}
		
		panXSucursal.getId().setCodigopan(catTipoPan.getCodigo());
		panXSucursal.getId().setIdUbicacion(catUbicacion.getId());
		panXSucursal.getId().setFecha(fecha);
		
		panXSucursal.setCatTipoPan(catTipoPan);
		panXSucursal.setCatUbicacion(catUbicacion);
		
		listaPrevio.add(panXSucursal);
		
		if(listaPrevio.size()>0) {
			bandera=true;
		}
		
		panXSucursal = new PanXSucursal();
		MsgUtil.msgInfo("Exito!", "Se agrego el articulo.");
	}
	
	public void editar(PanXSucursal panXSucursalNuevo) {
		panXSucursal = panXSucursalNuevo;
	}
	
	public void guardar(String accion) {
		try {		
			if (accion.equals("1")) {				
				
				for (PanXSucursal pansuc : listaPrevio) {
					getiBriocheServicio().guardarPanXSucursal(pansuc);
					
				}

				MsgUtil.msgInfo("Exito!", "Registro Guardado.");
				this.inicio();
			} else {
				getiBriocheServicio().actualizarPanXSucursal(panXSucursal);
				MsgUtil.msgInfo("Exito!", "Registro Actualizado.");
				
			}

			

		} catch (Exception e) {
			e.printStackTrace();
			MsgUtil.msgError("Error!", "Registro no procesado.");
		}

	}
	
	
	public Integer calculo (PanXSucursal pansuc) {
		System.out.println(pansuc.getCantidad()); 
		Integer enviado2=pansuc.getCantidadTPXDia()-pansuc.getCantidad();
		
		return enviado2;
		
	}
	
	
	//para perfil de sucursal
	 
	
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

	public PanXSucursal getPanXSucursal() {
		return panXSucursal;
	}

	public void setPanXSucursal(PanXSucursal panXSucursal) {
		this.panXSucursal = panXSucursal;
	}

	public List<PanXSucursal> getLista() {
		return lista;
	}

	public void setLista(List<PanXSucursal> lista) {
		this.lista = lista;
	}

	public List<PanXSucursal> getListaFilter() {
		return listaFilter;
	}

	public void setListaFilter(List<PanXSucursal> listaFilter) {
		this.listaFilter = listaFilter;
	}

	public PanXSucursal getSlctipoPan() {
		return slctipoPan;
	}

	public void setSlctipoPan(PanXSucursal slctipoPan) {
		this.slctipoPan = slctipoPan;
	}

	public List<PanXSucursal> getListaPrevio() {
		return listaPrevio;
	}

	public void setListaPrevio(List<PanXSucursal> listaPrevio) {
		this.listaPrevio = listaPrevio;
	}

	public Boolean getBandera() {
		return bandera;
	}

	public void setBandera(Boolean bandera) {
		this.bandera = bandera;
	}

	public CatTipoPan getCatTipoPan() {
		return catTipoPan;
	}

	public void setCatTipoPan(CatTipoPan catTipoPan) {
		this.catTipoPan = catTipoPan;
	}

	public CatUbicacion getCatUbicacion() {
		return catUbicacion;
	}

	public void setCatUbicacion(CatUbicacion catUbicacion) {
		this.catUbicacion = catUbicacion;
	}

	public String getNombreER() {
		return nombreER;
	}

	public void setNombreER(String nombreER) {
		this.nombreER = nombreER;
	}
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public Date getFechaConsulta() {
		return fechaConsulta;
	}

	public void setFechaConsulta(Date fechaConsulta) {
		this.fechaConsulta = fechaConsulta;
	}

	public List<PrecioCantidadSucursal> getListaPrevioPrecio() {
		return listaPrevioPrecio;
	}

	public void setListaPrevioPrecio(List<PrecioCantidadSucursal> listaPrevioPrecio) {
		this.listaPrevioPrecio = listaPrevioPrecio;
	}

}
