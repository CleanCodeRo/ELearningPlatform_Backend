package cleancode.eLearningPlatform.auth.repository;


import cleancode.eLearningPlatform.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    List<User> findAllByOrderByRankPointsDesc();

    List<User> findAllByOrderByWeeklyRankPointsDesc();

    boolean existsByEmail(String email);

    @Query("SELECT u FROM User u " +
            "WHERE (:id IS NULL OR u.id = :id) " +
            "AND (:email IS NULL OR :email = '' OR lower(u.email) LIKE lower(concat('%', :email, '%'))) ")
    List<User> findUsersBySearchParams(@Param("email") String email,
                                       @Param("id") Long id);
}
