package com.skillsdb.jobseekerservice.service;

import com.skillsdb.jobseekerservice.exception.AppliedJobDoesNotExist;
import com.skillsdb.jobseekerservice.exception.JobDoesNotExist;
import com.skillsdb.jobseekerservice.exception.JobSeekerAlreadyExist;
import com.skillsdb.jobseekerservice.exception.JobSeekerDoesNotExist;
import com.skillsdb.jobseekerservice.model.AppliedJob;
import com.skillsdb.jobseekerservice.model.JobSeeker;

import java.util.List;

public interface JobSeekerService {

    JobSeeker createJobSeeker(JobSeeker jobSeeker) throws JobSeekerAlreadyExist;
    JobSeeker updateJobSeeker(long jobSeekerId, JobSeeker jobSeeker) throws JobSeekerDoesNotExist;
    JobSeeker getJobSeekerById(long jobSeekerId) throws JobSeekerDoesNotExist;
    List<JobSeeker> getJobSeekerByName(String name) throws JobSeekerDoesNotExist;

    List<AppliedJob> getAppliedJobsByEmployerName(String appliedJobName);

    AppliedJob getAppliedJob(long jobSeekerId, long appliedJobId) throws JobSeekerDoesNotExist, AppliedJobDoesNotExist;
    AppliedJob applyJob(long jobSeekerId, AppliedJob appliedJob) throws JobSeekerDoesNotExist, JobDoesNotExist;

    List<AppliedJob> getAppliedJobByDescription(String description);

    List<JobSeeker> getJobSeekerBySkill(String skills);

    boolean deleteJobSeeker(long jobSeekerId) throws JobSeekerDoesNotExist;

    boolean deleteAppliedJob(long jobSeekerId, long appliedJobId) throws JobSeekerDoesNotExist, AppliedJobDoesNotExist;

    List<JobSeeker> getAllJobSeekers();

    List<Long> getAllAppliedJobIds();

}
