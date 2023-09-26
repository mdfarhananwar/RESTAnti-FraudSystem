package antifraud.controller;

import antifraud.models.transactionModel.DeleteResponse;
import antifraud.models.transactionModel.StolenCard;
import antifraud.models.transactionModel.StolenCardRequest;
import antifraud.services.StolenCardServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Controller for managing stolen card data.
 */
@RestController
public class StolenCardController {

    private final StolenCardServices stolenCardServices;

    @Autowired
    public StolenCardController(StolenCardServices stolenCardServices) {
        this.stolenCardServices = stolenCardServices;
    }

    /**
     * Save stolen card data to the database.
     * <p>
     * POST /api/antifraud/stolencard
     * Accepts data in JSON format:
     * {
     *   "number": "<String value, not empty>"
     * }
     * Responds with:
     * - HTTP Created status (201) and the saved stolen card information if successful. Response Body :
     * {
     *    "id": "<Long value, not empty>",
     *    "number": "<String value, not empty>"
     * }
     * - HTTP Conflict status (409) if the card number is already in the database.
     * - HTTP Bad Request status (400) if the card number has the wrong format.
     *
     * @param stolenCardRequest The stolen card data to be saved.
     * @return ResponseEntity containing the result of the stolen card data saving.
     */
    @PostMapping("/api/antifraud/stolencard")
    public ResponseEntity<StolenCard> saveStolenCard(@RequestBody StolenCardRequest stolenCardRequest) {
        return stolenCardServices.saveStolenCard(stolenCardRequest);
    }

    /**
     * Delete a card number from the database.
     * <p>
     * DELETE /api/antifraud/stolencard/{number}
     * Deletes the card number specified by {number}.
     * Responds with:
     * - HTTP OK status (200) and a message indicating successful removal. Response Body :
     * {
     *    "status": "Card <number> successfully removed!"
     * }
     * - HTTP Not Found status (404) if the card number is not found in the database.
     * - HTTP Bad Request status (400) if the card number has the wrong format.
     *
     * @param number The card number to be deleted.
     * @return ResponseEntity containing the result of the card number deletion.
     */
    @DeleteMapping("/api/antifraud/stolencard/{number}")
    public ResponseEntity<DeleteResponse> deleteStolenCard(@PathVariable String number) {
        return stolenCardServices.deleteStolenCard(number);
    }

    /**
     * Get a list of stolen card numbers.
     * <p>
     * GET /api/antifraud/stolencard
     * Responds with:
     * - HTTP OK status (200) and an array of JSON objects representing card numbers sorted by ID in ascending order.
     *  Response Body:
     *  [
     *     {
     *         "id": 1,
     *         "number": "4000008449433403"
     *     },
     *      ...
     *     {
     *         "id": 100,
     *         "number": "4000009455296122"
     *     }
     * ]
     * - An empty JSON array if the database is empty.
     *
     * @return ResponseEntity containing the list of stolen card numbers.
     */
    @GetMapping("/api/antifraud/stolencard")
    public ResponseEntity<List<StolenCard>> getListOfCard() {
        return stolenCardServices.getListOfStolenCard();
    }
}
