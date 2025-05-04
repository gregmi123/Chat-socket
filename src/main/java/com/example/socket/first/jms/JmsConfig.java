package com.example.socket.first.jms;

import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQPrefetchPolicy;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.jms.Queue;


/**
 * @author Gaurav Regmi
 */
@Configuration
@Slf4j
public class JmsConfig {

    @Value("${spring.activemq.broker.url}")
    private String brokerURL;

    @Value("${spring.activemq.broker.user}")
    private String brokerUser;

    @Value("${spring.activemq.broker.password}")
    private String brokerPassword;

    @Value("${spring.activemq.prefetch}")
    private String prefetch;

    @Bean("notificationWebSecond")
    public Queue notificationWebQueue() {
        return new ActiveMQQueue("bankxp.queue.second.notification.web");
    }

    @Bean
    public ActiveMQConnectionFactory activeMQConnectionFactory() {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        connectionFactory.setBrokerURL(brokerURL);
        connectionFactory.setPassword(brokerPassword);
        connectionFactory.setUserName(brokerUser);
        connectionFactory.setTrustAllPackages(true);
        connectionFactory.setPrefetchPolicy(prefetchPolicy());
        return connectionFactory;

    }

    @Bean
    public ActiveMQPrefetchPolicy prefetchPolicy() {
        ActiveMQPrefetchPolicy activeMQPrefetchPolicy = new ActiveMQPrefetchPolicy();
        activeMQPrefetchPolicy.setQueuePrefetch(Integer.parseInt(prefetch));
        return activeMQPrefetchPolicy;
    }

    @Bean
    public JmsTemplate jmsTemplate() {
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(activeMQConnectionFactory());
        jmsTemplate.setMessageConverter(jacksonJmsMessageConverter());
        return jmsTemplate;
    }

    @Bean(name = "notificationWebFactory")
    public DefaultJmsListenerContainerFactory notificationSeenJmsListenerContainerFactory() {
        DefaultJmsListenerContainerFactory defaultJmsListenerContainerFactory = new DefaultJmsListenerContainerFactory();
        defaultJmsListenerContainerFactory.setConnectionFactory(activeMQConnectionFactory());
        defaultJmsListenerContainerFactory.setMessageConverter(jacksonJmsMessageConverter());
        defaultJmsListenerContainerFactory.setConcurrency("10-20");
        defaultJmsListenerContainerFactory.setTaskExecutor(taskExecutor());

        return defaultJmsListenerContainerFactory;
    }

    @Bean
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor tE = new ThreadPoolTaskExecutor();
        tE.setCorePoolSize(100);
        tE.setMaxPoolSize(100);
        tE.setThreadNamePrefix("jms-thread-");
        tE.setKeepAliveSeconds(50);
        tE.setQueueCapacity(-1);
        return tE;
    }
}
