package com.github.catvod.demo;

import com.github.catvod.spider.Douban;

import java.util.HashMap;

public class TestDouban {
    public static void main(String[] args) throws Exception {
        Douban douban = new Douban();
        HashMap<String , String > extend = new HashMap<>();
//        extend.put("类型", "喜剧");
//        extend.put("年代", "2022");
//        extend.put("sort", "R");
//        System.out.println(douban.categoryContent("movie", "1", true, extend));
//        System.out.println(douban.categoryContent("movie", "2", true, extend));
//        System.out.println(douban.categoryContent("tv", "1", true, extend));
        System.out.println(douban.categoryContent("hot_gaia", "1", true, extend));
    }
}
