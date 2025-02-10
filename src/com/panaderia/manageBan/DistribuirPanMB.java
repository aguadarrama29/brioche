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

import com.panaderia.modelo.negocio.CatListaInicial;
import com.panaderia.modelo.negocio.CatTipoPan;
import com.panaderia.modelo.negocio.CatUbicacion;
import com.panaderia.modelo.negocio.PanXSucursal;
import com.panaderia.modelo.negocio.PanXSucursalId;
import com.panaderia.modelo.negocio.PrecioCantidadSucursal;
import com.panaderia.modelo.negocio.PrecioCantidadSucursalId;
import com.panaderia.seguridad.UserSession;
import com.panaderia.servicio.IBriocheServicio;
import com.panaderia.util.MsgUtil;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@ManagedBean(name = "distribuirMB")
@ViewScoped
public class DistribuirPanMB implements Serializable {

	private static final long serialVersionUID = -1055406860713559970L;

	private static final Logger logger = Logger.getLogger(DistribuirPanMB.class);

	@ManagedProperty(value = "#{BriocheServicio}")
	IBriocheServicio iBriocheServicio;
	@ManagedProperty(value = "#{userSession}")
	UserSession userSession;

	private List<CatTipoPan> lista;
	private List<CatTipoPan> listaFilter;
	private CatTipoPan slc;

	private int index = 0;
	private List<PanXSucursal> listaPrevio;
	
	
	
	private CatUbicacion catUbicacion;
	private PanXSucursal panXSucursal;
	Date fechaActual = new Date();
	private Date fecCompromiso;
	private int idSucursal = 0;
	//saber si vamos a sumar o restar pan
	private int tipoMov;
	private int cantMov;
	
	private float totalVenta;
	private int tipoAccion;

	private List<PrecioCantidadSucursal> listaPrevioPrecio;
	private PrecioCantidadSucursal precioCSucursal;
	
	public DistribuirPanMB() {

	}

	@PostConstruct
	public void inicio() {
		lista= new ArrayList<>();
		
		listaPrevio = new ArrayList<>();
		catUbicacion = new CatUbicacion();

		panXSucursal = new PanXSucursal();
		fecCompromiso = null;

		idSucursal = 6;
		tipoMov=0;
		cantMov=0;
		
		tipoAccion=0;
		
		listaPrevioPrecio = new ArrayList<>();
		precioCSucursal=new PrecioCantidadSucursal();

	}

	public void onTabChange(TabChangeEvent event) {
		// System.out.println("llegoo"+event.getTab().getTitle());
		// System.out.println("llegoo"+index);
		if (index == 1) {
			// si es administrador trae todo
			if (userSession.getUsuario().getCatPerfil().getId() == 2) {
				// si es usuario trae lo de su sucursal
			} else {
				// lista =
				// getiBriocheServicio().obtenerPanXSucursalesXFecha(fecha,1,userSession.getUsuario().getCatUbicacion().getId());
			}

		}

	}

	public List<CatUbicacion> getlistaSucursales() {
		return getiBriocheServicio().obtenerUbicaciones();
	}

