package org.upnext.mail_service.events;


import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.upnext.mail_service.EmailSender;
import org.upnext.mail_service.configurations.RabbitMQConfig;
import org.upnext.sharedlibrary.Events.NotificationEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.thymeleaf.context.Context;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationListener {
    private final EmailSender  emailSender;
    private final TemplateEngine templateEngine;

    @RabbitListener(queues = RabbitMQConfig.NOTIFICATION_QUEUE)
    public void notification(NotificationEvent event) {
        System.out.println("📥 Received: " + event);

        String subject = "\uD83E\uDD29 New Product Added";
        String templateName = "notification";

        Context context = new Context();
        context.setVariable("user_name", event.getUser_name());
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> variables = objectMapper.convertValue(event, Map.class);
        context.setVariables(variables);    
        String htmlBody = templateEngine.process(templateName, context);

        emailSender.sendHtmlEmail(event.getUser_email(), subject, htmlBody);
    }
}
