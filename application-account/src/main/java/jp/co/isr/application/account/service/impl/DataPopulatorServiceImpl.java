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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import jp.co.isr.application.account.model.entity.AccountClientDetails;
import jp.co.isr.application.account.model.entity.AccountClientGrantedAuthority;
import jp.co.isr.application.account.model.entity.AccountUserDetails;
import jp.co.isr.application.account.model.entity.AccountUserGrantedAuthority;
import jp.co.isr.application.account.repository.AccountClientDetailsRepository;
import jp.co.isr.application.account.repository.AccountClientGrantedAuthorityRepository;
import jp.co.isr.application.account.repository.AccountUserDetailsRepository;
import jp.co.isr.application.account.repository.AccountUserGrantedAuthorityRepository;
import jp.co.isr.application.account.service.DataPopulatorService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * This is an implementation for {@link DataPopulatorService}.
 *
 * @author Warren Nocos
 * @since 1.0
 * @version 1.0
 */
@Service
@Profile("installation")
@Transactional(readOnly = true)
public class DataPopulatorServiceImpl implements DataPopulatorService {

    protected final AccountUserGrantedAuthorityRepository accountUserGrantedAuthorityRepository;

    protected final AccountUserDetailsRepository accountUserDetailsRepository;

    protected final AccountClientGrantedAuthorityRepository accountClientGrantedAuthorityRepository;

    protected final AccountClientDetailsRepository accountClientDetailsRepository;

    @Inject
    public DataPopulatorServiceImpl(AccountUserGrantedAuthorityRepository accountUserGrantedAuthorityRepository,
            AccountUserDetailsRepository accountUserDetailsRepository, AccountClientGrantedAuthorityRepository accountClientGrantedAuthorityRepository,
            AccountClientDetailsRepository accountClientDetailsRepository) {
        this.accountUserGrantedAuthorityRepository = accountUserGrantedAuthorityRepository;
        this.accountUserDetailsRepository = accountUserDetailsRepository;
        this.accountClientGrantedAuthorityRepository = accountClientGrantedAuthorityRepository;
        this.accountClientDetailsRepository = accountClientDetailsRepository;
    }

    /**
     * Called after this object is initialized.
     */
    @PostConstruct
    public void populate() {
        populateAccountUserGrantedAuthority();
        populateAccountUserDetails();
        populateAccountClientGrantedAuthority();
        populateAccountClientDetailsService();
    }

    /**
     * {@inheritDoc }
     */
    @Override
    @Transactional
    public void populateAccountUserGrantedAuthority() {
        accountUserGrantedAuthorityRepository.save(Stream.of("ROLE_ADMIN", "ROLE_USER")
                .map(AccountUserGrantedAuthority::new)
                .peek(grantedAuthority -> grantedAuthority.setCreator("ANONYMOUS"))
                .collect(Collectors.toSet()));
    }

    /**
     * {@inheritDoc }
     */
    @Override
    @Transactional
    public void populateAccountUserDetails() {
        
        
        
        Set<AccountUserGrantedAuthority> accountUserGrantedAuthorities;
        accountUserGrantedAuthorities = StreamSupport.stream(accountUserGrantedAuthorityRepository.findAll().spliterator(), true)
                .collect(Collectors.toSet());
        accountUserDetailsRepository.save(Stream.of("admin,1234,true", "2,warren,1234,true")
                .map(userDetails -> userDetails.split(","))
                .map(userDetails -> new AccountUserDetails(userDetails[0], userDetails[1], accountUserGrantedAuthorities, Boolean.parseBoolean(userDetails[2])))
                .peek(userDetails -> userDetails.setCreator("ANONYMOUS"))
                .collect(Collectors.toSet()));
        accountUserGrantedAuthorities.removeIf(accountUserGrantedAuthority -> accountUserGrantedAuthority.getAuthority().equals("ROLE_ADMIN"));
        accountUserDetailsRepository.save(Stream.of("3,stockholder,1234,true", "4,kirby,1234,true")
                .map(userDetails -> userDetails.split(","))
                .map(userDetails -> new AccountUserDetails(userDetails[0], userDetails[1], accountUserGrantedAuthorities, Boolean.parseBoolean(userDetails[2])))
                .peek(userDetails -> userDetails.setCreator("ANONYMOUS"))
                .collect(Collectors.toSet()));
        accountUserGrantedAuthorities.removeIf(accountUserGrantedAuthority -> accountUserGrantedAuthority.getAuthority().equals("ROLE_STOCKTAKER"));
        accountUserDetailsRepository.save(Stream.of("5,user,1234,true", "6,mario,1234,true")
                .map(userDetails -> userDetails.split(","))
                .map(userDetails -> new AccountUserDetails(userDetails[0], userDetails[1], accountUserGrantedAuthorities, Boolean.parseBoolean(userDetails[2])))
                .peek(userDetails -> userDetails.setCreator("ANONYMOUS"))
                .collect(Collectors.toSet()));
    }

