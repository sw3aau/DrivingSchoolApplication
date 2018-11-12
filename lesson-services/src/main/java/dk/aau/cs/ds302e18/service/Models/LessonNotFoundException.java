package dk.aau.cs.ds302e18.service.Models;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.NOT_FOUND)
public class LessonNotFoundException extends RuntimeException {
    public LessonNotFoundException(String s) {
        super(s);
    }
}
