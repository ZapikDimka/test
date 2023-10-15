import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
/* проект неповний ще можна з відносною легкістю дописати багато функціоналу, який був би більш націлений на
* роботу з користувачем, але суть завдання була не в цьому, тому я вирішив цей нюанс опустити.
*
* хочу зазначити, що враховуючи розклад фену я не знайшов нічого крще,
*  ніж просто написати предмети в текстовий документ
* так би мовити руками для того, щоб комп'ютеру було легко сприймати дану інформацію,
*  адже мені не вдалося за той час, який я приділив на це завдання написати алекватно працюючий
*  алгоритм для зчитування дисциплін і груп для студентів напряму з наданого розкладу ФЕНу,
* тому пересів на джаву і на прикладі ІПЗ зробив працюючу систему розкладу.
* думаю, що в випадку ФЕНу "набагато дешевше" зробити так, як зробив я, або замовляти у їхнього деканату
* адекватно заповнену таблицю з розкладом
* */


class Group {
    private String name;
    private String weeks;
    private String dayOfWeek;
    private String time;
    private String classroom;

    public Group(String name, String weeks, String dayOfWeek, String time, String classroom) {
        this.name = name;
        this.weeks = weeks;
        this.dayOfWeek = dayOfWeek;
        this.time = time;
        this. classroom = classroom;
    }

    public String getName() {
        return name;
    }

    public String getWeeks() {
        return weeks;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public String getTime() {
        return time;
    }

    public String getClassroom() {
        return classroom;
    }
}

class Subject {
    private String name;
    private List<Group> groups;

    public Subject(String name) {
        this.name = name;
        this.groups = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void addGroup(Group group) {
        groups.add(group);
    }

    public List<Group> getGroups() {
        return groups;
    }
}

class Specialization {
    private String name;
    private List<Subject> subjects;

    public Specialization(String name) {
        this.name = name;
        this.subjects = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void addSubject(Subject subject) {
        subjects.add(subject);
    }

    public List<Subject> getSubjects() {
        return subjects;
    }
}

class Course {
    private String name;
    private List<Specialization> specializations;

    public Course(String name) {
        this.name = name;
        this.specializations = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void addSpecialization(Specialization specialization) {
        specializations.add(specialization);
    }

    public List<Specialization> getSpecializations() {
        return specializations;
    }
}

class Faculty {
    private String name;
    private List<Course> courses;

    public Faculty(String name) {
        this.name = name;
        this.courses = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void addCourse(Course course) {
        courses.add(course);
    }

    public List<Course> getCourses() {
        return courses;
    }
}

public class Main {
    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src//test.txt"));
            String line;
            List<Faculty> faculties = new ArrayList<>();
            Faculty currentFaculty = null;
            Course currentCourse = null;
            Specialization currentSpecialization = null;
            Subject currentSubject = null;

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    if (line.contains("Факультет")) {
                        String facultyName = line.substring("Факультет".length()).trim();
                        currentFaculty = new Faculty(facultyName);
                        faculties.add(currentFaculty);
                    } else if (line.contains("Курс")) {
                        String courseName = line.substring("Курс".length()).trim();
                        currentCourse = new Course(courseName);
                        currentFaculty.addCourse(currentCourse);
                    } else if (line.contains("Спеціальність")) {
                        String specializationName = line.substring("Спеціальність".length()).trim();
                        currentSpecialization = new Specialization(specializationName);
                        currentCourse.addSpecialization(currentSpecialization);
                    } else if (line.contains("Предмет")) {
                        String subjectName = line.substring("Предмет".length()).trim();
                        currentSubject = new Subject(subjectName);
                        currentSpecialization.addSubject(currentSubject);
                    } else if (line.contains("Група") && currentSubject != null) {
                        String[] groupDetails = line.split("[,;()]");
                        String name = groupDetails[0].trim();
                        String weeks = groupDetails[1].trim();
                        String dayOfWeek = groupDetails[2].trim();
                        String time = groupDetails[3].trim();
                        String classroom = groupDetails[4].trim();
                        Group group = new Group(name, weeks, dayOfWeek, time, classroom);
                        currentSubject.addGroup(group);
                    }
                }
            }
            reader.close();

            // Поступовий опитуальник
            Scanner scanner = new Scanner(System.in);

