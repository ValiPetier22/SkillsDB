package com.skillsdb.jobseekerservice.repository;

import com.skillsdb.jobseekerservice.model.AppliedJob;
import com.skillsdb.jobseekerservice.model.JobSeeker;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobSeekerRepository extends MongoRepository<JobSeeker, Long> {
    List<JobSeeker> findJobSeekerByName(String name);
    List<JobSeeker> findBySkillsLike(String skills);
}
