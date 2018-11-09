package dk.aau.cs.ds302e18.service;

import dk.aau.cs.ds302e18.service.DBConnector;
import dk.aau.cs.ds302e18.service.Student;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;


public class CourseController {
    private Connection conn;

    public CourseController() {
        this.conn = new DBConnector().createConnectionObject();
    }

    public String saveStudentnamesAsString(ArrayList<String> studentList) {
        String combinedString = "";
        for (String student : studentList) {
            combinedString += student + ":";
        }
        return combinedString;
    }

    /* Separates a string into usernames and creates an list of users with those usernames. */
    /*
    public ArrayList<Student> saveStringAsStudents(String studentStudentnames) {
        ArrayList<Student> studentList = new ArrayList<>();
        String[] parts = studentStudentnames.split(":");
        for (int i = 0; i < parts.length; i++) {
            studentList.add(new Student());
            studentList.get(i).setUsername(parts[i]);
        }
        return studentList;
    }
    */


    public void createEmptyCourse(int courseID) {
        try {
            /* Creates a mysql database connection */
            Statement st = conn.createStatement();
            st.executeUpdate("INSERT INTO `course`(`course_id`, `student_usernames`) VALUES (" + courseID + ",'')");

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* Takes an list of student users that will initially be assigned to the course as input */
    public void createEmptyCourse(int courseID, ArrayList<String> studentList) {
        try {
            Statement st = conn.createStatement();
            String usernamesAsString = saveStudentnamesAsString(studentList);
            st.executeUpdate("INSERT INTO `course`(`course_id`, `student_usernames`) VALUES (" + courseID + ",'" + usernamesAsString + "')");
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void addStudent(int courseID, String studentUsername) {
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT `student_usernames` FROM `course` WHERE `course_id` = " + courseID + "");
            rs.next();
            /* Finds the current list of students */
            String usernames = rs.getString("student_usernames");
            /* Adds the new student */
            usernames += studentUsername + ":";
            st.executeUpdate("UPDATE `course` SET`student_usernames`='" + usernames + "' WHERE `course_id` = '" + courseID + "'");
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeStudent(int courseID, String studentUsername) {
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT `student_usernames` FROM `course` WHERE `course_id` = " + courseID + "");
            rs.next();
            /* Finds the current list of students */
            String usernames = rs.getString("student_usernames");
            /* Removes the targeted student */
            String usernamesWithoutStudent = usernames.replace(studentUsername + ":", "");
            st.executeUpdate("UPDATE `course` SET`student_usernames`='" + usernamesWithoutStudent + "' WHERE `course_id` = '" + courseID + "'");
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteCourse(int courseID) {
        try {
            Statement st = conn.createStatement();
            st.executeUpdate("DELETE FROM `course` WHERE `course_id` = '" + courseID + "'");
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* Get list of students from course database */
    /* Prepare lesson date, lesson ID, instructor, location, type. DONE */
    /* needs to sort lessons after ID, and pick the ID incremented by one. DONE */
    /* Still needs number of hours at a time and start date should be the exact time the first lesson starts */
    /* every lesson needs an associated courseID if there are any */
    public void courseAddLessons(Date startDate, ArrayList<Integer> lessonPlacementsFromOffset, int numberLessons, int numberLessonsADay,
                                 ArrayList<String> studentList, int courseID,
                                 String location, String instructorName, int lessonType) {

        String usernamesString = saveStudentnamesAsString(studentList);
        ArrayList<Date> lessonDates = createLessonDates(startDate, lessonPlacementsFromOffset, numberLessons, numberLessonsADay);
        /* All added lessons will be initialized as active */
        int lessonState = 1;
        int lastEnteredLessonID;
        try {
            Statement st = conn.createStatement();
            /* Gets the ID of the last lesson created, which will be the first ID of the created course lessons */
            ResultSet rs = st.executeQuery("SELECT `lesson_id` FROM `lesson`");
            rs.last();
            lastEnteredLessonID = rs.getInt("lesson_id");

            for(int j = 0; j<lessonDates.size(); j++) {
                int lessonID = lastEnteredLessonID + 1 + j;
                String lessonDate = reformatDate(lessonDates.get(j));
                st.executeUpdate(   "INSERT INTO `lesson`(`lesson_id`, `state`, `lesson_date`, `instructor`, " +
                        "`lesson_location`, `lesson_type`, `student_list`, `course_id`) " + "VALUES (" + lessonID + ","+ lessonState +",'"+ lessonDate +"','"+ instructorName +"','"+
                        location + "',"+ lessonType +",'"+ usernamesString +"',"+ courseID +")");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* Creates an ArrayList of dates with the dates a course should contain. The dates are set to start at startDate
       and lessons will be distributed in the specified weekdays. Sunday is 0, monday is 1 and so on.
       */

    public ArrayList<Date> createLessonDates(Date startDate, ArrayList<Integer> weekdays, int numberLessonsToDistribute,
                                             int numberLessonsADay) {
        ArrayList<Date> lessonDates = new ArrayList<>();
        Date currentDayDate = startDate;
        int dayCount = 0;
        /* A lesson should minimum be 45 minutes according to law, and the two interviewed driving schools had a 45
           minute lesson duration. */
        int lessonDurationMinutes = 45;

        int oneMinuteInMilliseconds = 60000;
        int lessonDurationMilliseconds = oneMinuteInMilliseconds * lessonDurationMinutes;

        /* While all lessons has not yet been distributed */
        while (numberLessonsToDistribute > 0) {
            /* 86400000 * dayCount is not contained in a variable due to the possible of reaching an overflow of most
               data-types. Date is by default suitable to handle very large numbers. */
            currentDayDate = new Date(startDate.getTime() + 86400000 * dayCount);
            if(weekdays.contains(currentDayDate.getDay())) {
                /* If it is one of the weekdays specified in the weekdays array, add an number of lessons equal to
                 * number lessons a day. Also since it adds a several lessons in a loop it needs to check before each lesson
                 * is added if the necessary lessons have been distributed. */
                for(int g = 0; g<numberLessonsADay; g++){
                    if(numberLessonsToDistribute > 0) {
                        System.out.println(g);
                        lessonDates.add(new Date(currentDayDate.getTime() + lessonDurationMilliseconds * g));
                        numberLessonsToDistribute--;
                    }
                }
            }
            //System.out.println(dayCount);
            dayCount += 1;
        }
        return lessonDates;
    }

    //Reformats the Java Date format to the mySQL datetime format and returns it in a String.
    //This functions works based on the Java Date format for generating a Date.
    //This means that this function counterworks the addition of +1900 in allocating a new Date.
    //Therefor any pre-formatted dates won't work with this function.
    private String reformatDate(Date date) {
        if(date == null) {
            try {
                throw new Exception("Invalid date.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String formattetDate = (date.getYear() + 1900) + "-" + (date.getMonth() + 1) + "-" + date.getDate()
                + " " + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds();
        return formattetDate;
    }
}
