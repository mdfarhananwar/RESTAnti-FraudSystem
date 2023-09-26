package antifraud.dao.transactionDao;

import antifraud.models.transactionModel.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Repository interface for performing CRUD operations on Card entities.
 */
public interface CardRepository extends JpaRepository<Card, Long> {

    /**
     * Check if a card with the specified number exists.
     *
     * @param number The card number to check.
     * @return true if a card with the specified number exists, false otherwise.
     */
    boolean existsByNumber(String number);

    /**
     * Find the latest Card entity with the specified card number.
     *
     * @param number The card number to search for.
     * @return The latest Card entity with the specified card number, or null if not found.
     */
    @Query("SELECT c FROM Card c " +
            "WHERE c.number = ?1 " +
            "ORDER BY c.transactionId DESC")
    Card findLatestByNumber(String number);
}
