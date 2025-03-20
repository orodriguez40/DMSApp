// Otoniel Rodriguez-Perez
// CEN-3024C-24204
// 04/03/2025

/**
 * Student Class:
        * Used to define what a kind of information a student will contain.
        * <p>
     * It will be called by the StudentManagement class to perform CRUD operations and the custom action.
        * </p>

 **/
public class Student {

    // Attributes
    int id;
    String firstName;
    String lastName;
    String phoneNumber;
    String email;
    double gpa;
    boolean isContacted;

    /**
     *
     * Constructor for the Student class.
     *
     * @param id          the unique identifier for the student.
     * @param firstName   the student's first name.
     * @param lastName    the student's last name.
     * @param phoneNumber the student's phone number.
     * @param email       the student's email address.
     * @param gpa         the student's grade point average.
     * @param isContacted indicates whether the student has been contacted.
     */

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

    /**
     * Gets the student's ID.
     *
     * @return the student's ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the student's first name.
     *
     * @return the student's first name.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Gets the student's last name.
     *
     * @return the student's last name.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Gets the student's phone number.
     *
     * @return the student's phone number.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Gets the student's email address.
     *
     * @return the student's email address.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Gets the student's GPA.
     *
     * @return the student's GPA.
     */
    public double getGpa() {
        return gpa;
    }

    /**
     * Gets whether the student has been contacted.
     *
     * @return true if the student has been contacted, false otherwise.
     */
    public boolean getIsContacted() {
        return isContacted;
    }

    // Setters

    /**
     * Sets the student's ID.
     *
     * @param id the new student ID.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Sets the student's first name.
     *
     * @param firstName the new first name.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Sets the student's last name.
     *
     * @param lastName the new last name.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Sets the student's phone number.
     *
     * @param phoneNumber the new phone number.
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Sets the student's email address.
     *
     * @param email the new email address.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Sets the student's GPA.
     *
     * @param gpa the new GPA.
     */
    public void setGpa(double gpa) {
        this.gpa = gpa;
    }

    /**
     * Sets whether the student has been contacted.
     *
     * @param contacted true if the student has been contacted, false otherwise.
     */
    public void setIsContacted(boolean contacted) {
        isContacted = contacted;
    }

    /**
     * Returns a string representation of the student information.
     * <p>
     * The toString method is overwritten to display student information.
     * </p>
     *
     * @return a formatted string with the student's details.
     */
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
