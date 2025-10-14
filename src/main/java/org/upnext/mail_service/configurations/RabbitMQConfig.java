package org.upnext.mail_service.configurations;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class RabbitMQConfig {
    // ==== Email System ====
    public static final String EMAIL_QUEUE = "mail.events.queue";
    public static final String EMAIL_EXCHANGE = "mail.exchange";
    public static final String EMAIL_ROUTING_KEY = "mail.events";

    // === Forget-Password ===

    public static final String FORGET_PASS_QUEUE = "forget.pass.events.queue";
    public static final String FORGET_PASS_EXCHANGE = "forget.pass.exchange";
    public static final String FORGET_PASS_ROUTING_KEY = "forget.pass.events";

    // === Notification System ====

    public static final String NOTIFICATION_QUEUE = "notifications.queue";
    public static final String NOTIFICATION_EXCHANGE = "notifications.exchange";
    public static final String NOTIFICATION_ROUTING_KEY = "notifications.events";


    // === Email Beans ===
    @Bean
    public Queue emailQueue() {
        return new Queue(EMAIL_QUEUE, true);
    }

    @Bean
    public TopicExchange emailExchange() {
        return new TopicExchange(EMAIL_EXCHANGE);
    }

    @Bean
    public Binding bindingEmail(Queue emailQueue, TopicExchange emailExchange) {
        return BindingBuilder.bind(emailQueue).to(emailExchange).with(EMAIL_ROUTING_KEY);
    }

    // === Forget-Password ===

    @Bean
    public Queue forgetPassQueue() {
        return new Queue(FORGET_PASS_QUEUE, true);
    }

    @Bean
    public TopicExchange forgetPassExchange() {
        return new TopicExchange(FORGET_PASS_EXCHANGE);
    }

    @Bean
    public Binding bindingForgetPass(Queue forgetPassQueue, TopicExchange forgetPassExchange) {
        return BindingBuilder.bind(forgetPassQueue).to(forgetPassExchange).with(FORGET_PASS_ROUTING_KEY);
    }


    // === Notifications ===

    @Bean
    public Queue notificationQueue() {
        return  new Queue(NOTIFICATION_QUEUE, true);
    }

    @Bean
    public TopicExchange notificationExchange() {
        return new TopicExchange(NOTIFICATION_EXCHANGE);
    }

    @Bean
    public Binding bindingNotification(Queue notificationQueue, TopicExchange notificationExchange) {
        return BindingBuilder.bind(notificationQueue).to(notificationExchange).with(NOTIFICATION_ROUTING_KEY);
    }

    // === Serialization / Deserialization Config


    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitListenerContainerFactory<?> rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory, MessageConverter messageConverter) {

        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter);
        return factory;
    }
}
