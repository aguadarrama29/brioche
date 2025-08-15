package com.panaderia.modelo.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.type.StandardBasicTypes;

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

public class BriocheDAO implements Serializable, IBriocheDAO {

	private static final long serialVersionUID = -8183166007782782962L;
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(BriocheDAO.class);
	private SessionFactory sessionFactory;

	@Override
	public CatUsuario login(CatUsuario usuario) {
		String stringQuery;
		Query query;
		stringQuery = "FROM CatUsuario u WHERE u.descripcion = :usuario AND u.contrasenia = :contrasenia";
		query = getSessionFactory().getCurrentSession().createQuery(stringQuery);
		query.setString("usuario", usuario.getDescripcion());
		query.setString("contrasenia", usuario.getContrasenia());
		return (CatUsuario) query.uniqueResult();
	}

	// Perfil
	@SuppressWarnings("unchecked")
	@Override
	public List<CatPerfil> obtenerPerfiles() {
		String stringQuery;
		Query query;
		stringQuery = "FROM CatPerfil ORDER BY descripcion";
		query = getSessionFactory().getCurrentSession().createQuery(stringQuery);
		return query.list();
	}

	@Override
	public void guardarUsuario(CatPerfil catPerfil) {
		getSessionFactory().getCurrentSession().save(catPerfil);
	}

	@Override
	public CatPerfil obtenerPerfilXId(int id) {
		String stringQuery;
		Query query;
		stringQuery = "FROM CatPerfil us WHERE us.id = :id";
		query = getSessionFactory().getCurrentSession().createQuery(stringQuery);
		query.setInteger("id", id);
		return (CatPerfil) query.uniqueResult();
	}

	@Override
	public void actualizarPerfil(CatPerfil catPerfil) {
		getSessionFactory().getCurrentSession().update(catPerfil);
	}

	@Override
	public void eliminarPerfil(CatPerfil catPerfil) {
		getSessionFactory().getCurrentSession().delete(catPerfil);
	}

	// Usuario
	@SuppressWarnings("unchecked")
	@Override
	public List<CatUsuario> obtenerUsuarios() {
		String stringQuery;
		Query query;
		stringQuery = "FROM CatUsuario ORDER BY descripcion";
		query = getSessionFactory().getCurrentSession().createQuery(stringQuery);
		return query.list();
	}

	@Override
	public void guardarUsuario(CatUsuario usuario) {
		getSessionFactory().getCurrentSession().save(usuario);
	}

	@Override
	public CatUsuario obtenerUsuarioXId(int id) {
		String stringQuery;
		Query query;
		stringQuery = "FROM CatUsuario us WHERE us.id = :id";
		query = getSessionFactory().getCurrentSession().createQuery(stringQuery);
		query.setInteger("id", id);
		return (CatUsuario) query.uniqueResult();
	}

	@Override
	public void actualizarUsuario(CatUsuario usuario) {
		getSessionFactory().getCurrentSession().update(usuario);
	}

	@Override
	public void eliminarUsuario(CatUsuario usuario) {
		getSessionFactory().getCurrentSession().delete(usuario);
	}

	// Perfil
	@SuppressWarnings("unchecked")
	@Override
	public List<CatFormula> obtenerFormulas() {
		String stringQuery;
		Query query;
		stringQuery = "FROM CatFormula ORDER BY descripcion";
		query = getSessionFactory().getCurrentSession().createQuery(stringQuery);
		return query.list();
	}

	@Override
	public void guardarFormula(CatFormula formula) {
		getSessionFactory().getCurrentSession().save(formula);
	}

	@Override
	public CatFormula obtenerFormulaXId(int id) {
		String stringQuery;
		Query query;
		stringQuery = "FROM CatFormula us WHERE us.id = :id";
		query = getSessionFactory().getCurrentSession().createQuery(stringQuery);
		query.setInteger("id", id);
		return (CatFormula) query.uniqueResult();
	}

	@Override
	public void actualizarFormula(CatFormula formula) {
		getSessionFactory().getCurrentSession().update(formula);
	}

	@Override
	public void eliminarFormula(CatFormula formula) {
		getSessionFactory().getCurrentSession().delete(formula);
	}

	// Cliente
	@SuppressWarnings("unchecked")
	@Override
	public List<CatCliente> obtenerClientes() {
		String stringQuery;
		Query query;
		stringQuery = "FROM CatCliente ORDER BY descripcion";
		query = getSessionFactory().getCurrentSession().createQuery(stringQuery);
		return query.list();
	}

	@Override
	public void guardarCliente(CatCliente cliente) {
		getSessionFactory().getCurrentSession().save(cliente);
	}

	@Override
	public CatCliente obtenerClienteXId(int id) {
		String stringQuery;
		Query query;
		stringQuery = "FROM CatFormula us WHERE us.id = :id";
		query = getSessionFactory().getCurrentSession().createQuery(stringQuery);
		query.setInteger("id", id);
		return (CatCliente) query.uniqueResult();
	}

	@Override
	public void actualizarCliente(CatCliente cliente) {
		getSessionFactory().getCurrentSession().update(cliente);
	}

	@Override
	public void eliminarCliente(CatCliente cliente) {
		getSessionFactory().getCurrentSession().delete(cliente);
	}

	// Marca
	@SuppressWarnings("unchecked")
	@Override
	public List<CatMarca> obtenerMarcas() {
		String stringQuery;
		Query query;
		stringQuery = "FROM CatMarca ORDER BY descripcion";
		query = getSessionFactory().getCurrentSession().createQuery(stringQuery);
		return query.list();
	}

	@Override
	public void guardarMarca(CatMarca marca) {
		getSessionFactory().getCurrentSession().save(marca);
	}

	@Override
	public CatMarca obtenerMarcaXId(int id) {
		String stringQuery;
		Query query;
		stringQuery = "FROM CatMarca us WHERE us.id = :id";
		query = getSessionFactory().getCurrentSession().createQuery(stringQuery);
		query.setInteger("id", id);
		return (CatMarca) query.uniqueResult();
	}

