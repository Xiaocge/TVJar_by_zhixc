package com.github.catvod.spider;

import android.content.Context;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import com.github.catvod.bean.Class;
import com.github.catvod.bean.Result;
import com.github.catvod.bean.Vod;
import com.github.catvod.crawler.Spider;
import com.github.catvod.net.OkHttp;
import com.github.catvod.utils.Util;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Ole extends Spider {

    private String siteURL = "https://www.olevod.com";

    private Map<String, String> getHeader() {
        Map<String, String> header = new HashMap<>();
        header.put("User-Agent", Util.CHROME);
        // header.put("authority", "www.olevod.com");
        // header.put("accept",
        // "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7");
        // header.put("accept-language", "zh-CN,zh;q=0.9");
        // header.put("cache-control", "no-cache");
        // header.put("cookie",
        // "X_CACHE_KEY=e82fb2a4cba5f7a4a3dfe5a49855fbe6; 53274=53274; 52487=52487;
        // _gid=GA1.2.1310736166.1706755318; PHPSESSID=lltbl3o6ui2igqidataiqidoa0;
        // 404562=404562; 404561=404561; 53306=53306; _ga=GA1.1.869055243.1706608232;
        // history=%5B%7B%22name%22%3A%22%E9%80%83%E7%A6%BB%E7%96%AF%E4%BA%BA%E9%99%A2%22%2C%22pic%22%3A%22https%3A%2F%2Fstatic.olelive.com%2Fupload%2Fvod%2F20240127-1%2Fb9d0b0581c63a3917e116ac8c03a5b20.jpg%22%2C%22link%22%3A%22%2Findex.php%2Fvod%2Fplay%2Fid%2F53306%2Fsid%2F1%2Fnid%2F1.html%22%2C%22part%22%3A%22%E9%AB%98%E6%B8%85%E6%92%AD%E6%94%BE%22%7D%2C%7B%22name%22%3A%22CCTV5%22%2C%22pic%22%3A%22https%3A%2F%2Fstatic.olelive.com%2Fupload%2Fvod%2F20221119-1%2F2c12cae73005b654d5df6546411fae80.png%22%2C%22link%22%3A%22%2Findex.php%2Fvod%2Fplay%2Fid%2F40456%2Fsid%2F1%2Fnid%2F1.html%22%2C%22part%22%3A%22%E8%B6%85%E6%B8%85%E6%92%AD%E6%94%BE1%22%7D%5D;
        // _ga_BNR15WFH4G=GS1.1.1706755317.2.1.1706756429.0.0.0;
        // X_CACHE_KEY=364da9e0b68cdce8ebeb38425e721ea3");
        // header.put("pragma", "no-cache");
        // header.put("sec-ch-ua", "\"Not A(Brand\";v=\"99\", \"Google
        // Chrome\";v=\"121\", \"Chromium\";v=\"121\"");
        // header.put("sec-ch-ua-mobile", "?0");
        // header.put("sec-ch-ua-platform", "\"Windows\"");
        // header.put("sec-fetch-dest", "document");
        // header.put("sec-fetch-mode", "navigate");
        // header.put("sec-fetch-site", "none");
        // header.put("sec-fetch-user", "?1");
        // header.put("upgrade-insecure-requests", "1");
        // header.put("user-agent",
        // "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like
        // Gecko) Chrome/121.0.0.0 Safari/537.36");
        return header;
    }

    @Override
    public void init(Context context, String extend) throws Exception {
        if (!extend.isEmpty())
            siteURL = extend;
    }

    @Override
    public String homeContent(boolean filter) throws Exception {
        List<Class> classes = new ArrayList<>();
        List<String> typeIds = Arrays.asList("1", "2", "3", "4", "48", "49");
        List<String> typeNames = Arrays.asList("电影", "电视剧", "综艺", "动漫", "热门短剧", "体育赛事");
        for (int i = 0; i < typeIds.size(); i++) {
            classes.add(new Class(typeIds.get(i), typeNames.get(i)));
        }
        String f = "{\"1\": [{\"key\": \"class\", \"name\": \"剧情\", \"value\": [{\"n\": \"全部\", \"v\": \"\"}, {\"n\": \"动作片\", \"v\": \"cate_id=9&\"}, {\"n\": \"喜剧片\", \"v\": \"cate_id=10&\"}, {\"n\": \"爱情片\", \"v\": \"cate_id=11&\"}, {\"n\": \"恐怖片\", \"v\": \"cate_id=12&\"}, {\"n\": \"剧情片\", \"v\": \"cate_id=13&\"}, {\"n\": \"科幻片\", \"v\": \"cate_id=14&\"}, {\"n\": \"动画片\", \"v\": \"cate_id=17&\"}]}], \"2\": [{\"key\": \"class\", \"name\": \"剧情\", \"value\": [{\"n\": \"全部\", \"v\": \"\"}, {\"n\": \"国产剧\", \"v\": \"cate_id=22&\"}, {\"n\": \"香港剧\", \"v\": \"cate_id=23&\"}, {\"n\": \"台湾剧\", \"v\": \"cate_id=24&\"}, {\"n\": \"欧美剧\", \"v\": \"cate_id=25&\"}, {\"n\": \"日本剧\", \"v\": \"cate_id=26&\"}, {\"n\": \"韩国剧\", \"v\": \"cate_id=27&\"}, {\"n\": \"海外剧\", \"v\": \"cate_id=29&\"}]}]}";
        JSONObject filterConfig = new JSONObject(f);
        Document doc = Jsoup.parse(OkHttp.string(siteURL, getHeader()));
        ArrayList<Vod> list = new ArrayList<Vod>();
        for (Element li : doc.select(".video-card-wrap")) {
            String vid = siteURL + li.select("a").attr("href");
            String name = li.select("a").attr("title");
            String pic = li.select("a img").attr("data-src");
            if (!pic.startsWith("http"))
                pic = siteURL + pic;
            list.add(new Vod(vid, name, pic));
        }
        return Result.string(classes, list, filterConfig);
    }

    @Override
    public String categoryContent(String tid, String pg, boolean filter, HashMap<String, String> extend)
            throws Exception {
        String classType = extend.get("class") == null ? "" : extend.get("class");
        String cateId = extend.get("cateId") == null ? tid : extend.get("cateId");
        String cateURL = siteURL
                + String.format("/vod/index.html?%spage=%s&type_id=%s", classType, pg, cateId);
        System.out.println(cateURL);
        Document doc = Jsoup.parse(OkHttp.string(cateURL, getHeader()));
        List<Vod> list = new ArrayList<>();
        for (Element li : doc.select(".card.vertical")) {
            String vid = siteURL + li.attr("href");
            String name = li.attr("title");
            String pic = li.select("img").attr("data-src");
            if (!pic.startsWith("http"))
                pic = siteURL + pic;
            list.add(new Vod(vid, name, pic));
        }
        return Result.get().vod(list).page().string();
    }

    @Override
    public String detailContent(List<String> ids) throws Exception {
        Document doc = Jsoup.parse(OkHttp.string(ids.get(0), getHeader()));
        Elements sources = doc.select(".playlist-rect__container");
        Elements circuits = doc.select(".playlist-top__title.cms-playlist-name");
        StringBuilder vod_play_url = new StringBuilder();
        StringBuilder vod_play_from = new StringBuilder();
        for (int i = 0; i < sources.size(); i++) {
            String spanText = circuits.get(i).text();
            vod_play_from.append(spanText).append("$$$");
            Elements aElementArray = sources.get(i).select(".playlist-rect__col a");
            for (int j = 0; j < aElementArray.size(); j++) {
                Element a = aElementArray.get(j);
                String href = siteURL + a.attr("href");
                String text = a.text();
                vod_play_url.append(text).append("$").append(href);
                boolean notLastEpisode = j < aElementArray.size() - 1;
                vod_play_url.append(notLastEpisode ? "#" : "$$$");
            }
        }
        String name = doc.select(".playlist-intro__title").text();
        String description = doc.select(".playlist-top__update-info").text();
        Vod vod = new Vod();
        vod.setVodId(ids.get(0));
        vod.setVodName(name);
        vod.setVodContent(description);
        vod.setVodPlayFrom(vod_play_from.toString());
        vod.setVodPlayUrl(vod_play_url.toString());
        return Result.string(vod);
    }

    @Override
    public String searchContent(String key, boolean quick) throws Exception {
        String searchURL = siteURL + "/search/index.html?keyword=" + URLEncoder.encode(key);
        ;
        Document doc = Jsoup.parse(OkHttp.string(searchURL, getHeader()));
        List<Vod> list = new ArrayList<>();
        for (Element li : doc.select(".list_item")) {
            String vid = siteURL + li.select("strong a").attr("href");
            String name = li.select("strong a").attr("title");
            String pic = li.select(".figure_pic.lazy1").attr("src");
            if (!pic.startsWith("http"))
                pic = siteURL + pic;
            String remark = li.select(".figure_info").text();
            list.add(new Vod(vid, name, pic, remark));
        }
        return Result.string(list);
    }

    @Override
    public String playerContent(String flag, String id, List<String> vipFlags) throws Exception {
        Map<String, String> header = getHeader();
        String html = OkHttp.string(id, getHeader());
        Matcher matcher = Pattern.compile("var temCurVodFile = \"(.*?)\";").matcher(html);
        String realUrl = matcher.find() ? matcher.group(1) : "";
        return Result.get().url(realUrl).header(getHeader()).string();
    }
}