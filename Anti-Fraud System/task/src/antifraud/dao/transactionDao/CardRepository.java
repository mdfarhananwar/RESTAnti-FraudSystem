package antifraud.dao.transactionDao;

import antifraud.models.transactionModel.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CardRepository extends JpaRepository<Card, Long> {
    boolean existsByNumber(String number);
    @Query("SELECT c FROM Card c " +
            "WHERE c.number = ?1 " +
            "ORDER BY c.transactionId DESC")
    Card findLatestByNumber(String number);
}
