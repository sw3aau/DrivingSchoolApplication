package dk.aau.cs.ds302e18.shared.model;

public class CourseModel {
    private String studentUsernames;

    public String getStudentUsernames() {
        return studentUsernames;
    }

    public void setStudentUsernames(String studentUsernames) {
        this.studentUsernames = studentUsernames;
    }

    public Course translateModelToCourse(){
        Course course = new Course();
        course.setStudentUsernames(this.studentUsernames);
        return course;
    }
}
