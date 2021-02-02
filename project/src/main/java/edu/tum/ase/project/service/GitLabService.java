package edu.tum.ase.project.service;

import edu.tum.ase.project.model.GitLabUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class GitLabService {

    private static final String GITLAB_API_URL = "https://gitlab.lrz.de/api/v4/";

    @Autowired
    @Qualifier("restTemplate")
    private OAuth2RestOperations restTemplate;


    public GitLabUser findUser(String username){
        GitLabUser[] matchedUsers = restTemplate.getForObject(GITLAB_API_URL + "users?username=" + username, GitLabUser[].class);
        if (matchedUsers == null) {
            throw new RuntimeException("Could not execute the request to search for user '" + username + "'");
        }

        if (matchedUsers.length > 1) {
            throw new RuntimeException("Unexpectedly, several hits have appeared for the same username");
        }

        if (matchedUsers.length < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "GitLab LRZ username '" + username + "' does not exist");
        }

        return matchedUsers[0];
    }
}
