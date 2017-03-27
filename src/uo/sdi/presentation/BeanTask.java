package uo.sdi.presentation;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import alb.util.log.Log;
import uo.sdi.business.Services;
import uo.sdi.business.TaskService;
import uo.sdi.dto.Category;
import uo.sdi.dto.Task;
import uo.sdi.dto.User;

@ManagedBean(name = "task")
@SessionScoped
public class BeanTask implements Serializable {
	private static final long serialVersionUID = 6L;
	private Long id;
	private String tittle;
	private String comments;

	private Date planned;
	private Date finished;

	private Long category;

	private Task task;
	@ManagedProperty(value = "#{controller}")
	private BeanUsers bean;

	public Long getCategory() {
		return category;
	}

	public void setCategory(Long category) {
		this.category = category;
	}

	/*
	 * public void setCategory(Long id) { for (Category cat : categorias) {
	 * if(cat.getId() == id){ this.category = cat; break; } } }
	 */

	private List<Category> categorias;

	@PostConstruct
	public void init() {
		this.listaCategory();
		bean = (BeanUsers) FacesContext.getCurrentInstance()
				.getExternalContext().getSessionMap()
				.get(new String("controller"));
	}

	public BeanTask() {
		System.out.println("BeanLogin - No existia");
	}

	public String add() {
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

			this.task = new Task();
			task.setTitle(tittle);
			task.setComments(comments);
			if (category != null)
				task.setCategoryId(category);
			task.setPlanned(planned);
			task.setFinished(finished);
			task.setUserId(user.getId());

			if (service.findTaskById(id) == null)
				service.createTask(task);
			else {
				task.setId(id);
				service.updateTask(task);
			}

			if (category == null) {
				bean.listadoTareas();
			} else if (planned.after(new Date())) {
				bean.listadoSemana();
			} else {
				bean.listadoDia();
			}
			Log.info("Tarea " + task.getTitle() + "creada para el usuario "
					+ user.getLogin());
			this.resetCampos();
			return "exito"; // Nos vamos a la vista listado.xhtml

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
			return "error"; // Nos vamos la vista de error
		}

	}

	public String edit() {
		TaskService service;
		try {
			// Acceso a la implementacion de la capa de negocio
			// a trav��s de la factor��a
			FacesContext context = javax.faces.context.FacesContext
					.getCurrentInstance();
			HttpSession session = (HttpSession) context.getExternalContext()
					.getSession(false);
			User user = (User) session.getAttribute("LOGGEDIN_USER");

			service = Services.getTaskService();
			this.task = new Task();
			task.setTitle(tittle);
			task.setComments(comments);
			if (category != null)
				task.setCategoryId(category);
			task.setPlanned(planned);
			task.setFinished(finished);
			task.setUserId(user.getId());
			task.setId(id);
			service.updateTask(task);

			bean.listadoTareas();
			Log.info("Tarea " + task.getTitle() + "editada para el usuario "
					+ user.getLogin());
			this.resetCampos();
			return "exito"; // Nos vamos a la vista listado.xhtml

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
			return "error"; // Nos vamos la vista de error
		}

	}

	private void resetCampos() {
		this.id = null;
		this.tittle = "";
		this.comments = "";
		this.planned = null;
		this.finished = null;
		this.category = null;
	}

	public String listaCategory() {
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

			this.categorias = service.findCategoriesByUserId(user.getId());

			Log.trace("Cargada lista categorias para usuario "
					+ user.getLogin());
			return "exito"; // Nos vamos a la vista listado.xhtml

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
			return "error"; // Nos vamos la vista de error
		}
	}

	public String getTittle() {
		return tittle;
	}

	public void setTittle(String tittle) {
		this.tittle = tittle;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Date getPlanned() {
		return planned;
	}

	public void setPlanned(Date planned) {
		this.planned = planned;
	}

	public Date getFinished() {
		return finished;
	}

	public void setFinished(Date finished) {
		this.finished = finished;
	}

	public List<Category> getCategorias() {
		return categorias;
	}

	public void setCategorias(List<Category> categorias) {
		this.categorias = categorias;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public String establecer(Task vtask) {
		try {
			this.task = vtask;
			this.tittle = this.task.getTitle();
			this.category = vtask.getCategoryId();
			id = task.getId();
			planned = task.getPlanned();
			finished = task.getFinished();
			return "exito";
		} catch (Exception e) {
			e.printStackTrace();
			return "error"; // Nos vamos la vista de error
		}

	}

}