package com.nowcoder.community.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

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

}
