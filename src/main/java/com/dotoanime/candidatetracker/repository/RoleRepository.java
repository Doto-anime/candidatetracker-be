package com.dotoanime.candidatetracker.repository;

import com.dotoanime.candidatetracker.model.Role;
import com.dotoanime.candidatetracker.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName roleName);
}
