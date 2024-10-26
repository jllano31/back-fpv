package backFpv.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Entidad que representa una transacción en el sistema, con detalles
 * sobre el cliente, fondo, tipo de transacción, monto y fecha.
 */
@Document(collection = "transactions")
public class Transaction {

    /** ID único de la transacción. */
    @Id
    private String id;
    /** ID del cliente que realiza la transacción. */
    private String clientId;
    /** ID del fondo asociado a la transacción. */
    private String fundId;
    /** Tipo de la transacción (ej. suscripción, cancelación). */
    private String transactionType;
    /** Monto de la transacción. */
    private Double amount;
    /** Fecha en la que se realiza la transacción. */
    private LocalDateTime transactionDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getFundId() {
        return fundId;
    }

    public void setFundId(String fundId) {
        this.fundId = fundId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }
}
