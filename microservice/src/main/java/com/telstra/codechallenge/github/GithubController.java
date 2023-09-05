package com.telstra.codechallenge.github;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/github/")
public class GithubController {

    @Autowired
    private GithubService repoService;


    @GetMapping("/hottestRepos/{noOfRepos}")
    public List<HottestRepository> getRepositories(@PathVariable Long noOfRepos) {
        return repoService.getRepositories(noOfRepos);
    }

    @GetMapping("/userAccounts/{noOfAccounts}")
    public List<UserAccount> getZeroUserAccounts(@PathVariable Long noOfAccounts) {
        return repoService.getZeroUserAccounts(noOfAccounts);
    }
}
