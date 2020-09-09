package com.nowcoder.community;

import java.io.IOException;

public class WkTest {
    public static void main(String [] args){
        String cmd ="C:/Users/10295/Documents/java/wkhtmltopdf/bin/wkhtmltoimage --quality 75 https://www.baidu.com C:/Users/10295/Documents/javawork/data/wk-image/3.png";

        try {
            Runtime.getRuntime().exec(cmd);
            System.out.println("ok");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
