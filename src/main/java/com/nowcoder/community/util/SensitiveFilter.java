package com.nowcoder.community.util;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Component
public class SensitiveFilter {

    //初始化
    private static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);


    //替换符号
    private static final String REPLACEMENT = "***";

    //根节点
    private TrieNode rootNode = new TrieNode();


    //在spring容器实例化这个类之后，有这个注解的方法会被调用(初始化前缀树)
    @PostConstruct
    public void init(){

        try(
                InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
                //将字节流换为字符流，使用buffreader批量读入
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        ){
                String keyword;
                while((keyword = bufferedReader.readLine()) != null){
                    //添加到前缀树
                    this.addKeyword(keyword);
                }
        }catch (IOException e){
            logger.error("加载敏感词文件失败:"+e.getMessage());
        }
    }
    //将  !一个!!  敏感词添加到前缀树
    private void addKeyword(String keyword){
        TrieNode tempNode = rootNode;
        for (int i = 0; i < keyword.length();i++){
            char c = keyword.charAt(i);
            TrieNode subNode = tempNode.getSubNode(c);
            if (subNode == null){
                subNode = new TrieNode();
                tempNode.addSubNode(c,subNode);
            }
            //指向子节点，进入下一轮
            tempNode = subNode;

            //设置结束标识
            if (i == keyword.length()-1){
                tempNode.setKeyword(true);
            }
        }
    }

    //过滤敏感词
    public String filter(String text){
        if (StringUtils.isBlank(text))
            return null;

        //指针1  指向前缀树
        TrieNode tempNode = rootNode;
        //指针2  指向text的开始
        int begin = 0;
        //指针3  指向正在检查
        int position = 0;
        //结果
        StringBuilder stringBuilder = new StringBuilder();

        while (begin < text.length()){
            if (position < text.length()) {
                Character c = text.charAt(position);

                //跳过特殊符号符号
                if (isSymbol(c)){
                    //是否是根节点 ,指针一指向根节点，则此符号不违法，把这个特殊字符放入最终结果
                    if (tempNode == rootNode){
                        stringBuilder.append(c);
                        ++begin;
                    }
                    //无论特殊符号在开头/中间，
                    ++position;
                    continue;
                }

                //检查下级节点
                tempNode = tempNode.getSubNode(c);
                if (tempNode ==null){
                    //以begin为开头的字符不是敏感词
                    stringBuilder.append(text.charAt(begin));
                    ++begin;
                    position = begin;
                    tempNode = rootNode;
                }else if (tempNode.isKeyword()){
                    stringBuilder.append(REPLACEMENT);
                    //
                    ++position;
                    begin = position;
                    tempNode = rootNode;
                }else {
                    position++;
                }
            }else {
                stringBuilder.append(text.charAt(begin));
                ++begin;
                position = begin;
                tempNode = rootNode;
            }
        }
        return stringBuilder.toString();
    }

    //判断是否符号
    private boolean isSymbol(Character c){
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80|| c > 0x9fff);
    }

    //前缀树
    private class TrieNode{

        //关键词结束标志
        private boolean keyword = false;
        //子节点 key是下级字符，value是下级节点
        private Map<Character ,TrieNode> subNodes = new HashMap<>();


        public boolean isKeyword() {
            return keyword;
        }

        public void setKeyword(boolean keyword) {
            this.keyword = keyword;
        }

        //添加子节点
        public void addSubNode(Character c, TrieNode node){
            subNodes.put(c,node);
        }
        //获取子节点
        public TrieNode getSubNode(Character c){
            return subNodes.get(c);
        }

    }
}
