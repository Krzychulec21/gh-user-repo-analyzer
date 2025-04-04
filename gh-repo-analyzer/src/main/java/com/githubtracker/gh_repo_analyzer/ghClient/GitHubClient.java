package com.githubtracker.gh_repo_analyzer.ghClient;

import com.githubtracker.gh_repo_analyzer.exception.RateLimitExceededException;
import com.githubtracker.gh_repo_analyzer.exception.ResourceNotFoundException;
import com.githubtracker.gh_repo_analyzer.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GitHubClient {

    private final RestTemplate restTemplate;
    private final String baseUrl = "https://api.github.com";

    public List<GitHubRepository> getUserRepositories(String username) {
        try {
            GitHubRepository[] repositories = restTemplate.getForObject(
                    baseUrl + "/users/{username}/repos",
                    GitHubRepository[].class,
                    username
            );
            return Arrays.asList(repositories);
        } catch (HttpClientErrorException exception) {
            if (exception.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new UserNotFoundException("User '" + username + "' not found");
            } else if (exception.getStatusCode() == HttpStatus.FORBIDDEN) {
                throw new RateLimitExceededException("API rate limit exceeded");
            }
            throw exception;
        }
    }

    public List<GitHubBranch> getRepositoryBranches(String username, String repository) {
        try {
            GitHubBranch[] branches = restTemplate.getForObject(
                    baseUrl + "/repos/{username}/{repository}/branches",
                    GitHubBranch[].class,
                    username,
                    repository
            );
            return Arrays.asList(branches);
        } catch (HttpClientErrorException exception) {
            if (exception.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new ResourceNotFoundException("User or repository not found");
            } else if (exception.getStatusCode() == HttpStatus.FORBIDDEN) {
                throw new RateLimitExceededException("API rate limit exceeded");
            }
            throw exception;
        }
    }


}
