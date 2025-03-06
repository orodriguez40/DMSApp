// Otoniel Rodriguez-Perez
// CEN-3024C-24204
// 03/12/2025

// Admin Class:
// This is where the logic to initialize the CRUD operations and custom action will be.

// Imported Library
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Admin {
    private StudentManagement studentManagement;

    public Admin() {
        studentManagement = new StudentManagement();
    }

    public void handleUserChoice(int userChoice) {
        switch (userChoice) {
            case 1:
               // Student newStudent = UserInput.getStudentInfo();
               // boolean studentAdded = studentManagement.addStudentManual(newStudent);
               // System.out.println("Operation result(true = successfull/ false = unsuccessfull): " + studentAdded);
                break;
            case 2:
                String filepath = UserInput.getFileInfo(new Scanner(System.in));
                boolean fileProcessed = studentManagement.addStudentFile(filepath, new Scanner(System.in));
                System.out.println("Operation result(true = successfull/ false = unsuccessfull): " + fileProcessed);
                break;
            case 3:
                Student removeStudent = UserInput.searchStudentByID(new Scanner(System.in));
                boolean studentRemoved = studentManagement.removeStudent(removeStudent);
                System.out.println("Operation result(true = successfull/ false = unsuccessfull): " + studentRemoved);
                break;
            case 4:
                Student updateStudent = UserInput.searchStudentByID(new Scanner(System.in));
                boolean updatedStudent = studentManagement.updateStudent(updateStudent);
                System.out.println("Operation result(true = successfull/ false = unsuccessfull): " + updatedStudent);
                break;
            case 5:
                Student viewStudent = UserInput.searchStudentByID(new Scanner(System.in));
                boolean viewedStudent = studentManagement.viewStudent(viewStudent);
                System.out.println("Operation result(true = successfull/ false = unsuccessfull): " + viewedStudent);
                break;
            case 6:
                boolean viewedStudents = studentManagement.viewAllStudents();
                System.out.println("Operation result(true = successfull/ false = unsuccessfull): " + viewedStudents);
                break;
            case 7:
                boolean notContactedList = studentManagement.notContacted();
                System.out.println("Operation result(true = successfull/ false = unsuccessfull): " + notContactedList);
                break;
            case 8:
                System.out.println("Thank you for using the Success University's DMS application. Goodbye!\n");
                break;
            default:
                System.out.println("Invalid option. Please enter a number from 1 to 8.");
                break;
        }
    }
}


