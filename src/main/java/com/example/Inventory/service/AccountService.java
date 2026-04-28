package com.example.Inventory.service;

import com.example.Inventory.dto.account.RegisterFormDTO;
import com.example.Inventory.dto.account.UserRepDTO;
import com.example.Inventory.exception.CustomRuntimeException;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class AccountService {
    @Value(value = "${realm.inventory}")
    private String inventoryRealm;

    @Value(value = "${realm.supplier}")
    private String supplierRealm;

    Keycloak keycloak = Keycloak.getInstance(
            "http://localhost:9090",
            "master",
            "admin",
            "admin",
            "admin-cli");

    // fetch client
    private ClientRepresentation fetchClientMethod(String realm, String clientId) {
       return keycloak.realm(realm)
                .clients()
                .findByClientId(clientId)
                .get(0);
    }

    // list users method
    private List<UserRepresentation> usersListMethod(String realm) {
        return keycloak
                .realm(realm)
                .users()
                .list();
    }

    // list roles method
    private List<String> roleMethod(String realm, String userId, String clientId) {
        return keycloak.realm(realm)
                .users()
                .get(userId)
                .roles()
                .clientLevel(clientId)
                .listAll()
                .stream()
                .map(RoleRepresentation::getName)
                .toList();
    }

    // list users
    public List<UserRepDTO> userList() {
        List<UserRepresentation> response = usersListMethod(inventoryRealm);
        List<UserRepDTO> userRepList = new ArrayList<>();

        for(UserRepresentation rep : response) {
            ClientRepresentation client = fetchClientMethod(inventoryRealm, "inventory");
            List<String> role = roleMethod(inventoryRealm, rep.getId(), client.getId());

            UserRepDTO userRepDTO = UserRepDTO
                    .builder()
                    .id(rep.getId())
                    .email(rep.getEmail())
                    .firstName(rep.getFirstName())
                    .lastName(rep.getLastName())
                    .roles(role)
                    .build();

            userRepList.add(userRepDTO);

        }
        return userRepList;
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
            Response response = keycloak.realm(inventoryRealm)
                    .users()
                    .create(userRepresentation);

            String userId = CreatedResponseUtil.getCreatedId(response);

            // set password
            keycloak.realm(inventoryRealm)
                    .users()
                    .get(userId)
                    .resetPassword(setPassword);

            // set role
            ClientRepresentation client = keycloak.realm(inventoryRealm)
                    .clients()
                    .findByClientId("inventory")
                    .get(0);


            RoleRepresentation role = keycloak.realm(inventoryRealm)
                    .clients()
                    .get(client.getId())
                    .roles()
                    .get("role_user")
                    .toRepresentation();

            keycloak.realm(inventoryRealm)
                    .users()
                    .get(userId)
                    .roles()
                    .clientLevel(client.getId())
                    .add(List.of(role));


        } catch (RuntimeException e) {
            throw new CustomRuntimeException(e.getMessage());
        }

    }

    // list supplier
    public List<UserRepDTO> supplierList() {
        List<UserRepresentation> response = usersListMethod(supplierRealm);
        List<UserRepDTO> userRepList = new ArrayList<>();

        for(UserRepresentation rep : response) {
            ClientRepresentation client = fetchClientMethod(supplierRealm, "supplier");
            List<String> role = roleMethod(supplierRealm, rep.getId(), client.getId());

            UserRepDTO userRepDTO = UserRepDTO
                    .builder()
                    .id(rep.getId())
                    .email(rep.getEmail())
                    .firstName(rep.getFirstName())
                    .lastName(rep.getLastName())
                    .roles(role)
                    .build();

            userRepList.add(userRepDTO);

        }
        return userRepList;
    }

    // validate supplier
    public String validSupplier(String id) {
        try {
            UserRepresentation supplier = keycloak.realm(supplierRealm)
                    .users()
                    .get(id)
                    .toRepresentation();

            return supplier.getId();
        } catch (RuntimeException e) {
            throw new CustomRuntimeException(e.getMessage());
        }
    }
}
