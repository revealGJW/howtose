package cn.revealing.howtose.services;

import org.apache.commons.lang.CharUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;

/**
 * Created by GJW on 2017/12/5.
 */

@Service
public class SensitiveService implements InitializingBean{
    @Override
    public void afterPropertiesSet() throws Exception {
        try{
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("SensitiveWords.txt");
            InputStreamReader reader = new InputStreamReader(is);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String lineTxt;
            while((lineTxt = bufferedReader.readLine())!= null) {
                addWord(lineTxt.trim());
            }
            reader.close();
        }catch(Exception e){
            LOGGER.error("读取敏感词文件失败", e.getMessage());
        }
    }

    public static final Logger LOGGER = LoggerFactory.getLogger(SensitiveService.class);
    private class TrieNode {
        private boolean end = false;
        private Map<Character, TrieNode> subNodes = new HashMap<Character, TrieNode>();

        public void addSubNode(Character key, TrieNode trieNode) {
            subNodes.put(key, trieNode);
        }

        public TrieNode getSubNode(Character key) {
            return subNodes.get(key);
        }

        public boolean isEnd() {
            return end;
        }

        public void setEnd(boolean end) {
            this.end = end;
        }

    }

    public TrieNode rootNode = new TrieNode();

    public boolean isSymbol(char c){
        int ic = (int) c;
        return !CharUtils.isAsciiAlphanumeric(c) && (ic < 0x2E80 || ic > 0x9FFF);
    }
    public String filter(String text) {
        if(StringUtils.isBlank(text))
            return text;
        TrieNode tempNode = rootNode;
        int begin = 0;
        int position = 0;
        StringBuilder result = new StringBuilder();
        String replacement = "**";
        while(position < text.length()) {
            Character c = text.charAt(position);
            if(isSymbol(c)){
                if(tempNode == rootNode) {
                    result.append(c);
                    ++begin;
                }
                ++position;
                continue;
            }
            tempNode = tempNode.getSubNode(c);
            if (tempNode == null) {

                result.append(text.charAt(begin));
                position = begin + 1;
                begin = position;
                tempNode = rootNode;
            } else if (tempNode.isEnd()){
                result.append(replacement);
                position = position + 1;
                begin = position;
                tempNode = rootNode;
            } else {
                ++position;
            }

        }
        result.append(text.substring(begin));
        return result.toString();

    }

    public void addWord(String words) {
        TrieNode tempNode = rootNode;
        char[] characters = words.toCharArray();
        for (int i = 0; i < characters.length; ++i) {
            if(isSymbol(characters[i])) continue;
            if(tempNode.getSubNode(characters[i]) == null) {
                tempNode.addSubNode(characters[i], new TrieNode());
            }
            tempNode = tempNode.getSubNode(characters[i]);
            if(i == characters.length - 1) {
                tempNode.setEnd(true);
            }
        }
    }
}
