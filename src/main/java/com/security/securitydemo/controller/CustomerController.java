package com.security.securitydemo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.userdetails.User;


@RestController
@RequestMapping("v1")
public class CustomerController {

    @Autowired
    private SessionRegistry sessionRegistry;
    
    @GetMapping(value="/secured")
    public String helloSecured() {
        return "Hello World Secured";
    }

    @GetMapping(value="/notSecured")
    public String helloNotSecured() {
        return "Hello World NOT Secured";
    }



    @GetMapping("/session")
    public ResponseEntity<?> getDetailsSession(){
        String sessionId = "";
        User userObject = null;

        // Obtén todas las sesiones activas
        List<Object> sessions = sessionRegistry.getAllPrincipals();

        for (Object session : sessions) {
            if (session instanceof User) {

                User user = (User) session;
                userObject = user;

                List<SessionInformation> sessionInformationList = sessionRegistry.getAllSessions(user, false);

                // Aquí puedes realizar las operaciones que necesites con la lista de sesiones
                for (SessionInformation sessionInformation : sessionInformationList) {

                    // Accede a la información de cada sesión
                    sessionId = sessionInformation.getSessionId();
                }
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("response", "Hello world!");
        response.put("sessionId", sessionId);
        response.put("session", userObject);

        return ResponseEntity.ok(response);
    }
}