            // Зчитування списку груп, до яких користувач вже записаний
            List<String> enrolledGroups = new ArrayList<>();
            try (BufferedReader enrolledGroupsReader = new BufferedReader(new FileReader("src//groups.txt"))) {
                String enrolledGroupsLine;
                while ((enrolledGroupsLine = enrolledGroupsReader.readLine()) != null) {
                    enrolledGroups.add(enrolledGroupsLine);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            boolean isRunning = true;
            while (isRunning) {
                // Вибір факультету
                System.out.println("Доступні факультети:");
                for (int i = 0; i < faculties.size(); i++) {
                    System.out.println((i + 1) + ". " + faculties.get(i).getName());
                }
                System.out.print("Виберіть факультет (введіть номер): ");
                int facultyNumber = scanner.nextInt();
                scanner.nextLine();  // Очистка буфера

                if (facultyNumber > 0 && facultyNumber <= faculties.size()) {
                    Faculty selectedFaculty = faculties.get(facultyNumber - 1);

                    // Вибір курсу
                    List<Course> courses = selectedFaculty.getCourses();
                    System.out.println("Доступні курси:");
                    for (int i = 0; i < courses.size(); i++) {
                        System.out.println((i + 1) + ". " + courses.get(i).getName());
                    }
                    System.out.print("Виберіть курс (введіть номер): ");
                    int courseNumber = scanner.nextInt();
                    scanner.nextLine();  // Очистка буфера

                    if (courseNumber > 0 && courseNumber <= courses.size()) {
                        Course selectedCourse = courses.get(courseNumber - 1);

                        // Вибір спеціальності
                        List<Specialization> specializations = selectedCourse.getSpecializations();
                        System.out.println("Доступні спеціальності:");
                        for (int i = 0; i < specializations.size(); i++) {
                            System.out.println((i + 1) + ". " + specializations.get(i).getName());
                        }
                        System.out.print("Виберіть спеціальність (введіть номер): ");
                        int specializationNumber = scanner.nextInt();
                        scanner.nextLine();  // Очистка буфера

                        if (specializationNumber > 0 && specializationNumber <= specializations.size()) {
                            Specialization selectedSpecialization = specializations.get(specializationNumber - 1);

                            // Вибір предмету
                            List<Subject> subjects = selectedSpecialization.getSubjects();
                            System.out.println("Доступні предмети:");
                            for (int i = 0; i < subjects.size(); i++) {
                                System.out.println((i + 1) + ". " + subjects.get(i).getName());
                            }
                            System.out.print("Виберіть предмет (введіть номер): ");
                            int subjectNumber = scanner.nextInt();
                            scanner.nextLine();  // Очистка буфера

                            if (subjectNumber > 0 && subjectNumber <= subjects.size()) {
                                Subject selectedSubject = subjects.get(subjectNumber - 1);

                                // Вибір групи
                                List<Group> groups = selectedSubject.getGroups();
                                System.out.println("Доступні групи:");
                                for (int i = 0; i < groups.size(); i++) {
                                    Group group = groups.get(i);
                                    System.out.println((i + 1) + ". " + group.getName());
                                }
                                System.out.print("Виберіть групу (введіть номер): ");
                                int groupNumber = scanner.nextInt();
                                scanner.nextLine();  // Очистка буфера

                                if (groupNumber > 0 && groupNumber <= groups.size()) {
                                    Group selectedGroup = groups.get(groupNumber - 1);

                                    String groupInfo = "Предмет: " + selectedSubject.getName() +
                                            ", Група: " + selectedGroup.getName() +
                                            ", Тижні: " + selectedGroup.getWeeks() +
                                            ", День: " + selectedGroup.getDayOfWeek() +
                                            ", Час: " + selectedGroup.getTime() +
                                            ", Аудиторія: " + selectedGroup.getClassroom();

                                    if (!enrolledGroups.contains(groupInfo)) {
                                        // Додаємо групу до списку записаних груп
                                        enrolledGroups.add(groupInfo);
                                        System.out.println("Ви були успішно записані в групу.");
                                    } else {
                                        // Видаляємо групу зі списку записаних груп (виписуємося)
                                        enrolledGroups.remove(groupInfo);
                                        System.out.println("Вас виписано з групи.");
                                    }

                                    // Виведення всіх груп, у які записаний користувач
                                    printEnrolledGroups(enrolledGroups);
                                } else {
                                    System.out.println("Помилка: вибір групи не є дійсним.");
                                }
                            } else {
                                System.out.println("Помилка: вибір предмету не є дійсним.");
                            }
                        } else {
                            System.out.println("Помилка: вибір спеціальності не є дійсним.");
                        }
                    } else {
                        System.out.println("Помилка: вибір курсу не є дійсним.");
                    }
                } else {
                    System.out.println("Помилка: вибір факультету не є дійсним.");
                }

                System.out.print("Бажаєте продовжити (Так/Ні)? ");
                String continueChoice = scanner.nextLine().trim();
                if (!continueChoice.equalsIgnoreCase("Так")) {
                    isRunning = false;
                }
            }

            // Запис списку груп користувача у файл
            try (BufferedWriter enrolledGroupsWriter = new BufferedWriter(new FileWriter("src//groups.txt"))) {
                for (String group : enrolledGroups) {
                    enrolledGroupsWriter.write(group);
                    enrolledGroupsWriter.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printEnrolledGroups(List<String> enrolledGroups) {
        if (enrolledGroups.isEmpty()) {
            System.out.println("Ви ще не записані в жодну групу.");
        } else {
            System.out.println("Ви записані в наступні групи:");
            for (String group : enrolledGroups) {
                System.out.println(group);
            }
        }
    }
}
