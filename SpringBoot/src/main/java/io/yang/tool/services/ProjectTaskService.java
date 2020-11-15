package io.yang.tool.services;

import io.yang.tool.domain.Backlog;
import io.yang.tool.domain.Project;
import io.yang.tool.domain.ProjectTask;
import io.yang.tool.exceptions.ProjectIdException;
import io.yang.tool.exceptions.ProjectNotFoundException;
import io.yang.tool.repositories.BacklogRepository;
import io.yang.tool.repositories.ProjectRepository;
import io.yang.tool.repositories.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectTaskService {
    @Autowired
    private BacklogRepository backlogRepository;
    @Autowired
    private ProjectTaskRepository projectTaskRepository;
    @Autowired
    private ProjectRepository projectRepository;

    // add project_task based in project
    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask) {

        try{
            // projectTasks to be added to a specific project, project exists, backlog exists
            Backlog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier.toUpperCase());
            // set the backlog to projectTask
            projectTask.setBacklog(backlog);
            // project sequence to be like this:
            Integer backlogSequence = backlog.getPTSequence();
            // update the backlog sequence
            backlogSequence ++ ;
            backlog.setPTSequence(backlogSequence);
            // add sequence to project task (eg:ADDPR-3)
            projectTask.setProjectSequence(projectIdentifier.toUpperCase() + "-" + backlogSequence);
            projectTask.setProjectIdentifier(projectIdentifier.toUpperCase());
            // initial priority to 3 when priority is null
            if(projectTask.getPriority() == null || projectTask.getPriority() == 0) {
                projectTask.setPriority(3);
            }
            // initial status when status is null
            if(projectTask.getStatus() == null || projectTask.getStatus() == "") {
                projectTask.setStatus("TODO");
            }

            return projectTaskRepository.save(projectTask);
        } catch (Exception e) {
//            // Exception: Project not found => {ProjectNotFound:"Project not be found"}
//            if (backlog == null) {
//                throw new ProjectIdException("Project ID '" + projectIdentifier.toUpperCase() + "' doesn't exists");
//            }
            throw new ProjectNotFoundException("Project not found");
        }
    }

    public Iterable<ProjectTask> findBacklogById(String backlog_id) {
        backlog_id = backlog_id.toUpperCase();
        Project project = projectRepository.findByProjectIdentifier(backlog_id);
        if(project==null) {
            throw new ProjectNotFoundException("Project with ID '" + backlog_id + "' not exists");
        }
        return projectTaskRepository.findByProjectIdentifierOrderByPriority(backlog_id);
    }

    public ProjectTask findProjectTaskByProjectSequence(String backlog_id, String project_task_id) {
        project_task_id = project_task_id.toUpperCase();
        // check backlog exists
        Backlog backlog = backlogRepository.findByProjectIdentifier(backlog_id.toUpperCase());
        if (backlog == null) {
            throw new ProjectNotFoundException("Project with ID '" + backlog_id.toUpperCase() + "' not exists");
        }
        // check task exists
        ProjectTask projectTask = projectTaskRepository.findByProjectSequence(project_task_id);
        if(projectTask == null) {
            throw new ProjectNotFoundException("Project Task with ID '" + project_task_id + "' not exists");
        }
        // check task belongs to this project
        if(!projectTask.getProjectIdentifier().equals(backlog_id.toUpperCase())) {
            throw new ProjectNotFoundException("Project Task with ID '" + project_task_id + "' not exists");
        }
        return projectTask;
    }

    public ProjectTask updateByProjectSequence(ProjectTask updatedTask, String backlog_id, String project_task_id) {
        ProjectTask projectTask = findProjectTaskByProjectSequence(backlog_id, project_task_id);
        projectTask = updatedTask;

        return projectTaskRepository.save(projectTask);
    }

    public void deleteProjectTaskByProjectSequence(String backlog_id, String project_task_id) {
        ProjectTask projectTask = findProjectTaskByProjectSequence(backlog_id, project_task_id);

//        Backlog backlog = projectTask.getBacklog();
//        List<ProjectTask> projectTasks = backlog.getProjectTasks();
//        projectTasks.remove(projectTask);
//        backlogRepository.save(backlog);

        projectTaskRepository.delete(projectTask);
    }
}