	public void reporteTipoPan() {

		ExternalContext context;
		FacesContext facesContext = FacesContext.getCurrentInstance();
		context = facesContext.getExternalContext();
		ExternalContext externalContext = facesContext.getExternalContext();
		HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
		String absoluteDiskPath = context.getRealPath("/resources/");
		String reportPath = FacesContext.getCurrentInstance().getExternalContext()
				.getRealPath("/resources/reportes/reporteConcertacion.jasper");

		JasperPrint jasperPrint;
		HashMap<String, Object> parametros = new HashMap<String, Object>();

		JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(lista);

		parametros.put("patch", absoluteDiskPath);
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

	public void sucursal() {
		idSucursal = catUbicacion.getId();
		lista=new ArrayList<>();
		
		System.out.println(idSucursal+"   "+tipoAccion);
		
		CatTipoPan tp;
		if(tipoAccion==1) {
			List<CatListaInicial> ini= getiBriocheServicio().listaInicialXSucursal(idSucursal);
			for (CatListaInicial li : ini) {
			  tp = getiBriocheServicio().obtenerTipoPanXCodigo(li.getId().getCodigopan());
			  tp.setCantidad(li.getCantidad());
			  lista.add(tp);
			}
		}else {
			lista = getiBriocheServicio().obtenerTiposPan();
		}
		
	}
	
	public void accionTipo() {
		catUbicacion = new CatUbicacion();
		lista= new ArrayList<>();
	}
	
	

	public void guardarPanSucursal() {
		//System.out.println("ubicacion" + catUbicacion.getId() + "  " + catUbicacion.getDescripcion() + "      " + idSucursal);
		listaPrevio = new ArrayList<>();
		try {
			// recorrer la lista de panes y generar lista para sucursal
			for (CatTipoPan pan : lista) {
				// solo guardar pan que sea mayor a 0
				if (pan.getCantidad() != 0) {
					panXSucursal = new PanXSucursal();
					panXSucursal.setId(new PanXSucursalId());
					// usuario1 admin2 tipo1 es origen tipo2 destino(cambiara a q tipo 1 es venta 2
					// cancelado y otros que salgan)
					// esto si esta online en otro caso seria la idea original
					if (userSession.getUsuario().getCatPerfil().getId() == 1) {
						panXSucursal.getId().setTipo(2);
						panXSucursal.setRecibe("empleado");
					} else {
						panXSucursal.getId().setTipo(1);
						panXSucursal.setEntrega(userSession.getUsuario().getDescripcion());
					}

					panXSucursal.getId().setCodigopan(pan.getCodigo());
					panXSucursal.getId().setIdUbicacion(idSucursal);// catUbicacion.getId()
					panXSucursal.getId().setFecha(fechaActual);

					panXSucursal.setCatTipoPan(pan);

					CatUbicacion cat = new CatUbicacion();
					cat.setId(idSucursal);
					panXSucursal.setCatUbicacion(cat);
					panXSucursal.setCantidad(pan.getCantidad());
					panXSucursal.setComentario(pan.getComentaDistribu());
					panXSucursal.setEstatus(1);
					
					panXSucursal.setCantidadTPXDia(pan.getCantidad());
					listaPrevio.add(panXSucursal);
					
					
					//ahora por precios alimntar un inventario 
					precioCSucursal = new PrecioCantidadSucursal();
					PrecioCantidadSucursalId pcid = new PrecioCantidadSucursalId();
					pcid.setCosto(pan.getCosto());
					pcid.setIdUbicacion(cat.getId());
					pcid.setFecha(new Date());
					
					precioCSucursal.setId(pcid);
					precioCSucursal.setCantidad(pan.getCantidad());
					precioCSucursal.setCantidadVendida(pan.getCantidad());
					precioCSucursal.setCatUbicacion(cat);
					precioCSucursal.setEstatus(1);
					
					listaPrevioPrecio.add(precioCSucursal);
					
				}
			}

			// guardar los registros
			for (PanXSucursal pansuc : listaPrevio) {
				int existenciaF=0;
				int existeTP=0;
				
				//saber si ya existe
				PanXSucursal panExiste = getiBriocheServicio().panXSucXFechaXCodigo(pansuc.getId().getCodigopan(),
						pansuc.getId().getIdUbicacion(), pansuc.getId().getFecha());
				//si existe solo atualizar cantidad
				if(panExiste!=null) {
					existenciaF=panExiste.getCantidad()+pansuc.getCantidad();
					existeTP=panExiste.getCantidadTPXDia()+pansuc.getCantidad();
					pansuc.setCantidad(existenciaF);		
					pansuc.setCantidadTPXDia(existeTP);					
					getiBriocheServicio().actualizarPanXSucursal(pansuc);
				}else {
					getiBriocheServicio().guardarPanXSucursal(pansuc);
				}
			}
			
			
			// guardar los registros por precio solamente
			for (PrecioCantidadSucursal pcs : listaPrevioPrecio) {
				int existenciaInicial=0;
				int existeVendida=0;
				
				//saber si ya existePrecioCantidadSucursal
				PrecioCantidadSucursal panExistePrecio = getiBriocheServicio().precioXSucXFechaXPrecio(pcs.getId().getCosto(),
						pcs.getCatUbicacion().getId(), pcs.getId().getFecha());
				
				
				//si existe solo atualizar cantidad
				if(panExistePrecio!=null) {
					existenciaInicial=panExistePrecio.getCantidad()+pcs.getCantidad();
					existeVendida=panExistePrecio.getCantidadVendida()+pcs.getCantidad();
					pcs.setCantidad(existenciaInicial);		
					pcs.setCantidadVendida(existeVendida);					
					getiBriocheServicio().actualizarPrecioCantidadSucursal(pcs);
				}else {
					getiBriocheServicio().guardarPrecioCantidadSucursal(pcs);
				}
			}

			MsgUtil.msgInfo("Exito!", "Resgistros  Guardados con Exito.");
			this.inicio();
		} catch (Exception e) {
			e.printStackTrace();
			MsgUtil.msgError("Error!", "Registros no procesados.");
		}

	}

	// como administrador editar algun registro de la distribucion
	public void editar(PanXSucursal panXSucursalNuevo) {
		panXSucursal = panXSucursalNuevo;
	}

	public void actualizarRegDistribucion() {
		int canActual=panXSucursal.getCantidad();
		int totNuevo=0;
		
		int totGeneral=0;
		int totActual=panXSucursal.getCantidadTPXDia();
		
		if(tipoMov==1) {
			totNuevo=canActual+cantMov;
			totGeneral=totActual+cantMov;
		}else {
			totNuevo=canActual-cantMov;
			totGeneral=totActual-cantMov;
		}
				
		panXSucursal.setCantidad(totNuevo);
		panXSucursal.setCantidadTPXDia(totGeneral);
		try {
			getiBriocheServicio().actualizarPanXSucursal(panXSucursal);
			MsgUtil.msgInfo("Exito!", "Registro Actualizado.");
		} catch (Exception e) {
			e.printStackTrace();
			MsgUtil.msgError("Error!", "Registro no procesado.");
		}

	}

	public void cargaInvXsucursal() {
		// System.out.println("qqq "+fecCompromiso+1+" "+catUbicacion.getId());
		listaPrevio = getiBriocheServicio().obtenerPanXSucursalesXFecha(fecCompromiso, 4, catUbicacion.getId());
		totalVenta=0f;
		for (PanXSucursal ps : listaPrevio) {
			totalVenta+=(ps.getCantidad()*ps.getCatTipoPan().getCosto());
		}
	}

	public void limpiar() throws IOException {
		FacesContext.getCurrentInstance().getExternalContext().redirect("ERPan.xhtml");
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

	public CatTipoPan getSlc() {
		return slc;
	}

	public void setSlc(CatTipoPan slc) {
		this.slc = slc;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public List<PanXSucursal> getListaPrevio() {
		return listaPrevio;
	}

	public void setListaPrevio(List<PanXSucursal> listaPrevio) {
		this.listaPrevio = listaPrevio;
	}

	public CatUbicacion getCatUbicacion() {
		return catUbicacion;
	}

	public void setCatUbicacion(CatUbicacion catUbicacion) {
		this.catUbicacion = catUbicacion;
	}

	public PanXSucursal getPanXSucursal() {
		return panXSucursal;
	}

	public void setPanXSucursal(PanXSucursal panXSucursal) {
		this.panXSucursal = panXSucursal;
	}

	public Date getFecCompromiso() {
		return fecCompromiso;
	}

	public void setFecCompromiso(Date fecCompromiso) {
		this.fecCompromiso = fecCompromiso;
	}

	public int getTipoMov() {
		return tipoMov;
	}

	public void setTipoMov(int tipoMov) {
		this.tipoMov = tipoMov;
	}

	public int getCantMov() {
		return cantMov;
	}

	public void setCantMov(int cantMov) {
		this.cantMov = cantMov;
	}

	public float getTotalVenta() {
		return totalVenta;
	}

	public void setTotalVenta(float totalVenta) {
		this.totalVenta = totalVenta;
	}

	public int getTipoAccion() {
		return tipoAccion;
	}

	public void setTipoAccion(int tipoAccion) {
		this.tipoAccion = tipoAccion;
	}

}
