package org.upnext.mail_service.events;



import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.upnext.mail_service.EmailSender;
import org.upnext.mail_service.configurations.RabbitMQConfig;
import org.upnext.sharedlibrary.Events.MailEvent;


@Service
@RequiredArgsConstructor
public class MailListener {
    private final EmailSender emailSender;
    private final TemplateEngine templateEngine; // Thymeleaf engine

    @RabbitListener(queues = RabbitMQConfig.EMAIL_QUEUE)
    public void handelMailEvent(MailEvent event) {
        System.out.println("📥 Received: " + event);

        String subject = "✅ mail confirmation";
        String templateName = "email-confirmed";

        Context context = new Context();
        context.setVariable("name", event.getName());
        context.setVariable("action_url", event.getConfirmation_url());
        String htmlBody = templateEngine.process(templateName, context);

        emailSender.sendHtmlEmail(event.getEmail(), subject, htmlBody);

    }

    @RabbitListener(queues = RabbitMQConfig.FORGET_PASS_QUEUE)
    public void handelForgotPassEvent(MailEvent event) {
        System.out.println("📥 Received: " + event);

        String subject = "✅ Reset Password";
        String templateName = "forget-password";

        Context context = new Context();
        context.setVariable("name", event.getName());
        context.setVariable("action_url", event.getConfirmation_url());
        String htmlBody = templateEngine.process(templateName, context);

        emailSender.sendHtmlEmail(event.getEmail(), subject, htmlBody);
    }



}
