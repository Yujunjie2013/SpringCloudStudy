package org.junjie.security.core.support;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class RemoteTokenServicesExt extends RemoteTokenServices {
    private final AccessTokenConverter tokenConverter = new DefaultAccessTokenConverter();

    private final RestTemplate restTemplate;
    private final ResourceServerProperties resourceServerProperties;

    public RemoteTokenServicesExt(RestTemplate restTemplate, ResourceServerProperties resourceServerProperties) {
        this.restTemplate = restTemplate;
//        this.oAuth2ClientProperties = oAuth2ClientProperties;
        this.resourceServerProperties = resourceServerProperties;
        this.restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            // Ignore 400
            public void handleError(ClientHttpResponse response) throws IOException {
                if (response.getRawStatusCode() != 400) {
                    super.handleError(response);
                }
            }
        });
    }

    @Override
    public OAuth2Authentication loadAuthentication(String accessToken) throws AuthenticationException, InvalidTokenException {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("token", accessToken);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", this.getAuthorizationHeader(resourceServerProperties.getClientId(), resourceServerProperties.getClientSecret()));
        headers.set("Accept", "application/json");
        Map<String, Object> map = this.postForMap(resourceServerProperties.getTokenInfoUri(), formData, headers);
        if (map.containsKey("error")) {
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("check_token returned error: " + map.get("error"));
            }
            throw new InvalidTokenException(accessToken);
        } else if (map.containsKey("active") && !"true".equals(String.valueOf(map.get("active")))) {
            this.logger.debug("check_token returned active attribute: " + map.get("active"));
            throw new InvalidTokenException(accessToken);
        } else {
            return tokenConverter.extractAuthentication(map);
        }
    }

    private String getAuthorizationHeader(String clientId, String clientSecret) {
        if (clientId == null || clientSecret == null) {
            this.logger.warn("Null Client ID or Client Secret detected. Endpoint that requires authentication will reject request with 401 error.");
        }

        String creds = String.format("%s:%s", clientId, clientSecret);

        return "Basic " + Base64.encode(creds.getBytes(StandardCharsets.UTF_8));
    }

    private Map<String, Object> postForMap(String path, MultiValueMap<String, String> formData, HttpHeaders headers) {
        if (headers.getContentType() == null) {
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        }
//        Map map = restTemplate.exchange(path, HttpMethod.POST,
//                new HttpEntity<MultiValueMap<String, String>>(formData, headers), Map.class).getBody();

        ResponseEntity<Map> exchange = restTemplate.exchange(path, HttpMethod.POST,
                new HttpEntity<>(formData, headers), Map.class);
        Map body = exchange.getBody();
//        exchange.g
        return body;
    }

}
