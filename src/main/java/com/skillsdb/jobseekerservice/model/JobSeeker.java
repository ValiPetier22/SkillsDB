package com.skillsdb.jobseekerservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.annotation.Generated;
import java.util.List;

@Document
public class JobSeeker {
    @Id
    private long jobSeekerId;
    private String name;
    private String skills;
    private List<AppliedJob> appliedJobs;

    public long getJobSeekerId() {
        return jobSeekerId;
    }

    public void setJobSeekerId(long jobSeekerId) {
        this.jobSeekerId = jobSeekerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public List<AppliedJob> getAppliedJobs() {
        return appliedJobs;
    }

    public void setAppliedJobs(List<AppliedJob> appliedJobs) {
        this.appliedJobs = appliedJobs;
    }

    @Override
    public String toString() {
        return "JobSeeker{" +
                "jobSeekerId=" + jobSeekerId +
                ", name='" + name + '\'' +
                ", skills='" + skills + '\'' +
                ", appliedJobs=" + appliedJobs +
                '}';
    }
}
