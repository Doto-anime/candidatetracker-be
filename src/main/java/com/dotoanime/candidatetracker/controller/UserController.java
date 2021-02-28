package com.dotoanime.candidatetracker.controller;

import com.dotoanime.candidatetracker.payload.JobResponse;
import com.dotoanime.candidatetracker.payload.PagedResponse;
import com.dotoanime.candidatetracker.payload.UserIdentityAvailability;
import com.dotoanime.candidatetracker.payload.UserProfile;
import com.dotoanime.candidatetracker.repository.JobRepository;
import com.dotoanime.candidatetracker.repository.UserRepository;
import com.dotoanime.candidatetracker.security.CurrentUser;
import com.dotoanime.candidatetracker.security.UserPrincipal;
import com.dotoanime.candidatetracker.service.JobService;
import com.dotoanime.candidatetracker.util.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private JobService jobService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/checkUsername")
    public UserIdentityAvailability checkUsernameAvailability(@RequestParam(value = "username") String username) {
        Boolean isAvailable = !userRepository.existsByUsername(username);
        return new UserIdentityAvailability(isAvailable);
    }

    @GetMapping("/profile")
    @PreAuthorize("hasRole('USER')")
    public UserProfile getUserProfile(@CurrentUser UserPrincipal currentUser) {
        long jobCount = jobRepository.countByCreatedBy(currentUser.getId());

        return new UserProfile(currentUser.getId(), currentUser.getUsername(), currentUser.getName(), currentUser.getCreatedAt(), jobCount);
    }

    @GetMapping("/jobs")
    @PreAuthorize("hasRole('USER')")
    public PagedResponse<JobResponse> getMyJobs(@CurrentUser UserPrincipal currentUser,
                                                @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        return jobService.getMyJobs(currentUser, page, size);
    }

//    @GetMapping("/{username}/jobs")
//    public PagedResponse<JobResponse> getJobsCreatedBy(@PathVariable(value = "username") String username,
//                                                        @CurrentUser UserPrincipal currentUser,
//                                                        @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
//                                                        @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
//        return jobService.getJobsCreatedBy(username, currentUser, page, size);
//    }
}
