package antifraud.controller;

import antifraud.dao.transactionDao.AppStatusRepository;
import antifraud.models.transactionModel.*;
import antifraud.services.IpAddressServices;
import antifraud.services.StolenCardServices;
import antifraud.services.TransactionServices;
import antifraud.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
public class TransactionController {

    int count = 1;
    private boolean isFirstRequest = true;
    @Autowired
    private UserService userService;

    @Autowired
    private AppStatusRepository appStatusRepository;

    @Autowired
    private IpAddressServices ipAddressServices;

    @Autowired
    private StolenCardServices stolenCardServices;

    @Autowired
    private TransactionServices transactionServices;
    //@PreAuthorize("MERCHANT")
    @PostMapping("/api/antifraud/transaction")
    public ResponseEntity<?> validAmount(@RequestBody TransactionRequest transaction, Authentication authentication) {
        System.out.println("POSTMAPPING transaction");
        //return transactionServices.validTransaction(transaction,authentication);
        return transactionServices.processTransaction(transaction, authentication);

    }

    @GetMapping("/api/antifraud/history")
    public ResponseEntity<List<Transaction>> getHistory() {
        System.out.println("Enter HISTORY");

        AppStatus appStatus = appStatusRepository.findById(1L).orElse(null);

        if (appStatus == null) {
            System.out.println("First Request");
            appStatus = new AppStatus();
            appStatus.setFirstRequest(true);
            appStatusRepository.save(appStatus);
            return ResponseEntity.ok(Collections.emptyList());
        }

        return transactionServices.getListOfTransactions();
    }


    @GetMapping("/api/antifraud/history/{number}")
    public ResponseEntity<List<Transaction>> getTransactionByNumber(@PathVariable String number) {
        System.out.println("ENTER");
        return transactionServices.getTransactionByNumber(number);
    }

    @PutMapping("/api/antifraud/transaction")
    public ResponseEntity<Transaction> updateTransaction( @RequestBody TransactionRequestFeedback requestFeedback) {
        System.out.println("PUTMAPPINg ENTER FEEDBACK");
//        Feedback newFeedback = requestFeedback.getFeedback();
//        if (!transactionServices.isValidFeedback(newFeedback)) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//        }
        return transactionServices.updateTransactionFeedback(requestFeedback);
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleInvalidEnumValue(HttpMessageNotReadableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }



   @PostMapping("/api/antifraud/suspicious-ip")
   public ResponseEntity<IpAddress> saveSuspiciousIp(@RequestBody IpRequest ipRequest) {
        return ipAddressServices.saveSuspiciousIp(ipRequest);
   }

   @DeleteMapping("/api/antifraud/suspicious-ip/{ip}")
   public ResponseEntity<DeleteResponse> deleteIp(@PathVariable String ip) {
        return ipAddressServices.deleteIpAddress(ip);
   }

   @GetMapping("/api/antifraud/suspicious-ip")
   public ResponseEntity<List<IpAddress>> getListOfIp() {
        return ipAddressServices.getListOfSuspiciousIp();
   }

   @PostMapping("/api/antifraud/stolencard")
   public ResponseEntity<StolenCard> saveStolenCard(@RequestBody StolenCardRequest stolenCardRequest) {
        return stolenCardServices.saveStolenCard(stolenCardRequest);}

    @DeleteMapping("/api/antifraud/stolencard/{number}")
    public ResponseEntity<DeleteResponse> deleteStolenCard(@PathVariable String number) {
        return stolenCardServices.deleteStolenCard(number);
    }

    @GetMapping("/api/antifraud/stolencard")
    public ResponseEntity<List<StolenCard>> getListOfCard() {
        return stolenCardServices.getListOfStolenCard();
    }


}
