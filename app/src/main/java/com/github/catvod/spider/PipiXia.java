package com.github.catvod.spider;

import android.content.Context;

import com.github.catvod.crawler.Spider;
import com.github.catvod.net.OkHttp;
import com.github.catvod.utils.Utils;
import com.github.catvod.bean.Class;
import com.github.catvod.bean.Vod;
import com.github.catvod.bean.Result;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

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
        return Result.string(classes, list);
    }

    @Override
    public String categoryContent(String tid, String pg, boolean filter, HashMap<String, String> extend)
            throws Exception {
        String type = extend.get("cateId") == null ? tid : extend.get("cateId");
        String apiUrl = siteURL
                + String.format("/index.php/api/vod");
        long time = Math.round(new Date().getTime() / 1000.0);
        String uid = "DCC147D11943AF75";
        String key = Utils.MD5(String.format("DS%d%s", time, uid));
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

        return Result.string(list, 9999);
    }

    @Override
    public String detailContent(List<String> ids) throws Exception {
        Document doc = Jsoup.parse(OkHttp.string(ids.get(0), getHeader()));
        Elements sources = doc.select(".anthology-list-box ul");
        Elements circuits = doc.select(".anthology-tab > .swiper-wrapper > a");
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
        String searchURL = siteURL + "/vodsearch.html?wd=" + URLEncoder.encode(key, "utf-8");
        ;
        Document doc = Jsoup.parse(OkHttp.string(searchURL, getHeader()));
        List<Vod> list = new ArrayList<>();
        for (Element li : doc.select(".search-box ")) {
            String vid = siteURL + li.select(".public-list-exp").attr("href");
            String name = li.select(".thumb-txt").text();
            String pic = li.select("a.public-list-exp div").attr("data-original");
            if (!pic.startsWith("http"))
                pic = siteURL + pic;
            String remark = li.select(".public-list-prb").text();
            list.add(new Vod(vid, name, pic, remark));
        }
        return Result.string(list);
    }

    @Override
    public String playerContent(String flag, String id, List<String> vipFlags) throws Exception {
        String html = OkHttp.string(id, getHeader());
        String regex = "var player_aaaa=([^<]+)</script>";
        Matcher matcher = Pattern.compile(regex).matcher(html);

        if (!matcher.find()) {
            return Result.error("出错, 稍后再试");
        }
        String group = matcher.group(1);
        JSONObject objPlayer = new JSONObject(group);
        String url = objPlayer.getString("url");
        String from = objPlayer.getString("from");
        Map<String, String> hashMap = getConfigMap();
        String matchUrl = hashMap.get(from);
        if (matchUrl == null || matchUrl.isEmpty()) {
            matchUrl = "http://play.shijie.chat/player/ec.php?code=qq&if=1&url=";
        }
        String SecUrl = matchUrl + url;
        String sechtml = OkHttp.string(SecUrl, getHeader());

        String regex2 = "let ConFig = ([^<]+)</script>";
        Matcher matcher2 = Pattern.compile(regex2).matcher(sechtml);

        if (!matcher2.find()) {
            return Result.error("出错, 稍后再试");
        }
        String group2 = matcher2.group(1).replace(",box = $(\"#player\"),", "");

        JSONObject objPlayer2 = new JSONObject(group2);
        String uid = objPlayer2.getJSONObject("config").getString("uid");
        String d = objPlayer2.getString("url");
        String realUrl = decrypt(d, uid);

        return Result.get().url(realUrl).header(getHeader()).string();
    }

    public static Map<String, String> getConfigMap() {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("qq", "http://play.shijie.chat/player/ec.php?code=qq&if=1&url=");
        hashMap.put("qiyi",
                "http://play.shijie.chat/player/ec.php?code=qiyi&if=1&url=");
        hashMap.put("youku",
                "http://play.shijie.chat/player/ec.php?code=youku&if=1&url=");
        hashMap.put("mgtv",
                "http://play.shijie.chat/player/ec.php?code=mgtv&if=1&url=");
        hashMap.put("NBY",
                "http://play.shijie.chat/player/ec.php?code=qq&if=1&url=");
        hashMap.put("SLNB",
                "http://play.shijie.chat/player/ec.php?code=qq&if=1&url=");
        hashMap.put("FYNB",
                "http://play.shijie.chat/player/ec.php?code=qq&if=1&url=");
        hashMap.put("SPA",
                "http://play.shijie.chat/player/ec.php?code=qq&if=1&url=");
        hashMap.put("SPB",
                "http://play.shijie.chat/player/ec.php?code=qq&if=1&url=");
        hashMap.put("kyB",
                "http://play.shijie.chat/player/ec.php?code=qq&if=1&url=");
        hashMap.put("JMZN",
                "http://play.shijie.chat/player/ec.php?code=qq&if=1&url=");
        hashMap.put("ZNJSON",
                "http://play.shijie.chat/player/ec.php?code=qq&if=1&url=");
        hashMap.put("znkan",
                "http://play.shijie.chat/player/ec.php?code=qq&if=1&url=");
        hashMap.put("bilibili",
                "http://play.shijie.chat/player/ec.php?code=qq&if=1&url=");
        hashMap.put("pptv", "http://play.shijie.chat/player/?url=");
        hashMap.put("letv", "http://play.shijie.chat/player/?url=");
        hashMap.put("sohu", "http://play.shijie.chat/player/?url=");
        hashMap.put("DJMP4",
                "http://play.shijie.chat/player/ec.php?code=qq&if=1&url=");
        hashMap.put("ChenXi",
                "http://play.shijie.chat/player/ec.php?code=qq&if=1&url=");
        hashMap.put("HT-",
                "http://play.shijie.chat/player/ec.php?code=qq&if=1&url=");
        hashMap.put("htys", "http://play.shijie.chat/player/?url=");

        return hashMap;
    }

    public static String decrypt(String d, String uid) throws Exception {
        String key = "2890" + uid + "tB959C";
        String iv = "2F131BE91247866E";

        byte[] encryptedData = Base64.getDecoder().decode(d);
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        byte[] ivBytes = iv.getBytes(StandardCharsets.UTF_8);

        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

        byte[] decryptedData = cipher.doFinal(encryptedData);

        return new String(decryptedData, StandardCharsets.UTF_8);
    }
}