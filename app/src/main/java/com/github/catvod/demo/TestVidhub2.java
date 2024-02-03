package com.github.catvod.demo;

import android.content.Context;

import com.github.catvod.spider.Vidhub2;
import com.github.catvod.spider.Voflix;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

public class TestVidhub2 {
    Vidhub2 vidhub2;

    @Before
    public void init() throws Exception {
        vidhub2 = new Vidhub2();
        // voflix.init(new Context(), "https://www.voflix.me/");
    }

    @Test
    public void homeContent() throws Exception {
        System.out.println(vidhub2.homeContent(true));
    }

    @Test
    public void homeVideoContent() throws Exception {
        System.out.println(vidhub2.homeVideoContent());
    }

    @Test
    public void categoryContent() throws Exception {
        HashMap<String, String> extend = new HashMap<>();
        extend.put("area", "中国香港");
        System.out.println(vidhub2.categoryContent("1", "1", true, extend));
    }

    @Test
    public void detailContent() throws Exception {
        ArrayList<String> ids = new ArrayList<>();
        // ids.add("/detail/156852.html");
        ids.add("https://vidhub2.cc/voddetail/239550.html");
        System.out.println(vidhub2.detailContent(ids));
    }

    @Test
    public void searchContent() throws Exception {
        System.out.println(vidhub2.searchContent("我", true));
    }

    @Test
    public void playerContent() throws Exception {
        System.out.println(vidhub2.playerContent("", "/vodplay/239252-1-1.html", null));
    }

    public static void main(String[] args) throws Exception {
        TestVidhub2 item = new TestVidhub2();
        item.init();
        // item.homeContent();
        // item.categoryContent();
        // item.detailContent();
        // Qile.searchContent();
        item.playerContent();
    }
}