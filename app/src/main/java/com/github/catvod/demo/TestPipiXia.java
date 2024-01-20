package com.github.catvod.demo;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.github.catvod.spider.PipiXia;
import com.github.catvod.spider.Ys23;

public class TestPipiXia {
        public static void main(String[] args) throws Exception {
                PipiXia pipiXia = new PipiXia();
                // ys21.init(new Context(), "");
                // 首页测试，输出...
                // String s = pipiXia.homeContent(true);
                // System.out.println(s);

                // ArrayList<String> ids = new ArrayList<>();
                // ids.add("http://aikun.tv/v/114393.html");
                // String detailContent = pipiXia.detailContent(ids);
                // System.out.println(detailContent);

                // String playerContent = ys21.playerContent("",
                // "/vod/player.html?cate_id=12&id=139736",
                // null);
                // System.out.println(playerContent);

                // String searchContent = ys21.searchContent("繁华", true);
                // System.out.println(searchContent);

                // HashMap<String, String> map = new HashMap<>();
                // // map.put("cate_id=9&", "54");
                // String categoryContent = pipiXia.categoryContent("2", "1", true, map);
                // System.out.println(categoryContent);
                // 获取首页推荐视频测试
                // String s = voflix.homeVideoContent();
                // System.out.println(s);

                // 分类页面数据测试
                // HashMap<String, String> map = new HashMap<>();
                // map.put("area", "中国香港");
                // String s = voflix.categoryContent("1", "1", true, map);
                // System.out.println(s);

                // 详情页面数据测试
                // ArrayList<String> ids = new ArrayList<>();
                // ids.add("https://www.voflix.me/detail/1911.html");
                // String s = voflix.detailContent(ids);
                // System.out.println(s);

                // 搜索测试
                // String s = pipiXia.searchContent("我", true);
                // System.out.println(s);

                // 播放内容数据测试
                String s = pipiXia.playerContent("",
                                "http://aikun.tv/p/114488-1-1.html", null);
                System.out.println(s);

        }
}
