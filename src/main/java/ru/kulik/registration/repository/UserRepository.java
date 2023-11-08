package ru.kulik.registration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kulik.registration.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
