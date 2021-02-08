package edu.tum.ase.project.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GitLabUser {

    private String id;
    private String username;
    private String name;
    private String state;
    private String avatar_url;
    private String web_url;

}