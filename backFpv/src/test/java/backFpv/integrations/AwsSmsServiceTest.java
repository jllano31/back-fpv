package backFpv.integrations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class AwsSmsServiceTest {
    @Mock
    private SnsClient snsClient;

    @InjectMocks
    private AwsSmsService awsSmsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void sendSms_ReturnsMessageId() {
        String phoneNumber = "+573001234567";
        String message = "Hello, this is a test message!";
        String expectedMessageId = "12345-67890";
        PublishResponse mockResponse = PublishResponse.builder()
                .messageId(expectedMessageId)
                .build();
        when(snsClient.publish(any(PublishRequest.class))).thenReturn(mockResponse);
        String actualMessageId = awsSmsService.sendSms(phoneNumber, message);
        assertEquals(expectedMessageId, actualMessageId, "El ID del mensaje no coincide con el esperado");
    }
}
