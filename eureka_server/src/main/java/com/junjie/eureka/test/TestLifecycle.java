package com.junjie.eureka.test;

import com.netflix.eureka.EurekaServerConfig;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.eureka.server.EurekaServerBootstrap;
import org.springframework.cloud.netflix.eureka.server.event.EurekaRegistryAvailableEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaServerStartedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;

@Configuration(proxyBeanMethods = false)
public class TestLifecycle
        implements ServletContextAware, SmartLifecycle, Ordered {

    private static final Log log = LogFactory
            .getLog(TestLifecycle.class);

    @Autowired
    private EurekaServerConfig eurekaServerConfig;

    private ServletContext servletContext;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private EurekaServerBootstrap eurekaServerBootstrap;

    private boolean running;

    private int order = 1;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
        //第一步
        log.info("=============setServletContext...");
    }

    @Override
    public void start() {
        new Thread(() -> {
            try {
                // TODO: is this class even needed now?
                log.info("-----------start--------->");
//                eurekaServerBootstrap.contextInitialized(
//                        org.springframework.cloud.netflix.eureka.server.EurekaServerInitializerConfiguration.this.servletContext);
//                log.info("Started Eureka Server");
//
//                publish(new EurekaRegistryAvailableEvent(getEurekaServerConfig()));
//                org.springframework.cloud.netflix.eureka.server.EurekaServerInitializerConfiguration.this.running = true;
//                publish(new EurekaServerStartedEvent(getEurekaServerConfig()));
            } catch (Exception ex) {
                // Help!
                log.error("Could not initialize Eureka servlet context", ex);
            }
        }).start();
    }

    private EurekaServerConfig getEurekaServerConfig() {
        return this.eurekaServerConfig;
    }

    private void publish(ApplicationEvent event) {
        this.applicationContext.publishEvent(event);
    }

    @Override
    public void stop() {
        this.running = false;
        log.info("==========stop...");
        eurekaServerBootstrap.contextDestroyed(this.servletContext);
    }

    @Override
    public boolean isRunning() {
        log.info("----------isRunning...");
        return this.running;
    }

    @Override
    public int getPhase() {
        return 0;
    }

    @Override
    public boolean isAutoStartup() {
        log.info("----===------isAutoStartup...");
        return true;
    }

    @Override
    public void stop(Runnable callback) {
        log.info("--------stop----callback...");
        callback.run();
    }

    @Override
    public int getOrder() {
        return this.order;
    }

}
