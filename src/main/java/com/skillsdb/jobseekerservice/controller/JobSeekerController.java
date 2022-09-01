package com.skillsdb.jobseekerservice.controller;

import com.skillsdb.jobseekerservice.exception.AppliedJobDoesNotExist;
import com.skillsdb.jobseekerservice.exception.JobDoesNotExist;
import com.skillsdb.jobseekerservice.exception.JobSeekerAlreadyExist;
import com.skillsdb.jobseekerservice.exception.JobSeekerDoesNotExist;
import com.skillsdb.jobseekerservice.model.AppliedJob;
import com.skillsdb.jobseekerservice.model.JobSeeker;
import com.skillsdb.jobseekerservice.service.JobSeekerService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class JobSeekerController {

    @Autowired
    private JobSeekerService jobSeekerService;


    @GetMapping("/rest/job/getJobIds")
    public ResponseEntity<?> getAppliedJobIds() {
        List<Long> ids = jobSeekerService.getAllAppliedJobIds();
        return new ResponseEntity<>(ids, HttpStatus.OK);
    }

    @PostMapping("/jobSeeker/create")
    public ResponseEntity<?> addJobSeeker(@RequestBody JobSeeker jobSeeker) {
        try {
            return new ResponseEntity<>(this.jobSeekerService.createJobSeeker(jobSeeker), HttpStatus.CREATED);
        } catch (JobSeekerAlreadyExist e) {
            return new ResponseEntity<>("A JobSeeker with the provided id already exist", HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/jobSeeker/findById/{jobSeekerId}")
    public ResponseEntity<?> findJobSeekerById(@PathVariable("jobSeekerId") long jobSeekerId) {
        try {
            return new ResponseEntity<>(this.jobSeekerService.getJobSeekerById(jobSeekerId), HttpStatus.FOUND);
        } catch (JobSeekerDoesNotExist e) {
            return new ResponseEntity<>("JobSeeker with provided id does not exist", HttpStatus.NOT_FOUND);
        }
    }

    // List<JobSeeker> ?
    @GetMapping("/jobSeeker/findByName/{jobSeekerName}")
    public ResponseEntity<?> findJobSeekerByName(@PathVariable("jobSeekerName") String name) {
        try {
            return new ResponseEntity<>(this.jobSeekerService.getJobSeekerByName(name), HttpStatus.FOUND);
        } catch (JobSeekerDoesNotExist e) {
            return new ResponseEntity<>("JobSeeker with provided name does not exist", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/jobSeeker/findByAppliedJobId/{jobSeekerId}/{appliedJobId}")
    public ResponseEntity<?> findAppliedJob(@PathVariable("jobSeekerId") long jobSeekerId, @PathVariable("appliedJobId") long appliedJobId) {
        try {
            return new ResponseEntity<>(this.jobSeekerService.getAppliedJob(jobSeekerId, appliedJobId), HttpStatus.FOUND);
        } catch (JobSeekerDoesNotExist e) {
            return new ResponseEntity<>("JobSeeker with provided id does not exist", HttpStatus.NOT_FOUND);
        } catch (AppliedJobDoesNotExist e) {
            return new ResponseEntity<>("AppliedJob with provided id does not exist", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/jobSeeker/update/{jobSeekerId}")
    public ResponseEntity<?> updateJobSeeker(@RequestBody JobSeeker jobSeeker, @PathVariable("jobSeekerId") long jobSeekerId) {
        try {
            return new ResponseEntity<>(this.jobSeekerService.updateJobSeeker(jobSeekerId, jobSeeker), HttpStatus.OK);
        } catch (JobSeekerDoesNotExist e) {
            return new ResponseEntity<>("JobSeeker with provided id does not exist", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/jobSeeker/applyJob/{jobSeekerId}")
    public ResponseEntity<?> applyJob(@RequestBody AppliedJob appliedJob, @PathVariable("jobSeekerId") long jobSeekerId) {
        try {
            return new ResponseEntity<>(this.jobSeekerService.applyJob(jobSeekerId, appliedJob), HttpStatus.CREATED);
        } catch (JobSeekerDoesNotExist e) {
            return new ResponseEntity<>("JobSeekerId does not exist", HttpStatus.NOT_FOUND);
        } catch (JobDoesNotExist e) {
            return new ResponseEntity<>("Cannot apply for inexistent job", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/jobSeeker/findByEmployerName/{employerName}")
    public ResponseEntity<?> findByJobName(@PathVariable("employerName") String employerName) {
        List<AppliedJob> appliedJobList = this.jobSeekerService.getAppliedJobsByEmployerName(employerName);
        if(appliedJobList != null || appliedJobList.size() > 0) {
            return new ResponseEntity<>(appliedJobList, HttpStatus.OK);
        }
        return new ResponseEntity<>("No Employer with provided name found", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/jobSeeker/deleteJobSeeker/{jobSeekerId}")
    public ResponseEntity<?> deleteJobSeeker(@PathVariable("jobSeekerId") long jobSeekerId) {
        try {
            return new ResponseEntity<>(this.jobSeekerService.deleteJobSeeker(jobSeekerId), HttpStatus.OK);
        } catch (JobSeekerDoesNotExist e) {
            return new ResponseEntity<>("Provided jobSeekerId does not exist", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/jobSeeker/deleteAppliedJob/{jobSeekerId}/{appliedJobId}")
    public ResponseEntity<?> deleteAppliedJob(@PathVariable("jobSeekerId") long jobSeekerId, @PathVariable("appliedJobId") long appliedJobId) {
        try {
            return new ResponseEntity<>(this.jobSeekerService.deleteAppliedJob(jobSeekerId, appliedJobId), HttpStatus.OK);
        } catch (JobSeekerDoesNotExist e) {
            return new ResponseEntity<>("JobSeeker with provided id does not exist", HttpStatus.NOT_FOUND);
        } catch (AppliedJobDoesNotExist e) {
            return new ResponseEntity<>("AppliedJob with provided id does not exist", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/jobSeeker/getAppliedJobByDescription/{description}")
    public ResponseEntity<?> getAppliedJobByDescription(@PathVariable("description") String description) {
        List<AppliedJob> result = this.jobSeekerService.getAppliedJobByDescription(description);
        if(result != null) {
            return new ResponseEntity<>(result, HttpStatus.FOUND);
        }
        return new ResponseEntity<>("Not found AppliedJob with provided description", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/jobSeeker/getJobSeekerBySkills/{skills}")
    public ResponseEntity<?> getJobSeekerBySkills(@PathVariable("skills") String skills) {
        List<JobSeeker> jobSeekerList = this.jobSeekerService.getJobSeekerBySkill(skills);
        if(jobSeekerList != null) {
            return new ResponseEntity<>(jobSeekerList, HttpStatus.FOUND);
        }
        return new ResponseEntity<>("Not found JobSeekers with provided skills", HttpStatus.NOT_FOUND);
    }


    @GetMapping("/getAll")
    public ResponseEntity<?> getAllJobSeekers() {
        List<JobSeeker> jobList = jobSeekerService.getAllJobSeekers();
        if (jobList == null) {
            return new ResponseEntity<>("No JobSeekers found", HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(jobList, HttpStatus.OK);
        }
    }


}
