package backFpv.dto;

import java.util.List;

public class ClientDTO {
    // Propiedades de cliente
    private String id;
    private String name;
    private double availableBalance;
    private List<FundSubscribedDTO> subscribedFunds;
    private String email;
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
