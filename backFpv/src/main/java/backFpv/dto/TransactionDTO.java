package backFpv.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class TransactionDTO {

    private String id;

    @NotBlank(message = "El ID del cliente es obligatorio.")
    private String clientId;

    @NotBlank(message = "El ID del fondo es obligatorio.")
    private String fundId;

    @NotBlank(message = "El tipo de transacción es obligatorio.")
    private String transactionType;

    @NotNull(message = "El monto de la transacción es obligatorio.")
    @Min(value = 1, message = "El monto de la transacción debe ser mayor a 0.")
    private Double amount;

    @NotNull(message = "La fecha de la transacción es obligatoria.")
    private LocalDateTime transactionDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public @NotBlank(message = "El ID del cliente es obligatorio.") String getClientId() {
        return clientId;
    }

    public void setClientId(@NotBlank(message = "El ID del cliente es obligatorio.") String clientId) {
        this.clientId = clientId;
    }

    public @NotBlank(message = "El ID del fondo es obligatorio.") String getFundId() {
        return fundId;
    }

    public void setFundId(@NotBlank(message = "El ID del fondo es obligatorio.") String fundId) {
        this.fundId = fundId;
    }

    public @NotBlank(message = "El tipo de transacción es obligatorio.") String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(@NotBlank(message = "El tipo de transacción es obligatorio.") String transactionType) {
        this.transactionType = transactionType;
    }

    public @NotNull(message = "El monto de la transacción es obligatorio.") @Min(value = 1, message = "El monto de la transacción debe ser mayor a 0.") Double getAmount() {
        return amount;
    }

    public void setAmount(@NotNull(message = "El monto de la transacción es obligatorio.") @Min(value = 1, message = "El monto de la transacción debe ser mayor a 0.") Double amount) {
        this.amount = amount;
    }

    public @NotNull(message = "La fecha de la transacción es obligatoria.") LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(@NotNull(message = "La fecha de la transacción es obligatoria.") LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }
}
