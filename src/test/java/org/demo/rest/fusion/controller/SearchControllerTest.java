package org.demo.rest.fusion.controller;

import org.demo.rest.fusion.domain.Author;
import org.demo.rest.fusion.domain.Count;
import org.demo.rest.fusion.service.FusionDemoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(SearchController.class)
public class SearchControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private FusionDemoService fusionDemoService;

    @Test
    public void getTopCoAuthor_noArgs_expectValidJsonRepresentation () throws Exception {

        String authorName = "M.Rijnbeek";
        Author author = new Author();
        author.setName(authorName);
        when(fusionDemoService.getTopCoAuthor()).thenReturn(author);

        MvcResult result = mvc
                .perform(MockMvcRequestBuilders.get("/fusion-demo/top-co-author").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        String expectedBody  = "{\"author\":{\"name\":\""+authorName+"\"}}";
        JSONAssert.assertEquals(expectedBody, result.getResponse().getContentAsString(), false);
    }


    @Test
    public void getTopCoAuthorCount_noArgs_expectValidJsonRepresentation () throws Exception {

        int counted=6;
        Count cnt = new Count();
        cnt.setValue(counted);

        when(fusionDemoService.getTopCoAuthorPaperCount()).thenReturn(cnt);

        MvcResult result = mvc
                .perform(MockMvcRequestBuilders.get("/fusion-demo/top-co-author-count").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        String expectedBody  = "{\"count\":{\"value\":6}}";
        JSONAssert.assertEquals(expectedBody, result.getResponse().getContentAsString(), false);
    }

    @Test
    public void getTopCoAuthor_nothingFound_expectValidJsonRepresentation () throws Exception {
        when(fusionDemoService.getTopCoAuthor()).thenReturn(null);
        MvcResult result = mvc
                .perform(MockMvcRequestBuilders.get("/fusion-demo/top-co-author").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
       assertEquals("", result.getResponse().getContentAsString());
    }

    @Test
    public void getTopCoAuthors_invalidNumberProvided_expect4xx () throws Exception {
        when(fusionDemoService.getTopCoAuthor()).thenReturn(null);
        mvc.perform(MockMvcRequestBuilders.get("/fusion-demo/co-authors?minimumPapers=abc").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError()).andReturn();
    }

    @Test
    public void invalidUrl_pathDoesNotExist_expect4xx () throws Exception {
        when(fusionDemoService.getTopCoAuthor()).thenReturn(null);
        mvc.perform(MockMvcRequestBuilders.get("/fusion-demo/doesnotexist").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError()).andReturn();
    }

}
