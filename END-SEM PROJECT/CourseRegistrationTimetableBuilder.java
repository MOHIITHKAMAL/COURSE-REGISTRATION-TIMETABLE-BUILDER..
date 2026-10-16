import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

// ============================================================
// COURSE REGISTRATION & TIMETABLE BUILDER
// ============================================================

public class CourseRegistrationTimetableBuilder {

    // --------------------------------------------------------
    // MODULE 4: CLASS AND OBJECT - Course
    // --------------------------------------------------------
    static class Course {
        private String code;
        private String name;
        private int credits;
        private String timeSlot;
        private String faculty;
        private String prerequisite;

        // Parameterized constructor
        public Course(String code, String name, int credits,
                      String timeSlot, String faculty, String prerequisite) {
            this.code = code;
            this.name = name;
            this.credits = credits;
            this.timeSlot = timeSlot;
            this.faculty = faculty;
            this.prerequisite = prerequisite;
        }

        // Getters
        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        public int getCredits() {
            return credits;
        }

        public String getTimeSlot() {
            return timeSlot;
        }

        public String getFaculty() {
            return faculty;
        }

        public String getPrerequisite() {
            return prerequisite;
        }

        @Override
        public String toString() {
            return code + " - " + name +
                   " | Credits: " + credits +
                   " | Time: " + timeSlot +
                   " | Faculty: " + faculty;
        }
    }

    // --------------------------------------------------------
    // MODULE 4: CLASS AND OBJECT - Student
    // --------------------------------------------------------
    static class Student {
        private int studentId;
        private String name;
        private int maxCredits;

        private ArrayList<Course> registeredCourses;

        public Student(int studentId, String name, int maxCredits) {
            this.studentId = studentId;
            this.name = name;
            this.maxCredits = maxCredits;
            this.registeredCourses = new ArrayList<>();
        }

        public int getStudentId() {
            return studentId;
        }

        public String getName() {
            return name;
        }

        public int getMaxCredits() {
            return maxCredits;
        }

        public ArrayList<Course> getRegisteredCourses() {
            return registeredCourses;
        }

        public int getTotalCredits() {
            int total = 0;

            // MODULE 3: Enhanced for loop
            for (Course c : registeredCourses) {
                total += c.getCredits();
            }

            return total;
        }

        public void addCourse(Course course) {
            registeredCourses.add(course);
        }
    }

    // --------------------------------------------------------
    // MODULE 1: METHOD - Display available courses
    // --------------------------------------------------------
    public static void displayCourses(List<Course> courses) {

        System.out.println("\n========== AVAILABLE COURSES ==========");

        for (Course course : courses) {
            System.out.println(course);
        }
    }

    // --------------------------------------------------------
    // MODULE 3: SEARCHING
    // --------------------------------------------------------
    public static Course searchCourse(List<Course> courses, String code) {

        for (Course course : courses) {

            if (course.getCode().equalsIgnoreCase(code)) {
                return course;
            }
        }

        return null;
    }

    // --------------------------------------------------------
    // MODULE 2: CONDITIONAL STATEMENTS
    // Check prerequisite
    // --------------------------------------------------------
    public static boolean checkPrerequisite(Student student, Course course) {

        String prerequisite = course.getPrerequisite();

        // No prerequisite
        if (prerequisite.equalsIgnoreCase("NONE")) {
            return true;
        }

        // Check completed courses
        for (Course registered : student.getRegisteredCourses()) {

            if (registered.getCode().equalsIgnoreCase(prerequisite)) {
                return true;
            }
        }

        return false;
    }

    // --------------------------------------------------------
    // MODULE 2 + MODULE 3
    // Check timetable conflict
    // --------------------------------------------------------
    public static boolean hasTimeConflict(Student student, Course newCourse) {

        for (Course registered : student.getRegisteredCourses()) {

            if (registered.getTimeSlot()
                    .equalsIgnoreCase(newCourse.getTimeSlot())) {

                return true;
            }
        }

        return false;
    }

