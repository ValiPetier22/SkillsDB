package com.skillsdb.jobseekerservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.skillsdb.jobseekerservice.exception.AppliedJobDoesNotExist;
import com.skillsdb.jobseekerservice.exception.JobSeekerAlreadyExist;
import com.skillsdb.jobseekerservice.exception.JobSeekerDoesNotExist;
import com.skillsdb.jobseekerservice.model.AppliedJob;
import com.skillsdb.jobseekerservice.model.JobSeeker;
import com.skillsdb.jobseekerservice.service.JobSeekerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class JobSeekerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private JobSeekerController jobSeekerController;
    @Mock
    private JobSeekerService jobSeekerService;

    @MockBean
    private JobSeeker jobSeekerVlad;
    @MockBean
    private AppliedJob appliedJob;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(jobSeekerController).build();
        //mockMvc = MockMvcBuilders.standaloneSetup(jobSeekerController).setCustomArgumentResolvers(new DefaultValueHandlerMethodArgumentResolver()).build();

        jobSeekerVlad = new JobSeeker();
        jobSeekerVlad.setJobSeekerId(1L);
        jobSeekerVlad.setSkills("Java MySQL Mongo");
        jobSeekerVlad.setName("Vlad");

        appliedJob = new AppliedJob();
        appliedJob.setJobId(1L);
        appliedJob.setJobSeekerId(1L);
        appliedJob.setDescription("Java Developer");
        appliedJob.setEmployerName("Qualitest");
        appliedJob.setDateOfApplication();
    }

    @Test
    public void addJobSeekerSuccess() throws Exception {
        when(jobSeekerService.createJobSeeker(any())).thenReturn(jobSeekerVlad);
        mockMvc.perform(MockMvcRequestBuilders.post("/jobSeeker/create").contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(jobSeekerVlad)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void addJobSeekerFailure() throws Exception {
        when(jobSeekerService.createJobSeeker(any())).thenThrow(JobSeekerAlreadyExist.class);
        mockMvc.perform(MockMvcRequestBuilders.post("/jobSeeker/create").contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(jobSeekerVlad)))
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void getJobSeekerSuccess() throws Exception {
        when(jobSeekerService.getJobSeekerById(jobSeekerVlad.getJobSeekerId())).thenReturn(jobSeekerVlad);
        mockMvc.perform(MockMvcRequestBuilders.get("/jobSeeker/findById/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isFound());
    }

    @Test
    public void getJobSeekerFailure() throws Exception {
        when(jobSeekerService.getJobSeekerById(100L)).thenThrow(JobSeekerDoesNotExist.class);
        mockMvc.perform(MockMvcRequestBuilders.get("/jobSeeker/findById/100")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void getJobSeekerByNameSuccess() throws Exception {
        when(jobSeekerService.getJobSeekerByName(jobSeekerVlad.getName())).thenReturn(List.of(jobSeekerVlad));
        mockMvc.perform(MockMvcRequestBuilders.get("/jobSeeker/findByName/Vlad")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isFound());
    }

    @Test
    public void getJobSeekerByNameFailure() throws Exception {
        when(jobSeekerService.getJobSeekerByName("Steven")).thenThrow(JobSeekerDoesNotExist.class);
        mockMvc.perform(MockMvcRequestBuilders.get("/jobSeeker/findByName/Steven")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    //("/jobSeeker/findByAppliedJobId/{jobSeekerId}/{appliedJobId}")
    @Test
    public void getAppliedJobByIdSuccess() throws Exception {
        when(jobSeekerService.getAppliedJob(1L, 1L)).thenReturn(appliedJob);
        mockMvc.perform(MockMvcRequestBuilders.get("/jobSeeker/findByAppliedJobId/1/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isFound());
    }

    @Test
    public void getAppliedJobByIdJobSeekerIdFailure() throws Exception {
        when(jobSeekerService.getAppliedJob(100L, 1L)).thenThrow(JobSeekerDoesNotExist.class);
        mockMvc.perform(MockMvcRequestBuilders.get("/jobSeeker/findByAppliedJobId/100/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void getAppliedJobByAppliedJobIdFailure() throws Exception {
        when(jobSeekerService.getAppliedJob(1L, 100L)).thenThrow(AppliedJobDoesNotExist.class);
        mockMvc.perform(MockMvcRequestBuilders.get("/jobSeeker/findByAppliedJobId/1/100")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void updateJobSeekerSuccess() throws Exception {
        when(jobSeekerService.updateJobSeeker(1L, jobSeekerVlad)).thenReturn(jobSeekerVlad);
        //when(jobSeekerService.updateJobSeeker(1L, jobSeekerVlad)).thenReturn(jobSeekerVlad);
        mockMvc.perform(MockMvcRequestBuilders.put("/jobSeeker/update/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(appliedJob)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void updateJobSeekerFailure() throws Exception {
        when(jobSeekerService.updateJobSeeker(100L, jobSeekerVlad)).thenThrow(JobSeekerDoesNotExist.class);
        //when(jobSeekerService.updateJobSeeker(100L, jobSeekerVlad)).thenThrow(JobSeekerDoesNotExist.class);
        mockMvc.perform(MockMvcRequestBuilders.put("/jobSeeker/update/100")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(appliedJob)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    //"/jobSeeker/applyJob/{jobSeekerId}"
    @Test
    public void applyJobSuccess() throws Exception {
        when(jobSeekerService.applyJob(1L, appliedJob)).thenReturn(appliedJob);
        mockMvc.perform(MockMvcRequestBuilders.post("/jobSeeker/applyJob/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(appliedJob)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcResultHandlers.print());
    }


    private static String asJsonString(final Object obj) {
        try {
            ObjectMapper objMapper = new ObjectMapper();
            objMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            objMapper.registerModule(new JavaTimeModule());
            return objMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
