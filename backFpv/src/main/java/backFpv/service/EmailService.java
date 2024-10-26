package backFpv.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Servicio para enviar correos electrónicos en el sistema.
 */
@Service
public class EmailService {

    /** Componente para enviar correos electrónicos. */
    @Autowired
    private JavaMailSender mailSender;

    /**
     * Enviar un correo electrónico.
     *
     * @param to      Dirección de correo del destinatario.
     * @param subject Asunto del correo.
     * @param text    Contenido del correo.
     */
    public void sendEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            message.setFrom("pruebatecnica069@gmail.com");
            mailSender.send(message);
        } catch (MailException e) {
            throw new RuntimeException("Error al enviar el correo a " + to, e);
        }
    }
}
