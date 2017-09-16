package cn.revealing.howtose.aspect;

import org.apache.commons.lang.text.StrBuilder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by GJW on 2017/6/18.
 */
@Aspect
@Component
public class LogAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogAspect.class);
    @Before("execution(* cn.revealing.howtose.controller.*.*(..))")
    public void beforeMethod(JoinPoint joinPoint) {
        StrBuilder sb = new StrBuilder();
        for(Object arg : joinPoint.getArgs()){
            sb.append("arg: " + arg.toString() + " | ");
        }
        LOGGER.info("before method : " + sb.toString());
    }

    @After("execution(* cn.revealing.howtose.controller.*.*(..))")
    public void afterMethod() {

    }
}
