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

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import javax.annotation.PostConstruct;
import jp.co.isr.application.account.model.entity.Account;
import jp.co.isr.application.account.model.entity.AccountClientDetails;
import jp.co.isr.application.account.model.entity.AccountClientGrantedAuthority;
import jp.co.isr.application.account.model.entity.AccountLoginAudit;
import jp.co.isr.application.account.model.entity.AccountUserDetails;
import jp.co.isr.application.account.model.entity.AccountUserGrantedAuthority;
import jp.co.isr.application.account.repository.AccountClientDetailsRepository;
import jp.co.isr.application.account.repository.AccountClientGrantedAuthorityRepository;
import jp.co.isr.application.account.repository.AccountLoginAuditRepository;
import jp.co.isr.application.account.repository.AccountRepository;
import jp.co.isr.application.account.repository.AccountUserDetailsRepository;
import jp.co.isr.application.account.repository.AccountUserGrantedAuthorityRepository;
import jp.co.isr.application.account.service.DataPopulatorService;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
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
@Profile("initialization")
@Transactional(readOnly = true)
public class DataPopulatorServiceImpl implements DataPopulatorService {

    protected final AccountRepository accountRepository;

    protected final AccountUserGrantedAuthorityRepository accountUserGrantedAuthorityRepository;

    protected final AccountUserDetailsRepository accountUserDetailsRepository;

    protected final AccountLoginAuditRepository accountLoginAuditRepository;

    protected final AccountClientGrantedAuthorityRepository accountClientGrantedAuthorityRepository;

    protected final AccountClientDetailsRepository accountClientDetailsRepository;

    protected final PasswordEncoder passwordEncoder;

    protected final Random random;

    public DataPopulatorServiceImpl(AccountRepository accountRepository,
            AccountUserGrantedAuthorityRepository accountUserGrantedAuthorityRepository,
            AccountUserDetailsRepository accountUserDetailsRepository,
            AccountLoginAuditRepository accountLoginAuditRepository,
            AccountClientGrantedAuthorityRepository accountClientGrantedAuthorityRepository,
            AccountClientDetailsRepository accountClientDetailsRepository,
            PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.accountUserGrantedAuthorityRepository = accountUserGrantedAuthorityRepository;
        this.accountUserDetailsRepository = accountUserDetailsRepository;
        this.accountLoginAuditRepository = accountLoginAuditRepository;
        this.accountClientGrantedAuthorityRepository = accountClientGrantedAuthorityRepository;
        this.accountClientDetailsRepository = accountClientDetailsRepository;
        this.passwordEncoder = passwordEncoder;
        this.random = new Random();
    }

    /**
     * Called after this object is initialized.
     */
    @PostConstruct
    public void populate() {
        populateAccountUserGrantedAuthority();
        populateAccount();
        populateAccountUserDetails();
        populateAccountLoginAudit();
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
    public void populateAccount() {
        Stream.of(// Same first name, middle name, and last name
                "wa@gmail.com,Warren,Lo,Nocos,ANONYMOUS",
                "war@gmail.com,Warren,Lo,Nocos,ANONYMOUS",
                "warr@gmail.com,Warren,Lo,Nocos,ANONYMOUS",
                "warre@gmail.com,Warren,Lo,Nocos,ANONYMOUS",
                // Same middle name, and last name
                "loulonocos@gmail.com,Lou,Lo,Nocos,ANONYMOUS",
                "ricalonocos@gmail.com,Rica,Lo,Nocos,ANONYMOUS",
                "tinalonocos@gmail.com,Tina,Lo,Nocos,ANONYMOUS",
                "alenlonocos@gmail.com,Alen,Lo,Nocos,ANONYMOUS",
                // Same first name and middle name
                "warrenlosa@gmail.com,Warren,Lo,Sa,ANONYMOUS",
                "warrenloprex@gmail.com,Warren,Lo,Prex,ANONYMOUS",
                "warrenloantonio@gmail.com,Warren,Lo,Antonio,ANONYMOUS",
                "warrenlokortana@gmail.com,Warren,Lo,Kortana,ANONYMOUS",
                // Same first name, and last name
                "warrenveranocos@gmail.com,Warren,Vera,Nocos,ANONYMOUS",
                "warrenloiusnocos@gmail.com,Warren,Loius,Nocos,ANONYMOUS",
                "warrenquizanocos@gmail.com,Warren,Quiza,Nocos,ANONYMOUS",
                "warrenwevicknocos@gmail.com,Warren,Wevick,Nocos,ANONYMOUS")
                .map(account -> account.split(","))
                .map(accountDetails -> {
                    Account account = new Account();
                    account.setEmail(accountDetails[0]);
                    account.setFirstName(accountDetails[1]);
                    account.setMiddleName(accountDetails[2]);
                    account.setLastName(accountDetails[3]);
                    account.setAddresses(Collections.emptyMap());
                    account.setContacts(Collections.emptyMap());
                    account.setCreator(accountDetails[4]);
                    return account;
                })
                .forEach(accountRepository::save);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    @Transactional
    public void populateAccountUserDetails() {
        Set<AccountUserGrantedAuthority> allAuthorities;
        allAuthorities = StreamSupport.stream(accountUserGrantedAuthorityRepository.findAll().spliterator(), true)
                .collect(Collectors.toSet());
        Set<AccountUserGrantedAuthority> adminAuthority;
        adminAuthority = allAuthorities.stream()
                .filter(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_USER"))
                .collect(Collectors.toSet());
        Set<AccountUserGrantedAuthority> userAuthority;
        userAuthority = allAuthorities.stream()
                .filter(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_USER"))
                .collect(Collectors.toSet());
        List<Set<AccountUserGrantedAuthority>> athoritiesCollections;
        athoritiesCollections = Arrays.asList(allAuthorities, adminAuthority, userAuthority);
        accountRepository.findAllAsStream()
                .map(account -> {
                    return new AccountUserDetails(account.getEmail(), passwordEncoder.encode("1234"),
                            athoritiesCollections.get(random.nextInt(athoritiesCollections.size())), true);
                })
                .forEach(accountUserDetailsRepository::save);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    @Transactional
    public void populateAccountLoginAudit() {
        Date[] loginDates = new Date[30];
        for (int index = 0; index < loginDates.length; index++) {
            loginDates[index] = Date.from(ZonedDateTime.of(
                    LocalDate.of(2016, Month.SEPTEMBER, index + 1),
                    LocalTime.MIN, ZoneId.systemDefault()).toInstant());
        }
        accountRepository.findAllAsStream()
                .forEach(account -> {
                    for (Date loginDate : loginDates) {
                        AccountLoginAudit accountLoginAudit = new AccountLoginAudit(account);
                        accountLoginAuditRepository.save(accountLoginAudit);
                        // Overwrite audited loginTime
                        accountLoginAudit.setLoginTime(loginDate);
                        accountLoginAuditRepository.save(accountLoginAudit);
                    }
                });
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
