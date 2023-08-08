package antifraud.dao.transactionDao;

import antifraud.models.transactionModel.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllByOrderByTransactionIdAsc();
    List<Transaction> findAllByNumber(String number);
    List<Transaction> findByNumberAndDateBetween(String number, LocalDateTime startTime, LocalDateTime endTime);

}
