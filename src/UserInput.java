// Otoniel Rodriguez-Perez
// CEN-3024C-24204
// 02/23/2025

// UserInput Class:
// This class handles all user inputs and confirmations.

import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Scanner;

public class UserInput {

    // Method checks for user's choice in the main menu.
    public int usersChoice(Scanner scanner) {
        while (true) {
            try {
                System.out.print("Enter your choice (1-8): ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Clears the buffer.
                if (choice >= 1 && choice <= 8) {
                    return choice; // Return valid input.
                }
                System.out.println("Invalid choice. Please select a number between 1 and 8.");
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Clears invalid input.
            } catch (NoSuchElementException | IllegalStateException e) {
                System.out.println("Error: Unexpected input issue. Please try again.");
                scanner = new Scanner(System.in); // Reinitialize scanner to allow retry.
            }
        }
    }

    // Method to get a patron ID from user input.
    public static int manualIdInput(Scanner scanner) {
        while (true) {
            try {
                System.out.print("ID: ");
                String input = scanner.nextLine().trim();
                if (input.matches("\\d{8}") && Integer.parseInt(input) >= 10000000) {
                    int id = Integer.parseInt(input);

                    // Checks if the ID is unique.
                    if (StudentManagement.students.stream().noneMatch(student -> student.getId() == id)) {
                        return id; // Valid ID.
                    } else {
                        System.out.println("ID is already in use. Please enter a different number.");
                    }
                } else {
                    System.out.println("Invalid ID. It must be an 8-digit number starting with '1'.");
                }
            } catch (NoSuchElementException | IllegalStateException e) {
                System.out.println("Input issue detected. Please try again.");
                scanner = new Scanner(System.in);
            }
        }
    }

    private static String getValidatedString(Scanner scanner, String prompt, int maxLength) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                if (!input.isEmpty() && input.length() <= maxLength) {
                    return input;
                } else {
                    System.out.printf("Invalid input. Enter up to %d characters.%n", maxLength);
                }
            } catch (NoSuchElementException | IllegalStateException e) {
                System.out.println("Unexpected input issue. Please try again.");
                scanner = new Scanner(System.in);
            }
        }
    }

    public static String firstNameInput(Scanner scanner, String prompt) {
        return getValidatedString(scanner, prompt, 15);
    }

    public static String lastNameInput(Scanner scanner, String prompt) {
        return getValidatedString(scanner, prompt, 25);
    }

    public static String phoneNumberInput(Scanner scanner, String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                if (input.matches("\\(\\d{3}\\)\\s?\\d{3}-\\d{4}") || input.matches("\\(\\d{3}\\)\\d{3}-\\d{4}")) {
                    return input;
                } else {
                    System.out.println("Invalid input. Phone number must be in format (555) 555-5555.");
                }
            } catch (NoSuchElementException | IllegalStateException e) {
                System.out.println("Unexpected input issue. Please try again.");
                scanner = new Scanner(System.in);
            }
        }
    }

    public static String emailInput(Scanner scanner, String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                if (input.matches("^[A-Za-z0-9+_.-]+@[A-Za-z]+(?:\\.[A-Za-z]{2,3})$")) {
                    if (StudentManagement.students.stream().noneMatch(student -> Objects.equals(student.getEmail(), input))) {
                        return input; // Valid email.
                    } else {
                        System.out.println("Email is already in use. Please enter a different one.");
                    }
                } else {
                    System.out.println("Invalid email. Format: example@gmail.com");
                }
            } catch (NoSuchElementException | IllegalStateException e) {
                System.out.println("Unexpected input issue. Please try again.");
                scanner = new Scanner(System.in);
            }
        }
    }

    public static double gpaInput(Scanner scanner) {
        while (true) {
            try {
                System.out.print("GPA: ");
                if (scanner.hasNextDouble()) {
                    double gpa = scanner.nextDouble();
                    scanner.nextLine(); // Clears buffer.
                    if (gpa >= 0 && gpa <= 1.9) {
                        return gpa;
                    } else {
                        System.out.println("Invalid GPA. Must be between 0 and 1.9.");
                    }
                } else {
                    System.out.println("Invalid input. Please enter a numeric value.");
                    scanner.nextLine(); // Clears invalid input.
                }
            } catch (NoSuchElementException | IllegalStateException e) {
                System.out.println("Unexpected input issue. Please try again.");
                scanner = new Scanner(System.in);
            }
        }
    }

    public static int studentIdSearch(Scanner scanner) {
        while (true) {
            try {
                System.out.print("Enter student ID: ");
                String input = scanner.nextLine().trim();
                int id = Integer.parseInt(input);
                if (id >= 10000000 && id <= 99999999) {
                    return id;
                } else {
                    System.out.println("Invalid ID. It must be exactly 8 digits.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid 8-digit number.");
            } catch (NoSuchElementException | IllegalStateException e) {
                System.out.println("Unexpected input issue. Please try again.");
                scanner = new Scanner(System.in);
            }
        }
    }

    public static Student findStudentById(int id) {
        return StudentManagement.students.stream()
                .filter(student -> student.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public static boolean userConfirmation(Scanner scanner, String message) {
        while (true) {
            try {
                System.out.print(message);
                String choice = scanner.nextLine().trim().toLowerCase();
                if (choice.equals("y") || choice.equals("yes")) {
                    return true;
                } else if (choice.equals("n") || choice.equals("no")) {
                    return false;
                } else {
                    System.out.println("Invalid input. Enter 'y' for yes or 'n' for no.");
                }
            } catch (NoSuchElementException | IllegalStateException e) {
                System.out.println("Unexpected input issue. Please try again.");
                scanner = new Scanner(System.in);
            }
        }
    }
}
