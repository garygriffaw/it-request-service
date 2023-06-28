# IT Request Service

This repository contains an example application I built as an API for an IT Request system.  It uses Spring Boot 3 / Spring Framework 6.  Tests are included.  

I plan on adding more features.

## Features
* Register new users
* Authenticate existing users
* Logout users
* Roles are used to control the ability to perform certain actions.
 
### Any user has the following capabilities regardless of Role
* Create a request
* View the list of requests they created
* View a request that they created
* Update certain fields on a request they created

### Users with the Admin Role have the following capabilities
* View the list of all requests regardless of who created it
* View a request regardless of who created it
* Update a request regardless of who created it
* Update all of the fields on the request
* Delete a request regardless of who created it