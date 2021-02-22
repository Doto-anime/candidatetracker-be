package com.dotoanime.candidatetracker.repository;

import com.dotoanime.candidatetracker.model.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    Optional<Job> findById(Long jobId);

    Page<Job> findByCreatedBy(Long userId, Pageable pageable);

    long countByCreatedBy(Long userId);

    List<Job> findByIdIn(List<Long> jobIds);

    List<Job> findByIdIn(List<Long> jobIds, Sort sort);
}
