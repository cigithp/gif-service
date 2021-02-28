package com.sofi.proxy.gifservice.controller;

import com.sofi.proxy.gifservice.model.SearchResponse;
import com.sofi.proxy.gifservice.service.GifService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
public class Controller {
    private Logger logger = LoggerFactory.getLogger(Controller.class);
    private GifService gifService;

    @Autowired
    public void setGifService(GifService gifService) {
        this.gifService = gifService;
    }

    @GetMapping("/search/{term}")
    public SearchResponse searchGif(@PathVariable String term, @RequestParam(required = false) Integer page) {
        logger.info("Searching for "+term);
        page = page != null && page > 0 ? page : 0;
        return gifService.fetchGifImages(term, page);
    }
}
