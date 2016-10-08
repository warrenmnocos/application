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
import jp.co.isr.application.account.repository.AccountClientDetailsRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * This is an implementation for {@link ClientDetailsService}.
 *
 * @author Warren Nocos
 * @since 1.0
 * @version 1.0
 */
@Service
@Primary
@Transactional(readOnly = true)
public class AccountClientDetailsService implements ClientDetailsService {

    protected final AccountClientDetailsRepository accountClientDetailsRepository;

    @Inject
    public AccountClientDetailsService(AccountClientDetailsRepository accountClientDetailsRepository) {
        this.accountClientDetailsRepository = accountClientDetailsRepository;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        return accountClientDetailsRepository.findByClientId(clientId)
                .orElseThrow(() -> new ClientRegistrationException("Bad credentials"));
    }

}
