package backFpv.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "clients")
public class Client {

    @Id
    private String id;
    private String name;
    private double availableBalance;
    private List<FundSubscribed> subscribedFunds;

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
}
