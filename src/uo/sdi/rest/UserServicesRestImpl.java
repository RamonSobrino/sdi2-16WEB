package uo.sdi.rest;

import java.util.List;

import alb.util.date.DateUtil;
import uo.sdi.business.TaskService;
import uo.sdi.business.exception.BusinessException;
import uo.sdi.dto.Category;
import uo.sdi.dto.Task;
import uo.sdi.infrastructure.Factories;

public class UserServicesRestImpl implements UserServicesRest {

	private TaskService taskService = Factories.services.getTaskService();

	@Override
	public List<Category> listarCategorias(Long userId)
			throws BusinessException {
		return taskService.findCategoriesByUserId(userId);
	}

	@Override
	public List<Task> listarTareas(Long catId) throws BusinessException {
		return taskService.findTasksByCategoryId(catId);
	}

	@Override
	public void finalizarTarea(Task tarea) throws BusinessException {
		tarea.setFinished(DateUtil.today());
		taskService.updateTask(tarea);
	}

	@Override
	public void saveTarea(Task tarea) throws BusinessException {
		taskService.createTask(tarea);
	}

}
