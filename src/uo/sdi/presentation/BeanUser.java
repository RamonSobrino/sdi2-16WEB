package uo.sdi.presentation;

import java.io.Serializable;
import java.util.ResourceBundle;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import uo.sdi.dto.User;

@ManagedBean(name = "user")
@SessionScoped
public class BeanUser extends User implements Serializable {

	private static final long serialVersionUID = -4192084276740184132L;

	public void setUser(User u) {
		setId(u.getId());
		setLogin(u.getLogin());
		setPassword(u.getPassword());
		setEmail(u.getEmail());
		setStatus(u.getStatus());
		setIsAdmin(u.getIsAdmin());
	}

	public void initUser(ActionEvent event) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ResourceBundle bundle = facesContext.getApplication()
				.getResourceBundle(facesContext, "msgs");
		setId(null);
		setLogin(bundle.getString("valorDefectoLogin"));
		setEmail(bundle.getString("valorDefectoEmail"));
	}

}
