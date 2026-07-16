/*
* This service sends email notifications to dreamcart users.
* */
package com.dreamcart.backend.service;

import jakarta.validation.constraints.Email;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender){
        this.mailSender = mailSender;
    }
    /*
    * Sends order confirmation email.
    * */
    @Async
    public void sendOrderConfirmationEmail(
            String to,
            Long orderId,
            String customerName,
            String amount )
    {
        SimpleMailMessage message = new SimpleMailMessage();

            message.setTo(to);
            message.setSubject("DreamCart - Order Confirmation");

            message.setText(
                    "Hi" + customerName + ",\n\n" +
                     "Thank you for shopping with DreamCart. \n\n" +

                     "Your payment was successful.\n\n" +

                     "Order ID: " + orderId + "\n\n" +

                     "Amount: " + amount + "\n\n" +

                     "We are preparing your order.\n\n " +

                     "Regards,\n" +
                     "DreamCart Team."
            );
            mailSender.send(message);
    }
}
