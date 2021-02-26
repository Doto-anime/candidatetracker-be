package com.dotoanime.candidatetracker.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.Date;
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

    private String note;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date doneAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

//    @CreatedDate
//    private Instant createdAt = Instant.now();

//    @Enumerated(EnumType.STRING)
//    private StageStatus stageStatus;

    public Stage() {

    }

    public Stage(String name, String note, Date doneAt) {
        this.name = name;
        this.note = note;
        this.doneAt = doneAt;
    }
}
