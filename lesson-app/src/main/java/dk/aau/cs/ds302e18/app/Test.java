package dk.aau.cs.ds302e18.app;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.UUID;

public class Test
{
    public static void main(String[] args)
    {
        UUID uuid = UUID.randomUUID();
        System.out.println(uuid.toString());

    }
}
