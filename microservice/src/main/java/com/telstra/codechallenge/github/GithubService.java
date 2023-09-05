package com.telstra.codechallenge.github;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class GithubService {
    @Value("${githubToken}")
    private String githubToken;

    @Value("${githubURL}")
    private String githubURL;

    @Autowired
    private RestTemplate restTemplate;

public List<HottestRepository> getRepositories(Long noOfRepos) {
    LocalDate lastWeek = LocalDate.now().minusWeeks(1);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    String formattedDate = formatter.format(lastWeek);
    String url = githubURL+"/repositories?q=created:>"+ formattedDate+"&sort=stars&order=desc&per_page="+noOfRepos;
    List<HottestRepository> repositories = new ArrayList<>();

    HttpEntity<String> entity = getStringHttpEntity();
    try{
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        String repoResponse = response.getBody();
        Gson gson = new Gson();

        JsonObject jsonObject = gson.fromJson(repoResponse, JsonObject.class);
        JsonArray items = jsonObject.getAsJsonArray("items");

        for (JsonElement item : items) {
            JsonObject itemObj = item.getAsJsonObject();

            HottestRepository repository = new HottestRepository();

//                String name= obj.has("name") ? obj.get("name").toString() : "";
//                String description = obj.has("description") ? obj.get("description").toString() : "";
//                String language = obj.has("language") ? obj.get("language").toString() : "";
//                String html_url = obj.has("html_url") ? obj.get("html_url").toString() : "";
//                String watchers_count = obj.has("watchers_count") ? obj.get("watchers_count").toString() : "";

            String name= (itemObj.has("name") && itemObj.get("name") != null && !itemObj.get("name").isJsonNull()) ? itemObj.get("name").getAsString() : "";
            String description = (itemObj.has("description") && itemObj.get("description") != null && !itemObj.get("description").isJsonNull()) ? itemObj.get("description").getAsString() : "";
            String language = (itemObj.has("language") && itemObj.get("language") != null && !itemObj.get("language").isJsonNull()) ? itemObj.get("language").getAsString() : "";
            String html_url = (itemObj.has("html_url") && itemObj.get("html_url") != null && !itemObj.get("html_url").isJsonNull()) ? itemObj.get("html_url").getAsString() : "";
            long watchers_count = (itemObj.has("watchers_count") && itemObj.get("watchers_count") != null && !itemObj.get("watchers_count").isJsonNull()) ? itemObj.get("watchers_count").getAsLong() : 0;

            repository.setWatchers_count(watchers_count);
            repository.setLanguage(language);
            repository.setHtml_url(html_url);
            repository.setName(name);
            repository.setDescription(description);
            repositories.add(repository);
        }

    }catch (Exception e){
        e.printStackTrace();
    }
    return repositories;
}

public List<UserAccount> getZeroUserAccounts(Long noOfAccounts) {
    String url = githubURL+"/users?q=followers:0&sort=joined&order=asc&per_page="+noOfAccounts;

    List<UserAccount> accounts = new ArrayList<>();
    HttpEntity<String> entity = getStringHttpEntity();

    try{
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        String userAccountsResponse = response.getBody();
        Gson gson = new Gson();

        JsonObject accountObj = gson.fromJson(userAccountsResponse, JsonObject.class);
        JsonArray items = accountObj.getAsJsonArray("items");

        for (JsonElement item : items) {
            JsonObject itemObj = item.getAsJsonObject();

            UserAccount account = new UserAccount();

            int id= (itemObj.has("id") && itemObj.get("id") != null && !itemObj.get("id").isJsonNull()) ? itemObj.get("id").getAsInt() : 0;
            String login = (itemObj.has("login") && itemObj.get("login") != null && !itemObj.get("login").isJsonNull()) ? itemObj.get("login").getAsString() : "";
            String html_url = (itemObj.has("html_url") && itemObj.get("html_url") != null && !itemObj.get("html_url").isJsonNull()) ? itemObj.get("html_url").getAsString() : "";

            account.setId(id);
            account.setLogin(login);
            account.setHtml_url(html_url);
            accounts.add(account);
        }
    }catch(Exception e){
        e.printStackTrace();
    }

        return accounts;
}

    private HttpEntity<String> getStringHttpEntity() {
        HttpHeaders headers =  new HttpHeaders();
        headers.set("Authorization", githubToken);
        return new HttpEntity<>(headers);
    }
}

