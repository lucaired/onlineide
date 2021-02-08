package edu.tum.ase.project.service;

import edu.tum.ase.project.model.Project;
import edu.tum.ase.project.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private SecurityService securityService;

    public Project createProject(Project project) {
        return projectRepository.save(project);
    }

    @PreAuthorize("@securityService.isAuthorizedForProject(#project)")
    public Project updateProject(Project project) {
        return projectRepository.save(project);
    }

    @PreAuthorize("@securityService.isAuthorizedForProject(#projectId)")
    public Optional<Project> addMember(String projectId, String username) {
        Optional<Project> p = this.findById(projectId);
        if (p.isPresent()) {
            Project project = p.get();
            project.getMemberIds().add(username);
            Project updated = projectRepository.save(project);
            return Optional.of(updated);
        }
        return Optional.empty();
    }

    @PreAuthorize("@securityService.isAuthorizedForProject(#projectId)")
    public Optional<Project> removeMember(String projectId, String username) {
        Optional<Project> p = this.findById(projectId);
        if (p.isPresent()) {
            Project project = p.get();
            project.getMemberIds().remove(username);
            Project updated = projectRepository.save(project);
            return Optional.of(updated);
        }
        return Optional.empty();
    }

    @PostAuthorize("@securityService.isAuthorizedForProject(returnObject)")
    public Optional<Project> findByName(String name) {
        return projectRepository.findByName(name);
    }

    @PostAuthorize("@securityService.isAuthorizedForProject(returnObject)")
    public Optional<Project> findById(String id) {
        return projectRepository.findById(id);
    }

    @PostFilter("@securityService.isAuthorizedForProject(filterObject)")
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    @PreAuthorize("@securityService.isAuthorizedForProject(#id)")
    public void deleteById(String id) {
        projectRepository.deleteById(id);
    }
}