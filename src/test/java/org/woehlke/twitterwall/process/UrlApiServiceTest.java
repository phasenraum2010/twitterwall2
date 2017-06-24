package org.woehlke.twitterwall.process;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;
import org.woehlke.twitterwall.Application;
import org.woehlke.twitterwall.process.helper.TestHelperService;
import org.woehlke.twitterwall.oodm.entities.entities.Url;
import org.woehlke.twitterwall.oodm.exceptions.remote.FetchUrlException;
import org.woehlke.twitterwall.process.parts.UrlApiService;

import javax.transaction.Transactional;

import java.util.List;

/**
 * Created by tw on 21.06.17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes={Application.class})
@DataJpaTest(showSql=false)
@Transactional(Transactional.TxType.NOT_SUPPORTED)
public class UrlApiServiceTest {

    private static final Logger log = LoggerFactory.getLogger(UrlApiServiceTest.class);

    @Autowired
    private UrlApiService urlApiService;

    @Autowired
    private TestHelperService testHelperService;

    @Commit
    @Test
    public void fetchTweetsFromTwitterSearchTest() {
        testHelperService.waitForImport();
        Assert.assertTrue(true);
    }

    @Commit
    @Test
    public void fetchUrlTest(){
        log.info("------------------------------------");
        log.info("fetchUrlTest");
        List<Url> testData = urlApiService.getTestData();
        for(Url url:testData){
            try {
                log.info("expected: " + url.toString());
                Url foundUrl = urlApiService.fetchUrl(url.getUrl());
                Assert.assertEquals(foundUrl.getUrl(), url.getUrl());
                Assert.assertEquals(foundUrl.getDisplay(), url.getDisplay());
                Assert.assertEquals(foundUrl.getExpanded(), url.getExpanded());
                log.info("found:    " + foundUrl.toString());
            } catch (FetchUrlException e){
                log.error(e.getMessage());
            }
        }
        log.info("------------------------------------");
    }
}