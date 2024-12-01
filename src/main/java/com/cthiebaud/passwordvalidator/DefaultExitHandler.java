package com.cthiebaud.passwordvalidator;

public class DefaultExitHandler implements ExitHandler {
    @Override
    public void exit(int status) {
        System.exit(status);
    }
}
