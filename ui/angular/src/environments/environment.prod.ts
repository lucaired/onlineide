// By using requests like /api/projects instead of full URLs with localhost, we send the requests directly to the gateway.
// Since the gateway also serves this frontend, the browser uses the host and appends the part like /api/projects.
// The gateway then processes the incoming request and forwards it internally to the project microservice.
// So this frontend here does not communicate directly with the microservices, but always with the gateway.
export const environment = {
  production: true,
  api: {
    user: '/user',
    logout: '/logout',
    projects: '/api/projects',
    compiler: '/api/compile',
    darkMode: '/api/dark-mode'
  }
};
