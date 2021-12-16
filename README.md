# Spring Security Demo

This demo service using spring security for login/logout/userInfo using jwt token.

# Flow

1. Authenticate api is used to get the JWT Token and Store into in memory cache i.e redis
   (Just for sake of running standalone application, in memory redis and h2 is used)
2. Using Token that we got in first step, we can hit UserInfo api
3. If user wants to signout and token is till valid, then signout api can be called, This will delete token from redis cache


## Build

1. Min. java 8
2. Maven 3.5.x
3. Run mvn clean install


## Run

1. Import application in your IDE
2. Run "AuthenticationServiceApplication"

## Usage

Just start the application with the Spring Boot maven plugin (mvn spring-boot:run). The application is running at http://localhost:8080.

You can use the H2-Console for exploring the database under http://localhost:8080/h2-console

## Roles, Users
User and roles can be found in data.sql file in resources folder
Admin - admin@mock.com:pass
Agent - employee@mock.com:pass
Vendor - vendor@mock.com:pass

## API (Sample request can be found inside documentation)
### http://localhost:8080/authenticate
 This api is used for getting JWT Token.
 Http Method - POST 
 Request
```yaml
{
    "username": "admin@mock.com",
    "password": "pass"
}
```
 Response
```yaml
{
  "jwtToken": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbkBtb2NrLmNvbSIsInJvbGVzIjpbImFkbWluIl0sImlhdCI6MTYzOTY1MjM2MCwiZXhwIjoxNjM5NjUyNDIwfQ.7UXsC4XlP75lxxLLbYqb8dmLaBri3vuZxegSiGrng71v3Ctlgn3OJbtLGH1MkoMiAfAJvaXhdIdE3aZchHdpXQ"
}
```
### http://localhost:8080/getUserInfo
  This api is used to get User information using jwt token as this us secured api and only admin,agent role can see userinfo
  HTTP Method  - GET 
Request
Pass Token as HTTP Header with Key Authorization and value will be Bearer + token from authenticate api

Response
```yaml
{
  "id": 1,
  "firstName": "Mock-Admin",
  "lastName": "Mock-Last",
  "age": 36
}
```
### http://localhost:8080/signout
This api is used to delete token from in memory redis cache.

HTTP Method - DELETE

Request
Pass Token as HTTP Header with Key Authorization and value will be Bearer + token from authenticate api

Response
```yaml
{
  "status": true
}
```

## Screenshots of running app

### get Token admin role
![Screenshot from running application](documentation/authentication.png?raw=true "Screenshot JWT Spring Security Demo")

### get User Info
![Screenshot from running application](documentation/getUserInfo.png?raw=true "Screenshot JWT Spring Security Demo")

### Log out User
![Screenshot from running application](documentation/logout.png?raw=true "Screenshot JWT Spring Security Demo")

### get Token vendor role
![Screenshot from running application](documentation/vendorAuthenticate.png?raw=true "Screenshot JWT Spring Security Demo")

### get UserInfo(This should fail)
![Screenshot from running application](documentation/getUserInfoFailed.png?raw=true "Screenshot JWT Spring Security Demo")