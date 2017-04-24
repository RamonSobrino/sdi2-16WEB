package uo.sdi.rest;

import java.util.List;

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
	public List<Category> listarCategorias(User user) throws BusinessException {
		return 	taskService.findCategoriesByUserId(user.getId());
	}

	@Override
	public List<Task> listarTareas(Category cat) throws BusinessException {
		
		return taskService.findTasksByCategoryId(cat.getId());
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
