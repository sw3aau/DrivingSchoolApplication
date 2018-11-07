package dk.aau.cs.ds302e18.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.TimerTask;

//TimerTask class to send notifications to students based on lessons in the next 24 hour period.
public class NotificationTriggerTask extends TimerTask {

    @Override
    public void run() {
        TriggerNotifications();
    }

    //The function this task will run when the timer in NotificationTrigger executes it's next task.
    //When triggered it finds the current date and time and queries the database for lessons in the next 24 hour period.
    //If there is such lessons, it sends notifications to the students of the lessons, based on their username.
    private void TriggerNotifications() {
        //On function run nextDate is updated to the actual date based on Date();
        //1 day is then added to nextDate so that when it is used to query from the database it finds the next day's lessons.
        //86400000 is 24 hours in milliseconds.
        Date nextDate = new Date();
        nextDate.setTime(nextDate.getTime() + 86400000);
        Connection conn = new DBConnector().createConnectionObject();

        try {
            //Queries the database for lessons in next 24 hour period.
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT `lesson_date`, `lesson_location`, `student_list` " +
                                                "FROM `lesson` WHERE `lesson_date` < '" + reformatDate(nextDate) + "'");
            //If any lessons was found, runs through them and prepare to send notifications by storing the data from the database in local Strings.
            while(rs.next()) {
                String lessonDate = rs.getString("lesson_date");
                String lessonLocation = rs.getString("lesson_location");
                String studentList = rs.getString("student_list");
                String[] studentListArray = studentList.split(":");
                //Queries the database again, but for student's email and phonenumber, based on their username found from a lesson.
                for(String studentUsername : studentListArray) {
                    ResultSet rs2 = st.executeQuery("SELECT `email`, `phonenumber` FROM `account` WHERE `username` = '" + studentUsername + "`");
                    //Call Notification to send notifications to the students, based on the data found from the database.
                    //This uses the private helper function "constructNotificationMessage" to construct the actual notification message.
                    while(rs2.next()) {
                        String email = rs.getString("email");
                        String phonenumber = rs.getString("phonenumber");
                        Notification notification = new Notification(constructNotificationMessage(studentUsername, lessonLocation, lessonDate), phonenumber, email);
                    }
                }
            }
            conn.close();
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
