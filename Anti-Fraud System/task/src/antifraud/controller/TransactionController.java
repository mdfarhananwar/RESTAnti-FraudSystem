package antifraud.controller;
import antifraud.models.transactionModel.*;
import antifraud.services.IpAddressServices;
import antifraud.services.StolenCardServices;
import antifraud.services.TransactionServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class TransactionController {

    private final IpAddressServices ipAddressServices;
    private final StolenCardServices stolenCardServices;
    private final TransactionServices transactionServices;

    @Autowired
    public TransactionController(IpAddressServices ipAddressServices, StolenCardServices stolenCardServices, TransactionServices transactionServices) {
        this.ipAddressServices = ipAddressServices;
        this.stolenCardServices = stolenCardServices;
        this.transactionServices = transactionServices;
    }

    @PostMapping("/api/antifraud/transaction")
    public ResponseEntity<?> validAmount(@RequestBody TransactionRequest transaction, Authentication authentication) {
        return transactionServices.processTransaction(transaction, authentication);

    }

    @GetMapping("/api/antifraud/history")
    public ResponseEntity<List<Transaction>> getHistory() {
        return transactionServices.getListOfTransactions();
    }

    @GetMapping("/api/antifraud/history/{number}")
    public ResponseEntity<List<Transaction>> getTransactionByNumber(@PathVariable String number) {
        return transactionServices.getTransactionByNumber(number);
    }

    @PutMapping("/api/antifraud/transaction")
    public ResponseEntity<Transaction> updateTransaction(@RequestBody TransactionRequestFeedback requestFeedback) {
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
        return stolenCardServices.saveStolenCard(stolenCardRequest);
    }

    @DeleteMapping("/api/antifraud/stolencard/{number}")
    public ResponseEntity<DeleteResponse> deleteStolenCard(@PathVariable String number) {
        return stolenCardServices.deleteStolenCard(number);
    }

    @GetMapping("/api/antifraud/stolencard")
    public ResponseEntity<List<StolenCard>> getListOfCard() {
        return stolenCardServices.getListOfStolenCard();
    }

}
