package com.github.catvod.demo;

import android.content.Context;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.github.catvod.spider.PipiXia;
import com.github.catvod.spider.Ys23;

public class TestPipiXia {
        public static void main(String[] args) throws Exception {

                // String algorithm = "AES/CBC/PKCS5PADDING";
                // String keyText = "ssp_brush_cipher";
                // String ivText = "0000000000000000";
                // String plaintext =
                // "{\"mac\":\"00:DB:5D:DF:E5:38\",\"os\":\"android\",\"dpi\":\"720*1280\",\"devicetype\":\"pad\",\"c_ori\":0,\"serialno\":\"unknown\",\"oscode\":\"28\",\"osversion\":\"9\",\"imeiidfa\":\"\",\"model\":\"V1824A\",\"c_device\":\"vivo\",\"network\":1,\"carrieroperator\":0,\"sdkversion\":\"5.1.6.1\",\"pluginversion\":\"1144\",\"c_w\":720,\"c_h\":1280,\"c_pkgname\":\"app.pipixia.vip\",\"ua\":\"Mozilla\\/5.0
                // (Linux; Android 9; V1824A Build\\/PQ3A.190605.12141616; wv)
                // AppleWebKit\\/537.36 (KHTML, like Gecko) Version\\/4.0 Chrome\\/91.0.4472.114
                // Mobile
                // Safari\\/537.36\",\"density\":1.5,\"appversion\":\"3.8.18\",\"anid\":\"03c18b5c9935ba77\",\"imsi\":\"\",\"vapi\":\"2.9.1\",\"oaid\":\"\",\"devId\":\"03c18b5c9935ba77\",\"sspUid\":\"\",\"appChannelId\":\"\",\"appCustomData\":\"\",\"meid\":\"\",\"device_start_sec\":1705996674,\"country\":\"CN\",\"language\":\"zh\",\"physical_memory_byte\":18493493248,\"harddisk_size_byte\":30977060864,\"time_zone\":\"Asia\\/Shanghai\",\"syscpmtime\":1702541706000,\"cpnum\":4,\"rom_version\":\"9\",\"timestamp\":1705998913434,\"appid\":\"6874\",\"advplaceid\":\"12979\",\"reqnum\":2,\"muidtype\":\"1\",\"mode\":\"3\",\"sizeid\":202,\"sdkType\":0,\"isInit\":1,\"c_adtype\":1,\"pr_id_visited\":\"1371\",\"sessionId\":\"019351637218460b86dcd0959013d031\",\"requestId\":\"f1b61bb548214828b84d53be7b7ba2aa\"}";

                // byte[] keyBytes = keyText.getBytes(StandardCharsets.UTF_8);
                // byte[] ivBytes = ivText.getBytes(StandardCharsets.UTF_8);
                // SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");
                // IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);

                // Cipher cipher = Cipher.getInstance(algorithm);
                // cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);

                // byte[] encryptedBytes =
                // cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
                // String encryptedText = Base64.getEncoder().encodeToString(encryptedBytes);

                // System.out.println("Encrypted Text: " + encryptedText);

                ///////////////
                // String algorithm = "AES/CBC/PKCS5PADDING";
                // String keyText = "ssp_brush_cipher";
                // String ivText = "0000000000000000";
                // String encryptedHex =
                /////////////// "SKZZJME51F3E51B7E5427CD6306E7D019066799B161813BE237A6BE73BD4DD7527EE3D2BB0D397EE7717BD9905CB40B";

                // byte[] keyBytes = keyText.getBytes();
                // byte[] ivBytes = ivText.getBytes();
                // SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");
                // IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);

                // Cipher cipher = Cipher.getInstance(algorithm);
                // cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);

                // byte[] encryptedBytes = hexStringToByteArray(encryptedHex);
                // byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
                // String decryptedText = new String(decryptedBytes);

                // System.out.println("Decrypted Text: " + decryptedText);

                //////////////
                // String algorithm = "DES/CBC/PKCS5Padding";
                // String keyBase64 = "U1NQvVNES70=";
                // String ivBase64 = "EjRWeJCrze8=";
                // String encryptedHex =
                ////////////// "9B161813BE237A6BE73BD4DD7527EE3D2BB0D397EE7717BD9905CB4089EFC91F3B678F1984DEAB0BC4209B593B1A4337F43BF671DA519C87C0EAF6625057BDDE3E34CB9E5F402DB3223D78CC36912331A4DF3386DF5C54BD87D5EF2A7DF0B48D1C5099CE031EEFE2E31B9DACC5FFFDB24279128E2688C34AA315371671C534B4CECAD8CCC93415E59BD58E8F65796B3912721DC579E63924C814BA2FA340BB8C86398E4AD8DFA0DD8A3DA6D68FCF68E27F9B2CA4D14DA9B1E124B5BB051C67554FEF463F8B8FF36B339414D2BB5317EF89EC2A42F232F440CC3DC3F6EE3D5D6DE4F07B9C7D8EA3F950A0BDAE8BCFB1E54914EB99F100C65EDE96775FAA149DF6DC1440B502F884323F30F0DD2CB184F9C446E26265B78EC3EEA77DBEAD13305BBDDF5F27A999AF9D7EDC2A66EC09CAA5B08246A7A61E79D30A9939446C4EAEC3C8D788A634D07715DEB4C7DE08170D2B2CE4194E7E27510A662A9A9CFF2E2246100108021FA8D19BC3B99B00E9E5AA4BCD944DD61AC8420E228A2E107F13180C666F2135B4D7A6432DE9F32578B4E7F0D84C2EDB9975E15B8095083AE1DEBF8B";

                // byte[] keyBytes = Base64.getDecoder().decode(keyBase64);
                // byte[] ivBytes = Base64.getDecoder().decode(ivBase64);
                // SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "DES");
                // IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);

                // Cipher cipher = Cipher.getInstance(algorithm);
                // cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);

                // byte[] encryptedBytes = hexStringToByteArray(encryptedHex);
                // byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
                // String decryptedText = new String(decryptedBytes, StandardCharsets.UTF_8);

                // System.out.println("Decrypted Text: " + decryptedText);
                /////////////////

                // String encryptedText = "Your encrypted text"; // 要解密的密文
                // String key = "YourSecretKey"; // 密钥

                // byte[] encryptedBytes = Base64.getDecoder().decode(encryptedText);

                // Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
                // SecretKeySpec secretKey = new
                // SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
                // cipher.init(Cipher.DECRYPT_MODE, secretKey);

                // byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
                // String decryptedText = new String(decryptedBytes, StandardCharsets.UTF_8);

                // System.out.println("Decrypted Text: " + decryptedText);

                // PipiXia pipiXia = new PipiXia();
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
                // String s = pipiXia.searchContent("三大队", true);
                // System.out.println(s);

                // 播放内容数据测试
                PipiXia pipiXia = new PipiXia();
                String s = pipiXia.playerContent("",
                                "http://aikun.tv/p/115300-1-1.html", null);
                System.out.println(s);

        }

        // private static byte[] hexStringToByteArray(String hexString) {
        // int len = hexString.length();
        // byte[] data = new byte[len / 2];
        // for (int i = 0; i < len; i += 2) {
        // data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
        // + Character.digit(hexString.charAt(i + 1), 16));
        // }
        // return data;
        // }

        private static byte[] hexStringToByteArray(String hexString) {
                int len = hexString.length();
                byte[] data = new byte[len / 2];
                for (int i = 0; i < len; i += 2) {
                        data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                                        + Character.digit(hexString.charAt(i + 1), 16));
                }
                return data;
        }
}
