package com.example.Business.controllers;


import com.example.Business.TestDataUtil;
import com.example.Business.domain.dto.DepartmentDto;
import com.example.Business.domain.entities.Department;
import com.example.Business.mappers.impl.DepartmentMapper;
import com.example.Business.services.DepartmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class DepartmentControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    public DepartmentControllerIntegrationTests(MockMvc mockMvc, DepartmentMapper departmentMapper, DepartmentService departmentService) {
        this.mockMvc = mockMvc;
        this.objectMapper = new ObjectMapper();
        this.departmentMapper = departmentMapper;
        this.departmentService = departmentService;
    }

    @Test
    public void testThatCreateDepartmentSuccessfullyReturnsHttp201Created() throws Exception {

        Department department = TestDataUtil.createDepartmentIT();
        DepartmentDto departmentDto = departmentMapper.mapTo(department);
        String departmentJson = objectMapper.writeValueAsString(departmentDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/department")
                .contentType(MediaType.APPLICATION_JSON)
                .content(departmentJson)
        ).andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void testThatCreateDepartmentSuccessfullyReturnsSavedDepartment() throws Exception {
        Department department = TestDataUtil.createDepartmentIT();
        DepartmentDto departmentDto = departmentMapper.mapTo(department);
        String departmentJson = objectMapper.writeValueAsString(departmentDto);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/department")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(departmentJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("IT")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.maxSalary").value(5000.0)
        );
    }

    @Test
    public void testThatCreateTwoDepartmentSuccessfullyReturnsConflict() throws Exception {
        Department department = TestDataUtil.createDepartmentIT();
        DepartmentDto departmentDto = departmentMapper.mapTo(department);
        String departmentJson = objectMapper.writeValueAsString(departmentDto);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/department")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(departmentJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("IT")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.maxSalary").value(5000.0)
        );

        mockMvc.perform(MockMvcRequestBuilders.post("/department")
                .contentType(MediaType.APPLICATION_JSON)
                .content(departmentJson)
        ).andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Test
    public void testThatListDepartmentReturnsHttpStatus200() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/department")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatListDepartmentReturnsListOfDepartments() throws Exception {
        Department department = TestDataUtil.createDepartmentIT();
        departmentService.save(department);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/department")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].name").value("IT")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].maxSalary").value(5000.0)
        );
    }

    @Test
    public void testThatGetDepartmentReturnsHttpStatus200WhenDepartmentExist() throws Exception {
        Department department = TestDataUtil.createDepartmentIT();
        departmentService.save(department);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/department/1")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatGetDepartmentReturnsDepartmentWhenDepartmentExist() throws Exception {
        Department department = TestDataUtil.createDepartmentIT();
        departmentService.save(department);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/department/1")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(1)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("IT")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.maxSalary").value(5000.0)
        );
    }

    @Test
    public void testThatGetDepartmentReturnsHttpStatus404WhenNoDepartmentExists() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/department/99")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testThatFullUpdateDepartmentReturnsHttpStatus404WhenNoDepartmentExists() throws Exception {
        Department department = TestDataUtil.createDepartmentIT();
        departmentService.save(department);
        DepartmentDto departmentDto = departmentMapper.mapTo(department);
        String departmentJson = objectMapper.writeValueAsString(departmentDto);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/department/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(departmentJson)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testThatFullUpdateDepartmentReturnsHttpStatus200WhenDepartmentExists() throws Exception {
        Department department = TestDataUtil.createDepartmentIT();
        Department savedDepartment = departmentService.save(department);
        DepartmentDto departmentDto = departmentMapper.mapTo(savedDepartment);
        String departmentJson = objectMapper.writeValueAsString(departmentDto);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/department/" + savedDepartment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(departmentJson)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatFullUpdateUpdatesExistingDepartment() throws Exception {
        Department department = TestDataUtil.createDepartmentIT();
        Department savedDepartment = departmentService.save(department);

        Department departmentTwo = TestDataUtil.createDepartmentHR();
        departmentTwo.setId(savedDepartment.getId());
        DepartmentDto departmentDto = departmentMapper.mapTo(departmentTwo);

        String departmentJson = objectMapper.writeValueAsString(departmentDto);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/department/" + savedDepartment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(departmentJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(departmentTwo.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(departmentTwo.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.maxSalary").value(departmentTwo.getMaxSalary())
        );
    }

    @Test
    public void testThatSalaryUpdateExistingDepartmentReturnsHttpStatus20O() throws Exception {
        Department department = TestDataUtil.createDepartmentIT();
        Department savedDepartment = departmentService.save(department);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/department/" + savedDepartment.getId() + "/max-salary")
                        .param("increase", "100") // Use param to send the increase value
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatSalaryUpdateExistingDepartmentReturnsUpdatedDepartment() throws Exception {
        Department department = TestDataUtil.createDepartmentIT();
        Department savedDepartment = departmentService.save(department);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/department/" + savedDepartment.getId() + "/max-salary")
                        .param("increase", "100") // Use param to send the increase value
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(savedDepartment.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("IT")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.maxSalary").value(savedDepartment.getMaxSalary()+100)
        );
    }

    @Test
    public void testThatDeleteDepartmentReturnsHttpStatus404ForNonExistingDepartment() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/department/999")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testThatDeleteDepartmentReturnsHttpStatus204ForExistingDepartment() throws Exception {
        Department department = TestDataUtil.createDepartmentIT();
        Department savedDepartment = departmentService.save(department);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/department/" + savedDepartment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
