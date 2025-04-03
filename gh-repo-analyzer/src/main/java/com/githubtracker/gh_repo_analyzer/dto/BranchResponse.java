package com.githubtracker.gh_repo_analyzer.dto;

public record BranchResponse(
        String name,
        String lastCommitSha
) {}