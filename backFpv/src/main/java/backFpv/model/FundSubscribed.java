package backFpv.model;

/**
 * Entidad que representa la suscripción de un cliente a un fondo,
 * incluyendo el fondo, monto invertido y fecha de suscripción.
 */
public class FundSubscribed {

    /** ID del fondo al que el cliente está suscrito. */
    private String fundId;
    /** Nombre del fondo suscrito. */
    private String fundName;
    /** Monto invertido por el cliente en el fondo. */
    private double investedAmount;
    /** Fecha en la que el cliente se suscribió al fondo. */
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
