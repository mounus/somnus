package com.example.message;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class CommonUtils {
    /**
     * json对象转换成map对象
     * @param result
     * 			响应结果
     * @return map对象
     * */
    public static HashMap<String, Object> jsonToMap(String result) {
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        JsonParser parser = new JsonParser();
        try
        {
            JsonObject asJsonObject = parser.parse(result).getAsJsonObject();
            Set<Map.Entry<String, JsonElement>> entrySet = asJsonObject.entrySet();
            for (Map.Entry<String, JsonElement> m : entrySet) {
                hashMap.put(m.getKey(), m.getValue());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return hashMap;
    }
    /**
     * 将xml数据转换成map对象
     * @param xml
     * 			数据
     * @return Map 对象
     */
    public static HashMap<String, Object> xmlToMap(String xml) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        Document doc = null;
        try {
            doc = DocumentHelper.parseText(xml); // 将字符串转为XML
            Element rootElt = doc.getRootElement(); // 获取根节点
            for (Iterator i = rootElt.elementIterator(); i.hasNext();) {
                Element e = (Element) i.next();
                map.put(e.getName(), e.getText());
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}
