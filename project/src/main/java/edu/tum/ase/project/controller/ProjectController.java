package edu.tum.ase.project.controller;

import edu.tum.ase.project.error.ResourceAlreadyExistsException;
import edu.tum.ase.project.error.ResourceNotFoundException;
import edu.tum.ase.project.model.GitLabUser;
import edu.tum.ase.project.model.Project;
import edu.tum.ase.project.model.ProjectMember;
import edu.tum.ase.project.service.AuthService;
import edu.tum.ase.project.service.GitLabService;
import edu.tum.ase.project.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin
@RequestMapping(path = "/api/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private AuthService authService;

    @Autowired
    private GitLabService gitLabService;


    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Project> createProject(@Valid @RequestBody Project project) {
        if (projectService.findByName(project.getName()).isPresent())
            throw new ResourceAlreadyExistsException(Project.class, "name", project.getName());

        project.getMemberIds().add(authService.getCurrentUsername());
        Project p = projectService.createProject(project);

        URI location;
        try {
            location = new URI("/projects/" + p.getId());
        } catch (URISyntaxException e) {
            throw new RuntimeException("The URI for the created project with id '" + project.getId() + "' could not be assembled");
        }

        p.add(buildSourceFileLink(p));

        // Return HTTP status 201 (Created) and a header containing the location of the created resource
        return ResponseEntity.created(location).body(p);
    }

    @RequestMapping(method = RequestMethod.POST, path = {"/{id}/members"})
    public Project addMember(@PathVariable(name = "id") String projectId, @RequestBody ProjectMember member) {
        // The GitLab API is not used to implement the actual sharing of a project because the projects created in the OnlineIDE are not GitLab projects.
        // Instead, we only use the GitLab API to check if an intended new member even exists.
        GitLabUser user = gitLabService.findUser(member.getUsername());
        Project project = projectService.addMember(projectId, user.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException(Project.class, projectId));
        return project.add(buildSourceFileLink(project));
    }

    @RequestMapping(method = RequestMethod.DELETE, path = {"/{id}/members/{username}"})
    public Project removeMember(@PathVariable(name = "id") String projectId, @PathVariable(name = "username") String username) {
        Project p = projectService.removeMember(projectId, username).orElseThrow(() -> new ResourceNotFoundException(Project.class, projectId));
        return p.add(buildSourceFileLink(p));
    }

    @RequestMapping(method = RequestMethod.GET, path = {"/{id}"})
    public Project readProject(@PathVariable(name = "id") String id) {
        Project p = projectService.findById(id).orElseThrow(() -> new ResourceNotFoundException(Project.class, id));
        return p.add(buildSourceFileLink(p));
    }

    @RequestMapping(method = RequestMethod.GET)
    public CollectionModel<Project> readAllProjects() {
        // Since this is a list of resources, we need to use CollectionModel<> to apply Spring HATEOAS.
        // Simply returning List<Project> would create a response format that is different from the other endpoints.
        List<Project> projects = projectService.getAllProjects().stream().map(p -> p.add(buildSourceFileLink(p))).collect(Collectors.toList());
        return CollectionModel.of(projects);
    }

    @RequestMapping(method = RequestMethod.PUT, path = {"/{id}"})
    public Project replaceProject(@RequestBody Project newProject, @PathVariable(name = "id") String id) {

        // Actually a PUT request could also create the resource if it does not exist yet.
        // As we use auto-generated identifiers, however, we cannot simply use the id passed by the client in the URI.
        if (projectService.findById(id).isEmpty()) {
            throw new ResourceNotFoundException(Project.class, id);
        }

        // If the Java object has the same id, Hibernate will recognize that an update and no creation should take place
        newProject.setId(id);

        Project p = projectService.updateProject(newProject);
        return p.add(buildSourceFileLink(p));
    }

    // TODO: Add PATCH endpoint if needed

    @RequestMapping(method = RequestMethod.DELETE, path = {"/{id}"})
    public void deleteProject(@PathVariable(name = "id") String id) {
        if (projectService.findById(id).isEmpty())
            throw new ResourceNotFoundException(Project.class, id);
        projectService.deleteById(id);
    }

    private Link buildSourceFileLink(Project p) {
        return linkTo(methodOn(SourceFileController.class).readAllSourceFiles(p.getId())).withRel("sourcefiles");
    }

}
