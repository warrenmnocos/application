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
package jp.co.isr.application.account.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import jp.co.isr.application.account.model.dto.AccountDto;
import jp.co.isr.application.account.model.entity.Account;
import jp.co.isr.application.account.model.entity.AccountLoginAudit;
import jp.co.isr.application.account.repository.AccountLoginAuditRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * This is the test class for {@link AccountLoginAuditService}. This tests
 * business logic.
 *
 * @author Warren Nocos
 * @since 1.0
 * @version 1.0
 */
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
public class AccountLoginAuditServiceTest {

    /**
     * The repository that backs {@link AccountLoginAuditService}.
     */
    @MockBean
    protected AccountLoginAuditRepository accountLoginAuditRepository;

    /**
     * The service to be tested.
     */
    @Autowired
    @Qualifier("accountLoginAuditServiceImpl")
    protected AccountLoginAuditService accountLoginAuditService;

    @Autowired
    protected AccountModelService accountModelService;

    protected final Collection<LocalDate> localDates;

    public AccountLoginAuditServiceTest() {
        this.localDates = Collections.unmodifiableCollection(Arrays.asList(LocalDate.of(2015, Month.FEBRUARY, 1),
                LocalDate.of(2015, Month.MARCH, 1), LocalDate.of(2015, Month.APRIL, 1),
                LocalDate.of(2015, Month.MAY, 1), LocalDate.of(2015, Month.JUNE, 1),
                LocalDate.of(2015, Month.JULY, 1), LocalDate.of(2015, Month.AUGUST, 1),
                LocalDate.of(2015, Month.SEPTEMBER, 1), LocalDate.of(2015, Month.OCTOBER, 1)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void findDatesWithLoginActivityAscendinglyPositiveTest() {
        ZoneId defaultZoneId = ZoneId.systemDefault();
        Stream<Date> dates = localDates.stream()
                .map(localDate -> localDate.atTime(LocalTime.MIN))
                .map(localDateTime -> localDateTime.atZone(defaultZoneId))
                .map(ZonedDateTime::toInstant)
                .map(Date::from)
                .sorted();
        BDDMockito.given(accountLoginAuditRepository.findAllLoginDate(new PageRequest(0, Integer.MAX_VALUE)))
                .willReturn(dates);
        Optional<Integer> nullInteger = Optional.ofNullable(null);
        Collection<LocalDate> resultDates = accountLoginAuditService.findDatesWithLoginActivityAscendingly(nullInteger, nullInteger);
        Assertions.assertThat(resultDates).containsExactlyElementsOf(localDates);
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(roles = "USER")
    public void findDatesWithLoginActivityAscendinglySecurityNotAdminNegativeTest() {
        Optional<Integer> nullInteger = Optional.ofNullable(null);
        accountLoginAuditService.findDatesWithLoginActivityAscendingly(nullInteger, nullInteger);
    }

    @Test(expected = AuthenticationException.class)
    public void findDatesWithLoginActivityAscendinglySecurityNoRoleNegativeTest() {
        Optional<Integer> nullInteger = Optional.ofNullable(null);
        accountLoginAuditService.findDatesWithLoginActivityAscendingly(nullInteger, nullInteger);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void findDatesWithLoginActivityAscendinglyPaginationPositiveTest() {
        ZoneId defaultZoneId = ZoneId.systemDefault();
        Collection<Date> firstFiveDates = localDates.stream()
                .map(localDate -> localDate.atTime(LocalTime.MIN))
                .map(localDateTime -> localDateTime.atZone(defaultZoneId))
                .map(ZonedDateTime::toInstant)
                .map(Date::from)
                .sorted()
                .limit(5)
                .collect(Collectors.toCollection(TreeSet::new));
        BDDMockito.given(accountLoginAuditRepository.findAllLoginDate(new PageRequest(0, 5)))
                .willReturn(firstFiveDates.stream());
        Optional<Integer> zero = Optional.of(0);
        Optional<Integer> one = Optional.of(1);
        Optional<Integer> five = Optional.of(5);
        Collection<Date> resultDates = accountLoginAuditService.findDatesWithLoginActivityAscendingly(zero, five)
                .stream()
                .map(localDate -> localDate.atTime(LocalTime.MIN))
                .map(localDateTime -> localDateTime.atZone(defaultZoneId))
                .map(ZonedDateTime::toInstant)
                .map(Date::from)
                .sorted()
                .collect(Collectors.toCollection(TreeSet::new));
        Assertions.assertThat(resultDates)
                .containsExactlyElementsOf(firstFiveDates);
        Collection<Date> lastFiveDates = localDates.stream()
                .map(localDate -> localDate.atTime(LocalTime.MIN))
                .map(localDateTime -> localDateTime.atZone(defaultZoneId))
                .map(ZonedDateTime::toInstant)
                .map(Date::from)
                .filter(date -> !firstFiveDates.contains(date))
                .collect(Collectors.toCollection(TreeSet::new));
        BDDMockito.given(accountLoginAuditRepository.findAllLoginDate(new PageRequest(1, 5)))
                .willReturn(lastFiveDates.stream());
        resultDates = accountLoginAuditService.findDatesWithLoginActivityAscendingly(one, five)
                .stream()
                .map(localDate -> localDate.atTime(LocalTime.MIN))
                .map(localDateTime -> localDateTime.atZone(defaultZoneId))
                .map(ZonedDateTime::toInstant)
                .map(Date::from)
                .collect(Collectors.toCollection(TreeSet::new));
        Assertions.assertThat(resultDates)
                .containsExactlyElementsOf(lastFiveDates);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void findDatesWithLoginActivityAscendinglyPaginationWrongReturnNegativeTest() {
        ZoneId defaultZoneId = ZoneId.systemDefault();
        Collection<Date> firstFiveDates = localDates.stream()
                .map(localDate -> localDate.atTime(LocalTime.MIN))
                .map(localDateTime -> localDateTime.atZone(defaultZoneId))
                .map(ZonedDateTime::toInstant)
                .map(Date::from)
                .sorted()
                .limit(5)
                .collect(Collectors.toCollection(TreeSet::new));
        BDDMockito.given(accountLoginAuditRepository.findAllLoginDate(new PageRequest(0, 5)))
                .willReturn(firstFiveDates.stream());
        Collection<Date> lastFiveDates = localDates.stream()
                .map(localDate -> localDate.atTime(LocalTime.MIN))
                .map(localDateTime -> localDateTime.atZone(defaultZoneId))
                .map(ZonedDateTime::toInstant)
                .map(Date::from)
                .filter(date -> !firstFiveDates.contains(date))
                .collect(Collectors.toCollection(TreeSet::new));
        BDDMockito.given(accountLoginAuditRepository.findAllLoginDate(new PageRequest(1, 5)))
                .willReturn(lastFiveDates.stream());
        Optional<Integer> zero = Optional.of(0);
        Optional<Integer> one = Optional.of(1);
        Optional<Integer> five = Optional.of(5);
        Collection<Date> resultDates = accountLoginAuditService.findDatesWithLoginActivityAscendingly(zero, five)
                .stream()
                .map(localDate -> localDate.atTime(LocalTime.MIN))
                .map(localDateTime -> localDateTime.atZone(defaultZoneId))
                .map(ZonedDateTime::toInstant)
                .map(Date::from)
                .sorted()
                .collect(Collectors.toCollection(TreeSet::new));
        Assertions.assertThat(resultDates)
                .doesNotContainAnyElementsOf(lastFiveDates);
        resultDates = accountLoginAuditService.findDatesWithLoginActivityAscendingly(one, five)
                .stream()
                .map(localDate -> localDate.atTime(LocalTime.MIN))
                .map(localDateTime -> localDateTime.atZone(defaultZoneId))
                .map(ZonedDateTime::toInstant)
                .map(Date::from)
                .collect(Collectors.toCollection(TreeSet::new));
        Assertions.assertThat(resultDates)
                .doesNotContainAnyElementsOf(firstFiveDates);
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(roles = "USER")
    public void findDatesWithLoginActivityAscendinglyPaginationSecurityNotAdminNegativeTest() {
        Optional<Integer> nullInteger = Optional.ofNullable(null);
        accountLoginAuditService.findDatesWithLoginActivityAscendingly(nullInteger, nullInteger);
    }

    @Test(expected = AuthenticationException.class)
    public void findDatesWithLoginActivityAscendinglyPaginationSecurityNoRoleNegativeTest() {
        Optional<Integer> nullInteger = Optional.ofNullable(null);
        accountLoginAuditService.findDatesWithLoginActivityAscendingly(nullInteger, nullInteger);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void findAccountsWithLoginActivityAscendinglyPositiveTest() {
        Optional<LocalDate> start = Optional.ofNullable(LocalDate.of(2010, Month.MARCH, 18)),
                end = Optional.ofNullable(LocalDate.of(2010, Month.MARCH, 18));
        ZoneId systemZoneId = ZoneId.systemDefault();
        Date from = Date.from(ZonedDateTime.of(start.get(), LocalTime.MIN, systemZoneId).toInstant());
        Date to = Date.from(ZonedDateTime.of(end.get(), LocalTime.MAX, systemZoneId).toInstant());
        String[] accountsAsText = new String[]{
            "warrenmnocos@gmail.com,Warren,Manlangit,Nocos,ANONYMOUS",
            "karensume@gmail.com,Karen,Su,Me,ANONYMOUS",
            "ericcruzperez@gmail.com,Eric,Cruz,Perez,ANONYMOUS",
            "divinepenedasy@gmail.com,Divine,Peneda,Sy,ANONYMOUS",
            "miketanricafort@gmail.com,Mike,Tan,Ricafort,ANONYMOUS"
        };
        Collection<Account> accounts = Stream.of(accountsAsText)
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
                .collect(Collectors.toCollection(TreeSet::new));
        Iterable<AccountDto> correctAccountDtos = accounts.parallelStream()
                .map(accountModelService::toAccountDto)
                .collect(Collectors.toCollection(TreeSet::new));
        BDDMockito.given(accountLoginAuditRepository.findAccountsBetweenLoginTimeInclusive(
                from, to, new PageRequest(0, Integer.MAX_VALUE))).willReturn(accounts.stream());
        Optional<Integer> nullInteger = Optional.ofNullable(null);
        Collection<AccountDto> accountDtos = accountLoginAuditService.findAccountsWithLoginActivityAscendingly(
                start, end, nullInteger, nullInteger);
        Assertions.assertThat(accountDtos).containsExactlyElementsOf(correctAccountDtos);
    }

    @Test(expected = IllegalArgumentException.class)
    @WithMockUser(roles = "ADMIN")
    public void findAccountsWithLoginActivityAscendinglyWrongDatesNegativeTest() {
        Optional<LocalDate> start = Optional.ofNullable(LocalDate.of(2010, Month.MARCH, 18)),
                end = Optional.ofNullable(LocalDate.of(2014, Month.MARCH, 18));
        ZoneId systemZoneId = ZoneId.systemDefault();
        Date from = Date.from(ZonedDateTime.of(start.get(), LocalTime.MIN, systemZoneId).toInstant());
        Date to = Date.from(ZonedDateTime.of(end.get(), LocalTime.MAX, systemZoneId).toInstant());
        BDDMockito.given(accountLoginAuditRepository.findAccountsBetweenLoginTimeInclusive(
                from, to, new PageRequest(0, Integer.MAX_VALUE))).willReturn(Stream.of());
        Optional<Integer> nullInteger = Optional.ofNullable(null);
        accountLoginAuditService.findAccountsWithLoginActivityAscendingly(
                end, start, nullInteger, nullInteger);
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(roles = "USER")
    public void findAccountsWithLoginActivityAscendinglySecurityNotAdminNegativeTest() {
        Optional<LocalDate> start = Optional.ofNullable(LocalDate.of(2010, Month.MARCH, 18)),
                end = Optional.ofNullable(LocalDate.of(2010, Month.MARCH, 18));;
        Optional<Integer> nullInteger = Optional.ofNullable(null);
        accountLoginAuditService.findAccountsWithLoginActivityAscendingly(
                start, end, nullInteger, nullInteger);
    }

    @Test(expected = AuthenticationException.class)
    public void findAccountsWithLoginActivityAscendinglySecurityNoRoleNegativeTest() {
        Optional<LocalDate> start = Optional.ofNullable(LocalDate.of(2010, Month.MARCH, 18)),
                end = Optional.ofNullable(LocalDate.of(2010, Month.MARCH, 18));;
        Optional<Integer> nullInteger = Optional.ofNullable(null);
        accountLoginAuditService.findAccountsWithLoginActivityAscendingly(
                start, end, nullInteger, nullInteger);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void findAccountsWithLoginActivityAscendinglyPaginationPositiveTest() {
        Optional<LocalDate> start = Optional.ofNullable(LocalDate.of(2010, Month.MARCH, 18)),
                end = Optional.ofNullable(LocalDate.of(2010, Month.MARCH, 18));
        ZoneId systemZoneId = ZoneId.systemDefault();
        Date from = Date.from(ZonedDateTime.of(start.get(), LocalTime.MIN, systemZoneId).toInstant());
        Date to = Date.from(ZonedDateTime.of(end.get(), LocalTime.MAX, systemZoneId).toInstant());
        String[] accountsAsText = new String[]{
            "warrenmnocos@gmail.com,Warren,Manlangit,Nocos,ANONYMOUS",
            "karensume@gmail.com,Karen,Su,Me,ANONYMOUS",
            "ericcruzperez@gmail.com,Eric,Cruz,Perez,ANONYMOUS",
            "divinepenedasy@gmail.com,Divine,Peneda,Sy,ANONYMOUS",
            "miketanricafort@gmail.com,Mike,Tan,Ricafort,ANONYMOUS"
        };
        Collection<Account> accountsPageOne = Stream.of(accountsAsText)
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
                .collect(Collectors.toCollection(TreeSet::new));
        Iterable<AccountDto> correctAccountDtosPageOne = accountsPageOne.parallelStream()
                .map(accountModelService::toAccountDto)
                .collect(Collectors.toCollection(TreeSet::new));
        BDDMockito.given(accountLoginAuditRepository.findAccountsBetweenLoginTimeInclusive(
                from, to, new PageRequest(0, 5))).willReturn(accountsPageOne.stream());
        Optional<Integer> zero = Optional.of(0);
        Optional<Integer> five = Optional.of(5);
        Collection<AccountDto> accountDtos = accountLoginAuditService.findAccountsWithLoginActivityAscendingly(
                start, end, zero, five);
        Assertions.assertThat(accountDtos).containsExactlyElementsOf(correctAccountDtosPageOne);
        accountsAsText = new String[]{
            "loulonocos@gmail.com,Lou,Lo,Nocos,ANONYMOUS",
            "ricalonocos@gmail.com,Rica,Lo,Nocos,ANONYMOUS",
            "tinalonocos@gmail.com,Tina,Lo,Nocos,ANONYMOUS",
            "alenlonocos@gmail.com,Alen,Lo,Nocos,ANONYMOUS",};
        Collection<Account> accountsPageTwo = Stream.of(accountsAsText)
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
                .collect(Collectors.toCollection(TreeSet::new));
        Iterable<AccountDto> correctAccountDtosPageTwo = accountsPageTwo.parallelStream()
                .map(accountModelService::toAccountDto)
                .collect(Collectors.toCollection(TreeSet::new));
        BDDMockito.given(accountLoginAuditRepository.findAccountsBetweenLoginTimeInclusive(
                from, to, new PageRequest(0, 5))).willReturn(accountsPageTwo.stream());
        accountDtos = accountLoginAuditService.findAccountsWithLoginActivityAscendingly(
                start, end, zero, five);
        Assertions.assertThat(accountDtos).containsExactlyElementsOf(correctAccountDtosPageTwo);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void findAccountsWithLoginActivityAscendinglyPaginationWrongReturnNegativeTest() {
        Optional<LocalDate> start = Optional.ofNullable(LocalDate.of(2010, Month.MARCH, 18)),
                end = Optional.ofNullable(LocalDate.of(2010, Month.MARCH, 18));
        ZoneId systemZoneId = ZoneId.systemDefault();
        Date from = Date.from(ZonedDateTime.of(start.get(), LocalTime.MIN, systemZoneId).toInstant());
        Date to = Date.from(ZonedDateTime.of(end.get(), LocalTime.MAX, systemZoneId).toInstant());
        String[] accountsAsText = new String[]{
            "warrenmnocos@gmail.com,Warren,Manlangit,Nocos,ANONYMOUS",
            "karensume@gmail.com,Karen,Su,Me,ANONYMOUS",
            "ericcruzperez@gmail.com,Eric,Cruz,Perez,ANONYMOUS",
            "divinepenedasy@gmail.com,Divine,Peneda,Sy,ANONYMOUS",
            "miketanricafort@gmail.com,Mike,Tan,Ricafort,ANONYMOUS"
        };
        Collection<Account> accountsPageOne = Stream.of(accountsAsText)
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
                .collect(Collectors.toCollection(TreeSet::new));
        Iterable<AccountDto> correctAccountDtosPageOne = accountsPageOne.parallelStream()
                .map(accountModelService::toAccountDto)
                .collect(Collectors.toCollection(TreeSet::new));
        accountsAsText = new String[]{
            "loulonocos@gmail.com,Lou,Lo,Nocos,ANONYMOUS",
            "ricalonocos@gmail.com,Rica,Lo,Nocos,ANONYMOUS",
            "tinalonocos@gmail.com,Tina,Lo,Nocos,ANONYMOUS",
            "alenlonocos@gmail.com,Alen,Lo,Nocos,ANONYMOUS",};
        Collection<Account> accountsPageTwo = Stream.of(accountsAsText)
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
                .collect(Collectors.toCollection(TreeSet::new));
        Iterable<AccountDto> correctAccountDtosPageTwo = accountsPageTwo.parallelStream()
                .map(accountModelService::toAccountDto)
                .collect(Collectors.toCollection(TreeSet::new));
        BDDMockito.given(accountLoginAuditRepository.findAccountsBetweenLoginTimeInclusive(
                from, to, new PageRequest(0, 5))).willReturn(accountsPageOne.stream());
        Optional<Integer> zero = Optional.of(0);
        Optional<Integer> five = Optional.of(5);
        Collection<AccountDto> accountDtos = accountLoginAuditService
                .findAccountsWithLoginActivityAscendingly(start, end, zero, five);
        Assertions.assertThat(accountDtos).doesNotContainAnyElementsOf(correctAccountDtosPageTwo);
        BDDMockito.given(accountLoginAuditRepository.findAccountsBetweenLoginTimeInclusive(
                from, to, new PageRequest(0, 5))).willReturn(accountsPageTwo.stream());
        accountDtos = accountLoginAuditService.findAccountsWithLoginActivityAscendingly(
                start, end, zero, five);
        Assertions.assertThat(accountDtos).doesNotContainAnyElementsOf(correctAccountDtosPageOne);
    }

    @Test(expected = IllegalArgumentException.class)
    @WithMockUser(roles = "ADMIN")
    public void findAccountsWithLoginActivityAscendinglyPaginationWrongDatesNegativeTest() {
        Optional<LocalDate> start = Optional.ofNullable(LocalDate.of(2010, Month.MARCH, 18)),
                end = Optional.ofNullable(LocalDate.of(2014, Month.MARCH, 18));
        ZoneId systemZoneId = ZoneId.systemDefault();
        Date from = Date.from(ZonedDateTime.of(start.get(), LocalTime.MIN, systemZoneId).toInstant());
        Date to = Date.from(ZonedDateTime.of(end.get(), LocalTime.MAX, systemZoneId).toInstant());
        BDDMockito.given(accountLoginAuditRepository.findAccountsBetweenLoginTimeInclusive(
                from, to, new PageRequest(0, Integer.MAX_VALUE))).willReturn(Stream.of());
        Optional<Integer> zero = Optional.of(0);
        Optional<Integer> five = Optional.of(5);
        accountLoginAuditService.findAccountsWithLoginActivityAscendingly(
                end, start, zero, five);
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(roles = "USER")
    public void findAccountsWithLoginActivityAscendinglyPaginationSecurityNotAdminNegativeTest() {
        Optional<LocalDate> start = Optional.ofNullable(LocalDate.of(2010, Month.MARCH, 18)),
                end = Optional.ofNullable(LocalDate.of(2010, Month.MARCH, 18));
        Optional<Integer> nullInteger = Optional.ofNullable(null);
        accountLoginAuditService.findAccountsWithLoginActivityAscendingly(
                start, end, nullInteger, nullInteger);
    }

    @Test(expected = AuthenticationException.class)
    public void findAccountsWithLoginActivityAscendinglyPaginationSecurityNoRoleNegativeTest() {
        Optional<LocalDate> start = Optional.ofNullable(LocalDate.of(2010, Month.MARCH, 18)),
                end = Optional.ofNullable(LocalDate.of(2010, Month.MARCH, 18));
        Optional<Integer> nullInteger = Optional.ofNullable(null);
        accountLoginAuditService.findAccountsWithLoginActivityAscendingly(
                start, end, nullInteger, nullInteger);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void findFilteredAccountsWithLoginAuditPositiveTest() {
        Optional<LocalDate> start = Optional.ofNullable(LocalDate.of(2010, Month.MARCH, 18)),
                end = Optional.ofNullable(LocalDate.of(2014, Month.MARCH, 18));
        ZoneId systemZoneId = ZoneId.systemDefault();
        Date from = Date.from(ZonedDateTime.of(start.get(), LocalTime.MIN, systemZoneId).toInstant());
        Date to = Date.from(ZonedDateTime.of(end.get(), LocalTime.MAX, systemZoneId).toInstant());
        String[] accountsAsText = new String[]{
            "wa@gmail.com,Warren,Lo,Nocos,ANONYMOUS",
            "war@gmail.com,Warren,Lo,Nocos,ANONYMOUS",
            "warr@gmail.com,Warren,Lo,Nocos,ANONYMOUS",
            "warre@gmail.com,Warren,Lo,Nocos,ANONYMOUS",};
        Collection<Account> accounts = Stream.of(accountsAsText)
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
                .collect(Collectors.toCollection(TreeSet::new));
        Date[] loginDates = new Date[30];
        for (int index = 0; index < loginDates.length; index++) {
            loginDates[index] = Date.from(ZonedDateTime.of(
                    LocalDate.of(2016, Month.SEPTEMBER, index + 1),
                    LocalTime.MIN, systemZoneId).toInstant());
        }
        List<AccountLoginAudit> accountLoginAudits = accounts.parallelStream()
                .flatMap(account -> Arrays.stream(loginDates).parallel()
                .map(date -> {
                    AccountLoginAudit accountLoginAudit;
                    accountLoginAudit = new AccountLoginAudit(account);
                    accountLoginAudit.setLoginTime(date);
                    return accountLoginAudit;
                }))
                .collect(Collectors.toList());
        Collection<String> firstNames = Arrays.asList("Warren", "Anyone");
        BDDMockito.given(accountLoginAuditRepository.findByFilters(from, to,
                Collections.emptyList(), firstNames, Collections.emptyList(),
                Collections.emptyList()))
                .willReturn(accountLoginAudits);
        Map<String, Long> correctResult = accountLoginAudits.stream()
                .map(AccountLoginAudit::getAccount)
                .collect(Collectors.groupingBy(
                        Account::getEmail,
                        TreeMap::new,
                        Collectors.counting()));
        Map<String, Long> result = accountLoginAuditService.findFilteredAccountsWithLoginAudit(start, end,
                Collections.emptyList(), firstNames, Collections.emptyList(), Collections.emptyList());
        Assertions.assertThat(result).containsAllEntriesOf(correctResult);
    }

    @Test(expected = IllegalArgumentException.class)
    @WithMockUser(roles = "ADMIN")
    public void findFilteredAccountsWithLoginAuditWrongDatesNegativeTest() {
        Optional<LocalDate> start = Optional.ofNullable(LocalDate.of(2010, Month.MARCH, 18)),
                end = Optional.ofNullable(LocalDate.of(2014, Month.MARCH, 18));;
        accountLoginAuditService.findFilteredAccountsWithLoginAudit(end, start,
                Collections.emptyList(), Collections.emptyList(),
                Collections.emptyList(), Collections.emptyList());
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(roles = "USER")
    public void findFilteredAccountsWithLoginAuditSecurityNotAdminNegativeTest() {
        Optional<LocalDate> start = Optional.ofNullable(LocalDate.of(2010, Month.MARCH, 18)),
                end = Optional.ofNullable(LocalDate.of(2010, Month.MARCH, 18));;
        accountLoginAuditService.findFilteredAccountsWithLoginAudit(start, end,
                Collections.emptyList(), Collections.emptyList(),
                Collections.emptyList(), Collections.emptyList());
    }

    @Test(expected = AuthenticationException.class)
    public void findFilteredAccountsWithLoginAuditSecurityNoRoleNegativeTest() {
        Optional<LocalDate> start = Optional.ofNullable(LocalDate.of(2010, Month.MARCH, 18)),
                end = Optional.ofNullable(LocalDate.of(2010, Month.MARCH, 18));;
        accountLoginAuditService.findFilteredAccountsWithLoginAudit(start, end,
                Collections.emptyList(), Collections.emptyList(),
                Collections.emptyList(), Collections.emptyList());
    }

}
