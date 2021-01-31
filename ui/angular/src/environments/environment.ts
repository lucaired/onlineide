// Unlike the production environment, for development purposes we use the addresses of the actual microservices without service discovery and gateway.
// This makes local development a bit more lightweight because not everything has to be set up for Eureka and the Zuul Gateway.
// However, for the final local testing of a feature, the production environment should then be used.
export const environment = {
  production: false,
  api: {
    user: 'http://localhost/user',
    projects: 'http://localhost:8100/api/projects',
    compiler: 'http://localhost:8200/api/compile',
    darkMode: 'http://localhost:8300/api/dark-mode'
  }
};
