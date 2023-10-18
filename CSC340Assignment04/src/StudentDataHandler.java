import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * Class to handle all student data.
 */
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
    public void createStudent(int id) {
        BufferedWriter w;
        try {
            w = new BufferedWriter(new FileWriter(PATH_STRING, true));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (id < 0) {
            throw new IllegalArgumentException("ID must be positive.");
        }

        if (studentExists(id)) {
            throw new IllegalArgumentException("Student with ID " + id + " already exists.");
        }

        String data = getDataInput(id);

        try {
            w.write(data);
            w.flush();
            w.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Find student by ID and return formatted string.
     *
     * @param id ID to search for
     */
    public String findStudent(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("ID must be positive.");
        }
        try {
            List<String> fileContent = new ArrayList<>(Files.readAllLines(PATH, StandardCharsets.UTF_8));
            for (String line : fileContent) {
                String idString = line.substring(0, line.indexOf(","));
                if (Integer.parseInt(idString) == id) {
                    String[] data = line.split(",");
                    return String.format("""
                            ---------------------------------------------------------------------------------------
                            ID: %s
                            Name: %s
                            Date of Birth: %s
                            Major: %s
                            GPA: %s
                            """, data[0], data[1], data[2], data[3], data[4]);
                }
            }

            return String.format("Student with id %d not found.\n", id);
        } catch (IOException e) {
            throw new RuntimeException(e);
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
            throw new RuntimeException(e);
        }
        return found;
    }

    /**
     * Update student by ID.
     *
     * @param id ID to update
     */
    public String updateStudent(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("ID must be positive.");
        }

        try {
            List<String> fileContent = new ArrayList<>(Files.readAllLines(PATH, StandardCharsets.UTF_8));
            boolean found = false;
            boolean updated = false;

            for (int i = 0; i < fileContent.size(); i++) {
                String line = fileContent.get(i);
                String[] oldData = line.split(",");
                String idString = oldData[0];
                if (Integer.parseInt(idString) == id) {
                    found = true;

                    String newDataString = getDataInput(id);
                    String[] newData = newDataString.split(",");

                    if(confirmUpdate(oldData, newData)) {
                        updated = true;
                        fileContent.set(i, newDataString);
                    }
                    break;
                }
            }

            String result;

            if (found && updated) {
                Files.write(PATH, fileContent, StandardCharsets.UTF_8);
                result = String.format("Student with ID %d updated.\n", id);
            } else if (found) {
                result = String.format("Student with ID %d not updated.\n", id);
            } else {
                result = String.format("Student with ID %d not found.\n", id);
            }

            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Helper function to confirm update.
     * @param oldData old data to be overwritten
     * @param newData new data to overwrite with
     * @return true if user confirms update, false otherwise
     */
    private boolean confirmUpdate(String[] oldData, String[] newData) {
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
        System.out.println("---------------------------------------------------------------------------");
        System.out.println("Are you sure you want to perform this update? (y/n)");
        String choice = in.nextLine();

        return choice.equals("y");
    }

    /**
     * Delete student by ID.
     *
     * @param id ID to delete
     */
    public String deleteStudent(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("ID must be positive.");
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

                    if (confirmDelete(data)) {
                        deleted = true;
                        fileContent.remove(i);
                    }
                    break;
                }
            }

            String result;

            if (found && deleted) {
                Files.write(PATH, fileContent, StandardCharsets.UTF_8);
                result = String.format("Student with ID %d deleted\n.", id);
            } else if (found) {
                result = String.format("Student with ID %d not deleted.\n", id);
            } else {
                result = String.format("Student with ID %d not found.\n", id);
            }

            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Helper function to confirm delete.
     * @param data data to be deleted
     * @return true if user confirms delete, false otherwise
     */
    private boolean confirmDelete(String[] data) {
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

        return choice.equals("y");
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
            throw new RuntimeException(e);
        }
    }

    /**
     * Helper function to read in data for a student (assumes ID has already been read)
     *
     * @return String with student data
     */
    private String getDataInput(int id) {
        String data = id + ",";
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
    public String getAllStudents() {
        try {
            List<String> fileContents = new ArrayList<>(Files.readAllLines(PATH, StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            sb.append("""
                    Student Database:
                    ---------------------------------------------------------------------------------------
                    """);
            sb.append(String.format("%-10s%-20s%-20s%-20s%-20s\n", "ID", "Name", "Date of Birth", "Major", "GPA"));
            for (String line : fileContents) {
                String[] data = line.split(",");
                sb.append(String.format("%-10s%-20s%-20s%-20s%-20s\n", data[0], data[1], data[2], data[3], data[4]));
            }
            return sb.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
