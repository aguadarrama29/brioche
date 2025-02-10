package com.panaderia.manageBan;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import com.panaderia.modelo.bo.VentaPanBO;
import com.panaderia.modelo.negocio.CatTipoPan;
import com.panaderia.modelo.negocio.CatUbicacion;
import com.panaderia.modelo.negocio.CatUsuario;
import com.panaderia.modelo.negocio.PanXSucursal;
import com.panaderia.modelo.negocio.RegistroVenta;
import com.panaderia.modelo.negocio.RegistroVentaId;
import com.panaderia.seguridad.UserSession;
import com.panaderia.servicio.IBriocheServicio;
import com.panaderia.util.MsgUtil;

@ManagedBean(name = "ventaMB")
@ViewScoped
public class VentaMBElimina implements Serializable {

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 2117618834389775863L;
	@ManagedProperty(value = "#{BriocheServicio}")
	IBriocheServicio iBriocheServicio;
	
	@ManagedProperty(value = "#{userSession}")
	UserSession userSession;

	private List<PanXSucursal> lista;
	private List<PanXSucursal> articuloVenta;
	Date fecha = new Date();
	private List<VentaPanBO> listaslc;
	private PanXSucursal panXSucursa;
	private VentaPanBO panXSucursal;

	//nueva version
	private double total;
	private double cantArticulo;
	private boolean mostrarVen;
	private CatTipoPan catTipoPan;

	public VentaMBElimina() {
	}

	@PostConstruct
	public void inicio() {
		
		lista = iBriocheServicio.obtenerPanXSucursalesXFecha(fecha, 3,userSession.getUsuario().getCatUbicacion().getId());
		
		System.out.println("entroo inicio");
	}

	public void elegirArticulo(CatTipoPan pan) {//PanXSucursal pan
		//System.out.println("arti" + pan.getCodigo());
		//catTipoPan=pan;
		//cantArticulo = 1;
		
	}
	
	
	public void prueba(CatTipoPan catTipoPan2) {
		System.out.println("queeee 55555555555555uuu"+catTipoPan2.getCodigo());
	}
	
