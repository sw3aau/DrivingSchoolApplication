package dk.aau.cs.ds302e18.service;

public class CourseModel {
    private long courseID;
    private String studentUsernames;

    public long getCourseID() {
        return courseID;
    }

    public void setCourseID(long courseID) {
        this.courseID = courseID;
    }

    public String getStudentUsernames() {
        return studentUsernames;
    }

    public void setStudentUsernames(String studentUsernames) {
        this.studentUsernames = studentUsernames;
    }


    public Course translateModelToCourse(){
        Course course = new Course();
        course.setId(this.courseID);
        course.setStudentUsernames(this.studentUsernames);
        return course;
    }
}
