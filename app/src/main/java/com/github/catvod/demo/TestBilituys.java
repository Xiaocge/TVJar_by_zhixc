package com.github.catvod.demo;

import com.github.catvod.spider.Bilituys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TestBilituys {
    public static void main(String[] args) throws Exception {
        Bilituys bilituys = new Bilituys();
        // 首页内容测试
//        String s = bilituys.homeContent(true);
//        System.out.println(s);

        // 分类内容测试
//        HashMap<String, String> map = new HashMap<>();
//        map.put("year", "2023");
//        String s = bilituys.categoryContent("1", "1", true, map);
//        System.out.println(s);

        // 详情内容测试
//        List<String> list = new ArrayList<>();
//        list.add("https://www.bilituys.com/bilidetail/85267.html");
//        String s = bilituys.detailContent(list);
//        System.out.println(s);

        // 搜索内容测试
//        String s = bilituys.searchContent("我", true);
//        System.out.println(s);

        // 播放内容测试
        String s = bilituys.playerContent("", "https://www.bilituys.com/biliplay/85267-1-1.html", new ArrayList<>());
        System.out.println(s);
    }
}
