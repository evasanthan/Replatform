package com.github.bpark;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.context.TestPropertySource;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.WebApplicationContext;

//import static com.sun.org.apache.xerces.internal.util.PropertyState.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@WebMvcTest(BlogPostController.class)


public class Tests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private BlogPostDaoBean blogPostDaoBeanMock;
    @MockBean
    private SpamFilterService spamFilterServiceMock;

    private BindingResult bindingResultMock;
    private BlogPost blogPost;

    @Autowired private ObjectMapper mapper;

    @Before
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Before
    public void setUpProduct() throws Exception{

        blogPost = new BlogPost();
        blogPost.setContent("Welcome to Junit Test");
        blogPost.setShortContent("World Blog Strory");
        blogPost.setAuthor("Shamley David");
        blogPost.setId(1001);
        blogPost.setTitle("Welcome to Spring World.");

    }

    @Test
    public void ValidateBlogPostMethod()
            throws Exception {
       // assertThat(this.blogPostDaoBeanMock).isNotNull();
        mvc.perform(get("/"))

                .andExpect(status().isOk())
                .andExpect(view().name("posts"))
                .andDo(print());
    }

    @Test
    public void ValidateBlogPostNewMethod()
            throws Exception {
        // assertThat(this.blogPostDaoBeanMock).isNotNull();
        mvc.perform(get("/blogposts/new"))

                .andExpect(status().isOk())
                .andExpect(view().name("new"))
                .andDo(print());
    }

    @Test
    public void ValidateBlogPostofPostsMethod()
            throws Exception {
        mvc.perform(post("/blogposts/posts").param("title","Welcome to Blog").param("content","Java and Spring world"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/blogposts/"))
                .andExpect(view().name("redirect:/blogposts/"))
                .andDo(print());
    }

    @Test
    public void ValidateBlogPostGetArticlesMethod()
            throws Exception {
        // assertThat(this.blogPostDaoBeanMock).isNotNull();
        mvc.perform(get("/blogposts/"))

                .andExpect(status().isOk())
                .andExpect(view().name("posts"))
                .andDo(print());
    }


    @Test
    public void ValidateBlogPostGetViewArticlesMethod()
            throws Exception {
        assertThat(this.blogPostDaoBeanMock).isNotNull();
        when(blogPostDaoBeanMock.find(1)).thenReturn(blogPost);
        assertThat(this.blogPost).isNotNull();

        mvc.perform(get("/blogposts/{id}",1000))
                .andExpect(status().isOk())
                .andExpect(view().name("article"))
                //.andExpect(MockMvcResultMatchers.model().attributeExists("article"))
                .andDo(print());
    }

}