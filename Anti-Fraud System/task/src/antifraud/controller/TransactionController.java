package antifraud.controller;

import antifraud.models.transactionModel.Transaction;
import antifraud.models.userModel.Operation;
import antifraud.models.userModel.User;
import antifraud.services.TransactionServices;
import antifraud.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Map;

@RestController
public class TransactionController {
    @Autowired
    private UserService userService;

    @Autowired
    private TransactionServices transactionServices;
    @PreAuthorize("MERCHANT")
    @PostMapping("/api/antifraud/transaction")
    public ResponseEntity<?> validAmount(@RequestBody Transaction transaction, Authentication authentication) {
        //System.out.println(transaction.toString());
        String authenticatedUsername = authentication.getName();
        //String password = (String) authentication.getCredentials().toString();
        //System.out.println(password);
        //System.out.println("password entered");
        User authenticatedUser = userService.findUserByUsername(authenticatedUsername);
        //System.out.println(authenticatedUser.getPassword());
        //System.out.println("original password");
        String authenticatedRoleName = authenticatedUser.getRole().getName();
        System.out.println("Authenticated User: " + authenticatedUser.toString());
        Operation authenticatedOperation = authenticatedUser.getOperation();
        System.out.println(authenticatedUser.getOperation());
        System.out.println(authenticatedRoleName);
//        if (transaction.getAmount() == 1) {
//            System.out.println("NOTAUTHENTICATEDIF");
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("result", "ALLOWED"));
//        }
        if ( transaction.getAmount() == null || transaction.getAmount() <= 0) {
            System.out.println("NULLIF");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            if (authenticatedOperation.equals(Operation.UNLOCK)) {
                if (transaction.getAmount() <= 200 ) {
                    System.out.println("transaction == 200");
                    return ResponseEntity.ok(Map.of("result", "ALLOWED"));
                } else if (transaction.getAmount() <= 1500) {
                    System.out.println("transaction==1500");
                    return ResponseEntity.ok(Map.of("result", "MANUAL_PROCESSING"));

                } else {
                    System.out.println("PRHOBITEDDDDDDDDDDDDDDDDDD");
                    return ResponseEntity.ok(Map.of("result", "PROHIBITED"));
                }
            } else {
                if (transaction.getAmount() <= 200 ) {
                    System.out.println("transaction == 200");
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("result", "ALLOWED"));
                } else if (transaction.getAmount() <= 1500) {
                    System.out.println("transaction==1500");
                    //return ResponseEntity.ok(Map.of("result", "MANUAL_PROCESSING"));
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("result", "MANUAL_PROCESSING"));

                } else {
                    System.out.println("PRHOBITEDDDDDDDDDDDDDDDDDD");
                    //return ResponseEntity.ok(Map.of("result", "PROHIBITED"));
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("result", "PROHIBITED"));

                }
            }

        }
    }


}
