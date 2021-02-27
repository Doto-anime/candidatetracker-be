package com.dotoanime.candidatetracker.model;

import com.dotoanime.candidatetracker.model.audit.UserDateAudit;
import lombok.Data;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "jobs")
public class Job extends UserDateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(
            mappedBy = "job",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            orphanRemoval = true
    )
    @Fetch(FetchMode.SELECT)
    private List<Stage> stages = new ArrayList<>();

    @NotBlank(message = "Fill company name")
    private String companyName;

    @NotBlank
    private String position;

    @Column(columnDefinition="TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private JobStatus jobStatus;

    public void addStage(Stage stage) {
        stages.add(stage);
        stage.setJob(this);
    }

    public void removeStage(Stage stage) {
        stages.remove(stage);
        stage.setJob(null);
    }
}
