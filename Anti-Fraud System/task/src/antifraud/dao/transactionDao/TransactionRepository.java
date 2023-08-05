package antifraud.dao.transactionDao;

import antifraud.models.transactionModel.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByDateAfter(Date date);

    @Query("SELECT t FROM Transaction t WHERE t.date BETWEEN ?1 AND ?2")
    List<Transaction> findTransactionsAfterTime(Date oneHourAgo, Date fromTransaction);
}
