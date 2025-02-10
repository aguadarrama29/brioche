package com.panaderia.servicio;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.panaderia.modelo.dao.IBriocheDAO;
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

@Transactional(readOnly = true)
@Service
public class BriocheServicio implements IBriocheServicio {

	IBriocheDAO briocheDAO;

	@Override
	public CatUsuario login(CatUsuario usuario) {
		return getBriocheDAO().login(usuario);
	}

	// Usuario
	@Override
	public List<CatUsuario> obtenerUsuarios() {
		return getBriocheDAO().obtenerUsuarios();
	}

	@Transactional(readOnly = false)
	@Override
	public void guardarUsuario(CatUsuario usuario) {
		getBriocheDAO().guardarUsuario(usuario);
	}

	@Override
	public CatUsuario obtenerUsuarioXId(int id) {
		return getBriocheDAO().obtenerUsuarioXId(id);
	}

	@Transactional(readOnly = false)
	@Override
	public void actualizarUsuario(CatUsuario usuario) {
		getBriocheDAO().actualizarUsuario(usuario);
	}

	@Transactional(readOnly = false)
	@Override
	public void eliminarUsuario(CatUsuario usuario) {
		getBriocheDAO().eliminarUsuario(usuario);
	}

	// Perfil
	@Override
	public List<CatPerfil> obtenerPerfiles() {
		return getBriocheDAO().obtenerPerfiles();
	}

	@Transactional(readOnly = false)
	@Override
	public void guardarUsuario(CatPerfil catPerfil) {
		getBriocheDAO().guardarUsuario(catPerfil);
	}

	@Override
	public CatPerfil obtenerPerfilXId(int id) {
		return getBriocheDAO().obtenerPerfilXId(id);
	}

	@Transactional(readOnly = false)
	@Override
	public void actualizarPerfil(CatPerfil catPerfil) {
		getBriocheDAO().actualizarPerfil(catPerfil);
	}

	@Transactional(readOnly = false)
	@Override
	public void eliminarPerfil(CatPerfil catPerfil) {
		getBriocheDAO().eliminarPerfil(catPerfil);
	}

	// Formula
	@Override
	public List<CatFormula> obtenerFormulas() {
		return getBriocheDAO().obtenerFormulas();
	}

	@Transactional(readOnly = false)
	@Override
	public void guardarFormula(CatFormula formula) {
		getBriocheDAO().guardarFormula(formula);
	}

	@Override
	public CatFormula obtenerFormulaXId(int id) {
		return getBriocheDAO().obtenerFormulaXId(id);
	}

	@Transactional(readOnly = false)
	@Override
	public void actualizarFormula(CatFormula formula) {
		getBriocheDAO().actualizarFormula(formula);
	}

	@Transactional(readOnly = false)
	@Override
	public void eliminarFormula(CatFormula formula) {
		getBriocheDAO().eliminarFormula(formula);
	}

	// Cliente
	@Override
	public List<CatCliente> obtenerClientes() {
		return getBriocheDAO().obtenerClientes();
	}

	@Transactional(readOnly = false)
	@Override
	public void guardarCliente(CatCliente cliente) {
		getBriocheDAO().guardarCliente(cliente);
	}

	@Override
	public CatCliente obtenerClienteXId(int id) {
		return getBriocheDAO().obtenerClienteXId(id);
	}

	@Transactional(readOnly = false)
	@Override
	public void actualizarCliente(CatCliente cliente) {
		getBriocheDAO().actualizarCliente(cliente);
	}

	@Transactional(readOnly = false)
	@Override
	public void eliminarCliente(CatCliente cliente) {
		getBriocheDAO().eliminarCliente(cliente);
	}

	// Marca
	@Override
	public List<CatMarca> obtenerMarcas() {
		return getBriocheDAO().obtenerMarcas();
	}

	@Transactional(readOnly = false)
	@Override
	public void guardarMarca(CatMarca marca) {
		getBriocheDAO().guardarMarca(marca);
	}

	@Override
	public CatMarca obtenerMarcaXId(int id) {
		return getBriocheDAO().obtenerMarcaXId(id);
	}

	@Transactional(readOnly = false)
	@Override
	public void actualizarMarca(CatMarca marca) {
		getBriocheDAO().actualizarMarca(marca);
	}

