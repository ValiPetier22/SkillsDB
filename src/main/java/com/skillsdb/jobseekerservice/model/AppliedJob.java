package com.skillsdb.jobseekerservice.model;

import java.time.LocalDateTime;

public class AppliedJob {
    private long jobId;

    private long employerId;
    private String employerName;
    private String description;
    private String location;
    private LocalDateTime dateOfApplication;

    public AppliedJob() {
        this.dateOfApplication = LocalDateTime.now();
    }

    public long getJobId() {
        return jobId;
    }

    public void setJobId(long jobId) {
        this.jobId = jobId;
    }

    public long getJobSeekerId() {
        return employerId;
    }

    public void setJobSeekerId(long jobSeekerId) {
        this.employerId = jobSeekerId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDateTime getDateOfApplication() {
        return dateOfApplication;
    }

    public void setDateOfApplication() {
        this.dateOfApplication = LocalDateTime.now();
    }

    public String getEmployerName() {
        return employerName;
    }

    public void setEmployerName(String employerName) {
        this.employerName = employerName;
    }
}
