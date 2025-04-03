package com.githubtracker.gh_repo_analyzer.dto;

import java.util.List;

public record RepositoryResponse(
        String name,
        String ownerLogin,
        List<BranchResponse> branches
) {}