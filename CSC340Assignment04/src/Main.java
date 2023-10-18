import java.util.*;

public class Main {
    static final int CREATE = 1;
    static final int FIND = 2;
    static final int UPDATE = 3;
    static final int DELETE = 4;
    static final int PRINT = 5;
    static final int QUIT = 6;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        StudentDataHandler dataHandler = new StudentDataHandler("src/students.txt", in);

        int choice = 0;
        while (choice != 6) {
            dataHandler.cleanFile();
            displayMenu();
            choice = in.nextInt();
            in.nextLine(); //consume newline
            doCommand(choice, dataHandler, in);
        }

        dataHandler.cleanFile(); //clean file one last time before exiting
    }

    /**
     * Display list of choices for user
     */
    private static void displayMenu() {
        System.out.println("""
                ---------------------------------------------------------------------------------------
                This is a database of imaginary students who go to The University of Collegiate Studies.
                Would you like to:
                1. Create a new student.
                2. Find and print a student.
                3. Update a student's info.
                4. Delete a student.
                5. Print all students.
                6. Quit.
                Please enter your choice (1-6).""");
    }

    /**
     * Performs appropriate command based on choice
     *
     * @param choice      int code based on displayMenu()
     * @param dataHandler StudentDataHandler object to perform commands on
     */
    private static void doCommand(int choice, StudentDataHandler dataHandler, Scanner in) {
        int id;
        switch (choice) {
            case CREATE:
                System.out.println("Students have an ID, name, date of birth, major, and GPA.");
                System.out.println("Please enter the student's ID.");
                id = in.nextInt();
                in.nextLine(); //consume newline
                try {
                    dataHandler.createStudent(id);
                    System.out.println("Student created.");
                } catch (IllegalArgumentException e) {
                    System.out.println("Error: " + e.getMessage());
                }
                break;
            case FIND:
                System.out.println("Please enter the student's ID to find.");
                id = in.nextInt();
                in.nextLine(); //consume newline
                try {
                    String studentData = dataHandler.findStudent(id);
                    System.out.print(studentData);
                } catch (IllegalArgumentException e) {
                    System.out.println("Error: " + e.getMessage());
                }
                break;
            case UPDATE:
                System.out.println("Please enter the student's ID to update.");
                id = in.nextInt();
                in.nextLine(); //consume newline
                try {
                    String message = dataHandler.updateStudent(id);
                    System.out.print(message);
                } catch (IllegalArgumentException e) {
                    System.out.println("Error: " + e.getMessage());
                }
                break;
            case DELETE:
                System.out.println("Please enter the student's ID to delete.");
                id = in.nextInt();
                in.nextLine(); //consume newline
                try {
                    String message = dataHandler.deleteStudent(id);
                    System.out.print(message);
                } catch (IllegalArgumentException e) {
                    System.out.println("Error: " + e.getMessage());
                }
                break;
            case PRINT:
                String response = dataHandler.getAllStudents();
                System.out.print(response);
                break;
            case QUIT:
                System.out.println("Goodbye!");
                break;
            default:
                System.out.println("Invalid input.");
        }
    }

}