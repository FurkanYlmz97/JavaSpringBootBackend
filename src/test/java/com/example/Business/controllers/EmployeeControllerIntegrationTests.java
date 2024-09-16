package com.example.Business.controllers;


import com.example.Business.TestDataUtil;
import com.example.Business.domain.dto.EmployeeDto;
import com.example.Business.domain.entities.Department;
import com.example.Business.domain.entities.Employee;
import com.example.Business.mappers.impl.EmployeeMapper;
import com.example.Business.repositories.DepartmentRepository;
import com.example.Business.repositories.EmployeeRepository;
import com.example.Business.services.EmployeeService;
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
public class EmployeeControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    public EmployeeControllerIntegrationTests(MockMvc mockMvc, EmployeeMapper employeeMapper, EmployeeService employeeService) {
        this.mockMvc = mockMvc;
        this.objectMapper = new ObjectMapper();
        this.employeeMapper = employeeMapper;
        this.employeeService = employeeService;
    }


    @Test
    public void testThatCreateEmployeeSuccessfullyReturnsHttp201Created() throws Exception {

        Employee manager = TestDataUtil.createManager();
        EmployeeDto managerDto = employeeMapper.mapTo(manager);
        String managerJson = objectMapper.writeValueAsString(managerDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/employee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(managerJson)
        ).andExpect(MockMvcResultMatchers.status().isCreated());

        Employee employee = TestDataUtil.createEmployeeClara(manager, manager.getDepartment());
        EmployeeDto employeeDto = employeeMapper.mapTo(employee);
        String employeeJson = objectMapper.writeValueAsString(employeeDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/employee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(employeeJson)
        ).andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void testThatCreateEmployeeTwiceSuccessfullyReturnsHttp201Created() throws Exception {

        Employee manager = TestDataUtil.createManager();
        EmployeeDto managerDto = employeeMapper.mapTo(manager);
        String managerJson = objectMapper.writeValueAsString(managerDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/employee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(managerJson)
        ).andExpect(MockMvcResultMatchers.status().isCreated());

        managerDto.setEmail("sdsdsd");
        managerJson = objectMapper.writeValueAsString(managerDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/employee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(managerJson)
        ).andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void testThatCreateDepartmentSuccessfullyReturnsSavedEmployee() throws Exception {

        Employee manager = TestDataUtil.createManager();
        EmployeeDto managerDto = employeeMapper.mapTo(manager);
        String employeeJson = objectMapper.writeValueAsString(managerDto);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(employeeJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("Michael")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.email").value("michi@mail.com")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.position").value("Software")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.yearsOfExperience").value(3)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.department").value(manager.getDepartment())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.manager").isEmpty()
        );

        Employee employee = TestDataUtil.createEmployeeClara(manager, manager.getDepartment());
        EmployeeDto employeeDto = employeeMapper.mapTo(employee);
        employeeJson = objectMapper.writeValueAsString(employeeDto);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(employeeJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("Clara")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.email").value("clara@mail.com")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.position").value("Software")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.yearsOfExperience").value(1)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.department").value(manager.getDepartment())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.manager.email").value(manager.getEmail())
        );
    }

    @Test
    public void testThatCreateTwoEmployeeSameEmailSuccessfullyReturnsConflict() throws Exception {
        Employee manager = TestDataUtil.createManager();
        EmployeeDto managerDto = employeeMapper.mapTo(manager);
        String employeeJson = objectMapper.writeValueAsString(managerDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/employee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(employeeJson)
        ).andExpect(MockMvcResultMatchers.status().isCreated());

        mockMvc.perform(MockMvcRequestBuilders.post("/employee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(employeeJson)
        ).andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Test
    public void testThatListEmployeeReturnsHttpStatus200() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/employee")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatListEmployeeReturnsListOfEmployees() throws Exception {
        Employee manager = TestDataUtil.createManager();
        EmployeeDto managerDto = employeeMapper.mapTo(manager);
        String employeeJson = objectMapper.writeValueAsString(managerDto);


        mockMvc.perform(
                MockMvcRequestBuilders.post("/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(employeeJson)
        );

        mockMvc.perform(
                MockMvcRequestBuilders.get("/employee")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].name").value("Michael")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].email").value("michi@mail.com")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].position").value("Software")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].yearsOfExperience").value(3)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].department").value(manager.getDepartment())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].manager").isEmpty()
        );
    }

    @Test
    public void testThatGetEmployeeReturnsHttpStatus200WhenEmployeeExist() throws Exception {

        Employee employee = TestDataUtil.createManager();
        employeeService.save(employee);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/employee/1")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatGetEmployeeReturnsEmployeeWhenEmployeeExist() throws Exception {
        Employee employee = TestDataUtil.createManager();
        employeeService.save(employee);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/employee/1")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("Michael")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.email").value("michi@mail.com")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.position").value("Software")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.yearsOfExperience").value(3)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.department.name").value("IT")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.manager").isEmpty()
        );
    }

    @Test
    public void testThatGetEmployeeReturnsHttpStatus404WhenNoEmployeeExists() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/employee/99")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testThatFullUpdateEmployeeReturnsHttpStatus404WhenNoDEmployeeExists() throws Exception {

        Employee manager = TestDataUtil.createManager();
        employeeService.save(manager);
        EmployeeDto managerDto = employeeMapper.mapTo(manager);
        String employeeJson = objectMapper.writeValueAsString(managerDto);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/employee/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(employeeJson)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testThatFullUpdateEmployeeReturnsHttpStatus200WhenEmployeeExists() throws Exception {
        Employee manager = TestDataUtil.createManager();
        EmployeeDto managerDto = employeeMapper.mapTo(manager);
        String employeeJson = objectMapper.writeValueAsString(managerDto);
        Employee savedEmployee = employeeService.save(manager);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/employee/" + savedEmployee.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(employeeJson)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatFullUpdateUpdatesExistingEmployee() throws Exception {
        Employee manager = TestDataUtil.createManager();
        Employee savedEmployee = employeeService.save(manager);

        Employee employee = TestDataUtil.createEmployeeClara(null, savedEmployee.getDepartment());
        EmployeeDto employeeDto = employeeMapper.mapTo(employee);
        String employeeJson = objectMapper.writeValueAsString(employeeDto);



        mockMvc.perform(MockMvcRequestBuilders.put("/employee/" + savedEmployee.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(employeeJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("Clara")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.email").value("clara@mail.com")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.position").value("Software")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.yearsOfExperience").value(1)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.department").value(manager.getDepartment())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.manager").isEmpty()
        );
    }

    @Test
    public void testThatSalaryUpdateEmployeeReturnsCorrectHttpStatus() throws Exception {

        Employee manager = TestDataUtil.createManager();
        Employee savedManager = employeeService.save(manager);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/employee/" + savedManager.getId() + "/promote")
                        .param("increase", "100") // Use param to send the increase value
        ).andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/employee/" + savedManager.getId() + "/promote")
                        .param("increase", "10000") // Use param to send the increase value
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/employee/" + 99 + "/promote")
                        .param("increase", "10000") // Use param to send the increase value
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testThatSalaryUpdateEmployeeReturnsCorrectResults() throws Exception {
        Employee manager = TestDataUtil.createManager();
        Employee savedManager = employeeService.save(manager);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/employee/" + savedManager.getId() + "/promote")
                        .param("increase", "100") // Use param to send the increase value
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.salary").value(savedManager.getSalary()+100)
        );
    }

    @Test
    public void testThatAssignEmployeeReturnsCorrectHttpStatus() throws Exception {

        Employee manager = TestDataUtil.createManager();
        Employee savedManager = employeeService.save(manager);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/employee/" + 99 + "/assign")
                        .param("departmentId", "1") // Use param to send the increase value
        ).andExpect(MockMvcResultMatchers.status().isNotFound());

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/employee/" + savedManager.getId() + "/assign")
                        .param("departmentId", "2") // Use param to send the increase value
        ).andExpect(MockMvcResultMatchers.status().isNotFound());

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/employee/" + 1 + "/assign")
                        .param("departmentId", "1") // Use param to send the increase value
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatAssignEmployeeReturnsCorrectResults() throws Exception {
        Employee manager = TestDataUtil.createManager();
        Employee savedManager = employeeService.save(manager);

        Department department = TestDataUtil.createDepartmentHR();
        Department savedDepartment = departmentRepository.save(department);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/employee/" + savedManager.getId() + "/assign")
                        .param("departmentId", savedDepartment.getId().toString()) // Use param to send the increase value
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatExperienceEmployeeReturnsCorrectHttpStatus() throws Exception {

        Employee manager = TestDataUtil.createManager();
        Employee savedManager = employeeService.save(manager);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/employee/" + 99 + "/experience")
                        .param("experienceYearToAdd", "1") // Use param to send the increase value
        ).andExpect(MockMvcResultMatchers.status().isNotFound());

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/employee/" + savedManager.getId() + "/experience")
                        .param("experienceYearToAdd", "2") // Use param to send the increase value
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatExperiencenEmployeeReturnsCorrectResults() throws Exception {
        Employee manager = TestDataUtil.createManager();
        Employee savedManager = employeeService.save(manager);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/employee/" + savedManager.getId() + "/experience")
                        .param("experienceYearToAdd", "2") // Use param to send the increase value
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.yearsOfExperience").value(manager.getYearsOfExperience()+2)
        );
    }

    @Test
    public void testThatDeleteEmployeeReturnsHttpStatus404ForNonExistingEmployee() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/employee/999")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testThatDeleteEmployeeReturnsHttpStatus204ForExistingEmployee() throws Exception {
        Employee employee = TestDataUtil.createManager();
        Employee savedEmployee = employeeService.save(employee);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/employee/" + savedEmployee.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }

}
