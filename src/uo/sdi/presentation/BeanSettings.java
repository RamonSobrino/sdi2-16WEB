package uo.sdi.presentation;

import java.io.Serializable;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.*;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import alb.util.log.Log;
import alb.util.log.LogLevel;

@ManagedBean(name = "settings")
@SessionScoped
public class BeanSettings implements Serializable {

	private static final long serialVersionUID = 2L;

	private static final Locale ENGLISH = new Locale("en");
	private static final Locale SPANISH = new Locale("es");

	// uso de inyección de dependencia
	// @ManagedProperty(value = "#{user}")
	// private BeanUser user;

	private Locale locale = new Locale("es");

	// Se inicia correctamente el Managed Bean inyectado si JSF lo hubiera
	// creado
	// y en caso contrario se crea.
	// (hay que tener en cuenta que es un Bean de sesión)
	// Se usa @PostConstruct, ya que en el contructor no se sabe todavía si
	// el MBean ya estaba construido y en @PostConstruct SI.
	@PostConstruct
	public void init() {
		Log.info("BeanSettings - PostConstruct");
		Log.setLogLevel(LogLevel.DEBUG);
		// Buscamos el alumno en la sesión. Esto es un patrón factoría
		// claramente.
		// FIXME:
		// user = Factories.beans.createBeanAlumno();
	}

	// Es sólo a modo de traza.
	@PreDestroy
	public void end() {
		Log.info("BeanSettings - PreDestroy");
	}

	// public BeanUser getUser() {
	// return user;
	// }
	//
	// public void setUser(BeanUser user) {
	// this.user = user;
	// }

	public Locale getLocale() {
		// Aqui habria que cambiar algo de código para coger locale del
		// navegador
		// la primera vez que se accede a getLocale(), de momento dejamos como
		// idioma de partida “es”
		// this.locale =
		// FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
		return (locale);
	}

	public void setSpanish(ActionEvent event) {
		locale = SPANISH;
		try {
			FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
			/*
			 * if (user != null) user.iniciaAlumno(null);
			 */
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void setEnglish(ActionEvent event) {
		locale = ENGLISH;
		try {
			FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
			/*
			 * if (user != null) user.iniciaAlumno(null);
			 */
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}