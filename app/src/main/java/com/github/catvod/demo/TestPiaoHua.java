package com.github.catvod.demo;

import com.github.catvod.spider.PiaoHua;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

public class TestPiaoHua {

    PiaoHua PiaoHua;

    @Before
    public void init() {
        PiaoHua = new PiaoHua();
    }

    @Test
    public void homeContent() throws Exception {
        System.out.println(PiaoHua.homeContent(true));
    }

    @Test
    public void homeVideoContent() {
    }

    @Test
    public void categoryContent() throws Exception {
        HashMap<String, String> extend = new HashMap<>();
//        System.out.println(btPiaoHua.categoryContent("/dongzuo/", "1", true, extend));
        System.out.println(PiaoHua.categoryContent("/dongzuo/", "3", true, extend));
    }

    @Test
    public void detailContent() throws Exception {
        ArrayList<String> ids = new ArrayList<>();
//        ids.add("https://www.xpiaohua.com/column/lianxuju/20210221/51623.html");
//        ids.add("https://www.xpiaohua.com/column/dongzuo/20230626/63766.html");
//        ids.add("https://www.xpiaohua.com/column/dongzuo/20230622/63721.html");
        ids.add("https://www.xpiaohua.com/column/dongzuo/20230622/63719.html");
        System.out.println(PiaoHua.detailContent(ids));
    }

    @Test
    public void searchContent() throws Exception {
        System.out.println(PiaoHua.searchContent("我", true));
    }

    @Test
    public void playerContent() {
    }

    public static void main(String[] args) throws Exception {
        PiaoHua piaohua = new PiaoHua();
        piaohua.init();
        piaohua.homeContent();
        piaohua.homeVideoContent();
        piaohua.categoryContent();
        piaohua.detailContent();
        piaohua.searchContent();
        piaohua.playerContent();
    }
}