package org.lizheng;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.utils.URLEncodedUtils;
import org.lizheng.util.HttpUtil;


/**
 * 有道翻译接口
 * return: {"type":"ZH_CN2EN","errorCode":0,"elapsedTime":0,"translateResult":[[{"src":"计算","tgt":"To calculate"}]]}
 * @author zheng.li
 */
public class TranslationYoudao {

    private static final String AUTO_TRANSLATE_URL = "http://fanyi.youdao.com/translate?&doctype=json&type=AUTO&i=";

    private static final String[] TRIMS = new String[]{"<strong>","</strong>","<tt>","</tt>","<i>","</i>","\\*","/"};

    public static String doTranslate(String text) {
        String encodeText = URLEncodedUtils.formatSegments(trim(text)).replaceFirst("/","");
        String encodeUrl = AUTO_TRANSLATE_URL + encodeText;
        String responseText = HttpUtil.httpGet(encodeUrl);
        JSONObject responseJson = JSONObject.parseObject(responseText);
        if (responseJson.getInteger("errorCode") != 0) {
            System.out.println(responseText);
            return "";
        }
        String translateResult = responseJson.getString("translateResult");
        JSONArray resultArray =  JSONArray.parseArray(translateResult);
        if (resultArray == null) {
            return "";
        }
        StringBuilder ret = new StringBuilder();
        for (Object o : resultArray) {
            JSONArray result = (JSONArray)o;
            for (Object o1 : result) {
                JSONObject innerResult = (JSONObject)o1;
                ret.append(innerResult.getString("tgt"));
            }
        }
        return ret.toString();
    }

    private static String trim(String text) {
        String ret = text.replaceAll("<p>"," ").replaceAll("</p>"," ");
        for (String trim : TRIMS) {
            ret = ret.replaceAll(trim,"");
        }
        return ret;
    }

}
