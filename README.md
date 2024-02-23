# Spring Keycloak Auth

This quickstart demonstrates how to protect a Spring Boot REST service using Keycloak Authorization Services.

It tries to focus on the authorization features provided by Keycloak Authorization Services, where resources are
protected by a set of permissions and policies defined in Keycloak and access to these resources are enforced by a
policy enforcer(PEP) that intercepts every single request sent to the application to check whether or not access should
be granted.

## How to run

1. Copy `.env` file from `.env.example` and update the values

    ```bash
     cp .env.example .env
    ```

2. Update the `.env` file

3. Run `docker-compose.yml` file to start Keycloak server and Postgres databse

      ```bash
       docker-compose up
      ```

4. Run the spring boot application

5.Import `insomnia.json` file to open the requests in Insomnia

## Urls

- Keycloak: http://localhost:8081
- Spring Boot: http://localhost:8080
    - http://localhost:8080/ - can be invoked by any authenticated user
    - http://localhost:8080/admin - can be invoked by users with the `user_premium` role

## Docs

- https://github.com/keycloak/keycloak-quickstarts/tree/latest/spring/rest-authz-resource-server
- https://www.keycloak.org/docs/latest/authorization_services/index.html#_resource_server_create_client
- https://www.keycloak.org/docs/latest/authorization_services/index.html#_resource_server_default_config

### Repos

- https://github.com/keycloak/keycloak-quickstarts/tree/latest/spring/rest-authz-resource-server

## References

- [Spring OAuth 2.0 Resource Server JWT](https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/jwt.html)
- [Keycloak Authorization Services](https://www.keycloak.org/docs/latest/authorization_services/)
- [Keycloak Documentation](https://www.keycloak.org/documentation)
- [Keycloak Quickstarts Spring](https://github.com/keycloak/keycloak-quickstarts/blob/latest/spring/rest-authz-resource-server/README.md)
