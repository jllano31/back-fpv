package backFpv.integrations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

/**
 * Servicio para gestionar el envio de menajes SMS a los clientes,
 */
@Service
public class AwsSmsService {

    /** Cliente SNS utilizado para enviar mensajes SMS. */
    @Autowired
    private SnsClient snsClient;

    /**
     * Envía un mensaje SMS al número de teléfono especificado.
     *
     * @param phoneNumber el número de teléfono al que se enviará el mensaje (en formato E.164, e.g., "+57...").
     * @param message el contenido del mensaje SMS.
     * @return el ID del mensaje enviado.
     */
    public String sendSms(String phoneNumber, String message) {
        PublishRequest request = PublishRequest.builder()
                .message(message)
                .phoneNumber(phoneNumber)
                .build();
        PublishResponse result = snsClient.publish(request);
        return result.messageId();
    }
}
