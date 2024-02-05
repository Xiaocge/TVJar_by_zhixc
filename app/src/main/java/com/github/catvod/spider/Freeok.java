package com.github.catvod.spider;

import android.content.Context;
import android.util.Base64;

import com.github.catvod.bean.Class;
import com.github.catvod.bean.Result;
import com.github.catvod.bean.Vod;
import com.github.catvod.crawler.Spider;
import com.github.catvod.net.OkHttp;
import com.github.catvod.utils.AES;
import com.github.catvod.utils.Util;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Qile
 */
public class Freeok extends Spider {

    private static String siteUrl = "https://www.freeok.me";

    private Map<String, String> getHeader() {
        Map<String, String> header = new HashMap<>();
        header.put("User-Agent", Util.CHROME);
        return header;
    }

    @Override
    public void init(Context context, String extend) throws Exception {
        super.init(context, extend);
        if (!extend.isEmpty()) {
            this.siteUrl = extend;
        }
    }

    @Override
    public String homeContent(boolean filter) throws Exception {
        List<Class> classes = new ArrayList<>();
        List<String> typeIds = Arrays.asList("1", "2", "3", "4", "12", "5");
        List<String> typeNames = Arrays.asList("电影", "电视剧", "动漫", "综艺", "短剧", "少儿");
        for (int i = 0; i < typeIds.size(); i++)
            classes.add(new Class(typeIds.get(i), typeNames.get(i)));
        String f = "{\"1\": [{\"key\": \"class\", \"name\": \"剧情\", \"value\": [{\"n\": \"全部\", \"v\": \"\"},  {\"n\": \"谍战\", \"v\": \"谍战\"}, {\"n\": \"剧情\", \"v\": \"剧情\"}, {\"n\": \"科幻\", \"v\": \"科幻\"}, {\"n\": \"动作\", \"v\": \"动作\"}, {\"n\": \"喜剧\", \"v\": \"喜剧\"}, {\"n\": \"爱情\", \"v\": \"爱情\"}, {\"n\": \"冒险\", \"v\": \"冒险\"}, {\"n\": \"歌舞\", \"v\": \"歌舞\"},  {\"n\": \"奇幻\", \"v\": \"奇幻\"}, {\"n\": \"动画\", \"v\": \"动画\"}, {\"n\": \"恐怖\", \"v\": \"恐怖\"}, {\"n\": \"惊悚\", \"v\": \"惊悚\"}, {\"n\": \"丧尸\", \"v\": \"丧尸\"}, {\"n\": \"战争\", \"v\": \"战争\"}, {\"n\": \"传记\", \"v\": \"传记\"}, {\"n\": \"纪录\", \"v\": \"纪录\"}, {\"n\": \"犯罪\", \"v\": \"犯罪\"}, {\"n\": \"悬疑\", \"v\": \"悬疑\"}, {\"n\": \"历史\", \"v\": \"历史\"}, {\"n\": \"灾难\", \"v\": \"灾难\"}, {\"n\": \"其他\", \"v\": \"其他\"}]}, {\"key\": \"area\", \"name\": \"地区\", \"value\": [{\"n\": \"全部\", \"v\": \"\"}, {\"n\": \"中国大陆\", \"v\": \"中国大陆\"}, {\"n\": \"中国香港\", \"v\": \"中国香港\"}, {\"n\": \"中国台湾\", \"v\": \"中国台湾\"}, {\"n\": \"美国\", \"v\": \"美国\"}, {\"n\": \"法国\", \"v\": \"法国\"}, {\"n\": \"英国\", \"v\": \"英国\"}, {\"n\": \"日本\", \"v\": \"日本\"}, {\"n\": \"韩国\", \"v\": \"韩国\"}, {\"n\": \"德国\", \"v\": \"德国\"}, {\"n\": \"泰国\", \"v\": \"泰国\"}, {\"n\": \"印度\", \"v\": \"印度\"}, {\"n\": \"意大利\", \"v\": \"意大利\"}, {\"n\": \"西班牙\", \"v\": \"西班牙\"}, {\"n\": \"加拿大\", \"v\": \"加拿大\"}, {\"n\": \"其他\", \"v\": \"其他\"}]}, {\"key\": \"year\", \"name\": \"年份\", \"value\": [{\"n\": \"全部\", \"v\": \"\"}, {\"n\": \"2024\", \"v\": \"2024\"}, {\"n\": \"2023\", \"v\": \"2023\"}, {\"n\": \"2022\", \"v\": \"2022\"}, {\"n\": \"2021\", \"v\": \"2021\"}, {\"n\": \"2020\", \"v\": \"2020\"}, {\"n\": \"2019\", \"v\": \"2019\"}, {\"n\": \"2018\", \"v\": \"2018\"}, {\"n\": \"2017\", \"v\": \"2017\"}, {\"n\": \"2016\", \"v\": \"2016\"}, {\"n\": \"2015\", \"v\": \"2015\"}, {\"n\": \"2014\", \"v\": \"2014\"}, {\"n\": \"2013\", \"v\": \"2013\"}, {\"n\": \"2012\", \"v\": \"2012\"}, {\"n\": \"2011\", \"v\": \"2011\"}, {\"n\": \"2010\", \"v\": \"2010\"}]}, {\"key\": \"by\", \"name\": \"排序\", \"value\": [{\"n\": \"时间\", \"v\": \"time\"}, {\"n\": \"人气\", \"v\": \"hits\"}, {\"n\": \"评分\", \"v\": \"score\"}]}], \"2\": [{\"key\": \"cateId\", \"name\": \"类型\", \"value\": [{\"n\": \"全部\", \"v\": \"2\"}, {\"n\": \"国产剧\", \"v\": \"6\"}, {\"n\": \"港台剧\", \"v\": \"7\"}, {\"n\": \"日韩剧\", \"v\": \"8\"}, {\"n\": \"欧美剧\", \"v\": \"9\"}, {\"n\": \"海外剧\", \"v\": \"10\"}]}, {\"key\": \"class\", \"name\": \"剧情\", \"value\": [{\"n\": \"全部\", \"v\": \"\"}, {\"n\": \"古装\", \"v\": \"古装\"}, {\"n\": \"战争\", \"v\": \"战争\"}, {\"n\": \"青春偶像\", \"v\": \"青春偶像\"}, {\"n\": \"喜剧\", \"v\": \"喜剧\"}, {\"n\": \"家庭\", \"v\": \"家庭\"}, {\"n\": \"犯罪\", \"v\": \"犯罪\"}, {\"n\": \"动作\", \"v\": \"动作\"}, {\"n\": \"奇幻\", \"v\": \"奇幻\"}, {\"n\": \"剧情\", \"v\": \"剧情\"}, {\"n\": \"历史\", \"v\": \"历史\"}, {\"n\": \"经典\", \"v\": \"经典\"}, {\"n\": \"乡村\", \"v\": \"乡村\"}, {\"n\": \"情景\", \"v\": \"情景\"}, {\"n\": \"商战\", \"v\": \"商战\"}, {\"n\": \"网剧\", \"v\": \"网剧\"}, {\"n\": \"其他\", \"v\": \"其他\"}]}, {\"key\": \"area\", \"name\": \"地区\", \"value\": [{\"n\": \"全部\", \"v\": \"\"}, {\"n\": \"中国大陆\", \"v\": \"中国大陆\"}, {\"n\": \"中国台湾\", \"v\": \"中国台湾\"}, {\"n\": \"中国香港\", \"v\": \"中国香港\"}, {\"n\": \"韩国\", \"v\": \"韩国\"}, {\"n\": \"日本\", \"v\": \"日本\"}, {\"n\": \"美国\", \"v\": \"美国\"}, {\"n\": \"泰国\", \"v\": \"泰国\"}, {\"n\": \"英国\", \"v\": \"英国\"}, {\"n\": \"英国\", \"v\": \"英国\"}, {\"n\": \"其他\", \"v\": \"其他\"}]}, {\"key\": \"year\", \"name\": \"年份\", \"value\": [{\"n\": \"全部\", \"v\": \"\"}, {\"n\": \"2024\", \"v\": \"2024\"}, {\"n\": \"2023\", \"v\": \"2023\"}, {\"n\": \"2022\", \"v\": \"2022\"}, {\"n\": \"2021\", \"v\": \"2021\"}, {\"n\": \"2020\", \"v\": \"2020\"}, {\"n\": \"2019\", \"v\": \"2019\"}, {\"n\": \"2018\", \"v\": \"2018\"}, {\"n\": \"2017\", \"v\": \"2017\"}, {\"n\": \"2016\", \"v\": \"2016\"}, {\"n\": \"2015\", \"v\": \"2015\"}, {\"n\": \"2014\", \"v\": \"2014\"}, {\"n\": \"2013\", \"v\": \"2013\"}, {\"n\": \"2012\", \"v\": \"2012\"}, {\"n\": \"2011\", \"v\": \"2011\"}, {\"n\": \"2010\", \"v\": \"2010\"}, {\"n\": \"2009\", \"v\": \"2009\"}, {\"n\": \"2008\", \"v\": \"2008\"}, {\"n\": \"2006\", \"v\": \"2006\"}, {\"n\": \"2005\", \"v\": \"2005\"}, {\"n\": \"2004\", \"v\": \"2004\"}]}, {\"key\": \"by\", \"name\": \"排序\", \"value\": [{\"n\": \"时间\", \"v\": \"time\"}, {\"n\": \"人气\", \"v\": \"hits\"}, {\"n\": \"评分\", \"v\": \"score\"}]}],\"3\": [{\"key\": \"class\", \"name\": \"剧情\", \"value\": [{\"n\": \"全部\", \"v\": \"\"}, {\"n\": \"情感\", \"v\": \"情感\"}, {\"n\": \"科幻\", \"v\": \"科幻\"}, {\"n\": \"热血\", \"v\": \"热血\"}, {\"n\": \"推理\", \"v\": \"推理\"}, {\"n\": \"搞笑\", \"v\": \"搞笑\"}, {\"n\": \"冒险\", \"v\": \"冒险\"}, {\"n\": \"萝莉\", \"v\": \"萝莉\"}, {\"n\": \"校园\", \"v\": \"校园\"}, {\"n\": \"动作\", \"v\": \"动作\"}, {\"n\": \"机战\", \"v\": \"机战\"}, {\"n\": \"运动\", \"v\": \"运动\"}, {\"n\": \"战争\", \"v\": \"战争\"}, {\"n\": \"少年\", \"v\": \"少年\"}, {\"n\": \"少女\", \"v\": \"少女\"}, {\"n\": \"社会\", \"v\": \"社会\"}, {\"n\": \"原创\", \"v\": \"原创\"}, {\"n\": \"亲子\", \"v\": \"亲子\"}, {\"n\": \"益智\", \"v\": \"益智\"}, {\"n\": \"励志\", \"v\": \"励志\"}, {\"n\": \"其他\", \"v\": \"其他\"}]}, {\"key\": \"area\", \"name\": \"地区\", \"value\": [{\"n\": \"全部\", \"v\": \"\"}, {\"n\": \"中国大陆\", \"v\": \"中国大陆\"}, {\"n\": \"中国台湾\", \"v\": \"中国台湾\"}, {\"n\": \"中国香港\", \"v\": \"中国香港\"}, {\"n\": \"韩国\", \"v\": \"韩国\"}, {\"n\": \"日本\", \"v\": \"日本\"}, {\"n\": \"美国\", \"v\": \"美国\"}, {\"n\": \"泰国\", \"v\": \"泰国\"}, {\"n\": \"英国\", \"v\": \"英国\"}, {\"n\": \"英国\", \"v\": \"英国\"}, {\"n\": \"其他\", \"v\": \"其他\"}]}, {\"key\": \"year\", \"name\": \"年份\", \"value\": [{\"n\": \"全部\", \"v\": \"\"}, {\"n\": \"2024\", \"v\": \"2024\"}, {\"n\": \"2023\", \"v\": \"2023\"}, {\"n\": \"2022\", \"v\": \"2022\"}, {\"n\": \"2021\", \"v\": \"2021\"}, {\"n\": \"2020\", \"v\": \"2020\"}, {\"n\": \"2019\", \"v\": \"2019\"}, {\"n\": \"2018\", \"v\": \"2018\"}, {\"n\": \"2017\", \"v\": \"2017\"}, {\"n\": \"2016\", \"v\": \"2016\"}, {\"n\": \"2015\", \"v\": \"2015\"}, {\"n\": \"2014\", \"v\": \"2014\"}, {\"n\": \"2013\", \"v\": \"2013\"}, {\"n\": \"2012\", \"v\": \"2012\"}, {\"n\": \"2011\", \"v\": \"2011\"}, {\"n\": \"2010\", \"v\": \"2010\"}, {\"n\": \"2009\", \"v\": \"2009\"}, {\"n\": \"2008\", \"v\": \"2008\"}, {\"n\": \"2006\", \"v\": \"2006\"}, {\"n\": \"2005\", \"v\": \"2005\"}, {\"n\": \"2004\", \"v\": \"2004\"}]}, {\"key\": \"by\", \"name\": \"排序\", \"value\": [{\"n\": \"时间\", \"v\": \"time\"}, {\"n\": \"人气\", \"v\": \"hits\"}, {\"n\": \"评分\", \"v\": \"score\"}]}], \"4\": [{\"key\": \"class\", \"name\": \"剧情\", \"value\": [{\"n\": \"全部\", \"v\": \"\"}, {\"n\": \"选秀\", \"v\": \"选秀\"}, {\"n\": \"情感\", \"v\": \"情感\"}, {\"n\": \"访谈\", \"v\": \"访谈\"}, {\"n\": \"播报\", \"v\": \"播报\"}, {\"n\": \"YouTube\", \"v\": \"YouTube\"}, {\"n\": \"音乐\", \"v\": \"音乐\"}, {\"n\": \"脱口秀\", \"v\": \"脱口秀\"}, {\"n\": \"纪实\", \"v\": \"纪实\"}, {\"n\": \"曲艺\", \"v\": \"曲艺\"}, {\"n\": \"生活\", \"v\": \"生活\"}, {\"n\": \"真人秀\", \"v\": \"真人秀\"}, {\"n\": \"财经\", \"v\": \"财经\"}, {\"n\": \"八卦\", \"v\": \"八卦\"}]}, {\"key\": \"area\", \"name\": \"地区\", \"value\": [{\"n\": \"全部\", \"v\": \"\"}, {\"n\": \"中国大陆\", \"v\": \"中国大陆\"}, {\"n\": \"中国台湾\", \"v\": \"中国台湾\"}, {\"n\": \"中国香港\", \"v\": \"中国香港\"}, {\"n\": \"韩国\", \"v\": \"韩国\"}, {\"n\": \"日本\", \"v\": \"日本\"}, {\"n\": \"美国\", \"v\": \"美国\"}, {\"n\": \"泰国\", \"v\": \"泰国\"}, {\"n\": \"英国\", \"v\": \"英国\"}, {\"n\": \"其他\", \"v\": \"其他\"}]}, {\"key\": \"year\", \"name\": \"年份\", \"value\": [{\"n\": \"全部\", \"v\": \"\"}, {\"n\": \"2024\", \"v\": \"2024\"}, {\"n\": \"2023\", \"v\": \"2023\"}, {\"n\": \"2022\", \"v\": \"2022\"}, {\"n\": \"2021\", \"v\": \"2021\"}, {\"n\": \"2020\", \"v\": \"2020\"}, {\"n\": \"2019\", \"v\": \"2019\"}, {\"n\": \"2018\", \"v\": \"2018\"}, {\"n\": \"2017\", \"v\": \"2017\"}, {\"n\": \"2016\", \"v\": \"2016\"}, {\"n\": \"2015\", \"v\": \"2015\"}, {\"n\": \"2014\", \"v\": \"2014\"}, {\"n\": \"2013\", \"v\": \"2013\"}, {\"n\": \"2012\", \"v\": \"2012\"}, {\"n\": \"2011\", \"v\": \"2011\"}, {\"n\": \"2010\", \"v\": \"2010\"}, {\"n\": \"2009\", \"v\": \"2009\"}, {\"n\": \"2008\", \"v\": \"2008\"}, {\"n\": \"2006\", \"v\": \"2006\"}, {\"n\": \"2005\", \"v\": \"2005\"}, {\"n\": \"2004\", \"v\": \"2004\"}]}, {\"key\": \"by\", \"name\": \"排序\", \"value\": [{\"n\": \"时间\", \"v\": \"time\"}, {\"n\": \"人气\", \"v\": \"hits\"}, {\"n\": \"评分\", \"v\": \"score\"}]}]}";
        JSONObject filterConfig = new JSONObject(f);
        // List 首页推荐
        Document doc = Jsoup.parse(OkHttp.string(siteUrl, getHeader()));
        List<Vod> list = new ArrayList<>();
        for (Element li : doc.select(".module-items .module-item")) {
            String vid = siteUrl + li.attr("href");
            String name = li.attr("title");
            String pic = li.select("img").attr("data-original");
            String remark = li.select("[class=module-item-note]").text();
            list.add(new Vod(vid, name, pic, remark));
        }
        return Result.string(classes, list, filterConfig);
    }

