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

    private IpAddressRepository ipAddressRepository;

    @Autowired
    public IpAddressServices(IpAddressRepository ipAddressRepository) {
        this.ipAddressRepository = ipAddressRepository;
    }

    @Transactional
    public ResponseEntity<IpAddress> saveSuspiciousIp(IpRequest ipRequest) {
        String ip = ipRequest.getIp();
        IpAddress checkIpAddress = ipAddressRepository.findByIp(ip);

        if (!isValidIpAddress(ip)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if (ipAddressRepository.existsByIp(ip)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }


        IpAddress ipAddress = new IpAddress();
        ipAddress.setIp(ip);
        IpAddress savedIpAddress = ipAddressRepository.save(ipAddress);
        return ResponseEntity.ok(savedIpAddress);
    }

    @Transactional
    public ResponseEntity<DeleteResponse> deleteIpAddress(String ip) {
        System.out.println("Enter");

        if (!isValidIpAddress(ip)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if (!ipAddressRepository.existsByIp(ip)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        ipAddressRepository.deleteByIp(ip);
        String status = "IP " + ip + " successfully removed!";
        return ResponseEntity.ok(new DeleteResponse(status));
    }

    public ResponseEntity<List<IpAddress>> getListOfSuspiciousIp() {
        List<IpAddress> listOfSuspiciousIp = ipAddressRepository.findAllByOrderByIdAsc();
        if (listOfSuspiciousIp.isEmpty() || listOfSuspiciousIp.size() == 0) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        return ResponseEntity.ok(listOfSuspiciousIp);
    }

    public boolean isValidIpAddress(String ip) {
        if (ip == null || ip.equals("")) {
            return false;
        }
        String[] parts = ip.split("\\.");

        if (parts.length != 4) {
            return false;
        }

        for (String part : parts) {
            try {
                int num = Integer.parseInt(part);
                if (num < 0 || num > 255) {
                    return false;
                }
            } catch (NumberFormatException e) {
                return false;
            }
        }

        return true;
    }
    public boolean isIpSuspicious(String ip) {
        return ipAddressRepository.existsByIp(ip);
    }
}
