package org.woehlke.twitterwall.scheduled.service.backend;

import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.twitterwall.oodm.entities.entities.Url;
import org.woehlke.twitterwall.exceptions.remote.FetchUrlException;
import org.woehlke.twitterwall.oodm.service.entities.UrlServiceImpl;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by tw on 28.06.17.
 */
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
public class TwitterUrlServiceImpl implements TwitterUrlService {

    private static final Logger log = LoggerFactory.getLogger(UrlServiceImpl.class);

    @Override
    public Url fetchTransientUrl(String urlSrc) {
        String msg = "fetchTransientUrl "+urlSrc+" ";
        log.debug(msg);
        Url newUrl = null;
        try {
            String display;
            String expanded;
            int[] indices = {};
            long connTimeToLive = 30L;
            CloseableHttpClient httpclient = HttpClients.custom().setConnectionTimeToLive(connTimeToLive, TimeUnit.SECONDS).disableCookieManagement().evictIdleConnections(connTimeToLive,TimeUnit.SECONDS).build();
            HttpGet httpGet = new HttpGet(urlSrc);
            HttpClientContext context = HttpClientContext.create();
            CloseableHttpResponse response1 = httpclient.execute(httpGet, context);
            HttpHost target = context.getTargetHost();
            List<URI> redirectLocations = context.getRedirectLocations();
            URL location = URIUtils.resolve(httpGet.getURI(), target, redirectLocations).toURL();
            display = location.getHost();
            expanded = location.toExternalForm();
            newUrl = new Url(display, expanded, urlSrc, indices);
            response1.close();
        } catch (ClientProtocolException e) {
            log.warn(e.getMessage());
        } catch (NullPointerException e) {
            log.warn(e.getMessage());
        } catch (IllegalArgumentException e){
            log.warn(e.getMessage());
        } catch (URISyntaxException e) {
            log.warn(e.getMessage());
        } catch (IOException e) {
            log.warn(e.getMessage());
            if(newUrl != null){
                return newUrl;
            }
        } catch (RuntimeException ex) {
            log.warn(ex.getMessage());
        } catch (Exception ex) {
            log.warn(ex.getMessage());
        }
        if(newUrl == null){
            log.warn(msg+newUrl);
            throw new FetchUrlException(urlSrc);
        } else {
            log.debug(msg+newUrl);
            return newUrl;
        }
    }
}