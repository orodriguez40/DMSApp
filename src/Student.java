// Otoniel Rodriguez-Perez
// CEN-3024C-24204
// 03/12/2025

// Student Class:
// Used to define what a kind of information a student will contain.
// It will be called by the StudentManagement class to perform CRUD operations and the custom action.

public class Student {

    //Attributes
    int id;
    String firstName;
    String lastName;
    String phoneNumber;
    String email;
    double gpa;
    boolean isContacted;

    //Constructor
    public Student(int id, String firstName, String lastName, String phoneNumber, String email, double gpa, boolean isContacted) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.gpa = gpa;
        this.isContacted = isContacted;
    }

    // Getters

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public double getGpa() {
        return gpa;
    }

    public boolean getIsContacted() {
        return isContacted;
    }

    // Setters

    public void setId(int id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setGpa(double gpa) {
        this.gpa = gpa;
    }

    public void setIsContacted(boolean contacted) {
        isContacted = contacted;
    }

    // toString method is overwritten to display student information.
    @Override
    public String toString() {
        return "\nID: S" + id +
                "\nName: " + firstName + " " + lastName +
                "\nPhone Number: " + phoneNumber +
                "\nEmail: " + email +
                "\nGPA: " + gpa +
                "\nContacted: " + isContacted + "\n";
    }
}
