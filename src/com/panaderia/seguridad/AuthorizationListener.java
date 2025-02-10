package com.panaderia.seguridad;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

public class AuthorizationListener implements PhaseListener {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(AuthorizationListener.class);

	@Override
	public void afterPhase(PhaseEvent pe) {
		FacesContext facesContext = pe.getFacesContext();
		String currentPage = facesContext.getViewRoot().getViewId();

		boolean isLoginPage = (currentPage.lastIndexOf("index.xhtml") > -1);
		HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);

		if (session == null) {
			NavigationHandler nh = facesContext.getApplication().getNavigationHandler();
			nh.handleNavigation(facesContext, null, "login");
		} else {
			Object currentUser = session.getAttribute("usuario");

			if (!isLoginPage && (currentUser == null || currentUser == "")) {
				try {
					FacesContext.getCurrentInstance().getExternalContext()
							.redirect("/brioche/index.xhtml?faces-redirect=true");
				} catch (IOException e) {
					StringWriter stack = new StringWriter();
					e.printStackTrace(new PrintWriter(stack));
					logger.error(stack.toString());
				}

			}
		}
	}

	@Override
	public void beforePhase(PhaseEvent pe) {
	}

	@Override
	public PhaseId getPhaseId() {
		return PhaseId.RESTORE_VIEW;
	}

}