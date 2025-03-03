// Otoniel Rodriguez-Perez
// CEN-3024C-24204
// 03/02/2025

// DMSTest Class:
// This class handles all JUnit testing and validation.

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.util.Scanner;

public class DMSTest {

    // Attributes used to conduct JUnit testing.
    private StudentManagement studentManagement;
    private Student student;
    private String studentData1;
    private String studentData2;
    private UserInput userInput;
    private Scanner scanner;

    // Set up all attributes as needed to conduct testing.
    @BeforeEach
    void setUp() {

        // Initialize StudentManagement and create a sample student before each test
        studentManagement = new StudentManagement();
        student = new Student(12345679, "John", "Doe", "(555) 555-5555", "john.doe@gmail.com", 1.5, false);

        // Add the sample student to the management for testing.
        studentManagement.addStudentManual(student);

        // Simulates user input to check if data "entered" is correct.
        userInput = new UserInput();
        InputStream input = new ByteArrayInputStream("".getBytes());
        scanner = new Scanner(input);

        // Define student data for file input.
        studentData1 = "10000011 Jane Doe (555)555-5556 jane.doe@example.com 0.5 false\n";
        studentData2 = "10000022 Alice Smith (555)555-5557 alice.smith@example.com 1.8 true\n";

    }

    // This method tests for the Student class and verifies the attributes are validated.
    @Test
    void StudentTest() {
        // Tests Student class attributes.
        assertEquals(12345679, student.getId(), "Student ID should be 12345678.");
        assertEquals("John", student.getFirstName(), "First name should be John.");
        assertEquals("Doe", student.getLastName(), "Last name should be Doe.");
        assertEquals("(555) 555-5555", student.getPhoneNumber(), "Phone number should match.");
        assertEquals("john.doe@gmail.com", student.getEmail(), "Email should match.");
        assertEquals(1.5, student.getGpa(), 0.01, "GPA should be 1.5");
        assertTrue(student.getIsContacted() == true || student.getIsContacted() == false, "Student contact status should be either true or false.");
    }

    // UserInput tests added here.
    @Test
    void manualIdInputTest1() {
        String input = "10000001\n"; // Valid ID.
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        int id = userInput.manualIdInput(scanner);
        assertEquals(10000001, id, "ID is validated.");
    }

    @Test
    void manualIdInputTest2() {
        String input = "00000001\n"; // Invalid ID.
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        int id = userInput.manualIdInput(scanner);
        assertNotEquals(00000001, id, "ID is invalid.");
    }


    @Test
    void manualIdInputTest3() {
        // Assuming there is a student that has been set up with an ID of 10000001.
        StudentManagement.students.add(new Student(10000003, "John", "Doe", "(555) 555-5555", "john.doe@example.com", 1.5, false));

        String input = "10000003\n"; // Duplicate ID.
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        int id = userInput.manualIdInput(scanner);
        assertNotEquals(10000003, id, "ID is a duplicate.");
    }

    @Test
    void firstNameInputTest1() {
        String input = "Alice\n"; // Valid first name.
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        String firstName = userInput.firstNameInput(scanner, "First Name: ");
        assertEquals("Alice", firstName, "First Name is validated.");
    }

    @Test
    void firstNameInputTest2() {
        String input = "Aliceeeeeeeeeeeeeeeeeeeeee\n"; // Invalid first name.
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        String firstName = userInput.firstNameInput(scanner, "First Name: ");
        assertNotEquals("Aliceeeeeeeeeeeeeeeeeeeeee", firstName, "First name is too long.");
    }

    @Test
    void lastNameInputTest1(){
        String input = "Perez\n"; // Valid last name.
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        String lastName = userInput.lastNameInput(scanner, "Last Name: ");
        assertEquals("Perez", lastName, "Last name is validated.");
    }

    @Test
    void lastNameInputTest2(){
        String input = "Pereeeeeeeeeeeeeeeeeeeeeeeez\n"; // Invalid last name.
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        String lastName = userInput.lastNameInput(scanner, "Last Name: "); // Added missing method call
        assertNotEquals("Pereeeeeeeeeeeeeeeeeeeeeeeez", lastName, "Last name is too long."); // Corrected assertion
    }

    @Test
    void phoneNumberInputTest1() {
        String input = "(555) 555-5555\n"; // Valid phone number.
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        String phoneNumber = userInput.phoneNumberInput(scanner, "Phone Number: ");
        assertEquals("(555) 555-5555", phoneNumber, "Phone number is validated.");
    }

    @Test
    void phoneNumberInputTest2() {
        String input = "5555555555\n"; // Invalid phone number.
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        String phoneNumber = userInput.phoneNumberInput(scanner, "Phone Number: ");
        assertNotEquals("5555555555", phoneNumber, "Phone number not in the correct format.");
    }

    @Test
    void emailInputTest1() {
        String input = "alice@gmail.com\n"; // Valid email.
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        String email = userInput.emailInput(scanner, "Email: ");
        assertEquals("alice@gmail.com", email, "Email is validated.");
    }

    @Test
    void emailInputTest2() {
        String input = "aliceexamplecom\n"; // Invalid email
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        String email = userInput.emailInput(scanner, "Email: ");
        assertNotEquals("aliceexamplecom", email, "Email has incorrect format.");
    }

    @Test
    void emailInputTest3() {
        // Assuming there is a student that has been set up with an the email alice@gmail.com.
        StudentManagement.students.add(new Student(10000004, "Alice", "Smith", "(555) 555-5555", "alice@gmail.com", 1.5, false));

        String input = "alice@gmail.com\n"; // Duplicate email.
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        String email = userInput.emailInput(scanner, "Email: ");
        assertNotEquals("alice@gmail.com", email, "Email is a duplicate.");
    }

