package edu.tum.ase.project.controller;

import edu.tum.ase.errorhandling.exception.ResourceAlreadyExistsException;
import edu.tum.ase.errorhandling.exception.ResourceNotFoundException;
import edu.tum.ase.project.model.Project;
import edu.tum.ase.project.model.SourceFile;
import edu.tum.ase.project.service.ProjectService;
import edu.tum.ase.project.service.SourceFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(path = "/api/projects/{projectId}/sourcefiles")
public class SourceFileController {

    @Autowired
    private SourceFileService sourceFileService;

    @Autowired
    private ProjectService projectService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<SourceFile> createSourceFile(
            @Valid @RequestBody SourceFile sourceFile,
            @PathVariable(name = "projectId") String projectId) {

        Project p = getProject(projectId);
        sourceFile.setProject(p);

        if (p.getSourceFiles().stream().anyMatch(sf -> sf.getFileName().equals(sourceFile.getFileName()))) {
            throw new ResourceAlreadyExistsException(SourceFile.class, "name", sourceFile.getFileName());
        }

        // No field of a Sourcefile is unique, therefore, we can always just create it.
        SourceFile s = sourceFileService.createSourceFile(sourceFile);

        URI location;
        try {
            location = new URI("/projects/" + s.getId());
        } catch (URISyntaxException e) {
            throw new RuntimeException("The URI for the created project with id '" + s.getId() + "' could not be assembled");
        }
        return ResponseEntity.created(location).body(s);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public SourceFile readSourceFile(
            @PathVariable(name = "projectId") String projectId,
            @PathVariable(name = "id") String id) {
        getProject(projectId); // Ensure existence of the project from the URL
        return sourceFileService.findById(id).orElseThrow(() -> new ResourceNotFoundException(SourceFile.class, id));
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<SourceFile> readAllSourceFiles(@PathVariable(name = "projectId") String projectId) {
        getProject(projectId); // Ensure existence of the project from the URL
        return sourceFileService.findSourceFilesByProject(projectId);
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/{id}")
    public SourceFile replaceSourceFile(@Valid @RequestBody SourceFile newSourceFile,
                                        @PathVariable(name = "projectId") String projectId,
                                        @PathVariable(name = "id") String id) {
        // Actually a PUT request could also create the resource if it does not exist yet.
        // As we use auto-generated identifiers, however, we cannot simply use the id passed by the client in the URI.
        if (sourceFileService.findById(id).isEmpty()) {
            throw new ResourceNotFoundException(SourceFile.class, id);
        }

        Project p = getProject(projectId);
        newSourceFile.setProject(p);
        newSourceFile.setId(id);

        return sourceFileService.updateSourceFile(newSourceFile);
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
    public void deleteSourceFile(
            @PathVariable(name = "projectId") String projectId,
            @PathVariable(name = "id") String id) {
        getProject(projectId); // Ensure existence of the project from the URL
        if (sourceFileService.findById(id).isEmpty())
            throw new ResourceNotFoundException(SourceFile.class, id);
        sourceFileService.deleteById(id);
    }

    private Project getProject(String projectId) {
        return projectService.findById(projectId).orElseThrow(() -> new ResourceNotFoundException(Project.class, projectId));
    }
}