	@Override
	public void actualizarMarca(CatMarca marca) {
		getSessionFactory().getCurrentSession().update(marca);
	}

	@Override
	public void eliminarMarca(CatMarca marca) {
		getSessionFactory().getCurrentSession().delete(marca);
	}

	// Proveedor
	@SuppressWarnings("unchecked")
	@Override
	public List<CatProveedor> obtenerProveedores() {
		String stringQuery;
		Query query;
		stringQuery = "FROM CatProveedor ORDER BY descripcion";
		query = getSessionFactory().getCurrentSession().createQuery(stringQuery);
		return query.list();
	}

	@Override
	public void guardarProveedor(CatProveedor proveedor) {
		getSessionFactory().getCurrentSession().save(proveedor);
	}

	@Override
	public CatProveedor obtenerProveedorXId(int id) {
		String stringQuery;
		Query query;
		stringQuery = "FROM CatProveedor us WHERE us.id = :id";
		query = getSessionFactory().getCurrentSession().createQuery(stringQuery);
		query.setInteger("id", id);
		return (CatProveedor) query.uniqueResult();
	}

	@Override
	public void actualizarProveedor(CatProveedor proveedor) {
		getSessionFactory().getCurrentSession().update(proveedor);
	}

	@Override
	public void eliminarProveedor(CatProveedor proveedor) {
		getSessionFactory().getCurrentSession().delete(proveedor);
	}

	// Ubicacion
	@SuppressWarnings("unchecked")
	@Override
	public List<CatUbicacion> obtenerUbicaciones() {
		String stringQuery;
		Query query;
		stringQuery = "FROM CatUbicacion ORDER BY descripcion";
		query = getSessionFactory().getCurrentSession().createQuery(stringQuery);
		return query.list();
	}

	@Override
	public void guardarUbicacion(CatUbicacion ubicacion) {
		getSessionFactory().getCurrentSession().save(ubicacion);
	}

	@Override
	public CatUbicacion obtenerUbicacionXId(int id) {
		String stringQuery;
		Query query;
		stringQuery = "FROM CatUbicacion us WHERE us.id = :id";
		query = getSessionFactory().getCurrentSession().createQuery(stringQuery);
		query.setInteger("id", id);
		return (CatUbicacion) query.uniqueResult();
	}

	@Override
	public void actualizarUbicacion(CatUbicacion ubicacion) {
		getSessionFactory().getCurrentSession().update(ubicacion);
	}

	@Override
	public void eliminarUbicacion(CatUbicacion ubicacion) {
		getSessionFactory().getCurrentSession().delete(ubicacion);
	}

	// Unidad
	@SuppressWarnings("unchecked")
	@Override
	public List<CatUnidadMedida> obtenerUnidades() {
		String stringQuery;
		Query query;
		stringQuery = "FROM CatUnidadMedida ORDER BY descripcion ASC";
		query = getSessionFactory().getCurrentSession().createQuery(stringQuery);
		return query.list();
	}

	@Override
	public void guardarUnidad(CatUnidadMedida unidad) {
		getSessionFactory().getCurrentSession().save(unidad);
	}

	@Override
	public CatUnidadMedida obtenerUnidadXId(int id) {
		String stringQuery;
		Query query;
		stringQuery = "FROM CatUnidadMedida us WHERE us.id = :id";
		query = getSessionFactory().getCurrentSession().createQuery(stringQuery);
		query.setInteger("id", id);
		return (CatUnidadMedida) query.uniqueResult();
	}

	@Override
	public void actualizarUnidad(CatUnidadMedida unidad) {
		getSessionFactory().getCurrentSession().update(unidad);
	}

	@Override
	public void eliminarUnidad(CatUnidadMedida unidad) {
		getSessionFactory().getCurrentSession().delete(unidad);
	}

	// Materiales
	@SuppressWarnings("unchecked")
	@Override
	public List<CatMateriaPrima> obtenerMaterias() {
		String stringQuery;
		Query query;
		stringQuery = "FROM CatMateriaPrima ORDER BY descripcion";
		query = getSessionFactory().getCurrentSession().createQuery(stringQuery);
		return query.list();
	}

	@Override
	public void guardarMateria(CatMateriaPrima materia) {
		getSessionFactory().getCurrentSession().save(materia);
	}

	@Override
	public CatMateriaPrima obtenerMateriaXId(int id) {
		String stringQuery;
		Query query;
		stringQuery = "FROM CatMateriaPrima us WHERE us.id = :id";
		query = getSessionFactory().getCurrentSession().createQuery(stringQuery);
		query.setInteger("id", id);
		return (CatMateriaPrima) query.uniqueResult();
	}

	@Override
	public void actualizarMateria(CatMateriaPrima materia) {
		getSessionFactory().getCurrentSession().update(materia);
	}

	@Override
	public void eliminarMateria(CatMateriaPrima materia) {
		getSessionFactory().getCurrentSession().delete(materia);
	}

	// Materiales pria por formula
	@SuppressWarnings("unchecked")
	@Override
	public List<MateriaFormula> obtenerMPXFormula() {
		String stringQuery;
		Query query;
		stringQuery = "FROM MateriaFormula mf ORDER BY mf.id.idMateria";
		query = getSessionFactory().getCurrentSession().createQuery(stringQuery);
		return query.list();
	}

	@Override
	public void guardarMPXFormula(MateriaFormula materia) {
		getSessionFactory().getCurrentSession().save(materia);
	}

	@Override
	public MateriaFormula obtenerMPXFormulaXId(MateriaFormulaId formulaId) {
		String stringQuery;
		Query query;
		stringQuery = "FROM MateriaFormula us WHERE us.id.idFormula = :idformula " + "AND us.id.idMateria = :idmateria";
		query = getSessionFactory().getCurrentSession().createQuery(stringQuery);
		query.setInteger("idformula", formulaId.getIdFormula());
		query.setInteger("idmateria", formulaId.getIdMateria());
		return (MateriaFormula) query.uniqueResult();
	}

