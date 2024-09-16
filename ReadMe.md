# Welcome to the Business API

Welcome to the Business API! This API allows you to manage departments and employees within an organization.

To explore and interact with the API, after building and running the containers, you can use the Swagger UI, which provides a user-friendly interface for testing the API endpoints. To access the Swagger UI, open a web browser and navigate to the following URL: [Swagger UI](/swagger-ui/index.html#/).

## API Endpoints

In the Swagger UI, you will see a list of available endpoints categorized by controllers. Here are the key methods you can try out:

### 1. DepartmentController:

- **POST /department:** Add a new department by providing the department's details in the request body.
- **GET /department:** Retrieve a list of all departments.
- **GET /department/{id}:** Get details of a specific department by its ID.
- **PUT /department/{id}:** Update the details of an existing department by its ID.
- **DELETE /department/{id}:** Delete a department by its ID. When a department is deleted, all employees associated with that department will have their department set to null.
- **PATCH /department/{id}/increase-max-salary:** Increase the maximum salary limit for a department.

**Business Rules for Departments:**
- A department can only be deleted if it exists. When deleting a department, all employees in that department will be disassociated from it (their department will be set to null).
- When increasing the maximum salary for a department, the new salary limit is added to the current maximum salary.

### 2. EmployeeController:

- **POST /employee:** Add a new employee by providing the employee's details in the request body. If the employee has a department or manager that does not exist in the database, they will be created automatically.
- **GET /employee:** Retrieve a list of all employees.
- **GET /employee/{id}:** Get details of a specific employee by their ID.
- **GET /employee/by-department?departmentId={departmentId}:** Retrieve a list of employees within a specific department.
- **PUT /employee/{id}:** Update the details of an existing employee by their ID.
- **DELETE /employee/{id}:** Delete an employee by their ID. When an employee is deleted, they will be disassociated from their department, and any employees they manage will no longer have them as a manager.
- **PATCH /employee/{id}/promote:** Promote an employee by increasing their salary. Employees can only be promoted if the new salary does not exceed the department's maximum salary and if they have at least 2 years of experience.
- **PATCH /employee/{employeeId}/assign?departmentId={departmentId}:** Assign an employee to a department. This can only be done if the employee's salary does not exceed the department's maximum salary.
- **PATCH /employee/{employeeId}/experience:** Increase the experience of an employee by a specified number of years.

**Business Rules for Employees:**
- An employee can only be assigned to a department if their salary does not exceed the department's maximum salary.
- An employee can only be promoted if their new salary does not exceed the department's maximum salary and they have at least 2 years of experience.
- When an employee is deleted, they will be disassociated from their department, and any employees they manage will no longer have them as a manager.
- When adding an employee, if they have a department or manager not already existing in the database, they will be automatically created and saved.

In the Swagger UI, you can expand each endpoint to view its details, including required parameters and response types. You can also execute the endpoints directly from the UI by filling in the necessary information and clicking the 'Execute' button. This makes it easy to test and interact with the API without the need for a separate REST client.

Please ensure you provide valid data when testing the endpoints, especially for POST, PUT, and PATCH requests, as the API includes validation logic to ensure the integrity of the data. Happy exploring!
