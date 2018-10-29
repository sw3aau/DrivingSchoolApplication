package dk.aau.cs.ds302e18.app;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Test
{
    public static void main(String[] args)
    {
        Student student = new Student("Test","User","20304050",
                "peter@servon.eu", "20/10-2018", "Testvej 120",
                "9000", "Aalborg", "TestUser");
        student.setPassword("$2a$11$dp4wMyuqYE3KSwIyQmWZFeUb7jCsHAdk7ZhFc0qGw6i5J124imQBi");
        System.out.println(student.toString());

        String password = "test";
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(11);
        System.out.println(passwordEncoder.encode(password));



    }
}
