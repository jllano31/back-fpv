package backFpv.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Entidad que representa un fondo en el sistema, incluyendo su nombre,
 * monto mínimo de inversión y categoría.
 */
@Document(collection = "funds")
public class Fund {

    /** ID único del fondo. */
    @Id
    private String id;
    /** Nombre del fondo. */
    private String name;
    /** Monto mínimo requerido para invertir en el fondo. */
    private Double minimumInvestment;
    /** Categoría del fondo (ej. renta fija, renta variable). */
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
