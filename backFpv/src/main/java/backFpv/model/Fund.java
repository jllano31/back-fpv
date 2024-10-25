package backFpv.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "funds")
public class Fund {

    @Id
    private String id;
    private String name;
    private Double minimumInvestment;
    private String category;

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

    public Double getMinimumInvestment() {
        return minimumInvestment;
    }

    public void setMinimumInvestment(Double minimumInvestment) {
        this.minimumInvestment = minimumInvestment;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