    // --------------------------------------------------------
    // MODULE 4: EXCEPTION HANDLING
    // Register course
    // --------------------------------------------------------
    public static void registerCourse(Student student, Course course)
            throws Exception {

        // Check duplicate registration
        for (Course registered : student.getRegisteredCourses()) {

            if (registered.getCode().equalsIgnoreCase(course.getCode())) {
                throw new Exception("Course already registered!");
            }
        }

        // Check credit limit
        if (student.getTotalCredits() + course.getCredits()
                > student.getMaxCredits()) {

            throw new Exception("Credit limit exceeded!");
        }

        // Check prerequisite
        if (!checkPrerequisite(student, course)) {

            throw new Exception(
                    "Prerequisite not completed: "
                    + course.getPrerequisite()
            );
        }

        // Check timetable conflict
        if (hasTimeConflict(student, course)) {

            throw new Exception(
                    "Timetable conflict at "
                    + course.getTimeSlot()
            );
        }

        student.addCourse(course);

        System.out.println(
                "SUCCESS: " + course.getCode()
                + " registered successfully."
        );
    }

    // --------------------------------------------------------
    // MODULE 5: SORTING
    // Sort courses by credits
    // --------------------------------------------------------
    public static void sortCoursesByCredits(List<Course> courses) {

        courses.sort(
            Comparator.comparingInt(Course::getCredits)
        );
    }

    // --------------------------------------------------------
    // MODULE 5: STRING PROCESSING
    // --------------------------------------------------------
    public static void parseCourseCode(String code) {

        String[] parts = code.split("-");

        System.out.println("\nCourse Code Parts:");

        for (String part : parts) {
            System.out.println(part);
        }
    }

    // --------------------------------------------------------
    // MODULE 6: LAMBDA + STREAM
    // Display courses having 4 credits
    // --------------------------------------------------------
    public static void displayFourCreditCourses(
            List<Course> courses) {

        System.out.println("\n========== 4 CREDIT COURSES ==========");

        courses.stream()
                .filter(c -> c.getCredits() == 4)
                .forEach(System.out::println);
    }

    // --------------------------------------------------------
    // MODULE 6: STREAM + COLLECTORS
    // Group courses by faculty
    // --------------------------------------------------------
    public static void displayFacultyReport(
            List<Course> courses) {

        Map<String, Long> facultyCount =
                courses.stream()
                        .collect(
                            Collectors.groupingBy(
                                Course::getFaculty,
                                Collectors.counting()
                            )
                        );

        System.out.println("\n========== FACULTY REPORT ==========");

        facultyCount.forEach(
                (faculty, count) ->
                        System.out.println(
                                faculty + " : " + count + " courses"
                        )
        );
    }

    // --------------------------------------------------------
    // MODULE 5: FILE I/O
    // Save timetable to file
    // --------------------------------------------------------
    public static void saveTimetableToFile(Student student) {

        Path path = Paths.get("timetable.txt");

        try (BufferedWriter writer =
                     Files.newBufferedWriter(path)) {

            writer.write("COURSE TIMETABLE");
            writer.newLine();
            writer.write("========================");
            writer.newLine();

            for (Course course : student.getRegisteredCourses()) {

                writer.write(
                        course.getCode() + " | "
                        + course.getName() + " | "
                        + course.getTimeSlot() + " | "
                        + course.getFaculty()
                );

                writer.newLine();
            }

            writer.write(
                    "Total Credits: "
                    + student.getTotalCredits()
            );

            System.out.println(
                    "\nTimetable saved to timetable.txt"
            );

        } catch (IOException e) {

            System.out.println(
                    "File error: " + e.getMessage()
            );
        }
    }

    // --------------------------------------------------------
    // Display student's timetable
    // --------------------------------------------------------
    public static void displayTimetable(Student student) {

        System.out.println("\n========== STUDENT TIMETABLE ==========");

        if (student.getRegisteredCourses().isEmpty()) {

            System.out.println("No courses registered.");

            return;
        }

        for (Course course : student.getRegisteredCourses()) {

            System.out.println(
                    course.getTimeSlot()
                    + " -> "
                    + course.getCode()
                    + " - "
                    + course.getName()
                    + " | "
                    + course.getFaculty()
            );
        }

        System.out.println(
                "\nTotal Credits: "
                + student.getTotalCredits()
        );
    }

