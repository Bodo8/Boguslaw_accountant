package pl.coderstrust.emailprocessor;

import static pl.coderstrust.configuration.CompanyConfigurationProvider.COMPANY_EMAIL;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import pl.coderstrust.model.Invoice;

import java.time.LocalDate;

@Component
public class EmailSender {

  private static final Logger log = LoggerFactory.getLogger(EmailSender.class);
  private final TemplateEngine templateEngine;
  private JavaMailSender javaMailSender;

  public EmailSender(TemplateEngine templateEngine, JavaMailSender javaMailSender) {
    this.templateEngine = templateEngine;
    this.javaMailSender = javaMailSender;
  }

  public void sendEmail(String to, Invoice invoice) {

    Context context = new Context();
    context.setVariable("header", "Test");
    context.setVariable("title", "Invoice number: " + invoice.getId());
    context.setVariable("date", invoice.getDate().toString());

    String body = templateEngine.process("template", context);

    MimeMessage mail = javaMailSender.createMimeMessage();
    try {
      MimeMessageHelper helper = new MimeMessageHelper(mail, true);
      helper.setTo(to);
      helper.setReplyTo(COMPANY_EMAIL);
      helper.setFrom(COMPANY_EMAIL);
      helper.setSubject("Invoice number: " + invoice.getId());
      helper.setText(body, true);

    } catch (MessagingException e) {
      e.printStackTrace();
      log.info("Wrong email address");
    }

    javaMailSender.send(mail);
  }

  @Scheduled(cron = "0 38 15 * * *")
  public void sendEmailReport () {
    Invoice invoice = new Invoice(123, LocalDate.now(), null, null);
    log.info("send Email Report is working");
    sendEmail("andrzej.slomkowski@gmail.com", invoice);
  }
}