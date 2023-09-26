package antifraud.services;

import antifraud.dao.transactionDao.IpAddressRepository;
import antifraud.models.transactionModel.DeleteResponse;
import antifraud.models.transactionModel.IpAddress;
import antifraud.models.transactionModel.IpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
public class IpAddressServices {

    private final IpAddressRepository ipAddressRepository;

    @Autowired
    public IpAddressServices(IpAddressRepository ipAddressRepository) {
        this.ipAddressRepository = ipAddressRepository;
    }

    /**
     * Save a suspicious IP address to ban it from transactions.
     *
     * @param ipRequest The IP request object containing the IP address.
     * @return ResponseEntity with the status and the saved IP address.
     */
    @Transactional
    public ResponseEntity<IpAddress> saveSuspiciousIp(IpRequest ipRequest) {
        String ip = ipRequest.getIp();

        // Check if IP addresses compliance with IPv4
        // Any address following this format consists of four series of numbers from 0 to 255 separated by dots.
        // Here is an example of a valid IP address: 132.245.4.216
        if (isIpNotValid(ip)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        // Check if an IP is already in the database
        if (ipAddressRepository.existsByIp(ip)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        // Save the Ip address in the database
        IpAddress ipAddress = new IpAddress();
        ipAddress.setIp(ip);
        IpAddress savedIpAddress = ipAddressRepository.save(ipAddress);
        return ResponseEntity.ok(savedIpAddress);
    }

    /**
     * Delete a suspicious IP address from the database.
     *
     * @param ip The IP address to be deleted.
     * @return ResponseEntity with the status and a response message.
     */
    @Transactional
    public ResponseEntity<DeleteResponse> deleteIpAddress(String ip) {
        // Check if the provided IP address is in a valid format
        if (isIpNotValid(ip)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        // Check if the IP address exists in the database
        if (!ipAddressRepository.existsByIp(ip)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Delete the IP address from the database
        ipAddressRepository.deleteByIp(ip);

        // Prepare the response message
        String status = "IP " + ip + " successfully removed!";
        DeleteResponse response = new DeleteResponse(status);

        // Respond with HTTP OK and the response body
        return ResponseEntity.ok(response);
    }

    /**
     * Get a list of all suspicious IP addresses.
     *
     * @return ResponseEntity with the status and a list of suspicious IP addresses.
     */
    public ResponseEntity<List<IpAddress>> getListOfSuspiciousIp() {

        // Sort IP addresses by ID in ascending order
        List<IpAddress> listOfSuspiciousIp = ipAddressRepository.findAllByOrderByIdAsc();

        // Check if list is empty
        if (listOfSuspiciousIp.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        return ResponseEntity.ok(listOfSuspiciousIp);
    }

    // Helper Methods :-----------------------------------------------------------------------------------------------

    // Method to check if Ip address compliance with IPV4
    public boolean isIpNotValid(String ip) {

        // Check if IP is empty
        if (ip == null || ip.isEmpty()) {
            return true;
        }
        String[] parts = ip.split("\\.");

        // Check if IP address follows the format and consist of four series of numbers separated by dot
        if (parts.length != 4) {
            return true;
        }
        // Check if IP address of four series of numbers separated by dot are from 0 to 255
        for (String part : parts) {
            try {
                int num = Integer.parseInt(part);
                if (num < 0 || num > 255) {
                    return true;
                }
            } catch (NumberFormatException e) {
                return true;
            }
        }

        return false;
    }

    public boolean isIpSuspicious(String ip) {
        return ipAddressRepository.existsByIp(ip);
    }
}
