import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Main {
    static final String PATH_STRING = "./src/students.txt";
    static final Path PATH = Path.of(PATH_STRING);
    static final Scanner in = new Scanner(System.in);

    public static void main(String[] args) {
        int choice = 0;
        while (choice != 5) {
            cleanFile();
            choice = displayMenu();
            doCommand(choice);
        }

    }

    /**
     * Display list of choices for user
     * @return the int code of selected user choice
     */
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

    /**
     * Performs appropriate command based on choice
     * @param choice int code based on displayMenu()
     */
    public static void doCommand(int choice) {
        int id;
        switch (choice) {
            case 1:
                newStudent();
                break;
            case 2:
                System.out.println("Please enter the student's ID to find.");
                id = in.nextInt();
                in.nextLine(); //consume newline
                findStudent(id);
                break;
            case 3:
                System.out.println("Please enter the student's ID to update.");
                id = in.nextInt();
                in.nextLine(); //consume newline
                updateStudent(id);
                break;
            case 4:
                System.out.println("Please enter the student's ID to delete.");
                id = in.nextInt();
                in.nextLine(); //consume newline
                deleteStudent(id);
                break;
            case 5:
                System.out.println("Goodbye!");
                break;
            default:
                System.out.println("Invalid input");
        }
    }

    /**
     * Create a new student and write to file.
     */
    public static void newStudent() {
        BufferedWriter w;
        try {
            w = new BufferedWriter(new FileWriter(PATH_STRING, true));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Students have an ID, name, date of birth, major, and GPA.");
        System.out.println("Please enter the student's ID.");
        int id = in.nextInt();

        while (studentExists(id)) {
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

    /**
     * Find student by ID and print out the data
     */
    public static void findStudent(int id) {
        try {
            List<String> fileContent = new ArrayList<>(Files.readAllLines(PATH, StandardCharsets.UTF_8));
            for (String line : fileContent) {
                String idString = line.substring(0, line.indexOf(","));
                if (Integer.parseInt(idString) == id) {
                    System.out.println("Student data: " + line);
                    return;
                }
            }

            System.out.printf("Student with id %d not found.\n", id);
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    /**
     * Helper function to see if student with ID exists.
     * @param id ID to search for
     * @return true or false
     */
    private static boolean studentExists(int id) {
        boolean found = false;
        try {
            List<String> fileContent = new ArrayList<>(Files.readAllLines(PATH, StandardCharsets.UTF_8));
            for (String line : fileContent) {
                String idString = line.substring(0, line.indexOf(","));
                if (Integer.parseInt(idString) == id) {
                    found = true;
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        return found;
    }

    /**
     * Update student by ID.
     */
    public static void updateStudent(int id) {
        try {
            String data = id + ",";
            data += getStudentData();

            List<String> fileContent = new ArrayList<>(Files.readAllLines(PATH, StandardCharsets.UTF_8));
            boolean found = false;
            for (int i = 0; i < fileContent.size(); i++) {
                String line = fileContent.get(i);
                String idString = line.substring(0, line.indexOf(","));
                if (Integer.parseInt(idString) == id) {
                    found = true;
                    fileContent.set(i, data);
                    break;
                }
            }

            if(found) {
                Files.write(PATH, fileContent, StandardCharsets.UTF_8);
                System.out.printf("Student with ID %d updated.", id);
            } else {
                System.out.printf("Student with ID %d not found.\n", id);
            }
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    /**
     * Delete student by ID.
     */
    public static void deleteStudent(int id) {
        try {
            List<String> fileContent = new ArrayList<>(Files.readAllLines(PATH, StandardCharsets.UTF_8));
            boolean found = false;
            for (int i = 0; i < fileContent.size(); i++) {
                String line = fileContent.get(i);
                String idString = line.substring(0, line.indexOf(","));
                if (Integer.parseInt(idString) == id) {
                    found = true;
                    fileContent.remove(i);
                    break;
                }
            }
            if(found) {
                Files.write(PATH, fileContent, StandardCharsets.UTF_8);
                System.out.printf("Student with ID %d deleted\n.", id);
            } else {
                System.out.printf("Student with ID %d not found.\n", id);
            }
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    /**
     * Remove all empty lines from the file, and sort by ID.
     */
    public static void cleanFile() {
        try {
            List<String> fileContent = new ArrayList<>(Files.readAllLines(PATH, StandardCharsets.UTF_8));
            for (int i = 0; i < fileContent.size(); i++) {
                if (fileContent.get(i).isEmpty()) {
                    fileContent.remove(i);
                    break;
                }
            }

            fileContent.sort(Comparator.comparing(s -> Integer.parseInt(s.substring(0, s.indexOf(","))))); //sort by ID
            Files.write(PATH, fileContent, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    /**
     * Helper function to read in data for a student (assumes ID has already been read)
     * @return String with student data
     */
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