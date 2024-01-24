package com.reto.elorchat.model.controller.request;

import java.security.SecureRandom;

public class PasswordGenerator {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int PASSWORD_LENGTH = 8;

    private String password;

    public PasswordGenerator() {
        this.password = generateRandomPassword();
    }

    private String generateRandomPassword() {
        StringBuilder randomPassword = new StringBuilder();

        // Agregar caracteres aleatorios
        SecureRandom random = new SecureRandom();
        while (randomPassword.length() < PASSWORD_LENGTH) {
            int index = random.nextInt(CHARACTERS.length());
            randomPassword.append(CHARACTERS.charAt(index));
        }

        if (randomPassword.length() > PASSWORD_LENGTH) {
            randomPassword.setLength(PASSWORD_LENGTH);
        }

        return randomPassword.toString();
    }

    public String getPassword() {
        return password;
    }

}
