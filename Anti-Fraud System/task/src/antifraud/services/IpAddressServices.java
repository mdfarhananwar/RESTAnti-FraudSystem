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

    @Transactional
    public ResponseEntity<IpAddress> saveSuspiciousIp(IpRequest ipRequest) {
        String ip = ipRequest.getIp();

        if (isIpNotValid(ip)) {
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

        if (isIpNotValid(ip)) {
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
        if (listOfSuspiciousIp.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        return ResponseEntity.ok(listOfSuspiciousIp);
    }

    public boolean isIpNotValid(String ip) {
        if (ip == null || ip.equals("")) {
            return true;
        }
        String[] parts = ip.split("\\.");

        if (parts.length != 4) {
            return true;
        }

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
