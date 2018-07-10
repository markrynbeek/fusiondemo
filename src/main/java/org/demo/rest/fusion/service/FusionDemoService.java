package org.demo.rest.fusion.service;

import org.demo.rest.fusion.domain.Author;
import org.demo.rest.fusion.domain.Count;
import org.demo.rest.fusion.domain.PercentileRank;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;


@Service
public class FusionDemoService {

    @Autowired
    FusionAuthenticationService fusionAuthenticationService;

    @Value("${fusion.base.url}")
    private String baseUrl;

    @Value("${fusion.coauthor.query.path}")
    private String coAuthorQueryPath;

    public List<Author> getCoAuthors(int minimumPapers) {
        JSONArray authorArray = performCoAuthorQuery(minimumPapers);
        List<Author> authors = new ArrayList<>();
        for (int i=0; i<authorArray.length(); i++) {
            JSONObject item = authorArray.getJSONObject(i);
            Author author = new Author();
            author.setName(item.getString("value"));
            authors.add(author);
        }
        return authors;
    }

    // TODO - we can cap the query for this method, only need top 1
    public Author getTopCoAuthor() {
        List<Author> authors = getCoAuthors(1);
        if (authors.size()>0) {
            return authors.get(0);
        }
        else {
            return null;
        }
    }

    public Count getTopCoAuthorPaperCount() {
        JSONArray authorArray = performCoAuthorQuery(1);
        if (authorArray.length()>0) {
            Count c = new Count();
            c.setValue(authorArray.getJSONObject(0).getInt("count"));
            return c;
        }
        else {
            return null;
        }
    }

    public List<PercentileRank> getCoAuthorPaperCountPercentileRank() {
        JSONArray authorArray = performCoAuthorQuery(1);
        SortedMap<Integer,Integer> paperCountFreq = new TreeMap<>();
        int total=0;
        for (int i=0; i<authorArray.length(); i++) {
            JSONObject item = authorArray.getJSONObject(i);
            int paperCount = item.getInt("count");
            int count = paperCountFreq.containsKey(paperCount) ? paperCountFreq.get(paperCount) : 0;
            paperCountFreq.put(paperCount, count + 1);
            total += 1;
        }
        List<PercentileRank> percentileRanks = new ArrayList();
        int countSoFar=0;
        for(int i : paperCountFreq.keySet()){
            double percentileRank = ((countSoFar + (0.5 * paperCountFreq.get(i) )) / total ) * 100;
            PercentileRank pr = new PercentileRank();
            pr.setCount(i);
            pr.setRank(percentileRank);
            percentileRanks.add(pr);
            countSoFar += paperCountFreq.get(i);
        }
        return percentileRanks;
    }


    private JSONArray performCoAuthorQuery(int minimumPapers) {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Cookie", fusionAuthenticationService.getCookie());
        HttpEntity requestEntity = new HttpEntity(null, requestHeaders);
        RestTemplate restTemplate = new RestTemplate();
        String url = baseUrl + coAuthorQueryPath;
        url = url.replace("MIN_COUNT",String.valueOf(minimumPapers));
        ResponseEntity response = restTemplate.exchange(url,HttpMethod.GET,requestEntity,String.class);
        JSONObject jsonObject = new JSONObject(response.getBody().toString());
        JSONArray authorArray = jsonObject.getJSONObject("facet_counts").getJSONObject("facet_pivot").getJSONArray("authors_ss,authors_ss");
        return authorArray;
    }

}
