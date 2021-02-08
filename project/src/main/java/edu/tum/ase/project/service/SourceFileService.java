package edu.tum.ase.project.service;

import edu.tum.ase.project.model.SourceFile;
import edu.tum.ase.project.repository.SourceFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SourceFileService {
    @Autowired
    private SourceFileRepository sourceFileRepository;

    @Autowired
    private SecurityService securityService;

    public SourceFile createSourceFile(SourceFile sourceFile) {
        return sourceFileRepository.save(sourceFile);
    }

    @PostAuthorize("returnObject.isEmpty() || @securityService.isAuthorizedForProject(returnObject.get().project)")
    public Optional<SourceFile> findById(String id) {
        return sourceFileRepository.findById(id);
    }

    @PreAuthorize("@securityService.isAuthorizedForProject(#projectId)")
    public List<SourceFile> findSourceFilesByProject(String projectId) {
        return sourceFileRepository.findByProjectId(projectId);
    }

    @PreAuthorize("@securityService.isAuthorizedForProject(#newSourceFile.project)")
    public SourceFile updateSourceFile(SourceFile newSourceFile) {
        return sourceFileRepository.save(newSourceFile);
    }

    @PreAuthorize("@securityService.isAuthorizedForProject(#id)")
    public void deleteById(String id) {
        sourceFileRepository.deleteById(id);
    }
}