    /**
     * {@inheritDoc }
     */
    @Override
    @Transactional
    public void populateAccountClientGrantedAuthority() {
        accountClientGrantedAuthorityRepository.save(Stream.of("ROLE_WEB_APP", "ROLE_MOBILE_APP", "ROLE_ADMIN")
                .map(AccountClientGrantedAuthority::new)
                .peek(grantedAuthority -> grantedAuthority.setCreator("ANONYMOUS"))
                .collect(Collectors.toSet()));
    }

    /**
     * {@inheritDoc }
     */
    @Override
    @Transactional
    public void populateAccountClientDetailsService() {
        Set<AccountClientGrantedAuthority> accountClientGrantedAuthorities;
        accountClientGrantedAuthorities = StreamSupport
                .stream(accountClientGrantedAuthorityRepository.findAll().spliterator(), true)
                .collect(Collectors.toSet());

        Set<String> authorizedGrantTypes = new HashSet<>();
        authorizedGrantTypes.add("authorization_code");
        authorizedGrantTypes.add("refresh_token");
        authorizedGrantTypes.add("password");

        Set<String> autoApproveScopes = new HashSet<>();
        autoApproveScopes.add("read");
        autoApproveScopes.add("write");

        Set<String> registeredRedirectUris = new HashSet<>();
        registeredRedirectUris.add("http://127.0.0.1");
        registeredRedirectUris.add("https://127.0.0.1");
        registeredRedirectUris.add("http://localhost");
        registeredRedirectUris.add("https://localhost");

        Set<AccountClientDetails> accountClientDetailses;
        accountClientDetailses = new HashSet<>();

        AccountClientDetails accountClientDetailse;
        accountClientDetailse = new AccountClientDetails();
        accountClientDetailse.setClientId("d56h2sf5c5drsdgr4xpl234cm085021r");
        accountClientDetailse.setClientSecret("of7ho3vc86t00hd6bg7qrwpl12h754pl");
        accountClientDetailse.setAccessTokenValiditySeconds(3600);
        accountClientDetailse.setRefreshTokenValiditySeconds(3600);
        accountClientDetailse.setAutoApprove(true);
        accountClientDetailse.setSecretRequired(true);
        accountClientDetailse.setScoped(true);
        accountClientDetailse.setAuthorizedGrantTypes(authorizedGrantTypes);
        accountClientDetailse.setAuthorities(new ArrayList<>(accountClientGrantedAuthorities));
        accountClientDetailse.setScope(autoApproveScopes);
        accountClientDetailse.setAutoApproveScopes(autoApproveScopes);
        accountClientDetailse.setRegisteredRedirectUri(registeredRedirectUris);
        accountClientDetailse.setCreator("ANONYMOUS");
        accountClientDetailses.add(accountClientDetailse);

        accountClientDetailse = new AccountClientDetails();
        accountClientDetailse.setClientId("hftr0754shf6md8612h754dul09f756");
        accountClientDetailse.setClientSecret("hf75635dnlfh6sgr658f8s8a4f5g7h");
        accountClientDetailse.setAccessTokenValiditySeconds(60);
        accountClientDetailse.setRefreshTokenValiditySeconds(60);
        accountClientDetailse.setAutoApprove(true);
        accountClientDetailse.setSecretRequired(true);
        accountClientDetailse.setScoped(true);
        accountClientDetailse.setAuthorizedGrantTypes(authorizedGrantTypes);
        accountClientDetailse.setAuthorities(new ArrayList<>(accountClientGrantedAuthorities));
        accountClientDetailse.setScope(autoApproveScopes);
        accountClientDetailse.setAutoApproveScopes(autoApproveScopes);
        accountClientDetailse.setRegisteredRedirectUri(registeredRedirectUris);
        accountClientDetailse.setCreator("ANONYMOUS");
        accountClientDetailses.add(accountClientDetailse);

        accountClientDetailsRepository.save(accountClientDetailses);
    }

}
