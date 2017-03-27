package uo.sdi.presentation;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.bean.*;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;

import alb.util.log.Log;
import uo.sdi.business.AdminService;
import uo.sdi.business.Services;
import uo.sdi.business.TaskService;
import uo.sdi.business.UserService;
import uo.sdi.business.exception.BusinessException;
import uo.sdi.dto.Task;
import uo.sdi.dto.User;
import uo.sdi.infrastructure.BundleFactorie;

@ManagedBean(name = "controller")
@SessionScoped
public class BeanUsers implements Serializable {
	private static final long serialVersionUID = 55555L;
	// Se añade este atributo de entidad para recibir el User concreto
	// selecionado de la tabla o de un formulario
	// Es necesario inicializarlo para que al entrar desde el formulario de
	// AltaForm.xml se puedan
	// dejar los avalores en un objeto existente.

	// uso de inyección de dependencia
	@ManagedProperty(value = "#{user}")
	private BeanUser user;

	// private User[] users = null;
	private List<User> users = null;
	// private Task[] tasks =null;
	private List<Task> tasks = null;

	private String tipoListado;

	/*
	 * public Task[] getTasks() { return tasks; }
	 * 
	 * public void setTasks(Task[] tasks) { this.tasks = tasks; }
	 */

	@SuppressWarnings("unused")
	private boolean inboxFlag;
	private boolean inboxFinalizados = false;
	@SuppressWarnings("unused")
	private boolean semanaFlag;
	@SuppressWarnings("unused")
	private boolean diaFlag;

	public boolean getInboxFlag() {
		return this.tipoListado.equals("Inbox");
	}

	public void setInboxFlag(boolean inboxFlag) {
		this.inboxFlag = inboxFlag;
	}

	public boolean getSemanaFlag() {
		return this.tipoListado.equals("Semana");
	}

	public void setSemanaFlag(boolean semanaFlag) {
		this.semanaFlag = semanaFlag;
	}

	public boolean getDiaFlag() {
		return this.tipoListado.equals("Dia");
	}

	public void setDiaFlag(boolean diaFlag) {
		this.diaFlag = diaFlag;
	}

	@SuppressWarnings("unused")
	private boolean colorFecha;
	@SuppressWarnings("unused")
	private boolean colorCategoria;

	public boolean getColorFecha() {
		return rojoFecha();
	}

	public void setColorFecha(boolean colorFecha) {
		this.colorFecha = colorFecha;
	}

	public boolean getColorCategoria() {
		return rojoCategoria();
	}

	public void setColorCategoria(boolean colorCategoria) {
		this.colorFecha = colorCategoria;
	}

	public boolean rojoFecha() {
		return this.tipoListado.equals("Inbox")
				|| this.tipoListado.equals("Dia");
	}

	public boolean rojoCategoria() {
		return this.tipoListado.equals("Semana");
	}

	public BeanUser getUser() {
		return user;
	}

	public List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

	public void setUser(BeanUser user) {
		this.user = user;
	}

