package antifraud.dao.transactionDao;

import antifraud.models.transactionModel.StolenCard;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Repository interface for performing CRUD operations on StolenCard entities.
 */
public interface StolenCardRepository extends JpaRepository<StolenCard, Long> {

    /**
     * Check if a stolen card with the specified card number exists.
     *
     * @param number The card number to check.
     * @return true if a stolen card with the specified card number exists, false otherwise.
     */
    boolean existsByNumber(String number);

    /**
     * Delete a stolen card entry with the specified card number.
     *
     * @param number The card number to delete.
     */
    void deleteByNumber(String number);

    /**
     * Find all stolen cards and order them by card number in ascending order.
     *
     * @return A list of StolenCard entities ordered by card number in ascending order.
     */
    List<StolenCard> findAllByOrderByNumberAsc();

}