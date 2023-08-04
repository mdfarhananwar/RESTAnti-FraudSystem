package antifraud.services;

import antifraud.models.transactionModel.Result;
import antifraud.models.transactionModel.Transaction;
import antifraud.models.transactionModel.TransactionResponse;
import antifraud.models.userModel.Operation;
import antifraud.models.userModel.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionServices {
    String fullInfo;
    String result;
    private UserService userService;
    private IpAddressServices ipAddressServices;
    private StolenCardServices stolenCardServices;

    @Autowired
    public TransactionServices(UserService userService, IpAddressServices ipAddressServices, StolenCardServices stolenCardServices) {
        this.userService = userService;
        this.ipAddressServices = ipAddressServices;
        this.stolenCardServices = stolenCardServices;
    }

    @Transactional
    public ResponseEntity<TransactionResponse> validTransaction(Transaction transaction, Authentication authentication) {
        String authenticatedUsername = authentication.getName();
        User authenticatedUser = userService.findUserByUsername(authenticatedUsername);
        String authenticatedRoleName = authenticatedUser.getRole().getName();
        System.out.println("Authenticated User: " + authenticatedUser.toString());
        Operation authenticatedOperation = authenticatedUser.getOperation();
        System.out.println(authenticatedUser.getOperation());
        System.out.println(authenticatedRoleName);
        Long amount = transaction.getAmount();
        String ip = transaction.getIp();
        String number = transaction.getNumber();

        if (!ipAddressServices.isValidIpAddress(ip) || !stolenCardServices.isValidCardNumber(number)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if (transaction.getAmount() == null || transaction.getAmount() <= 0) {
            System.out.println("NULLIF");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        fullInfo = getFullInfo(amount, ip, number);

        if (authenticatedOperation.equals(Operation.UNLOCK)) {
            if (fullInfo.equals("none")) {
                TransactionResponse transactionResponse = new TransactionResponse(Result.ALLOWED, "none");
                return ResponseEntity.ok(transactionResponse);
            } else if (fullInfo.equals("amount") && amount <= 1500) {
                TransactionResponse transactionResponse = new TransactionResponse(Result.MANUAL_PROCESSING, "amount");
                return ResponseEntity.ok(transactionResponse);
            } else if (fullInfo.equals("amount") && amount > 1500) {
                TransactionResponse transactionResponse = new TransactionResponse(Result.PROHIBITED, "amount");
                return ResponseEntity.ok(transactionResponse);
            } else {
                TransactionResponse transactionResponse = new TransactionResponse(Result.PROHIBITED, fullInfo);
                return ResponseEntity.ok(transactionResponse);
            }

        } else {
            if (fullInfo.equals("none")) {
                TransactionResponse transactionResponse = new TransactionResponse(Result.ALLOWED, "none");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(transactionResponse);
            } else if (fullInfo.equals("amount") && amount <= 1500) {
                TransactionResponse transactionResponse = new TransactionResponse(Result.MANUAL_PROCESSING, "amount");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(transactionResponse);

            } else if (fullInfo.equals("amount") && amount > 1500) {
                TransactionResponse transactionResponse = new TransactionResponse(Result.PROHIBITED, "amount");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(transactionResponse);
            } else {
                TransactionResponse transactionResponse = new TransactionResponse(Result.PROHIBITED, fullInfo);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(transactionResponse);
            }
        }

    }

    public String getFullInfo(Long amount, String ip, String number) {
        String info = "";
        String amountInfo = "";
        String cardInfo = "";
        String ipInfo = "";
        if (amount > 200) {
            amountInfo = "amount";
        }
        if (amount <= 200) {
            amountInfo = "none";
        }
        if (ipAddressServices.isIpSuspicious(ip)) {
            ipInfo = "ip";
        }
        if (stolenCardServices.isCardStolen(number)) {
            cardInfo = "card-number";
        }
        if (amount > 200 && !ipInfo.equals("ip") && !cardInfo.equals("card-number")) {
            info = amountInfo;
        } else if (amount > 1500 && ipInfo.equals("ip") && !cardInfo.equals("card-number")) {
            info = amountInfo + ", " + ipInfo;
        } else if (amount > 1500 && !ipInfo.equals("ip") && cardInfo.equals("card-number")) {
            info = amountInfo + ", " + cardInfo;
        } else if (amount > 1500 && ipInfo.equals("ip") && cardInfo.equals("card-number")) {
            info = amountInfo + ", " + cardInfo + ", " + ipInfo;
        } else if (amount <= 200 && !ipInfo.equals("ip") && !cardInfo.equals("card-number")) {
            info = amountInfo;
        } else if (amount <= 1500 && ipInfo.equals("ip") && !cardInfo.equals("card-number")) {
            info = ipInfo;
        } else if (amount <= 1500 && !ipInfo.equals("ip") && cardInfo.equals("card-number")) {
            info = cardInfo;
        } else if (amount <= 1500 && ipInfo.equals("ip") && cardInfo.equals("card-number")) {
            info = cardInfo + ", " + ipInfo;
        }
        return info;
    }

//   public void amountValid(Long amount) {
//      if (amount <= 200) {
//         amountInfo = "none";
//         result = "ALLOWED";
//      } else if (amount <= 1500) {
//         amountInfo = "amount";
//         result = "MANUAL_PROCESSING";
//      } else {
//         amountInfo = "amount";
//         result = "PROHIBITED";
//      }
//
//   }


}
