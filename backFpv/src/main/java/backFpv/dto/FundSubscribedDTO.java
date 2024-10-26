package backFpv.dto;

/**
 * DTO que representa la suscripción de un cliente a un fondo, incluyendo
 * información como el ID del fondo, nombre, monto invertido y fecha de suscripción.
 */
public class FundSubscribedDTO {

    /** ID del fondo al que el cliente está suscrito. */
    private String fundId;
    /** Nombre del fondo. */
    private String fundName;
    /** Monto invertido en el fondo por el cliente. */
    private double investedAmount;
    /** Fecha de suscripción del cliente al fondo. */
    private String subscriptionDate;

    public String getFundId() {
        return fundId;
    }

    public void setFundId(String fundId) {
        this.fundId = fundId;
    }

    public String getFundName() {
        return fundName;
    }

    public void setFundName(String fundName) {
        this.fundName = fundName;
    }

    public double getInvestedAmount() {
        return investedAmount;
    }

    public void setInvestedAmount(double investedAmount) {
        this.investedAmount = investedAmount;
    }

    public String getSubscriptionDate() {
        return subscriptionDate;
    }

    public void setSubscriptionDate(String subscriptionDate) {
        this.subscriptionDate = subscriptionDate;
    }
}
