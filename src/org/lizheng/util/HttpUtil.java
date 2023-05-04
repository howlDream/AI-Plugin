package org.lizheng.util;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author zheng.li
 */
public class HttpUtil {


    public static String invokeApi(String urlString, Map<String,String> params) throws IOException {

        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        //设置超时间为3秒
        conn.setConnectTimeout(3*1000);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            conn.setRequestProperty(entry.getKey(),entry.getValue());
        }
        conn.connect();
        StringBuilder ret = new StringBuilder();
        // read response
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                ret.append(line);
            }
        } finally {
            conn.disconnect();
        }

        return ret.toString();
    }

    /**
     * 向服务端发送 get请求
     */
    public static String httpGet(String url) {
        String responseText = null;
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(url);
        try {
            CloseableHttpResponse response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                responseText =  EntityUtils.toString(entity);
//                Logs.getInstance("httpLog").setPrintConsole(false).write(url + " \n ret: " + responseText + "\n");
            }
            response.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭连接,释放资源
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return responseText;
    }

    public static void httpDownload(String url,String downloadPath) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(url);
        try {
            File file = new File(downloadPath);
            if (!file.exists()) {
                file.createNewFile();
            }
            CloseableHttpResponse response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                entity.writeTo(new FileOutputStream(file));
            }
            response.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭连接,释放资源
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String unzip(String filePath,String zipDir) {
        String name = "";
        try {
            BufferedOutputStream dest = null;
            BufferedInputStream is = null;
            ZipEntry entry;
            ZipFile zipfile = new ZipFile(filePath);

            Enumeration dir = zipfile.entries();
            if (!new File(zipDir).exists()) {
                new File(zipDir).mkdir();
            }
            while (dir.hasMoreElements()){
                entry = (ZipEntry) dir.nextElement();

                if( entry.isDirectory()){
                    name = entry.getName();
                    name = name.substring(0, name.length() - 1);
                    File fileObject = new File(zipDir + name);
                    fileObject.mkdir();
                }
            }

            Enumeration e = zipfile.entries();
            while (e.hasMoreElements()) {
                entry = (ZipEntry) e.nextElement();
                if (!entry.isDirectory()) {
                    is = new BufferedInputStream(zipfile.getInputStream(entry));
                    int count;
                    byte[] dataByte = new byte[1024];
                    File file = new File(zipDir+entry.getName());
                    if (!file.exists() && !file.createNewFile()) {
                        continue;
                    }
                    FileOutputStream fos = new FileOutputStream(file);
                    dest = new BufferedOutputStream(fos, 1024);
                    while ((count = is.read(dataByte, 0, 1024)) != -1) {
                        dest.write(dataByte, 0, count);
                    }
                    dest.flush();
                    dest.close();
                    is.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  name;
    }


    public static void main(String[] args) throws IOException {
//        String api = "http://192.168.10.13:8088/getDistlist";
//        httpGet(api);
//        httpDownload("http://192.168.10.13:8088/download/xbb-pro-dingtalk-front/master/local.zip","D:/local.zip");
//        unzip("D:/local.zip","D:/local/");
        FileUtils.deleteDirectory(new File("D:/simple-dingtalk/dist"));
        FileUtils.moveDirectory(new File("D:/local"),new File("D:/simple-dingtalk/dist"));
    }

}
