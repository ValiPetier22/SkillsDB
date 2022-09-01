package com.skillsdb.jobseekerservice.service;
import static org.junit.jupiter.api.Assertions.assertThrows;
import com.skillsdb.jobseekerservice.exception.JobSeekerAlreadyExist;
import com.skillsdb.jobseekerservice.exception.JobSeekerDoesNotExist;
import com.skillsdb.jobseekerservice.model.AppliedJob;
import com.skillsdb.jobseekerservice.model.JobSeeker;
import com.skillsdb.jobseekerservice.repository.JobSeekerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

public class JobSeekerServiceImplementationTest {
    private JobSeeker jobSeekerVlad;
    private JobSeeker jobSeekerVladDuplicate;
    private JobSeeker jobSeekerBob;
    private Optional<JobSeeker> optionalEmptyJobSeeker;

    private AppliedJob vladAppliedJob;

    @Mock
    private JobSeekerRepository jobSeekerRepository;

    @InjectMocks
    private JobSeekerImplementation jobSeekerImplementation;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        jobSeekerVlad = new JobSeeker();
        jobSeekerVlad.setJobSeekerId(1L);
        jobSeekerVlad.setSkills("Java MySQL Mongo");
        jobSeekerVlad.setName("Vlad");

        jobSeekerBob = new JobSeeker();
        jobSeekerBob.setSkills("MySQL");
        jobSeekerBob.setJobSeekerId(2L);
        jobSeekerBob.setName("Bob");

        jobSeekerVladDuplicate = new JobSeeker();
        jobSeekerVladDuplicate.setJobSeekerId(1L);
        jobSeekerVlad.setSkills("Some set of skills");
        jobSeekerVlad.setName("Vlad");

        vladAppliedJob = new AppliedJob();
        vladAppliedJob.setJobSeekerId(1L);
        vladAppliedJob.setJobId(1L);
        vladAppliedJob.setEmployerName("Qualitest");
        vladAppliedJob.setDescription("Java developer");

        jobSeekerVlad.setAppliedJobs(List.of(vladAppliedJob));

        //jobSeekerImplementation = new JobSeekerImplementation();
    }

    @Test
    public void addJobSeekerSuccess() {

        when(jobSeekerRepository.save(jobSeekerVlad)).thenReturn(jobSeekerVlad);
        when(jobSeekerRepository.save(jobSeekerBob)).thenReturn(jobSeekerBob);
    }

    @Test
    public void addJobSeekerFailure() {
        when(jobSeekerRepository.findById(1L)).thenReturn(Optional.of(jobSeekerVlad));
        assertThrows(JobSeekerAlreadyExist.class, () -> { jobSeekerImplementation.createJobSeeker(jobSeekerVlad);});
    }

//    @Test
//    public void updateJobSeekerSuccess() {
//
//    }
//
//    @Test
//    public void updateJobSeekerFailure() {
//
//    }

    @Test
    public void getJobSeekerByIdSuccess() throws JobSeekerDoesNotExist {
        when(jobSeekerRepository.findById(1L)).thenReturn(Optional.of(jobSeekerVlad));
        JobSeeker fetchedJobSeeker = jobSeekerImplementation.getJobSeekerById(1L);
        assertEquals(jobSeekerVlad, fetchedJobSeeker);
    }

    @Test
    public void getJobSeekerByIdFailure() throws JobSeekerDoesNotExist {
        when(jobSeekerRepository.findById(100L)).thenReturn(Optional.empty());
        assertThrows(JobSeekerDoesNotExist.class, () -> { jobSeekerImplementation.getJobSeekerById(100L);});
    }

    @Test
    public void getJobSeekerByNameSuccess() {
        when(jobSeekerRepository.findJobSeekerByName("Vlad")).thenReturn(List.of(jobSeekerVlad));
    }

    @Test
    public void getJobSeekerByNameFailure() throws JobSeekerDoesNotExist {
        when(jobSeekerRepository.findJobSeekerByName("Steve")).thenReturn(null);
        assertThrows(JobSeekerDoesNotExist.class, () -> { jobSeekerImplementation.getJobSeekerByName("Steve");});
    }

    @Test
    // applyJob
    public void applyJobSuccess() {
        jobSeekerVlad.setAppliedJobs(List.of(vladAppliedJob));
        when(jobSeekerRepository.findById(1L)).thenReturn(Optional.of(jobSeekerVlad));
    }

    @Test
    public void applyJobFailureJobSeekerDoesNotExist() {
        when(jobSeekerRepository.findById(100L)).thenReturn(Optional.empty());
        assertThrows(JobSeekerDoesNotExist.class, () -> { jobSeekerImplementation.applyJob(100L, vladAppliedJob);});
    }

    // getAppliedJobByDescription
//    @Test
//    public void getAppliedJobByDescription() throws Exception {
//
//    }
//
//    // getAppliedJobsByEmployerName
//    @Test
//    public void getAppliedJobsByEmployerName() {
//
//    }


}
