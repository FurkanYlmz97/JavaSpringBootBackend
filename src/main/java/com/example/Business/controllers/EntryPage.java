package com.example.Business.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EntryPage {

    @GetMapping
    public String introduction() {
        String welcomeMessage = "Welcome to the Business API! This API allows you to manage departments and employees within an organization. <br>" +
                "To explore and interact with the API, you can use the Swagger UI, which provides a user-friendly interface for testing the API endpoints. <br>" +
                "To access the Swagger UI, open a web browser and navigate to the following URL: <a href='swagger-ui/index.html#/'>swagger-ui/index.html#/</a>. <br><br>" +
                "In the Swagger UI, you will see a list of available endpoints categorized by controllers. Here are the key methods you can try out: <br><br>" +
                "<strong>1. DepartmentController:</strong><br>" +
                "- <strong>POST /department:</strong> Add a new department by providing the department's details in the request body. <br>" +
                "- <strong>GET /department:</strong> Retrieve a list of all departments. <br>" +
                "- <strong>GET /department/{id}:</strong> Get details of a specific department by its ID. <br>" +
                "- <strong>PUT /department/{id}:</strong> Update the details of an existing department by its ID. <br>" +
                "- <strong>DELETE /department/{id}:</strong> Delete a department by its ID. When a department is deleted, all employees associated with that department will have their department set to null. <br>" +
                "- <strong>PATCH /department/{id}/increase-max-salary:</strong> Increase the maximum salary limit for a department. <br><br>" +
                "<strong>Business Rules for Departments:</strong><br>" +
                "- A department can only be deleted if it exists. When deleting a department, all employees in that department will be disassociated from it (their department will be set to null). <br>" +
                "- When increasing the maximum salary for a department, the new salary limit is added to the current maximum salary. <br><br>" +
                "<strong>2. EmployeeController:</strong><br>" +
                "- <strong>POST /employee:</strong> Add a new employee by providing the employee's details in the request body. If the employee has a department or manager that does not exist in the database, they will be created automatically. <br>" +
                "- <strong>GET /employee:</strong> Retrieve a list of all employees. <br>" +
                "- <strong>GET /employee/{id}:</strong> Get details of a specific employee by their ID. <br>" +
                "- <strong>GET /employee/by-department?departmentId={departmentId}:</strong> Retrieve a list of employees within a specific department. <br>" +
                "- <strong>PUT /employee/{id}:</strong> Update the details of an existing employee by their ID. <br>" +
                "- <strong>DELETE /employee/{id}:</strong> Delete an employee by their ID. When an employee is deleted, they will be disassociated from their department, and any employees they manage will no longer have them as a manager. <br>" +
                "- <strong>PATCH /employee/{id}/promote:</strong> Promote an employee by increasing their salary. Employees can only be promoted if the new salary does not exceed the department's maximum salary and if they have at least 2 years of experience. <br>" +
                "- <strong>PATCH /employee/{employeeId}/assign?departmentId={departmentId}:</strong> Assign an employee to a department. This can only be done if the employee's salary does not exceed the department's maximum salary. <br>" +
                "- <strong>PATCH /employee/{employeeId}/experience:</strong> Increase the experience of an employee by a specified number of years. <br><br>" +
                "<strong>Business Rules for Employees:</strong><br>" +
                "- An employee can only be assigned to a department if their salary does not exceed the department's maximum salary. <br>" +
                "- An employee can only be promoted if their new salary does not exceed the department's maximum salary and they have at least 2 years of experience. <br>" +
                "- When an employee is deleted, they will be disassociated from their department, and any employees they manage will no longer have them as a manager. <br>" +
                "- When adding an employee, if they have a department or manager not already existing in the database, they will be automatically created and saved. <br><br>" +
                "In the Swagger UI, you can expand each endpoint to view its details, including required parameters and response types. <br>" +
                "You can also execute the endpoints directly from the UI by filling in the necessary information and clicking the 'Execute' button. <br>" +
                "This makes it easy to test and interact with the API without the need for a separate REST client. <br><br>" +
                "Please ensure you provide valid data when testing the endpoints, especially for POST, PUT, and PATCH requests, as the API " +
                "includes validation logic to ensure the integrity of the data. Happy exploring!";

        return welcomeMessage;
    }
}
