package antifraud.dao.transactionDao;

import antifraud.models.transactionModel.IpAddress;
import antifraud.models.transactionModel.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByDateAfter(Date date);

    @Query("SELECT t FROM Transaction t WHERE t.date BETWEEN ?1 AND ?2")
    List<Transaction> findTransactionsAfterTime(LocalDateTime oneHourAgo, LocalDateTime fromTransaction);
    List<Transaction> findAllByOrderByTransactionIdAsc();
    List<Transaction> findAllByNumber(String number);
    List<Transaction> findByNumberAndDateBetween(String number, LocalDateTime startTime, LocalDateTime endTime);
    Transaction findByIp(String ip);
    @Query("SELECT t FROM Transaction t ORDER BY t.date DESC")
    Transaction findLatestTransaction();
    Transaction findFirstByOrderByDateDesc();
    Transaction findFirstByOrderByTransactionIdDesc();
}
