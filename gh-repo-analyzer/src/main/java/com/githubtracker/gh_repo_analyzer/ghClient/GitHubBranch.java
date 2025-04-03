package com.githubtracker.gh_repo_analyzer.ghClient;

// the same structure as in example response in documentation
public record GitHubBranch(String name, Commit commit) {
    public record Commit (String sha) {}
}
