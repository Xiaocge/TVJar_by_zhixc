package com.github.catvod.demo;

import android.content.Context;

import com.github.catvod.spider.Freeok;
import com.github.catvod.spider.Vidhub2;
import com.github.catvod.spider.Voflix;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

public class TestFreeok {
    Freeok item;

    @Before
    public void init() throws Exception {
        item = new Freeok();
        // voflix.init(new Context(), "https://www.voflix.me/");
    }

    @Test
    public void homeContent() throws Exception {
        System.out.println(item.homeContent(true));
    }

    @Test
    public void homeVideoContent() throws Exception {
        System.out.println(item.homeVideoContent());
    }

    @Test
    public void categoryContent() throws Exception {
        HashMap<String, String> extend = new HashMap<>();
        // extend.put("area", "中国香港");
        System.out.println(item.categoryContent("1", "1", true, extend));
    }

    @Test
    public void detailContent() throws Exception {
        ArrayList<String> ids = new ArrayList<>();
        // ids.add("/detail/156852.html");
        ids.add("https://www.freeok.me/detail/1599.html");
        System.out.println(item.detailContent(ids));
    }

    @Test
    public void searchContent() throws Exception {
        System.out.println(item.searchContent("绿夜", true));
    }

    @Test
    public void playerContent() throws Exception {
        System.out.println(item.playerContent("", "https://www.freeok.me/play/124424-3-1.html", null));
    }

    public static void main(String[] args) throws Exception {
        TestFreeok ok = new TestFreeok();
        ok.init();
        // ok.homeContent();
        // ok.categoryContent();
        // ok.detailContent();
        ok.searchContent();
        // ok.playerContent();
    }
}