	@Override
	public void actualizarMPXFormula(MateriaFormula materia) {
		getSessionFactory().getCurrentSession().update(materia);
	}

	@Override
	public void eliminarMPXFormula(MateriaFormula materia) {
		getSessionFactory().getCurrentSession().delete(materia);
	}

	// Marca
	@SuppressWarnings("unchecked")
	@Override
	public List<CatTipoPan> obtenerTiposPan() {
		String stringQuery;
		Query query;
		stringQuery = "FROM CatTipoPan ORDER BY descripcion";
		query = getSessionFactory().getCurrentSession().createQuery(stringQuery);
		return query.list();
	}

	@Override
	public void guardarTipoPan(CatTipoPan tipoPan) {
		getSessionFactory().getCurrentSession().save(tipoPan);
	}

	@Override
	public CatTipoPan obtenerTipoPanXCodigo(String codigo) {
		String stringQuery;
		Query query;
		stringQuery = "FROM CatTipoPan us WHERE us.codigo = :codigo AND us.estatus = :estatu";
		query = getSessionFactory().getCurrentSession().createQuery(stringQuery);
		query.setString("codigo", codigo);
		query.setBoolean("estatu", true);
		return (CatTipoPan) query.uniqueResult();
	}

	@Override
	public void actualizarTipoPan(CatTipoPan tipoPan) {
		getSessionFactory().getCurrentSession().update(tipoPan);
	}

	@Override
	public void eliminarTipoPan(CatTipoPan tipoPan) {
		getSessionFactory().getCurrentSession().delete(tipoPan);
	}

	// pan por sucursal
	@SuppressWarnings("unchecked")
	@Override
	public List<PanXSucursal> obtenerPanXSucursales() {
		String stringQuery;
		Query query;
		stringQuery = "FROM PanXSucursal ORDER BY id.idUbicacion";
		query = getSessionFactory().getCurrentSession().createQuery(stringQuery);
		return query.list();
	}

	@Override
	public void guardarPanXSucursal(PanXSucursal pansuc) {
		getSessionFactory().getCurrentSession().save(pansuc);
	}

	@Override
	public PanXSucursal obtenerPanXSucursalXId(PanXSucursalId id) {
		String stringQuery;
		Query query;
		stringQuery = "FROM PanXSucursal us WHERE us.id.codigopan = :codigopa " + "AND us.id.idUbicacion = :idubica";
		query = getSessionFactory().getCurrentSession().createQuery(stringQuery);
		query.setString("codigopa", id.getCodigopan());
		query.setInteger("idubica", id.getIdUbicacion());
		return (PanXSucursal) query.uniqueResult();
	}

	@Override
	public void actualizarPanXSucursal(PanXSucursal pansuc) {
		getSessionFactory().getCurrentSession().update(pansuc);
	}
	
	

