package edu.tum.ase.project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "project_source_files")
public class SourceFile {
    private static final int SOURCE_CODE_MAX_LENGTH = 100000;

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "source_file_id")
    private String id;


    @Column(name = "file_name")
    @NotBlank
    private String fileName;

    //TODO: check length
    @Size(max = SOURCE_CODE_MAX_LENGTH)
    @Column(name = "source_code", length = SOURCE_CODE_MAX_LENGTH)
    private String sourceCode;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    @JsonIgnore
    // This field is not necessary in the response body, because the project id is already part of the URL (which can be used to query all the project information)
    private Project project;
}