    @Override
    public String categoryContent(String tid, String pg, boolean filter, HashMap<String, String> extend)
            throws Exception {
        HashMap<String, String> ext = new HashMap<>();
        if (extend != null && extend.size() > 0)
            ext.putAll(extend);
        String cateId = ext.get("cateId") == null ? tid : ext.get("cateId");
        String area = ext.get("area") == null ? "" : ext.get("area");
        String year = ext.get("year") == null ? "" : ext.get("year");
        String by = ext.get("by") == null ? "" : ext.get("by");
        String classType = ext.get("class") == null ? "" : ext.get("class");
        String vodType = siteUrl
                + Jsoup.parse(OkHttp.string(siteUrl, getHeader())).select("a.links").eq(1).attr("href");
        String vodShow = Jsoup.parse(OkHttp.string(vodType, getHeader())).select("a.module-heading-tab-link")
                .attr("href").split("/")[1];
        String cateUrl = siteUrl
                + String.format("/" + vodShow + "/%s-%s-%s-%s-----%s---%s.html", cateId, area, by, classType, pg, year);
        Document doc = Jsoup.parse(OkHttp.string(cateUrl, getHeader()));
        List<Vod> list = new ArrayList<>();
        for (Element li : doc.select(".module .module-item")) {
            String vid = siteUrl + li.attr("href");
            String name = li.attr("title");
            String pic = li.select("img").attr("data-original");
            String remark = li.select("[class=module-item-note]").text();
            list.add(new Vod(vid, name, pic, remark));
        }
        return Result.string(list);
    }