	public List<User> getUsers() {
		return (users);
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	// Se inicia correctamente el MBean inyectado si JSF lo hubiera crea
	// y en caso contrario se crea. (hay que tener en cuenta que es un Bean de
	// sesión)
	// Se usa @PostConstruct, ya que en el contructor no se sabe todavía si el
	// Managed Bean
	// ya estaba construido y en @PostConstruct SI.
	@PostConstruct
	public void init() {
		Log.trace("BeanUsers - PostConstruct");
	}

	@PreDestroy
	public void end() {
		Log.trace("BeanUsers - PreDestroy");
	}

	public void iniciaUser(ActionEvent event) {
		user.initUser(event);
	}

	public String listado() {
		AdminService service;
		try {
			// Acceso a la implementacion de la capa de negocio
			// a trav��s de la factor��a
			service = Services.getAdminService();
			// De esta forma le damos informaci��n a toArray para poder hacer el
			// casting a User[]
			// users = (User[]) service.findAllUsers().toArray(new User[0]);
			users = service.findAllUsers();
			return "exito"; // Nos vamos a la vista listado.xhtml

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
			return "error"; // Nos vamos la vista de error
		}

	}

	/*
	 * Listado Inbox
	 */
	public String listadoTareas() {
		TaskService service;
		try {
			// Acceso a la implementacion de la capa de negocio
			// a trav��s de la factor��a
			service = Services.getTaskService();
			// De esta forma le damos informaci��n a toArray para poder hacer el
			// casting a User[]
			FacesContext context = javax.faces.context.FacesContext
					.getCurrentInstance();
			HttpSession session = (HttpSession) context.getExternalContext()
					.getSession(false);
			User user = (User) session.getAttribute("LOGGEDIN_USER");

			List<Task> lista = service.findInboxTasksByUserId(user.getId());
			if (this.inboxFinalizados) {
				lista.addAll(service.findFinishedInboxTasksByUserId(user
						.getId()));
			}

			tasks = lista;// (Task[]) lista.toArray(new Task[0]);

			this.tipoListado = "Inbox";
			Log.trace("Cargado listado inbox para usuario " + user.getLogin());
			return "exito"; // Nos vamos a la vista listado.xhtml

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
			return "error"; // Nos vamos la vista de error
		}

	}

	public void checkListado() {
		try {
			// this.inboxFinalizados=true;
			this.listadoTareas();

		} catch (Exception e) {
			e.printStackTrace();
			// Nos vamos la vista de error
		}

	}

	public String listadoSemana() {
		TaskService service;
		try {
			// Acceso a la implementacion de la capa de negocio
			// a trav��s de la factor��a
			service = Services.getTaskService();
			// De esta forma le damos informaci��n a toArray para poder hacer el
			// casting a User[]
			FacesContext context = javax.faces.context.FacesContext
					.getCurrentInstance();
			HttpSession session = (HttpSession) context.getExternalContext()
					.getSession(false);
			User user = (User) session.getAttribute("LOGGEDIN_USER");

			List<Task> lista = service.findWeekTasksByUserId(user.getId());
			List<Task> lista2 = new ArrayList<>();
			for (Task task : lista) {
				if (task.getCategoryId() != null)
					lista2.add(task);
			}
			tasks = lista2;// (Task[]) lista.toArray(new Task[0]);

			this.tipoListado = "Semana";
			Log.trace("Cargado listado semana para usuario " + user.getLogin());
			return "exito"; // Nos vamos a la vista listado.xhtml

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
			return "error"; // Nos vamos la vista de error
		}

	}

	public String listadoDia() {
		TaskService service;
		try {
			// Acceso a la implementacion de la capa de negocio
			// a trav��s de la factor��a
			service = Services.getTaskService();
			// De esta forma le damos informaci��n a toArray para poder hacer el
			// casting a User[]
			FacesContext context = javax.faces.context.FacesContext
					.getCurrentInstance();
			HttpSession session = (HttpSession) context.getExternalContext()
					.getSession(false);
			User user = (User) session.getAttribute("LOGGEDIN_USER");

			List<Task> lista = service.findTodayTasksByUserId(user.getId());
			List<Task> lista2 = new ArrayList<>();
			for (Task task : lista) {
				if (task.getCategoryId() != null)
					lista2.add(task);
			}
			tasks = lista2;// (Task[]) lista.toArray(new Task[0]);

			this.tipoListado = "Dia";
			Log.trace("Cargado listado dia para usuario " + user.getLogin());
			return "exito"; // Nos vamos a la vista listado.xhtml

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
			return "error"; // Nos vamos la vista de error
		}

	}

	public boolean isInboxFinalizados() {
		return inboxFinalizados;
	}

	public void setInboxFinalizados(boolean inboxFinalizados) {
		this.inboxFinalizados = inboxFinalizados;
	}

	public String baja(User vuser) {
		AdminService service;
		ResourceBundle bundle = BundleFactorie.getMessagesBundle();
		FacesContext cont = FacesContext.getCurrentInstance();
		try {
			// Acceso a la implementacion de la capa de negocio
			// a trav��s de la factor��a
			service = Services.getAdminService();
			// Aliminamos el User seleccionado en la tabla
			service.deepDeleteUser(vuser.getId());
			// Actualizamos el javabean de Users inyectado en la tabla.
			// users = (User[]) service.findAllUsers().toArray(new User[0]);
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					bundle.getString("success"),
					bundle.getString("success_delete_user"));
			cont.addMessage(null, msg);
			users = service.findAllUsers();

			Log.info("Usuario " + vuser.getLogin() + " eliminado");

			return "exito"; // Nos vamos a la vista de listado.

		} catch (Exception e) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					bundle.getString("error"),
					bundle.getString("error_delete_user"));
			cont.addMessage(null, msg);
			Log.error(e.getMessage());
			return "error"; // Nos vamos a la vista de error
		}

	}

	public String deactivate(User vuser) {
		AdminService service;
		ResourceBundle bundle = BundleFactorie.getMessagesBundle();
		service = Services.getAdminService();
		FacesContext cont = FacesContext.getCurrentInstance();
		try {
			service.disableUser(vuser.getId());
			String message = bundle.getString("correct_deactivate");
			message = MessageFormat.format(message, vuser.getLogin());
			cont.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					bundle.getString("success"), message));
			users = service.findAllUsers();
			Log.info("Usuario " + vuser.getLogin() + " desactivado");
			return "exito";
		} catch (BusinessException e) {
			cont.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					bundle.getString("error"), e.getMessage()));
			Log.error(e.getMessage());
			return null;
		}

	}

	public String activate(User vuser) {
		AdminService service;
		ResourceBundle bundle = BundleFactorie.getMessagesBundle();
		service = Services.getAdminService();
		FacesContext cont = FacesContext.getCurrentInstance();
		try {
			service.enableUser(vuser.getId());
			String message = bundle.getString("correct_activate");
			message = MessageFormat.format(message, vuser.getLogin());
			cont.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					bundle.getString("success"), message));
			users = service.findAllUsers();
			Log.info("Usuario " + vuser.getLogin() + " activado");
			return "exito";
		} catch (BusinessException e) {
			cont.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					bundle.getString("error"), e.getMessage()));
			Log.error(e.getMessage());
			return null;
		}

	}

	public String edit() {
		UserService service;
		try {
			// Acceso a la implementacion de la capa de negocio
			// a trav��s de la factor��a
			service = Services.getUserService();
			// Recargamos el User seleccionado en la tabla de la base de datos
			// por si hubiera cambios.
			user = (BeanUser) service.findUser(user.getId());
			return "exito"; // Nos vamos a la vista de Edición.

		} catch (Exception e) {
			e.printStackTrace();
			return "error"; // Nos vamos a la vista de error.
		}

	}

	public String delete(Task vtask) {
		TaskService service;
		try {
			// Acceso a la implementacion de la capa de negocio
			// a trav��s de la factor��a
			service = Services.getTaskService();
			// Eliminamos la Task seleccionado en la tabla
			service.deleteTask(vtask.getId());

			this.listadoTareas();
			Log.info("Tarea " + vtask.getTitle() + " eliminada");
			return "exito"; // Nos vamos a la vista de listado.

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
			return "error"; // Nos vamos a la vista de error
		}
	}

	public String reiniciar() {
		AdminService service;
		ResourceBundle bundle = BundleFactorie.getMessagesBundle();
		FacesContext cont = FacesContext.getCurrentInstance();
		try {

			service = Services.getAdminService();
			service.initDataBase();
			cont.addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, bundle
							.getString("success"), bundle
							.getString("success_restart_base")));
			Log.info("Base de datos reiniciada");
			return "exito"; // Nos vamos a la vista de listado.

		} catch (Exception e) {
			e.printStackTrace();
			cont.addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle
							.getString("error"), bundle
							.getString("error_restart_base")));
			Log.error(e.getMessage());
			return "error"; // Nos vamos a la vista de error
		}
	}

	public String cerrar() {
		return "exito";
	}

	public String login() {
		return "exito";
	}

	public String addTarea() {
		return "exito";
	}

}
