package com.panaderia.manageBan;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

import com.panaderia.modelo.negocio.CatListaInicial;
import com.panaderia.modelo.negocio.CatTipoPan;
import com.panaderia.modelo.negocio.CatUbicacion;
import com.panaderia.modelo.negocio.CatUsuario;
import com.panaderia.modelo.negocio.PanXSucursal;
import com.panaderia.modelo.negocio.PanXSucursalId;
import com.panaderia.modelo.negocio.PrecioCantidadSucursal;
import com.panaderia.modelo.negocio.PrecioCantidadVenta;
import com.panaderia.modelo.negocio.PrecioCantidadVentaId;
import com.panaderia.modelo.negocio.RegistroVenta;
import com.panaderia.modelo.negocio.RegistroVentaId;
import com.panaderia.seguridad.UserSession;
import com.panaderia.servicio.IBriocheServicio;
import com.panaderia.util.MsgUtil;
import com.panaderia.util.PrintExamples;
import com.panaderia.util.PrinterService;

@ManagedBean(name = "registroVentaMB")
@ViewScoped
public class RegistroVentaController implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4852379740209328096L;
	Date fecha = new Date();
	private List<PanXSucursal> lista;
	private PanXSucursal panXSucursal;
	private float cantArticulo;
	private float total;
	private boolean mostrarVen;
	private List<PanXSucursal> articuloVenta;
	String folioVF;
	private BigDecimal efectivo;
	private BigDecimal cambio;

	// ticket
	private List<RegistroVenta> ventaTicket;
	private DateFormat fecEnvioFormato = new SimpleDateFormat("dd-MM-yyyy");
	
	private boolean imprimeTicket;
	
	private int idSucursal;
	private List<CatTipoPan> listaIni;
	private List<PanXSucursal> listaPrevio;
	
	
	//por precios
	private List<PrecioCantidadSucursal> costos;
	private PrecioCantidadSucursal precioCantidad;
	private List<PrecioCantidadSucursal> articuloVentaPrecio;
	private int tipoVenta;
	
	private List<PrecioCantidadVenta> ventaTicketPrecio;
	
	private String nombreImpresora="";
	

	public RegistroVentaController() {

	}

	@PostConstruct
	public void inicio() {
		
		//dos lineas no van al ponerlo en el vps
		PrintExamples.printAvailable();
		nombreImpresora=PrintExamples.printDefault();
		
		listaIni= new ArrayList<>();
		listaPrevio = new ArrayList<>();
		lista = new ArrayList<>();
		costos = new ArrayList<>();
		
		cantArticulo = 1F;
		lista = iBriocheServicio.obtenerPanXSucursalesXFecha(fecha, 3,
				userSession.getUsuario().getCatUbicacion().getId());
		
		//obtener la lista por precio en esattus 2
		costos=iBriocheServicio.obtenerPrecioXSucursalesXFecha(fecha, 3,
				userSession.getUsuario().getCatUbicacion().getId());
		
		
		articuloVenta = new ArrayList<>();
		total = 0;
		ventaTicket = new ArrayList<>();

		efectivo = BigDecimal.ZERO;
		cambio = BigDecimal.ZERO;
		
		idSucursal=userSession.getUsuario().getCatUbicacion().getId();
		
		 List<PanXSucursal> listaTemp = iBriocheServicio.obtenerPanXSucursalesXFecha(fecha, 1,idSucursal);
		 
		 //Cambio por Precio
		 articuloVentaPrecio = new ArrayList<>();
		 tipoVenta=1;//1 venta 2Presuouesto 3cancelado 4Muestra
		 ventaTicketPrecio = new ArrayList<>();
		 
	}
	
	public void cargaInicial() {
		listaIni=new ArrayList<>();
		
		CatTipoPan tp;
		List<CatListaInicial> ini= getiBriocheServicio().listaInicialXSucursal(idSucursal);
		for (CatListaInicial li : ini) {
		  tp = getiBriocheServicio().obtenerTipoPanXCodigo(li.getId().getCodigopan());
		  tp.setCantidad(li.getCantidad());
		  listaIni.add(tp);
		}
		
		System.out.println("que ondacarga inicial"+listaIni.size());
		  this.guardarPanSucursal();
	}
	
	public void guardarPanSucursal() {
		System.out.println("guardarr"+listaIni.size());
		//System.out.println("ubicacion" + catUbicacion.getId() + "  " + catUbicacion.getDescripcion() + "      " + idSucursal);
		listaPrevio = new ArrayList<>();
		panXSucursal = new PanXSucursal();
		try {
			// recorrer la lista de panes y generar lista para sucursal
			for (CatTipoPan pan : listaIni) {
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
					panXSucursal.getId().setFecha(fecha);

					panXSucursal.setCatTipoPan(pan);

					CatUbicacion cat = new CatUbicacion();
					cat.setId(idSucursal);
					panXSucursal.setCatUbicacion(cat);
					panXSucursal.setCantidad(pan.getCantidad());
					panXSucursal.setComentario(pan.getComentaDistribu());
					panXSucursal.setEstatus(1);
					
					panXSucursal.setCantidadTPXDia(pan.getCantidad());
					listaPrevio.add(panXSucursal);
				}
			}

			// guardar los registros
			for (PanXSucursal pansuc : listaPrevio) {
				int existenciaF=0;
				
				//saber si ya existe
				PanXSucursal panExiste = getiBriocheServicio().panXSucXFechaXCodigo(pansuc.getId().getCodigopan(),
						pansuc.getId().getIdUbicacion(), pansuc.getId().getFecha());
				//si existe solo atualizar cantidad
				if(panExiste!=null) {
					existenciaF=panExiste.getCantidad()+pansuc.getCantidad();
					pansuc.setCantidad(existenciaF);		
					pansuc.setCantidadTPXDia(existenciaF);
					getiBriocheServicio().actualizarPanXSucursal(pansuc);
				}else {
					getiBriocheServicio().guardarPanXSucursal(pansuc);
				}
			}

			MsgUtil.msgInfo("Exito!", "Resgistros  Guardados con Exito.");
			this.inicio();
		} catch (Exception e) {
			e.printStackTrace();
			MsgUtil.msgError("Error!", "Registros no procesados.");
		}

	}

	//pide la cantidad de piezas de pan
	public void agrega(PanXSucursal pan) {
		// System.out.println("agregaa " + pan.getCatTipoPan().getCodigo());
		cantArticulo = 1;
		panXSucursal = new PanXSucursal();
		panXSucursal = pan;
		// System.out.println("agregaa " + panXSucursal.getCatTipoPan().getCodigo()+"
		// "+panXSucursal.getCantVenta());
	}
	
	//agrega de uno por uno
	public void agregaXPieza(PanXSucursal pan) {
		cantArticulo = 1;
		panXSucursal = new PanXSucursal();
		panXSucursal = pan;
		this.agregarArticulo();
		
	}

	public void agregarArticulo() {
		int indice = -1;
		int contador = -1;
		float cantidadV = 0;
		
		float existencia=panXSucursal.getCantidad()-cantArticulo;
		
		try {
			
			if(existencia>=0.0) {
				total = total + ((float) (panXSucursal.getCatTipoPan().getCosto()) * cantArticulo);

				/*
				 * if (panXSucursal == null) { articuloVenta = new ArrayList<>(); }
				 */
				System.out.println("uuuuuuuuuuuu" + articuloVenta.size());
				// es la primera vez no tiene nada la lista
				if (articuloVenta.size() == 0) {
					System.out.println("primera vez");
					// agregar la cantidad vendida en ete articulo
					panXSucursal.setCantVenta(cantArticulo);
					panXSucursal.setCodigoRepetido(panXSucursal.getCatTipoPan().getCodigo());
					articuloVenta.add(panXSucursal);
				} else {
					for (PanXSucursal p : articuloVenta) {
						contador++;
						if (p.getCatTipoPan().getCodigo().equals(panXSucursal.getCatTipoPan().getCodigo())) {
							indice = contador;
							cantidadV = p.getCantVenta();
						}

					}
					System.out.println("gggggggggggg" + indice);

					if (indice == -1) {
						System.out.println("no existe lo agregare");
						// agregar la cantidad vendida en ete articulo
						panXSucursal.setCantVenta(cantArticulo);
						panXSucursal.setCodigoRepetido(panXSucursal.getCatTipoPan().getCodigo());
						articuloVenta.add(panXSucursal);
					} else {
						System.out.println("existe");
						articuloVenta.get(indice).setCantVenta(cantArticulo + cantidadV);
					}
				}

				// si ya tenemos algo apra vender mostrar boton VENTA
				if (articuloVenta.size() > 0)
					mostrarVen = true;
				else
					mostrarVen = false;

				panXSucursal = new PanXSucursal();
				MsgUtil.msgInfo("Exito!", "Se agrego el articulo.");
				
			}else {
				MsgUtil.msgError("Error!", "La cantidad: "+cantArticulo+ " es mayor a la existencia: "+panXSucursal.getCantidad());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void cancelarArticulo() {
		panXSucursal = new PanXSucursal();
	}

	// elimina solo el registro elegido
	public void eliminar(PanXSucursal panXSucursal) {
		System.out.println("ar.getCantVenta()" + panXSucursal.getCantVenta());

		float t1 = (float) panXSucursal.getCatTipoPan().getCosto() * panXSucursal.getCantVenta();

		try {
			total = total - t1;
			articuloVenta.remove(panXSucursal);

			// si el articulo es igualacion eliminar el id que se eligio
			System.out.println("que seraaa" + panXSucursal.getCatTipoPan().getCodigo());
			System.out.println("que seraaa" + panXSucursal.getCatTipoPan().getDescripcion());
			// this.eliminItemXId(panXSucursal.getCatTipoPan().getCodigo());

			// si ya tenemos algo apra vender mostrar boton VENTA
			if (articuloVenta.size() > 0)
				mostrarVen = true;
			else
				mostrarVen = false;

			MsgUtil.msgInfo("Exito!", "Registro Eliminado.");
		} catch (Exception e) {
			e.printStackTrace();
			MsgUtil.msgError("Error!", "Registro no eliminado.");
		}
	}

	public void limpiarVenta() throws IOException {
		mostrarVen = false;
		if (userSession.getUsuario().getCatPerfil().getId() == 2)
			FacesContext.getCurrentInstance().getExternalContext().redirect("inicioAdminV2.xhtml");
		else
			FacesContext.getCurrentInstance().getExternalContext().redirect("inicioUsuario.xhtml");
	}

	public void cambioCalculo() {
		cambio = efectivo.subtract(new BigDecimal(total));
		cambio.setScale(2, BigDecimal.ROUND_UP);

	}

	public void guardarVenta() {
		System.out.println("quiere ticket   "+imprimeTicket);
		/*System.out.println("voy a vender tantos" + articuloVenta.size());
		
		for (PanXSucursal p : articuloVenta) {
		    System.out.println("arti"+p.getCantidad()+"     "+p.getCatTipoPan().getCosto());
		}*/

		try {
			if (articuloVenta == null || articuloVenta.size() > 0) {
				BigDecimal totalLocal = BigDecimal.ZERO;
				CatUbicacion ubi = new CatUbicacion();
				ubi.setId(userSession.getUsuario().getCatUbicacion().getId());

				CatUsuario usu = new CatUsuario();
				usu.setId(userSession.getUsuario().getId());

				Date fecha = new Date();
				RegistroVenta venta;
				RegistroVentaId rvid;

				Calendar cal = Calendar.getInstance();
				int anio = cal.get(Calendar.YEAR);
				// int mes = cal.get(Calendar.MONTH) + 1;

				// folio por a�o y sucursal suc-aaaa-00000000
				Integer con = iBriocheServicio.consecutivoXAnio(userSession.getUsuario().getCatUbicacion().getId());
				String folio = "";
				con++;

				// ubicacion a 3 cifras
				//int totalnombreC = Integer.toString(userSession.getUsuario().getCatUbicacion().getId()).trim().length();
				//int cadena = 3 - totalnombreC;

				// consecutivo a 8 cifras
				int totalCon = Integer.toString(con).trim().length();
				int cadenaC = 8 - totalCon;
				String conse = ceros(cadenaC) + con;
				folio = userSession.getUsuario().getCatUbicacion().getId() + "-" + anio + "-" + conse;

				folioVF = folio;

				// agrupar la lista por codigo y saber cuantos hay de cada uno
				//Map<String, Long> cuentaProductos2 = articuloVenta.stream().collect(Collectors.groupingBy(PanXSucursal::getCodigoRepetido, Collectors.counting()));

				// agrupar la lista por codigo y saber cuantos hay de cada uno pero suma la
				// cantidad a vender
				Map<String, Double> cuentaProductos = articuloVenta.stream().collect(Collectors.groupingBy(
						PanXSucursal::getCodigoRepetido, Collectors.summingDouble(PanXSucursal::getCantVenta)));

				// System.out.println("cuentaa"+cuentaProductos+"folio de venta"+folioVF);

				List<String> codigo = new ArrayList<>();

				// convertirlo a una lista primero recorrer el hashmap
				for (Map.Entry<String, Double> entry : cuentaProductos.entrySet()) {
					// System.out.println("clave=" + entry.getKey() + ", valor=" +
					// entry.getValue()+"listaaaaaaaaaaa"+articuloVenta.size());

					// recorrer la lista original
					for (PanXSucursal q : articuloVenta) {
						// revisar que la clave del hash sea igual a uno en la lista original
						if (q.getCatTipoPan().getCodigo() == entry.getKey()) {
							// revisar que solo se agrege a la nueva lista una unica vez cada clave
							if (!codigo.contains(entry.getKey())) {

								// formar el nuevo objeto
								rvid = new RegistroVentaId();
								rvid.setFolio(folio);
								rvid.setCodigoTipoPan(entry.getKey());

								// System.out.println("aaaaaaaaa "+entry.getValue().intValue());
								venta = new RegistroVenta();
								venta.setId(rvid);
								venta.setCantidad(entry.getValue().intValue());
								venta.setCatUbicacion(ubi);
								venta.setCatUsuario(usu);
								venta.setEstatus('V');
								venta.setFecha(fecha);
								venta.setConsecutivo(Integer.parseInt(conse));
								venta.setFormaPago(1);
								venta.setAcuenta(new BigDecimal(0));
								venta.setDescuento(new BigDecimal(0));

								venta.setEfectivo(efectivo);

								if (q.getCatTipoPan().getDescripcion().startsWith("AGRANEL")) {
									// System.out.println("paso13333"+q.getCosto()+" "+q.getPresentacion());
									venta.setTipo(2);
									venta.setPrecio(new BigDecimal(q.getCatTipoPan().getCosto()));
									venta.setListrosIgualacion(null);

									// this.bajaInventario("nada", 0, 2);
								} else {
									venta.setTipo(1);
									BigDecimal c = new BigDecimal(q.getCatTipoPan().getCosto());//q.getCatTipoPan().getCosto() * entry.getValue()
									venta.setPrecio(c);

									// restar al inventario lo que vendo
									this.bajaInventario(entry.getKey(), entry.getValue().intValue(), 1);
								}

								// calcular el total ya tengo el costo por cantidad
								totalLocal = totalLocal.add(venta.getPrecio());

								venta.setTotal(new BigDecimal(total));
								// que limina que solo entre una vez cada clave
								codigo.add(entry.getKey());
								getiBriocheServicio().guardarRegistroVenta(venta);

								venta.getId().getFolio();
							}

						}
					}
				}

				// al vender sale el ticket en automatico
				if(imprimeTicket)
					this.imprimirTicket();
				
				this.limpiarVenta();
				MsgUtil.msgInfo("Exito!", "Registro Guardado.");
			} else {
				MsgUtil.msgError("Error!", "Debe agregar un registro para la venta.");
			}

			//this.inicio();
			
			mostrarVen = false;

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String ceros(int numero) {

		String cero = "";
		for (int i = 0; i < numero; i++) {
			cero += "0";
		}
		return cero;
	}
	
	public String espacios(int numero) {
		String cero = "";
		for (int i = 0; i < numero; i++) {
			cero += " ";
		}
		return cero;
	}

	public void imprimirTicket() {
		String fechaActual = fecEnvioFormato.format(new Date());
		//System.out.println(folioVF+"   que es el tickettt");
		try {
			ventaTicket = getiBriocheServicio().obtenerRegistroVentaXFol(folioVF);
			PrinterService printerService = new PrinterService();
			String strContenido = "\t PANADERIA LA BRIOCHE  \n";
			strContenido += "\t SUCURSAL  "+ userSession.getUsuario().getCatUbicacion().getDescripcion()+"\n";
			strContenido += "Telefono(s): 728 28 7 53 80 \n\n";
		    strContenido += " Descrip\t  Cantidad\t   Precio\n";

			BigDecimal total = BigDecimal.ZERO;
			Integer totArti = 0;
			for (RegistroVenta catM : ventaTicket) {
				BigDecimal pFinal=BigDecimal.ZERO;
				pFinal=catM.getPrecio().multiply(new BigDecimal(catM.getCantidad()));
				if (catM.getCatTipoPan().getDescripcion().length() >= 10) {
					strContenido += catM.getCatTipoPan().getDescripcion().substring(0, 10) + "\t    "
							+ catM.getCantidad() + "\t\t " + pFinal + "\n";
				}else {
					//quede a 10 caracteres la descrip
					int totalEspacios = catM.getCatTipoPan().getDescripcion().trim().length();
					int cadenaC = 10 - totalEspacios;
					String desFinal = catM.getCatTipoPan().getDescripcion().concat(espacios(cadenaC));
					strContenido += desFinal + "\t    " + catM.getCantidad() + "\t\t "
							+ pFinal + "\n";
				}
					
//System.out.println("que cantidad tengo"+catM.getCantidad());
				total = total.add((catM.getPrecio().multiply( new BigDecimal(catM.getCantidad()))));
				totArti = totArti + catM.getCantidad();
			}
			
			strContenido +="\t\t\t\t\t\t TOTAL:    "+total;
			strContenido +="\t EFECTIVO: "+efectivo+"\n";
			strContenido +="\t CAMBIO:   "+efectivo.subtract(total)+"\n";
			strContenido +="\t Total Art.:   "+totArti;
			strContenido += "\n "
					
					+ "  \n"
					+ "AGRADECEMOS SU PREFERENCIA!  \n"
					+ "Fecha: "+fechaActual
					+ " \n\n\n\n\n\n\n\n\n\n";
			
			
//System.out.println(strContenido);"EPSON-T88V"
			printerService.printString(nombreImpresora, strContenido);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// List<ItemIgualacion> itemIgual;
	public void bajaInventario(String codigo, int cantidad, int tipo) {
		// tipo es para saber si es art normal(1) o de igualacion(2)
		if (tipo == 1) {
			PanXSucursal artBaja = getiBriocheServicio().panXSucXFechaXCodigo(codigo,
					userSession.getUsuario().getCatUbicacion().getId(), fecha);

			Integer nuevaCantidad = artBaja.getCantidad() - cantidad;
			artBaja.setCantidad(nuevaCantidad);
			getiBriocheServicio().actualizarPanXSucursal(artBaja);
		} else if (tipo == 2) {
			/*
			  itemIgual=getiBriocheServicio().obtenerItemIgActivos(); ArtIgualacion ar;
			  
			  //se buscan itemsIguala que esten activos para dar de baja for
			  (ItemIgualacion a : itemIgual) { ar= new ArtIgualacion(); //obtener
			  artIgualacion por codigoArticulo
			  ar=getiBriocheServicio().obtenerArtIgualacionXCodigo(a.getCodigoArticulo());
			  
			  //descontarle a ltExistente e BigDecimal exisReal=ar.getLtExiste();
			  exisReal=exisReal.subtract(new BigDecimal(a.getCantidad()));
			  ar.setLtExiste(exisReal);
			  
			  //actualizar artIgulacion getiBriocheServicio().actualizarArtIgualacion(ar);
			  
			  //ItemIgualacion estatus a false y actualziar a.setEstatus(false);
			  a.setFolio(folioVF); getiBriocheServicio().actualizarItemIgualacion(a);
			  
			  }
			 */
		}

	}
	
	
	
	
	
	/*
	 * para la etapa de todo por precio
	 */
	
	//pide la cantidad de piezas de pan
	public void agregaXPrecio(PrecioCantidadSucursal panPre) {
		cantArticulo = 1;
		precioCantidad = new PrecioCantidadSucursal();
		precioCantidad = panPre;
		
	}
	
	public void agregarArticuloPrecio() {
		int indice = -1;
		int contador = -1;
		float cantidadV = 0;
		
		float existencia=precioCantidad.getCantidadVendida() -cantArticulo;
		
		try {
			
			if(existencia>=0.0) {
				total = total + ((float) (precioCantidad.getId().getCosto()) * cantArticulo); //el total de costoX#Piezas

				System.out.println("uuuuuuuuuuuu" + articuloVenta.size());
				// es la primera vez no tiene nada la lista
				if (articuloVentaPrecio.size() == 0) {
					System.out.println("primera vez");
					// agregar la cantidad vendida en ete articulo
					precioCantidad.setCantidad((int)cantArticulo); //temporalmente guardamos en cantidad la cantidad seleccionada en el dlg
					//panXSucursal.setCodigoRepetido(panXSucursal.getCatTipoPan().getCodigo());
					articuloVentaPrecio.add(precioCantidad);
				} else {
					for (PrecioCantidadSucursal p : articuloVentaPrecio) {
						contador++;
						if (p.getId().getCosto()==precioCantidad.getId().getCosto()) {
							indice = contador;
							cantidadV = p.getCantidad();
						}

					}
					System.out.println("gggggggggggg" + indice);

					if (indice == -1) {
						System.out.println("no existe lo agregare");
						// agregar la cantidad vendida en ete articulo
						precioCantidad.setCantidad((int)cantArticulo);
						articuloVentaPrecio.add(precioCantidad);
					} else {
						System.out.println("existe");
						articuloVentaPrecio.get(indice).setCantidad((int) cantArticulo + (int)cantidadV);
					}
				}

				// si ya tenemos algo apra vender mostrar boton VENTA
				if (articuloVentaPrecio.size() > 0)
					mostrarVen = true;
				else
					mostrarVen = false;

				precioCantidad = new PrecioCantidadSucursal();
				MsgUtil.msgInfo("Exito!", "Se agrego el articulo.");
				
			}else {
				MsgUtil.msgError("Error!", "La cantidad: "+cantArticulo+ " es mayor a la existencia: "+precioCantidad.getCantidadVendida());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	// elimina solo el registro elegido
	public void eliminarPrecio(PrecioCantidadSucursal preCantidad) {
		System.out.println("ar.getCantVenta()" + preCantidad.getCantidad());

		float t1 = (float) preCantidad.getId().getCosto() * preCantidad.getCantidad();

		try {
			total = total - t1;
			articuloVentaPrecio.remove(preCantidad);

			

			// si ya tenemos algo apra vender mostrar boton VENTA
			if (articuloVentaPrecio.size() > 0)
				mostrarVen = true;
			else
				mostrarVen = false;

			MsgUtil.msgInfo("Exito!", "Registro Eliminado.");
		} catch (Exception e) {
			e.printStackTrace();
			MsgUtil.msgError("Error!", "Registro no eliminado.");
		}
	}
	
	
	public void cancelarArticuloPrecio() {
		precioCantidad = new PrecioCantidadSucursal();
	}
	
	
	public void guardarVentaPrecio() {
		try {
			if (articuloVentaPrecio == null || articuloVentaPrecio.size() > 0) {
				CatUbicacion ubi = new CatUbicacion();
				ubi.setId(userSession.getUsuario().getCatUbicacion().getId());

				CatUsuario usu = new CatUsuario();
				usu.setId(userSession.getUsuario().getId());

				PrecioCantidadVenta venta;
				PrecioCantidadVentaId rvid;

				Calendar cal = Calendar.getInstance();
				int anio = cal.get(Calendar.YEAR);
				// int mes = cal.get(Calendar.MONTH) + 1;

				// folio por año y sucursal suc-aaaa-00000000
				Integer con = iBriocheServicio.consecutivoXAnioPrecio(userSession.getUsuario().getCatUbicacion().getId());
				String folio = "";
				con++;
				
				// consecutivo a 8 cifras
				int totalCon = Integer.toString(con).trim().length();
				int cadenaC = 8 - totalCon;
				String conse = ceros(cadenaC) + con;
				folio = userSession.getUsuario().getCatUbicacion().getId() + "-" + anio + "-" + conse;

				folioVF = folio;
				// recorrer la lista original
				for (PrecioCantidadSucursal q : articuloVentaPrecio) {
					// formar el nuevo objeto
					rvid = new PrecioCantidadVentaId();
					rvid.setCosto(q.getId().getCosto());
					rvid.setFolio(folio);

					venta = new PrecioCantidadVenta();
					venta.setId(rvid);
					venta.setCantidad(q.getCantidad());
					venta.setFecha(new Date());
					venta.setIdUbicacion(q.getCatUbicacion().getId());
					venta.setEstatus(tipoVenta);
					venta.setIdUsuario(userSession.getUsuario().getId());
					venta.setConsecutivo(con);
				
					getiBriocheServicio().guardarPrecioCantidadVenta(venta);

					venta.getId().getFolio();
					
					//actualizar cantidadSucursal
					PrecioCantidadSucursal pcs=getiBriocheServicio().precioXSucXFechaXPrecio(q.getId().getCosto(), q.getCatUbicacion().getId(), q.getId().getFecha());
					
					Integer cantidad=0;
					if(pcs!=null) {
						System.out.println("que tengoo"+pcs.getId().getCosto());
						cantidad=pcs.getCantidadVendida()-q.getCantidad();
						pcs.setCantidadVendida(cantidad);
						getiBriocheServicio().actualizarPrecioCantidadSucursal(pcs);
					}

				}

				// al vender sale el ticket en automatico
				if(imprimeTicket)
					this.imprimirTicketPrecio();
				
				this.limpiarVenta();
				MsgUtil.msgInfo("Exito!", "Registro Guardado.");
			} else {
				MsgUtil.msgError("Error!", "Debe agregar un registro para la venta.");
			}

			//this.inicio();
			
			mostrarVen = false;

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void imprimirTicketPrecio() {
		String fechaActual = fecEnvioFormato.format(new Date());
		try {
			ventaTicketPrecio = getiBriocheServicio().registroVentaXFolPrecio(folioVF);
			PrinterService printerService = new PrinterService();
			String strContenido = "\t PANADERIA LA BRIOCHE  \n";
			strContenido += "\t SUCURSAL  "+ userSession.getUsuario().getCatUbicacion().getDescripcion()+"\n";
			strContenido += "Telefono(s): 728 28 7 53 80 \n\n";
		    strContenido += " Descrip\t  Cantidad\t   Precio\n";

			BigDecimal total = BigDecimal.ZERO;
			Integer totArti = 0;
			for (PrecioCantidadVenta catM : ventaTicketPrecio) {
				double pFinal;
				pFinal=catM.getId().getCosto()*catM.getCantidad();
				
				strContenido += catM.getCantidad() + "\t    " + catM.getId().getCosto() + "\t\t "
						+ pFinal + "\n";
					
				BigDecimal prB = new BigDecimal(catM.getId().getCosto());
				total = total.add((prB.multiply( new BigDecimal(catM.getCantidad()))));
				totArti = totArti + catM.getCantidad();
			}
			
			strContenido +="\t\t\t\t\t\t TOTAL:    "+total;
			strContenido +="\t EFECTIVO: "+efectivo+"\n";
			strContenido +="\t CAMBIO:   "+efectivo.subtract(total)+"\n";
			strContenido +="\t Total Art.:   "+totArti;
			strContenido += "\n "
					
					+ "  \n"
					+ "AGRADECEMOS SU PREFERENCIA!  \n"
					+ "Fecha: "+fechaActual
					+ " \n\n\n\n\n\n\n\n\n\n";
			
			
			//System.out.println(strContenido);
			printerService.printString("EPSON-T88V", strContenido);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	
	
	public void imprimirTicketPrueba() {
		String fechaActual = fecEnvioFormato.format(new Date());
		//System.out.println(folioVF+"   que es el tickettt");
		try {
			PrinterService printerService = new PrinterService();
			String strContenido = "\t PANADERIA LA BRIOCHE  \n";
			strContenido += "Telefono(s): 728 28 7 53 80 \n\n";
		    strContenido += " Descrip\t  Cantidad\t   Precio\n";

			
			strContenido +="\t\t\t\t\t\t TOTAL:    "+total;
			strContenido +="\t EFECTIVO: "+efectivo+"\n";
			strContenido += "\n "
					
					+ "  \n"
					+ "AGRADECEMOS SU PREFERENCIA!  \n"
					+ "Fecha: "+fechaActual
					+ " \n\n\n\n\n\n\n\n\n\n\r\n"
			+ " \r\n";
			System.out.println(nombreImpresora);
			
			printerService.printString(nombreImpresora, strContenido);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	//*****

	@ManagedProperty(value = "#{BriocheServicio}")
	IBriocheServicio iBriocheServicio;

	@ManagedProperty(value = "#{userSession}")
	UserSession userSession;

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

	public List<PanXSucursal> getLista() {
		return lista;
	}

	public void setLista(List<PanXSucursal> lista) {
		this.lista = lista;
	}

	public PanXSucursal getPanXSucursal() {
		return panXSucursal;
	}

	public void setPanXSucursal(PanXSucursal panXSucursal) {
		this.panXSucursal = panXSucursal;
	}

	public float getCantArticulo() {
		return cantArticulo;
	}

	public void setCantArticulo(float cantArticulo) {
		this.cantArticulo = cantArticulo;
	}

	public float getTotal() {
		return total;
	}

	public void setTotal(float total) {
		this.total = total;
	}

	public boolean isMostrarVen() {
		return mostrarVen;
	}

	public void setMostrarVen(boolean mostrarVen) {
		this.mostrarVen = mostrarVen;
	}

	public List<PanXSucursal> getArticuloVenta() {
		return articuloVenta;
	}

	public void setArticuloVenta(List<PanXSucursal> articuloVenta) {
		this.articuloVenta = articuloVenta;
	}

	public BigDecimal getEfectivo() {

		return efectivo;
	}

	public void setEfectivo(BigDecimal efectivo) {
		this.efectivo = efectivo;
	}

	public BigDecimal getCambio() {
		return cambio;
	}

	public void setCambio(BigDecimal cambio) {
		this.cambio = cambio;
	}

	public boolean isImprimeTicket() {
		return imprimeTicket;
	}

	public void setImprimeTicket(boolean imprimeTicket) {
		this.imprimeTicket = imprimeTicket;
	}

	public List<PrecioCantidadSucursal> getCostos() {
		return costos;
	}

	public void setCostos(List<PrecioCantidadSucursal> costos) {
		this.costos = costos;
	}

	public List<PrecioCantidadSucursal> getArticuloVentaPrecio() {
		return articuloVentaPrecio;
	}

	public void setArticuloVentaPrecio(List<PrecioCantidadSucursal> articuloVentaPrecio) {
		this.articuloVentaPrecio = articuloVentaPrecio;
	}

	
}
