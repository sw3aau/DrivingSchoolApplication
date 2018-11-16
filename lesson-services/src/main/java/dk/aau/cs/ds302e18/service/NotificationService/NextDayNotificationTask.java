package dk.aau.cs.ds302e18.service.NotificationService;

import dk.aau.cs.ds302e18.service.DBConnector;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.TimerTask;

//TimerTask class to send notifications to students based on lessons in the next 24 hour period.
public class NextDayNotificationTask extends TimerTask {

    @Override
    public void run() {
        TriggerNotifications();
    }

    //The function this task will run when the timer in NotificationTimer executes it's next task.
    //When triggered it finds the current date and time and queries the database for lessons in the next 24 hour period.
    //If there is such lessons, it sends notifications to the students of the lessons, based on their username.
    private void TriggerNotifications() {
        //Stores the current date into a new date for comparison later.
        Date currDate = new Date();
        //On function run nextDate is updated to the actual date based on Date();
        //1 day is then added to nextDate so that when it is used to query from the database it finds the next day's lessons.
        //86400000 is 24 hours in milliseconds.
        Date nextDate = new Date();
        nextDate.setTime(nextDate.getTime() + 86400000);
        //2 Connections is required since this function will be working with 2 ResultSets. Each ResultSet will have it's own Connection.
        Connection conn = new DBConnector().createConnectionObject();
        Connection conn2 = new DBConnector().createConnectionObject();

        try {
            //Queries the database for lessons in next 24 hour period.
            //1 Statement for each Connection.
            Statement statement = conn.createStatement();
            Statement statement2 = conn2.createStatement();
            if(statement == null || statement2 == null) {
                try {
                    conn.close();
                    conn2.close();
                    throw new Exception("Invalid statement.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //Queries the database for lesson between currDate and nextDate.
            ResultSet lessonResultSet = statement.executeQuery("SELECT `lesson_date`, `lesson_location`, `student_list` " +
                                                "FROM `lesson` WHERE `lesson_date` BETWEEN '" + reformatDate(currDate) + "' AND '" + reformatDate(nextDate) + "'");
            //If any lessons was found, runs through them and prepare to send notifications by storing the data from the database in local Strings.
            while(lessonResultSet.next()) {
                String lessonDate = lessonResultSet.getString("lesson_date");
                String lessonLocation = lessonResultSet.getString("lesson_location");
                String studentList = lessonResultSet.getString("student_list");
                String[] studentListArray = studentList.split(":");
                //Queries the database again, but for student's email and phonenumber, based on their username found from a lesson.
                for(String studentUsername : studentListArray) {
                    ResultSet usernameResultSet = statement2.executeQuery("SELECT `email`, `phonenumber` FROM `account` WHERE `username` = '" + studentUsername + "'");
                    //Call Notification to send notifications to the students, based on the data found from the database.
                    //This uses the private helper function "constructNotificationMessage" to construct the actual notification message.
                    while(usernameResultSet.next()) {
                        String email = usernameResultSet.getString("email");
                        String phonenumber = usernameResultSet.getString("phonenumber");
                        //The String before phonenumber is the region code. +45 is for Denmark.
                        Notification notification = new Notification(constructNotificationMessage(studentUsername, lessonLocation, lessonDate), "+45" + phonenumber, email);
                    }
                    usernameResultSet.close();
                }
            }
            lessonResultSet.close();
            conn.close();
            conn2.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    //Constructs a notification message based on Strings of username, lessonLocation and lessonDate.
    private String constructNotificationMessage(String username, String lessonLocation, String lessonDate) {
        String[] stringArray = lessonDate.split(" ");
        return username + " your lesson tomorrow (" + stringArray[0] + ") is at " + lessonLocation + ", at the time: " + stringArray[1] + ".";
    }
}
