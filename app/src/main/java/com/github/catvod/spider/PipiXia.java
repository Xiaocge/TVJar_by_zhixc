package com.github.catvod.spider;

import android.content.Context;
import android.text.TextUtils;

import com.github.catvod.crawler.Spider;
import com.github.catvod.net.OkHttp;
import com.github.catvod.net.OkResult;
import com.github.catvod.utils.Util;
import com.github.catvod.utils.Utils;
import com.google.gson.Gson;
import com.github.catvod.bean.Class;
import com.github.catvod.bean.Vod;
import com.github.catvod.bean.Result;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileReader;
import java.net.URLEncoder;
import java.time.Instant;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class PipiXia extends Spider {

    private String siteURL = "http://aikun.tv";

    private Map<String, String> getHeader() {
        Map<String, String> header = new HashMap<>();
        header.put("User-Agent", Utils.CHROME);
        header.put("Referer", siteURL + "/");
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
        List<String> typeIds = Arrays.asList("1", "2", "3", "4");
        List<String> typeNames = Arrays.asList("剧集", "电影", "动漫", "综艺");
        for (int i = 0; i < typeIds.size(); i++) {
            classes.add(new Class(typeIds.get(i), typeNames.get(i)));
        }
        String f = "{\"1\": [{\"key\": \"class\", \"name\": \"剧情\", \"value\": [{\"n\": \"全部\", \"v\": \"\"}, {\"n\": \"动作片\", \"v\": \"cate_id=9&\"}, {\"n\": \"喜剧片\", \"v\": \"cate_id=10&\"}, {\"n\": \"爱情片\", \"v\": \"cate_id=11&\"}, {\"n\": \"恐怖片\", \"v\": \"cate_id=12&\"}, {\"n\": \"剧情片\", \"v\": \"cate_id=13&\"}, {\"n\": \"科幻片\", \"v\": \"cate_id=14&\"}, {\"n\": \"动画片\", \"v\": \"cate_id=17&\"}]}], \"2\": [{\"key\": \"class\", \"name\": \"剧情\", \"value\": [{\"n\": \"全部\", \"v\": \"\"}, {\"n\": \"国产剧\", \"v\": \"cate_id=22&\"}, {\"n\": \"香港剧\", \"v\": \"cate_id=23&\"}, {\"n\": \"台湾剧\", \"v\": \"cate_id=24&\"}, {\"n\": \"欧美剧\", \"v\": \"cate_id=25&\"}, {\"n\": \"日本剧\", \"v\": \"cate_id=26&\"}, {\"n\": \"韩国剧\", \"v\": \"cate_id=27&\"}, {\"n\": \"海外剧\", \"v\": \"cate_id=29&\"}]}]}";
        JSONObject filterConfig = new JSONObject(f);
        Document doc = Jsoup.parse(OkHttp.string(siteURL, getHeader()));
        ArrayList<Vod> list = new ArrayList<Vod>();
        for (Element li : doc.select(".public-list-div")) {
            String vid = siteURL + li.select("a").attr("href");
            String name = li.select("a").attr("title");
            String pic = li.select("a >div").attr("data-original");
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
        String type = extend.get("cateId") == null ? tid : extend.get("cateId");
        String apiUrl = siteURL
                + String.format("/index.php/api/vod");
        long time = Math.round(Instant.now().getEpochSecond());
        String uid = "DCC147D11943AF75";
        String key = Util.MD5(String.format("DS%d%s", time, uid));
        String params = String.format("type=%s&page=%s&time=%s&key=%s", type, pg, time, key);
        JSONObject object = new JSONObject(OkHttp.postFromString(apiUrl, params, getHeader()));

        List<Vod> list = new ArrayList<>();
        if ((Integer) object.get("code") != 1) {
            return Result.string(list);
        }
        JSONArray data = object.getJSONArray("list");
        for (int i = 0, len = data.length(); i < len; i++) {
            JSONObject item = data.getJSONObject(i);
            String vid = String.format("%s/v/%s.html", siteURL, item.get("vod_id"));
            String name = item.getString("vod_name");
            String pic = item.getString("vod_pic");
            if (!pic.startsWith("http"))
                pic = siteURL + "/" + pic;
            list.add(new Vod(vid, name, pic));
        }

        return Result.string(list);
    }

    @Override
    public String detailContent(List<String> ids) throws Exception {
        Document doc = Jsoup.parse(OkHttp.string(ids.get(0), getHeader()));
        Elements sources = doc.select(".anthology-list-box ul");
        Elements circuits = doc.select(".anthology-tab > .swiper-wrapper > a > i");
        StringBuilder vod_play_url = new StringBuilder();
        StringBuilder vod_play_from = new StringBuilder();
        for (int i = 0; i < sources.size(); i++) {
            String spanText = circuits.get(i).text();
            vod_play_from.append(spanText).append("$$$");
            Elements aElementArray = sources.get(i).select("li a");
            for (int j = 0; j < aElementArray.size(); j++) {
                Element a = aElementArray.get(j);
                String href = siteURL + a.attr("href");
                String text = a.text();
                vod_play_url.append(text).append("$").append(href);
                boolean notLastEpisode = j < aElementArray.size() - 1;
                vod_play_url.append(notLastEpisode ? "#" : "$$$");
            }
        }
        String name = doc.select(".slide-info-title").text();
        String description = doc.select(".switch-box").text();
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
        String html = OkHttp.string(id, getHeader());
        Matcher matcher = Pattern.compile("var temCurVodFile = \"(.*?)\";").matcher(html);
        String realUrl = matcher.find() ? matcher.group(1) : "";
        return Result.get().url(realUrl).header(getHeader()).string();
    }
}