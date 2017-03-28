package uo.sdi.presentation;

import java.io.Serializable;
import java.util.Map;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import alb.util.log.Log;
import uo.sdi.business.LoginService;
import uo.sdi.business.exception.BusinessException;
import uo.sdi.dto.User;
import uo.sdi.dto.types.UserStatus;
import uo.sdi.infrastructure.Factories;
import uo.sdi.presentation.helper.BundleFactorie;

@ManagedBean(name = "login")
@SessionScoped
public class BeanLogin implements Serializable {
	private static final long serialVersionUID = 6L;
	// For login
	private String name = "";
	private String password = "";

	// for register
	private String email = "";
	private String passwordAgain = "";

	private String result = "login_form_result_valid";

	public BeanLogin() {
		System.out.println("BeanLogin - No existia");
	}

	public String verify() {
		ResourceBundle bundle = BundleFactorie.getMessagesBundle();
		LoginService login = Factories.services.getLoginService();
		User user;
		try {
			user = login.doLogin(name, password);
		} catch (BusinessException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle
							.getString("error"), bundle
							.getString("login_form_result_error")));
			Log.error(e.getMessage());
			return "fallo";
		}
		if (user != null) {
			putUserInSession(user);

			if (user.getIsAdmin()) {
				Log.info("Administrador " + user.getLogin()
						+ " entrado en sesion");
				return "exitoAdministrador";
			}
			Log.info("Usuario " + user.getLogin() + " entrado en sesion");
			return "exitoUser";
		}
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle
						.getString("error"), bundle
						.getString("login_form_result_error")));
		Log.warn("Usuario " + name + " no existe o contraseña incorrecta");
		return "fallo";
	}

	public String register() {
		FacesContext cont = FacesContext.getCurrentInstance();
		ResourceBundle bundle = BundleFactorie.getMessagesBundle();
		// TODO: Change to hash password
		User u = new User().setLogin(name).setEmail(email).setIsAdmin(false)
				.setPassword(password).setStatus(UserStatus.ENABLED);
		try {
			Factories.services.getUserService().registerUser(u);
			cont.addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, bundle
							.getString("success"), bundle
							.getString("success_register")));
		} catch (BusinessException e) {
			cont.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					bundle.getString("error"), e.getMessage()));
			e.printStackTrace();
			Log.warn(e.getMessage());
			return "fallo"; // Volvemos a register
		}
		Log.info("Usuario " + name + " registrado");
		return "exito"; // Nos vamos a index
	}

	public String logout() {
		putUserOutOfSession();
		FacesContext cont = FacesContext.getCurrentInstance();
		ResourceBundle bundle = BundleFactorie.getMessagesBundle();
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
				bundle.getString("success"), bundle.getString("success_logout"));
		cont.addMessage(null, msg);
		Log.info("Sesion cerrada");
		return "exito";
	}

	private void putUserInSession(User user) {
		Map<String, Object> session = FacesContext.getCurrentInstance()
				.getExternalContext().getSessionMap();
		session.put("LOGGEDIN_USER", user);
	}

	private void putUserOutOfSession() {
		Map<String, Object> session = FacesContext.getCurrentInstance()
				.getExternalContext().getSessionMap();
		session.remove("LOGGEDIN_USER");
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getResult() {
		return result;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPasswordAgain() {
		return passwordAgain;
	}

	public void setPasswordAgain(String passwordAgain) {
		this.passwordAgain = passwordAgain;
	}

	public void setResult(String result) {
		this.result = result;
	}
}