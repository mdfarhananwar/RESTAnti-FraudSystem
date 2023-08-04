package antifraud.dao.transactionDao;

import antifraud.models.transactionModel.StolenCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StolenCardRepository extends JpaRepository<StolenCard, Long> {
    StolenCard findByNumber(String number);
    boolean existsByNumber(String number);
    void deleteByNumber(String number);
    List<StolenCard> findAllByOrderByNumberAsc();

}