package com.skillsdb.jobseekerservice.aspect;

import com.skillsdb.jobseekerservice.model.JobSeeker;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggerAspect {

    Log myLog = LogFactory.getLog(AspectConfig.class);

    @Pointcut("execution (* com.skillsdb.jobseekerservice.controller.JobSeekerController.create(..))")
    public void createPointcut() {}

    @Before("createPointcut()")
    public void beforeCreatingJobSeeker(JoinPoint jp) {

        this.myLog.info("Going to create jobSeeker" + jp.toString());
    }

    @After("createPointcut()")
    public void afterCreatingJobSeeker(JoinPoint jp) {

        this.myLog.info("JobSeeker was created " + jp.toString());
    }

    @Around("createPointcut()")
    public Object whileCreatingJobSeeker(ProceedingJoinPoint proceed) throws Throwable {
        Object object = proceed.proceed();

        try {
            ResponseEntity responseEntity = (ResponseEntity) object;
            JobSeeker jobSeeker = (JobSeeker) responseEntity.getBody();
            myLog.info("Creating jobSeeker with id " + jobSeeker.getJobSeekerId());
        } catch(Exception e) {

        }
        return object;
    }

    @AfterThrowing(pointcut = "createPointcut()", throwing = "exceptObj")
    public void handleCreateJobSeeker(Exception exceptObj) {
        this.myLog.warn("Exception raised " + exceptObj.getMessage());
    }


    @Pointcut("execution (* com.skillsdb.jobseekerservice.controller.JobSeekerController.findById(..))")
    public void findJobSeekerByIdPointcut() {}

    @Before("findJobSeekerByIdPointcut()")
    public void beforeGettingJobSeeker(JoinPoint jp) {

        this.myLog.info("Going to view jobSeeker" + jp.toString());
    }

    @After("findJobSeekerByIdPointcut()")
    public void afterFindingJobSeeker(JoinPoint jp) {

        this.myLog.info("JobSeeker was found " + jp.toString());
    }

    @Around("findJobSeekerByIdPointcut()")
    public Object whileGettingJobSeeker(ProceedingJoinPoint proceed) throws Throwable {
        Object object = proceed.proceed();

        try {
            ResponseEntity responseEntity = (ResponseEntity) object;
            JobSeeker jobSeeker = (JobSeeker) responseEntity.getBody();
            myLog.info("Going to view jobSeeker with id " + jobSeeker.getJobSeekerId());
        } catch(Exception e) {

        }
        return object;
    }

    @AfterThrowing(pointcut = "findJobSeekerByIdPointcut()", throwing = "exceptObj")
    public void handleViewSeekerById(Exception exceptObj) {
        this.myLog.warn("Exception raised " + exceptObj.getMessage());
    }

}
