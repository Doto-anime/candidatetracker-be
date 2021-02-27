package com.dotoanime.candidatetracker.util;

import com.dotoanime.candidatetracker.model.Job;
import com.dotoanime.candidatetracker.model.User;
import com.dotoanime.candidatetracker.payload.StageResponse;
import com.dotoanime.candidatetracker.payload.JobResponse;
import com.dotoanime.candidatetracker.payload.UserSummary;
import com.dotoanime.candidatetracker.security.UserPrincipal;

import java.util.List;
import java.util.stream.Collectors;

public class ModelMapper {

    public static JobResponse mapJobToJobResponse(Job job, User creator) {
        JobResponse jobResponse = new JobResponse();
        jobResponse.setId(job.getId());
        jobResponse.setCompanyName(job.getCompanyName());
        jobResponse.setPosition(job.getPosition());
        jobResponse.setDescription(job.getDescription());
        jobResponse.setJobStatus(job.getJobStatus());
        jobResponse.setCreatedAt(job.getCreatedAt());
        jobResponse.setUpdatedAt(job.getUpdatedAt());

        List<StageResponse> stageRespons = job.getStages().stream().map(stage -> {
            StageResponse stageResponse = new StageResponse();
            stageResponse.setId(stage.getId());
            stageResponse.setName(stage.getName());
            stageResponse.setNote(stage.getNote());
            stageResponse.setDoneAt(stage.getDoneAt());

            return stageResponse;
        }).collect(Collectors.toList());

        jobResponse.setStages(stageRespons);
        UserSummary creatorSummary = new UserSummary(creator.getId(), creator.getUsername(), creator.getName());
        jobResponse.setCreatedBy(creatorSummary);

        return jobResponse;
    }

}