    // --------------------------------------------------------
    // MODULE 3: RECURSION
    // Calculate sum recursively
    // --------------------------------------------------------
    public static int recursiveSum(int n) {

        if (n == 0) {
            return 0;
        }

        return n + recursiveSum(n - 1);
    }

    // --------------------------------------------------------
    // MAIN METHOD
    // --------------------------------------------------------
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // MODULE 1: INPUT
        System.out.println("==============================================");
        System.out.println(" COURSE REGISTRATION & TIMETABLE BUILDER");
        System.out.println("==============================================");

        System.out.print("Enter Student ID: ");
        int id = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter Student Name: ");
        String name = sc.nextLine();

        System.out.print("Enter Maximum Credits Allowed: ");
        int maxCredits = sc.nextInt();

        Student student =
                new Student(id, name, maxCredits);

        // ----------------------------------------------------
        // COURSE DATA
        // ----------------------------------------------------

        List<Course> courses = new ArrayList<>();

        courses.add(
                new Course(
                        "CS101",
                        "Programming Fundamentals",
                        4,
                        "MON-09:00",
                        "Dr. Rao",
                        "NONE"
                )
        );

        courses.add(
                new Course(
                        "CS102",
                        "Data Structures",
                        4,
                        "TUE-10:00",
                        "Dr. Kumar",
                        "CS101"
                )
        );

        courses.add(
                new Course(
                        "CS103",
                        "Database Systems",
                        3,
                        "WED-09:00",
                        "Dr. Sharma",
                        "NONE"
                )
        );

        courses.add(
                new Course(
                        "CS104",
                        "Computer Networks",
                        3,
                        "TUE-10:00",
                        "Dr. Rao",
                        "CS102"
                )
        );

        courses.add(
                new Course(
                        "CS105",
                        "Java Programming",
                        4,
                        "THU-11:00",
                        "Dr. Kumar",
                        "CS101"
                )
        );

        // ----------------------------------------------------
        // DISPLAY COURSES
        // ----------------------------------------------------

        displayCourses(courses);

        // ----------------------------------------------------
        // REGISTER COURSES
        // ----------------------------------------------------

        System.out.print(
                "\nEnter number of courses to register: "
        );

        int n = sc.nextInt();
        sc.nextLine();

        for (int i = 0; i < n; i++) {

            System.out.print(
                    "Enter course code: "
            );

            String code = sc.nextLine();

            Course selected =
                    searchCourse(courses, code);

            try {

                if (selected == null) {

                    throw new Exception(
                            "Course not found!"
                    );
                }

                registerCourse(student, selected);

            } catch (Exception e) {

                System.out.println(
                        "Registration Failed: "
                        + e.getMessage()
                );
            }
        }

        // ----------------------------------------------------
        // DISPLAY TIMETABLE
        // ----------------------------------------------------

        displayTimetable(student);

        // ----------------------------------------------------
        // MODULE 5: SORTING
        // ----------------------------------------------------

        sortCoursesByCredits(courses);

        System.out.println(
                "\n========== COURSES SORTED BY CREDITS =========="
        );

        for (Course c : courses) {
            System.out.println(c);
        }

        // ----------------------------------------------------
        // MODULE 5: STRING
        // ----------------------------------------------------

        parseCourseCode("CS-101");

        // ----------------------------------------------------
        // MODULE 6: FILTERING WITH STREAM
        // ----------------------------------------------------

        displayFourCreditCourses(courses);

        // ----------------------------------------------------
        // MODULE 6: GROUPING
        // ----------------------------------------------------

        displayFacultyReport(courses);

        // ----------------------------------------------------
        // MODULE 5: FILE HANDLING
        // ----------------------------------------------------

        saveTimetableToFile(student);

        // ----------------------------------------------------
        // MODULE 3: RECURSION DEMO
        // ----------------------------------------------------

        System.out.println(
                "\nRecursive Sum of 1 to 5: "
                + recursiveSum(5)
        );

        // ----------------------------------------------------
        // FINAL MESSAGE
        // ----------------------------------------------------

        System.out.println(
                "\n=============================================="
        );

        System.out.println(
                "Registration process completed!"
        );

        System.out.println(
                "=============================================="
        );

        sc.close();
    }
}