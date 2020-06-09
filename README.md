# Design

# Initial Setup Choices
1. Spring Boot DevTools - DEVELOPER TOOLS - Provides fast application restarts, LiveReload, and configurations for enhanced development experience.
1. Lombok - DEVELOPER TOOLS - Java annotation library which helps to reduce boilerplate code.
1. Spring Configuration Processor - DEVELOPER TOOLS - Generate metadata for developers to offer contextual help and "code completion" when working with custom configuration keys (ex.application.properties/.yml files).
1. Spring Web - WEB - Build web, including RESTful, applications using Spring MVC. Uses Apache Tomcat as the default embedded container.
1. Spring Data JPA - SQL - Persist data in SQL stores with Java Persistence API using Spring Data and Hibernate.
1. H2 Database - SQL - Provides a fast in-memory database that supports JDBC API and R2DBC access, with a small (2mb) footprint. Supports embedded and server modes as well as a browser based console application.

# Implicit Design Steps
* Separation of Concern - the requirements will change.  You need to make the application fault tolerant and loosely coupled.  Separation of Concern means you do not push the database table Object out to the presentation layer.  Any changes to the database cause ripples in the service and presentation code.  
    * Database Layer - this is the database model and the repositories that access the database.  The JPARepository interface handle a lot of the DAO code. The layer uses model and repository packages. 
    * Service Layer - knows about database layer and converts those objects to generic digests.  Handles the business logic.  The layer uses the domain, model, repository and service packages.
    * Presentation Layer - knows about the Service layer, has no idea the database layer exists. In this case the Presentation layer handles the API interaction with the outside world.  Validates requests and authorization.  Also handles version changes. The layer uses the rest, service and domain packages.
    
* Security - use HTTPS.  Security is broken down into two parts - AuthN (Authentication) and AuthZ (Authorization).  Up to your organization if using OAuth 2.0, or Single Signon with SAML, or JSON web tokens, or api tokens or ...
    * Provide the least amount of authorization possible
    * Default to deny instead of approve
    
* Never expose information on URLs
* Provide the minimum amount of feedback - E*Trade had different login error messages for invalid user or invalid password.  Bad actors used that information to find a list of valid user ids.  After that they worked on trying to hack the passwords.  A simple "invalid user id or password" message stopped that attach vector.

* Provide the version number as part of the URI.  That allows clients interact the same version instead of a requirement change breaking their code.  Another option is GraphQL.  We used that at Ibotta to handle version changes.  The client provides which fields it wants to see.  Changes to the underlying object are ignored.

* Documentation - I prefer Swagger.  The documentation is generated from the code.  We ran into issues at E*Trade where the program managers were supposed to update the API wiki every release.  Documentation drift caused some angry/confused clients.

* Testing - Unit test each piece.  Unit tests provide validation for refactoring code.  Integration and Unit tests validate the acceptance criteria.  

# REST Design Steps
1. Understand the Object Model - this will change as requirements evolve.  
    * The Contact Object has a name and a phone number.  The name will change to two fields: first and last name.
    * The next change is to add an email address
    * Then add multiple phone numbers
    * Then add physical address
    
1. Create Model URIs.  A contact has CRUD operations:
    * Create - /contact
    * Read -   /contact/**{id}**
    * Update - /contact/**{id}**
    * Update phone - /contact/**{id}**/phone
    * Delete - /contact/**{id}**
1. Another part of the Model URI are the collection operations (these only involve GET or Read operations.  Bulk update is a bad idea):
    * Retrieve all - this needs optional parameters to handle paging. A starting index and number of results.  The results have to be sorted so that the calling program can reliably work through the table.
        * /contacts?start={beginning index}&size={number of results}
    * Search - suggest breaking up into search first name and search last name 
        * /contacts/search_first_name/{name}?wildcard={true}
        * /contacts/search_last_name/{name}?wildcard={true} 

1. Assign HTTP verbs - read is GET, create is POST, update is PUT or PATCH, delete is DELETE. I prefer additional small URIs to update part of resource instead of a PATCH with a lot of internal switching code.  HEAD is a read that has no content, usually employed when you want to check if the resource has changed or the resource will fit in your storage.

# Implementation
1. The schema.sql contains the database schema.
1. The data.sql file contains SQL commands to run at start up.

# Test

## Version 1
### All Contacts
```bash
curl -i http://localhost:8080/v1/contacts
HTTP/1.1 200
Content-Type: application/json
Transfer-Encoding: chunked
Date: Tue, 09 Jun 2020 03:05:57 GMT

[{"id":1,"name":"Bob Haskel","phone":"3035551234"},{"id":2,"name":"Boba Loo","phone":"8015556874"},{"id":3,"name":"George Amish","phone":"1035559876"}]
```

### One Contact
```bash
curl -i http://localhost:8080/v1/contact/1
HTTP/1.1 200
Content-Type: application/json
Transfer-Encoding: chunked
Date: Tue, 09 Jun 2020 03:10:30 GMT

{"id":1,"name":"Bob Haskel","phone":"3035551234"}
```

### Contact not found
```bash
curl -i http://localhost:8080/v1/contact/123
HTTP/1.1 404
Content-Type: text/plain;charset=UTF-8
Content-Length: 18
Date: Tue, 09 Jun 2020 03:25:19 GMT

unable to find 123
```

### Negative Contact Id
```bash
curl -i http://localhost:8080/v1/contact/-999
HTTP/1.1 400
Content-Type: text/plain;charset=UTF-8
Content-Length: 19
Date: Tue, 09 Jun 2020 03:10:41 GMT
Connection: close

id must be positive
```

### Missing Contact Id
```bash
curl -i http://localhost:8080/v1/contact
HTTP/1.1 404
Vary: Origin
Vary: Access-Control-Request-Method
Vary: Access-Control-Request-Headers
Content-Type: application/json
Transfer-Encoding: chunked
Date: Tue, 09 Jun 2020 03:09:54 GMT

{"timestamp":"2020-06-09T03:09:54.114+00:00","status":404,"error":"Not Found","message":"No message available","path":"/v1/contact"}
```

## Version 2
### All Contacts
```bash
curl -i http://localhost:8080/v2/contacts
HTTP/1.1 200
Content-Type: application/json
Transfer-Encoding: chunked
Date: Tue, 09 Jun 2020 04:29:23 GMT

[{"id":1,"firstName":"Bob","lastName":"Haskel","phone":"3035551234"},{"id":2,"firstName":"Boba","lastName":"Loo","phone":"8015556874"},{"id":3,"firstName":"George","lastName":"Amish","phone":"1035559876"}]
```

### One Contact
```bash
curl -i http://localhost:8080/v2/contact/1
HTTP/1.1 200
Content-Type: application/json
Transfer-Encoding: chunked
Date: Tue, 09 Jun 2020 04:32:11 GMT

{"id":1,"firstName":"Bob","lastName":"Haskel","phone":"3035551234"}
```
