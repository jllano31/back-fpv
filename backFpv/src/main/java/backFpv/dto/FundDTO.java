package backFpv.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class FundDTO {
    @NotBlank(message = "El nombre del fondo es obligatorio.")
    @Size(min = 3, max = 100, message = "El nombre del fondo debe tener entre 3 y 100 caracteres.")
    private String name;

    @NotNull(message = "El monto mínimo de vinculación es obligatorio.")
    @Min(value = 1, message = "El monto mínimo de vinculación debe ser mayor a 0.")
    private Double minimumInvestment;

    @NotBlank(message = "La categoría del fondo es obligatoria.")
    private String category;

    public @NotBlank(message = "El nombre del fondo es obligatorio.") @Size(min = 3, max = 100, message = "El nombre del fondo debe tener entre 3 y 100 caracteres.") String getName() {
        return name;
    }

    public void setName(@NotBlank(message = "El nombre del fondo es obligatorio.") @Size(min = 3, max = 100, message = "El nombre del fondo debe tener entre 3 y 100 caracteres.") String name) {
        this.name = name;
    }

    public @NotNull(message = "El monto mínimo de vinculación es obligatorio.") @Min(value = 1, message = "El monto mínimo de vinculación debe ser mayor a 0.") Double getMinimumInvestment() {
        return minimumInvestment;
    }

    public void setMinimumInvestment(@NotNull(message = "El monto mínimo de vinculación es obligatorio.") @Min(value = 1, message = "El monto mínimo de vinculación debe ser mayor a 0.") Double minimumInvestment) {
        this.minimumInvestment = minimumInvestment;
    }

    public @NotBlank(message = "La categoría del fondo es obligatoria.") String getCategory() {
        return category;
    }

    public void setCategory(@NotBlank(message = "La categoría del fondo es obligatoria.") String category) {
        this.category = category;
    }
}
