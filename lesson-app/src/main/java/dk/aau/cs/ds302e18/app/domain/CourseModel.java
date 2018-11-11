package dk.aau.cs.ds302e18.app.domain;

public class CourseModel {
    private long id;
    private String studentUsernames;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStudentUsernames() {
        return studentUsernames;
    }

    public void setStudentUsernames(String studentUsernames) {
        this.studentUsernames = studentUsernames;
    }


    public Course translateModelToCourse(){
        Course course = new Course();
        course.setId(this.id);
        course.setStudentUsernames(this.studentUsernames);
        return course;
    }
}
