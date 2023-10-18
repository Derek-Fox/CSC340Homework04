import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class StudentDataHandler {

    private final String PATH_STRING;
    private final Path PATH;
    final Scanner in;

    public StudentDataHandler(String pathString, Scanner in) {
        PATH_STRING = pathString;
        PATH = Path.of(PATH_STRING);
        this.in = in;
    }


    /**
     * Create a new student and write to file.
     */
    public  void newStudent() {
        BufferedWriter w;
        try {
            w = new BufferedWriter(new FileWriter(PATH_STRING, true));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Students have an ID, name, date of birth, major, and GPA.");
        System.out.println("Please enter the student's ID.");
        int id = in.nextInt();

        while (id < 0) {
            System.out.println("ID must be positive. Please re-enter.");
            id = in.nextInt();
        }

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
     * @param id ID to search for
     */
    public void findStudent(int id) {
        if (id < 0) {
            System.out.println("ID must be positive.");
            return;
        }
        try {
            List<String> fileContent = new ArrayList<>(Files.readAllLines(PATH, StandardCharsets.UTF_8));
            for (String line : fileContent) {
                String idString = line.substring(0, line.indexOf(","));
                if (Integer.parseInt(idString) == id) {
                    String[] data = line.split(",");
                    System.out.printf("""
                            ---------------------------------------------------------------------------------------
                            ID: %s
                            Name: %s
                            Date of Birth: %s
                            Major: %s
                            GPA: %s
                            """, data[0], data[1], data[2], data[3], data[4]);
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
     *
     * @param id ID to search for
     * @return true or false
     */
    private boolean studentExists(int id) {
        if (id < 0) {
            return false;
        }
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
     * @param id ID to update
     */
    public void updateStudent(int id) {
        if (id < 0) {
            System.out.println("ID must be positive.");
            return;
        }

        try {
            List<String> fileContent = new ArrayList<>(Files.readAllLines(PATH, StandardCharsets.UTF_8));
            boolean found = false;
            boolean updated = false;

            for (int i = 0; i < fileContent.size(); i++) {
                String line = fileContent.get(i);
                String idString = line.substring(0, line.indexOf(","));
                if (Integer.parseInt(idString) == id) {
                    found = true;
                    String[] oldData = line.split(",");
                    String data = id + ",";
                    data += getStudentData();
                    String[] newData = data.split(",");

                    System.out.println("About to perform the following update:");
                    System.out.printf("""
                            ---------------------------------------------------------------------------
                            | %-14s| %-27s| %-27s|
                            ---------------------------------------------------------------------------
                            """, "Field", "Old Value", "New Value");
                    System.out.printf("| %-14s| %-27s| %-27s|\n", "Name", oldData[1], newData[1]);
                    System.out.printf("| %-14s| %-27s| %-27s|\n", "Date of Birth", oldData[2], newData[2]);
                    System.out.printf("| %-14s| %-27s| %-27s|\n", "Major", oldData[3], newData[3]);
                    System.out.printf("| %-14s| %-27s| %-27s|\n", "GPA", oldData[4], newData[4].substring(0, newData[4].length() - 1)); //remove newline
                    System.out.println("Are you sure you want to perform this update? (y/n)");
                    String choice = in.nextLine();

                    if (choice.equals("y")) {
                        updated = true;
                        fileContent.set(i, data);
                    }
                    break;
                }
            }

            if (found && updated) {
                Files.write(PATH, fileContent, StandardCharsets.UTF_8);
                System.out.printf("Student with ID %d updated.\n", id);
            } else if (found && !updated) {
                System.out.printf("Student with ID %d not updated.\n", id);
            } else {
                System.out.printf("Student with ID %d not found.\n", id);
            }
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    /**
     * Delete student by ID.
     * @param id ID to delete
     */
    public void deleteStudent(int id) {
        if (id < 0) {
            System.out.println("ID must be positive.");
            return;
        }
        try {
            List<String> fileContent = new ArrayList<>(Files.readAllLines(PATH, StandardCharsets.UTF_8));
            boolean found = false;
            boolean deleted = false;

            for (int i = 0; i < fileContent.size(); i++) {
                String line = fileContent.get(i);
                String idString = line.substring(0, line.indexOf(","));
                if (Integer.parseInt(idString) == id) {
                    found = true;
                    String[] data = line.split(",");

                    System.out.println("About to delete the following student:");
                    System.out.printf("""
                            ---------------------------------------------------------------------------------------
                            ID: %s
                            Name: %s
                            Date of Birth: %s
                            Major: %s
                            GPA: %s
                            """, data[0], data[1], data[2], data[3], data[4]);

                    System.out.println("Are you sure you want to delete this student? (y/n)");
                    String choice = in.nextLine();

                    if (choice.equals("y")) {
                        deleted = true;
                        fileContent.remove(i);
                    }
                    break;
                }
            }

            if (found && deleted) {
                Files.write(PATH, fileContent, StandardCharsets.UTF_8);
                System.out.printf("Student with ID %d deleted\n.", id);
            } else if (found && !deleted) {
                System.out.printf("Student with ID %d not deleted.\n", id);
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
    public void cleanFile() {
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
     *
     * @return String with student data
     */
    private  String getStudentData() {
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
        while (gpa < 0 || gpa > 4) {
            System.out.println("GPA must be between 0 and 4. Please re-enter.");
            gpa = in.nextDouble();
        }
        in.nextLine(); //consume newline
        data += gpa + "\n";
        return data;
    }

    /**
     * Print all students in a formatted table.
     */
    public void printAllStudents() {
        try {
            List<String> fileContents = new ArrayList<>(Files.readAllLines(PATH, StandardCharsets.UTF_8));
            System.out.println("""
                    Student Database:
                    ---------------------------------------------------------------------------------------""");
            System.out.printf("%-10s%-20s%-20s%-20s%-20s\n", "ID", "Name", "Date of Birth", "Major", "GPA");
            for (String line : fileContents) {
                String[] data = line.split(",");
                System.out.printf("%-10s%-20s%-20s%-20s%-20s\n", data[0], data[1], data[2], data[3], data[4]);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
