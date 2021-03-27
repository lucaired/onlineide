# Online IDE

This is the repository for the project "OnlineIDE" in the course "Advanced Topics of Software Engineering". It was implemented by:
- [Adrian Mitter](https://www.linkedin.com/in/adrian-mitter-052157195/)
- [Luca A. Müller](https://github.com/lucaired)
- [Marc Bede]()
- [Matthias Unterfrauner](https://www.linkedin.com/in/matthiasunt/)
- [Wenliang Peng]()

## Deployment

The application can be easily deployed once access to the GitLab repository is granted.

- Copy the docker-compose.yml file from the project root and place it in a desired location.
- Use the GitLab deployment credentials to grant docker access to the container-registry. By running the following command:
```shell
docker login -u <CI_DEPLOY_USERNAME> -p <CI_DEPLOY_TOKEN> gitlab.lrz.de:5005
```
- By providing the OAUTH-credentials, the application can be started simply by calling docker-compose up:
```shell
OAUTH_CLIENT_ID=<OAUTH_CLIENT_ID> OAUTH_CLIENT_SECRET=<OAUTH_CLIENT_SECRET> docker-compose up
```

These steps are automated for deployments to our live environment (Google VM) using our continuous deployment strategy.

### Scaling

In order to launch multiple instances of a microservice, the `--scale SERVICE=NUM` option of `docker-compose up` can be used. 
Currently, the maximum number of instances per microservice is limited to 5, as this corresponds to the range of host port mappings in `docker-compose.yml`.

## Run locally

To run the online IDE locally, you can basically follow the steps of the deployment section above. However, with the following differences:
- Use different credentials to log in to the LRZ Container Registry if necessary.
- Make sure to set the environment variables `$OAUTH_CLIENT_ID` and `$OAUTH_CLIENT_SECRET` or specify the concrete values directly in the `docker-compose up` command described above.
- Make sure that you use the OAuth credentials for the local application, not the ones for the production version.


## Postman collection

The REST API can be explored with the following Postman Collection, which can be imported into the local Postman instance by one click.
It provides all relevant requests, if possible with matching sample data. 
Authenticated requests can therefore be easily executed as follows:
1. Select the environment "Production" in the upper right corner. This will set the correct URL and the necessary OAuth credentials.
2. Right click on our collection "OnlineIDE (5-1)" on the left, then click "Edit". The "Authorization" tab will already have all the necessary data entered. 
You just have to scroll down, click on "Get New Access Token" and log in to the LRZ. In the afterwards appearing modal, click on "Use Token" (top right). 
Now the OAuth Access Token will be passed automatically with every request. 
3. In order for the access to work outside the browser, we have to pass the XSRF token for non-GET requests. This is what you get back from the server with each request. Click on the request "[DUMMY] Obtain XSRF token", so that its custom Postman script takes the XSRF token as a variable and adds it to all future requests as a header.
4. Execute arbitrary requests without necessary changes in "Authorization" or "Headers"

[![Run in Postman](https://run.pstmn.io/button.svg)](https://app.getpostman.com/run-collection/dc0a5b4742177f152106#?env%5BProduction%5D=W3sia2V5IjoiYmFzZVVybEFQSSIsInZhbHVlIjoiaHR0cDovLzM1LjI0Mi4yNDMuODIvYXBpIiwiZW5hYmxlZCI6dHJ1ZX0seyJrZXkiOiJiYXNlVXJsR2F0ZXdheSIsInZhbHVlIjoiaHR0cDovLzM1LjI0Mi4yNDMuODIiLCJlbmFibGVkIjp0cnVlfSx7ImtleSI6Ik9BdXRoQ2FsbGJhY2tVUkwiLCJ2YWx1ZSI6Imh0dHA6Ly8zNS4yNDIuMjQzLjgyL2xvZ2luIiwiZW5hYmxlZCI6dHJ1ZX0seyJrZXkiOiJPQXV0aENsaWVudElkIiwidmFsdWUiOiI5ZGE2Y2Y5NTViZDBjYzMwZWQzODAwNmNhOWQxYzM5NTI3MzM2ZWQzMjgxMWY5NzFiYjczM2UxZGU3YWI2ZTJkIiwiZW5hYmxlZCI6dHJ1ZX0seyJrZXkiOiJPQXV0aENsaWVudFNlY3JldCIsInZhbHVlIjoiZTRiOTgwMWE0NmRhODc5ODQyODg1ZTMzNmUyN2I1YWUwYmFiN2YwNGVmMzk2ZTYwOGMzMDhlOWNjY2QyOWJlNiIsImVuYWJsZWQiOnRydWV9LHsia2V5IjoieHNyZlRva2VuIiwidmFsdWUiOiIiLCJlbmFibGVkIjp0cnVlfV0=)
