# Success University Outreach DMS

## Developer/Presenter  
**Otoniel Rodriguez-Perez**  

## Class Information  
- **Class:** CEN-3024C  
- **CRN:** 24204  
- **Date:** 02/23/2025  

## Project Overview  
The **Success University Outreach DMS** is a **Data Management System (DMS)** designed to assist **Success University** administrators in tracking students who are below **Satisfactory Academic Progress (SAP)**. The system aims to streamline student outreach efforts by organizing student data, monitoring GPA, and facilitating communication between staff and students.  

---

## Features  

### Functional Requirements  
- **Student Data Management:**  
  - Add student information manually or via text file.  
  - View all students in the database.  
  - Update student information.  
  - Delete student records when they meet SAP.  

- **Custom Functionalities:**  
  - Display a list of students who have not been contacted.  
  - Calculate the required GPA for students to meet SAP (2.0).  

### Non-Functional Requirements  
- **Scalability:** Capable of handling increasing student data efficiently.  
- **Performance:** Rapid response time for CRUD operations.  
- **Security:** Authentication required (username/password).  
- **Compliance:** Proper handling of student **Personally Identifiable Information (PII)**.  

---

## Data Storage & Development Phases  
1. **Phase 1:** Command Line Interface (CLI) implementation.  
2. **Phase 2:** Unit testing for functional validation.  
3. **Phase 3:** Implementation of a **Graphical User Interface (GUI)** using **JavaFX**.  
4. **Phase 4:** Integration with a **MySQL database** using **JDBC**.  

---

## User Interface (UI) Design  
- **Login Page:** Username & password authentication.  
- **Dashboard:** Navigation for adding, viewing, updating, and deleting students.  
- **Student Management:** Manual entry & file-based student uploads.  
- **Student Search:** Find students by ID.  
- **Outreach Tracking:** Identify students who need outreach & calculate required GPA.  

---

## UML & User Stories  
### UML Use Case Diagrams  
- Use case diagrams illustrate system functionalities.  

### User Stories  
- As an **outreach specialist**, I want to manually enter student information to track outreach efforts.  
- As an **outreach specialist**, I want to upload a text file of student lists to avoid manual entry.  
- As an **outreach specialist**, I want to view a list of students needing outreach.  
- As an **outreach specialist**, I want to update or remove student information when necessary.  

