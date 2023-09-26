package antifraud.dao.transactionDao;

import antifraud.models.transactionModel.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for performing CRUD operations on Transaction entities.
 */
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    /**
     * Find all transactions and order them by transaction ID in ascending order.
     *
     * @return A list of Transaction entities ordered by transaction ID in ascending order.
     */
    List<Transaction> findAllByOrderByTransactionIdAsc();

    /**
     * Find all transactions associated with a specific card number.
     *
     * @param number The card number to filter transactions by.
     * @return A list of Transaction entities associated with the specified card number.
     */
    List<Transaction> findAllByNumber(String number);

    /**
     * Find transactions associated with a specific card number within a given time range.
     *
     * @param number     The card number to filter transactions by.
     * @param startTime  The start time of the time range.
     * @param endTime    The end time of the time range.
     * @return A list of Transaction entities associated with the specified card number within the given time range.
     */
    List<Transaction> findByNumberAndDateBetween(String number, LocalDateTime startTime, LocalDateTime endTime);

}
