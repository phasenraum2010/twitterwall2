package org.woehlke.twitterwall.oodm.entities;

import org.junit.Assert;
import org.junit.Ignore;
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
import org.woehlke.twitterwall.oodm.exceptions.FindTweetByIdTwitterException;
import org.woehlke.twitterwall.oodm.service.TweetService;
import org.woehlke.twitterwall.process.StoreTweetsProcess;
import org.woehlke.twitterwall.process.TwitterApiService;
import javax.transaction.Transactional;


/**
 * Created by tw on 18.06.17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes={Application.class})
@DataJpaTest(showSql=false)
@Transactional(Transactional.TxType.NOT_SUPPORTED)
public class TweetTest {

    private static final Logger log = LoggerFactory.getLogger(TweetTest.class);

    @Autowired
    private TweetService tweetService;

    @Autowired
    private TwitterApiService twitterApiService;

    @Autowired
    private StoreTweetsProcess storeTweetsProcess;
    
    private final static long millisToWaitForFetchTweetsFromTwitterSearch = 20000;

    private long idTwitterToFetch[] = {
            876329508009279488L,
            876356335784394752L,
            876676270913880066L,
            876566077836337152L,
            876563676395962368L,
            876514968933478400L,
            876514568671023104L,
            876513930478313472L,
            876510758632386563L,
            876496934676180992L
    };

    @Commit
    @Test
    public void fetchTweetsFromTwitterSearchTest() {
        log.info("-----exampleTest-------------------------------------------");
        log.info("Hello, Testing-World. We are waiting for fetchTweetsFromTwitterSearch");
        //this.scheduledTasksFacade.fetchTweetsFromTwitterSearch();
        log.info("number of tweets: "+tweetService.count());
        try {
            Thread.sleep(millisToWaitForFetchTweetsFromTwitterSearch);
            log.info("number of tweets: "+tweetService.count());
            for(long id : idTwitterToFetch)   {
               org.springframework.social.twitter.api.Tweet twitterTweet = twitterApiService.findOneTweetById(id);
                storeTweetsProcess.storeOneTweet(twitterTweet);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assert.assertTrue(true);
        log.info("number of tweets: "+tweetService.count());
        log.info("------------------------------------------------");
    }

    private void performTest(long idTwitter,String output,boolean retweet){
        log.info("idTwitter: "+idTwitter);
        try {
            Tweet tweet = tweetService.findByIdTwitter(idTwitter);
            log.info("text:          " + tweet.getText());
            log.info("Expected:      " + output + "---");
            String formattedText;
            if (retweet) {
                formattedText = tweet.getRetweetedStatus().getFormattedText();
            } else {
                formattedText = tweet.getFormattedText();
            }
            log.info("FormattedText: " + formattedText + "---");
            Assert.assertEquals(output, formattedText);
        } catch (FindTweetByIdTwitterException e){
            log.error(e.getMessage());
        }
        log.info("------------------------------------------------");
    }

    @Test
    public void testText1() {
        long idTwitter = 876329508009279488L;
        String output = "Neu in <a class=\"tweet-action tweet-hashtag\" href=\"/hashtag/TYPO3\">#TYPO3</a> 8 LTS: <a class=\"tweet-action tweet-hashtag\" href=\"/hashtag/Doctrine\">#Doctrine</a> ein <a class=\"tweet-action tweet-hashtag\" href=\"/hashtag/ORM\">#ORM</a> für PHP nach dem Vorbild  <a class=\"tweet-action tweet-hashtag\" href=\"/hashtag/Hibernate\">#Hibernate</a> in <a class=\"tweet-action tweet-hashtag\" href=\"/hashtag/Java\">#Java</a>  <a href=\"http://www.<a href=\"http://www.doctrine-project.org/\" target=\"_blank\">doctrine-project.org</a> \" target=\"_blank\">doctrine-project.org</a> Kenn… <a href=\"https://twitter.com/i/web/status/876329508009279488\" target=\"_blank\">twitter.com/i/web/status/8…</a> ";
        boolean retweet = false;
        performTest(idTwitter,output,retweet);
    }

    @Test
    public void testText2() {
        long idTwitter = 876356335784394752L;
        String output = "Twitterwall zum <a class=\"tweet-action tweet-hashtag\" href=\"/hashtag/t3cb\">#t3cb</a> <a href=\"http://<a href=\"http://natural-born-coder.de/\" target=\"_blank\">natural-born-coder.de</a> \" target=\"_blank\">natural-born-coder.de</a> <br/><br/><a class=\"tweet-photo\" href=\"https://twitter.com/port80guru/status/876356335784394752/photo/1\" target=\"_blank\"><img class=\"tweet-photo\" src=\"https://pbs.twimg.com/media/DClxcLmXsAAW1t6.jpg\" /></a> ";
        boolean retweet = false;
        performTest(idTwitter,output,retweet);
    }

    @Test
    public void testText3() {
        long idTwitter = 876676270913880066L;
        String output = "War schön gestern auf dem <a class=\"tweet-action tweet-hashtag\" href=\"/hashtag/t3cb\">#t3cb</a> … freu mich auf das nächste Jahr. Hoffentlich dann ohne Stau bei der Rückfahrt :)";
        boolean retweet = false;
        performTest(idTwitter,output,retweet);
    }

    @Ignore
    @Test
    public void testText4() {
        long idTwitter = 876566077836337152L;
        String output = "War ein sehr geiles Berlin Wochenende. Danke <a class=\"tweet-action tweet-hashtag\" href=\"/hashtag/t3cb\">#t3cb</a> und natürlich ans Orga Team. <a class=\"tweet-action tweet-hashtag\" href=\"/hashtag/TYPO3\">#TYPO3</a> rockt";
        boolean retweet = true;
        performTest(idTwitter,output,retweet);
    }

    @Ignore
    @Test
    public void testText5() {
        long idTwitter = 876563676395962368L;
        String output = "Empfehlenswert nicht nur für Neulinge. Danke Wolfgang für das kostenlose <a class=\"tweet-action tweet-hashtag\" href=\"/hashtag/Videotraining\">#Videotraining</a> als Preis im Newbie-Jeopard… <a href=\"https://twitter.com/i/web/status/876563676395962368\" target=\"_blank\">twitter.com/i/web/status/8…</a> ";
        boolean retweet = true;
        performTest(idTwitter,output,retweet);
    }

    @Ignore
    @Test
    public void testText6() {
        long idTwitter = 876514968933478400L;
        String output = "In 3 Stunden endet die Sonderaktion zum <a class=\"tweet-action tweet-hashtag\" href=\"/hashtag/t3cb\">#t3cb</a>  15% günstiger mit dem Gutscheincode t3cb -&gt; <a href=\"https://wwagner.net/t3cb\" target=\"_blank\">wwagner.net/t3cb</a> <a class=\"tweet-action tweet-hashtag\" href=\"/hashtag/TYPO3\">#TYPO3</a> <a class=\"tweet-action tweet-hashtag\" href=\"/hashtag/Videotraining\">#Videotraining</a> ";
        boolean retweet = true;
        performTest(idTwitter,output,retweet);
    }

    @Ignore
    @Test
    public void testText7() {
        long idTwitter = 876514568671023104L;
        String output = "Ich hätte schon vorher mal zum <a class=\"tweet-action tweet-hashtag\" href=\"/hashtag/t3cb\">#t3cb</a> gehen sollen. Es war 2017 super und wird es hoffentlich 2018 wieder werden. Danke Orga-Team!";
        boolean retweet = true;
        performTest(idTwitter,output,retweet);
    }

    @Test
    public void testText8() {
        long idTwitter = 876513930478313472L;
        String output = "Passend zur Session \"Barrierefreie Websites\" heute nachmittag. <a class=\"tweet-action tweet-hashtag\" href=\"/hashtag/t3cb\">#t3cb</a> <a href=\"https://twitter.com/Real_CSS_Tricks/status/876483677991075841\" target=\"_blank\">twitter.com/Real_CSS_Trick…</a> ";
        boolean retweet = true;
        performTest(idTwitter,output,retweet);
    }

    @Test
    public void testText9() {
        long idTwitter = 876511554962903040L;
        String output = "<a class=\"tweet-action tweet-profile\" href=\"/profile/cpsitgmbh\">@cpsitgmbh</a> <a class=\"tweet-action tweet-profile\" href=\"/profile/t3c_berlin\">@t3c_berlin</a> Dank dem Orga- Team für das tolle <a class=\"tweet-action tweet-hashtag\" href=\"/hashtag/t3cb\">#t3cb</a> 2017 - ein rundum gelungener Event.";
        boolean retweet = false;
        performTest(idTwitter,output,retweet);
    }

    @Test
    public void testText10() {
        long idTwitter = 876510758632386563L;
        String output = "So, jetzt kann der entspannte Teil vom TYPO3camp an der CPS-IT Bar losgehen <a class=\"tweet-action tweet-hashtag\" href=\"/hashtag/t3cb\">#t3cb</a> <a class=\"tweet-action tweet-hashtag\" href=\"/hashtag/caipis\">#caipis</a> #gintonic… <a href=\"https://twitter.com/i/web/status/876090162102636544\" target=\"_blank\">twitter.com/i/web/status/8…</a> ";
        boolean retweet = true;
        performTest(idTwitter,output,retweet);
    }

    @Test
    public void testText11() {
        long idTwitter = 876496934676180992L;
        String output = "Abschlussrede. War toll mit euch. DANKE! <a class=\"tweet-action tweet-hashtag\" href=\"/hashtag/t3cb\">#t3cb</a> <a class=\"tweet-action tweet-hashtag\" href=\"/hashtag/TYPO3\">#TYPO3</a> <a href=\"https://www.instagram.com/p/BVe6_ULAnEt/\" target=\"_blank\">instagram.com/p/BVe6_ULAnEt/</a> ";
        boolean retweet = true;
        performTest(idTwitter,output,retweet);
    }
}