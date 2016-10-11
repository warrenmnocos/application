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
import jp.co.isr.application.account.model.entity.AccountLoginAudit;
import jp.co.isr.application.account.repository.AccountLoginAuditRepository;
import jp.co.isr.application.account.repository.AccountRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    protected final AccountRepository accountRepository;

    protected final AccountLoginAuditRepository accountLoginAuditRepository;

    @Inject
    public AuthenticationSuccessAuditingListenerService(AccountRepository accountRepository,
            AccountLoginAuditRepository accountLoginAuditRepository) {
        this.accountRepository = accountRepository;
        this.accountLoginAuditRepository = accountLoginAuditRepository;
    }

    /**
     * {@inheritDoc }
     * This listener audits every successful login.
     */
    @Override
    @Transactional
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        Object principal = event.getAuthentication().getPrincipal();
        if (principal != null && principal instanceof UserDetails
                && event.getAuthentication().isAuthenticated()) {
            UserDetails userDetails = (UserDetails) principal;
            accountRepository.findByEmail(userDetails.getUsername())
                    .map(AccountLoginAudit::new)
                    .ifPresent(accountLoginAuditRepository::save);
        }
    }

}
