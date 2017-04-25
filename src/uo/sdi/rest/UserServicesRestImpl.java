package uo.sdi.rest;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import alb.util.date.DateUtil;
import uo.sdi.business.TaskService;
import uo.sdi.business.exception.BusinessException;
import uo.sdi.dto.Category;
import uo.sdi.dto.Task;
import uo.sdi.dto.User;
import uo.sdi.infrastructure.Factories;

public class UserServicesRestImpl implements UserServicesRest {

	private TaskService taskService = Factories.services.getTaskService();

	@Override
	public List<Category> listarCategorias(HttpServletRequest req)
			throws BusinessException {
		User u = (User) req.getSession(true).getAttribute("user");
		return taskService.findCategoriesByUserId(u.getId());
	}

	@Override
	public List<Task> listarTareas(Long catId) throws BusinessException {
		return taskService.findTasksByCategoryId(catId);
	}

	@Override
	public void finalizarTarea(Task tarea) throws BusinessException {
		Task task = taskService.findTaskById(tarea.getId());
		task.setFinished(DateUtil.today());
		taskService.updateTask(task);
	}

	@Override
	public void saveTarea(Task tarea, HttpServletRequest req) throws BusinessException {
		User u = (User) req.getSession(true).getAttribute("user");
		tarea.setUserId(u.getId());
		taskService.createTask(tarea);
	}

}
