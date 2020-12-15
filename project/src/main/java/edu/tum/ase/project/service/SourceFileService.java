package edu.tum.ase.project.service;

import edu.tum.ase.project.model.SourceFile;
import edu.tum.ase.project.repository.SourceFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SourceFileService {
    @Autowired
    private SourceFileRepository sourceFileRepository;

    public SourceFile createSourceFile(SourceFile sourceFile){
        return sourceFileRepository.save(sourceFile);
    }

    public Optional<SourceFile> findById(String id){
        return sourceFileRepository.findById(id);
    }

    public List<SourceFile> findAllSourceFiles(){
        return sourceFileRepository.findAll();
    }

    public List<SourceFile> findSourceFilesByProject(String projectId) {
        return sourceFileRepository.findByProjectId(projectId);
    }

    public SourceFile updateSourceFile(SourceFile newSourceFile) {
        return sourceFileRepository.save(newSourceFile);
    }

    public void deleteById(String id) {
        sourceFileRepository.deleteById(id);
    }
}