	public void agregarArticulo() {
		
		try {
			System.out.println("queeee"+cantArticulo);
			System.out.println("queeee"+catTipoPan.getCodigo());
			//si la categoria tiene un valor mayor a 0 en descuento se aplicara 
			/*if(panXSucursa.getCatCategoria().getDescuento().intValue()>0) {
				BigDecimal desc=BigDecimal.ZERO;
				desc=panXSucursa.getCosto().multiply(panXSucursa.getCatCategoria().getDescuento()).divide(new BigDecimal(100));
				//System.out.println(desc);
				
				panXSucursa.setCosto(panXSucursa.getCosto().subtract(desc).setScale(2));
				//System.out.println(articulo.getCosto());
			}*/
			
				total = total + (panXSucursa.getCatTipoPan().getCosto() * cantArticulo); //antes 1
				
				if(panXSucursa==null) {
					articuloVenta= new ArrayList<>();
				}
				
				//agregar la cantidad vendida en ete articulo 
				panXSucursa.setCantVenta((float)cantArticulo);				
				articuloVenta.add(panXSucursa);
				
				//si ya tenemos algo apra vender mostrar boton VENTA
				if(articuloVenta.size()>0)
					mostrarVen=true;else
						mostrarVen=false;
				
				panXSucursa = new PanXSucursal();
				MsgUtil.msgInfo("Exito!", "Se agrego el articulo.");
			/*}else {
				articulo = new Articulo();
				MsgUtil.msgInfo("Error!", "El articulo no cuenta con Existencia.");
			}*/
				
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void cancelarArticulo() {
		panXSucursa= new PanXSucursal();
		cantArticulo=0;
	}
	
	//elimina solo el registro elegido
	public void eliminar(PanXSucursal panXSucursal) {
		System.out.println("ar.getCantVenta()"+panXSucursal.getCantVenta());
		
		double t1=panXSucursal.getCatTipoPan().getCosto()*panXSucursal.getCantVenta();
		
		try {			
			total=total-t1;		
			articuloVenta.remove(panXSucursal);
			
			//si el articulo es igualacion eliminar el id que se eligio
			System.out.println("que seraaa"+panXSucursal.getCatTipoPan().getCodigo());
			System.out.println("que seraaa"+panXSucursal.getCatTipoPan().getDescripcion());
			//this.eliminItemXId(panXSucursal.getCatTipoPan().getCodigo());
			
			//si ya tenemos algo apra vender mostrar boton VENTA
			if(articuloVenta.size()>0)
				mostrarVen=true;else
					mostrarVen=false;			
			
			MsgUtil.msgInfo("Exito!", "Registro Eliminado.");
		} catch (Exception e) {
			e.printStackTrace();
			MsgUtil.msgError("Error!", "Registro no eliminado.");
		}
	}
	
	public void limpiar() throws IOException {
		FacesContext.getCurrentInstance().getExternalContext().redirect("inicioUsuario.xhtml");
	}

	public void guardarVenta() {

		try {
			if (listaslc.size() > 0) {

				CatUbicacion ubi = new CatUbicacion();
				ubi.setId(userSession.getUsuario().getCatUbicacion().getId());

				CatUsuario usu = new CatUsuario();
				usu.setId(userSession.getUsuario().getId());

				Date fecha = new Date();
				RegistroVenta venta;
				RegistroVentaId rvid;

				Calendar cal = Calendar.getInstance();
				// cal.setTime(fecha);
				int anio = cal.get(Calendar.YEAR);
				int mes = cal.get(Calendar.MONTH) + 1;

				// folio por año y sucursal
				Integer con = getiBriocheServicio()
						.consecutivoXAnio(userSession.getUsuario().getCatUbicacion().getId());
				String folio = "";
				con++;

				// ubicacion a 3 cifras
				int totalnombreC = Integer.toString(userSession.getUsuario().getCatUbicacion().getId()).trim().length();
				int cadena = 3 - totalnombreC;
				String ub = ceros(cadena) + userSession.getUsuario().getCatUbicacion().getId();

				// consecutivo a 8 cifras
				int totalCon = Integer.toString(con).trim().length();
				int cadenaC = 8 - totalCon;
				String conse = ceros(cadenaC) + con;
				folio = ub + "-" + anio + "-" + conse;

				// agrupar la lista por codigo y saber cuantos hay de cada uno
				Map<String, Long> cuentaProductos = listaslc.stream()
						.collect(Collectors.groupingBy(VentaPanBO::getCodigo, Collectors.counting()));

				// System.out.println("cuentaa"+cuentaProductos);

				List<String> codigo = new ArrayList<>();
				// List<RegistroVenta> regi=new ArrayList<>();

				// convertirlo a una lista primero recorrer el hashmap
				for (Map.Entry<String, Long> entry : cuentaProductos.entrySet()) {
					// System.out.println("clave=" + entry.getKey() + ", valor=" +
					// entry.getValue()+"listaaaaaaaaaaa"+listaslc.size());

					// recorrer la lista original
					for (VentaPanBO q : listaslc) {
						// revisar que la clave del hash sea igual a uno en la lista original
						if (q.getCodigo() == entry.getKey()) {
							// revisar que solo se agrege a la nueva lista una unica vez cada clave
							if (!codigo.contains(entry.getKey())) {

								// formar el nuevo objeto
								rvid = new RegistroVentaId();
								rvid.setFolio(folio);
								rvid.setCodigoTipoPan(entry.getKey());

								venta = new RegistroVenta();
								venta.setId(rvid);
								venta.setCatTipoPan(q.getPan().getCatTipoPan());
								venta.setCantidad(entry.getValue().intValue());
								venta.setCatUbicacion(ubi);
								venta.setCatUsuario(usu);
								venta.setEstatus('V');
								venta.setFecha(fecha);
								venta.setConsecutivo(con);

								// agregar a la lista final de productos y agregar en lista
								// que limina que solo entre una vez cada clave
								codigo.add(entry.getKey());
								getiBriocheServicio().guardarRegistroVenta(venta);
								// regi.add(venta);
							}

						}
					}
				}
				MsgUtil.msgInfo("Exito!", "Registro Guardado.");
			} else {

				MsgUtil.msgError("Error!", "Debe agregar un registro para la venta.");
			}

			this.inicio();

		} catch (Exception e) {
			e.printStackTrace();
			MsgUtil.msgError("Error!", "Registro no procesado.");
		}

	}

	public String ceros(int numero) {

		String cero = "";
		for (int i = 0; i < numero; i++) {
			cero += "0";
		}
		return cero;
	}

	public UserSession getUserSession() {
		return userSession;
	}

	public void setUserSession(UserSession userSession) {
		this.userSession = userSession;
	}

	public List<PanXSucursal> getLista() {
		return lista;
	}

	public void setLista(List<PanXSucursal> lista) {
		this.lista = lista;
	}

	
	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public List<VentaPanBO> getListaslc() {
		return listaslc;
	}

	public void setListaslc(List<VentaPanBO> listaslc) {
		this.listaslc = listaslc;
	}

	public PanXSucursal getPanXSucursa() {
		return panXSucursa;
	}

	public void setPanXSucursa(PanXSucursal panXSucursa) {
		this.panXSucursa = panXSucursa;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}
	
	public VentaPanBO getPanXSucursal() {
		return panXSucursal;
	}

	public void setPanXSucursal(VentaPanBO panXSucursal) {
		this.panXSucursal = panXSucursal;
	}

	public IBriocheServicio getiBriocheServicio() {
		return iBriocheServicio;
	}

	public void setiBriocheServicio(IBriocheServicio iBriocheServicio) {
		this.iBriocheServicio = iBriocheServicio;
	}

	public double getCantArticulo() {
		return cantArticulo;
	}

	public void setCantArticulo(double cantArticulo) {
		this.cantArticulo = cantArticulo;
	}

	public List<PanXSucursal> getArticuloVenta() {
		return articuloVenta;
	}

	public void setArticuloVenta(List<PanXSucursal> articuloVenta) {
		this.articuloVenta = articuloVenta;
	}

	public boolean isMostrarVen() {
		return mostrarVen;
	}

	public void setMostrarVen(boolean mostrarVen) {
		this.mostrarVen = mostrarVen;
	}

	public CatTipoPan getCatTipoPan() {
		return catTipoPan;
	}

	public void setCatTipoPan(CatTipoPan catTipoPan) {
		this.catTipoPan = catTipoPan;
	}

	

}
