package tzamsk.Soteria.Repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tzamsk.Soteria.Entities.User;

public interface UserRepository {

    @Query("SELECT u FROM User u WHERE u.email = :email")
    public User getUserByEmail(@Param("email") String email);

    @Query("SELECT u FROM User u WHERE u.email = :email AND u.password = :password")
    public User getUserByEmailPassword(@Param("email") String email, @Param("password") String password);

    @Query("UPDATE User u SET u.active = ?2 WHERE u.id = ?1")
    @Modifying
    public void updateActiveStatus(Integer id, boolean active);

    @Query("UPDATE User u SET u.banned = ?2 WHERE u.id = ?1")
    @Modifying
    public void updateBannedStatus(Integer id, boolean banned);
}
