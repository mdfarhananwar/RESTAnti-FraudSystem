package antifraud.services;

import antifraud.dao.transactionDao.TransactionRepository;
import antifraud.models.transactionModel.Region;
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

import java.util.*;

@Service
public class TransactionServices {
    String fullInfo;
    String result;
    String amountIpCardInfo;
    private UserService userService;
    private IpAddressServices ipAddressServices;
    private StolenCardServices stolenCardServices;
    private TransactionRepository transactionRepository;


    @Autowired
    public TransactionServices(UserService userService, IpAddressServices ipAddressServices, StolenCardServices stolenCardServices, TransactionRepository transactionRepository) {
        this.userService = userService;
        this.ipAddressServices = ipAddressServices;
        this.stolenCardServices = stolenCardServices;
        this.transactionRepository = transactionRepository;
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
        Set<String> ipSet = new HashSet<>();
        Set<Region> regionSet = new HashSet<>();
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
        Transaction savedTransaction = transactionRepository.save(transaction);
        Date date = savedTransaction.getDate();
        System.out.println(date);
        System.out.println("Date from transaction");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // Subtract one hour
        calendar.add(Calendar.HOUR_OF_DAY, -1);
        // Get the modified date
        Date oneHourAgo = calendar.getTime();
        System.out.println(oneHourAgo);
        System.out.println("Date before 1 hour");
//        List<Transaction> transactionsLastHour = transactionRepository.findByDateAfter(oneHourAgo);
        List<Transaction> transactionsLastHour = transactionRepository.findTransactionsAfterTime(oneHourAgo, date);
        for (Transaction transaction1 : transactionsLastHour) {
            System.out.println(transaction1);
            System.out.println("...................................................");
            String ip1 = transaction1.getIp();
            Region region = transaction1.getRegion();
            ipSet.add(ip1);
            regionSet.add(region);
        }

        fullInfo = getFullInfo(amount, ip, number, ipSet, regionSet);
        System.out.println(fullInfo);
        System.out.println("FullINFOREGIONDATE");
        System.out.println("SIZEOFIP");
        System.out.println(ipSet.size());
        System.out.println("SIZEOFREGION");
        System.out.println(regionSet.size());

        if (authenticatedOperation.equals(Operation.UNLOCK)) {
            System.out.println("OPERATIONUNLOCKENTER");
            if (ipSet.size() < 3 && regionSet.size() < 3) {
                System.out.println("IPSETREGIONSETNOTUPTO3");
                if (fullInfo.equals("none")) {
                    TransactionResponse transactionResponse = new TransactionResponse(Result.ALLOWED, "none");
                    return ResponseEntity.ok(transactionResponse);
                } else if (fullInfo.equals("amount") && amount <= 1500) {
                    TransactionResponse transactionResponse = new TransactionResponse(Result.MANUAL_PROCESSING, "amount");
                    return ResponseEntity.ok(transactionResponse);
                } else if (fullInfo.equals("amount") && amount > 1500) {
                    TransactionResponse transactionResponse = new TransactionResponse(Result.PROHIBITED, "amount");
                    return ResponseEntity.ok(transactionResponse);
                }
                TransactionResponse transactionResponse = new TransactionResponse(Result.PROHIBITED, fullInfo);
                return ResponseEntity.ok(transactionResponse);
            } else if (ipSet.size() == 3 || regionSet.size() == 3) {
                System.out.println("IPSETREGIONSETUPTO3");
                TransactionResponse transactionResponse = new TransactionResponse(Result.MANUAL_PROCESSING, fullInfo);
                return ResponseEntity.ok(transactionResponse);
            } else {
                System.out.println("IPSETREGIONSETNOTMORE3");
                TransactionResponse transactionResponse = new TransactionResponse(Result.PROHIBITED, fullInfo);
                return ResponseEntity.ok(transactionResponse);
            }

        } else {
            System.out.println("OPERATIONLOCKENTER");
            if (ipSet.size() < 3 && regionSet.size() < 3) {
                System.out.println("IPSETREGIONSETNOTUPTO3");
                if (fullInfo.equals("none")) {
                    System.out.println();
                    TransactionResponse transactionResponse = new TransactionResponse(Result.ALLOWED, "none");
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(transactionResponse);
                } else if (fullInfo.equals("amount") && amount <= 1500) {
                    TransactionResponse transactionResponse = new TransactionResponse(Result.MANUAL_PROCESSING, "amount");
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(transactionResponse);

                } else if (fullInfo.equals("amount") && amount > 1500) {
                    TransactionResponse transactionResponse = new TransactionResponse(Result.PROHIBITED, "amount");
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(transactionResponse);
                }
                TransactionResponse transactionResponse = new TransactionResponse(Result.PROHIBITED, fullInfo);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(transactionResponse);
            } else if (ipSet.size() == 3 || regionSet.size() == 3) {
                System.out.println("IPSETREGIONSETUPTO3");
                TransactionResponse transactionResponse = new TransactionResponse(Result.MANUAL_PROCESSING, fullInfo);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(transactionResponse);
            } else {
                System.out.println("IPSETREGIONSETNOTMORE3");
                TransactionResponse transactionResponse = new TransactionResponse(Result.PROHIBITED, fullInfo);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(transactionResponse);
            }

        }

    }

    public String getFullInfo(Long amount, String ip, String number, Set<String> ipSet, Set<Region> regionSet) {
        String info = "";
        String ipCorrelationInfo = "";
        String regionCorrelationInfo = "";

        if (ipSet.size() >= 3) {
            ipCorrelationInfo = "ip-correlation";
        }
        if (regionSet.size() >= 3) {
            regionCorrelationInfo = "region-correlation";
        }
        amountIpCardInfo = getAmountIpCardInfo(amount, ip, number);
        System.out.println(amountIpCardInfo);
        System.out.println("amountIpCardInfo");
        System.out.println(ipSet.size());
        System.out.println(regionSet.size());
        info = amountIpCardInfo;
        if (ipSet.size() < 3 && regionSet.size() < 3) {
            //info = amountIpCardInfo;
            System.out.println("NO one is ON");
        } else if (ipSet.size() >= 3 && regionSet.size() < 3) {
            if (info.equals("none")) {
                info = ipCorrelationInfo;
            } else {
                info += ", " + ipCorrelationInfo;
            }

            System.out.println(info);
            System.out.println("ipSet is on");
        } else if (ipSet.size() < 3 && regionSet.size() >= 3) {
            if (info.equals("none")) {
                info = regionCorrelationInfo;
            } else {
                info += ", " + regionCorrelationInfo;
            }
            System.out.println();
            System.out.println(info);
            System.out.println("regionset is on");
        } else {
            if (info.equals("none")) {
                info = ipCorrelationInfo + regionCorrelationInfo;
            } else {
                info += ", " + ipCorrelationInfo + ", " + regionCorrelationInfo;
            }

            System.out.println(info);
            System.out.println("Both are on");
        }
        return info;
    }

    public String getAmountIpCardInfo(Long amount, String ip, String number) {
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

}
