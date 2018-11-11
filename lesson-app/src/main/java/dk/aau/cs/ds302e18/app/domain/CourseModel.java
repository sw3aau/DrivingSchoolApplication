package dk.aau.cs.ds302e18.app.domain;

public class CourseModel {
    private Integer courseID;
    private String studentUsernames;

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
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
        course.setCourseID(this.courseID);
        course.setStudentUsernames(this.studentUsernames);
        return course;
    }
}
