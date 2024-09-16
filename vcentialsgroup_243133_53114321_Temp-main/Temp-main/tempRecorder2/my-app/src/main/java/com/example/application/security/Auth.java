package com.example.application.security;

import com.google.firebase.auth.FirebaseToken;

import java.util.Map;
import java.util.Optional;

public class Auth {

    public static boolean isNotLoggedIn(AuthenticatedUser authenticatedUser){
        return !isLoggedIn(authenticatedUser);
    }

    public static boolean isLoggedIn(AuthenticatedUser authenticatedUser){
        return authenticatedUser != null && authenticatedUser.get().isPresent();
    }

    public static boolean isAdmin(AuthenticatedUser authenticatedUser){
        Optional<FirebaseToken> firebaseToken = authenticatedUser.get();
        if(firebaseToken.isEmpty()) return false;
        Map<String, Object> claims = firebaseToken.get().getClaims();
        String role = (String) claims.get("role");
        return role != null && role.equals("admin");
    }
}
