package org.woehlke.twitterwall.frontend.controller;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.woehlke.twitterwall.Application;
import org.woehlke.twitterwall.PrepareDataTest;
import org.woehlke.twitterwall.oodm.entities.Task;
import org.woehlke.twitterwall.oodm.entities.parts.CountedEntities;
import org.woehlke.twitterwall.oodm.entities.parts.TaskType;
import org.woehlke.twitterwall.oodm.service.TaskService;
import org.woehlke.twitterwall.scheduled.service.persist.CountedEntitiesService;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Created by tw on 13.07.17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes={Application.class},webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TaskControllerTest {

    private static final Logger log = LoggerFactory.getLogger(TaskControllerTest.class);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskController controller;

    @Autowired
    private TaskService taskService;

    @Autowired
    private PrepareDataTest prepareDataTest;

    @Autowired
    private CountedEntitiesService countedEntitiesService;

    @Commit
    @Test
    public void controllerIsPresentTest(){
        log.info("controllerIsPresentTest");
        assertThat(controller).isNotNull();
    }

    @Commit
    @Test
    public void setupTestData() throws Exception {
        String msg = "setupTestData: ";
        prepareDataTest.getTestDataTweets(msg);
        prepareDataTest.getTestDataUser(msg);
        Assert.assertTrue(true);
    }

    //TODO: #201 https://github.com/phasenraum2010/twitterwall2/issues/201
    //@Ignore
    @WithMockUser
    @Commit
    @Test
    public void getAllTest()throws Exception {
        MvcResult result = this.mockMvc.perform(get("/task/all"))
            .andExpect(status().isOk())
            .andExpect(view().name( "/task/all"))
            .andExpect(model().attributeExists("tasks"))
            .andExpect(model().attributeExists("page"))
            .andReturn();

        String content = result.getResponse().getContentAsString();

        log.info("#######################################");
        log.info("#######################################");
        log.info(content);
        log.info("#######################################");
        log.info("#######################################");
        Assert.assertTrue(true);
    }

    //TODO: #186 https://github.com/phasenraum2010/twitterwall2/issues/186
    //@Ignore
    @WithMockUser
    @Commit
    @Test
    public void getTaskByIdTest() throws Exception {
        CountedEntities countedEntities = countedEntitiesService.countAll();
        String msg ="getTaskByIdTest: ";
        Task task = taskService.create(msg, TaskType.FETCH_TWEETS_FROM_TWITTER_SEARCH,countedEntities);
        long id = task.getId();
        MvcResult result = this.mockMvc.perform(get("/task/"+id))
            .andExpect(status().isOk())
            .andExpect(view().name( "/task/id"))
            .andExpect(model().attributeExists("task"))
            .andExpect(model().attributeExists("taskHistoryList"))
            .andExpect(model().attributeExists("page"))
            .andReturn();

        String content = result.getResponse().getContentAsString();

        log.info("#######################################");
        log.info("#######################################");
        log.info(content);
        log.info("#######################################");
        log.info("#######################################");
        Assert.assertTrue(true);
    }

    //TODO: #183 https://github.com/phasenraum2010/twitterwall2/issues/183
    //@Ignore
    @WithMockUser
    @Commit
    @Test
    public void createTestDataTest() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/task/start/createTestData"))
                .andExpect(status().isOk())
                .andExpect(view().name( "/task/start/createTestData"))
                .andExpect(model().attributeExists("latestTweets"))
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attributeExists("page"))
                .andReturn();

        String content = result.getResponse().getContentAsString();

        log.info("#######################################");
        log.info("#######################################");
        log.info(content);
        log.info("#######################################");
        log.info("#######################################");
        Assert.assertTrue(true);
    }

    //TODO: #185 https://github.com/phasenraum2010/twitterwall2/issues/185
    //@Ignore
    @WithMockUser
    @Commit
    @Test
    public void getOnListRenewTest() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/task/start/user/onlist/renew"))
                .andExpect(status().isOk())
                .andExpect(view().name( "/task/start/renew"))
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attributeExists("page"))
                .andReturn();

        String content = result.getResponse().getContentAsString();

        log.info("#######################################");
        log.info("#######################################");
        log.info(content);
        log.info("#######################################");
        log.info("#######################################");
        Assert.assertTrue(true);
    }
}
