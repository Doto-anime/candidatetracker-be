package com.dotoanime.candidatetracker.controller;

import com.dotoanime.candidatetracker.model.*;
import com.dotoanime.candidatetracker.payload.*;
import com.dotoanime.candidatetracker.repository.JobRepository;
import com.dotoanime.candidatetracker.repository.UserRepository;
import com.dotoanime.candidatetracker.security.CurrentUser;
import com.dotoanime.candidatetracker.security.UserPrincipal;
import com.dotoanime.candidatetracker.service.JobService;
import com.dotoanime.candidatetracker.util.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    @PreAuthorize("hasRole('USER')")
    public JobResponse getJobById(@CurrentUser UserPrincipal currentUser,
                                   @PathVariable Long jobId) {
        return jobService.getJobById(jobId, currentUser);
    }

    @PatchMapping("/{jobId}")
    @PreAuthorize("hasRole('USER')")
    public JobResponse updateJob(@CurrentUser UserPrincipal currentUser,
                         @PathVariable Long jobId,
                         @Valid @RequestBody JobRequest jobRequest) {
        return jobService.updateJob(jobId, jobRequest, currentUser);
    }

    @PostMapping("/{jobId}/stages")
    @PreAuthorize("hasRole('USER')")
    public JobResponse addStage(@CurrentUser UserPrincipal currentUser,
                                @PathVariable Long jobId,
                                @Valid @RequestBody StageRequest stageRequest) {
        return jobService.addStage(jobId, stageRequest, currentUser);
    }

    @PatchMapping("/{jobId}/stages/{stageId}")
    @PreAuthorize("hasRole('USER')")
    public JobResponse updateStage(@CurrentUser UserPrincipal currentUser,
                                @PathVariable Long jobId,
                                @PathVariable Long stageId,
                                @Valid @RequestBody StageRequest stageRequest) {
        return jobService.updateStage(jobId, stageId, stageRequest, currentUser);
    }

    @GetMapping("/connection")
    public ResponseEntity<?> checkConnection() {
        return ResponseEntity.ok().body(new ApiResponse(true, "Connected"));
    }
}
