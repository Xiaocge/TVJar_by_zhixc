package com.github.catvod.spider;

import android.content.Context;
import android.text.TextUtils;

import com.github.catvod.bean.Class;
import com.github.catvod.bean.Result;
import com.github.catvod.bean.Vod;
import com.github.catvod.crawler.Spider;
import com.github.catvod.net.OkHttp;
import com.github.catvod.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author zhixc
 *         Vodflix
 */
public class Vidhub2 extends Spider {

    private String siteURL = "https://vidhub2.cc";

    private Map<String, String> getHeader() {
        Map<String, String> header = new HashMap<>();
        header.put("User-Agent", Utils.CHROME);
        header.put("Referer", siteURL + "/");
        return header;
    }

    /**
     * 爬虫代码初始化
     *
     * @param context 上下文对象
     * @param extend  配置文件的 ext 参数
     */
    @Override
    public void init(Context context, String extend) throws Exception {
        super.init(context, extend);
        // 域名经常性发生变化，通过外部配置文件传入，可以方便修改
        if (extend.endsWith("/"))
            extend = extend.substring(0, extend.lastIndexOf("/"));
        siteURL = extend;
    }

    /**
     * 首页内容初始化，主要要完成分类id和值、二级筛选等，
     * 也可以在这个方法里面完成首页推荐视频获取
     *
     * @param filter true: 开启二级筛选 false:关闭
     * @return 返回字符串
     */
    @Override
    public String homeContent(boolean filter) throws Exception {
        List<Class> classes = new ArrayList<>();
        List<String> typeIds = Arrays.asList("1", "2", "3", "4");
        List<String> typeNames = Arrays.asList("电影", "连续剧", "综艺", "动漫");
        for (int i = 0; i < typeIds.size(); i++)
            classes.add(new Class(typeIds.get(i), typeNames.get(i)));
        Document doc = Jsoup.parse(OkHttp.string(siteURL, getHeader()));
        List<Vod> list = parseVodList(doc.select(".module-item"));

        return Result.string(classes, list);
    }

    public List<Vod> parseVodList(Elements items) {
        ArrayList<Vod> list = new ArrayList<>();
        for (Element li : items) {
            String vid = siteURL + li.select(".module-item-titlebox >a").attr("href");
            String name = li.select(".module-item-titlebox >a").attr("title");
            String pic = li.select(".module-item-pic img").attr("data-src");
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

        Elements playerList = doc.select(".scroll-box .scroll-content");
        Elements circuits = doc.select(".module-tab-content");
        Map<String, String> playMap = new LinkedHashMap<>();
        for (int i = 0; i < playerList.size(); i++) {
            String circuitName = circuits.get(i).select("span").text();
            List<String> vodItems = new ArrayList<>();
            for (Element a : playerList.get(i).select("a")) {
                String episodeUrl = siteURL + a.attr("href");
                String episodeName = a.attr("title");
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
        String playPageUrl = siteURL + id;
        Document doc = Jsoup.parse(OkHttp.string(playPageUrl, getHeader()));
        String playerIfUri = doc.select("#player_if").attr("src");
        String html = OkHttp.string(siteURL + playerIfUri, getHeader());

        Pattern pattern = Pattern.compile("(?<=var config =)[\\s\\S]*?(?=})");
        Matcher matcher = pattern.matcher(html);
        String url = "";
        if (!matcher.find()) {
            return "";
        }
        JSONObject config = new JSONObject(matcher.group(0) + "}");
        url = config.getString("url");

        Pattern pattern2 = Pattern.compile("(?<=var bt_token = )[\\s\\S]*?(?=;)");
        Matcher matcher2 = pattern2.matcher(html);
        if (!matcher2.find()) {
            return "";
        }
        String btString = matcher2.group(0);
        String btToken = btString.substring(1, btString.length() - 1);
        String key = getKey(btToken);
        String iv = getIv("QGWj00YLpJaENZ6U");

        String decrypt = decrypt(url, key, iv);

        return Result.get().header(getHeader()).url(decrypt).string();
    }

    public static String decrypt(String data, String key, String iv) throws Exception {
        byte[] encryptedData = Base64.getDecoder().decode(data);
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        byte[] ivBytes = iv.getBytes();

        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

        byte[] decryptedData = cipher.doFinal(encryptedData);

        return new String(decryptedData, StandardCharsets.UTF_8);
    }

    public String getKey(String btToken) {
        String format = String.format("%s%s", btToken, "MaxwjxiifC2MWAD8");
        String md5 = Utils.MD5(format).substring(0x6, 0x6 + 0x10);
        return md5;
    }

    public String getIv(String xx) {
        String md5 = Utils.MD5(xx).substring(0x3, 0x3 + 0x10);
        return md5;
    }
}
