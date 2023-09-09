package com.bitwarden.passwordless.error;

import java.io.IOException;

public class MalformedUrlException extends IOException {
    public MalformedUrlException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
