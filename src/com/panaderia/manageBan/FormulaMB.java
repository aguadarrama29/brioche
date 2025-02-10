package com.panaderia.manageBan;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import com.panaderia.modelo.negocio.CatFormula;
import com.panaderia.modelo.negocio.CatMateriaPrima;
import com.panaderia.modelo.negocio.MateriaFormula;
import com.panaderia.modelo.negocio.MateriaFormulaId;
import com.panaderia.seguridad.UserSession;
import com.panaderia.servicio.IBriocheServicio;
import com.panaderia.util.MsgUtil;



@ManagedBean(name = "formulaMB")
@ViewScoped
public class FormulaMB implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2395072936818096911L;
	@ManagedProperty(value = "#{BriocheServicio}")
	IBriocheServicio iBriocheServicio;
	@ManagedProperty(value = "#{userSession}")
	UserSession userSession;

	
	private CatFormula formula;
	private List<CatFormula> lista;
	private List<CatFormula> listaFilter;
	private CatFormula slcformula;
	
	private CatMateriaPrima  catMateriaPrima;
	
	private MateriaFormula materiaFormula;
	private BigDecimal cantidadMP;
	private MateriaFormula slcmateria;
	private List<MateriaFormula> listaMPF;
	private List<MateriaFormula> listaFilterMPF;
	
	private boolean bandera=true;

	public FormulaMB() {

	}

	@PostConstruct
	public void inicio() {
		formula = new CatFormula();
		materiaFormula= new MateriaFormula();
		
		lista = getiBriocheServicio().obtenerFormulas();
		listaMPF=getiBriocheServicio().obtenerMPXFormula();
		bandera=true;
	}

	public void guardar() {

		try {

			if (formula.getId() == 0) {
				
				getiBriocheServicio().guardarFormula(formula);

				lista.add(formula);

				MsgUtil.msgInfo("Exito!", "Registro Guardado.");
			} else {
				
				getiBriocheServicio().actualizarFormula(formula);
				MsgUtil.msgInfo("Exito!", "Registro Actualizado.");
				
			}

			//formula = new CatFormula();
			cancelar();

		} catch (Exception e) {
			e.printStackTrace();
			MsgUtil.msgError("Error!", "Registro no procesado.");
		}

	}

	public void editar() {
		formula = slcformula;
	}

	public void eliminar() {
		try {
			getiBriocheServicio().eliminarFormula(slcformula);
			lista.remove(slcformula);
			formula = new CatFormula();
			MsgUtil.msgInfo("Exito!", "Registro Eliminado.");
		} catch (Exception e) {
			e.printStackTrace();
			MsgUtil.msgError("Error!", "Registro no eliminado.");
		}
	}

	public void cancelar() {
		formula = new CatFormula();
		slcformula = null;
		catMateriaPrima=null;
		
		cantidadMP=null;
		bandera=true;
	}
	
	
	//obtener materia prima complete
			public List<CatMateriaPrima> completeMateria(String query) {
				List<CatMateriaPrima> articulos = new ArrayList<CatMateriaPrima>();
				articulos=getiBriocheServicio().obtenerMaterias();
				
				List<CatMateriaPrima> filtrados = new ArrayList<CatMateriaPrima>();

				for (int i = 0; i < articulos.size(); i++) {
					CatMateriaPrima c = articulos.get(i);
					if (c.getDescripcion() != null) {
						if (c.getDescripcion().toUpperCase().contains(query.toUpperCase())) {
							filtrados.add(c);
						}
					}
				}
				return filtrados;
			}

			
			public void editarMateria() {
				materiaFormula = slcmateria;
				catMateriaPrima=getiBriocheServicio().obtenerMateriaXId(slcmateria.getId().getIdMateria());
				cantidadMP=slcmateria.getCantidadMatPrima();
				bandera=false;
			}
			
			
			public void guardarIngrediente() {

				try {	
					MateriaFormulaId mid= new MateriaFormulaId();
					mid.setIdFormula(formula.getId());
					mid.setIdMateria(catMateriaPrima.getId());
										
					if (bandera) {
						MateriaFormula existeMF=getiBriocheServicio().obtenerMPXFormulaXId(mid);
						//System.out.println("que tengo"+existeMF.getId().getIdMateria());
						//if(existeMF.getId().getIdMateria()!=catMateriaPrima.getId()) {
						if(existeMF==null) {
							materiaFormula.setId(mid);
							materiaFormula.setCantidadMatPrima(cantidadMP);
							materiaFormula.setUnidadMedidaDes(catMateriaPrima.getCatUnidadMedida().getAbreviatura());
							getiBriocheServicio().guardarMPXFormula(materiaFormula);
							listaMPF.add(materiaFormula);
							MsgUtil.msgInfo("Exito!", "Registro Guardado.");
						}else {
							MsgUtil.msgWarning("Error!", "Ya existe un registro con esta Materia Prima.");
						}
						
						
					}else {
						materiaFormula.setCantidadMatPrima(cantidadMP);
						getiBriocheServicio().actualizarMPXFormula(materiaFormula);
						MsgUtil.msgInfo("Exito!", "Registro Actualizado.");
					}
				
					cancelar();

				} catch (Exception e) {
					e.printStackTrace();
					MsgUtil.msgError("Error!", "Registro no procesado.");
				}

			}
			
			
			public void eliminarMateria() {
				try {
					getiBriocheServicio().eliminarMPXFormula(slcmateria);
					listaMPF.remove(slcmateria);
					materiaFormula = new MateriaFormula();
					MsgUtil.msgInfo("Exito!", "Registro Eliminado.");
				} catch (Exception e) {
					e.printStackTrace();
					MsgUtil.msgError("Error!", "Registro no eliminado.");
				}
			}
			
			
			public CatMateriaPrima cargaMateriaPri(int di) {
				return getiBriocheServicio().obtenerMateriaXId(di);
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

	public CatFormula getformula() {
		return formula;
	}

	public void setformula(CatFormula formula) {
		this.formula = formula;
	}

	public List<CatFormula> getLista() {
		return lista;
	}

	public void setLista(List<CatFormula> lista) {
		this.lista = lista;
	}

	public List<CatFormula> getListaFilter() {
		return listaFilter;
	}

	public void setListaFilter(List<CatFormula> listaFilter) {
		this.listaFilter = listaFilter;
	}

	public CatFormula getSlcformula() {
		return slcformula;
	}

	public void setSlcformula(CatFormula slcformula) {
		this.slcformula = slcformula;
	}

	public CatFormula getFormula() {
		return formula;
	}

	public void setFormula(CatFormula formula) {
		this.formula = formula;
	}

	public CatMateriaPrima getCatMateriaPrima() {
		return catMateriaPrima;
	}

	public void setCatMateriaPrima(CatMateriaPrima catMateriaPrima) {
		this.catMateriaPrima = catMateriaPrima;
	}

	public MateriaFormula getMateriaFormula() {
		return materiaFormula;
	}

	public void setMateriaFormula(MateriaFormula materiaFormula) {
		this.materiaFormula = materiaFormula;
	}

	public BigDecimal getCantidadMP() {
		return cantidadMP;
	}

	public void setCantidadMP(BigDecimal cantidadMP) {
		this.cantidadMP = cantidadMP;
	}

	public boolean isBandera() {
		return bandera;
	}

	public void setBandera(boolean bandera) {
		this.bandera = bandera;
	}

	public MateriaFormula getSlcmateria() {
		return slcmateria;
	}

	public void setSlcmateria(MateriaFormula slcmateria) {
		this.slcmateria = slcmateria;
	}

	public List<MateriaFormula> getListaMPF() {
		return listaMPF;
	}

	public void setListaMPF(List<MateriaFormula> listaMPF) {
		this.listaMPF = listaMPF;
	}

	public List<MateriaFormula> getListaFilterMPF() {
		return listaFilterMPF;
	}

	public void setListaFilterMPF(List<MateriaFormula> listaFilterMPF) {
		this.listaFilterMPF = listaFilterMPF;
	}
	

}
