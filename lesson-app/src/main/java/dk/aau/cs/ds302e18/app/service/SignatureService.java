package dk.aau.cs.ds302e18.app.service;

        import dk.aau.cs.ds302e18.app.domain.Signature;
        import dk.aau.cs.ds302e18.app.domain.SignatureModel;
        import org.springframework.beans.factory.annotation.Value;
        import org.springframework.core.ParameterizedTypeReference;
        import org.springframework.http.HttpEntity;
        import org.springframework.http.HttpMethod;
        import org.springframework.stereotype.Service;
        import org.springframework.web.client.RestTemplate;

        import java.util.List;

/* Class responsible for reading signature related data from the 8100 server. */
@Service
public class SignatureService
{
    private static final String SIGNATURES = "/signatures";
    private static final String SLASH = "/";

    @Value("${ds.service.url}")
    private String signatureServiceUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    /* Retrieves an list of signatures from the 8100 server and returns it as list of signatures in the format specified in
       the Signature class. */
    public List<Signature> getAllSignatures()
    {
        String url = signatureServiceUrl + SIGNATURES;
        HttpEntity<String> request = new HttpEntity<>(null, null);
        return this.restTemplate.exchange(url, HttpMethod.GET, request, new ParameterizedTypeReference<List<Signature>>() { }).getBody();
    }

    /* Returns an signature object from the 8100 server that has just been added */
    public Signature addSignature(SignatureModel signatureModel)
    {
        String url = signatureServiceUrl + SIGNATURES;
        HttpEntity<SignatureModel> request = new HttpEntity<>(signatureModel, null);
        return this.restTemplate.exchange(url, HttpMethod.POST, request, Signature.class).getBody();
    }


    public Signature getSignature(long id) {
        String url = signatureServiceUrl + SIGNATURES + SLASH + id;
        HttpEntity<String> request = new HttpEntity<>(null, null);
        return this.restTemplate.exchange(url, HttpMethod.GET, request, Signature.class).getBody();
    }

    public Signature updateSignature(long id, SignatureModel signatureModel) {
        System.out.println(signatureModel);
        String url = signatureServiceUrl + SIGNATURES + SLASH + id;
        HttpEntity<SignatureModel> request = new HttpEntity<>(signatureModel, null);
        return this.restTemplate.exchange(url, HttpMethod.PUT, request, Signature.class).getBody();
    }


}
