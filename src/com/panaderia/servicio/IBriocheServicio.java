package com.panaderia.servicio;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.panaderia.modelo.bo.VentaBO;
import com.panaderia.modelo.negocio.ArticuloPedido;
import com.panaderia.modelo.negocio.CatCliente;
import com.panaderia.modelo.negocio.CatEstatusPedido;
import com.panaderia.modelo.negocio.CatFormula;
import com.panaderia.modelo.negocio.CatListaInicial;
import com.panaderia.modelo.negocio.CatListaInicialId;
import com.panaderia.modelo.negocio.CatMarca;
import com.panaderia.modelo.negocio.CatMateriaPrima;
import com.panaderia.modelo.negocio.CatPerfil;
import com.panaderia.modelo.negocio.CatProveedor;
import com.panaderia.modelo.negocio.CatTipoPan;
import com.panaderia.modelo.negocio.CatUbicacion;
import com.panaderia.modelo.negocio.CatUnidadMedida;
import com.panaderia.modelo.negocio.CatUsuario;
import com.panaderia.modelo.negocio.MateriaFormula;
import com.panaderia.modelo.negocio.MateriaFormulaId;
import com.panaderia.modelo.negocio.PanXSucursal;
import com.panaderia.modelo.negocio.PanXSucursalId;
import com.panaderia.modelo.negocio.Pedido;
import com.panaderia.modelo.negocio.PrecioCantidadSucursal;
import com.panaderia.modelo.negocio.PrecioCantidadVenta;
import com.panaderia.modelo.negocio.RegistroEfectivo;
import com.panaderia.modelo.negocio.RegistroVenta;

public interface IBriocheServicio {

	public CatUsuario login(CatUsuario usuario);

	// Usuario
	public List<CatUsuario> obtenerUsuarios();

	public void guardarUsuario(CatUsuario usuario);

	public CatUsuario obtenerUsuarioXId(int id);

	public void actualizarUsuario(CatUsuario usuario);

	public void eliminarUsuario(CatUsuario usuario);

	// Perfil
	public List<CatPerfil> obtenerPerfiles();

	public void guardarUsuario(CatPerfil catPerfil);

	public CatPerfil obtenerPerfilXId(int id);

	public void actualizarPerfil(CatPerfil catPerfil);

	public void eliminarPerfil(CatPerfil catPerfil);

	// Formulas
	public List<CatFormula> obtenerFormulas();

	public void guardarFormula(CatFormula formula);

	public CatFormula obtenerFormulaXId(int id);

	public void actualizarFormula(CatFormula formula);

	public void eliminarFormula(CatFormula formula);

	// Cliente
	public List<CatCliente> obtenerClientes();

	public void guardarCliente(CatCliente cliente);

	public CatCliente obtenerClienteXId(int id);

	public void actualizarCliente(CatCliente cliente);

	public void eliminarCliente(CatCliente cliente);

	// Marca
	public List<CatMarca> obtenerMarcas();

	public void guardarMarca(CatMarca marca);

	public CatMarca obtenerMarcaXId(int id);

	public void actualizarMarca(CatMarca marca);

	public void eliminarMarca(CatMarca marca);

	// proveedor
	public List<CatProveedor> obtenerProveedores();

	public void guardarProveedor(CatProveedor proveedor);

	public CatProveedor obtenerProveedorXId(int id);

	public void actualizarProveedor(CatProveedor proveedor);

	public void eliminarProveedor(CatProveedor proveedor);

	// ubicacion
	public List<CatUbicacion> obtenerUbicaciones();

	public void guardarUbicacion(CatUbicacion ubicacion);

	public CatUbicacion obtenerUbicacionXId(int id);

	public void actualizarUbicacion(CatUbicacion ubicacion);

	public void eliminarUbicacion(CatUbicacion ubicacion);

	// Unidad
	public List<CatUnidadMedida> obtenerUnidades();

	public void guardarUnidad(CatUnidadMedida unidad);

	public CatUnidadMedida obtenerUnidadXId(int id);

	public void actualizarUnidad(CatUnidadMedida unidad);

	public void eliminarUnidad(CatUnidadMedida unidad);

	// Materia
	public List<CatMateriaPrima> obtenerMaterias();

	public void guardarMateria(CatMateriaPrima materia);

	public CatMateriaPrima obtenerMateriaXId(int id);

	public void actualizarMateria(CatMateriaPrima materia);

	public void eliminarMateria(CatMateriaPrima materia);

	// Materia prima por formula
	public List<MateriaFormula> obtenerMPXFormula();

	public void guardarMPXFormula(MateriaFormula materia);

	public MateriaFormula obtenerMPXFormulaXId(MateriaFormulaId formulaId);

	public void actualizarMPXFormula(MateriaFormula materia);

	public void eliminarMPXFormula(MateriaFormula materia);

	// Tipo de pan
	public List<CatTipoPan> obtenerTiposPan();

	public void guardarTipoPan(CatTipoPan tipoPan);

	public CatTipoPan obtenerTipoPanXCodigo(String id);

