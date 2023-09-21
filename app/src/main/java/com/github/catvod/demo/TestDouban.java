package com.github.catvod.demo;

import com.github.catvod.spider.Douban;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

public class TestDouban {
    Douban douban;

    @Before
    public void init() {
        douban = new Douban();
    }

    @Test
    public void homeContent() throws Exception {
        System.out.println(douban.homeContent(true));
    }

    @Test
    public void categoryContent() throws Exception {
        HashMap<String, String> extend = new HashMap<>();
//        extend.put("类型", "喜剧");
//        extend.put("年代", "2022");
//        extend.put("sort", "R");
//        System.out.println(douban.categoryContent("movie", "1", true, extend));
//        System.out.println(douban.categoryContent("movie", "2", true, extend));
//        System.out.println(douban.categoryContent("tv", "1", true, extend));
        System.out.println(douban.categoryContent("hot_gaia", "1", true, extend));
    }
}
