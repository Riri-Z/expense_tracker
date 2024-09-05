package com.expense_tracker.email.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.expense_tracker.exception.user.FailedSendingResetPasswordEmail;

@Service
public class EmailService /* extends IEmailService */ {

	private static final Logger log = LoggerFactory.getLogger(EmailService.class);

	private final JavaMailSender javaMailSender;

	public EmailService(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}

	public void sendPasswordResetEmail(String to, String resetUrl) {
		try {

			SimpleMailMessage message = new SimpleMailMessage();
			message.setTo(to);
			message.setFrom("toto@gmail.com");
			message.setSubject("Reset email");
			message.setText("Click on the link to reset your password : " + resetUrl);
			javaMailSender.send(message);
		}
		catch (MailException ex) {
			log.error("Exception found: {}", ex.getMessage());
			throw new FailedSendingResetPasswordEmail("Failed to send email for password reset");
		}

	}

}
