package edu.tum.ase.apigateway;

import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@EnableOAuth2Sso
public class AuthController {

    @RequestMapping(path = "/authenticated", method = RequestMethod.GET)
    public boolean authenticated() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication != null) {
            return authentication.getAuthorities().stream()
                    .noneMatch(grantedAuthority ->
                            grantedAuthority.getAuthority().equals("ROLE_ANONYMOUS"));
        }
        return false;
    }

    @RequestMapping(path = "/user", method = RequestMethod.GET)
    public Principal user() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