	@Override
	public void eliminarPanXSucursal(PanXSucursal pansuc) {
		getSessionFactory().getCurrentSession().delete(pansuc);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PanXSucursal> obtenerPanXSucursalesXFecha(Date fec, int tipo, int sucursal) {
		Session session = null;

		try {
			session = sessionFactory.openSession();
			// 2admin 1usuario //tipo 1 es entrega tipo2 es cuando reciben
			if (tipo == 2) {
				return (List<PanXSucursal>) session
						.createQuery("FROM PanXSucursal a WHERE a.id.fecha = :fec AND a.id.tipo = 1")
						.setParameter("fec", fec, StandardBasicTypes.DATE).list();
			} else if (tipo == 1) {
				return (List<PanXSucursal>) session
						.createQuery("FROM PanXSucursal a WHERE a.id.fecha = :fec"
								+ " AND a.id.idUbicacion = :idSucur AND a.estatus = 1")
						.setParameter("fec", fec, StandardBasicTypes.DATE)
						.setParameter("idSucur", sucursal, StandardBasicTypes.INTEGER).list();
			} else if (tipo == 3) {
				// System.out.println("daooo 33333"+fec+" "+sucursal);
				return (List<PanXSucursal>) session
						.createQuery("FROM PanXSucursal a WHERE a.id.fecha = :fec"
								+ " AND a.id.idUbicacion = :idSucur AND a.estatus = 2 "
								+ " AND a.cantidad > 0 ORDER BY a.catTipoPan.descripcion")
						.setParameter("fec", fec, StandardBasicTypes.DATE)
						.setParameter("idSucur", sucursal, StandardBasicTypes.INTEGER).list();
			} else if (tipo == 4) {
				return (List<PanXSucursal>) session
						.createQuery("FROM PanXSucursal a WHERE a.id.fecha = :fec" + " AND a.id.idUbicacion = :idSucur")
						.setParameter("fec", fec, StandardBasicTypes.DATE)
						.setParameter("idSucur", sucursal, StandardBasicTypes.INTEGER).list();

			} else {
				return null;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
				session = null;
			}
		}
	}
	
	

	// registro ventas
	@SuppressWarnings("unchecked")
	@Override
	public List<RegistroVenta> obtenerRegistroVenta() {
		String stringQuery;
		Query query;
		stringQuery = "FROM RegistroVenta ORDER BY folio";
		query = getSessionFactory().getCurrentSession().createQuery(stringQuery);
		return query.list();
	}

	@Override
	public void guardarRegistroVenta(RegistroVenta registroVenta) {
		getSessionFactory().getCurrentSession().save(registroVenta);
	}

	@Override
	public RegistroVenta obtenerRegistroVentaXFolio(String id) {
		String stringQuery;
		Query query;
		stringQuery = "FROM RegistroVenta us WHERE us.id.codigopan = :codigopa " + "AND us.id.idUbicacion = :idubica";
		query = getSessionFactory().getCurrentSession().createQuery(stringQuery);
		query.setString("folio", id);
		return (RegistroVenta) query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RegistroVenta> obtenerRegistroVentaXFol(String folio) {
		String stringQuery;
		Query query;
		stringQuery = "FROM RegistroVenta us WHERE us.id.folio = :folio "; // + "AND us.id.idUbicacion = :idubica";
		query = getSessionFactory().getCurrentSession().createQuery(stringQuery);
		query.setString("folio", folio);
		return query.list();
	}

	@Override
	public void actualizarRegistroVenta(RegistroVenta registroVenta) {
		getSessionFactory().getCurrentSession().update(registroVenta);
	}

	@Override
	public void eliminarRegistroVenta(RegistroVenta registroVenta) {
		getSessionFactory().getCurrentSession().delete(registroVenta);
	}

	@Override
	public int consecutivoXAnio(int cveUbica) {
	    try {
	        Calendar calInicio = Calendar.getInstance();
	        calInicio.set(Calendar.MONTH, Calendar.JANUARY);
	        calInicio.set(Calendar.DAY_OF_MONTH, 1);
	        calInicio.set(Calendar.HOUR_OF_DAY, 0);
	        calInicio.set(Calendar.MINUTE, 0);
	        calInicio.set(Calendar.SECOND, 0);
	        calInicio.set(Calendar.MILLISECOND, 0);
	        Date fechaInicio = calInicio.getTime();

	        Calendar calFin = Calendar.getInstance();
	        calFin.set(Calendar.MONTH, Calendar.DECEMBER);
	        calFin.set(Calendar.DAY_OF_MONTH, 31);
	        calFin.set(Calendar.HOUR_OF_DAY, 23);
	        calFin.set(Calendar.MINUTE, 59);
	        calFin.set(Calendar.SECOND, 59);
	        calFin.set(Calendar.MILLISECOND, 999);
	        Date fechaFin = calFin.getTime();

	        Session session = sessionFactory.openSession();
	        String hql = "SELECT COALESCE(MAX(o.consecutivo), 0) " +
	                     "FROM RegistroVenta o " +
	                     "WHERE o.fecha >= :inicio AND o.fecha <= :fin " +
	                     "AND o.catUbicacion.id = :uni";

	        Query query = session.createQuery(hql);
	        query.setParameter("inicio", fechaInicio);
	        query.setParameter("fin", fechaFin);
	        query.setInteger("uni", cveUbica);

	        int maxConsecutivo = ((Number) query.uniqueResult()).intValue();
	        session.close();
	        return maxConsecutivo;

	    } catch (Exception e) {
	        e.printStackTrace();
	        return 0;
	    }
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<RegistroVenta> obtenerRegVentaXDia(Date fecha) {
		Session session = null;

		try {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(fecha);
			calendar.add(Calendar.HOUR_OF_DAY, 23);
			calendar.add(Calendar.MINUTE, 59);
			calendar.add(Calendar.SECOND, 59);
			// calendar.add(Calendar.MILLISECOND, 999);

			Calendar calendar2 = Calendar.getInstance();
			calendar2.setTime(fecha);
			calendar2.add(Calendar.HOUR_OF_DAY, 0);
			calendar2.add(Calendar.MINUTE, 0);
			calendar2.add(Calendar.SECOND, 0);
			/*
			 * System.out.println(calendar2.getTime());
			 * System.out.println(calendar.getTime());
			 */

			session = sessionFactory.openSession();
			return (List<RegistroVenta>) session
					.createQuery("FROM RegistroVenta rv WHERE rv.fecha BETWEEN :fec1 AND :fec2 ORDER BY rv.fecha DESC")
					.setDate("fec1", calendar2.getTime()).setDate("fec2", calendar.getTime()).list();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
				session = null;
			}
		}
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<RegistroVenta> obtenerTotalVentaXDiaXSucursal(Date fecha, int idSucursal) {
	    try {
	        // Obtener rango de inicio y fin del día
	        Calendar calInicio = Calendar.getInstance();
	        calInicio.setTime(fecha);
	        calInicio.set(Calendar.HOUR_OF_DAY, 0);
	        calInicio.set(Calendar.MINUTE, 0);
	        calInicio.set(Calendar.SECOND, 0);
	        calInicio.set(Calendar.MILLISECOND, 0);
	        Date fechaInicio = calInicio.getTime();

	        Calendar calFin = Calendar.getInstance();
	        calFin.setTime(fecha);
	        calFin.set(Calendar.HOUR_OF_DAY, 23);
	        calFin.set(Calendar.MINUTE, 59);
	        calFin.set(Calendar.SECOND, 59);
	        calFin.set(Calendar.MILLISECOND, 999);
	        Date fechaFin = calFin.getTime();

	        Session session = sessionFactory.openSession();
	        StringBuilder hql = new StringBuilder("FROM RegistroVenta o WHERE o.fecha >= :inicio AND o.fecha <= :fin ");

	        if (idSucursal != 1) {
	            hql.append("AND o.catUbicacion.id = :uni ");
	        }

	        hql.append("ORDER BY o.id.folio");

	        Query query = session.createQuery(hql.toString());
	        query.setParameter("inicio", fechaInicio);
	        query.setParameter("fin", fechaFin);

	        if (idSucursal != 1) {
	            query.setInteger("uni", idSucursal);
	        }

	        List<RegistroVenta> ventas = query.list();
	        session.close();
	        return ventas;

	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}

	
	@Override
	public BigDecimal obtenerTotalVentaXDiaXTipo(Date fecha, int idSucursal) {
	    try {
	        // Rango de inicio y fin del día
	        Calendar calInicio = Calendar.getInstance();
	        calInicio.setTime(fecha);
	        calInicio.set(Calendar.HOUR_OF_DAY, 0);
	        calInicio.set(Calendar.MINUTE, 0);
	        calInicio.set(Calendar.SECOND, 0);
	        calInicio.set(Calendar.MILLISECOND, 0);
	        Date fechaInicio = calInicio.getTime();

	        Calendar calFin = Calendar.getInstance();
	        calFin.setTime(fecha);
	        calFin.set(Calendar.HOUR_OF_DAY, 23);
	        calFin.set(Calendar.MINUTE, 59);
	        calFin.set(Calendar.SECOND, 59);
	        calFin.set(Calendar.MILLISECOND, 999);
	        Date fechaFin = calFin.getTime();

	        Session session = sessionFactory.openSession();
	        String hql;
	        Query query;

	        if (idSucursal == 1) {
	            hql = "SELECT COALESCE(SUM(o.total), 0) " +
	                  "FROM RegistroVenta o " +
	                  "WHERE o.fecha >= :inicio AND o.fecha <= :fin";
	            query = session.createQuery(hql);
	        } else {
	            hql = "SELECT COALESCE(SUM(o.total), 0) " +
	                  "FROM RegistroVenta o " +
	                  "WHERE o.fecha >= :inicio AND o.fecha <= :fin " +
	                  "AND o.catUbicacion.id = :uni";
	            query = session.createQuery(hql);
	            query.setInteger("uni", idSucursal);
	        }

	        query.setParameter("inicio", fechaInicio);
	        query.setParameter("fin", fechaFin);

	        BigDecimal total = (BigDecimal) query.uniqueResult();
	        session.close();
	        return total;

	    } catch (Exception e) {
	        e.printStackTrace();
	        return BigDecimal.ZERO;
	    }
	}


	@Override
	public BigDecimal obtenerIngresoXDiaXTipo(Date fecha, int tipo, int idSucursal, int idUsuario) {
	    try {
	        // Calcular rango del día
	        Calendar calInicio = Calendar.getInstance();
	        calInicio.setTime(fecha);
	        calInicio.set(Calendar.HOUR_OF_DAY, 0);
	        calInicio.set(Calendar.MINUTE, 0);
	        calInicio.set(Calendar.SECOND, 0);
	        calInicio.set(Calendar.MILLISECOND, 0);
	        Date fechaInicio = calInicio.getTime();

	        Calendar calFin = Calendar.getInstance();
	        calFin.setTime(fecha);
	        calFin.set(Calendar.HOUR_OF_DAY, 23);
	        calFin.set(Calendar.MINUTE, 59);
	        calFin.set(Calendar.SECOND, 59);
	        calFin.set(Calendar.MILLISECOND, 999);
	        Date fechaFin = calFin.getTime();

	        Session session = sessionFactory.openSession();
	        StringBuilder hql = new StringBuilder(
	            "SELECT COALESCE(SUM(o.monto), 0) " +
	            "FROM RegistroEfectivo o " +
	            "WHERE o.fecha >= :inicio AND o.fecha <= :fin " +
	            "AND o.tipo = :tipo " +
	            "AND o.catUsuario.id = :idUsu "
	        );

	        if (idSucursal != 1) {
	            hql.append("AND o.idSucursal = :uni ");
	        }

	        Query query = session.createQuery(hql.toString());
	        query.setParameter("inicio", fechaInicio);
	        query.setParameter("fin", fechaFin);
	        query.setInteger("tipo", tipo);
	        query.setInteger("idUsu", idUsuario);

	        if (idSucursal != 1) {
	            query.setInteger("uni", idSucursal);
	        }

	        BigDecimal total = (BigDecimal) query.uniqueResult();
	        session.close();
	        return total;

	    } catch (Exception e) {
	        e.printStackTrace();
	        return BigDecimal.ZERO;
	    }
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<RegistroVenta> obtenerRegVentaXDiaConsulta(Date fecha, int idSucursal) {
	    try {
	        // Rango de inicio y fin del día
	        Calendar calInicio = Calendar.getInstance();
	        calInicio.setTime(fecha);
	        calInicio.set(Calendar.HOUR_OF_DAY, 0);
	        calInicio.set(Calendar.MINUTE, 0);
	        calInicio.set(Calendar.SECOND, 0);
	        calInicio.set(Calendar.MILLISECOND, 0);
	        Date fechaInicio = calInicio.getTime();

	        Calendar calFin = Calendar.getInstance();
	        calFin.setTime(fecha);
	        calFin.set(Calendar.HOUR_OF_DAY, 23);
	        calFin.set(Calendar.MINUTE, 59);
	        calFin.set(Calendar.SECOND, 59);
	        calFin.set(Calendar.MILLISECOND, 999);
	        Date fechaFin = calFin.getTime();

	        Session session = sessionFactory.openSession();
	        StringBuilder hql = new StringBuilder("FROM RegistroVenta o WHERE o.fecha >= :inicio AND o.fecha <= :fin ");

	        if (idSucursal != 1) {
	            hql.append("AND o.catUbicacion.id = :uni ");
	        }

	        hql.append("ORDER BY o.catUbicacion.descripcion");

	        Query query = session.createQuery(hql.toString());
	        query.setParameter("inicio", fechaInicio);
	        query.setParameter("fin", fechaFin);

	        if (idSucursal != 1) {
	            query.setInteger("uni", idSucursal);
	        }

	        List<RegistroVenta> obs = query.list();
	        session.close();
	        return obs;

	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<RegistroVenta> obtenerRegVentaXUsuario(Date fecha, int idUsuario) {
	    try {
	        // Obtener inicio y fin del día
	        Calendar calInicio = Calendar.getInstance();
	        calInicio.setTime(fecha);
	        calInicio.set(Calendar.HOUR_OF_DAY, 0);
	        calInicio.set(Calendar.MINUTE, 0);
	        calInicio.set(Calendar.SECOND, 0);
	        calInicio.set(Calendar.MILLISECOND, 0);
	        Date fechaInicio = calInicio.getTime();

	        Calendar calFin = Calendar.getInstance();
	        calFin.setTime(fecha);
	        calFin.set(Calendar.HOUR_OF_DAY, 23);
	        calFin.set(Calendar.MINUTE, 59);
	        calFin.set(Calendar.SECOND, 59);
	        calFin.set(Calendar.MILLISECOND, 999);
	        Date fechaFin = calFin.getTime();

	        Session session = sessionFactory.openSession();
	        String hql = "FROM RegistroVenta o " +
	                     "WHERE o.fecha >= :inicio AND o.fecha <= :fin " +
	                     "AND o.catUsuario.id = :idUsuario";

	        Query query = session.createQuery(hql);
	        query.setParameter("inicio", fechaInicio);
	        query.setParameter("fin", fechaFin);
	        query.setInteger("idUsuario", idUsuario);

	        List<RegistroVenta> ventas = query.list();
	        session.close();
	        return ventas;

	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}


	@Override
	public PanXSucursal panXSucXFechaXCodigo(String codigo, int sucursal, Date fecha) {
		String stringQuery;
		Query query;
		stringQuery = "FROM PanXSucursal us WHERE us.id.codigopan = :codigo AND"
				+ " us.id.idUbicacion = :sucursal AND us.id.fecha = :fecha";
		query = getSessionFactory().getCurrentSession().createQuery(stringQuery);
		query.setString("codigo", codigo);
		query.setInteger("sucursal", sucursal);
		query.setDate("fecha", fecha);
		return (PanXSucursal) query.uniqueResult();
	}
	

	// Registro Efectivo
	@SuppressWarnings("unchecked")
	@Override
	public List<RegistroEfectivo> obtenerRegistrosEfectivo() {
		String stringQuery;
		Query query;
		stringQuery = "FROM RegistroEfectivo";
		query = getSessionFactory().getCurrentSession().createQuery(stringQuery);
		return query.list();
	}

	@Override
	public void guardarRegistroEfectivo(RegistroEfectivo registroEfectivo) {
		getSessionFactory().getCurrentSession().save(registroEfectivo);
	}

	@Override
	public RegistroEfectivo obtenerRegistroEfectivoXId(int id) {
		String stringQuery;
		Query query;
		stringQuery = "FROM RegistroEfectivo us WHERE us.id = :id";
		query = getSessionFactory().getCurrentSession().createQuery(stringQuery);
		query.setInteger("id", id);
		return (RegistroEfectivo) query.uniqueResult();
	}

	@Override
	public void actualizarRegistroEfectivo(RegistroEfectivo registroEfectivo) {
		getSessionFactory().getCurrentSession().update(registroEfectivo);
	}

	@Override
	public void eliminarRegistroEfectivo(RegistroEfectivo registroEfectivo) {
		getSessionFactory().getCurrentSession().delete(registroEfectivo);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RegistroEfectivo> obtenerRegistrosXEstatusXFecha(int estatus, Date fecha, int idSucursal) {
	    try {
	        // Inicio del día
	        Calendar calInicio = Calendar.getInstance();
	        calInicio.setTime(fecha);
	        calInicio.set(Calendar.HOUR_OF_DAY, 0);
	        calInicio.set(Calendar.MINUTE, 0);
	        calInicio.set(Calendar.SECOND, 0);
	        calInicio.set(Calendar.MILLISECOND, 0);
	        Date fechaInicio = calInicio.getTime();

	        // Fin del día
	        Calendar calFin = Calendar.getInstance();
	        calFin.setTime(fecha);
	        calFin.set(Calendar.HOUR_OF_DAY, 23);
	        calFin.set(Calendar.MINUTE, 59);
	        calFin.set(Calendar.SECOND, 59);
	        calFin.set(Calendar.MILLISECOND, 999);
	        Date fechaFin = calFin.getTime();

	        Session session = sessionFactory.openSession();
	        StringBuilder hql = new StringBuilder();
	        hql.append("FROM RegistroEfectivo o ")
	           .append("WHERE o.estatus = :estatus ")
	           .append("AND o.fecha >= :inicio AND o.fecha <= :fin ");

	        if (idSucursal != 1) {
	            hql.append("AND o.idSucursal = :idSuc ");
	        } else {
	            hql.append("ORDER BY o.idSucursal ");
	        }

	        Query query = session.createQuery(hql.toString());
	        query.setInteger("estatus", estatus);
	        query.setParameter("inicio", fechaInicio);
	        query.setParameter("fin", fechaFin);

	        if (idSucursal != 1) {
	            query.setInteger("idSuc", idSucursal);
	        }

	        List<RegistroEfectivo> obs = query.list();
	        session.close();
	        return obs;

	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}


	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	
	//Pedido
	@SuppressWarnings("unchecked")
	@Override
	public List<Pedido> obtenerPedidos(){
		String stringQuery;
		Query query;
		stringQuery = "FROM Pedido where catEstatusPedido.id= :id   ORDER BY fecEvento ASC";
		query = getSessionFactory().getCurrentSession().createQuery(stringQuery);
		query.setInteger("id", 1);
		return query.list();
	}
	
	public Pedido obtenerPedidoXId(int idTipo) {
		String stringQuery;
		Query query;
		stringQuery = "FROM Pedido us WHERE us.folio = :id";
		query = getSessionFactory().getCurrentSession().createQuery(stringQuery);
		query.setInteger("id", idTipo);
		return (Pedido) query.uniqueResult();
	}
	

	@Override
	public void guardarPedido(Pedido pedido) {
		getSessionFactory().getCurrentSession().save(pedido);
	}
	

	@Override
	public void actualizarPedido(Pedido pedido) {
		getSessionFactory().getCurrentSession().update(pedido);
	}

	@Override
	public void eliminarPedido(Pedido pedido) {
		getSessionFactory().getCurrentSession().delete(pedido);
	}
	
	@Override
	public long consePedidoXAnio(int anio) {
		String stringQuery;
		Query query;
		stringQuery = "SELECT COALESCE(COUNT(us.anio),0) FROM Pedido us "
				+ " WHERE us.anio = :id";
		query = getSessionFactory().getCurrentSession().createQuery(stringQuery);
		query.setInteger("id", anio);
		return (long) query.uniqueResult();
	}
	
	
	//CatEstatusPedido
	@SuppressWarnings("unchecked")
	@Override
	public List<CatEstatusPedido> obtenerCatEstatusPedidos(){
		String stringQuery;
		Query query;
		stringQuery = "FROM CatEstatusPedido ORDER BY descripcion";
		query = getSessionFactory().getCurrentSession().createQuery(stringQuery);
		return query.list();
	}
	
	public CatEstatusPedido obtenerCatEstatusPedidoXId(int idTipo) {
		String stringQuery;
		Query query;
		stringQuery = "FROM CatEstatusPedido us WHERE us.id = :id";
		query = getSessionFactory().getCurrentSession().createQuery(stringQuery);
		query.setInteger("id", idTipo);
		return (CatEstatusPedido) query.uniqueResult();
	}
	
	//articulo Pedido
	@SuppressWarnings("unchecked")
	@Override
	public List<ArticuloPedido> obtenerArticuloPedidos(){
		String stringQuery;
		Query query;
		stringQuery = "FROM ArticuloPedido ORDER BY folio";
		query = getSessionFactory().getCurrentSession().createQuery(stringQuery);
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ArticuloPedido> obtenerArticuloPXFolio(String folio){
		String stringQuery;
		Query query;
		stringQuery = "FROM ArticuloPedido where  folio= :folio";
		query = getSessionFactory().getCurrentSession().createQuery(stringQuery);
		query.setString("folio", folio);
		return query.list();
	}
	
	@Override
	public ArticuloPedido obtenerArticuloPedidoXId(int idTipo) {
		String stringQuery;
		Query query;
		stringQuery = "FROM ArticuloPedido us WHERE us.id = :id";
		query = getSessionFactory().getCurrentSession().createQuery(stringQuery);
		query.setInteger("id", idTipo);
		return (ArticuloPedido) query.uniqueResult();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Pedido> obtenerEventoXMD(String mes, String dia) {
		if (dia==null || dia.trim().length()<=0)
			dia="0";
		
		String stringQuery;
		Query query;
		if (dia.equals("0")) {
			stringQuery = "FROM Pedido d WHERE TO_CHAR(d.fecInicio,'MM') = :mes ORDER BY d.horaI ASC";
		} else {
			stringQuery = "FROM Pedido d WHERE TO_CHAR(d.fecInicio,'MM') = :mes " + 
					"AND TO_CHAR(d.fecInicio,'dd') = :dia ORDER BY d.horaI ASC";
		}

		query = getSessionFactory().getCurrentSession().createQuery(stringQuery);
		query.setString("mes", mes);
		if (!dia.equals("0"))
			query.setString("dia", dia);
		return query.list();
	}
	
	@Override
	public void guardarArticuloPedido(ArticuloPedido articuloPedido) {
		getSessionFactory().getCurrentSession().save(articuloPedido);
	}
	
	@Override
	public void actualizarArticuloPedido(ArticuloPedido articuloPedido) {
		getSessionFactory().getCurrentSession().update(articuloPedido);
	}
	
	@Override
	public void eliminaArticuloPedido(ArticuloPedido articuloPedido) {
		getSessionFactory().getCurrentSession().delete(articuloPedido);
	}
	

	@Override
	public void guardarCatEstatusPedido(CatEstatusPedido estatusP) {
		getSessionFactory().getCurrentSession().save(estatusP);
	}
	

	@Override
	public void actualizarCatEstatusPedido(CatEstatusPedido estatusP) {
		getSessionFactory().getCurrentSession().update(estatusP);
	}

	@Override
	public void eliminarCatEstatusPedido(CatEstatusPedido estatusP) {
		getSessionFactory().getCurrentSession().delete(estatusP);
	}
	
	
	// CatListaInicial
	@SuppressWarnings("unchecked")
	@Override
	public List<CatListaInicial> listaInicialXSucursal(int idsucursal){
		String stringQuery;
		Query query;
		stringQuery = "FROM CatListaInicial u where  u.id.idUbicacion= :idsucursal";
		query = getSessionFactory().getCurrentSession().createQuery(stringQuery);
		query.setInteger("idsucursal", idsucursal);
		return query.list();
	}
			
	@Override
	public void guardarCatListaInicial(CatListaInicial catListaInicial) {
		getSessionFactory().getCurrentSession().save(catListaInicial);
	}

	@Override
	public CatListaInicial catListaInicialXId(CatListaInicialId id) {
		String stringQuery;
		Query query;
		stringQuery = "FROM CatListaInicial us WHERE us.id.idUbicacion = :id AND "
				+ "us.id.codigopan = :codigo";
		query = getSessionFactory().getCurrentSession().createQuery(stringQuery);
		query.setInteger("id", id.getIdUbicacion());
		query.setString("codigo", id.getCodigopan());
		return (CatListaInicial) query.uniqueResult();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Double> preciosXSucursalesXFecha(Date fec, int tipo, int sucursal) {
		Session session = null;

		try {
			session = sessionFactory.openSession();
			return (List<Double>) session
					.createQuery("select DISTINCT(c.costo) as costo FROM PanXSucursal as ps INNER JOIN CatTipoPan as c c.codigo = ps.codigo WHERE ps.id.fecha = :fec"
							+ " AND ps.id.idUbicacion = :idSucur AND ps.estatus = 2 "
							+ " AND ps.cantidad > 0 ORDER BY c.costo")
					.setParameter("fec", fec, StandardBasicTypes.DATE)
					.setParameter("idSucur", sucursal, StandardBasicTypes.INTEGER).list();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
				session = null;
			}
		}
	}
	
	
	//PrecioCantidadSucursal
	@Override
	public PrecioCantidadSucursal precioXSucXFechaXPrecio(double costo, int sucursal, Date fecha) {
		String stringQuery;
		Query query;
		stringQuery = "FROM PrecioCantidadSucursal us WHERE us.id.costo = :costo AND"
				+ " us.id.idUbicacion = :sucursal AND us.id.fecha = :fecha";
		query = getSessionFactory().getCurrentSession().createQuery(stringQuery);
		query.setDouble("costo", costo);
		query.setInteger("sucursal", sucursal);
		query.setDate("fecha", fecha);
		return (PrecioCantidadSucursal) query.uniqueResult();
	}
	
	
	@Override
	public void actualizarCatListaInicial(CatListaInicial catListaInicial) {
		getSessionFactory().getCurrentSession().update(catListaInicial);
	}
	
	@Override
	public void eliminarCatListaInicial(CatListaInicial catListaInicial) {
		getSessionFactory().getCurrentSession().delete(catListaInicial);
	}
	
	
	//PrecioCantidadSucursal
	@Override
	public void guardarPrecioCantidadSucursal(PrecioCantidadSucursal pansuc) {
		getSessionFactory().getCurrentSession().save(pansuc);
	}

	@Override
	public void actualizarPrecioCantidadSucursal(PrecioCantidadSucursal pansuc) {
		getSessionFactory().getCurrentSession().update(pansuc);
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<PrecioCantidadSucursal> obtenerPrecioXSucursalesXFecha(Date fec, int tipo, int sucursal){
		Session session = null;
		try {
			session = sessionFactory.openSession();
			// 2admin 1usuario //tipo 1 es entrega tipo2 es cuando reciben
			if (tipo == 2) {
				return (List<PrecioCantidadSucursal>) session
						.createQuery("FROM PrecioCantidadSucursal a WHERE a.id.fecha = :fec AND a.id.tipo = 1")
						.setParameter("fec", fec, StandardBasicTypes.DATE).list();
			} else if (tipo == 1) {
				return (List<PrecioCantidadSucursal>) session
						.createQuery("FROM PrecioCantidadSucursal a WHERE a.id.fecha = :fec"
								+ " AND a.id.idUbicacion = :idSucur AND a.estatus = 1")
						.setParameter("fec", fec, StandardBasicTypes.DATE)
						.setParameter("idSucur", sucursal, StandardBasicTypes.INTEGER).list();
			} else if (tipo == 3) {
				return (List<PrecioCantidadSucursal>) session
						.createQuery("FROM PrecioCantidadSucursal a WHERE a.id.fecha = :fec"
								+ " AND a.id.idUbicacion = :idSucur AND a.estatus = 2 "
								+ " AND a.cantidadVendida > 0 ORDER BY a.id.costo")
						.setParameter("fec", fec, StandardBasicTypes.DATE)
						.setParameter("idSucur", sucursal, StandardBasicTypes.INTEGER).list();
			} else if (tipo == 4) {
				return (List<PrecioCantidadSucursal>) session
						.createQuery("FROM PrecioCantidadSucursal a WHERE a.id.fecha = :fec" + " AND a.id.idUbicacion = :idSucur")
						.setParameter("fec", fec, StandardBasicTypes.DATE)
						.setParameter("idSucur", sucursal, StandardBasicTypes.INTEGER).list();

			} else {
				return null;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
				session = null;
			}
		}
	}
	
	
	//PrecioCantidadVenta
	@Override
	public void guardarPrecioCantidadVenta(PrecioCantidadVenta registroVenta) {
		getSessionFactory().getCurrentSession().save(registroVenta);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<PrecioCantidadVenta> registroVentaXFolPrecio(String folio) {
		String stringQuery;
		Query query;
		stringQuery = "FROM PrecioCantidadVenta us WHERE us.id.folio = :folio "; // + "AND us.id.idUbicacion = :idubica";
		query = getSessionFactory().getCurrentSession().createQuery(stringQuery);
		query.setString("folio", folio);
		return query.list();
	}
	
	/*@Override
	public int consecutivoXAnioPrecio(int cveUbica) {

		Calendar cal = Calendar.getInstance();
		// cal.setTime(fecha);
		int anio = cal.get(Calendar.YEAR);
		int mes = cal.get(Calendar.MONTH) + 1;
		int dia = cal.get(Calendar.DAY_OF_MONTH);
		int hora = cal.get(Calendar.HOUR);

		Session session = sessionFactory.openSession();
		String stringQuery;
		Query query;
		stringQuery = "SELECT COALESCE(MAX(o.consecutivo), 0) FROM PrecioCantidadVenta o WHERE YEAR(o.fecha)= :anio AND"
				+ " o.idUbicacion = :uni "; // AND HOUR(o.fecha) > :hora
		query = session.createQuery(stringQuery);
		query.setInteger("anio", anio);
		query.setInteger("uni", cveUbica);
		// query.setInteger("hora", hora);

		int obs = (Integer) query.uniqueResult();
		session.close();
		return obs;
	}*/
	
	
	@Override
	public int consecutivoXAnioPrecio(int cveUbica) {
	    Calendar inicio = Calendar.getInstance();
	    inicio.set(Calendar.MONTH, Calendar.JANUARY);
	    inicio.set(Calendar.DAY_OF_MONTH, 1);
	    inicio.set(Calendar.HOUR_OF_DAY, 0);
	    inicio.set(Calendar.MINUTE, 0);
	    inicio.set(Calendar.SECOND, 0);
	    inicio.set(Calendar.MILLISECOND, 0);

	    Calendar fin = Calendar.getInstance();
	    fin.set(Calendar.MONTH, Calendar.DECEMBER);
	    fin.set(Calendar.DAY_OF_MONTH, 31);
	    fin.set(Calendar.HOUR_OF_DAY, 23);
	    fin.set(Calendar.MINUTE, 59);
	    fin.set(Calendar.SECOND, 59);
	    fin.set(Calendar.MILLISECOND, 999);

	    Session session = sessionFactory.openSession();
	    String stringQuery;
	    Query query;
	    stringQuery = "SELECT COALESCE(MAX(o.consecutivo), 0) " +
	                  "FROM PrecioCantidadVenta o " +
	                  "WHERE o.fecha BETWEEN :inicio AND :fin " +
	                  "AND o.idUbicacion = :uni";

	    query = session.createQuery(stringQuery);
	    query.setParameter("inicio", inicio.getTime());
	    query.setParameter("fin", fin.getTime());
	    query.setParameter("uni", cveUbica);

	    Integer obs = (Integer) query.uniqueResult();
	    session.close();

	    return obs != null ? obs : 0;
	}


}
