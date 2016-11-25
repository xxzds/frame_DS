package com.anjz;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 测试基类
 * @author ding.shuai
 * @date 2016年7月20日下午9:52:13
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/spring.xml" })
public class BaseTest  {
    public static final Logger logger = LoggerFactory.getLogger(BaseTest.class);

}
