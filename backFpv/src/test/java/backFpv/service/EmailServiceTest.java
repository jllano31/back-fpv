package backFpv.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendEmail_Success() {
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));
        emailService.sendEmail("test@example.com", "Subject", "Message body");
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void testSendEmail_ThrowsMailException() {
        doThrow(new MailException("SMTP error") {}).when(mailSender).send(any(SimpleMailMessage.class));
        Exception exception = assertThrows(RuntimeException.class, () ->
                emailService.sendEmail("test@example.com", "Subject", "Message body"));
        assertTrue(exception.getMessage().contains("Error al enviar el correo a test@example.com"));
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}