    @Override
    public String detailContent(List<String> ids) throws Exception {
        String detailUrl = ids.get(0);
        String content = OkHttp.string(detailUrl, getHeader());
        Document doc = Jsoup.parse(content);
        Elements sources = doc.select(".module-play-list"); // 线路
        Elements circuits = doc.select(".module-tab-item"); // 播放源标题
        StringBuilder vod_play_url = new StringBuilder(); // 线路/播放源 里面的各集的播放页面链接
        StringBuilder vod_play_from = new StringBuilder(); // 线路 / 播放源标题
        for (int i = 0; i < sources.size(); i++) {
            String spanText = circuits.get(i).select("span").text();
            spanText = spanText.replaceAll("OK无广", "OK无广").replaceAll("超清频道①", "索尼资源").replaceAll("超清频道②", "快看资源");
            if (spanText.contains("夸克4K") || spanText.contains("备用频道") || spanText.contains("高清频道")) {
                continue;
            }
            String smallText = circuits.get(i).select("small").text();
            String playFromText = spanText + "(共" + smallText + "集)";
            vod_play_from.append(playFromText).append("$$$");
            Elements aElementArray = sources.get(i).select("a");
            for (int j = 0; j < aElementArray.size(); j++) {
                Element a = aElementArray.get(j);
                String href = siteUrl + a.attr("href");
                String text = a.text();
                vod_play_url.append(text).append("$").append(href);
                boolean notLastEpisode = j < aElementArray.size() - 1;
                vod_play_url.append(notLastEpisode ? "#" : "$$$");
            }
        }

        String title = doc.select(".module-info-heading")
                .get(0)
                .getElementsByTag("h1").text();
        String pic = doc.select(".module-info-poster")
                .get(0)
                .select("img")
                .attr("data-original");
        Elements elements = doc.select(".module-info-heading").select(".module-info-tag-link");
        String classifyName = ""; // 影片类型
        String year = ""; // 影片年代
        String area = ""; // 地区
        if (elements.size() >= 3) {
            classifyName = elements.get(2).select("a").text();
            year = elements.get(0).select("a").text();
            area = elements.get(1).select("a").text();
        }
        Elements moduleInfoItems = doc.select(".module-info-items").select(".module-info-item");
        String remark = ""; // 备注
        String actor = ""; // 演员
        String director = ""; // 导演
        if (moduleInfoItems.size() >= 6) {
            remark = moduleInfoItems.get(4).select(".module-info-item-content").text();
            actor = moduleInfoItems.get(3).select("a").text();
            director = moduleInfoItems.get(1).select("a").text();
        }
        String brief = doc.select(".module-info-introduction-content").text();
        Vod vod = new Vod();
        vod.setVodId(ids.get(0));
        vod.setVodPic(pic);
        vod.setVodYear(year);
        vod.setVodName(title);
        vod.setVodArea(area);
        vod.setVodActor(actor);
        vod.setVodRemarks(remark);
        vod.setVodContent(brief);
        vod.setVodDirector(director);
        vod.setTypeName(classifyName);
        vod.setVodPlayFrom(vod_play_from.toString());
        vod.setVodPlayUrl(vod_play_url.toString());
        return Result.string(vod);
    }

