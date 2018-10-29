package dk.aau.cs.ds302e18.app.service;

import dk.aau.cs.ds302e18.app.domain.Lesson;
import dk.aau.cs.ds302e18.app.domain.LessonModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class LessonService
{
    private static final String LESSONS = "/lessons";
    private static final String SLASH = "/";

    @Value("http://localhost:8100")
    private String lessonServiceUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public List<Lesson> getAllLessons()
    {
        String url = lessonServiceUrl + LESSONS;
        HttpEntity<String> request = new HttpEntity<>(null, null);
        return this.restTemplate.exchange(url, HttpMethod.GET, request, new ParameterizedTypeReference<List<Lesson>>() { }).getBody();
    }

    public Lesson addLesson(LessonModel lessonModel)
    {
        String url = lessonServiceUrl + LESSONS;
        HttpEntity<LessonModel> request = new HttpEntity<>(lessonModel, null);
        return this.restTemplate.exchange(url, HttpMethod.POST, request, Lesson.class).getBody();
    }


    public Lesson getLesson(long id) {
        String url = lessonServiceUrl + LESSONS + SLASH + id;
        HttpEntity<String> request = new HttpEntity<>(null, null);
        return this.restTemplate.exchange(url, HttpMethod.GET, request, Lesson.class).getBody();
    }

    public Lesson updateLesson(long id, LessonModel lessonModel) {
        System.out.println(lessonModel);
        String url = lessonServiceUrl + LESSONS + SLASH + id;
        HttpEntity<LessonModel> request = new HttpEntity<>(lessonModel, null);
        return this.restTemplate.exchange(url, HttpMethod.PUT, request, Lesson.class).getBody();
    }


}
