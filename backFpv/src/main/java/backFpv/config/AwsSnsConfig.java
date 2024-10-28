package backFpv.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;

@Configuration
public class AwsSnsConfig {

    /** AWS Access Key para autenticación. */
    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    /** AWS Secret Key para autenticación. */
    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    /** Región de AWS para el cliente SNS. */
    @Value("${cloud.aws.region.static}")
    private String region;

    /**
     * Configura y retorna una instancia de SnsClient para interactuar con Amazon SNS.
     *
     * @return cliente SNS configurado
     */
    @Bean
    public SnsClient snsClient() {
        return SnsClient.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)))
                .build();
    }
}
