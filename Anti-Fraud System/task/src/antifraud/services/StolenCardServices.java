package antifraud.services;

import antifraud.dao.transactionDao.StolenCardRepository;
import antifraud.models.transactionModel.DeleteResponse;
import antifraud.models.transactionModel.StolenCard;
import antifraud.models.transactionModel.StolenCardRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StolenCardServices {

    private final StolenCardRepository stolenCardRepository;

    @Autowired
    public StolenCardServices(StolenCardRepository stolenCardRepository) {
        this.stolenCardRepository = stolenCardRepository;
    }

    /**
     * Save stolen card data to the database.
     *
     * @param stolenCardRequest The stolen card request object containing the card number.
     * @return ResponseEntity with the status and the saved stolen card.
     */
    @Transactional
    public ResponseEntity<StolenCard> saveStolenCard(StolenCardRequest stolenCardRequest) {
        // Extract the card number from the request
        String cardNumber = stolenCardRequest.getNumber();

        // Check if the provided card number is in a valid format
        if (isCardNotValid(cardNumber)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        // Check if the card number already exists in the database
        if (stolenCardRepository.existsByNumber(cardNumber)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        // Create a new StolenCard object and save it to the database
        StolenCard stolenCard = new StolenCard();
        stolenCard.setNumber(cardNumber);
        StolenCard savedStolenCard = stolenCardRepository.save(stolenCard);

        // Respond with HTTP Ok and the response body
        return ResponseEntity.ok(savedStolenCard);
    }

    /**
     * Delete a stolen card number from the database.
     *
     * @param number The card number to be deleted.
     * @return ResponseEntity with the status and a response message.
     */
    @Transactional
    public ResponseEntity<DeleteResponse> deleteStolenCard(String number) {
        // Check if the provided card number is in a valid format
        if (isCardNotValid(number)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        // Check if the card number exists in the database
        if (!stolenCardRepository.existsByNumber(number)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Delete the card number from the database
        stolenCardRepository.deleteByNumber(number);

        // Prepare the response message
        String status = "Card " + number + " successfully removed!";
        DeleteResponse response = new DeleteResponse(status);

        // Respond with HTTP OK and the response body
        return ResponseEntity.ok(response);
    }

    /**
     * Get a list of stolen card numbers stored in the database.
     *
     * @return ResponseEntity with the status and a list of stolen card numbers.
     */
    public ResponseEntity<List<StolenCard>> getListOfStolenCard() {
        // Retrieve a list of stolen card numbers sorted by ID in ascending order
        List<StolenCard> listOfStolenCards = stolenCardRepository.findAllByOrderByNumberAsc();

        // Respond with HTTP OK and the list of stolen card numbers
        return ResponseEntity.ok(listOfStolenCards);
    }

    // Helper Methods :-----------------------------------------------------------------------------------------------

    /**
     * Check the validity of a credit card number using the Luhn algorithm.
     *
     * @param number The credit card number to be validated.
     * @return {@code true} if the card number is valid, {@code false} otherwise.
     */
    public boolean isCardNotValid(String number) {
        // Remove all non-digit characters from the input string
        String cleanedNumber = number.replaceAll("\\D", "");

        int sum = 0;
        boolean alternate = false;

        // Iterate through the digits in reverse order
        for (int i = cleanedNumber.length() - 1; i >= 0; i--) {
            int digit = Integer.parseInt(cleanedNumber.substring(i, i + 1));

            // Double every other digit and subtract 9 if it's greater than 9
            if (alternate) {
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
            }
            // Add the digit to the running sum
            sum += digit;
            // Toggle the 'alternate' flag to double every other digit
            alternate = !alternate;
        }
        // The card number is valid if the sum is a multiple of 10
        return sum % 10 != 0;
    }

    public boolean isCardNotStolen(String number) {
        return !stolenCardRepository.existsByNumber(number);
    }
}
