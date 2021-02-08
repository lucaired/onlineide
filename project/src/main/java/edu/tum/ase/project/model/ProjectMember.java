package edu.tum.ase.project.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectMember {

    /**
     * The username is not necessarily the TUM identifier (like ga32xyz), but the LRZ GitLab username, which can be equal to the TUM identifier)
     */
    private String username;

}
