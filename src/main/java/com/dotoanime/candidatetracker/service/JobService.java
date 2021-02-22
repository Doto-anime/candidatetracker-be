package com.dotoanime.candidatetracker.service;

import com.dotoanime.candidatetracker.exception.BadRequestException;
import com.dotoanime.candidatetracker.exception.ResourceNotFoundException;
import com.dotoanime.candidatetracker.exception.UnauthorizedException;
import com.dotoanime.candidatetracker.model.*;
import com.dotoanime.candidatetracker.payload.PagedResponse;
import com.dotoanime.candidatetracker.payload.JobRequest;
import com.dotoanime.candidatetracker.payload.JobResponse;
import com.dotoanime.candidatetracker.repository.JobRepository;
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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private UserRepository userRepository;

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

        jobRequest.getStages().forEach(stageRequest -> {
            job.addStage(new Stage(stageRequest.getName(), stageRequest.getNote()));
        });

        return jobRepository.save(job);
    }

    public JobResponse getJobById(Long jobId, UserPrincipal currentUser) {
        Job job = jobRepository.findById(jobId).orElseThrow(
                () -> new ResourceNotFoundException("Job", "id", jobId));

        if (!job.getCreatedBy().equals(currentUser.getId())){
            throw new UnauthorizedException("You don't have access to this page");
        }

        // Retrieve job creator details
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
