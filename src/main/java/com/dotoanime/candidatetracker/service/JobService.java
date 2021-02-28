package com.dotoanime.candidatetracker.service;

import com.dotoanime.candidatetracker.error.BadRequestException;
import com.dotoanime.candidatetracker.error.ResourceNotFoundException;
import com.dotoanime.candidatetracker.model.Job;
import com.dotoanime.candidatetracker.model.JobStatus;
import com.dotoanime.candidatetracker.model.Stage;
import com.dotoanime.candidatetracker.model.User;
import com.dotoanime.candidatetracker.payload.JobRequest;
import com.dotoanime.candidatetracker.payload.JobResponse;
import com.dotoanime.candidatetracker.payload.PagedResponse;
import com.dotoanime.candidatetracker.payload.StageRequest;
import com.dotoanime.candidatetracker.repository.JobRepository;
import com.dotoanime.candidatetracker.repository.StageRepository;
import com.dotoanime.candidatetracker.repository.UserRepository;
import com.dotoanime.candidatetracker.security.UserPrincipal;
import com.dotoanime.candidatetracker.util.AppConstants;
import com.dotoanime.candidatetracker.util.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StageRepository stageRepository;

    private static final Logger logger = LoggerFactory.getLogger(JobService.class);

//    public PagedResponse<JobResponse> getAllJobs(UserPrincipal currentUser, int page, int size) {
//        validatePageNumberAndSize(page, size);
//
//        // Retrieve Jobs
//        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
//        Page<Job> jobs = jobRepository.findAll(pageable);
//
//        if(jobs.getNumberOfElements() == 0) {
//            return new PagedResponse<>(Collections.emptyList(), jobs.getNumber(),
//                    jobs.getSize(), jobs.getTotalElements(), jobs.getTotalPages(), jobs.isLast());
//        }
//
//        // Map Jobs to JobResponses containing vote counts and job creator details
//        List<Long> jobIds = jobs.map(Job::getId).getContent();
//        Map<Long, User> creatorMap = getJobCreatorMap(jobs.getContent());
//
//        List<JobResponse> jobRespons = jobs.map(job -> {
//            return ModelMapper.mapJobToJobResponse(job,
//                    creatorMap.get(job.getCreatedBy())
//            );
//        }).getContent();
//
//        return new PagedResponse<>(jobRespons, jobs.getNumber(),
//                jobs.getSize(), jobs.getTotalElements(), jobs.getTotalPages(), jobs.isLast());
//    }

    public PagedResponse<JobResponse> getMyJobs(UserPrincipal currentUser, int page, int size) {
        validatePageNumberAndSize(page, size);

        User user = userRepository.findByUsername(currentUser.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", currentUser.getUsername()));

        // Retrieve all jobs created by the given username
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<Job> jobs = jobRepository.findByCreatedBy(currentUser.getId(), pageable);

        if (jobs.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), jobs.getNumber(),
                    jobs.getSize(), jobs.getTotalElements(), jobs.getTotalPages(), jobs.isLast());
        }

        // Map Jobs to JobResponses containing vote counts and job creator details
        List<Long> jobIds = jobs.map(Job::getId).getContent();

        List<JobResponse> jobRespons = jobs.map(job -> {
            return ModelMapper.mapJobToJobResponse(job,
                    user
            );
        }).getContent();

        return new PagedResponse<>(jobRespons, jobs.getNumber(),
                jobs.getSize(), jobs.getTotalElements(), jobs.getTotalPages(), jobs.isLast());
    }

