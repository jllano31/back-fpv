package backFpv.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa a un cliente en el sistema, incluyendo sus datos personales,
 * saldo disponible, fondos suscritos, y detalles de contacto.
 */
@Document(collection = "clients")
public class Client {

    /** ID único del cliente. */
    @Id
    private String id;
    /** Nombre del cliente. Es obligatorio. */
    @NotBlank(message = "El nombre del cliente es obligatorio.")
    private String name;
    /** Saldo disponible del cliente. Es obligatorio. */
    @NotNull(message = "El saldo disponible es obligatorio.")
    private double availableBalance;
    /** Lista de fondos a los que el cliente está suscrito. */
    private List<FundSubscribed> subscribedFunds = new ArrayList<>();
    /** Correo electrónico del cliente. Debe ser válido y es obligatorio. */
    @NotBlank(message = "El correo electrónico es obligatorio.")
    @Email(message = "El correo electrónico debe ser válido.")
    private String email;
    /** Número de teléfono del cliente. Debe contener 10 dígitos y es obligatorio. */
    @NotBlank(message = "El número de teléfono es obligatorio.")
    @Pattern(regexp = "^[0-9]{10}$", message = "El número de teléfono debe contener 10 dígitos.")
    private String phoneNumber;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(double availableBalance) {
        this.availableBalance = availableBalance;
    }

    public List<FundSubscribed> getSubscribedFunds() {
        return subscribedFunds;
    }

    public void setSubscribedFunds(List<FundSubscribed> subscribedFunds) {
        this.subscribedFunds = subscribedFunds;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
