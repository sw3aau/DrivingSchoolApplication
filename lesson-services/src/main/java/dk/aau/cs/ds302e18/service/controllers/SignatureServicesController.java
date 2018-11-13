package dk.aau.cs.ds302e18.service.controllers;

import dk.aau.cs.ds302e18.service.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/signature")
public class SignatureServicesController
{
    private static final Logger LOGGER = LoggerFactory.getLogger(SignatureServicesController.class);

    private final SignatureRepository signatureRepository;

    public SignatureServicesController(SignatureRepository signatureRepository)
    {
        super();
        this.signatureRepository = signatureRepository;
    }

    @GetMapping
    public List<Signature> getAllSignatureRequests()
    {
        System.out.println(this.signatureRepository.findAll());
        return new ArrayList<>(this.signatureRepository.findAll());
    }
    /* Get = responsible for retrieving information only */
    @GetMapping("/{id}")
    public Signature getSignature(@PathVariable Long id){
        Optional<Signature> signature = this.signatureRepository.findById(id);
        if(signature.isPresent()){
            return signature.get();
        }
        throw new SignatureNotFoundException("Signature not found with id: " + id);
    }


    /* Post = responsible for posting new information directly after it has been created to the website, and create fitting
    links to the new information. */
    @PostMapping
    public ResponseEntity<Signature> addSignature(@RequestBody SignatureModel model){
        /* Translates the input entered in the add signature menu into input that can be entered in the database. */
        Signature signature = this.signatureRepository.save(model.translateModelToSignature());
        /* The new signature will be placed in the current browser /id , with an id that matches the entered signatureadmin ID. */
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(signature.getId()).toUri();
        /* The connection to the new signature is created. */
        return ResponseEntity.created(location).body(signature);
    }


    /* Put = responsible for updating existing database entries*/
    @PutMapping("/{id}")
    public Signature updateSignature(@PathVariable Long id, @RequestBody SignatureModel model){
        /* Throw an error if the selected signature do not exist. */
        Optional<Signature> existing = this.signatureRepository.findById(id);
        if(!existing.isPresent()){
            throw new SignatureNotFoundException("Signature not found with id: " + id);
        }
        /* Translates input from the interface into an signature object */
        Signature signature = model.translateModelToSignature();
        /* Uses the ID the signature already had to save the signature */
        signature.setId(id);
        return this.signatureRepository.save(signature);
    }

    /* NOT IMPLEMENTED: Delete = responsible for deleting database entries. */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.RESET_CONTENT)
    public void deleteSignature(@PathVariable Long id){
        Optional<Signature> existing = this.signatureRepository.findById(id);
        if(!existing.isPresent()){
            throw new SignatureNotFoundException("Signature not found with id: " + id);
        }
        this.signatureRepository.deleteById(id);
    }
}
