package com.bibekarsoftwaretechnologies.FinancialProCalculator;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendSuggestionMail extends AsyncTask<Void, Void, Boolean> {
    // Email configuration
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";
    private static final String EMAIL = "pollyprojects@gmail.com";
    private static final String PASSWORD = "ykbz qlqy ayrj shco"; // Use the generated app password here

    private Context context;
    private Session session;

    // Hardcoded recipient email
    private static final String RECIPIENT_EMAIL = "mobmasterofficial@gmail.com";

    private String subject;
    private String message;

    // Constructor
    public SendSuggestionMail(Context context, String subject, String message) {
        this.context = context;
        this.subject = subject;
        this.message = message;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        // Properties for the email
        Properties props = new Properties();
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.socketFactory.port", SMTP_PORT);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", SMTP_PORT);
        props.put("mail.smtp.starttls.enable", "true");

        // Create a session
        session = Session.getInstance(props, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL, PASSWORD);
            }
        });

        try {
            // Create MimeMessage
            MimeMessage mm = new MimeMessage(session);
            mm.setFrom(new InternetAddress(EMAIL));
            mm.addRecipient(Message.RecipientType.TO, new InternetAddress(RECIPIENT_EMAIL));
            mm.setSubject(subject);
            mm.setText(message);

            // Send email
            Transport.send(mm);
            return true; // Email sent successfully
        } catch (MessagingException e) {
            e.printStackTrace();
            return false; // Failed to send email
        }
    }

    @Override
    protected void onPostExecute(Boolean success) {
        super.onPostExecute(success);

        // Show a toast message after sending the email
        if (context != null) {
            Toast.makeText(context, success ? "Suggestion sent successfully!" : "Failed to send suggestion.", Toast.LENGTH_LONG).show();
        }
    }
}
