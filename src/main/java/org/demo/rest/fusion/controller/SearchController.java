package org.demo.rest.fusion.controller;

import org.demo.rest.fusion.domain.Author;
import org.demo.rest.fusion.domain.Count;
import org.demo.rest.fusion.service.FusionDemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "/fusion-demo")
public class SearchController {

    @Autowired
    FusionDemoService fusionDemoService;

    @GetMapping(value = "/co-authors")
    public HashMap<String, List<Author>> getCoAuthors(@RequestParam(required = false, defaultValue = "1") Integer minimumPapers) {
        HashMap<String, List<Author>> returnMap = new HashMap<>();
        returnMap.put("authors", fusionDemoService.getCoAuthors(minimumPapers));
        return returnMap;
    }

    @GetMapping(value = "/top-co-author")
    public Author getTopCoAuthor() {
        return fusionDemoService.getTopCoAuthor();
    }

    @GetMapping(value = "/top-co-author-count")
    public Count getTopCoAuthorPaperCount() {
        return fusionDemoService.getTopCoAuthorPaperCount();
    }
}