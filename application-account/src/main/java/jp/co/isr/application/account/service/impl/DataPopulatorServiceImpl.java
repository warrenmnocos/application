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

import java.time.LocalTime;
import java.time.Month;
import java.time.Year;
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
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
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
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

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

    protected final AccountRepository accountRepository;

    protected final AccountUserGrantedAuthorityRepository accountUserGrantedAuthorityRepository;

    protected final AccountUserDetailsRepository accountUserDetailsRepository;

    protected final AccountLoginAuditRepository accountLoginAuditRepository;

    protected final AccountClientGrantedAuthorityRepository accountClientGrantedAuthorityRepository;

    protected final AccountClientDetailsRepository accountClientDetailsRepository;

    /**
     * This is used for transaction management, as transactions may not be
     * available when {@link PostConstruct} method is invoked.
     *
     * @see
     * <a href='http://stackoverflow.com/questions/17346679/transactional-on-postconstruct-method'>
     * Transactions at PostConstruct</a>
     */
    protected final TransactionTemplate transactionTemplate;

    protected final PasswordEncoder passwordEncoder;

    protected final Random random;

    @Inject
    public DataPopulatorServiceImpl(AccountRepository accountRepository,
            AccountUserGrantedAuthorityRepository accountUserGrantedAuthorityRepository,
            AccountUserDetailsRepository accountUserDetailsRepository,
            AccountLoginAuditRepository accountLoginAuditRepository,
            AccountClientGrantedAuthorityRepository accountClientGrantedAuthorityRepository,
            AccountClientDetailsRepository accountClientDetailsRepository,
            PlatformTransactionManager platformTransactionManager,
            @Named("bCryptPasswordEncoder") PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.accountUserGrantedAuthorityRepository = accountUserGrantedAuthorityRepository;
        this.accountUserDetailsRepository = accountUserDetailsRepository;
        this.accountLoginAuditRepository = accountLoginAuditRepository;
        this.accountClientGrantedAuthorityRepository = accountClientGrantedAuthorityRepository;
        this.accountClientDetailsRepository = accountClientDetailsRepository;
        this.transactionTemplate = new TransactionTemplate(platformTransactionManager);
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
    public void populateAccountUserGrantedAuthority() {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                accountUserGrantedAuthorityRepository.save(Stream.of("ROLE_ADMIN", "ROLE_USER")
                        .map(AccountUserGrantedAuthority::new)
                        .peek(grantedAuthority -> grantedAuthority.setCreator("ANONYMOUS"))
                        .collect(Collectors.toSet()));
            }
        });
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void populateAccount() {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
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
        });
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void populateAccountUserDetails() {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
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
                try (Stream<Account> accounts = accountRepository.findAllAsStream()) {
                    accounts.map(account -> {
                        AccountUserDetails accountUserDetails = new AccountUserDetails(account.getEmail(), passwordEncoder.encode("1234"),
                                athoritiesCollections.get(random.nextInt(athoritiesCollections.size())), true);
                        accountUserDetails.setCreator("ANONYMOUS");
                        return accountUserDetails;
                    }).forEach(accountUserDetailsRepository::save);
                }
            }
        });
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void populateAccountLoginAudit() {
        List<Account> accounts = accountRepository.findAll();
        // Unit of work, used to dispatch in batch for performance
        // LOGIC: In a day, there are 3 login attempts per account
        // (midnight, noon, and a moment before midnight).
        // If there are 16 accounts, this unit can dispatch 48
        // login times
        Function<Stream<Date>, TransactionCallbackWithoutResult> batchSave;
        batchSave = dates -> {
            return new TransactionCallbackWithoutResult() {
                @Override
                protected void doInTransactionWithoutResult(TransactionStatus status) {
                    dates.flatMap(date -> accounts.stream().map(AccountLoginAudit::new)
                            .peek(accountLoginAuditRepository::save)
                            // Overwrite audited login time
                            .peek(accountLoginAudit -> accountLoginAudit.setLoginTime(date)))
                            .forEach(accountLoginAuditRepository::save);
                }
            };
        };
        // Populate all accounts with login audit from year 2010 to year 2015
        // of all months, of all days, every midnight, noon time, and a moment
        // before midnight
        ZoneId systemZoneId = ZoneId.systemDefault();
        Stream.of(Year.of(2010), Year.of(2011), Year.of(2012), Year.of(2013), Year.of(2014), Year.of(2015))
                .flatMap(year -> Arrays.stream(Month.values()).map(year::atMonth))
                .flatMap(yearMonth -> IntStream.rangeClosed(1, 31).filter(yearMonth::isValidDay).mapToObj(yearMonth::atDay))
                .map(localDate -> Stream.of(LocalTime.MIDNIGHT, LocalTime.NOON, LocalTime.MAX).map(localDate::atTime))
                .map(localDateTimes -> localDateTimes.map(localDateTime -> localDateTime.atZone(systemZoneId)))
                .map(zoneDateTimes -> zoneDateTimes.map(ZonedDateTime::toInstant))
                .map(instants -> instants.map(Date::from))
                .map(batchSave::apply)
                .parallel()
                .forEach(transactionTemplate::execute);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void populateAccountClientGrantedAuthority() {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                accountClientGrantedAuthorityRepository.save(Stream.of("ROLE_WEB_APP", "ROLE_MOBILE_APP", "ROLE_ADMIN")
                        .map(AccountClientGrantedAuthority::new)
                        .peek(grantedAuthority -> grantedAuthority.setCreator("ANONYMOUS"))
                        .collect(Collectors.toSet()));
            }
        });
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void populateAccountClientDetailsService() {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
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
                registeredRedirectUris.add("http://172.17.0.1");
                registeredRedirectUris.add("https://172.17.0.1");

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
        });
    }

}
