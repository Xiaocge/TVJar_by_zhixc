package com.github.catvod.demo;

import com.github.catvod.spider.SP360;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TestSP360 {
    public static void main(String[] args) throws Exception {
        SP360 sp360 = new SP360();
        // 首页内容测试
//        String s = sp360.homeContent(true);
//        System.out.println(s);

        // 分类内容测试
//        HashMap<String, String> map = new HashMap<>();
//        map.put("year", "2023");
//        map.put("actor", "成龙");
//        String s = sp360.categoryContent("1", "1", true, map);
//        System.out.println(s);

        // 详情内容测试
        List<String> list = new ArrayList<>();
        // 电影
//        list.add("{\"detailReferer\":\"https://www.360kan.com/m/gKTiahH7QXH4TB.html\",\"detailUrl\":\"https://api.web.360kan.com/v1/detail?cat=1&id=gKTiahH7QXH4TB&callback=\"}");
        // 电视剧
        list.add("{\"detailReferer\":\"https://www.360kan.com/tv/QLFtcX7mRmDqMX.html\",\"detailUrl\":\"https://api.web.360kan.com/v1/detail?cat=2&id=QLFtcX7mRmDqMX&callback=\"}");
//        list.add("{\"detailReferer\":\"https://www.360kan.com/tv/QbFpaX7mRmPqMH.html\",\"detailUrl\":\"https://api.web.360kan.com/v1/detail?cat=2&id=QbFpaX7mRmPqMH&callback=\"}");
        // 综艺
//        list.add("{\"detailReferer\":\"https://www.360kan.com/va/Z8cla3Nz8pYCFD.html\",\"detailUrl\":\"https://api.web.360kan.com/v1/detail?cat=3&id=Z8cla3Nz8pYCFD&callback=\"}");
//        list.add("{\"detailReferer\":\"https://www.360kan.com/va/ZsclbnNy7pgCDD.html\",\"detailUrl\":\"https://api.web.360kan.com/v1/detail?cat=3&id=ZsclbnNy7pgCDD&callback=\"}");
//        list.add("{\"detailReferer\":\"https://www.360kan.com/va/Y8glbalv7Jc6DD.html\",\"detailUrl\":\"https://api.web.360kan.com/v1/detail?cat=3&id=Y8glbalv7Jc6DD&callback=\"}");
        // 动漫
//        list.add("{\"detailReferer\":\"https://www.360kan.com/ct/OUPkaZ7kNY7vDj.html\",\"detailUrl\":\"https://api.web.360kan.com/v1/detail?cat=4&id=OUPkaZ7kNY7vDj&callback=\"}");
//        list.add("{\"detailReferer\":\"https://www.360kan.com/ct/QE8qcp7jMoOxDj.html\",\"detailUrl\":\"https://api.web.360kan.com/v1/detail?cat=4&id=QE8qcp7jMoOxDj&callback=\"}");
        String s = sp360.detailContent(list);
        System.out.println(s);

        // 搜索内容测试
//        String s = sp360.searchContent("三体", true);
//        String s = sp360.searchContent("满江红", true);
//        System.out.println(s);
    }
}
