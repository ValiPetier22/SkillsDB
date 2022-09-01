package com.skillsdb.jobseekerservice.service;

import com.skillsdb.jobseekerservice.exception.AppliedJobDoesNotExist;
import com.skillsdb.jobseekerservice.exception.JobDoesNotExist;
import com.skillsdb.jobseekerservice.exception.JobSeekerAlreadyExist;
import com.skillsdb.jobseekerservice.exception.JobSeekerDoesNotExist;
import com.skillsdb.jobseekerservice.model.AppliedJob;
import com.skillsdb.jobseekerservice.model.JobSeeker;
import com.skillsdb.jobseekerservice.repository.JobSeekerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobSeekerImplementation implements JobSeekerService{

    @Autowired
    private JobSeekerRepository jobSeekerRepository;

    private static String urlJob = "http://localhost:8086/job/getAllJobIds";

    @Autowired
    private RestTemplate restTemplate;

    public List<Long> getJobIds() {
        Long[] jobIds = restTemplate.getForObject(urlJob, Long[].class);
        return Arrays.asList(jobIds);
    }

    @Override
    public JobSeeker createJobSeeker(JobSeeker jobSeeker) throws JobSeekerAlreadyExist {
        Optional<JobSeeker> jobSeekerFetch = this.jobSeekerRepository.findById(jobSeeker.getJobSeekerId());
        if(jobSeekerFetch.isPresent()) {
            throw new JobSeekerAlreadyExist();
        }
        return this.jobSeekerRepository.save(jobSeeker);
    }

    @Override
    public JobSeeker updateJobSeeker(long jobSeekerId, JobSeeker jobSeeker) throws JobSeekerDoesNotExist {
        Optional<JobSeeker> jobSeekerFetch = this.jobSeekerRepository.findById(jobSeekerId);
        if(jobSeekerFetch.isEmpty()) {
            throw new JobSeekerDoesNotExist();
        }
        // Made in order to not null out the applied jobs, this should not be allowed to be updated?
        List<AppliedJob> currentlyAppliedJobs = jobSeekerFetch.get().getAppliedJobs();
        jobSeeker.setAppliedJobs(currentlyAppliedJobs);
        return this.jobSeekerRepository.save(jobSeeker);
    }

    @Override
    public JobSeeker getJobSeekerById(long jobSeekerId) throws JobSeekerDoesNotExist {
//        JobSeeker fetchedJobSeeker = this.jobSeekerRepository.findJobSeekerByJobSeekerId(jobSeekerId);
//        if(fetchedJobSeeker == null) {
//            throw new JobSeekerDoesNotExist();
//        }
//        return fetchedJobSeeker;
        Optional<JobSeeker> optionalJobSeeker = this.jobSeekerRepository.findById(jobSeekerId);
        if(optionalJobSeeker.isEmpty()) {
            throw new JobSeekerDoesNotExist();
        }
        return optionalJobSeeker.get();
    }

    @Override
    public List<JobSeeker> getJobSeekerByName(String name) throws JobSeekerDoesNotExist {
        List<JobSeeker> fetchedJobSeeker = this.jobSeekerRepository.findJobSeekerByName(name);
        if(fetchedJobSeeker == null) {
            throw new JobSeekerDoesNotExist();
        }
        return fetchedJobSeeker;
    }

    @Override
    public List<AppliedJob> getAppliedJobsByEmployerName(String appliedJobName) {
        List<JobSeeker> jobSeekerList = this.jobSeekerRepository.findAll();
        List<AppliedJob> result = new ArrayList<>();
        for(JobSeeker jobSeeker : jobSeekerList) {
            List<AppliedJob> aux = jobSeeker.getAppliedJobs();
            if(aux != null) {
                List<AppliedJob> streamedList = aux.stream().filter(appliedJob -> appliedJob.getEmployerName().equalsIgnoreCase(appliedJobName))
                        .collect(Collectors.toList());
                System.out.println("streamedlist of " + jobSeeker.getName() + " " + streamedList);
                if(streamedList != null) {
                    result.addAll(streamedList);
                }
            }
        }
        return result;
    }

    @Override
    public AppliedJob getAppliedJob(long jobSeekerId, long appliedJobId) throws JobSeekerDoesNotExist, AppliedJobDoesNotExist {
        Optional<JobSeeker> optionalJobSeeker = this.jobSeekerRepository.findById(jobSeekerId);
        if(optionalJobSeeker.isEmpty()) {
            throw new JobSeekerDoesNotExist();
        }
        JobSeeker jobSeekerObj = optionalJobSeeker.get();
        List<AppliedJob> appliedJobList = jobSeekerObj.getAppliedJobs();
        Optional<AppliedJob> optionalAppliedJob = appliedJobList.stream().filter(appliedJob -> appliedJob.getJobId() == appliedJobId).findFirst();
        if(optionalAppliedJob.isEmpty()) {
            throw new AppliedJobDoesNotExist();
        }
        return optionalAppliedJob.get();
    }

    @Override
    public AppliedJob applyJob(long jobSeekerId, AppliedJob appliedJob) throws JobSeekerDoesNotExist, JobDoesNotExist {
        Optional<JobSeeker> optionalJobSeeker = this.jobSeekerRepository.findById(jobSeekerId);
        if(optionalJobSeeker.isEmpty()) {
            throw new JobSeekerDoesNotExist();
        }
        JobSeeker jobSeekerObj = optionalJobSeeker.get();
        List<AppliedJob> jobList = jobSeekerObj.getAppliedJobs();
        if(jobList == null) {
            jobList = new ArrayList<>();
        }

        // Should check if the JobId exists
        List<Long> jobIds = getJobIds();
        if (!jobIds.contains(appliedJob.getJobId()) || getAllAppliedJobIds().contains(appliedJob.getJobId())) {
            throw new JobDoesNotExist();
        }


        jobList.add(appliedJob);
        jobSeekerObj.setAppliedJobs(jobList);
        this.jobSeekerRepository.save(jobSeekerObj);
        return appliedJob;
    }

    @Override
    public List<AppliedJob> getAppliedJobByDescription(String description) {
        List<JobSeeker> jobSeekerList = this.jobSeekerRepository.findAll();
        List<AppliedJob> result = new ArrayList<>();
        if(jobSeekerList != null) {
            for(JobSeeker jobSeeker : jobSeekerList) {
                List<AppliedJob> aux = jobSeeker.getAppliedJobs();
                if(aux != null) {
                    List<AppliedJob> streamedList = aux.stream().filter(appliedJob -> appliedJob.getDescription().contains(description))
                            .collect(Collectors.toList());
                    if(streamedList != null) {
                        result.addAll(streamedList);
                    }
                }
            }
        }
        return result;
    }

    @Override
    public List<JobSeeker> getJobSeekerBySkill(String skills) {
        List<JobSeeker> jobSeekerList = this.jobSeekerRepository.findBySkillsLike(skills);
        if(jobSeekerList != null) {
            return jobSeekerList;
        }
        return new ArrayList<>();
    }

    @Override
    public boolean deleteJobSeeker(long jobSeekerId) throws JobSeekerDoesNotExist {
        Optional<JobSeeker> optionalJobSeeker = this.jobSeekerRepository.findById(jobSeekerId);
        if(optionalJobSeeker.isEmpty()) {
            throw new JobSeekerDoesNotExist();
        }
        this.jobSeekerRepository.deleteById(jobSeekerId);
        return true;
    }

    @Override
    public boolean deleteAppliedJob(long jobSeekerId, long appliedJobId) throws JobSeekerDoesNotExist, AppliedJobDoesNotExist {
        Optional<JobSeeker> optionalJobSeeker = this.jobSeekerRepository.findById(jobSeekerId);
        if(optionalJobSeeker.isEmpty()) {
            throw new JobSeekerDoesNotExist();
        }
        JobSeeker jobSeekerObj = optionalJobSeeker.get();
        List<AppliedJob> appliedJobList = jobSeekerObj.getAppliedJobs();
        if(appliedJobList.removeIf(appliedJob -> appliedJob.getJobId() == appliedJobId)) {
            jobSeekerObj.setAppliedJobs(appliedJobList);
            return true;
        }
        throw new AppliedJobDoesNotExist();
    }

    @Override
    public List<JobSeeker> getAllJobSeekers() {
        return jobSeekerRepository.findAll();
    }

    @Override
    public List<Long> getAllAppliedJobIds() {
        List<JobSeeker> allJobSeekers = getAllJobSeekers();
        List<Long> appliedJobsIds = new ArrayList<>();
        for (JobSeeker jobSeeker : allJobSeekers) {
            List<AppliedJob> appliedJobs = jobSeeker.getAppliedJobs();
            for (AppliedJob appliedJob : appliedJobs) {
                appliedJobsIds.add(appliedJob.getJobId());
            }
        }
        return appliedJobsIds;
    }


}
