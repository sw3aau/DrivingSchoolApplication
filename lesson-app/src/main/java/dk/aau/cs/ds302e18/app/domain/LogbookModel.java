package dk.aau.cs.ds302e18.app.domain;

public class LogbookModel {

    private long courseID;
    private String student;
    private String lessonList;
    private boolean isActive;

    public long getCourseID() {
        return courseID;
    }

    public void setCourseID(long courseID) {
        this.courseID = courseID;
    }

    public String getStudent() {
        return student;
    }

    public void setStudent(String student) {
        this.student = student;
    }

    public String getLessonList() {
        return lessonList;
    }

    public void setLessonList(String lessonList) {
        this.lessonList = lessonList;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Logbook translateModelToLogbook(){
        Logbook logbook = new Logbook();
        logbook.setCourseID(this.courseID);
        logbook.setStudent(this.student);
        logbook.setLessonList(this.lessonList);
        logbook.setActive(this.isActive);
        return logbook;
    }
}
