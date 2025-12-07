package com.nutmeg.clientPortfolio.expection;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PortfolioModelNotFound extends RuntimeException {
    public PortfolioModelNotFound(String message) {
        super(message);
    }
}
