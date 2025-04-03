package com.githubtracker.gh_repo_analyzer.mapper;

import com.githubtracker.gh_repo_analyzer.dto.BranchResponse;
import com.githubtracker.gh_repo_analyzer.dto.RepositoryResponse;
import com.githubtracker.gh_repo_analyzer.ghClient.GitHubBranch;
import com.githubtracker.gh_repo_analyzer.ghClient.GitHubRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GitHubRepositoryMapper {

    public RepositoryResponse mapToRepositoryResponse(GitHubRepository repository, List<GitHubBranch> branches) {
        List<BranchResponse> branchResponses = branches.stream()
                .map(branch -> new BranchResponse(branch.name(), branch.commit().sha()))
                .collect(Collectors.toList());
        return new RepositoryResponse(repository.name(), repository.owner().login(), branchResponses);

    }
}
