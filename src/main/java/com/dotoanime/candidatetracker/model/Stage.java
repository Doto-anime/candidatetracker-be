package com.dotoanime.candidatetracker.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.Objects;

@Data
@Entity
@Table(name = "stages")
public class Stage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Please provide stage name")
    private String name;

    @NotBlank(message = "Please provide stage note")
    private String note;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @CreatedDate
    private Instant createdAt = Instant.now();

//    @Enumerated(EnumType.STRING)
//    private StageStatus stageStatus;

    public Stage() {

    }

    public Stage(String name, String note) {
        this.name = name;
        this.note = note;
    }
}
