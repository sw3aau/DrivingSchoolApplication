package dk.aau.cs.ds302e18.service.models;

public class CourseModel {

    private String studentUsernames;
    private String studentToDelete;
    private long courseTableID;

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

    public Course translateModelToCourse(){
        Course course = new Course();
        course.setStudentUsernames(this.studentUsernames);
        return course;
    }
}
