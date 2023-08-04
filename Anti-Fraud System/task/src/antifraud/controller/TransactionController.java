package antifraud.controller;

import antifraud.models.transactionModel.*;
import antifraud.models.userModel.Operation;
import antifraud.models.userModel.User;
import antifraud.services.IpAddressServices;
import antifraud.services.StolenCardServices;
import antifraud.services.TransactionServices;
import antifraud.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Map;

@RestController
public class TransactionController {
    @Autowired
    private UserService userService;

    @Autowired
    private IpAddressServices ipAddressServices;

    @Autowired
    private StolenCardServices stolenCardServices;

    @Autowired
    private TransactionServices transactionServices;
    @PreAuthorize("MERCHANT")
    @PostMapping("/api/antifraud/transaction")
    public ResponseEntity<?> validAmount(@RequestBody Transaction transaction, Authentication authentication) {
        //System.out.println(transaction.toString());
//        String authenticatedUsername = authentication.getName();
//
//        User authenticatedUser = userService.findUserByUsername(authenticatedUsername);String authenticatedRoleName = authenticatedUser.getRole().getName();
//        System.out.println("Authenticated User: " + authenticatedUser.toString());
//        Operation authenticatedOperation = authenticatedUser.getOperation();
//        System.out.println(authenticatedUser.getOperation());
//        System.out.println(authenticatedRoleName);
//
//        if ( transaction.getAmount() == null || transaction.getAmount() <= 0) {
//            System.out.println("NULLIF");
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//        } else {
//            if (authenticatedOperation.equals(Operation.UNLOCK)) {
//                if (transaction.getAmount() <= 200 ) {
//                    return ResponseEntity.ok(Map.of("result", "ALLOWED"));
//                } else if (transaction.getAmount() <= 1500) {
//                    return ResponseEntity.ok(Map.of("result", "MANUAL_PROCESSING"));
//                } else {
//                    System.out.println("PRHOBITEDDDDDDDDDDDDDDDDDD");
//                    return ResponseEntity.ok(Map.of("result", "PROHIBITED"));
//                }
//            } else {
//                if (transaction.getAmount() <= 200 ) {
//                    System.out.println("transaction == 200");
//                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("result", "ALLOWED"));
//                } else if (transaction.getAmount() <= 1500) {
//                    System.out.println("transaction==1500");
//                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("result", "MANUAL_PROCESSING"));
//
//                } else {
//                    System.out.println("PRHOBITEDDDDDDDDDDDDDDDDDD");
//                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("result", "PROHIBITED"));
//
//                }
//            }
//
//        }
        return transactionServices.validTransaction(transaction,authentication);
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