//    public PagedResponse<JobResponse> getJobsCreatedBy(String username, UserPrincipal currentUser, int page, int size) {
//        validatePageNumberAndSize(page, size);
//
//        User user = userRepository.findByUsername(username)
//                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
//
//        // Retrieve all jobs created by the given username
//        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
//        Page<Job> jobs = jobRepository.findByCreatedBy(user.getId(), pageable);
//
//        if (jobs.getNumberOfElements() == 0) {
//            return new PagedResponse<>(Collections.emptyList(), jobs.getNumber(),
//                    jobs.getSize(), jobs.getTotalElements(), jobs.getTotalPages(), jobs.isLast());
//        }
//
//        // Map Jobs to JobResponses containing vote counts and job creator details
//        List<Long> jobIds = jobs.map(Job::getId).getContent();
//
//        List<JobResponse> jobRespons = jobs.map(job -> {
//            return ModelMapper.mapJobToJobResponse(job,
//                    user
//            );
//        }).getContent();
//
//        return new PagedResponse<>(jobRespons, jobs.getNumber(),
//                jobs.getSize(), jobs.getTotalElements(), jobs.getTotalPages(), jobs.isLast());
//    }

    public Job createJob(JobRequest jobRequest) {
        Job job = new Job();
        job.setCompanyName(jobRequest.getCompanyName());
        job.setPosition(jobRequest.getPosition());
        job.setDescription(jobRequest.getDescription());
        job.setJobStatus(JobStatus.ONGOING);

        if (!(jobRequest.getStages() == null)){
            jobRequest.getStages().forEach(stageRequest -> {
                job.addStage(new Stage(stageRequest.getName(), stageRequest.getNote(), stageRequest.getDoneAt()));
            });
        }

        return jobRepository.save(job);
    }

    public JobResponse updateJob(Long jobId, JobRequest jobRequest){
        Job job = jobRepository.findById(jobId).orElseThrow(
                () -> new ResourceNotFoundException("Job", "id", jobId));

        if(jobRequest.getCompanyName() != null) job.setCompanyName(jobRequest.getCompanyName());
        if(jobRequest.getPosition() != null) job.setPosition(jobRequest.getPosition());
        if(jobRequest.getDescription() != null) job.setDescription(jobRequest.getDescription());
        if(jobRequest.getJobStatus() != null) job.setJobStatus(JobStatus.fromString(jobRequest.getJobStatus()));

        jobRepository.save(job);

        User creator = userRepository.findById(job.getCreatedBy())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", job.getCreatedBy()));

        return ModelMapper.mapJobToJobResponse(job, creator);
    }

    public JobResponse getJobById(Long jobId, UserPrincipal currentUser) {
        Job job = jobRepository.findById(jobId).orElseThrow(
                () -> new ResourceNotFoundException("Job", "id", jobId));

        // Retrieve job creator details
        User creator = userRepository.findById(job.getCreatedBy())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", job.getCreatedBy()));

        return ModelMapper.mapJobToJobResponse(job, creator);
    }

    public JobResponse addStage(Long jobId, StageRequest stageRequest) {
        Job job = jobRepository.findById(jobId).orElseThrow(
                () -> new ResourceNotFoundException("Job", "id", jobId));

        job.addStage(new Stage(stageRequest.getName(), stageRequest.getNote(), stageRequest.getDoneAt()));
        Job jobReturn = jobRepository.save(job);

        // Retrieve job creator details
        User creator = userRepository.findById(job.getCreatedBy())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", job.getCreatedBy()));

        return ModelMapper.mapJobToJobResponse(jobReturn, creator);
    }

    public JobResponse updateStage(Long jobId, Long stageId, StageRequest stageRequest){
        Job job = jobRepository.findById(jobId).orElseThrow(
                () -> new ResourceNotFoundException("Job", "id", jobId));

        Optional<Stage> op = job.getStages().stream().filter(stage -> stageId.equals(stage.getId())).findFirst();
        if (op.isPresent()){
            Stage stage = op.get();
            if (stageRequest.getName() != null) stage.setName(stageRequest.getName());
            if (stageRequest.getNote() != null) stage.setNote(stageRequest.getNote());
            if (stageRequest.getDoneAt() != null) stage.setDoneAt(stageRequest.getDoneAt());

            stageRepository.save(stage);
        } else {
            throw new BadRequestException("Stage with id " + stageId + " is not found");
        }

        User creator = userRepository.findById(job.getCreatedBy())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", job.getCreatedBy()));

        return ModelMapper.mapJobToJobResponse(job, creator);
    }

    private void validatePageNumberAndSize(int page, int size) {
        if(page < 0) {
            throw new BadRequestException("Page number cannot be less than zero.");
        }

        if(size > AppConstants.MAX_PAGE_SIZE) {
            throw new BadRequestException("Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
        }
    }

    Map<Long, User> getJobCreatorMap(List<Job> jobs) {
        // Get Job Creator details of the given list of polls
        List<Long> creatorIds = jobs.stream()
                .map(Job::getCreatedBy)
                .distinct()
                .collect(Collectors.toList());

        List<User> creators = userRepository.findByIdIn(creatorIds);
        Map<Long, User> creatorMap = creators.stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));

        return creatorMap;
    }
}
