package com.example.Inventory.service;

import com.example.Inventory.dto.account.AccessTokenDTO;
import com.example.Inventory.dto.account.RegisterFormDTO;
import com.example.Inventory.dto.account.UserInfoDTO;
import com.example.Inventory.exception.CustomRuntimeException;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Objects;

@Service
public class AccountService {
    private final RestTemplate restTemplate = new RestTemplate();
    Keycloak keycloak = Keycloak.getInstance(
            "http://localhost:9090",
            "master",
            "admin",
            "admin",
            "admin-cli");

    public String adminAccessToken() {
        String url = "http://localhost:9090/realms/master/protocol/openid-connect/token";

        // set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // set body
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", "admin-cli");
        map.add("username", "admin");
        map.add("password", "admin");
        map.add("grant_type", "password");

        // set http
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        // send request
        ResponseEntity<String> res = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
        ObjectMapper mapper = new ObjectMapper();
        AccessTokenDTO mapped = mapper.readValue(res.getBody(), AccessTokenDTO.class);

        return mapped.access_token();
    }

    // list users
    public List<UserInfoDTO> listAllUsers() {
        // data
        String allUsersUrl = "http://localhost:9090/admin/realms/InventoryRealm/users";
        String accessToken = adminAccessToken();

        // set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        // set http
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);

        // send request
        ResponseEntity<String> res = restTemplate.exchange(allUsersUrl, HttpMethod.GET, request, String.class);

        // map and return
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(res.getBody(), new TypeReference<>(){});

    }

    // register
    public void register(RegisterFormDTO register) {
        // init user details
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(register.username());
        userRepresentation.setFirstName(register.firstname());
        userRepresentation.setLastName(register.lastname());
        userRepresentation.setEmail(register.email());
        userRepresentation.setEnabled(true);

        // init password credentials
        CredentialRepresentation setPassword = new CredentialRepresentation();
        setPassword.setType("password");
        setPassword.setUserLabel("My password");
        setPassword.setTemporary(false);

        if(Objects.equals(register.password(), register.confirmPassword())) {
            setPassword.setValue(register.password());
        } else {
            throw new CustomRuntimeException("Password not match!");
        }

        try {
            // create user
            Response response = keycloak.realm("InventoryRealm")
                    .users()
                    .create(userRepresentation);

            String userId = CreatedResponseUtil.getCreatedId(response);

            // set password
            keycloak.realm("InventoryRealm")
                    .users()
                    .get(userId)
                    .resetPassword(setPassword);

            // set role
            ClientRepresentation client = keycloak.realm("InventoryRealm")
                    .clients()
                    .findByClientId("inventory")
                    .get(0);


            RoleRepresentation role = keycloak.realm("InventoryRealm")
                    .clients()
                    .get(client.getId())
                    .roles()
                    .get("role_user")
                    .toRepresentation();

            keycloak.realm("InventoryRealm")
                    .users()
                    .get(userId)
                    .roles()
                    .clientLevel(client.getId())
                    .add(List.of(role));


        } catch (RuntimeException e) {
            throw new CustomRuntimeException(e.getMessage());
        }

    }
}
