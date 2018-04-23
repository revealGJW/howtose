package cn.revealing.howtose.participle;

/**
 * @author guojiawei
 * @date 2018/4/20
 */
import cn.revealing.howtose.model.Keyword;
import cn.revealing.howtose.services.KeywordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

@Component
public class FBSegment {

    private static volatile FBSegment fbSegment = new FBSegment();
    static {
        fbSegment.Init();
    }


    private static Set<String> seg_dict;

    //加载词典
    public static void Init(){
        seg_dict = new HashSet<String>();
        String dicpath = "dict.txt";
        String line = null;

        BufferedReader br;
        try{
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(dicpath);
            InputStreamReader reader = new InputStreamReader(is);
            br = new BufferedReader(reader);
            while((line = br.readLine()) != null){
                line = line.trim();
                if(line.isEmpty())
                    continue;
                seg_dict.add(line);
            }
            br.close();
        }catch(IOException e){
            e.printStackTrace();
        }

    }

    public static void addWords(List<String> strings) {
        seg_dict.addAll(strings);
    }

    public static void addWord(String string) {
        seg_dict.add(string);
    }
    /**
     * 前向算法分词
     */
    private static List<String> FMM2( String  phrase){
        int maxlen = 16;
        List<String> fmm_list = new Vector<String>();
        int len_phrase = phrase.length();
        int i=0,j=0;

        while(i < len_phrase){
            int end = i+maxlen;
            if(end >= len_phrase)
                end = len_phrase;
            String phrase_sub = phrase.substring(i, end);
            for(j = phrase_sub.length(); j >=0; j--){
                if(j == 1)
                    break;
                String key =  phrase_sub.substring(0, j);
                if(seg_dict.contains(key)){
                    fmm_list.add(key);
                    i +=key.length() -1;
                    break;
                }
            }
            if(j == 1)
                fmm_list.add(""+phrase_sub.charAt(0));
            i+=1;
        }
        return fmm_list;
    }

    /**
     *
     * @return 后向分词结果
     */
    private static List<String> BMM2( String  phrase){
        int maxlen = 16;
        Vector<String> bmm_list = new Vector<String>();
        int len_phrase = phrase.length();
        int i=len_phrase,j=0;

        while(i > 0){
            int start = i - maxlen;
            if(start < 0)
                start = 0;
            String phrase_sub = phrase.substring(start, i);
            for(j = 0; j < phrase_sub.length(); j++){
                if(j == phrase_sub.length()-1)
                    break;
                String key =  phrase_sub.substring(j);
                if(seg_dict.contains(key)){
                    bmm_list.insertElementAt(key, 0);
                    i -=key.length() -1;
                    break;
                }
            }
            if(j == phrase_sub.length() -1)
                bmm_list.insertElementAt(""+phrase_sub.charAt(j), 0);
            i -= 1;
        }
        return bmm_list;
    }

    /**

     */
    public static List<String> segment( String phrase){
        List<String> fmm_list = FMM2(phrase);
        List<String> bmm_list = BMM2(phrase);
        //如果正反向分词结果词数不同，则取分词数量较少的那个
        if(fmm_list.size() != bmm_list.size()){
            if(fmm_list.size() > bmm_list.size())
                return bmm_list;
            else return fmm_list;
        }
        //如果分词结果词数相同
        else{
            //如果正反向的分词结果相同，就说明没有歧义，可返回任意一个
            int i ,FSingle = 0, BSingle = 0;
            boolean isSame = true;
            for(i = 0; i < fmm_list.size();  i++){
                if(!fmm_list.get(i).equals(bmm_list.get(i)))
                    isSame = false;
                if(fmm_list.get(i).length() ==1)
                    FSingle +=1;
                if(bmm_list.get(i).length() ==1)
                    BSingle +=1;
            }
            if(isSame)
                return fmm_list;
            else{
                //分词结果不同，返回其中单字较少的那个
                if(BSingle > FSingle)
                    return fmm_list;
                else return bmm_list;
            }
        }
    }
    public static void main(String [] args) {
        String test = "如何评价张靓颖离婚?";
        System.out.println(FBSegment.segment(test));
    }
}
