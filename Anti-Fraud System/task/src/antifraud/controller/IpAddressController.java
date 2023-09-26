package antifraud.controller;

import antifraud.models.transactionModel.DeleteResponse;
import antifraud.models.transactionModel.IpAddress;
import antifraud.models.transactionModel.IpRequest;
import antifraud.services.IpAddressServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing suspicious IP addresses.
 */
@RestController
public class IpAddressController {

    private final IpAddressServices ipAddressServices;

    @Autowired
    public IpAddressController(IpAddressServices ipAddressServices) {
        this.ipAddressServices = ipAddressServices;
    }

    /**
     * Save a suspicious IP address to the database.
     * <p>
     * POST /api/antifraud/suspicious-ip
     * Accepts data in JSON format:
     * {
     *   "ip": "<String value, not empty>"
     * }
     * Responds with:
     * - HTTP Created status (201) and the saved IP address information if successful. Response Body:
     * {
     *    "id": "<Long value, not empty>",
     *    "ip": "<String value, not empty>"
     * }
     * - HTTP Conflict status (409) if the IP address is already in the database.
     * - HTTP Bad Request status (400) if the IP address has the wrong format.
     *
     * @param ipRequest The IP address to be saved.
     * @return ResponseEntity containing the result of the IP address saving.
     */
    @PostMapping("/api/antifraud/suspicious-ip")
    public ResponseEntity<IpAddress> saveSuspiciousIp(@RequestBody IpRequest ipRequest) {
        return ipAddressServices.saveSuspiciousIp(ipRequest);
    }

    /**
     * Delete an IP address from the database.
     * <p>
     * DELETE /api/antifraud/suspicious-ip/{ip}
     * Deletes the IP address specified by {ip}.
     * Responds with:
     * - HTTP OK status (200) and a message indicating successful removal. Response Body:
     * {
     *    "status": "IP <ip address> successfully removed!"
     * }
     * - HTTP Not Found status (404) if the IP address is not found in the database.
     * - HTTP Bad Request status (400) if the IP address has the wrong format.
     *
     * @param ip The IP address to be deleted.
     * @return ResponseEntity containing the result of the IP address deletion.
     */
    @DeleteMapping("/api/antifraud/suspicious-ip/{ip}")
    public ResponseEntity<DeleteResponse> deleteIp(@PathVariable String ip) {
        return ipAddressServices.deleteIpAddress(ip);
    }

    /**
     * Get a list of suspicious IP addresses.
     * <p>
     * GET /api/antifraud/suspicious-ip
     * Responds with:
     * - HTTP OK status (200) and an array of JSON objects representing IP addresses sorted by ID in ascending order.
     *  Response Body:
     *  [
     *     {
     *         "id": 1,
     *         "ip": "192.168.1.1"
     *     },
     *      ...
     *     {
     *         "id": 100,
     *         "ip": "192.168.1.254"
     *     }
     * ]
     * - An empty JSON array if the database is empty.
     *
     * @return ResponseEntity containing the list of suspicious IP addresses.
     */
    @GetMapping("/api/antifraud/suspicious-ip")
    public ResponseEntity<List<IpAddress>> getListOfIp() {
        return ipAddressServices.getListOfSuspiciousIp();
    }
}
