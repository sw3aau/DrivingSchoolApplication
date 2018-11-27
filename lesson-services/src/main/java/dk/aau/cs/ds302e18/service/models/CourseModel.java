package dk.aau.cs.ds302e18.service.models;

public class CourseModel {

    private String studentUsernames;
    private String instructorUsername;
    private String studentToDelete;
    private long courseTableID;
    private CourseType courseType;

    public String getInstructorUsername() {
        return instructorUsername;
    }

    public void setInstructorUsername(String instructorUsername) {
        this.instructorUsername = instructorUsername;
    }

    public String getStudentUsernames() {
        return studentUsernames;
    }

    public void setStudentUsernames(String studentUsernames) {
        this.studentUsernames = studentUsernames;
    }

    public String getStudentToDelete() {
        return studentToDelete;
    }

    public void setStudentToDelete(String studentToDelete) {
        this.studentToDelete = studentToDelete;
    }

    public long getCourseTableID() {
        return courseTableID;
    }

    public void setCourseTableID(long courseTableID) {
        this.courseTableID = courseTableID;
    }

    public CourseType getCourseType() {
        return courseType;
    }

    public void setCourseType(CourseType courseType) {
        this.courseType = courseType;
    }

    public Course translateModelToCourse(){
        Course course = new Course();
        course.setStudentUsernames(this.studentUsernames);
        course.setInstructorUsername(this.instructorUsername);
        course.setCourseType(this.courseType);
        return course;
    }
}
