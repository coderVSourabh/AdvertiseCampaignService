package com.vsourabh.simplead.helper;

import java.util.logging.Logger;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class AdAppContextBuilder implements ApplicationContextAware {

	private static ApplicationContext applicationContext;
	static final Logger log = Logger.getLogger(AdAppContextBuilder.class.getName());

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Autowired
	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		applicationContext = context;
		log.info("SimpleAd:method setApplicationContext");
	}

}
