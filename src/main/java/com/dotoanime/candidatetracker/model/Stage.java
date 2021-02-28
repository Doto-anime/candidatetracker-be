package com.dotoanime.candidatetracker.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "stages")
public class Stage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String note;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date doneAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    public Stage() {

    }

    public Stage(String name, String note, Date doneAt) {
        this.name = name;
        this.note = note;
        this.doneAt = doneAt;
    }
}
