package com.dotoanime.candidatetracker.controller;

import com.dotoanime.candidatetracker.model.Job;
import com.dotoanime.candidatetracker.payload.*;
import com.dotoanime.candidatetracker.repository.JobRepository;
import com.dotoanime.candidatetracker.repository.UserRepository;
import com.dotoanime.candidatetracker.security.CurrentUser;
import com.dotoanime.candidatetracker.security.UserPrincipal;
import com.dotoanime.candidatetracker.service.JobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobService jobService;

    private static final Logger logger = LoggerFactory.getLogger(JobController.class);

//    @GetMapping
//    public PagedResponse<JobResponse> getJobs(@CurrentUser UserPrincipal currentUser,
//                                               @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
//                                               @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
//        return jobService.getAllJobs(currentUser, page, size);
//    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createJob(@Valid @RequestBody JobRequest jobRequest) {
        Job job = jobService.createJob(jobRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{jobId}")
                .buildAndExpand(job.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "Job Created Successfully"));
    }

    @GetMapping("/{jobId}")
//    @PostAuthorize("returnObject.createdBy == principal.id")
    @PreAuthorize("hasRole('USER')")
    public JobResponse getJobById(@CurrentUser UserPrincipal currentUser,
                                   @PathVariable Long jobId) {
        return jobService.getJobById(jobId, currentUser);
    }

    @PatchMapping("/{jobId}")
    @PreAuthorize("hasRole('USER')")
    public JobResponse updateJob(@PathVariable Long jobId,
                                 @CurrentUser UserPrincipal currentUser,
                                @Valid @RequestBody JobUpdateRequest jobUpdateRequest) {
        return jobService.updateJob(jobId, jobUpdateRequest, currentUser);
    }

    @PostMapping("/{jobId}/stages")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<JobResponse> addStage(@PathVariable Long jobId,
                                                @CurrentUser UserPrincipal currentUser,
                                                @Valid @RequestBody StageRequest stageRequest) {
        JobResponse jobResponse = jobService.addStage(jobId, stageRequest, currentUser);

        return new ResponseEntity<>(jobResponse, HttpStatus.CREATED);
    }

    @PatchMapping("/{jobId}/stages/{stageId}")
    @PreAuthorize("hasRole('USER')")
    public JobResponse updateStage(@PathVariable Long jobId,
                                   @CurrentUser UserPrincipal currentUser,
                                    @PathVariable Long stageId,
                                    @Valid @RequestBody StageUpdateRequest stageUpdateRequest) {
        return jobService.updateStage(jobId, stageId, stageUpdateRequest, currentUser);
    }

    @GetMapping("/connection")
    public ResponseEntity<?> checkConnection() {
        return ResponseEntity.ok().body(new ApiResponse(true, "Connected"));
    }
}
