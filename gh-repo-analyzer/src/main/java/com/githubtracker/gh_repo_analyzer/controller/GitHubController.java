package com.githubtracker.gh_repo_analyzer.controller;

import com.githubtracker.gh_repo_analyzer.dto.RepositoryResponse;
import com.githubtracker.gh_repo_analyzer.service.GitHubService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/github")
public class GitHubController {

    private final GitHubService gitHubService;

    @GetMapping("/users/{username}/repos")
    public ResponseEntity<List<RepositoryResponse>> getRepositories(@PathVariable String username) {
       return ResponseEntity.ok(gitHubService.getUserRepositories(username));
        }
}
