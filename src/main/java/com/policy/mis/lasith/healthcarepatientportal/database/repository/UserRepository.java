package com.policy.mis.lasith.healthcarepatientportal.database.repository;

import com.policy.mis.lasith.healthcarepatientportal.database.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByNic(String nic);
}
