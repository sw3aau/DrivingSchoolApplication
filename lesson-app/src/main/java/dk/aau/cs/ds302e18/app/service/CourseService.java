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
    private static final String REQUESTS = "/course";
    private static final String SLASH = "/";

    @Value("http://localhost:8100")
    private String storeServiceUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    /* Retrieves an list of store from the 8100 server and returns it as list of lessons in the format specified in
       the Lesson class. */
    public List<Course> getAllCourseRequests()
    {
        String url = storeServiceUrl + REQUESTS;
        HttpEntity<String> request = new HttpEntity<>(null, null);
        return this.restTemplate.exchange(url, HttpMethod.GET, request, new ParameterizedTypeReference<List<Course>>() { }).getBody();
    }

    /* Returns an store object from the 8100 server that has just been added */
    public Course addCourseRequest(CourseModel courseModel)
    {
        String url = storeServiceUrl + REQUESTS;
        HttpEntity<CourseModel> request = new HttpEntity<>(courseModel, null);
        return this.restTemplate.exchange(url, HttpMethod.POST, request, Course.class).getBody();
    }


    public Course getCourseRequest(long id) {
        String url = storeServiceUrl + REQUESTS + SLASH + id;
        HttpEntity<String> request = new HttpEntity<>(null, null);
        return this.restTemplate.exchange(url, HttpMethod.GET, request, Course.class).getBody();
    }

    public Course acceptCourseRequest(long id, CourseModel storeModel) {
        System.out.println(storeModel);
        String url = storeServiceUrl + REQUESTS + SLASH + id;
        HttpEntity<CourseModel> request = new HttpEntity<>(storeModel, null);
        return this.restTemplate.exchange(url, HttpMethod.PUT, request, Course.class).getBody();
    }
}
