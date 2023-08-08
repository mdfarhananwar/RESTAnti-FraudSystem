package antifraud.dao.transactionDao;

        import antifraud.models.transactionModel.IpAddress;
        import org.springframework.data.jpa.repository.JpaRepository;

        import java.util.List;

public interface IpAddressRepository extends JpaRepository<IpAddress, Long> {
    boolean existsByIp(String ip);
    void deleteByIp(String ip);

    List<IpAddress> findAllByOrderByIdAsc();

}
