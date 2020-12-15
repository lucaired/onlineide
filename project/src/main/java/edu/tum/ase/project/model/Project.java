package edu.tum.ase.project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import javax.transaction.Transactional;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "projects")
public class Project extends RepresentationModel<Project> {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "project_id")
    private String id;

    @Column(name = "name", nullable = false, unique = true)
    @NotBlank()
    private String name;

    @OneToMany(mappedBy = "project")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private List<SourceFile> sourceFiles = new ArrayList<>();
}