package org.demo.rest.fusion.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Manages Fusion authentication, cookie based.
 */

@Service
public class FusionAuthenticationService {

    @Value("${fusion.base.url}")
    private String basehUrl;

    @Value("${fusion.auth.path}")
    private String authPath;

    @Value("${fusion.auth.user}")
    private String authUsername;

    @Value("${fusion.auth.password}")
    private String authPassword;

    /**
     * Returns a cookie that can used to authenticate for Fusion Query Pipelines API
     * TODO: use a cache to re-use same cookie within a given time span instead of doing a request every time
     * @return cookie
     */
    public String getCookie() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        String credentialInput = createCredentialInput();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity(credentialInput, headers);

        ResponseEntity<String> response = restTemplate.exchange(basehUrl + authPath, HttpMethod.POST, entity, String.class);

        String cookie = response.getHeaders().getFirst(headers.SET_COOKIE);
        if (cookie.contains(";")) {
            cookie = cookie.substring(0, cookie.indexOf(";"));
        }
        return cookie;
    }

    private String createCredentialInput() {
        return "{\"username\":\"" + authUsername + "\",\"password\":\"" + authPassword + "\"}";

    }
}
