package dk.aau.cs.ds302e18.app.service;

import dk.aau.cs.ds302e18.app.domain.Course;
import dk.aau.cs.ds302e18.app.domain.CourseModel;
import dk.aau.cs.ds302e18.app.domain.Lesson;
import dk.aau.cs.ds302e18.app.domain.LessonModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/* Class responsible for reading lesson related data from the 8100 server. */
@Service
public class CourseService
{
    private static final String COURSE = "/course";
    private static final String SLASH = "/";

    @Value("http://localhost:8100")
    private String courseServiceURL;

    private final RestTemplate restTemplate = new RestTemplate();

    /* Retrieves an list of lessons from the 8100 server and returns it as list of lessons in the format specified in
       the Lesson class. */
    public List<Lesson> getAllLessons()
    {
        String url = courseServiceURL + COURSE;
        HttpEntity<String> request = new HttpEntity<>(null, null);
        return this.restTemplate.exchange(url, HttpMethod.GET, request, new ParameterizedTypeReference<List<Lesson>>() { }).getBody();
    }

    /* Returns an lesson object from the 8100 server that has just been added */
    public Course addCourse(CourseModel lessonModel)
    {
        String url = courseServiceURL + COURSE;
        HttpEntity<CourseModel> request = new HttpEntity<>(lessonModel, null);
        return this.restTemplate.exchange(url, HttpMethod.POST, request, Course.class).getBody();
    }


    public Lesson getLesson(Integer id) {
        String url = courseServiceURL + COURSE + SLASH + id;
        HttpEntity<String> request = new HttpEntity<>(null, null);
        return this.restTemplate.exchange(url, HttpMethod.GET, request, Lesson.class).getBody();
    }

    public Course updateCourse(Integer id, CourseModel lessonModel) {
        System.out.println(lessonModel);
        /* Hvorfra på hjemmesiden de henter infoen */
        String url = courseServiceURL + COURSE + SLASH + id;
        HttpEntity<CourseModel> request = new HttpEntity<>(lessonModel, null);
        /* PUT = hvad de skal gøre med objectet, i dette tilfælde Lesson.class */
        return this.restTemplate.exchange(url, HttpMethod.PUT, request, Course.class).getBody();
    }
}
