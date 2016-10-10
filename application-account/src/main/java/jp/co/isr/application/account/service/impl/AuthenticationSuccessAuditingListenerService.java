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
package jp.co.isr.application.account.service.impl;

import javax.inject.Inject;
import javax.inject.Named;
import jp.co.isr.application.account.model.dto.AccountDto;
import jp.co.isr.application.account.service.AccountLoginAuditService;
import jp.co.isr.application.account.service.AccountService;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * This a service, implementing {@link ApplicationListener}, which handles
 * authentication auditing upon successful login. This service listens to {@link
 * AuthenticationSuccessEvent}.
 *
 * @author Warren Nocos
 * @since 1.0
 * @version 1.0
 */
@Service
public class AuthenticationSuccessAuditingListenerService
        implements ApplicationListener<AuthenticationSuccessEvent> {

    protected final AccountService accountService;

    protected final AccountLoginAuditService accountLoginAuditService;

    @Inject
    public AuthenticationSuccessAuditingListenerService(
            @Named("accountServiceImpl") AccountService accountService,
            @Named("accountLoginAuditServiceImpl") AccountLoginAuditService accountLoginAuditService) {
        this.accountService = accountService;
        this.accountLoginAuditService = accountLoginAuditService;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        Object principal = event.getAuthentication().getPrincipal();
        if (principal != null && principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            AccountDto accountDto = accountService.findAccountByEmail(userDetails.getUsername());
            accountLoginAuditService.audit(accountDto);
        }
    }

}
