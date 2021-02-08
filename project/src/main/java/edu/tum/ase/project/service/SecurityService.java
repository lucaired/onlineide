package edu.tum.ase.project.service;

import edu.tum.ase.project.model.Project;
import edu.tum.ase.project.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SecurityService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private AuthService authService;

    /**
     * Shorthand method to check if the current user is authorized for the given project
     *
     * @return Whether the given user is authorized for this project
     */
    public boolean isAuthorizedForProject(String projectId) {
        Optional<Project> p = projectRepository.findById(projectId);
        return isAuthorizedForProject(p);
    }

    /**
     * Shorthand method to check if the current user is authorized for the given project
     *
     * @return Whether the given user is authorized for this project
     */
    public boolean isAuthorizedForProject(Optional<Project> project) {
        if (project.isEmpty()) {
            return true;
        }
        return isAuthorizedForProject(project.get());
    }

    /**
     * Shorthand method to check if the current user is authorized for the given project
     *
     * @return Whether the given user is authorized for this project
     */
    public boolean isAuthorizedForProject(Project project) {
        return project.getMemberIds().contains(authService.getCurrentUsername());
    }

}
