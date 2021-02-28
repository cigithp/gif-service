package com.sofi.proxy.gifservice;

import com.sofi.proxy.gifservice.controller.Controller;
import com.sofi.proxy.gifservice.model.SearchResponse;
import com.sofi.proxy.gifservice.service.GifService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GifServiceApplicationTests {

	@Autowired
	Controller controller;

	@Autowired
	GifService gifService;

	@Test
	void contextLoads() {
		SearchResponse res = controller.searchGif("funny", 3);
		Assert.assertNotNull(res);
		Assert.assertEquals(5, res.getData().size());
	}

}
