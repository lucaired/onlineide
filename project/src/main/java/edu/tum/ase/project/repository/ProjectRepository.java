package edu.tum.ase.project.repository;

import edu.tum.ase.project.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Note that Spring provides a variety of Repository abstractions, JpaRepository is
 * technology-specific
 * see https://docs.spring.io/spring-data/jdbc/docs/1.0.11.RELEASE/reference/html/#
 * repositories.core-concepts
 */
@Repository
public interface ProjectRepository extends JpaRepository<Project, String> {
    Project findByName(String name);
}
