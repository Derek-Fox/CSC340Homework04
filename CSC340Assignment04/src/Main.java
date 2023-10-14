import java.io.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        BufferedReader r = null;
        FileWriter w = null;
        try {
            r = readFile();
            w = writeFile();
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        int choice = displayMenu();
        doCommand(choice, r, w);
    }

    public static BufferedReader readFile() throws FileNotFoundException {
        return new BufferedReader(new FileReader("students.txt"));
    }

    public static FileWriter writeFile() throws IOException {
        return new FileWriter("students.txt");
    }

    public static int displayMenu() {
        System.out.println("""
                This is a database of imaginary students who go to The University of Collegiate Studies.
                Would you like to:
                1. Create a new student.
                2. Query for a student.
                3. Update a student's info.
                4. Delete a student.
                5. Quit.
                Please enter your choice (1-5).""");
        Scanner in = new Scanner(System.in);
        return in.nextInt();
    }

    public static void doCommand(int choice, BufferedReader r, FileWriter w) {
        switch (choice) {
            case 1:
                newStudent(w);
                break;
            case 2:
                findStudent(r);
                break;
            case 3:
                updateStudent(r, w);
                break;
            case 4:
                deleteStudent(r, w);
                break;
            case 5:
                System.out.println("Goodbye!");
                break;
        }
    }

    public static void newStudent(FileWriter w) {
        System.out.println("Students have an ID, name, date of birth, major, and GPA.");
        Scanner in = new Scanner(System.in);
        System.out.println("Please enter the student's ID.");
        String data = "";
        int id = in.nextInt();
        data += id + ",";
        System.out.println("Please enter the student's name.");
        String name = in.next();
        data += name + ",";
        System.out.println("Please enter the student's date of birth.");
        String dob = in.next();
        data += dob + ",";
        System.out.println("Please enter the student's major.");
        String major = in.next();
        data += major + ",";
        System.out.println("Please enter the student's GPA.");
        double gpa = in.nextDouble();
        data += gpa + "\n";
        try {
            w.write(data);
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    public static void findStudent(BufferedReader r) {
        System.out.println("Please enter the student's ID.");
        Scanner in = new Scanner(System.in);
        int id = in.nextInt();
        String line = "";
        try {
            while ((line = r.readLine()) != null) {
                String[] student = line.split(",");
                if (Integer.parseInt(student[0]) == id) {
                    System.out.println("ID: " + student[0]);
                    System.out.println("Name: " + student[1]);
                    System.out.println("Date of Birth: " + student[2]);
                    System.out.println("Major: " + student[3]);
                    System.out.println("GPA: " + student[4]);
                }
            }
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    public static void updateStudent(BufferedReader r, FileWriter w) {
        System.out.println("Please enter the student's ID to update.");
        Scanner in = new Scanner(System.in);
        int id = in.nextInt();
        String line = "";
        try {
            while ((line = r.readLine()) != null) {
                String[] student = line.split(",");
                if (Integer.parseInt(student[0]) == id) {
                    System.out.println("Please enter the student's new name.");
                    String name = in.next();
                    student[1] = name;
                    System.out.println("Please enter the student's new date of birth.");
                    String dob = in.next();
                    student[2] = dob;
                    System.out.println("Please enter the student's new major.");
                    String major = in.next();
                    student[3] = major;
                    System.out.println("Please enter the student's new GPA.");
                    double gpa = in.nextDouble();
                    student[4] = Double.toString(gpa);
                    String data = "";
                    for (int i = 0; i < student.length; i++) {
                        data += student[i] + ",";
                    }
                    data += "\n";
                    w.write(data);
                }
            }
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    public static void deleteStudent() {

    }
}