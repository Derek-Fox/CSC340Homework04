import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    static final String FILE_PATH = "./src/students.txt";
    static Scanner in = new Scanner(System.in);

    public static void main(String[] args) {
        int choice = 0;
        while (choice != 5) {
            removeAllEmptyLines();
            choice = displayMenu();
            doCommand(choice);
        }

    }

    public static int displayMenu() {
        System.out.println("""
                ---------------------------------------------------------------------------------------
                This is a database of imaginary students who go to The University of Collegiate Studies.
                Would you like to:
                1. Create a new student.
                2. Query for a student.
                3. Update a student's info.
                4. Delete a student.
                5. Quit.
                Please enter your choice (1-5).""");
        return in.nextInt();
    }

    public static void doCommand(int choice) {
        switch (choice) {
            case 1:
                newStudent();
                break;
            case 2:
                findStudent();
                break;
            case 3:
                updateStudent();
                break;
            case 4:
                deleteStudent();
                break;
            case 5:
                System.out.println("Goodbye!");
                break;
            default:
                System.out.println("Invalid input");
        }
    }

    public static void newStudent() {
        BufferedWriter w;
        try {
            w = new BufferedWriter(new FileWriter(FILE_PATH, true));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Students have an ID, name, date of birth, major, and GPA.");
        System.out.println("Please enter the student's ID.");
        int id = in.nextInt();

        while (findStudent(id)) {
            System.out.println("Student with id " + id + " already exists. Please re-enter.");
            id = in.nextInt();
        }
        in.nextLine(); //consume newline

        String data = id + ",";
        data += getStudentData();
        try {
            w.write(data);
            w.flush();
            w.close();
            System.out.println("New student created!");
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    public static void findStudent() {
        BufferedReader r = null;
        try {
            r = new BufferedReader(new FileReader(FILE_PATH));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Please enter the student's ID.");
        int id = in.nextInt();

        try {
            String line = r.readLine();
            while (line != null && !line.isEmpty()) {
                String[] student = line.split(",");
                if (Integer.parseInt(student[0]) == id) {
                    System.out.println("ID: " + student[0]);
                    System.out.println("Name: " + student[1]);
                    System.out.println("Date of Birth: " + student[2]);
                    System.out.println("Major: " + student[3]);
                    System.out.println("GPA: " + student[4]);
                }
                line = r.readLine();
            }
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    private static boolean findStudent(int id) {
        BufferedReader r = null;
        try {
            r = new BufferedReader(new FileReader(FILE_PATH));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        try {
            String line = r.readLine();
            while (line != null && !line.isEmpty()) {
                String[] student = line.split(",");
                if (Integer.parseInt(student[0]) == id) {
                    return true;
                }
                line = r.readLine();
            }
            r.close();
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        return false;
    }

    public static void updateStudent() {
        BufferedReader r = null;
        try {
            r = new BufferedReader(new FileReader(FILE_PATH));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Please enter the student's ID to update.");
        int id = in.nextInt();
        in.nextLine(); //consume newline

        try {
            String line = r.readLine();

            while (line != null && !line.isEmpty()) {
                String idString = line.substring(0, line.indexOf(","));
                if (Integer.parseInt(idString) == id) {
                    String data = id + ",";
                    data += getStudentData();

                    Path path = Path.of(FILE_PATH);
                    List<String> fileContent = new ArrayList<>(Files.readAllLines(path, StandardCharsets.UTF_8));
                    for (int i = 0; i < fileContent.size(); i++) {
                        if (fileContent.get(i).equals(line)) {
                            fileContent.set(i, data);
                            break;
                        }
                    }

                    Files.write(path, fileContent, StandardCharsets.UTF_8);
                    return;
                }
                line = r.readLine();
            }
            System.out.println("Student with id " + id + " not found.");
            r.close();
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    public static void deleteStudent() {
        System.out.println("Please enter the student's ID to delete.");
        int id = in.nextInt();
        in.nextLine(); //consume newline

        try {
            Path path = Path.of(FILE_PATH);
            List<String> fileContent = new ArrayList<>(Files.readAllLines(path, StandardCharsets.UTF_8));
            boolean found = false;
            for (int i = 0; i < fileContent.size(); i++) {
                String line = fileContent.get(i);
                if (Integer.parseInt(line.substring(0, line.indexOf(","))) == id) {
                    found = true;
                    fileContent.remove(i);
                    break;
                }
            }
            Files.write(path, fileContent, StandardCharsets.UTF_8);
            System.out.println(found ? "Student deleted." : "Student with ID " + id + " not found.");
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    public static void removeAllEmptyLines() {
        try {
            Path path = Path.of(FILE_PATH);
            List<String> fileContent = new ArrayList<>(Files.readAllLines(path, StandardCharsets.UTF_8));
            for (int i = 0; i < fileContent.size(); i++) {
                if (fileContent.get(i).isEmpty()) {
                    fileContent.remove(i);
                    break;
                }
            }

            Files.write(path, fileContent, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    public static String getStudentData() {
        String data = "";
        System.out.println("Please enter the student's name.");
        String name = in.nextLine();
        data += name + ",";
        System.out.println("Please enter the student's date of birth.");
        String dob = in.nextLine();
        data += dob + ",";
        System.out.println("Please enter the student's major.");
        String major = in.nextLine();
        data += major + ",";
        System.out.println("Please enter the student's GPA.");
        double gpa = in.nextDouble();
        data += gpa + "\n";
        return data;
    }
}