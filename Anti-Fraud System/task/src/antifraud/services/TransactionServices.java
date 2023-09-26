package antifraud.services;

import antifraud.dao.transactionDao.CardRepository;
import antifraud.dao.transactionDao.TransactionRepository;
import antifraud.models.transactionModel.*;
import antifraud.models.userModel.Operation;
import antifraud.models.userModel.User;
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

    private final UserService userService;
    private final IpAddressServices ipAddressServices;
    private final StolenCardServices stolenCardServices;
    private final TransactionRepository transactionRepository;
    private final CardRepository cardRepository;
    Authentication authentication;
    private TransactionRequest request;
    private List<String> errors;



    @Autowired
    public TransactionServices(
            UserService userService,
            IpAddressServices ipAddressServices,
            StolenCardServices stolenCardServices,
            TransactionRepository transactionRepository,
            CardRepository cardRepository) {
        this.userService = userService;
        this.ipAddressServices = ipAddressServices;
        this.stolenCardServices = stolenCardServices;
        this.transactionRepository = transactionRepository;
        this.cardRepository = cardRepository;
    }
    /**
     * Validate a transaction request based on transaction amount, IP address, and card number.
     *
     * @param request The transaction request object containing transaction details.
     * @return ResponseEntity with the status and the result of the transaction validation.
     */
    public ResponseEntity<TransactionResponse> processTransaction(
            TransactionRequest request,
            Authentication authentication) {

        // Set the request and initialize the errors list
        this.authentication = authentication;
        this.request = request;

        // return response from helper method
        return getValidatedTransaction(request);
    }


    /**
     * Retrieve the transaction history sorted by transaction ID in ascending order.
     *
     * @return ResponseEntity with the status and the list of transaction history.
     */
    public ResponseEntity<List<Transaction>> getListOfTransactions() {
        // Retrieve the transaction history sorted by transaction ID in ascending order
        List<Transaction> transactionList = transactionRepository.findAllByOrderByTransactionIdAsc();

        // Check if the history is empty, and respond accordingly
        if (transactionList.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }

        // Respond with the HTTP OK status (200) and the transaction history
        return ResponseEntity.ok(transactionList);
    }

    /**
     * Retrieve the transaction history for a specified card number.
     *
     * @param number The card number for which the transaction history is requested.
     * @return ResponseEntity with the status and the list of transaction history for the specified card number.
     */
    public ResponseEntity<List<Transaction>> getTransactionByNumber(String number) {
        // Check if the card number is not valid
        if (stolenCardServices.isCardNotValid(number)) {
            return ResponseEntity.badRequest().build();
        }

        // Check if the card number is not stolen
        if (stolenCardServices.isCardNotStolen(number)) {
            return ResponseEntity.notFound().build();
        }

        // Retrieve the transaction history for the specified card number from the repository
        List<Transaction> transactionList = transactionRepository.findAllByNumber(number);

        // Check if transactions were found for the card number
        if (transactionList.isEmpty()) {
            // Respond with the HTTP Not Found status (404) if no transactions were found
            return ResponseEntity.notFound().build();
        }

        // Respond with the HTTP OK status (200) and the transaction history for the specified card number
        return ResponseEntity.ok(transactionList);
    }

    /**
     * Update feedback for a specific transaction and adjust transaction limits.
     *
     * @param requestFeedback The request object containing transaction ID and feedback.
     * @return ResponseEntity with the status and the updated transaction details.
     */
    @Transactional
    public ResponseEntity<Transaction> updateTransactionFeedback(TransactionRequestFeedback requestFeedback) {
        // Retrieve the transaction ID from the request
        Long id = requestFeedback.getTransactionId();

        // Check if the transaction with the specified ID exists
        Optional<Transaction> checkTransaction = transactionRepository.findById(id);

        if (checkTransaction.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Transaction transaction = checkTransaction.get();
        String newFeedback = requestFeedback.getFeedback();
        Result currentResult = transaction.getResult();
        String currentFeedback = transaction.getFeedback();

        // Check if the new feedback is empty or has an invalid format
        if (newFeedback.isEmpty() || !isValidFeedback(newFeedback)) {
            return ResponseEntity.badRequest().build();
        }

        // Check if the transaction already has feedback
        if (!currentFeedback.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        // Check if the new feedback is the same as the current feedback or result
        if (newFeedback.equals(currentFeedback) || newFeedback.equals(currentResult.toString())) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }

        // Set the new feedback and adjust transaction limits
        transaction.setFeedback(stringToFeedback(newFeedback));
        changeLimit(transaction, newFeedback);

        // Respond with the HTTP OK status (200) and the updated transaction details
        return ResponseEntity.ok(transaction);
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

    public static boolean isValidFeedback(String value) {
        for (Feedback feedback : Feedback.values()) {
            if (feedback.name().equals(value)) {
                return true;
            }
        }
        return false;
    }





    /**
     * Helper method to change the limit for a certain transaction based on the provided feedback.
     *
     * @param transaction The transaction to be checked
     * @param feedback    The feedback to be provided
     */
    private void changeLimit(Transaction transaction, String feedback) {
        String trResult = String.valueOf(transaction.getResult());

        //Finding the latest Card by Number
        //Finding Card by Number can have multiple Transactions having different transactionId
        //Latest Card = Card of that number having the latest transactionId
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
    }



    /**
     * Helper method to Validate a transaction request based on transaction amount, IP address, and card number.
     *
     * @param request The transaction request object containing transaction details.
     * @return ResponseEntity with the status and the result of the transaction validation.
     */
    public ResponseEntity<TransactionResponse> getValidatedTransaction(TransactionRequest request) {
        // Set the request and initialize the errors list
        this.request = request;
        this.errors = new ArrayList<>();

        // Initialize variables for transaction validation
        Result result = Result.ALLOWED;
        Long maxAllowed = 200L;
        Long maxManual = 1500L;
        Long amount = request.getAmount();

        // Get the authenticated user's information
        String authenticatedUsername = authentication.getName();
        User authenticatedUser = userService.findUserByUsername(authenticatedUsername);
        Operation authenticatedOperation = authenticatedUser.getOperation();

        // Check if the transaction is illegal
        if (isIllegalTransaction()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        // Create a transaction object and save it
        Transaction transaction = new Transaction(
                request.getAmount(),
                request.getIp(),
                request.getNumber(),
                request.getRegion(),
                request.getDate());
        transaction = transactionRepository.save(transaction);
        Long savedTransactionId = transaction.getTransactionId();
        String savedNumber = transaction.getNumber();
        Card card = new Card();

        // Check if the card number exists in the database
        if (!cardRepository.existsByNumber(savedNumber)) {
            card.setTransactionId(savedTransactionId); // Set the saved transactionId
            card.setNumber(savedNumber);
            cardRepository.save(card);
        } else {
            card = cardRepository.findLatestByNumber(savedNumber);
            maxAllowed = (long) card.getMaxAllowed();
            maxManual = (long) card.getMaxManual();
        }

        // Iterate through validation results for amount, maxAllowed, and maxManual
        for (String field : getAllResults(amount, maxAllowed, maxManual).keySet()) {
            // Determine the transaction result based on validation results
            Result fieldResult = getAllResults(amount, maxAllowed, maxManual).get(field);
            if ((fieldResult == Result.MANUAL_PROCESSING && result == Result.PROHIBITED) ||
                    fieldResult == Result.ALLOWED) {
                continue;
            }

            if (fieldResult == Result.PROHIBITED && result != Result.PROHIBITED) {
                this.errors = new ArrayList<>();
            }

            result = fieldResult;
            this.errors.add(field);
        }

        // Update the transaction result and save it
        transaction.setResult(result);
        transactionRepository.save(transaction);

        // Get the error information
        String checkInfo = getInfo();
        if (result == Result.ALLOWED) checkInfo = "none";

        // Respond based on the authenticated operation
        if (authenticatedOperation.equals(Operation.UNLOCK)) {
            return ResponseEntity.ok(new TransactionResponse(result, checkInfo));
        } else {
            transactionRepository.delete(transaction);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new TransactionResponse(result, checkInfo));
        }
    }



// Helper methods


    private boolean isIllegalTransaction() {
        return ipAddressServices.isIpNotValid(request.getIp())
                || stolenCardServices.isCardNotValid(request.getNumber())
                || !isValidAmount(request.getAmount())
                || !isValidDate(request.getDate())
                || !isValidRegion(request.getRegion());
    }

    private Map<String, Result> getAllResults(Long amount, Long maxAllowed, Long maxManual) {
        Map<String, Result> allResults = new HashMap<>();
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
    }

    private Result getResultByCardNumber() {
        return stolenCardServices.isCardNotStolen(request.getNumber()) ? Result.ALLOWED
                : Result.PROHIBITED;
    }

    private Result getResultByAmount(Long amount, Long maxAllowed, Long maxManual) {
        if (amount <= maxAllowed) {
            return Result.ALLOWED;
        } else if (amount <= maxManual) {
            return Result.MANUAL_PROCESSING;
        }
        return Result.PROHIBITED;
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

    public Feedback stringToFeedback(String value) {
        if (value.equals("PROHIBITED")) {
            return Feedback.PROHIBITED;
        } else if (value.equals("MANUAL_PROCESSING")) {
            return Feedback.MANUAL_PROCESSING;
        }
        return Feedback.ALLOWED;
    }


}
