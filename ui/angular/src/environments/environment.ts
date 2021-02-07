// Unlike the production environment, for development purposes we use the addresses of the actual microservices without service discovery and gateway.
// This makes local development a bit more lightweight because not everything has to be set up for Eureka and the Zuul Gateway.
// However, for the final local testing of a feature, the production environment should then be used.
export const environment = {
  production: false,
  api: {
    user: 'http://localhost/user',
    authenticated: 'http://localhost/authenticated',
    login: 'http://localhost/login',
    logout: 'http://localhost/logout',
    projects: 'http://localhost:80/api/projects',
    compiler: 'http://localhost/api/compile',
    darkMode: 'http://localhost/api/dark-mode'
  }
};
