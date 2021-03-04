package com.dotoanime.candidatetracker.Utils;


import com.dotoanime.candidatetracker.model.Job;
import com.dotoanime.candidatetracker.model.User;

public class TestUtil {

    public static User createValidUser() {
        User user = new User();
        user.setUsername("test-user");
        user.setName("test-display");
        user.setPassword("P4ssword");
        return user;
    }

    public static User createValidUser(String username) {
        User user = createValidUser();
        user.setUsername(username);
        return user;
    }

    public static Job createValidHoax() {
        Job job = new Job();
        job.setCompanyName("Miracle inc");
        job.setPosition("Full Stack Developer");
        return job;
    }
}
