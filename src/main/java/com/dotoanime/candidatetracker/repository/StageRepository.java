package com.dotoanime.candidatetracker.repository;

import com.dotoanime.candidatetracker.model.Job;
import com.dotoanime.candidatetracker.model.Stage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StageRepository extends JpaRepository<Stage, Long> {
    Optional<Stage> findById(Long stageId);
}
