package backFpv.dto;

import java.util.List;

/**
 * DTO para representar los datos del cliente en el sistema, incluyendo
 * información personal y fondos suscritos.
 */
public class ClientDTO {

    /** ID único del cliente. */
    private String id;
    /** Nombre del cliente. */
    private String name;
    /** Saldo disponible del cliente. */
    private double availableBalance;
    /** Lista de fondos a los que el cliente está suscrito. */
    private List<FundSubscribedDTO> subscribedFunds;
    /** Correo electrónico del cliente. */
    private String email;
    /** Número de teléfono del cliente. */
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

    public List<FundSubscribedDTO> getSubscribedFunds() {
        return subscribedFunds;
    }

    public void setSubscribedFunds(List<FundSubscribedDTO> subscribedFunds) {
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
