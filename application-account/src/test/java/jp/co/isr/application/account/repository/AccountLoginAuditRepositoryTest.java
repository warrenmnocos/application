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
package jp.co.isr.application.account.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.stream.Stream;
import jp.co.isr.application.account.model.entity.Account;
import jp.co.isr.application.account.model.entity.AccountLoginAudit;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * This is the test class for {@link AccountLoginAuditRepository}.
 *
 * @author Warren Nocos
 * @since 1.0
 * @version 1.0
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class AccountLoginAuditRepositoryTest {

    @Autowired
    protected AccountRepository accountRepository;

    @Autowired
    protected AccountLoginAuditRepository accountLoginAuditRepository;

    protected final String[] accountsAsText;

    protected final Date[] loginDates;

    public AccountLoginAuditRepositoryTest() {
        accountsAsText = new String[]{
            // Same first name, middle name, and last name
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
            "warrenwevicknocos@gmail.com,Warren,Wevick,Nocos,ANONYMOUS"};
        loginDates = new Date[30];
        for (int index = 0; index < loginDates.length; index++) {
            loginDates[index] = Date.from(ZonedDateTime.of(
                    LocalDate.of(2016, Month.SEPTEMBER, index + 1),
                    LocalTime.MIN, ZoneId.systemDefault()).toInstant());
        }
    }

    /**
     * Populate {@link Account} repository.
     */
    protected void populateAccount() {
        Stream.of(accountsAsText)
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
     * Populate {@link AccountLoginAudit} repository.
     */
    protected void populateAccountLoginAudits() {
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

    @Test
    public void findByFiltersFirstNamesMiddleNamesLastNamesPositiveTest() {
        populateAccount();
        populateAccountLoginAudits();
        Collection<String> firstNames = Arrays.asList("Warren", "Prex"),
                middleNames = Arrays.asList("Lo", "Quiza"),
                lastNames = Arrays.asList("Nocos", "Antonio");
        long size = accountLoginAuditRepository.findByFilters(
                loginDates[0],
                loginDates[loginDates.length - 1],
                Collections.emptyList(),
                firstNames,
                middleNames,
                lastNames,
                new PageRequest(0, Integer.MAX_VALUE)).size();
        // Always correct
        long correctSize = accountLoginAuditRepository.findAll()
                .parallelStream()
                .filter(accountLoginAudit -> {
                    return accountLoginAudit.getLoginTime().equals(loginDates[0])
                            || accountLoginAudit.getLoginTime().after(loginDates[0]);
                })
                .filter(accountLoginAudit -> {
                    return accountLoginAudit.getLoginTime().equals(loginDates[loginDates.length - 1])
                            || accountLoginAudit.getLoginTime().before(loginDates[loginDates.length - 1]);
                })
                .filter(accountLoginAudit -> firstNames.contains(accountLoginAudit.getAccount().getFirstName()))
                .filter(accountLoginAudit -> middleNames.contains(accountLoginAudit.getAccount().getMiddleName()))
                .filter(accountLoginAudit -> lastNames.contains(accountLoginAudit.getAccount().getLastName()))
                .count();
        Assertions.assertThat(size).isEqualTo(correctSize);
    }

    @Test
    public void findByFiltersFirstNamesMiddleNamesPositiveTest() {
        populateAccount();
        populateAccountLoginAudits();
        Collection<String> firstNames = Arrays.asList("Warren", "Prex"),
                middleNames = Arrays.asList("Lo", "Quiza");
        long size = accountLoginAuditRepository.findByFilters(
                loginDates[0],
                loginDates[loginDates.length - 1],
                Collections.emptyList(),
                firstNames,
                middleNames,
                Collections.emptyList(),
                new PageRequest(0, Integer.MAX_VALUE)).size();
        // Always correct
        long correctSize = accountLoginAuditRepository.findAll()
                .parallelStream()
                .filter(accountLoginAudit -> {
                    return accountLoginAudit.getLoginTime().equals(loginDates[0])
                            || accountLoginAudit.getLoginTime().after(loginDates[0]);
                })
                .filter(accountLoginAudit -> {
                    return accountLoginAudit.getLoginTime().equals(loginDates[loginDates.length - 1])
                            || accountLoginAudit.getLoginTime().before(loginDates[loginDates.length - 1]);
                })
                .filter(accountLoginAudit -> firstNames.contains(accountLoginAudit.getAccount().getFirstName()))
                .filter(accountLoginAudit -> middleNames.contains(accountLoginAudit.getAccount().getMiddleName()))
                .count();
        Assertions.assertThat(size).isEqualTo(correctSize);
    }

    @Test
    public void findByFiltersFirstNamesLastNamesPositiveTest() {
        populateAccount();
        populateAccountLoginAudits();
        Collection<String> firstNames = Arrays.asList("Warren", "Prex"),
                lastNames = Arrays.asList("Nocos", "Antonio");
        long size = accountLoginAuditRepository.findByFilters(
                loginDates[0],
                loginDates[loginDates.length - 1],
                Collections.emptyList(),
                firstNames,
                Collections.emptyList(),
                lastNames,
                new PageRequest(0, Integer.MAX_VALUE)).size();
        // Always correct
        long correctSize = accountLoginAuditRepository.findAll()
                .parallelStream()
                .filter(accountLoginAudit -> {
                    return accountLoginAudit.getLoginTime().equals(loginDates[0])
                            || accountLoginAudit.getLoginTime().after(loginDates[0]);
                })
                .filter(accountLoginAudit -> {
                    return accountLoginAudit.getLoginTime().equals(loginDates[loginDates.length - 1])
                            || accountLoginAudit.getLoginTime().before(loginDates[loginDates.length - 1]);
                })
                .filter(accountLoginAudit -> firstNames.contains(accountLoginAudit.getAccount().getFirstName()))
                .filter(accountLoginAudit -> lastNames.contains(accountLoginAudit.getAccount().getLastName()))
                .count();
        Assertions.assertThat(size).isEqualTo(correctSize);
    }

    @Test
    public void findByFiltersMiddleNamesLastNamesPositiveTest() {
        populateAccount();
        populateAccountLoginAudits();
        Collection<String> middleNames = Arrays.asList("Lo", "Quiza"),
                lastNames = Arrays.asList("Nocos", "Antonio");
        long size = accountLoginAuditRepository.findByFilters(
                loginDates[0],
                loginDates[loginDates.length - 1],
                Collections.emptyList(),
                Collections.emptyList(),
                middleNames,
                lastNames,
                new PageRequest(0, Integer.MAX_VALUE)).size();
        // Always correct
        long correctSize = accountLoginAuditRepository.findAll()
                .parallelStream()
                .filter(accountLoginAudit -> {
                    return accountLoginAudit.getLoginTime().equals(loginDates[0])
                            || accountLoginAudit.getLoginTime().after(loginDates[0]);
                })
                .filter(accountLoginAudit -> {
                    return accountLoginAudit.getLoginTime().equals(loginDates[loginDates.length - 1])
                            || accountLoginAudit.getLoginTime().before(loginDates[loginDates.length - 1]);
                })
                .filter(accountLoginAudit -> middleNames.contains(accountLoginAudit.getAccount().getMiddleName()))
                .filter(accountLoginAudit -> lastNames.contains(accountLoginAudit.getAccount().getLastName()))
                .count();
        Assertions.assertThat(size).isEqualTo(correctSize);
    }

    @Test
    public void findByFiltersEmailsPositiveTest() {
        populateAccount();
        populateAccountLoginAudits();
        Collection<String> emails = Arrays.asList("warrenlosa@gmail.com", "war@gmail.com", "warrenloiusnocos@gmail.com", "warrenrocoantonio@gmail.com");
        long size = accountLoginAuditRepository.findByFilters(
                loginDates[0],
                loginDates[loginDates.length - 1],
                emails,
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                new PageRequest(0, Integer.MAX_VALUE)).size();
        // Always correct
        long correctSize = accountLoginAuditRepository.findAll()
                .parallelStream()
                .filter(accountLoginAudit -> {
                    return accountLoginAudit.getLoginTime().equals(loginDates[0])
                            || accountLoginAudit.getLoginTime().after(loginDates[0]);
                })
                .filter(accountLoginAudit -> {
                    return accountLoginAudit.getLoginTime().equals(loginDates[loginDates.length - 1])
                            || accountLoginAudit.getLoginTime().before(loginDates[loginDates.length - 1]);
                })
                .filter(accountLoginAudit -> emails.contains(accountLoginAudit.getAccount().getEmail()))
                .count();
        Assertions.assertThat(size).isEqualTo(correctSize);
    }

    @Test
    public void findByFiltersFirstNamesPositiveTest() {
        populateAccount();
        populateAccountLoginAudits();
        Collection<String> firstNames = Arrays.asList("Warren", "Ana");
        long size = accountLoginAuditRepository.findByFilters(
                loginDates[0],
                loginDates[loginDates.length - 1],
                Collections.emptyList(),
                firstNames,
                Collections.emptyList(),
                Collections.emptyList(),
                new PageRequest(0, Integer.MAX_VALUE)).size();
        // Always correct
        long correctSize = accountLoginAuditRepository.findAll()
                .parallelStream()
                .filter(accountLoginAudit -> {
                    return accountLoginAudit.getLoginTime().equals(loginDates[0])
                            || accountLoginAudit.getLoginTime().after(loginDates[0]);
                })
                .filter(accountLoginAudit -> {
                    return accountLoginAudit.getLoginTime().equals(loginDates[loginDates.length - 1])
                            || accountLoginAudit.getLoginTime().before(loginDates[loginDates.length - 1]);
                })
                .filter(accountLoginAudit -> firstNames.contains(accountLoginAudit.getAccount().getFirstName()))
                .count();
        Assertions.assertThat(size).isEqualTo(correctSize);
    }

    @Test
    public void findByFiltersEmailsFirstNamesPositiveTest() {
        populateAccount();
        populateAccountLoginAudits();
        Collection<String> emails = Arrays.asList("warrenlosa@gmail.com", "war@gmail.com", "warrenloiusnocos@gmail.com", "warrenrocoantonio@gmail.com");
        Collection<String> firstNames = Arrays.asList("Warren", "Ana");
        long size = accountLoginAuditRepository.findByFilters(
                loginDates[0],
                loginDates[loginDates.length - 1],
                emails,
                firstNames,
                Collections.emptyList(),
                Collections.emptyList(),
                new PageRequest(0, Integer.MAX_VALUE)
        ).size();
        // Always correct
        long correctSize = accountLoginAuditRepository.findAll()
                .parallelStream()
                .filter(accountLoginAudit -> {
                    return accountLoginAudit.getLoginTime().equals(loginDates[0])
                            || accountLoginAudit.getLoginTime().after(loginDates[0]);
                })
                .filter(accountLoginAudit -> {
                    return accountLoginAudit.getLoginTime().equals(loginDates[loginDates.length - 1])
                            || accountLoginAudit.getLoginTime().before(loginDates[loginDates.length - 1]);
                })
                .filter(accountLoginAudit -> emails.contains(accountLoginAudit.getAccount().getEmail()))
                .filter(accountLoginAudit -> firstNames.contains(accountLoginAudit.getAccount().getFirstName()))
                .count();
        Assertions.assertThat(size).isEqualTo(correctSize);
    }

    @Test
    public void findByFiltersLastNamePositiveTest() {
        populateAccount();
        populateAccountLoginAudits();
        Collection<String> lastNames = Arrays.asList("Nocos", "Prex");
        long size = accountLoginAuditRepository.findByFilters(
                loginDates[0],
                loginDates[loginDates.length - 1],
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                lastNames,
                new PageRequest(0, Integer.MAX_VALUE)).size();
        // Always correct
        long correctSize = accountLoginAuditRepository.findAll()
                .parallelStream()
                .filter(accountLoginAudit -> {
                    return accountLoginAudit.getLoginTime().equals(loginDates[0])
                            || accountLoginAudit.getLoginTime().after(loginDates[0]);
                })
                .filter(accountLoginAudit -> {
                    return accountLoginAudit.getLoginTime().equals(loginDates[loginDates.length - 1])
                            || accountLoginAudit.getLoginTime().before(loginDates[loginDates.length - 1]);
                })
                .filter(accountLoginAudit -> lastNames.contains(accountLoginAudit.getAccount().getLastName()))
                .count();
        Assertions.assertThat(size).isEqualTo(correctSize);
    }

    @Test
    public void findByFiltersEmailsFirstNamesLastNamesPositiveTest() {
        populateAccount();
        populateAccountLoginAudits();
        Collection<String> emails = Arrays.asList("warrenlosa@gmail.com", "war@gmail.com", "warrenloiusnocos@gmail.com", "warrenrocoantonio@gmail.com");
        Collection<String> firstNames = Arrays.asList("Warren", "Ana");
        Collection<String> lastNames = Arrays.asList("Nocos", "Prex");
        long size = accountLoginAuditRepository.findByFilters(
                loginDates[0],
                loginDates[loginDates.length - 1],
                emails,
                firstNames,
                Collections.emptyList(),
                lastNames,
                new PageRequest(0, Integer.MAX_VALUE)).size();
        // Always correct
        long correctSize = accountLoginAuditRepository.findAll()
                .parallelStream()
                .filter(accountLoginAudit -> {
                    return accountLoginAudit.getLoginTime().equals(loginDates[0])
                            || accountLoginAudit.getLoginTime().after(loginDates[0]);
                })
                .filter(accountLoginAudit -> {
                    return accountLoginAudit.getLoginTime().equals(loginDates[loginDates.length - 1])
                            || accountLoginAudit.getLoginTime().before(loginDates[loginDates.length - 1]);
                })
                .filter(accountLoginAudit -> emails.contains(accountLoginAudit.getAccount().getEmail()))
                .filter(accountLoginAudit -> firstNames.contains(accountLoginAudit.getAccount().getFirstName()))
                .filter(accountLoginAudit -> lastNames.contains(accountLoginAudit.getAccount().getLastName()))
                .count();
        Assertions.assertThat(size).isEqualTo(correctSize);
    }

    @Test
    public void findByFiltersMiddleNamePositiveTest() {
        populateAccount();
        populateAccountLoginAudits();
        Collection<String> middleNames = Arrays.asList("Lo", "Quiza");
        long size = accountLoginAuditRepository.findByFilters(
                loginDates[0],
                loginDates[loginDates.length - 1],
                Collections.emptyList(),
                Collections.emptyList(),
                middleNames,
                Collections.emptyList(),
                new PageRequest(0, Integer.MAX_VALUE)).size();
        // Always correct
        long correctSize = accountLoginAuditRepository.findAll()
                .parallelStream()
                .filter(accountLoginAudit -> {
                    return accountLoginAudit.getLoginTime().equals(loginDates[0])
                            || accountLoginAudit.getLoginTime().after(loginDates[0]);
                })
                .filter(accountLoginAudit -> {
                    return accountLoginAudit.getLoginTime().equals(loginDates[loginDates.length - 1])
                            || accountLoginAudit.getLoginTime().before(loginDates[loginDates.length - 1]);
                })
                .filter(accountLoginAudit -> middleNames.contains(accountLoginAudit.getAccount().getMiddleName()))
                .count();
        Assertions.assertThat(size).isEqualTo(correctSize);
    }

    @Test
    public void findByFiltersEmailsFirstNamesMiddleNamesLastNamesPositiveTest() {
        populateAccount();
        populateAccountLoginAudits();
        Collection<String> emails = Arrays.asList("warrenlosa@gmail.com", "war@gmail.com", "warrenloiusnocos@gmail.com", "warrenrocoantonio@gmail.com");
        Collection<String> firstNames = Arrays.asList("Warren", "Ana");
        Collection<String> middleNames = Arrays.asList("Lo", "Quiza");
        Collection<String> lastNames = Arrays.asList("Nocos", "Prex");
        long size = accountLoginAuditRepository.findByFilters(
                loginDates[0],
                loginDates[loginDates.length - 1],
                emails,
                firstNames,
                middleNames,
                lastNames,
                new PageRequest(0, Integer.MAX_VALUE)).size();
        // Always correct
        long correctSize = accountLoginAuditRepository.findAll()
                .parallelStream()
                .filter(accountLoginAudit -> {
                    return accountLoginAudit.getLoginTime().equals(loginDates[0])
                            || accountLoginAudit.getLoginTime().after(loginDates[0]);
                })
                .filter(accountLoginAudit -> {
                    return accountLoginAudit.getLoginTime().equals(loginDates[loginDates.length - 1])
                            || accountLoginAudit.getLoginTime().before(loginDates[loginDates.length - 1]);
                })
                .filter(accountLoginAudit -> emails.contains(accountLoginAudit.getAccount().getEmail()))
                .filter(accountLoginAudit -> firstNames.contains(accountLoginAudit.getAccount().getFirstName()))
                .filter(accountLoginAudit -> middleNames.contains(accountLoginAudit.getAccount().getMiddleName()))
                .filter(accountLoginAudit -> lastNames.contains(accountLoginAudit.getAccount().getLastName()))
                .count();
        Assertions.assertThat(size).isEqualTo(correctSize);
    }

}
