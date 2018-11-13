package dk.aau.cs.ds302e18.app.controllers;

import dk.aau.cs.ds302e18.app.domain.CanvasModel;
import dk.aau.cs.ds302e18.app.domain.SignatureCanvas;
import dk.aau.cs.ds302e18.app.service.SignatureService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.UUID;

@Controller
@RequestMapping("/")
public class SignatureController
{

    private final SignatureService signatureService;

    public SignatureController(SignatureService signatureService)
    {
        super();
        this.signatureService = signatureService;
    }

    @GetMapping(value = "/canvas/{id}")
    public String getCanvasPage(HttpSession session, @PathVariable long id)
    {
        System.out.println("GETMAP" + id);
        System.out.println(session.getAttribute("testSession"));
        return "canvas";
    }

    @PostMapping(value = "/canvas/{id}")
    public String postCanvasPage(@RequestBody CanvasModel canvasModel, @PathVariable long id)
    {
        System.out.println("Received");
        System.out.println("Canvas ID" + id);

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails)principal).getUsername();
        String role = String.valueOf(((UserDetails)principal).getAuthorities());
        System.out.println("role " + role);

        if (role.contains("ADMIN")) System.out.println("Contains admin");

        SignatureCanvas signatureCanvas = new SignatureCanvas();

        UUID uuid = UUID.randomUUID();

        signatureCanvas.upload("p3-project", uuid.toString(), canvasModel.getDataUrl());

        return "canvas";
    }
}
