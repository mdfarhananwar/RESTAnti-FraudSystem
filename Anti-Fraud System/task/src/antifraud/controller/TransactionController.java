package antifraud.controller;

import antifraud.models.transactionModel.Transaction;
import antifraud.models.transactionModel.TransactionRequest;
import antifraud.models.transactionModel.TransactionRequestFeedback;
import antifraud.services.TransactionServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Controller for managing transactions.
 */
@RestController
public class TransactionController {


    private final TransactionServices transactionServices;

    @Autowired
    public TransactionController(TransactionServices transactionServices) {
        this.transactionServices = transactionServices;
    }

    /**
     * Calculate whether a transaction is ALLOWED, PROHIBITED, or requires MANUAL_PROCESSING based on the transaction amount.
     * <p>
     * POST /api/antifraud/transaction
     * Accepts data in JSON format:
     * {
     *   "amount": <Long>
     * }
     * Responds with:
     * - HTTP OK (200) and a JSON response indicating the transaction result.
     * - HTTP Bad Request (400) if the request contains incorrect data.
     *
     * @param transaction The transaction request.
     * @param authentication The user's authentication.
     * @return ResponseEntity containing the transaction result.
     */
    @PostMapping("/api/antifraud/transaction")
    public ResponseEntity<?> validAmount(@RequestBody TransactionRequest transaction, Authentication authentication) {
        return transactionServices.processTransaction(transaction, authentication);

    }

    /**
     * Get the transaction history.
     * <p>
     * GET /api/antifraud/history
     * Responds with:
     * - HTTP OK (200) and an array of JSON objects representing transactions sorted by ID in ascending order.
     *  Response Body :
     *  [
     *     {
     *       "transactionId": <Long>,
     *       "amount": <Long>,
     *       "ip": "<String value, not empty>",
     *       "number": "<String value, not empty>",
     *       "region": "<String value, not empty>",
     *       "date": "yyyy-MM-ddTHH:mm:ss",
     *       "result": "<String>",
     *       "feedback": "<String>"
     *     },
     *      ...
     *     {
     *       "transactionId": <Long>,
     *       "amount": <Long>,
     *       "ip": "<String value, not empty>",
     *       "number": "<String value, not empty>",
     *       "region": "<String value, not empty>",
     *       "date": "yyyy-MM-ddTHH:mm:ss",
     *       "result": "<String>",
     *       "feedback": "<String>"
     *     }
     * ]
     * - An empty JSON array if the history is empty.
     *
     * @return ResponseEntity containing the transaction history.
     */
    @GetMapping("/api/antifraud/history")
    public ResponseEntity<List<Transaction>> getHistory() {
        return transactionServices.getListOfTransactions();
    }

    /**
     * Get the transaction history for a specified card number.
     * <p>
     * GET /api/antifraud/history/{number}
     * Responds with:
     * - HTTP OK (200) and an array of JSON objects representing transactions for the specified card number.
     *  Response Body :
     *  [
     *     {
     *       "transactionId": <Long>,
     *       "amount": <Long>,
     *       "ip": "<String value, not empty>",
     *       "number": number,
     *       "region": "<String value, not empty>",
     *       "date": "yyyy-MM-ddTHH:mm:ss",
     *       "result": "<String>",
     *       "feedback": "<String>"
     *     },
     *      ...
     *     {
     *       "transactionId": <Long>,
     *       "amount": <Long>,
     *       "ip": "<String value, not empty>",
     *       "number": number,
     *       "region": "<String value, not empty>",
     *       "date": "yyyy-MM-ddTHH:mm:ss",
     *       "result": "<String>",
     *       "feedback": "<String>"
     *     }
     * ]
     * - HTTP Not Found (404) if transactions for the card number are not found.
     * - HTTP Bad Request (400) if the card number format is incorrect.
     *
     * @param number The card number.
     * @return ResponseEntity containing the transaction history for the specified card number.
     */
    @GetMapping("/api/antifraud/history/{number}")
    public ResponseEntity<List<Transaction>> getTransactionByNumber(@PathVariable String number) {
        return transactionServices.getTransactionByNumber(number);
    }

    /**
     * Update feedback for a transaction.
     * <p>
     * PUT /api/antifraud/transaction
     * Accepts data in JSON format:
     * {
     *   "transactionId": <Long>,
     *   "feedback": "<String>"
     * }
     * Responds with:
     * - HTTP OK (200) and the updated transaction details if successful. Response Body :
     * {
     *   "transactionId": <Long>,
     *   "amount": <Long>,
     *   "ip": "<String value, not empty>",
     *   "number": "<String value, not empty>",
     *   "region": "<String value, not empty>",
     *   "date": "yyyy-MM-ddTHH:mm:ss",
     *   "result": "<String>",
     *   "feedback": "<String>"
     * }
     * - HTTP Conflict (409) if feedback for the transaction already exists.
     * - HTTP Bad Request (400) if the feedback format is incorrect.
     * - HTTP Unprocessable Entity (422) if the feedback throws an exception.
     * - HTTP Not Found (404) if the transaction is not found in history.
     *
     * @param requestFeedback The transaction feedback request.
     * @return ResponseEntity containing the updated transaction details.
     */
    @PutMapping("/api/antifraud/transaction")
    public ResponseEntity<Transaction> updateTransaction(@RequestBody TransactionRequestFeedback requestFeedback) {
        return transactionServices.updateTransactionFeedback(requestFeedback);
    }

    /**
     * Exception handler for handling invalid enum values.
     *
     * @param ex The HttpMessageNotReadableException.
     * @return ResponseEntity with HTTP Bad Request (400) status.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleInvalidEnumValue(HttpMessageNotReadableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

}
