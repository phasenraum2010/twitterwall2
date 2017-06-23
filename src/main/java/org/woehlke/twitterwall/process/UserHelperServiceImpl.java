package org.woehlke.twitterwall.process;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.woehlke.twitterwall.oodm.entities.User;
import org.woehlke.twitterwall.oodm.entities.entities.HashTag;
import org.woehlke.twitterwall.oodm.entities.entities.Mention;
import org.woehlke.twitterwall.oodm.entities.entities.Url;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tw on 22.06.17.
 */
@Service
@Transactional(Transactional.TxType.NOT_SUPPORTED)
public class UserHelperServiceImpl implements UserHelperService {

    private static final Logger log = LoggerFactory.getLogger(UserHelperServiceImpl.class);

    private final UrlHelper urlHelper;

    @Autowired
    public UserHelperServiceImpl(UrlHelper urlHelper) {
        this.urlHelper = urlHelper;
    }

    @Override
    public User getEntitiesForUrlDescription(User user){
        String description = user.getDescription();
        user.setMentions(this.getMentions(description));
        user.setUrls(this.getUrls(description));
        user.setTags(this.getHashTags(description));
        log.info("description "+ description);
        log.info("++++++++++++++++++++");
        for(HashTag hashTag:user.getTags()){
            log.info("found hashTag: "+hashTag.toString());
        }
        for(Url url:user.getUrls()){
            log.info("found url: "+ url.toString());
        }
        for(Mention mention:user.getMentions()){
            log.info("found mention: "+mention.toString());
        }
        log.info("++++++++++++++++++++");
        return user;
    }

    private Set<HashTag> getHashTags(String description){
        Set<HashTag> hashTags = new LinkedHashSet<>();
        if(description!=null) {
            Pattern hashTagPattern = Pattern.compile("#(\\w*)(" + stopChar + ")");
            Matcher m3 = hashTagPattern.matcher(description);
            while (m3.find()) {
                hashTags.add(new HashTag(m3.group(1), indices));
            }
            Pattern hashTagPattern2 = Pattern.compile("#(\\w*)$");
            Matcher m4 = hashTagPattern2.matcher(description);
            while (m4.find()) {
                hashTags.add(new HashTag(m4.group(1), indices));
            }
        }
        return hashTags;
    }

    private Set<Url> getUrls(String description){
        Set<Url> urls = new LinkedHashSet<>();
        if(description!=null) {
            Pattern hashTagPattern = Pattern.compile("(https://t\\.co/\\w*)(" + stopChar + ")");
            Matcher m3 = hashTagPattern.matcher(description);
            while (m3.find()) {
                urls.add(this.urlHelper.fetchUrl(m3.group(1)));
            }
            Pattern hashTagPattern2 = Pattern.compile("(https://t\\.co/\\w*)$");
            Matcher m4 = hashTagPattern2.matcher(description);
            while (m4.find()) {
                urls.add(this.urlHelper.fetchUrl(m4.group(1)));
            }
        }
        return urls;
    }

    private Set<Mention> getMentions(String description){
        Set<Mention> mentions = new LinkedHashSet<>();
        if(description!=null) {
            Pattern mentionPattern1 = Pattern.compile("@(\\w*)(" + stopChar + ")");
            Matcher m3 = mentionPattern1.matcher(description);
            while (m3.find()) {
                mentions.add(getMention(m3.group(1)));
            }
            Pattern mentionPattern2 = Pattern.compile("@(\\w*)$");
            Matcher m4 = mentionPattern2.matcher(description);
            while (m4.find()) {
                mentions.add(getMention(m4.group(1)));
            }
        }
        return mentions;
    }

    private Mention getMention(String mentionString) {
        try {
            Thread.sleep(50L);
        } catch (InterruptedException e) {
            log.warn(e.getMessage());
        }
        long idTwitter = new Date().getTime();
        String screenName=mentionString;
        String name=mentionString;
        int[] myindices = indices;
        return new Mention(idTwitter,screenName,name,myindices);
    }

    static private int[] indices = {};

    static private String stopChar;

    static {
        StringBuffer x = new StringBuffer("[\\s");
        x.append("\\!");
        x.append("\\%");
        x.append("\\&");
        x.append("\\'");
        x.append("\\(");
        x.append("\\)");
        x.append("\\*");
        x.append("\\+");
        x.append("\\,");
        x.append("\\-");
        x.append("\\.");
        x.append("\\/");
        x.append("\\:");
        x.append("\\;");
        x.append("\\=");
        x.append("\\?");
        x.append("\\[");
        x.append("\\]");
        x.append("\\^");
        x.append("\\…");
        x.append("\\`");
        x.append("\\{");
        x.append("\\|");
        x.append("\\}");
        x.append("\\~");
        x.append("\\\\z");
        x.append("]");
        stopChar = x.toString();
    }
}