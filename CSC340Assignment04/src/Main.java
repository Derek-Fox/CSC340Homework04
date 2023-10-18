import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        StudentDataHandler dataHandler = new StudentDataHandler("src/students.txt", in);
        int choice = 0;
        while (choice != 6) {
            dataHandler.cleanFile();
            displayMenu();
            choice = in.nextInt();
            in.nextLine(); //consume newline
            doCommand(choice, dataHandler);
        }
        dataHandler.cleanFile(); //clean file one last time before exiting
    }

    /**
     * Display list of choices for user
     * @return the int code of selected user choice
     */
    public static void displayMenu() {
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
     * @param choice int code based on displayMenu()
     */
    public static void doCommand(int choice, StudentDataHandler dataHandler) {
        int id;
        switch (choice) {
            case 1:
                dataHandler.newStudent();
                break;
            case 2:
                System.out.println("Please enter the student's ID to find.");
                id = dataHandler.in.nextInt();
                dataHandler.in.nextLine(); //consume newline
                dataHandler.findStudent(id);
                break;
            case 3:
                System.out.println("Please enter the student's ID to update.");
                id = dataHandler.in.nextInt();
                dataHandler.in.nextLine(); //consume newline
                dataHandler.updateStudent(id);
                break;
            case 4:
                System.out.println("Please enter the student's ID to delete.");
                id = dataHandler.in.nextInt();
                dataHandler.in.nextLine(); //consume newline
                dataHandler.deleteStudent(id);
                break;
            case 5:
                dataHandler.printAllStudents();
                break;
            case 6:
                System.out.println("Goodbye!");
                break;
            default:
                System.out.println("Invalid input.");
        }
    }

    }