package cn.revealing.howtose.participle;

import cn.revealing.howtose.model.Keyword;
import cn.revealing.howtose.services.KeywordService;
import com.hankcs.hanlp.dictionary.CustomDictionary;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by GJW on 2018/4/13.
 */
@Service
public class DictionaryService implements ApplicationListener<ContextRefreshedEvent>{
    @Autowired
    KeywordService keywordService;

    public void addKeywords() {
        List<Keyword> words = keywordService.getKeywords(0, 9999);
        for(Keyword keyword : words) {
            if(keyword.getType() == 1)
                FBSegment.addWord(keyword.getWord());
        }
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        addKeywords();
    }
}
