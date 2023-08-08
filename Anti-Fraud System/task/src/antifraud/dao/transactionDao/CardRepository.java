package antifraud.dao.transactionDao;

import antifraud.models.transactionModel.Card;
import antifraud.models.transactionModel.StolenCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CardRepository extends JpaRepository<Card, Long> {
    Card findByNumber(String number);
    boolean existsByNumber(String number);
    boolean existsByTransactionId(Long transactionId);
    Card findByTransactionId(Long transactionId);
    @Query("SELECT c FROM Card c " +
            "WHERE c.number = ?1 " +
            "ORDER BY c.transactionId DESC")
    Card findLatestByNumber(String number);
}
