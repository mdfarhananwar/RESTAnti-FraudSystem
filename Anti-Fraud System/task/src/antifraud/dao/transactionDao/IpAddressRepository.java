package antifraud.dao.transactionDao;

import antifraud.models.transactionModel.IpAddress;
        import org.springframework.data.jpa.repository.JpaRepository;

        import java.util.List;
/**
 * Repository interface for performing CRUD operations on IpAddress entities.
 */
public interface IpAddressRepository extends JpaRepository<IpAddress, Long> {

    /**
     * Check if an IP address with the specified IP exists.
     *
     * @param ip The IP address to check.
     * @return true if an IP address with the specified IP exists, false otherwise.
     */
    boolean existsByIp(String ip);

    /**
     * Delete an IP address entry with the specified IP.
     *
     * @param ip The IP address to delete.
     */
    void deleteByIp(String ip);

    /**
     * Find all IP addresses and order them by ID in ascending order.
     *
     * @return A list of IpAddress entities ordered by ID in ascending order.
     */
    List<IpAddress> findAllByOrderByIdAsc();

}
