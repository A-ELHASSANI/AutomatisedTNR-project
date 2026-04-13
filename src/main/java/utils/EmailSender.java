package utils;

import java.io.File;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailSender {

    public void sendInvoiceEmail(String poNumber) {

//        final String username = "elhassani.anas06@gmail.com";
//        final String password = "kixx mcfo lqqh wlhv"; 
        
        final String username = "r44.aspen@gmail.com";
        final String password = "bnbe ghzv uphv tzcl"; 
        
      Properties props = new Properties();
      props.put("mail.smtp.auth", "true");
      props.put("mail.smtp.starttls.enable", "true");
      props.put("mail.smtp.starttls.required", "true");
      props.put("mail.smtp.ssl.protocols", "TLSv1.2");
      props.put("mail.smtp.host", "smtp.gmail.com");
      props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
            new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));

            message.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse("invoices@accorhotels-test.coupahost.com")
            );

            message.setSubject(poNumber);

            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText("Please find attached invoice.");

            MimeBodyPart attachmentPart = new MimeBodyPart();
            attachmentPart.attachFile(new File("C:\\Users\\anas.elhassani\\Downloads\\TEST.pdf"));

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            multipart.addBodyPart(attachmentPart);

            message.setContent(multipart);

            javax.mail.Transport.send(message);

            System.out.println("Email sent successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}