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

    @Transactional
    public ResponseEntity<StolenCard> saveStolenCard(StolenCardRequest stolenCardRequest) {
        String number = stolenCardRequest.getNumber();
        if (isCardNotValid(number)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if (stolenCardRepository.existsByNumber(number)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        StolenCard stolenCard = new StolenCard();
        stolenCard.setNumber(number);
        StolenCard savedStolenCard = stolenCardRepository.save(stolenCard);
        return ResponseEntity.ok(savedStolenCard);
    }

    @Transactional
    public ResponseEntity<DeleteResponse> deleteStolenCard(String number) {

        if (isCardNotValid(number)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if (!stolenCardRepository.existsByNumber(number)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        stolenCardRepository.deleteByNumber(number);
        String status = "Card " + number + " successfully removed!";
        return ResponseEntity.ok(new DeleteResponse(status));

    }

    public ResponseEntity<List<StolenCard>> getListOfStolenCard() {
        List<StolenCard> listOfStolenCards = stolenCardRepository.findAllByOrderByNumberAsc();
        return ResponseEntity.ok(listOfStolenCards);
    }

    public boolean isCardNotValid(String number) {
        // Remove all non-digit characters from the input string
        String cleanedNumber = number.replaceAll("\\D", "");

        int sum = 0;
        boolean alternate = false;

        // Iterate through the digits in reverse order
        for (int i = cleanedNumber.length() - 1; i >= 0; i--) {
            int digit = Integer.parseInt(cleanedNumber.substring(i, i + 1));

            if (alternate) {
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
            }

            sum += digit;
            alternate = !alternate;
        }

        return sum % 10 != 0;
    }

    public boolean isCardNotStolen(String number) {
        return !stolenCardRepository.existsByNumber(number);
    }
}
