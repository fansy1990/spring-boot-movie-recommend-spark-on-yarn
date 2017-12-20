package main.spring.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import main.spring.util.Utils;

import java.io.IOException;

/**
 * tomcat启动后，执行代码
 */
//@Controller
public class InitListener implements ApplicationListener<ContextRefreshedEvent> {
	private  Logger logger = LoggerFactory.getLogger(getClass());
	@Override
	public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
		Utils utils = contextRefreshedEvent.getApplicationContext().getBean(Utils.class);
		try {
			logger.info("Initialization begin ...");
			// 设置操作Hadoop的用户为root
			System.setProperty("HADOOP_USER_NAME",
					"root");

			utils.init();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
