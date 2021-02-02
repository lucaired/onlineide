package edu.tum.ase.project;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;

@Configuration
public class OAuthConfig {

    @Primary
    @Bean
    public OAuth2RestOperations restTemplate(@Qualifier("oauth2ClientContext") OAuth2ClientContext context) {
        ClientCredentialsResourceDetails details = new ClientCredentialsResourceDetails();
        return new OAuth2RestTemplate(details, context);
    }

}
