/*
 * Copyright 2016 International Systems Research Co. (ISR).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jp.co.isr.application.account.endpoint.rest;

import java.util.Collections;
import jp.co.isr.application.account.service.exception.AccountNotFoundException;
import jp.co.isr.application.account.service.exception.GrantedAuthorityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * This is used as an interceptor to handle exceptions for RESTful endpoints.
 *
 * @author Warren Nocos
 * @since 1.0
 * @version 1.0
 */
@ControllerAdvice
public class ExceptionHandlingControllerAdvice {

    /**
     * This is used to intercept {@link AccountNotFoundException} and
     * {@link GrantedAuthorityNotFoundException} exceptions.
     *
     * @param exception the {@link Exception}
     * @return the response
     */
    @ExceptionHandler({AccountNotFoundException.class, GrantedAuthorityNotFoundException.class})
    protected ResponseEntity<?> onIllegalRequestStateFailure(Exception exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Collections.singletonMap("message", exception.getMessage()));
    }

    /**
     * This is used to intercept {@link AccessDeniedException} and
     * {@link SecurityException} exceptions.
     *
     * @param exception the {@link Exception}
     * @return the response
     */
    @ExceptionHandler({AccessDeniedException.class, SecurityException.class})
    protected ResponseEntity<?> onSecurityFailure(Exception exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Collections.singletonMap("message", exception.getMessage()));
    }

    /**
     * This is used to intercept unexpected exceptions.
     *
     * @param exception the {@link Exception}
     * @return the response
     */
    @ExceptionHandler
    protected ResponseEntity<?> onFailure(Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Collections.singletonMap("message", exception.getMessage()));
    }

}
