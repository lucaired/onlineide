package edu.tum.ase.project.controller;

import edu.tum.ase.project.error.ResourceAlreadyExistsException;
import edu.tum.ase.project.error.ResourceNotFoundException;
import edu.tum.ase.project.model.Project;
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


    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Project> createProject(@Valid @RequestBody Project project) {
        if (projectService.findByName(project.getName()).isPresent())
            throw new ResourceAlreadyExistsException(Project.class, "name", project.getName());

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

        if (projectService.findByName(newProject.getName()).isPresent()) {
            throw new ResourceAlreadyExistsException(Project.class, "name", newProject.getName());
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
