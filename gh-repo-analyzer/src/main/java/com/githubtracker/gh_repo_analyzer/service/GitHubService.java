package com.githubtracker.gh_repo_analyzer.service;

import com.githubtracker.gh_repo_analyzer.dto.RepositoryResponse;
import com.githubtracker.gh_repo_analyzer.ghClient.GitHubBranch;
import com.githubtracker.gh_repo_analyzer.ghClient.GitHubClient;
import com.githubtracker.gh_repo_analyzer.ghClient.GitHubRepository;
import com.githubtracker.gh_repo_analyzer.mapper.GitHubRepositoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GitHubService {


    private final GitHubClient gitHubClient;
    private final GitHubRepositoryMapper mapper;


   public List<RepositoryResponse> getUserRepositories(String username) {
       List<GitHubRepository> repositories = gitHubClient.getUserRepositories(username);

       return repositories.stream()
               .filter(repository -> !repository.fork())
               .map(repository -> {
                   List<GitHubBranch> branches = gitHubClient.getRepositoryBranches(repository.owner().login(), repository.name());
                   return mapper.mapToRepositoryResponse(repository, branches);
               })
               .collect(Collectors.toList());
   }
}