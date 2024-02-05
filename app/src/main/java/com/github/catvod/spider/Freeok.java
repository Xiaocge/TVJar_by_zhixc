package com.github.catvod.spider;

import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;
import okhttp3.*;

import com.github.catvod.bean.Class;
import com.github.catvod.bean.Result;
import com.github.catvod.bean.Vod;
import com.github.catvod.crawler.Spider;
import com.github.catvod.net.OkHttp;
import com.github.catvod.utils.Util;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Xiaocge
 *         Vodflix
 */
public class Freeok extends Spider {

    private String siteURL = "https://www.freeok.me";

    private Map<String, String> getHeader() {
        Map<String, String> header = new HashMap<>();
        header.put("User-Agent", Util.CHROME);
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
        List<String> typeIds = Arrays.asList("1", "2", "3", "4", "24");
        List<String> typeNames = Arrays.asList("电影", "电视剧", "动漫", "综艺", "解说");
        for (int i = 0; i < typeIds.size(); i++)
            classes.add(new Class(typeIds.get(i), typeNames.get(i)));
        Document doc = Jsoup.parse(OkHttp.string(siteURL, getHeader()));
        List<Vod> list = parseVodList(doc.select(".module-items a"));

        return Result.string(classes, list);
    }

    public List<Vod> parseVodList(Elements items) {
        ArrayList<Vod> list = new ArrayList<>();
        for (Element a : items) {
            String vid = siteURL + a.attr("href");
            String name = a.attr("title");
            String pic = a.select(".module-item-pic img").attr("data-original");
            if (!pic.startsWith("http"))
                pic = siteURL + pic;
            list.add(new Vod(vid, name, pic));
        }

        return list;
    }

    /**
     * 分类内容方法
     *
     * @param tid    影片分类id值，来自 homeContent 里面的 type_id 值
     * @param pg     第几页
     * @param filter 不用管这个参数
     * @param extend 用户已经选择的二级筛选数据
     * @return 返回字符串
     *         https://vidhub2.cc/vodshow/3--------4---.html
     *         https://vidhub2.cc/vodshow/1--------1---.html
     */
    @Override
    public String categoryContent(String tid, String pg, boolean filter, HashMap<String, String> extend)
            throws Exception {
        String cateUrl = siteURL + String.format("/vodshow/%s--------%s---.html", tid, pg);
        Document doc = Jsoup.parse(OkHttp.string(cateUrl, getHeader()));
        List<Vod> list = parseVodList(doc.select(".module-item"));
        int page = Integer.parseInt(pg), count = Integer.MAX_VALUE, limit = 40, total = Integer.MAX_VALUE;

        return Result.get().vod(list).page(page, count, limit, total).string();
    }

    @Override
    public String detailContent(List<String> ids) throws Exception {
        String html = OkHttp.string(ids.get(0), getHeader());
        Document doc = Jsoup.parse(html);
        String name = doc.select(".page-title").text();
        String pic = doc.select(".module-item-pic a").attr("title");
        String year = doc.select(".video-info-item").get(2).text(); // 影片年代
        String remark = doc.select(".video-info-item").get(4).text();
        String actor = doc.select(".video-info-item").get(1).select("a").text();
        String director = doc.select(".video-info-item").get(0).select("a").text();
        String description = doc.select(".video-info-content").text().trim();

        Elements playerList = doc.select(".module-blocklist .scroll-content > a");
        Elements circuits = doc.select(".module-tab-content .module-tab-item");
        Map<String, String> playMap = new LinkedHashMap<>();
        for (int i = 0; i < circuits.size(); i++) {
            String circuitName = circuits.get(i).select("span").text().trim();
            List<String> vodItems = new ArrayList<>();
            for (Element a : playerList) {
                String episodeUrl = siteURL + a.attr("href");
                String episodeName = a.select("span").text();
                vodItems.add(episodeName + "$" + episodeUrl);
            }
            if (vodItems.size() > 0)
                playMap.put(circuitName, TextUtils.join("#", vodItems));
        }

        Vod vod = new Vod();
        vod.setVodId(ids.get(0));
        vod.setVodName(name);
        vod.setVodPic(pic);
        vod.setVodYear(year);
        vod.setVodDirector(director);
        vod.setVodActor(actor);
        vod.setVodRemarks(remark);
        vod.setVodContent(description);
        vod.setVodPlayFrom(TextUtils.join("$$$", playMap.keySet()));
        vod.setVodPlayUrl(TextUtils.join("$$$", playMap.values()));
        return Result.string(vod);
    }

    /**
     * 正则获取字符串
     *
     * @param regexStr 正则表达式字符串
     * @param htmlStr  网页源码
     * @return 返回正则获取的字符串结果
     */
    private String getStrByRegex(String regexStr, String htmlStr) {
        Pattern pattern = Pattern.compile(regexStr, Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(htmlStr);
        if (matcher.find()) {
            return matcher.group(1)
                    .replaceAll("</?[^>]+>", "")
                    .replace("\n", "")
                    .replace("\t", "")
                    .trim();
        }
        return "";
    }

    /**
     * 搜索
     *
     * @param key 关键字/词
     * @return 返回值
     */
    @Override
    public String searchContent(String key, boolean quick) throws Exception {
        String searchUrl = siteURL + "/index.php/ajax/suggest?mid=1&wd=" + URLEncoder.encode(key) + "&limit=20";
        String html = OkHttp.string(searchUrl, getHeader());
        JSONArray jsonArray = new JSONObject(html).getJSONArray("list");
        List<Vod> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject item = jsonArray.getJSONObject(i);
            String vodId = "/detail/" + item.getInt("id") + ".html";
            String name = item.getString("name");
            String pic = item.getString("pic");
            list.add(new Vod(vodId, name, pic));
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

    public Response getHtml() throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("https://www.freeok.me/play/124424-3-1.html")
                .method("GET", null)
                .build();
        Response response = client.newCall(request).execute();

        return response;
    }
}
