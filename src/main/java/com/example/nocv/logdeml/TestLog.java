package com.example.nocv.logdeml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestLog {

    public static final Logger logger = LoggerFactory.getLogger(TestLog.class);
    public static void main(String[] args) {
       // System.out.println(); //沉重 带有锁

        int sss = 444;
        int yyy = 555;
        int mmm = 666;
        logger.info("sss:{},yyy:{},mmm:{}",sss,yyy,mmm);
        logger.warn("warn");
        logger.error("error");
    }
}