    @Override
    public String searchContent(String key, boolean quick) throws Exception {
        String searchUrl = siteUrl + "/index.php/ajax/suggest.html?mid=1&wd=" + URLEncoder.encode(key);
        String content = OkHttp.string(searchUrl, getHeader());
        JSONArray lists = new JSONObject(content).getJSONArray("list");
        List<Vod> list = new ArrayList<>();
        for (int i = 0; i < lists.length(); i++) {
            JSONObject item = lists.getJSONObject(i);
            String vid = siteUrl + "/vod-detail/" + item.getInt("id") + ".html";
            String name = item.getString("name");
            String pic = item.getString("pic");
            list.add(new Vod(vid, name, pic));
        }
        return Result.string(list);
    }

    @Override
    public String playerContent(String flag, String id, List<String> vipFlags) throws Exception {
        // Response html = getHtml();
        String html = OkHttp.string(id, getHeader());
        String regex = "var player_aaaa=([^<]+)</script>";
        Matcher matcher = Pattern.compile(regex).matcher(html);
        if (!matcher.find()) {
            return "";
        }
        JSONObject jsonObject = new JSONObject(matcher.group(1));
        Integer encrypt = jsonObject.getInt("encrypt");
        String encodeUrl = jsonObject.getString("url");
        String realUrl = "";
        if (encrypt == 1) {
            realUrl = URLDecoder.decode(encodeUrl);
        } else if (encrypt == 2) {
            byte[] decodedBytes = Base64.decode(encodeUrl, Base64.DEFAULT);
            realUrl = URLDecoder.decode(new String(decodedBytes), "UTF-8");
        }

        return Result.get().header(getHeader()).url(realUrl).string();
    }
}
