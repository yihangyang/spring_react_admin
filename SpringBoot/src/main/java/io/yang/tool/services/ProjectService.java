package io.yang.tool.services;

import io.yang.tool.domain.Project;
import io.yang.tool.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    public Project saveOrUpdateProject(Project project) {
        // logic
        return projectRepository.save(project);
    }
}
