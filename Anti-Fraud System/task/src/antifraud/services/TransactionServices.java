package antifraud.services;

import antifraud.dao.transactionDao.CardRepository;
import antifraud.dao.transactionDao.TransactionRepository;
import antifraud.models.transactionModel.*;
import antifraud.models.userModel.Operation;
import antifraud.models.userModel.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TransactionServices {
    String fullInfo;
    //String result;
    String amountIpCardInfo;
    private UserService userService;
    private IpAddressServices ipAddressServices;
    private StolenCardServices stolenCardServices;
    private TransactionRepository transactionRepository;
    private CardRepository cardRepository;
    private TransactionRequest request;
    private Result result;
    private List<String> errors;
    Authentication authentication;


    @Autowired
    public TransactionServices(UserService userService, IpAddressServices ipAddressServices,
                               StolenCardServices stolenCardServices,
                               TransactionRepository transactionRepository,
                               CardRepository cardRepository) {
        this.userService = userService;
        this.ipAddressServices = ipAddressServices;
        this.stolenCardServices = stolenCardServices;
        this.transactionRepository = transactionRepository;
        this.cardRepository = cardRepository;
    }

//    @Transactional
//    public ResponseEntity<TransactionResponse> validTransaction(TransactionRequest transaction, Authentication authentication) {
//
//        String authenticatedUsername = authentication.getName();
//        User authenticatedUser = userService.findUserByUsername(authenticatedUsername);
//        String authenticatedRoleName = authenticatedUser.getRole().getName();
//        System.out.println("Authenticated User: " + authenticatedUser.toString());
//        Operation authenticatedOperation = authenticatedUser.getOperation();
//        System.out.println(authenticatedUser.getOperation());
//        System.out.println(authenticatedRoleName);
//        Set<String> ipSet = new HashSet<>();
//        Set<Region> regionSet = new HashSet<>();
//        Long amount = transaction.getAmount();
//        String ip = transaction.getIp();
//        String number = transaction.getNumber();
//        System.out.println(transaction.toString());
//
//        if (!ipAddressServices.isValidIpAddress(ip) || !stolenCardServices.isValidCardNumber(number)) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//        }
//        if (transaction.getAmount() == null || transaction.getAmount() <= 0) {
//            System.out.println("NULLIF");
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//        }
//
//        LocalDateTime date = transaction.getDate();
//        Region region = transaction.getRegion();
//        Transaction saveTransaction = new Transaction(amount, ip, number, region, date);
//        Transaction savedTransaction = transactionRepository.save(saveTransaction);
//        System.out.println(date);
//        System.out.println("Date from transaction");
//        LocalDateTime oneHourAgo = date.minus(1, ChronoUnit.HOURS);
//        System.out.println(oneHourAgo);
//        System.out.println("Date before 1 hour");
//        List<Transaction> transactionsLastHour = transactionRepository.findTransactionsAfterTime(oneHourAgo, date);
//        for (Transaction transaction1 : transactionsLastHour) {
//            System.out.println(transaction1);
//            System.out.println("...................................................");
//            String ip1 = transaction1.getIp();
//            Region region1 = transaction1.getRegion();
//            ipSet.add(ip1);
//            regionSet.add(region1);
//        }
//
//        fullInfo = getFullInfo(amount, ip, number, ipSet, regionSet);
//        System.out.println(fullInfo);
//        System.out.println("FullINFOREGIONDATE");
//        System.out.println("SIZEOFIP");
//        System.out.println(ipSet.size());
//        System.out.println("SIZEOFREGION");
//        System.out.println(regionSet.size());
//        Result result;
//        if (fullInfo.equals("none")) {
//            result = Result.ALLOWED;
//        } else if (fullInfo.equals("amount") && amount <= 1500) {
//            result = Result.MANUAL_PROCESSING;
//        } else if (fullInfo.equals("amount") && amount > 1500) {
//            result = Result.PROHIBITED;
//        } else {
//            result = Result.PROHIBITED;
//        }
//
//        TransactionResponse transactionResponse;
//        if ((ipSet.size() < 3 && regionSet.size() < 3) || (ipSet.size() == 3 || regionSet.size() == 3)) {
//            if (authenticatedOperation.equals(Operation.UNLOCK)) {
//                System.out.println("OPERATIONUNLOCKENTER");
//                if (ipSet.size() < 3 && regionSet.size() < 3) {
//                    System.out.println("IPSETREGIONSETNOTUPTO3");
//                    transactionResponse = new TransactionResponse(result, fullInfo);
//                } else {
//                    System.out.println("IPSETREGIONSETUPTO3");
//                    result = Result.MANUAL_PROCESSING;
//                    transactionResponse = new TransactionResponse(Result.MANUAL_PROCESSING, fullInfo);
//                }
//            } else {
//                System.out.println("OPERATIONLOCKENTER");
//                System.out.println("IPSETREGIONSETNOTUPTO3");
//                transactionResponse = new TransactionResponse(result, fullInfo);
//            }
//        } else {
//            System.out.println("IPSETREGIONSETNOTMORE3");
//            result = Result.PROHIBITED;
//            transactionResponse = new TransactionResponse(Result.PROHIBITED, fullInfo);
////            if (result == Result.PROHIBITED) {
//                transactionRepository.delete(savedTransaction);
////            }
//        }
////        if (result == Result.PROHIBITED) {
////            transactionRepository.delete(savedTransaction);
////        }
//
//        savedTransaction.setResult(result);
//        //transactionRepository.save(savedTransaction);
//
//        if (authenticatedOperation.equals(Operation.UNLOCK)) {
//            return ResponseEntity.ok(transactionResponse);
//        } else {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(transactionResponse);
//        }
//
//
//    }
    public ResponseEntity<List<Transaction>> getListOfTransactions() {

        //List<Transaction> transactionList = transactionRepository.findAllByOrderByTransactionIdAsc();
        List<Transaction> transactionList = transactionRepository.findAllByOrderByTransactionIdAsc();
        System.out.println("SIZE OF TRANSACTION");
        System.out.println(transactionList.size());
        System.out.println("SIZE OF TRANSACTION");
        for (Transaction transaction : transactionList) {
            TransactionResponseList responseList = new TransactionResponseList(transaction);
            System.out.println(responseList);
        }
        //System.out.println(transactionList);
        if (transactionList.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        return ResponseEntity.ok(transactionList);
        //return ResponseEntity.ok(Collections.emptyList());
    }
    @Transactional
//    public ResponseEntity<Transaction> updateTransactionFeedback(TransactionRequestFeedback requestFeedback) {
//        System.out.println("Enter FEEDback");
//        Long id = requestFeedback.getTransactionId();
//        System.out.println(id + "      This is id from Request Body");
//        String feedback = String.valueOf(requestFeedback.getFeedback());
//        System.out.println(feedback + "      This is feedback from Request Body");
//        Optional<Transaction> checkTransaction = transactionRepository.findById(id);
//        if (checkTransaction.isEmpty()) {
//            System.out.println("There is no Transactions by the requested id ");
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
//        String checkFeedBack = String.valueOf(checkTransaction.get().getFeedback());
//        System.out.println(checkFeedBack + "      This is feedback from Response Body");
//        String checkResult = String.valueOf(checkTransaction.get().getResult());
//        System.out.println(checkResult);
//        System.out.println(checkResult + "      This is checkResult from Response Body");
//        Feedback newFeedback = requestFeedback.getFeedback();
////        Feedback currentFeedback = transaction.getFeedback();
//
//        if (!isValidFeedback(newFeedback)) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//        }
//        if (!checkFeedBack.isEmpty()) return ResponseEntity.status(HttpStatus.CONFLICT).build();
//        if (checkResult.equals("ALLOWED") && feedback != null) {
//            System.out.println("The Result is already Allowed");
//            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
//        }
//        if (feedback.equals(checkFeedBack) || feedback.equals(checkResult)) {
//            System.out.println("The Feedback is already present ");
//            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
//        }
//        if (checkResult.equals("MANUAL_PROCESSING") && feedback.equals("PROHIBITED")) {
//            System.out.println("The Result is Manual Proccesing and u want to prohibit it");
//            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
//        }
////        changeLimit(checkTransaction.get(), feedback);
//
//        checkTransaction.get().setFeedback(requestFeedback.getFeedback());
//        Transaction save = transactionRepository.save(checkTransaction.get());
//        System.out.println(save.getNumber() + "The Feedback is not present Update it to latest");
//        changeLimit(save, feedback);
//        return ResponseEntity.ok(save);
//    }
    public ResponseEntity<Transaction> updateTransactionFeedback(TransactionRequestFeedback requestFeedback) {
        Long id = requestFeedback.getTransactionId();
        Optional<Transaction> checkTransaction = transactionRepository.findById(id);

        if (checkTransaction.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Transaction transaction = checkTransaction.get();
        String newFeedback = requestFeedback.getFeedback();
        Result currentResult = transaction.getResult();
        String currentFeedback = transaction.getFeedback();
        System.out.println("FInd requestFeedback, current Result, and current Feedback");
        System.out.println(newFeedback);
        System.out.println(currentResult);
        System.out.println(currentFeedback);

        if (newFeedback.isEmpty() || !isValidFeedback(newFeedback)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if (!currentFeedback.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

//        if (currentResult == Result.ALLOWED && ) {
//            System.out.println("because current Result is allowed");
//            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
//        }

        if (newFeedback.toString().equals(currentFeedback) || newFeedback.toString().equals(currentResult.toString())) {
            System.out.println("Because new Feedback and current feedback same, or newfeedback=result");
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }

//        if (currentResult == Result.MANUAL_PROCESSING && newFeedback.equals("PROHIBITED")) {
//            System.out.println("currentResult is Manua; and feedback is Prohibited");
//            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
//        }

        transaction.setFeedback(stringToFeedback(newFeedback));
        //Transaction updatedTransaction = transactionRepository.save(transaction);
        changeLimit(transaction, newFeedback);

        return ResponseEntity.ok(transaction);
    }

    /**
     * Helper method to change the limit for a certain transaction based on the provided feedback.
     *
     * @param transaction The transaction to be checked
     * @param feedback    The feedback to be provided
     */
    private void changeLimit(Transaction transaction, String feedback) {
        String trResult = String.valueOf(transaction.getResult());
        //StolenCard card = stolenCardServices.findCardByNumber(transaction.getNumber());
        Card card = cardRepository.findLatestByNumber(transaction.getNumber());


        // Formula for increasing the limit: new_limit = 0.8 * current_limit + 0.2 * value_from_transaction
        int increasedAllowed = (int) Math.ceil(0.8 * card.getMaxAllowed() + 0.2 * transaction.getAmount());
        int decreasedAllowed = (int) Math.ceil(0.8 * card.getMaxAllowed() - 0.2 * transaction.getAmount());
        int increasedManual = (int) Math.ceil(0.8 * card.getMaxManual() + 0.2 * transaction.getAmount());
        int decreasedManual = (int) Math.ceil(0.8 * card.getMaxManual() - 0.2 * transaction.getAmount());

        // Set the new limit based on the feedback
        if (feedback.equals("MANUAL_PROCESSING") && trResult.equals("ALLOWED")) {
            card.setMaxAllowed(decreasedAllowed);
        } else if (feedback.equals("PROHIBITED") && trResult.equals("ALLOWED")) {
            card.setMaxAllowed(decreasedAllowed);
            card.setMaxManual(decreasedManual);
        } else if (feedback.equals("ALLOWED") && trResult.equals("MANUAL_PROCESSING")) {
            card.setMaxAllowed(increasedAllowed);
        } else if (feedback.equals("PROHIBITED") && trResult.equals("MANUAL_PROCESSING")) {
            card.setMaxManual(decreasedManual);
        } else if (feedback.equals("ALLOWED") && trResult.equals("PROHIBITED")) {
            card.setMaxAllowed(increasedAllowed);
            card.setMaxManual(increasedManual);
        } else if (feedback.equals("MANUAL_PROCESSING") && trResult.equals("PROHIBITED")) {
            card.setMaxManual(increasedManual);
        }
        cardRepository.save(card);
        System.out.println(card.toString());
        System.out.println("After changing limit");

    }

    public ResponseEntity<List<Transaction>> getTransactionByNumber(String number) {
        if (!stolenCardServices.isValidCardNumber(number)) {
            System.out.println("NOTVALID");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if (!stolenCardServices.isCardStolen(number)) {
            System.out.println("NOTFOUND");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        System.out.println("ELSECASE");
        List<Transaction> transactionList = transactionRepository.findAllByNumber(number);
        return ResponseEntity.ok(transactionList);
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
    public void validateTransaction(TransactionRequest request) {
        this.request = request;
        this.errors = new ArrayList<>();
        this.result = Result.ALLOWED;
        Long maxAllowed = 200l;
        Long maxManual = 1500l;
//       Transaction latestTransaction = transactionRepository.findFirstByOrderByDateDesc();
//        //Transaction latestTransaction = transactionRepository.findFirstByOrderByTransactionIdDesc();
//        if (latestTransaction != null) {
//            maxAllowed = (long) latestTransaction.getMaxAllowed();
//            System.out.println(maxAllowed);
//            System.out.println("MAXALLOWED");
//            System.out.println("Transation ID");
//            System.out.println(latestTransaction.getTransactionId());
//            maxManual = (long) latestTransaction.getMaxManual();
//        }
        Long amount = request.getAmount();



        if (isIllegalTransaction()) {
            return;
        }

        for (String field : getAllResults(amount, maxAllowed, maxManual).keySet()) {
            Result fieldResult = getAllResults(amount, maxAllowed, maxManual).get(field);

            if ((fieldResult == Result.MANUAL_PROCESSING && this.result == Result.PROHIBITED) ||
                    fieldResult == Result.ALLOWED) {
                continue;
            }

            if (fieldResult == Result.PROHIBITED && this.result != Result.PROHIBITED) {
                this.errors = new ArrayList<>();
            }

            this.result = fieldResult;
            System.out.println("FINAL RESULT");
            System.out.println(this.result);

            this.errors.add(field);
        }

    }

    public ResponseEntity<TransactionResponse> getValidatedTransaction(TransactionRequest request) {
        this.request = request;
        this.errors = new ArrayList<>();
        this.result = Result.ALLOWED;
        Long maxAllowed = 200l;
        Long maxManual = 1500l;
//       Transaction latestTransaction = transactionRepository.findFirstByOrderByDateDesc();
//        //Transaction latestTransaction = transactionRepository.findFirstByOrderByTransactionIdDesc();
//        if (latestTransaction != null) {
//            maxAllowed = (long) latestTransaction.getMaxAllowed();
//            System.out.println(maxAllowed);
//            System.out.println("MAXALLOWED");
//            System.out.println("Transation ID");
//            System.out.println(latestTransaction.getTransactionId());
//            maxManual = (long) latestTransaction.getMaxManual();
//        }
        Long amount = request.getAmount();
        String authenticatedUsername = authentication.getName();
        User authenticatedUser = userService.findUserByUsername(authenticatedUsername);
        String authenticatedRoleName = authenticatedUser.getRole().getName();
        System.out.println("Authenticated User: " + authenticatedUser.toString());
        Operation authenticatedOperation = authenticatedUser.getOperation();
        if (isIllegalTransaction()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }


        Transaction transaction = new Transaction(request.getAmount(), request.getIp(), request.getNumber(),
                request.getRegion(),
                request.getDate());
        transaction = transactionRepository.save(transaction);
        Long savedTransactionId = transaction.getTransactionId();
        String savedNumber = transaction.getNumber();
        Card card = new Card();
        if (!cardRepository.existsByNumber(savedNumber)) {
            card.setTransactionId(savedTransactionId); // Set the saved transactionId
            card.setNumber(savedNumber);
            System.out.println(card.toString());
            System.out.println("Am I getting the changed limit");
            //card.setMaxManual(Math.toIntExact((long) maxManual));
            //card.setMaxAllowed(Math.toIntExact(maxAllowed));
            card = cardRepository.save(card);
        } else {
            card = cardRepository.findLatestByNumber(savedNumber);
            //card = cardRepository.findByTransactionId(savedTransactionId);
            maxAllowed = (long)card.getMaxAllowed();
            maxManual = (long)card.getMaxManual();
        }



        System.out.println(maxAllowed);
            System.out.println("MAXALLOWED");
            System.out.println("Transation ID");
            System.out.println(transaction.getTransactionId());

        for (String field : getAllResults(amount, maxAllowed, maxManual).keySet()) {
            Result fieldResult = getAllResults(amount, maxAllowed, maxManual).get(field);

            if ((fieldResult == Result.MANUAL_PROCESSING && this.result == Result.PROHIBITED) ||
                    fieldResult == Result.ALLOWED) {
                continue;
            }

            if (fieldResult == Result.PROHIBITED && this.result != Result.PROHIBITED) {
                this.errors = new ArrayList<>();
            }

            this.result = fieldResult;
            System.out.println("FINAL RESULT");
            System.out.println(this.result);

            this.errors.add(field);
        }


        transaction.setResult(result);


        transactionRepository.save(transaction);
        String checkInfo = getInfo();
        if (result == Result.ALLOWED)  checkInfo = "none";
        if (authenticatedOperation.equals(Operation.UNLOCK)) {

            return ResponseEntity.ok(new TransactionResponse(result, checkInfo));
        } else {
            transactionRepository.delete(transaction);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new TransactionResponse(result, checkInfo));
        }

        //return ResponseEntity.ok(new TransactionResponse(result, getInfo()));
    }


    private boolean isIllegalTransaction() {
        return !ipAddressServices.isValidIpAddress(request.getIp())
                || !stolenCardServices.isValidCardNumber(request.getNumber())
                || !isValidAmount(request.getAmount())
                || !isValidDate(request.getDate())
                || !isValidRegion(request.getRegion());
    }

    private Map<String, Result> getAllResults(Long amount, Long maxAllowed, Long maxManual) {
        //Long amount = request.getAmount();
//        Long maxAllowed = (long)transaction.getMaxAllowed();
//        Long maxManual = (long)transaction.getMaxManual();
        System.out.println("Enter GET ALL RESULTS");
        Map<String, Result> allResults = new HashMap<>();
        System.out.println(getResultByAmount(amount, maxAllowed, maxManual));
        System.out.println("RESULT BY CHECKING AMOUNT");
        System.out.println(getResultByCardNumber());
        System.out.println("RESULT BY getResultByCardNumber");
        System.out.println(getResultBySuspiciousIp());
        System.out.println("RESULT BY getResultBySuspiciousIp");
        System.out.println(getResultByIp());
        System.out.println("RESULT BY getResultByIp");
        System.out.println(getResultByRegion());
        System.out.println("RESULT BY getResultByRegion");
        allResults.put("amount", getResultByAmount(amount, maxAllowed, maxManual));

        allResults.put("card-number", getResultByCardNumber());
        allResults.put("ip", getResultBySuspiciousIp());
        allResults.put("ip-correlation", getResultByIp());
        allResults.put("region-correlation", getResultByRegion());

        return allResults;
    }

    private Result getResultBySuspiciousIp() {
        return !ipAddressServices.isIpSuspicious(request.getIp()) ? Result.ALLOWED
                : Result.PROHIBITED;
//        return !suspiciousIpRepository.existsByIp(request.getIP()) ? "ALLOWED"
//                : "PROHIBITED";
    }

    private Result getResultByCardNumber() {
        return !stolenCardServices.isCardStolen(request.getNumber()) ? Result.ALLOWED
                : Result.PROHIBITED;
//        return !stolenCardRepository.existsByNumber(request.getNumber()) ? "ALLOWED"
//                : "PROHIBITED";
    }

    private Result getResultByAmount(Long amount, Long maxAllowed, Long maxManual) {
        System.out.println("ENTER AMOUNT RESULT");
//        System.out.println(request.getAmount());
//        Transaction checkTransaction = transactionRepository.findByIp(request.getIp());
//        System.out.println(checkTransaction);
//        System.out.println("Checking Transaction from Ip");
//        Long checkMaxAllowed = (long) checkTransaction.getMaxAllowed();
//        Long checkMaxManual = (long) checkTransaction.getMaxManual();
        System.out.println("AMOUNT MAXALLOW AND MAXMANUAL INSIDE getResultByAmount");
        System.out.println(amount);
        System.out.println(maxAllowed);
        System.out.println(maxManual);
        if (amount<= maxAllowed) {
            System.out.println("AMOUNT IS LESS AND EQUAL THAN 200");
            return Result.ALLOWED;
        } else if (amount <= maxManual) {
            System.out.println("AMOUNT IS LESS AND EQUAL THAN 1500");
            return Result.MANUAL_PROCESSING;
        }
        System.out.println("AMOUNT IS MORE THAN 1500");
        return Result.PROHIBITED;
//        return request.getAmount() <= 200 ?  Result.ALLOWED
//                : request.getAmount() <= 1500 ? Result.MANUAL_PROCESSING
//                : Result.PROHIBITED;
    }

    private Result getResultByRegion() {
        List<Region> regions = new ArrayList<>();
        int count = 0;

        for (Transaction transaction : getTransactionsInLastHours()) {
            if (!transaction.getRegion().equals(request.getRegion()) && !regions.contains(transaction.getRegion())) {
                regions.add(transaction.getRegion());
                count++;
            }
        }

        return count < 2 ? Result.ALLOWED : count == 2 ? Result.MANUAL_PROCESSING : Result.PROHIBITED;
    }

    private Result getResultByIp() {
        List<String> ips = new ArrayList<>();
        int count = 0;

        for (Transaction transaction : getTransactionsInLastHours()) {
            if (!transaction.getIp().equals(request.getIp()) && !ips.contains(transaction.getIp())) {
                ips.add(transaction.getIp());
                count++;
            }
        }

        return count < 2 ? Result.ALLOWED : count == 2 ? Result.MANUAL_PROCESSING : Result.PROHIBITED;
    }

    private List<Transaction> getTransactionsInLastHours() {
        LocalDateTime requestDateTime = request.getDate();
        LocalDateTime requestDateTimeMinusOneHour = requestDateTime.minusHours(1);

        return transactionRepository
                .findByNumberAndDateBetween(request.getNumber(), requestDateTimeMinusOneHour, requestDateTime);
    }

    private String getInfo() {
        return this.errors.isEmpty() ? "none"
                : errors
                .stream()
                .sorted()
                .collect(Collectors.joining(", "));
    }
    public static boolean isValidAmount(Long amount) {
        return amount != null && amount > 0;
    }

    public static boolean isValidRegion(Region region) {
        String region1 = region.toString();
        return region1.matches("EAP|ECA|HIC|LAC|MENA|SA|SSA");
    }

    public static boolean isValidDate(LocalDateTime dates) {
        String date = dates.toString();
        try {
            LocalDateTime.parse(date);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
    public ResponseEntity<TransactionResponse> processTransaction(TransactionRequest request, Authentication authentication) {
        this.authentication = authentication;
        this.request = request;
        //validateTransaction(request);

        return getValidatedTransaction(request);
    }
    public Feedback stringToFeedback(String value) {
        if (value.equals("PROHIBITED")) {
            return Feedback.PROHIBITED;
        } else if (value.equals("MANUAL_PROCESSING")) {
            return Feedback.MANUAL_PROCESSING;
        }
        return Feedback.ALLOWED;
    }

        public static boolean isValidFeedback(String value) {
            for (Feedback feedback : Feedback.values()) {
                if (feedback.name().equals(value)) {
                    return true;
                }
            }
            return false;
        }


}