	@Transactional(readOnly = false)
	@Override
	public void eliminarMarca(CatMarca marca) {
		getBriocheDAO().eliminarMarca(marca);
	}

	// Proveedor
	@Override
	public List<CatProveedor> obtenerProveedores() {
		return getBriocheDAO().obtenerProveedores();
	}

	@Transactional(readOnly = false)
	@Override
	public void guardarProveedor(CatProveedor proveedor) {
		getBriocheDAO().guardarProveedor(proveedor);
	}

	@Override
	public CatProveedor obtenerProveedorXId(int id) {
		return getBriocheDAO().obtenerProveedorXId(id);
	}

	@Transactional(readOnly = false)
	@Override
	public void actualizarProveedor(CatProveedor proveedor) {
		getBriocheDAO().actualizarProveedor(proveedor);
	}

	@Transactional(readOnly = false)
	@Override
	public void eliminarProveedor(CatProveedor proveedor) {
		getBriocheDAO().eliminarProveedor(proveedor);
	}

	// Marca
	@Override
	public List<CatUbicacion> obtenerUbicaciones() {
		return getBriocheDAO().obtenerUbicaciones();
	}

	@Transactional(readOnly = false)
	@Override
	public void guardarUbicacion(CatUbicacion ubicacion) {
		getBriocheDAO().guardarUbicacion(ubicacion);
	}

	@Override
	public CatUbicacion obtenerUbicacionXId(int id) {
		return getBriocheDAO().obtenerUbicacionXId(id);
	}

	@Transactional(readOnly = false)
	@Override
	public void actualizarUbicacion(CatUbicacion ubicacion) {
		getBriocheDAO().actualizarUbicacion(ubicacion);
	}

	@Transactional(readOnly = false)
	@Override
	public void eliminarUbicacion(CatUbicacion ubicacion) {
		getBriocheDAO().eliminarUbicacion(ubicacion);
	}

	// Unidad
	@Override
	public List<CatUnidadMedida> obtenerUnidades() {
		return getBriocheDAO().obtenerUnidades();
	}

	@Transactional(readOnly = false)
	@Override
	public void guardarUnidad(CatUnidadMedida unidad) {
		getBriocheDAO().guardarUnidad(unidad);
	}

	@Override
	public CatUnidadMedida obtenerUnidadXId(int id) {
		return getBriocheDAO().obtenerUnidadXId(id);
	}

	@Transactional(readOnly = false)
	@Override
	public void actualizarUnidad(CatUnidadMedida unidad) {
		getBriocheDAO().actualizarUnidad(unidad);
	}

	@Transactional(readOnly = false)
	@Override
	public void eliminarUnidad(CatUnidadMedida unidad) {
		getBriocheDAO().eliminarUnidad(unidad);
	}

	// Materia
	@Override
	public List<CatMateriaPrima> obtenerMaterias() {
		return getBriocheDAO().obtenerMaterias();
	}

	@Transactional(readOnly = false)
	@Override
	public void guardarMateria(CatMateriaPrima materia) {
		getBriocheDAO().guardarMateria(materia);
	}

	@Override
	public CatMateriaPrima obtenerMateriaXId(int id) {
		return getBriocheDAO().obtenerMateriaXId(id);
	}

	@Transactional(readOnly = false)
	@Override
	public void actualizarMateria(CatMateriaPrima materia) {
		getBriocheDAO().actualizarMateria(materia);
	}

	@Transactional(readOnly = false)
	@Override
	public void eliminarMateria(CatMateriaPrima materia) {
		getBriocheDAO().eliminarMateria(materia);
	}

	// Materia
	@Override
	public List<MateriaFormula> obtenerMPXFormula() {
		return getBriocheDAO().obtenerMPXFormula();
	}

	@Transactional(readOnly = false)
	@Override
	public void guardarMPXFormula(MateriaFormula materia) {
		getBriocheDAO().guardarMPXFormula(materia);
	}

	@Override
	public MateriaFormula obtenerMPXFormulaXId(MateriaFormulaId formulaId) {
		return getBriocheDAO().obtenerMPXFormulaXId(formulaId);
	}

	@Transactional(readOnly = false)
	@Override
	public void actualizarMPXFormula(MateriaFormula materia) {
		getBriocheDAO().actualizarMPXFormula(materia);
	}