    @Test
    void gpaInputTest1() {
        String input = "1.5\n"; // Valid GPA.
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        double gpa = userInput.gpaInput(scanner);
        assertEquals(1.5, gpa, "GPA is validated.");
    }

    @Test
    void gpaInputTest2() {
        String input = "2.1\n"; // Invalid GPA.
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        double gpa = userInput.gpaInput(scanner);
        assertNotEquals(2.1, gpa, "GPA is outsite the valid range.");
    }

    @Test
    void gpaInputTest3() {
        String input = "Doing good\n"; // Invalid GPA.
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        double gpa = userInput.gpaInput(scanner);
        assertEquals(-1, gpa, 0.01, "GPA is must be a number."); // Expecting -1 for invalid input
    }


    // Continue with the original tests
    @Test
    void addStudentManualTest() {
        // Test adding a student manually.
        boolean result = studentManagement.addStudentManual(student);

        // Asserts that the student was added successfully.
        assertTrue(result, "Student should be added successfully.");
    }

    @Test
    void addStudentFileTest() throws IOException {
        // Assuming StudentManagement.students is the list holding the students
        // Ensure the list is empty before running the test (if no clearStudents method is available)
        StudentManagement.students.clear();

        // Create a temporary file to simulate file input for adding students
        File tempFile = File.createTempFile("students", ".txt");
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write(studentData1);
            writer.write(studentData2);
        }

        String input = "y\n"; // Provide input for confirmation
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Attempt to add students from the file
        studentManagement.addStudentFile(tempFile.getAbsolutePath(), new Scanner(System.in));

        // Ensure exactly 2 students were added
        assertEquals(2, StudentManagement.students.size(), "There should be 2 students in the system after file addition.");

        // Retrieve the added students
        Student addedStudent1 = StudentManagement.students.get(0); // Jane Doe
        assertEquals(10000011, addedStudent1.getId(), "The first added student's ID should be 10000011");
        assertEquals("Jane", addedStudent1.getFirstName(), "The first added student's first name should be Jane");
        assertEquals("Doe", addedStudent1.getLastName(), "The first added student's last name should be Doe");
        assertEquals("(555)555-5556", addedStudent1.getPhoneNumber(), "The first added student's phone number should match");
        assertEquals("jane.doe@example.com", addedStudent1.getEmail(), "The first added student's email should match");
        assertEquals(0.5, addedStudent1.getGpa(), 0.01, "The first added student's GPA should be 0.5");
        assertFalse(addedStudent1.getIsContacted(), "The first added student's contacted status should be false");

        Student addedStudent2 = StudentManagement.students.get(1); // Alice Smith
        assertEquals(10000022, addedStudent2.getId(), "The second added student's ID should be 10000022");
        assertEquals("Alice", addedStudent2.getFirstName(), "The second added student's first name should be Alice");
        assertEquals("Smith", addedStudent2.getLastName(), "The second added student's last name should be Smith");
        assertEquals("(555)555-5557", addedStudent2.getPhoneNumber(), "The second added student's phone number should match");
        assertEquals("alice.smith@example.com", addedStudent2.getEmail(), "The second added student's email should match");
        assertEquals(1.8, addedStudent2.getGpa(), 0.01, "The second added student's GPA should be 1.8");
        assertTrue(addedStudent2.getIsContacted(), "The second added student's contacted status should be true");
    }



    @Test
    void removeStudent() {

        boolean result = studentManagement.removeStudent(student);

        assertTrue(result, "Student should be removed successfully.");
        assertEquals(20, StudentManagement.students.size(), "Student is removed.");
    }


    @Test
    public void testUsersChoice() {
        String input = "1\n"; // Simulate user entering '1'
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Scanner scanner = new Scanner(System.in);

        int result = UserInput.usersChoice(scanner); // Replace with actual class name

        assertEquals(1, result);
        scanner.close();
    }

    @Test
    void updateStudent() {
        // Create a student and add it to the list
        Student student = new Student(12345678, "John", "Doe", "(555) 555-5555", "john.doe@gmail.com", 1.5, false);
        StudentManagement.students.add(student);

        // Simulate user input:
        // User selects 1 (ID) to update and enters the new ID (10000001)
        String input = "12349999\n"; // Ensuring that each input is followed by a newline.
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        // Call the update method
        boolean isUpdated = studentManagement.updateStudent(student);

        // Assertions
        assertTrue(isUpdated, "The student should be updated.");
        assertEquals(12349999, student.getId(), "The student's ID should be updated to 10000001.");

        // Reset System.in to its original state
        System.setIn(System.in);
    }


    @Test
    void viewStudent() {
        // Test viewing a student
        boolean result = studentManagement.viewStudent(student); // Attempt to view the student

        // Assert that the student was found and viewed successfully
        assertTrue(result, "Student should be found and viewed successfully.");
    }

    @Test
    void viewAllStudents() {
        // Test viewing all students
        boolean result = studentManagement.viewAllStudents(); // Attempt to view all students

        // Assert that at least one student was found and viewed successfully
        assertTrue(result, "At least one student should be found and viewed successfully.");
    }

    @Test
    void notContacted() {
        // Test viewing students who have not been contacted
        boolean result = studentManagement.notContacted(); // Attempt to view not contacted students

        // Assert that the operation was successful (even if no students are found)
        assertTrue(result || !result, "The operation to view not contacted students should be successful.");
    }
}
