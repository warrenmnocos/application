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

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Stream;
import javax.validation.ConstraintViolationException;
import jp.co.isr.application.account.model.entity.Account;
import jp.co.isr.application.account.model.entity.Address;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * This is the test class for {@link AccountRepository}.
 *
 * @author Warren Nocos
 * @since 1.0
 * @version 1.0
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class AccountRepositoryTest {

    @Autowired
    protected AccountRepository accountRepository;

    @Test
    @Transactional
    public void saveAccountPositiveTest() {
        Account account = new Account();
        account.setEmail("warrenmnocos@gmail.com");
        account.setFirstName("Warren");
        account.setMiddleName("Manlangit");
        account.setLastName("Nocos");
        account.setCreator("ANONYMOUS");
        Address address = new Address();
        address.setCity("Cebu");
        address.setCountry("Philippines");
        address.setTown("NA");
        address.setZipCode("6000");
        account.setAddresses(Collections.singletonMap("home", address));
        account.setContacts(Collections.singletonMap("mobile", "03123456789"));
        accountRepository.save(account);
        Optional<Account> accountFromRepository = accountRepository.findById(account.getId());
        Assertions.assertThat(accountFromRepository).isNotNull();
        Assertions.assertThat(accountFromRepository.get()).isNotNull();
        Assertions.assertThat(accountFromRepository.get().getId()).isEqualTo(account.getId());
        Assertions.assertThat(accountFromRepository.get().getEmail()).isEqualTo(account.getEmail());
        Assertions.assertThat(accountFromRepository.get().getFirstName()).isEqualTo(account.getFirstName());
        Assertions.assertThat(accountFromRepository.get().getMiddleName()).isEqualTo(account.getMiddleName());
        Assertions.assertThat(accountFromRepository.get().getLastName()).isEqualTo(account.getLastName());
        Assertions.assertThat(accountFromRepository.get().getCreator()).isEqualTo(account.getCreator());
        Assertions.assertThat(accountFromRepository.get().getAddresses()).isNotNull();
        Assertions.assertThat(accountFromRepository.get().getAddresses()).isNotEmpty();
        Assertions.assertThat(accountFromRepository.get().getAddresses()).hasSameSizeAs(account.getAddresses());
        Assertions.assertThat(accountFromRepository.get().getAddresses()).containsAllEntriesOf(account.getAddresses());
        Assertions.assertThat(accountFromRepository.get().getContacts()).isNotNull();
        Assertions.assertThat(accountFromRepository.get().getContacts()).isNotEmpty();
        Assertions.assertThat(accountFromRepository.get().getContacts()).hasSameSizeAs(account.getContacts());
        Assertions.assertThat(accountFromRepository.get().getContacts()).containsAllEntriesOf(account.getContacts());
    }

    @Test(expected = ConstraintViolationException.class)
    @Transactional
    public void saveAccountNegativeEmailConstraintViolationTest() {
        Account account = new Account();
        account.setFirstName("Warren");
        account.setMiddleName("Manlangit");
        account.setLastName("Nocos");
        account.setCreator("ANONYMOUS");
        Address address = new Address();
        address.setCity("Cebu");
        address.setCountry("Philippines");
        address.setTown("NA");
        address.setZipCode("6000");
        account.setAddresses(Collections.singletonMap("home", address));
        account.setContacts(Collections.singletonMap("mobile", "03123456789"));
        accountRepository.save(account);
        accountRepository.findById(account.getId())
                .ifPresent(persistedAccount -> {
                    Assertions.assertThat(persistedAccount).isEqualTo(account);
                });
    }

    @Test(expected = ConstraintViolationException.class)
    @Transactional
    public void saveAccountNegativeFirstNameConstraintViolationTest() {
        Account account = new Account();
        account.setEmail("warrenmnocos@gmail.com");
        account.setMiddleName("Manlangit");
        account.setLastName("Nocos");
        account.setCreator("ANONYMOUS");
        Address address = new Address();
        address.setCity("Cebu");
        address.setCountry("Philippines");
        address.setTown("NA");
        address.setZipCode("6000");
        account.setAddresses(Collections.singletonMap("home", address));
        account.setContacts(Collections.singletonMap("mobile", "03123456789"));
        accountRepository.save(account);
        accountRepository.findById(account.getId())
                .ifPresent(persistedAccount -> {
                    Assertions.assertThat(persistedAccount).isEqualTo(account);
                });
    }

    @Test(expected = ConstraintViolationException.class)
    @Transactional
    public void saveAccountNegativeLastNameConstraintViolationTest() {
        Account account = new Account();
        account.setEmail("warrenmnocos@gmail.com");
        account.setFirstName("Warren");
        account.setMiddleName("Manlangit");
        Address address = new Address();
        address.setCity("Cebu");
        address.setCountry("Philippines");
        address.setTown("NA");
        address.setZipCode("6000");
        account.setAddresses(Collections.singletonMap("home", address));
        account.setContacts(Collections.singletonMap("mobile", "03123456789"));
        accountRepository.save(account);
        accountRepository.findById(account.getId())
                .ifPresent(persistedAccount -> {
                    Assertions.assertThat(persistedAccount).isEqualTo(account);
                });
    }

    @Test(expected = ConstraintViolationException.class)
    @Transactional
    public void saveAccountNegativeCreatorConstraintViolationTest() {
        Account account = new Account();
        account.setEmail("warrenmnocos@gmail.com");
        account.setFirstName("Warren");
        account.setMiddleName("Manlangit");
        account.setCreator("ANONYMOUS");
        Address address = new Address();
        address.setCity("Cebu");
        address.setCountry("Philippines");
        address.setTown("NA");
        address.setZipCode("6000");
        account.setAddresses(Collections.singletonMap("home", address));
        account.setContacts(Collections.singletonMap("mobile", "03123456789"));
        accountRepository.save(account);
        accountRepository.findById(account.getId())
                .ifPresent(persistedAccount -> {
                    Assertions.assertThat(persistedAccount).isEqualTo(account);
                });
    }

    @Test
    @Transactional
    public void findAllAsStreamPositiveTest() {
        String[] accountsAsText = new String[]{
            "warrenmnocos@gmail.com,Warren,Manlangit,Nocos,ANONYMOUS",
            "karensume@gmail.com,Karen,Su,Me,ANONYMOUS",
            "ericcruzperez@gmail.com,Eric,Cruz,Perez,ANONYMOUS",
            "divinepenedasy@gmail.com,Divine,Peneda,Sy,ANONYMOUS",
            "miketanricafort@gmail.com,Mike,Tan,Ricafort,ANONYMOUS"
        };
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
        long accountsCount = accountRepository.findAllAsStream().count();
        Assertions.assertThat(accountsCount).isEqualTo(accountsAsText.length);
    }

    @Test
    @Transactional
    public void findAllAsStreamNegativeTest() {
        String[] accountsAsText = new String[]{
            "warrenmnocos@gmail.com,Warren,Manlangit,Nocos,ANONYMOUS",
            "karensume@gmail.com,Karen,Su,Me,ANONYMOUS",
            "ericcruzperez@gmail.com,Eric,Cruz,Perez,ANONYMOUS",
            "divinepenedasy@gmail.com,Divine,Peneda,Sy,ANONYMOUS",
            "miketanricafort@gmail.com,Mike,Tan,Ricafort,ANONYMOUS"
        };
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
        long accountsCount = accountRepository.findAllAsStream().count();
        Assertions.assertThat(accountsCount).isNotEqualTo(accountsAsText.length + 1);
    }

    @Test
    @Transactional
    public void findAllAsPagedStreamPositiveTest() {
        String[] accountsAsText = new String[]{
            "warrenmnocos@gmail.com,Warren,Manlangit,Nocos,ANONYMOUS",
            "karensume@gmail.com,Karen,Su,Me,ANONYMOUS",
            "ericcruzperez@gmail.com,Eric,Cruz,Perez,ANONYMOUS",
            "divinepenedasy@gmail.com,Divine,Peneda,Sy,ANONYMOUS",
            "miketanricafort@gmail.com,Mike,Tan,Ricafort,ANONYMOUS"
        };
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
        long accountsCount = accountRepository.findAllAsPagedStream(new PageRequest(0, 2)).count();
        Assertions.assertThat(accountsCount).isEqualTo(2);
        accountsCount = accountRepository.findAllAsPagedStream(new PageRequest(1, 2)).count();
        Assertions.assertThat(accountsCount).isEqualTo(2);
        accountsCount = accountRepository.findAllAsPagedStream(new PageRequest(2, 2)).count();
        Assertions.assertThat(accountsCount).isEqualTo(1);
        accountsCount = accountRepository.findAllAsPagedStream(new PageRequest(3, 2)).count();
        Assertions.assertThat(accountsCount).isEqualTo(0);
    }

    @Test
    @Transactional
    public void findAllAsPagedStreamNegativeTest() {
        String[] accountsAsText = new String[]{
            "warrenmnocos@gmail.com,Warren,Manlangit,Nocos,ANONYMOUS",
            "karensume@gmail.com,Karen,Su,Me,ANONYMOUS",
            "ericcruzperez@gmail.com,Eric,Cruz,Perez,ANONYMOUS",
            "divinepenedasy@gmail.com,Divine,Peneda,Sy,ANONYMOUS",
            "miketanricafort@gmail.com,Mike,Tan,Ricafort,ANONYMOUS"
        };
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
        long accountsCount = accountRepository.findAllAsPagedStream(new PageRequest(0, 2)).count();
        Assertions.assertThat(accountsCount).isNotEqualTo(accountsAsText.length);
        accountsCount = accountRepository.findAllAsPagedStream(new PageRequest(1, 2)).count();
        Assertions.assertThat(accountsCount).isNotEqualTo(accountsAsText.length);
        accountsCount = accountRepository.findAllAsPagedStream(new PageRequest(2, 2)).count();
        Assertions.assertThat(accountsCount).isNotEqualTo(0);
    }

    @Test
    @Transactional
    public void findByEmailPositiveTest() {
        Account account = new Account();
        account.setEmail("warrenmnocos@gmail.com");
        account.setFirstName("Warren");
        account.setMiddleName("Manlangit");
        account.setLastName("Nocos");
        account.setCreator("ANONYMOUS");
        Address address = new Address();
        address.setCity("Cebu");
        address.setCountry("Philippines");
        address.setTown("NA");
        address.setZipCode("6000");
        account.setAddresses(Collections.singletonMap("home", address));
        account.setContacts(Collections.singletonMap("mobile", "03123456789"));
        accountRepository.save(account);
        Optional<Account> accountFromRepository = accountRepository.findByEmail(account.getEmail());
        Assertions.assertThat(accountFromRepository).isNotNull();
        Assertions.assertThat(accountFromRepository.isPresent()).isTrue();
        Assertions.assertThat(accountFromRepository.get()).isNotNull();
        Assertions.assertThat(accountFromRepository.get().getEmail()).isEqualTo(account.getEmail());
    }

    @Test
    @Transactional
    public void findByEmailNegativeTest() {
        Account account = new Account();
        account.setEmail("warrenmnocos@gmail.com");
        account.setFirstName("Warren");
        account.setMiddleName("Manlangit");
        account.setLastName("Nocos");
        account.setCreator("ANONYMOUS");
        Address address = new Address();
        address.setCity("Cebu");
        address.setCountry("Philippines");
        address.setTown("NA");
        address.setZipCode("6000");
        account.setAddresses(Collections.singletonMap("home", address));
        account.setContacts(Collections.singletonMap("mobile", "03123456789"));
        accountRepository.save(account);
        Optional<Account> accountFromRepository = accountRepository.findByEmail("xxxx@xxxxx.xx");
        Assertions.assertThat(accountFromRepository).isNotNull();
        Assertions.assertThat(accountFromRepository.isPresent()).isFalse();
    }

    @Test
    @Transactional
    public void findByIdPositiveTest() {
        Account account = new Account();
        account.setEmail("warrenmnocos@gmail.com");
        account.setFirstName("Warren");
        account.setMiddleName("Manlangit");
        account.setLastName("Nocos");
        account.setCreator("ANONYMOUS");
        Address address = new Address();
        address.setCity("Cebu");
        address.setCountry("Philippines");
        address.setTown("NA");
        address.setZipCode("6000");
        account.setAddresses(Collections.singletonMap("home", address));
        account.setContacts(Collections.singletonMap("mobile", "03123456789"));
        accountRepository.save(account);
        Optional<Account> accountFromRepository = accountRepository.findById(account.getId());
        Assertions.assertThat(accountFromRepository).isNotNull();
        Assertions.assertThat(accountFromRepository.isPresent()).isTrue();
        Assertions.assertThat(accountFromRepository.get()).isNotNull();
        Assertions.assertThat(accountFromRepository.get().getId()).isEqualTo(account.getId());
    }

    @Test
    @Transactional
    public void findByIdNegativeTest() {
        Account account = new Account();
        account.setEmail("warrenmnocos@gmail.com");
        account.setFirstName("Warren");
        account.setMiddleName("Manlangit");
        account.setLastName("Nocos");
        account.setCreator("ANONYMOUS");
        Address address = new Address();
        address.setCity("Cebu");
        address.setCountry("Philippines");
        address.setTown("NA");
        address.setZipCode("6000");
        account.setAddresses(Collections.singletonMap("home", address));
        account.setContacts(Collections.singletonMap("mobile", "03123456789"));
        accountRepository.save(account);
        Optional<Account> accountFromRepository = accountRepository.findById(account.getId() + 1);
        Assertions.assertThat(accountFromRepository).isNotNull();
        Assertions.assertThat(accountFromRepository.isPresent()).isFalse();
    }

}