	@Transactional(readOnly = false)
	@Override
	public void eliminarMPXFormula(MateriaFormula materia) {
		getBriocheDAO().eliminarMPXFormula(materia);
	}

	// Tipo de pan
	@Override
	public List<CatTipoPan> obtenerTiposPan() {
		return getBriocheDAO().obtenerTiposPan();
	}

	@Transactional(readOnly = false)
	@Override
	public void guardarTipoPan(CatTipoPan tipoPan) {
		getBriocheDAO().guardarTipoPan(tipoPan);
	}

	@Override
	public CatTipoPan obtenerTipoPanXCodigo(String id) {
		return getBriocheDAO().obtenerTipoPanXCodigo(id);
	}

	@Transactional(readOnly = false)
	@Override
	public void actualizarTipoPan(CatTipoPan tipoPan) {
		getBriocheDAO().actualizarTipoPan(tipoPan);
	}

	@Transactional(readOnly = false)
	@Override
	public void eliminarTipoPan(CatTipoPan tipoPan) {
		getBriocheDAO().eliminarTipoPan(tipoPan);
	}

	@Override
	public List<PanXSucursal> obtenerPanXSucursales() {
		return getBriocheDAO().obtenerPanXSucursales();
	}

	@Transactional(readOnly = false)
	@Override
	public void guardarPanXSucursal(PanXSucursal pansuc) {
		getBriocheDAO().guardarPanXSucursal(pansuc);
	}

	@Override
	public PanXSucursal obtenerPanXSucursalXId(PanXSucursalId id) {
		return getBriocheDAO().obtenerPanXSucursalXId(id);
	}

	@Transactional(readOnly = false)
	@Override
	public void actualizarPanXSucursal(PanXSucursal pansuc) {
		getBriocheDAO().actualizarPanXSucursal(pansuc);
	}
	

	@Transactional(readOnly = false)
	@Override
	public void eliminarPanXSucursal(PanXSucursal pansuc) {
		getBriocheDAO().eliminarPanXSucursal(pansuc);
	}

	@Override
	public List<PanXSucursal> obtenerPanXSucursalesXFecha(Date fec, int tipo, int sucursal) {
		return getBriocheDAO().obtenerPanXSucursalesXFecha(fec, tipo, sucursal);
	}

	public PanXSucursal panXSucXFechaXCodigo(String codigo, int sucursal, Date fecha) {
		return getBriocheDAO().panXSucXFechaXCodigo(codigo, sucursal, fecha);
	}
	

	// registro venta
	@Override
	public List<RegistroVenta> obtenerRegistroVenta() {
		return getBriocheDAO().obtenerRegistroVenta();
	}

	@Override
	@Transactional(readOnly = false) // sin esta linea en ocasiones no guarda
	public void guardarRegistroVenta(RegistroVenta registroVenta) {
		getBriocheDAO().guardarRegistroVenta(registroVenta);
	}
	
	@Override
	@Transactional(readOnly = false) // sin esta linea en ocasiones no guarda
	public void guardarPrecioCantidadVenta(PrecioCantidadVenta registroVenta) {
		getBriocheDAO().guardarPrecioCantidadVenta(registroVenta);
	}

	@Override
	public RegistroVenta obtenerRegistroVentaXFolio(String id) {
		return getBriocheDAO().obtenerRegistroVentaXFolio(id);
	}

	@Override
	public List<RegistroVenta> obtenerRegistroVentaXFol(String folio) {
		return getBriocheDAO().obtenerRegistroVentaXFol(folio);
	}

	@Transactional(readOnly = false)
	@Override
	public void actualizarRegistroVenta(RegistroVenta registroVenta) {
		getBriocheDAO().actualizarRegistroVenta(registroVenta);
	}

	public void eliminarRegistroVenta(RegistroVenta registroVenta) {
		getBriocheDAO().eliminarRegistroVenta(registroVenta);
	}

	@Override
	public int consecutivoXAnio(int cveUbica) {
		return getBriocheDAO().consecutivoXAnio(cveUbica);
	}

	public List<RegistroVenta> obtenerRegVentaXDia(Date fecha) {
		return getBriocheDAO().obtenerRegVentaXDia(fecha);
	}

