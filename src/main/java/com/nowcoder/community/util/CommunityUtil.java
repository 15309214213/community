package com.nowcoder.community.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CommunityUtil {
    //生成随机字符串
    public static String generateUUID(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    //md5加密 （只能加密无法解密，因此会给原始密码上加入一些随机字符串(salt)然后再加密，这样会更安全)
    // heollo-》123456
    //hello+abd=》12345698786657
    public static String md5(String key){
        if(StringUtils.isAllBlank(key)){
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }



    //json demo
    public static String getJSONString(int code, String msg , Map<String , Object> map){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code" , code);
        jsonObject.put("msg" , msg);
        if (map != null){
            for (String key : map.keySet()){
                jsonObject.put(key , map);
            }
        }
        return jsonObject.toJSONString();
    }

    public static String getJSONString(int code , String msg){
        return getJSONString(code , msg , null);
    }
    public static String getJSONString(int code){
        return getJSONString(code , null , null);
    }
    public static void main(String[] args ){
        Map<String , Object> map = new HashMap<>();
        map.put("name","lisi");
        map.put("age",88);
        System.out.println(getJSONString(0 , "ok" , map));
        System.out.println(getJSONString(0 , "ok" ));
        System.out.println(getJSONString(0 ));
    }

}
