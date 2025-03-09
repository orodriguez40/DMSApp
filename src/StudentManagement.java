// Otoniel Rodriguez-Perez
// CEN-3024C-24204
// 03/02/2025

// StudentManagement Class:
// This class manages all CRUD functions related to Students.

// Imported Library
import java.util.*;

public class StudentManagement {

    // Attribute to store all Students in an ArrayList.
    static final List<Student> students = new ArrayList<>();

    Scanner scanner = new Scanner(System.in);

    // Method is called to add a Student manually.
    public static boolean addStudentManual(Student newStudent) {
        if (newStudent != null) {
            students.add(newStudent);
            return true; // Successfully added student.
        } else {
            return false; // Student not added.
        }
    }
    }