	public List<RegistroVenta> obtenerRegVentaXDiaConsulta(Date fecha, int idSucursal) {
		return getBriocheDAO().obtenerRegVentaXDiaConsulta(fecha, idSucursal);
	}
	
	public BigDecimal obtenerIngresoXDiaXTipo(Date fecha,int tipo,int idSucursal,int idUsuario) {
		return getBriocheDAO().obtenerIngresoXDiaXTipo(fecha,tipo, idSucursal,idUsuario);
	}
	
	public BigDecimal obtenerTotalVentaXDiaXTipo(Date fecha,int idSucursal) {
		return getBriocheDAO().obtenerTotalVentaXDiaXTipo(fecha,idSucursal);
	}
	
	public List<RegistroVenta> obtenerTotalVentaXDiaXSucursal(Date fecha,int idSucursal){
		return getBriocheDAO().obtenerTotalVentaXDiaXSucursal(fecha,idSucursal);
	}

	public List<RegistroVenta> obtenerRegVentaXUsuario(Date fecha, int idUsuario) {
		return getBriocheDAO().obtenerRegVentaXUsuario(fecha, idUsuario);
	}

	public IBriocheDAO getBriocheDAO() {
		return briocheDAO;
	}

	public void setBriocheDAO(IBriocheDAO briocheDAO) {
		this.briocheDAO = briocheDAO;
	}

	// Registro Efectivo
	@Override
	public List<RegistroEfectivo> obtenerRegistrosEfectivo() {
		return getBriocheDAO().obtenerRegistrosEfectivo();
	}

	@Transactional(readOnly = false)
	@Override
	public void guardarRegistroEfectivo(RegistroEfectivo registroEfectivo){
		getBriocheDAO().guardarRegistroEfectivo(registroEfectivo);
	}

	@Override
	public RegistroEfectivo obtenerRegistroEfectivoXId(int id) {
		return getBriocheDAO().obtenerRegistroEfectivoXId(id);
	}

	@Transactional(readOnly = false)
	@Override
	public void actualizarRegistroEfectivo(RegistroEfectivo registroEfectivo) {
		getBriocheDAO().actualizarRegistroEfectivo(registroEfectivo);
	}

	@Transactional(readOnly = false)
	@Override
	public void eliminarRegistroEfectivo(RegistroEfectivo registroEfectivo) {
		getBriocheDAO().eliminarRegistroEfectivo(registroEfectivo);
	}
	
	public List<RegistroEfectivo> obtenerRegistrosXEstatusXFecha(int estatus,Date fecha,int idSucursal) {
		return getBriocheDAO().obtenerRegistrosXEstatusXFecha(estatus,fecha,idSucursal);
	}
	
	
	//PEDIDO
		@Override
		public List<Pedido> obtenerPedidos(){
			return getBriocheDAO().obtenerPedidos();
		}
		
		@Override
		public Pedido obtenerPedidoXId(int idTipo){
			return getBriocheDAO().obtenerPedidoXId(idTipo);
		}
		
		@Override
		@Transactional(readOnly = false)
		public void guardarPedido(Pedido pedido) {
			getBriocheDAO().guardarPedido(pedido);
		}
		
		@Override
		@Transactional(readOnly = false)
		public void actualizarPedido(Pedido pedido) {
			getBriocheDAO().actualizarPedido(pedido);
		}
		
		@Override
		@Transactional(readOnly = false)
		public void eliminarPedido(Pedido pedido) {
			getBriocheDAO().eliminarPedido(pedido);
		}
		
		@Override
		public long consePedidoXAnio(int anio) {
			return getBriocheDAO().consePedidoXAnio(anio);
		}
		
		@Override
		public List<Pedido> obtenerEventoXMD(String mes, String dia){
			return getBriocheDAO().obtenerEventoXMD(mes,dia);
		}
		
		//catEsattusPedido
		@Override
		public List<CatEstatusPedido> obtenerCatEstatusPedidos(){
			return getBriocheDAO().obtenerCatEstatusPedidos();
		}
		
		@Override
		public CatEstatusPedido obtenerCatEstatusPedidoXId(int idTipo) {
			return getBriocheDAO().obtenerCatEstatusPedidoXId(idTipo);
		}
		
		@Override
		@Transactional(readOnly = false)
		public void guardarCatEstatusPedido(CatEstatusPedido estatusP) {
			getBriocheDAO().guardarCatEstatusPedido(estatusP);
		}
		
