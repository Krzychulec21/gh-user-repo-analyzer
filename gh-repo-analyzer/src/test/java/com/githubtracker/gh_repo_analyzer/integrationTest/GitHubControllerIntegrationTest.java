package com.githubtracker.gh_repo_analyzer.integrationTest;

import com.githubtracker.gh_repo_analyzer.dto.ErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class GitHubControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    private final String baseUrl = "https://api.github.com";

    @BeforeEach
    public void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void shouldReturnRepositoriesForValidUser() throws Exception {
        String reposJson = "[{" +
                "\"name\": \"Hello-World\", " +
                "\"owner\": {\"login\": \"octocat\"}, " +
                "\"fork\": false" +
                "}]";
        mockServer.expect(ExpectedCount.once(), requestTo(baseUrl + "/users/octocat/repos"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(reposJson, MediaType.APPLICATION_JSON));

        String branchesJson = "[{" +
                "\"name\": \"main\", " +
                "\"commit\": {\"sha\": \"abc123\"}" +
                "}]";
        mockServer.expect(ExpectedCount.once(), requestTo(baseUrl + "/repos/octocat/Hello-World/branches"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(branchesJson, MediaType.APPLICATION_JSON));

        mockMvc.perform(get("/api/v1/github/users/octocat/repos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Hello-World")))
                .andExpect(jsonPath("$[0].ownerLogin", is("octocat")))
                .andExpect(jsonPath("$[0].branches", hasSize(1)))
                .andExpect(jsonPath("$[0].branches[0].name", is("main")))
                .andExpect(jsonPath("$[0].branches[0].lastCommitSha", is("abc123")));

        mockServer.verify();
    }

    @Test
    void shouldReturn404ForNonExistentUser() throws Exception {
        String invalidUser = "nonexistentuser";

        mockServer.expect(ExpectedCount.once(), requestTo(baseUrl + "/users/" + invalidUser + "/repos"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        mockMvc.perform(get("/api/v1/github/users/" + invalidUser + "/repos"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("User '"+invalidUser + "' not found")));

        mockServer.verify();
    }

    @Test
    void shouldReturn404ForRepositoryBranchesNotFound() throws Exception {
        String reposJson = "[{" +
                "\"name\": \"Hello-World\", " +
                "\"owner\": {\"login\": \"octocat\"}, " +
                "\"fork\": false" +
                "}]";
        mockServer.expect(ExpectedCount.once(), requestTo(baseUrl + "/users/octocat/repos"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(reposJson, MediaType.APPLICATION_JSON));

        mockServer.expect(ExpectedCount.once(), requestTo(baseUrl + "/repos/octocat/Hello-World/branches"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        mockMvc.perform(get("/api/v1/github/users/octocat/repos"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("User or repository not found")));

        mockServer.verify();
    }

    @Test
    void shouldReturnEmptyListWhenNoRepositoriesFound() throws Exception {
        String reposJson = "[]";
        mockServer.expect(ExpectedCount.once(), requestTo(baseUrl + "/users/octocat/repos"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(reposJson, MediaType.APPLICATION_JSON));

        mockMvc.perform(get("/api/v1/github/users/octocat/repos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        mockServer.verify();
    }

    @Test
    void shouldReturnRateLimitExceeded() throws Exception {
        String fakeResponse = "{\"message\":\"API rate limit exceeded\"}";

        mockServer.expect(requestTo(baseUrl + "/users/octocat/repos"))
                .andRespond(withStatus(HttpStatus.FORBIDDEN)
                        .body(fakeResponse)
                        .contentType(MediaType.APPLICATION_JSON));

        mockMvc.perform(get("/api/v1/github/users/octocat/repos"))
                .andExpect(status().isTooManyRequests());

        mockServer.verify();
    }


}