package com.sofi.proxy.gifservice.service;

import com.sofi.proxy.gifservice.configuration.Config;
import com.sofi.proxy.gifservice.model.Data;
import com.sofi.proxy.gifservice.model.SearchResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Optional;
import java.util.stream.Collectors;

public interface GifService {
    SearchResponse fetchGifImages(String term, int page);

    class GifServiceImpl implements GifService {
        private Logger logger = LoggerFactory.getLogger(GifService.class);

        private static final String API_URL = "apiURL";
        private static final String API_KEY = "apiKey";
        private static final String LIMIT = "limit";

        private Config config;
        private RestTemplate restTemplate;

        @Autowired
        public void setConfig(Config config) {
            this.config = config;
        }

        @Autowired
        public void setRestTemplate(RestTemplate restTemplate) {
            this.restTemplate = restTemplate;
        }

        private String query = "%s/v1/gifs/search?api_key=%s&q=%s&limit=%s&offset=%s&rating=g&lang=en";

        private Optional<Data> callGiphyAPI(String term, int page) throws UnsupportedEncodingException {
            String endpoint = config.getProperty(API_URL);
            String apiKey = config.getProperty(API_KEY);
            String limit = config.getProperty(LIMIT);
            String reqURL = String.format(query, endpoint, apiKey, URLEncoder.encode(term, "UTF-16"), limit, page * Integer.parseInt(limit));
            logger.info("Request URL is : " + reqURL);
            return Optional.ofNullable(restTemplate.getForObject(reqURL, Data.class));
        }

        @Override
        public SearchResponse fetchGifImages(String term, int page) {
            Optional<Data> res = null;
            try {
                logger.info("Calling Giphy API..");
                res = callGiphyAPI(term, page);
                logger.info("Call success");
            } catch (UnsupportedEncodingException e) {
                logger.error("UnsupportedEncodingException occurred. Message: " + e.getMessage());
            } catch (Exception ex) {
                logger.error("Exception occurred. Message: " + ex.getMessage());
            }
            if (res != null && res.isPresent()) {
                int limit = Integer.parseInt(config.getProperty(LIMIT));
                try {
                    if (res.get().getData().size() < limit)
                        return new SearchResponse();
                    SearchResponse r = new SearchResponse();
                    //mapping the Giphy API response to gif service api response
                    r.setData(res.get().getData().subList(0, limit).stream().map(a -> {
                        SearchResponse.GifData g = r.new GifData();
                        g.setGif_id(a.getId());
                        g.setUrl(a.getUrl());
                        return g;
                    }).collect(Collectors.toList()));
                    if(page * limit >= res.get().getPagination().getTotal_count())
                        r.setLast_page(true);
                    return r;
                } catch (NumberFormatException ne) {
                    logger.error("NumberFormatException occurred. Failed to retrieve limit from config. Message: " + ne.getMessage());
                }
            }
            return new SearchResponse();
        }
    }
}
