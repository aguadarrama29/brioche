package com.panaderia.manageBan;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.panaderia.modelo.negocio.ArticuloPedido;
import com.panaderia.modelo.negocio.CatCliente;
import com.panaderia.modelo.negocio.CatEstatusPedido;
import com.panaderia.modelo.negocio.CatTipoPan;
import com.panaderia.modelo.negocio.Pedido;
import com.panaderia.seguridad.UserSession;
import com.panaderia.servicio.IBriocheServicio;
import com.panaderia.util.MsgUtil;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

@ManagedBean(name = "pedidoMB")
@ViewScoped
public class PedidoMB implements Serializable{	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8092379298253441232L;

	private static final Logger logger = LoggerFactory.getLogger(PedidoMB.class);

	@ManagedProperty(value = "#{BriocheServicio}")
	IBriocheServicio iBriocheServicio;
	@ManagedProperty(value = "#{userSession}")
	UserSession userSession;
	
	private DateFormat fecEnvioFormato = new SimpleDateFormat("yyyy-MM-dd");
	private  SimpleDateFormat getYearFormat = new SimpleDateFormat("yyyy");
	
	private Integer accion;
	private List<Pedido> pedidos;
	private List<Pedido> listaFilter;
	private Pedido pedido;
	private Date fechaHoy;
	private CatCliente cliente;
	private List<CatCliente> listaClientes;
	private Integer idCliente;
	private Integer anio;
	private List<CatEstatusPedido> estatus;
	private Integer idCatEstPedido;
	
	private double total;
	//private Articulo articulo;
	private CatTipoPan articulo;
	private BigDecimal cantidad;
	
	private List<ArticuloPedido> artPedido;
	private ArticuloPedido artP;
	private Integer verMapa;
	
	private String mes;
	private String dia;
	
	public PedidoMB() {
		
	}

	@PostConstruct
	public void inicio() {		
		listaFilter= new ArrayList<Pedido>();
		pedido= new Pedido();
		fechaHoy= new Date();
		cliente= new CatCliente();
		listaClientes= new ArrayList<CatCliente>();
		
		estatus= new ArrayList<>();
		idCliente=0;
		idCatEstPedido=0;
		
		//pedidos= getiPinturaServicio().obtenerPedidos();
		Date date = new Date();       
        String currentYear = getYearFormat.format(date);
		anio= Integer.parseInt(currentYear);
		total=0;
		
		articulo= new CatTipoPan();
		artPedido= new ArrayList<>();
		verMapa=0;
		
		//obtener pedidos del mes
		Calendar fecha = Calendar.getInstance();
        Integer mesi = fecha.get(Calendar.MONTH) + 1;
       // Integer diai = fecha.get(Calendar.DAY_OF_MONTH);
        
        if(mesi.toString().length()==1) {
        	 mes = "0"+Integer.toString(mesi);
        }else {
        	 mes = Integer.toString(mesi);
        }
        dia = "0";
		
		pedidos=getiBriocheServicio().obtenerEventoXMD(mes,dia);
	}
	
	
	public void agregar() {
		accion=1;	
		listaClientes=getiBriocheServicio().obtenerClientes();
		estatus=getiBriocheServicio().obtenerCatEstatusPedidos();
		pedido.setCatEstatusPedido(new CatEstatusPedido());
		
		idCliente=0;
		idCatEstPedido=1;
		
	}
	
	public void editar(Pedido u) {
		listaClientes=getiBriocheServicio().obtenerClientes();
		estatus=getiBriocheServicio().obtenerCatEstatusPedidos();
		pedido = u;
		idCliente=pedido.getCatCliente().getId();
		idCatEstPedido=pedido.getCatEstatusPedido().getId();
		accion=2;
	}
	
	public void agregarProducto(Pedido u) {
		pedido = u;
		artPedido=getiBriocheServicio().obtenerArticuloPXFolio(pedido.getFolio());
		
		if(pedido.getTotal()!=null)
			total=pedido.getTotal().doubleValue();
	}
	
	public void cancelar() {
		pedido = new Pedido();
	}
	
