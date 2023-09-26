package antifraud.models.transactionModel;

/**
 * A request object used to send IP addresses to the anti-fraud system.
 */
public class IpRequest {
    private String ip;

    /**
     * Default constructor for the IP request.
     */
    public IpRequest() {
    }

    /**
     * Get the IP address from the request.
     *
     * @return The IP address as a string.
     */
    public String getIp() {
        return ip;
    }

    /**
     * Set the IP address in the request.
     *
     * @param ip The IP address as a string.
     */
    public void setIp(String ip) {
        this.ip = ip;
    }
}