	public void actualizarTipoPan(CatTipoPan tipoPan);

	public void eliminarTipoPan(CatTipoPan tipoPan);

	// pan por sucursal
	public List<PanXSucursal> obtenerPanXSucursales();

	public void guardarPanXSucursal(PanXSucursal pansuc);

	public PanXSucursal obtenerPanXSucursalXId(PanXSucursalId id);

	public void actualizarPanXSucursal(PanXSucursal pansuc);

	public void eliminarPanXSucursal(PanXSucursal pansuc);

	public List<PanXSucursal> obtenerPanXSucursalesXFecha(Date fec, int tipo, int sucursal);

	public PanXSucursal panXSucXFechaXCodigo(String codigo, int sucursal, Date fecha);

	// Registro VEnta
	public List<RegistroVenta> obtenerRegistroVenta();

	public void guardarRegistroVenta(RegistroVenta registroVenta);

	public RegistroVenta obtenerRegistroVentaXFolio(String id);

	public List<RegistroVenta> obtenerRegistroVentaXFol(String folio);

	public void actualizarRegistroVenta(RegistroVenta registroVenta);

	public void eliminarRegistroVenta(RegistroVenta registroVenta);

	public int consecutivoXAnio(int cveUbica);

	public List<RegistroVenta> obtenerRegVentaXDia(Date fecha);

	public List<RegistroVenta> obtenerRegVentaXDiaConsulta(Date fecha, int idSucursal);

	// consulta para reporte del usuario por fecha y su id
	public List<RegistroVenta> obtenerRegVentaXUsuario(Date fecha, int idUsuario);

	public BigDecimal obtenerIngresoXDiaXTipo(Date fecha, int tipo, int idSucursal, int idUsuario);

	public BigDecimal obtenerTotalVentaXDiaXTipo(Date fecha, int idSucursal);

	public List<RegistroVenta> obtenerTotalVentaXDiaXSucursal(Date fecha, int idSucursal);

	// Registro Efectivo
	public List<RegistroEfectivo> obtenerRegistrosEfectivo();

	public void guardarRegistroEfectivo(RegistroEfectivo registroEfectivo);

	public RegistroEfectivo obtenerRegistroEfectivoXId(int id);

	public void actualizarRegistroEfectivo(RegistroEfectivo registroEfectivo);

	public void eliminarRegistroEfectivo(RegistroEfectivo registroEfectivo);

	public List<RegistroEfectivo> obtenerRegistrosXEstatusXFecha(int estatus, Date fecha, int idSucursal);

	// Pedido
	public List<Pedido> obtenerPedidos();

	public Pedido obtenerPedidoXId(int idTipo);

	public void guardarPedido(Pedido pedido);

	public void actualizarPedido(Pedido pedido);

	public void eliminarPedido(Pedido pedido);

	public long consePedidoXAnio(int anio);

	public List<Pedido> obtenerEventoXMD(String mes, String dia);

	// estatus Pedido
	public List<CatEstatusPedido> obtenerCatEstatusPedidos();

	public CatEstatusPedido obtenerCatEstatusPedidoXId(int idTipo);

	public void guardarCatEstatusPedido(CatEstatusPedido estatusP);

	public void actualizarCatEstatusPedido(CatEstatusPedido estatusP);

	public void eliminarCatEstatusPedido(CatEstatusPedido estatusP);

	// articulo Pedido
	public List<ArticuloPedido> obtenerArticuloPedidos();

	public ArticuloPedido obtenerArticuloPedidoXId(int idTipo);

	public void guardarArticuloPedido(ArticuloPedido articuloPedido);

	public void actualizarArticuloPedido(ArticuloPedido articuloPedido);

	public void eliminaArticuloPedido(ArticuloPedido articuloPedido);

	public List<ArticuloPedido> obtenerArticuloPXFolio(String folio);

	// CatListaInicial
	public List<CatListaInicial> listaInicialXSucursal(int idsucursal);

	public void guardarCatListaInicial(CatListaInicial catListaInicial);

	public CatListaInicial catListaInicialXId(CatListaInicialId id);

	public void actualizarCatListaInicial(CatListaInicial catListaInicial);

	public void eliminarCatListaInicial(CatListaInicial catListaInicial);
	
	public List<Double> preciosXSucursalesXFecha(Date fec, int tipo, int sucursal);
	
	//PrecioCantidadSucursal
	public PrecioCantidadSucursal precioXSucXFechaXPrecio(double costo, int sucursal, Date fecha);
	
	public void guardarPrecioCantidadSucursal(PrecioCantidadSucursal pansuc);

	public void actualizarPrecioCantidadSucursal(PrecioCantidadSucursal pansuc);
	
	public List<PrecioCantidadSucursal> obtenerPrecioXSucursalesXFecha(Date fec, int tipo, int sucursal);
	
	//	precio_cantidad_venta
	public void guardarPrecioCantidadVenta(PrecioCantidadVenta registroVenta);
	
	public List<PrecioCantidadVenta> registroVentaXFolPrecio(String folio);
	
	public int consecutivoXAnioPrecio(int cveUbica);
}
