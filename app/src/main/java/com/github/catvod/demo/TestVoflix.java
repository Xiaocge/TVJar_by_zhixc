package com.github.catvod.demo;

import android.content.Context;
import com.github.catvod.spider.Voflix;

import java.util.ArrayList;
import java.util.HashMap;

public class TestVoflix {
        public static void main(String[] args) throws Exception {
                Voflix voflix = new Voflix();
                voflix.init(new Context(), "https://www.voflix.me/");
                // 首页测试，输出...
                // String s = voflix.homeContent(true);
                // System.out.println(s);

                // 获取首页推荐视频测试
                // String s = voflix.homeVideoContent();
                // System.out.println(s);

                // 分类页面数据测试
                // HashMap<String, String> map = new HashMap<>();
                // map.put("area", "中国香港");
                // String s = voflix.categoryContent("1", "1", true, map);
                // System.out.println(s);

                // 详情页面数据测试
                ArrayList<String> ids = new ArrayList<>();
                ids.add("https://www.voflix.me/detail/1911.html");
                String s = voflix.detailContent(ids);
                System.out.println(s);

                // 搜索测试
                // String s = voflix.searchContent("我", true);
                // System.out.println(s);

                // 播放内容数据测试
                // String s = voflix.playerContent("",
                // "https://www.voflix.me/play/1911-1-1.html", null);
                // System.out.println(s);

        }
}
