package com.githubtracker.gh_repo_analyzer.ghClient;

//the same structure as in example response in documentation
public record GitHubRepository (String name, Owner owner, boolean fork) {
    public record Owner (String login) {}
}
