// Otoniel Rodriguez-Perez
// CEN-3024C-24204
// 02/23/2025

// FileHandler Class:
// This class handles file operations and conditions for adding students.

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class FileHandler {

    // Method is called to read and process file.
    public boolean addStudentsByFile(String filePath, List<Student> students, Scanner scanner) {
        // BufferedReader will attempt to read the file.
        try (BufferedReader readFile = new BufferedReader(new FileReader(filePath))) {
            // Lists to track invalid entries based on the issue type.
            List<String> invalidEntries = new ArrayList<>();
            List<String> invalidIDs = new ArrayList<>();
            List<String> invalidFirstNames = new ArrayList<>();
            List<String> invalidLastNames = new ArrayList<>();
            List<String> invalidPhoneNumbers = new ArrayList<>();
            List<String> invalidEmails = new ArrayList<>();
            List<String> invalidGPA = new ArrayList<>();
            List<String> invalidContacted = new ArrayList<>();

            // Maps and sets to track duplicates.
            Map<Integer, Integer> idCountMap = new HashMap<>(); // Tracks ID occurrences.
            Map<String, Integer> emailCountMap = new HashMap<>(); // Tracks email occurrences.
            List<Student> validStudents = new ArrayList<>();

            // Reads rows from the text file.
            String row;
            while ((row = readFile.readLine()) != null) {
                String[] details = row.split(" ");
                if (details.length != 7) {
                    invalidEntries.add(row);
                    continue;
                }

                try {
                    int id = Integer.parseInt(details[0].trim());

                    // Check if the ID is exactly 8 digits.
                    if (id < 10000000 || id > 99999999) {
                        invalidIDs.add(row);
                        continue;
                    }

                    // Checks IDs for duplicates.
                    idCountMap.put(id, idCountMap.getOrDefault(id, 0) + 1);

                    // Checks first name (1-15 characters)
                    String firstName = details[1].trim();
                    if (firstName.isEmpty() || firstName.length() > 15) {
                        invalidFirstNames.add(row);
                        continue;
                    }

                    // Checks last name (1-25 characters)
                    String lastName = details[2].trim();
                    if (lastName.isEmpty() || lastName.length() > 25) {
                        invalidLastNames.add(row);
                        continue;
                    }

                    // Checks phone number format (xxx) xxx-xxxx
                    String phoneNumber = details[3].trim();
                    if (!phoneNumber.matches("\\(\\d{3}\\)\\s?\\d{3}-\\d{4}") || !phoneNumber.matches("\\(\\d{3}\\)\\d{3}-\\d{4}")) {
                        invalidPhoneNumbers.add(row);
                        continue;
                    }

                    // Checks email format and uniqueness.
                    String email = details[4].trim();
                    if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z]+(?:\\.[A-Za-z]{2,3})$")) {
                        invalidEmails.add(row);
                        continue;
                    }

                    // Checks for duplicate emails.
                    emailCountMap.put(email, emailCountMap.getOrDefault(email, 0) + 1);

                    // Checks GPA (0.0 - 1.9).
                    double gpa;
                    try {
                        gpa = Double.parseDouble(details[5].trim());
                    } catch (NumberFormatException e) {
                        invalidGPA.add(row);
                        continue;
                    }
                    if (gpa < 0 || gpa > 1.9) {
                        invalidGPA.add(row);
                        continue;
                    }

                    // checks contacted status (true/false only).
                    String contactedValue = details[6].trim();
                    if (!contactedValue.equalsIgnoreCase("true") && !contactedValue.equalsIgnoreCase("false")) {
                        invalidContacted.add(row);
                        continue;
                    }
                    boolean isContacted = Boolean.parseBoolean(contactedValue);

                    // Add valid students to the temporary list.
                    validStudents.add(new Student(id, firstName, lastName, phoneNumber, email, gpa, isContacted));
                } catch (NumberFormatException e) {
                    invalidEntries.add(row);
                }
            }

            // Checks duplicates within the file.
            Set<Integer> duplicateIDsInFile = idCountMap.entrySet().stream()
                    .filter(entry -> entry.getValue() > 1)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toSet());

            Set<String> duplicateEmailsInFile = emailCountMap.entrySet().stream()
                    .filter(entry -> entry.getValue() > 1)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toSet());

            // Checks duplicates against DMS students.
            Set<Integer> existingStudentIDs = students.stream()
                    .map(Student::getId)
                    .collect(Collectors.toSet());

            Set<String> existingEmails = students.stream()
                    .map(Student::getEmail)
                    .collect(Collectors.toSet());

            // Combine all duplicate IDs and emails.
            Set<Integer> allDuplicateIDs = new HashSet<>(duplicateIDsInFile);
            allDuplicateIDs.addAll(existingStudentIDs);

            Set<String> allDuplicateEmails = new HashSet<>(duplicateEmailsInFile);
            allDuplicateEmails.addAll(existingEmails);

            // Remove duplicates from valid students.
            validStudents.removeIf(student -> allDuplicateIDs.contains(student.getId()) || allDuplicateEmails.contains(student.getEmail()));

            // Display results.
            if (validStudents.isEmpty()) {
                System.out.println("\nNo valid students found.");
            } else {
                System.out.println("\nValid students to be added:");
                validStudents.forEach(System.out::println);

                // User confirmation to add students
                if (UserInput.userConfirmation(scanner, "Are you sure you want to add these students? (y/n): ")) {
                    students.addAll(validStudents);
                    System.out.println("\nStudents added successfully!");
                } else {
                    System.out.println("\nNo students were added.");
                }
            }

            // Display duplicate IDs.
            if (!allDuplicateIDs.isEmpty()) {
                System.out.println("\nDuplicate IDs found (rows containing these values are skipped):\n");
                allDuplicateIDs.forEach(System.out::println);
            }

            // Display duplicate emails.
            if (!allDuplicateEmails.isEmpty()) {
                System.out.println("\nDuplicate emails found (rows containing these values are skipped):\n");
                allDuplicateEmails.forEach(System.out::println);
            }

            // Display invalid entries.
            if (!invalidEntries.isEmpty() || !invalidIDs.isEmpty() || !invalidFirstNames.isEmpty() ||
                    !invalidLastNames.isEmpty() || !invalidPhoneNumbers.isEmpty() || !invalidEmails.isEmpty() ||
                    !invalidGPA.isEmpty() || !invalidContacted.isEmpty()) {

                System.out.println("\nInvalid entries detected and skipped:");

                if (!invalidIDs.isEmpty()) {
                    System.out.println("\nThe following entries have invalid IDs (must be exactly 8 digits and start with a 1):");
                    invalidIDs.forEach(rows -> System.out.println("Invalid ID: " + rows));
                }

                if (!invalidFirstNames.isEmpty()) {
                    System.out.println("\nThe following entries have invalid first names:");
                    invalidFirstNames.forEach(rows -> System.out.println("Invalid First Name: " + rows));
                }

                if (!invalidLastNames.isEmpty()) {
                    System.out.println("\nThe following entries have invalid last names:");
                    invalidLastNames.forEach(rows -> System.out.println("Invalid Last Name: " + rows));
                }

                if (!invalidPhoneNumbers.isEmpty()) {
                    System.out.println("\nThe following entries have invalid phone numbers(must follow (xxx)xxx-xxxx format):");
                    invalidPhoneNumbers.forEach(rows -> System.out.println("Invalid Phone Number: " + rows));
                }

                if (!invalidEmails.isEmpty()) {
                    System.out.println("\nThe following entries have invalid emails (must be unique and follow example@example.com format):");
                    invalidEmails.forEach(rows -> System.out.println("Invalid Email: " + rows));
                }

                if (!invalidGPA.isEmpty()) {
                    System.out.println("\nThe following entries have invalid GPAs (must be between 0 and 1.9):");
                    invalidGPA.forEach(rows -> System.out.println("Invalid GPA: " + rows));
                }

                if (!invalidContacted.isEmpty()) {
                    System.out.println("\nThe following entries have invalid contacted status:");
                    invalidContacted.forEach(rows -> System.out.println("Invalid Contacted Status: " + rows));
                }

                if (!invalidEntries.isEmpty()) {
                    System.out.println("\nThe following entries have incorrect formatting:");
                    invalidEntries.forEach(rows -> System.out.println("Invalid Formatting: " + rows));
                }

            }

            System.out.println("\nFile processing complete. Returning to the main menu.");

        } catch (IOException e) {
            System.out.println("\nError reading the file. Please check the file path and try again.");
        }
        return true; // Modify to indicate completion without early exit
    }
}