		@Override
		@Transactional(readOnly = false)
		public void actualizarCatEstatusPedido(CatEstatusPedido estatusP) {
			getBriocheDAO().actualizarCatEstatusPedido(estatusP);
		}
		
		@Override
		@Transactional(readOnly = false)
		public void eliminarCatEstatusPedido(CatEstatusPedido estatusP) {
			getBriocheDAO().eliminarCatEstatusPedido(estatusP);
		}
		
		//articulo Pedido
		@Override
		public List<ArticuloPedido> obtenerArticuloPedidos(){
			return getBriocheDAO().obtenerArticuloPedidos();
		}
		
		@Override
		public ArticuloPedido obtenerArticuloPedidoXId(int idTipo) {
			return getBriocheDAO().obtenerArticuloPedidoXId(idTipo);
		}
		
		@Override
		@Transactional(readOnly = false)
		public void guardarArticuloPedido(ArticuloPedido articuloPedido) {
			getBriocheDAO().guardarArticuloPedido(articuloPedido);
		}
		
		@Override
		@Transactional(readOnly = false)
		public void actualizarArticuloPedido(ArticuloPedido articuloPedido) {
			getBriocheDAO().actualizarArticuloPedido(articuloPedido);
		}
		
		@Override
		@Transactional(readOnly = false)
		public void eliminaArticuloPedido(ArticuloPedido articuloPedido) {
			getBriocheDAO().eliminaArticuloPedido(articuloPedido);
		}
		
		@Override
		public List<ArticuloPedido> obtenerArticuloPXFolio(String folio){
			return getBriocheDAO().obtenerArticuloPXFolio(folio);
		}
		
		
		// CatListaInicial
		@Override
		public List<CatListaInicial> listaInicialXSucursal(int idsucursal){
			return getBriocheDAO().listaInicialXSucursal(idsucursal);
		}
		
		@Override
		@Transactional(readOnly = false)
		public void guardarCatListaInicial(CatListaInicial catListaInicial) {
			getBriocheDAO().guardarCatListaInicial(catListaInicial);
		}

		@Override
		public CatListaInicial catListaInicialXId(CatListaInicialId id) {
			return getBriocheDAO().catListaInicialXId(id);
		}
		
		@Override
		@Transactional(readOnly = false)
		public void actualizarCatListaInicial(CatListaInicial catListaInicial) {
			 getBriocheDAO().actualizarCatListaInicial(catListaInicial);
		}
		
		@Override
		@Transactional(readOnly = false)
		public void eliminarCatListaInicial(CatListaInicial catListaInicial) {
			 getBriocheDAO().eliminarCatListaInicial(catListaInicial);
		}
		
		public List<Double> preciosXSucursalesXFecha(Date fec, int tipo, int sucursal){
			return getBriocheDAO().preciosXSucursalesXFecha(fec, tipo, sucursal);
		}
		
		
		//PrecioCantidadSucursal
		public PrecioCantidadSucursal precioXSucXFechaXPrecio(double costo, int sucursal, Date fecha) {
			return getBriocheDAO().precioXSucXFechaXPrecio(costo, sucursal, fecha);
		}
		
		@Transactional(readOnly = false)
		@Override
		public void guardarPrecioCantidadSucursal(PrecioCantidadSucursal pansuc) {
			getBriocheDAO().guardarPrecioCantidadSucursal(pansuc);
		}

		@Transactional(readOnly = false)
		@Override
		public void actualizarPrecioCantidadSucursal(PrecioCantidadSucursal pansuc) {
			getBriocheDAO().actualizarPrecioCantidadSucursal(pansuc);
		}
		
		public List<PrecioCantidadSucursal> obtenerPrecioXSucursalesXFecha(Date fec, int tipo, int sucursal){
			return getBriocheDAO().obtenerPrecioXSucursalesXFecha(fec, tipo, sucursal);
		}
		
		@Override
		public List<PrecioCantidadVenta> registroVentaXFolPrecio(String folio) {
			return getBriocheDAO().registroVentaXFolPrecio(folio);
		}
		
		@Override
		public int consecutivoXAnioPrecio(int cveUbica) {
			return getBriocheDAO().consecutivoXAnioPrecio(cveUbica);
		}

}
