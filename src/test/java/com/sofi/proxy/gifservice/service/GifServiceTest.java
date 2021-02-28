package com.sofi.proxy.gifservice.service;

import com.sofi.proxy.gifservice.configuration.Config;
import com.sofi.proxy.gifservice.model.Data;
import com.sofi.proxy.gifservice.model.GifData;
import com.sofi.proxy.gifservice.model.SearchResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class GifServiceTest {

    @Mock
    Config config;

    @Mock
    RestTemplate restTemplate;

    GifService.GifServiceImpl gifService;

    @Before
    public void setup() {
        gifService = new GifService.GifServiceImpl();
        gifService.setConfig(config);
        gifService.setRestTemplate(restTemplate);
        Mockito.when(config.getProperty(Mockito.anyString())).thenReturn("5");
    }

    @Test
    public void testFetchGifImages_1Image() {
        Data data = new Data();
        data.setData(getGifDataList(1));
        Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.any())).thenReturn(data);
        SearchResponse res = gifService.fetchGifImages("funny", 3);
        Assert.assertNotNull(res);
        Assert.assertEquals(0, res.getData().size());
    }

    private List<GifData> getGifDataList(int n) {
        List<GifData> res = new ArrayList<>();
        for(int i = 0; i < n; i++) {
            GifData gifData = new GifData();
            gifData.setId("1234ABD");
            gifData.setUrl("http://abc.com");
            res.add(gifData);
        }
        return res;
    }

    @Test
    public void testFetchGifImages_5Images() {
        Data data = new Data();
        data.setData(getGifDataList(5));
        Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.any())).thenReturn(data);
        SearchResponse res = gifService.fetchGifImages("funny", 3);
        Assert.assertNotNull(res);
        Assert.assertNotNull(res.getData());
        Assert.assertEquals(5, res.getData().size());
    }

    @Test
    public void testFetchGifImages_0Images() {
        Data data = new Data();
        List<GifData> list = new ArrayList<>();
        data.setData(list);
        Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.any())).thenReturn(data);
        SearchResponse res = gifService.fetchGifImages("funny", 3);
        Assert.assertNotNull(res);
        Assert.assertEquals(0, res.getData().size());
    }

    @Test
    public void testFetchGifImages_Exception() {
        Data data = new Data();
        GifData gifData = new GifData();
        gifData.setId("1234ABD");
        gifData.setUrl("http://abc.com");
        List<GifData> list = new ArrayList<>();
        list.add(gifData);
        data.setData(list);
        Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.any())).thenThrow(RuntimeException.class);
        SearchResponse res = gifService.fetchGifImages("funny", 3);
        Assert.assertNotNull(res);
        Assert.assertEquals(0, res.getData().size());
    }

    @Test
    public void testFetchImages_MoreThan5() {
        Data data = new Data();
        data.setData(getGifDataList(15));
        Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.any())).thenReturn(data);
        SearchResponse res = gifService.fetchGifImages("funny", 3);
        Assert.assertNotNull(res);
        Assert.assertNotNull(res.getData());
        Assert.assertEquals(5, res.getData().size());
    }
}
