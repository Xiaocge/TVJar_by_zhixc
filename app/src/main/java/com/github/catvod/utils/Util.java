package com.github.catvod.utils;


import android.content.Context;


import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

    public static final Pattern RULE = Pattern.compile(
            "http((?!http).){12,}?\\.(m3u8|mp4|mkv|flv|mp3|m4a|aac)\\?.*|http((?!http).){12,}\\.(m3u8|mp4|mkv|flv|mp3|m4a|aac)|http((?!http).)*?video/tos*");
    public static final String CHROME = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/117.0.0.0 Safari/537.36";
    public static final String ACCEPT = "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7";
    public static final List<String> MEDIA = Arrays.asList("mp4", "mkv", "wmv", "flv", "avi", "iso", "mpg", "ts", "mp3",
            "aac", "flac", "m4a", "ape", "ogg");
    public static final List<String> SUB = Arrays.asList("srt", "ass", "ssa", "vtt");

    public static boolean isVip(String url) {
        List<String> hosts = Arrays.asList("iqiyi.com", "v.qq.com", "youku.com", "le.com", "tudou.com", "mgtv.com",
                "sohu.com", "acfun.cn", "bilibili.com", "baofeng.com", "pptv.com");
        for (String host : hosts)
            if (url.contains(host))
                return true;
        return false;
    }

    public static boolean isBlackVodUrl(String url) {
        List<String> hosts = Arrays.asList("973973.xyz", ".fit:");
        for (String host : hosts)
            if (url.contains(host))
                return true;
        return false;
    }

    public static boolean isVideoFormat(String url) {
        if (url.contains("url=http") || url.contains(".js") || url.contains(".css") || url.contains(".html"))
            return false;
        return RULE.matcher(url).find();
    }

    public static boolean isSub(String ext) {
        return SUB.contains(ext);
    }

    public static boolean isMedia(String text) {
        return MEDIA.contains(getExt(text));
    }

    public static String getExt(String name) {
        return name.contains(".") ? name.substring(name.lastIndexOf(".") + 1) : name;
    }

    public static String getSize(double size) {
        if (size <= 0)
            return "";
        if (size > 1024 * 1024 * 1024 * 1024.0) {
            size /= (1024 * 1024 * 1024 * 1024.0);
            return String.format(Locale.getDefault(), "%.2f%s", size, "TB");
        } else if (size > 1024 * 1024 * 1024.0) {
            size /= (1024 * 1024 * 1024.0);
            return String.format(Locale.getDefault(), "%.2f%s", size, "GB");
        } else if (size > 1024 * 1024.0) {
            size /= (1024 * 1024.0);
            return String.format(Locale.getDefault(), "%.2f%s", size, "MB");
        } else {
            size /= 1024.0;
            return String.format(Locale.getDefault(), "%.2f%s", size, "KB");
        }
    }


    public static String removeExt(String text) {
        return text.contains(".") ? text.substring(0, text.lastIndexOf(".")) : text;
    }

    public static String substring(String text) {
        return substring(text, 1);
    }

    public static String substring(String text, int num) {
        if (text != null && text.length() > num) {
            return text.substring(0, text.length() - num);
        } else {
            return text;
        }
    }

    public static String getVar(String data, String param) {
        for (String var : data.split("var"))
            if (var.contains(param))
                return checkVar(var);
        return "";
    }

    private static String checkVar(String var) {
        if (var.contains("'"))
            return var.split("'")[1];
        if (var.contains("\""))
            return var.split("\"")[1];
        return "";
    }

    public static String MD5(String src) {
        return MD5(src, "UTF-8");
    }

    public static String MD5(String src, String charset) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(src.getBytes(charset));
            BigInteger no = new BigInteger(1, messageDigest);
            StringBuilder sb = new StringBuilder(no.toString(16));
            while (sb.length() < 32)
                sb.insert(0, "0");
            return sb.toString().toLowerCase();
        } catch (Exception e) {
            return "";
        }
    }

    public static String getDigit(String text) {
        try {
            String newText = text;
            Matcher matcher = Pattern.compile(".*(1080|720|2160|4k|4K).*").matcher(text);
            if (matcher.find())
                newText = matcher.group(1) + " " + text;
            matcher = Pattern.compile("^([0-9]+)").matcher(text);
            if (matcher.find())
                newText = matcher.group(1) + " " + newText;
            return newText.replaceAll("\\D+", "") + " " + newText.replaceAll("\\d+", "");
        } catch (Exception e) {
            return "";
        }
    }
}
