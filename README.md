# GitHub Repo Analyzer

This is a simple Spring Boot API that lists GitHub repositories for a given user. It returns repositories that are not forks. For each repository, the API provides:
- **Repository Name**
- **Owner Login**
- **For each branch:** branch name and last commit SHA

If a user is not found, the API returns a 404 response with a JSON error message like this:

```json
{
  "status": 404,
  "message": "User 'username' not found"
}
```

If the API rate limit is exceeded (for unauthorized requests which are used here - 50 queries per hour), the API returns a 429 response with a JSON error message like this:

```json
{
  "status": 429,
  "message": "API rate limit exceeded"
}
```
## How to run
1. Execute command `https://github.com/Krzychulec21/gh-user-repo-analyzer.git`
2. Execute command `mvn clean install` in project catalog to build project.
3. Run app by command `mvn spring-boot:run`

# How to use
`http://localhost:8080/api/v1/github/users/{username}/repos`  
Replace {username} with any GitHub username you want to check.
For examle: 
![image](https://github.com/user-attachments/assets/6912677a-643e-4f2d-9873-291c7b3c3d2d)

## Used Tools

- **Java 21**
- **Spring Boot 3.4.4**
- **Maven**
- **Lombok**