	public void guardar() {
		try {
			//guardar promovido
			CatCliente cl= new CatCliente();
			cl.setId(idCliente);
			pedido.setCatCliente(cl);
			
			CatEstatusPedido ep= new CatEstatusPedido();
			ep.setId(idCatEstPedido);
			pedido.setCatEstatusPedido(ep);
			
			if (accion==1) {	
				//calcular folio
				long conseMuni=getiBriocheServicio().consePedidoXAnio(anio);
				conseMuni+=1;	
				//el folio es de 10  posiciones lo relleno con 0
				int ceros=5-String.valueOf(conseMuni).length();
				String ce=ceros(ceros)+conseMuni+anio.toString();
				
				pedido.setFolio(ce);
		        pedido.setAnio(anio);
		        
		       	
		        getiBriocheServicio().guardarPedido(pedido);
			    MsgUtil.msgInfo("Exito!", "Registro Guardado.");
			} else {
				
				/*if(pedido.getCatEstatusPedido().getId()==2) {
					//SI EL ESTATUS ES 2(REG TIENDA) Y CATEGORIA ES 
					//SE REGRESA A INV LAS CANTIDADES
					artPedido=getiBriocheServicio().obtenerArticuloPXFolio(pedido.getFolio());
					for (ArticuloPedido ap : artPedido) {
						//solo categoria de rentables se regresa existencia 
						if((ap.getArticulo().getCatCategoria().getId()>=26 && ap.getArticulo().getCatCategoria().getId()<=32) ||
								(ap.getArticulo().getCatCategoria().getId()>=41 && ap.getArticulo().getCatCategoria().getId()<=47) ) {
							
							Articulo ai=new Articulo();
							Integer exisOriginal=null;
							Integer exisNueva=null;
							
							ai=ap.getArticulo();
							//System.out.println("articulo"+ai.getCodigo());
							exisOriginal=ai.getExistencia();
							exisNueva=exisOriginal+ap.getCantidad().intValue();
							//System.out.println("existencia nueva"+exisNueva);
							ai.setExistencia(exisNueva);
							
							getiBriocheServicio().actualizarArticulo(ai);
						}
						
					}	
					
				}*/
				
				//actualziar el pedido
				getiBriocheServicio().actualizarPedido(pedido);
				
				MsgUtil.msgInfo("Exito!", "Registro Actualizado.");
				
			}
			
			this.inicio();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//obtener clientes por nombre
	public List<CatCliente> completeClientes(String query) {
		List<CatCliente> articulos = new ArrayList<CatCliente>();
		articulos=getiBriocheServicio().obtenerClientes(); //obtenerArticulos
		
		List<CatCliente> filtrados = new ArrayList<CatCliente>();

		for (int i = 0; i < articulos.size(); i++) {
			CatCliente c = articulos.get(i);
			if (c.getDescripcion() != null) {
				if (c.getDescripcion().toUpperCase().contains(query.toUpperCase())) {
					filtrados.add(c);
				}
			}
		}
		return filtrados;
	}
	
	
	//obtener atos del Cliente
	public void cargaDatos() {
		//System.out.println("que traigooo"+idCliente);
		if(idCliente==0) {
			pedido.setNombre("");
		}else {			
			cliente = getiBriocheServicio().obtenerClienteXId(idCliente);	
			if(cliente!=null) {
				if(cliente.getId()!=4) {
					pedido.setNombre(cliente.getDescripcion());
					pedido.setDomicilio(cliente.getDireccion());
					pedido.setTelefono(cliente.getTelefono());
				}else {
					pedido.setNombre("");
					pedido.setDomicilio("");
					pedido.setTelefono("");
				}
				
			}
		}
		
	}
	
	public String ceros(int numero) {		
		String cero="";
		for (int i = 0; i < numero; i++) {
			cero+="0";
		}
		return cero;
	}	
	
	
	public void limpiar() throws IOException {
		FacesContext.getCurrentInstance().getExternalContext().redirect("pedido.xhtml");
	}
	
	//obtener los articulos por tipo
	public List<CatTipoPan> completeArticulosDes(String query) {
		List<CatTipoPan> articulos = new ArrayList<CatTipoPan>();
		articulos=getiBriocheServicio().obtenerTiposPan(); //obtenerArticulosobtenerArtXTipo(1)
		
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
	
	
	public BigDecimal obtenerFaltante(Pedido p) {
		BigDecimal resultado= BigDecimal.ZERO;
		
		if(p.getTotal()!=null && p.getACuenta()!=null)
			resultado=p.getTotal().subtract(p.getACuenta());
		
		return resultado;
		
	}
	
	//metodos para elegir articulo y su cantidad
	public void elegirArticulo() {
		//System.out.println("arti"+articulo.getDescripcion());
		cantidad=null;
	}
	
	public void agregarArticulo() {
		//System.out.println("Cantidad"+cantidad+ " foliooo"+pedido.getFolio());
		
		artP=new ArticuloPedido();
		artP.setCatTipoPan(articulo);
		artP.setCantidad(cantidad);
		try {
			//if(articulo.getExistencia()>0 && articulo.getExistencia()>=cantidad.intValue()) {
				total = total + (articulo.getCosto() * cantidad.doubleValue());
				
				//se daa el registro de una vez
				//artPedido.add(artP);
				artP.setFolio(pedido.getFolio());;
				getiBriocheServicio().guardarArticuloPedido(artP);
				artPedido=getiBriocheServicio().obtenerArticuloPXFolio(pedido.getFolio());
				
				//dar de baja en inventario
				/*Integer cantOri=articulo.getExistencia();
				Integer exisNueva=cantOri-cantidad.intValue();
				articulo.setExistencia(exisNueva);
				getiBriocheServicio().actualizarArticulo(articulo);*/
				
				articulo = new CatTipoPan();
				MsgUtil.msgInfo("Exito!", "Se agrego el articulo.");
			/*}else {
				articulo = new Articulo();
				MsgUtil.msgInfo("Error!", "El articulo no cuenta con existencia suficiente.");
			}*/
				
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//elimina solo el registro elegido
	public void eliminarAP(ArticuloPedido ar) {
		
		double t1=ar.getCatTipoPan().getCosto()*ar.getCantidad().doubleValue();
		try {			
			total=total-t1;	
			getiBriocheServicio().eliminaArticuloPedido(ar);
			artPedido.remove(ar);		
			
			//regresr la existencia a incentario
			/*CatTipoPan art=getiBriocheServicio().obtenerTipoPanXCodigo(ar.getCatTipoPan().getCodigo());
			Integer exisActual=art.getExistencia();
			Integer exisNueva=exisActual+ar.getCantidad().intValue();
			
			art.setExistencia(exisNueva);
			getiBriocheServicio().actualizarArticulo(art);*/
			
			
			MsgUtil.msgInfo("Exito!", "Registro Eliminado.");
		} catch (Exception e) {
			e.printStackTrace();
			MsgUtil.msgError("Error!", "Registro no eliminado.");
		}
	}
	
	
	public void guardarTotales() {
		/*System.out.println("quefolio menjo"+pedido.getFolio());
		System.out.println("total"+total);
		System.out.println("a cuenta"+pedido.getACuenta());*/
		
		pedido.setTotal(new BigDecimal(total));
		try {
			getiBriocheServicio().actualizarPedido(pedido);
			MsgUtil.msgInfo("Exito!", "Registro Actualizado.");
		} catch (Exception e) {
			e.printStackTrace();
			MsgUtil.msgError("Error!", "Registro no Actualizado.");
		}
	}
	//
	
	
	
	
	public void mostrarNota(Pedido p) throws Exception {
		System.out.println("****************"+p.getFolio());
		
		ExternalContext context;
		FacesContext facesContext = FacesContext.getCurrentInstance();
		context = facesContext.getExternalContext();
		ExternalContext externalContext = facesContext.getExternalContext();
		HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
		String absoluteDiskPath = context.getRealPath("/resources/");
		String reportPath = "";
		
		reportPath = FacesContext.getCurrentInstance().getExternalContext()
				.getRealPath("/resources/reportes/notaPedido.jasper");
		JasperPrint jasperPrint;
		HashMap<String, Object> parametros = new HashMap<String, Object>();
		
		//Date date1=new SimpleDateFormat("yyyy-MM-dd").parse(fecEnvioFormato.format(fecEnvio));
		
		 parametros.put("patch", absoluteDiskPath);
		 parametros.put("folio",p.getFolio());
		

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
			response.setHeader("Content-Disposition", "inline; filename=\"" + "Nota"+p.getFolio()+".pdf" + "\"");
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
	
	
	//para los colores en base a fecha del evento contraq feha de hoy
	public int mostrarPR(Date fechaTabla) {
		int valorFinal;
		if(fechaTabla!=null) {
			
			//dias entre fechas
			long fechaInicialMs = fechaTabla.getTime();
			long fechaFinalMs = fechaHoy.getTime();
			long diferencia =  fechaInicialMs- fechaFinalMs;
			double dias = Math.floor(diferencia / (1000 * 60 * 60 * 24));
			
			
			//System.out.println("diasssssss"+dias);
			/*mayor a 7 verde		1
			5 a 6  amarillo 	2
			2 a  4  naranja 	3
			si es igual rojo 	4*/
			
			if(dias>=7)
				valorFinal=1;else if(dias<=6 && dias>5)
					valorFinal=2;else if(dias<=4 && dias>=2)
						valorFinal=3;else 
							valorFinal=4;
		}else
			valorFinal=0;
		
		
		//System.out.println("que vnio"+valorFinal);
		
		
		return valorFinal;
	}
	
	public void MapaCoor(Pedido p) {
		pedido=p;
		verMapa=1;
	}
		
	public void obtenerEventos() {		
		pedidos = new ArrayList<>();
		if(mes==null && dia==null) {
			Calendar fecha = Calendar.getInstance();
	     //   Integer anioi = fecha.get(Calendar.YEAR);
	        Integer mesi = fecha.get(Calendar.MONTH) + 1;
	        Integer diai = fecha.get(Calendar.DAY_OF_MONTH);
	        
	        if(mesi.toString().length()==1) {
	        	 mes = "0"+Integer.toString(mesi);
	        }else {
	        	 mes = Integer.toString(mesi);
	        }
	        dia = Integer.toString(diai);
		}
		
		pedidos=getiBriocheServicio().obtenerEventoXMD(mes,dia);
	}
	
	
	public UserSession getUserSession() {
		return userSession;
	}

	public void setUserSession(UserSession userSession) {
		this.userSession = userSession;
	}

	public Integer getAccion() {
		return accion;
	}

	public void setAccion(Integer accion) {
		this.accion = accion;
	}

	public List<Pedido> getPedidos() {
		return pedidos;
	}

	public void setPedidos(List<Pedido> pedidos) {
		this.pedidos = pedidos;
	}

	public List<Pedido> getListaFilter() {
		return listaFilter;
	}

	public void setListaFilter(List<Pedido> listaFilter) {
		this.listaFilter = listaFilter;
	}

	public Pedido getPedido() {
		return pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}

	public Date getFechaHoy() {
		return fechaHoy;
	}

	public void setFechaHoy(Date fechaHoy) {
		this.fechaHoy = fechaHoy;
	}

	public CatCliente getCliente() {
		return cliente;
	}

	public void setCliente(CatCliente cliente) {
		this.cliente = cliente;
	}

	public List<CatCliente> getListaClientes() {
		return listaClientes;
	}

	public void setListaClientes(List<CatCliente> listaClientes) {
		this.listaClientes = listaClientes;
	}

	public Integer getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Integer idCliente) {
		this.idCliente = idCliente;
	}

	public Integer getAnio() {
		return anio;
	}

	public void setAnio(Integer anio) {
		this.anio = anio;
	}

	public List<CatEstatusPedido> getEstatus() {
		return estatus;
	}

	public void setEstatus(List<CatEstatusPedido> estatus) {
		this.estatus = estatus;
	}

	public Integer getIdCatEstPedido() {
		return idCatEstPedido;
	}

	public void setIdCatEstPedido(Integer idCatEstPedido) {
		this.idCatEstPedido = idCatEstPedido;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public BigDecimal getCantidad() {
		return cantidad;
	}

	public void setCantidad(BigDecimal cantidad) {
		this.cantidad = cantidad;
	}

	public List<ArticuloPedido> getArtPedido() {
		return artPedido;
	}

	public void setArtPedido(List<ArticuloPedido> artPedido) {
		this.artPedido = artPedido;
	}

	public Integer getVerMapa() {
		return verMapa;
	}

	public void setVerMapa(Integer verMapa) {
		this.verMapa = verMapa;
	}

	public String getMes() {
		return mes;
	}

	public void setMes(String mes) {
		this.mes = mes;
	}

	public String getDia() {
		return dia;
	}

	public void setDia(String dia) {
		this.dia = dia;
	}

	public IBriocheServicio getiBriocheServicio() {
		return iBriocheServicio;
	}

	public void setiBriocheServicio(IBriocheServicio iBriocheServicio) {
		this.iBriocheServicio = iBriocheServicio;
	}

	public CatTipoPan getArticulo() {
		return articulo;
	}

	public void setArticulo(CatTipoPan articulo) {
		this.articulo = articulo;
	}	

}
