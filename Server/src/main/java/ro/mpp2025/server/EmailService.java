package ro.mpp2025.server;

import java.io.InputStream;
import java.io.IOException;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailService {

    public EmailService() { }

    public static void sendLoginConf(String to, String messageData) {
        Properties props = new Properties();
        InputStream in = EmailService.class.getClassLoader()
                .getResourceAsStream("transportserver.properties");
        if (in == null) {
            throw new RuntimeException("Nu s-a găsit fișierul transport.properties!");
        }
        try {
            props.load(in);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        } finally {
            try { if (in != null) in.close(); } catch (IOException ignored) {}
        }

        String mailUser = props.getProperty("mail.username");
        String mailPass = props.getProperty("mail.password");
        String subject = "Confirmare login";
        String text =  (messageData == null ? "" : messageData);

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mailUser, mailPass);
            }
        });

        try {
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(mailUser));
            msg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));
            msg.setSubject(subject);
            msg.setText(text);
            Transport.send(msg);
            System.out.println("Email trimis către: " + to);
        } catch (MessagingException me) {
            me.printStackTrace();
        }
    }

    public static void sendLogoutConf(String to, String messageData) {
        Properties props = new Properties();
        InputStream in = EmailService.class.getClassLoader()
                .getResourceAsStream("transportserver.properties");
        if (in == null) {
            throw new RuntimeException("Nu s-a găsit fișierul transport.properties!");
        }
        try {
            props.load(in);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        } finally {
            try { if (in != null) in.close(); } catch (IOException ignored) {}
        }

        String mailUser = props.getProperty("mail.username");
        String mailPass = props.getProperty("mail.password");
        String subject = "Confirmare logout";
        String text = (messageData == null ? "" : messageData);

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mailUser, mailPass);
            }
        });

        try {
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(mailUser));
            msg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));
            msg.setSubject(subject);
            msg.setText(text);
            Transport.send(msg);
            System.out.println("Email trimis către: " + to);
        } catch (MessagingException me) {
            me.printStackTrace();
        }
    }
}
