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

import java.time.Instant;
import jp.co.isr.application.account.service.exception.AccountNotFoundException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.NotNull;
import jp.co.isr.application.account.model.dto.AccountDto;
import jp.co.isr.application.account.model.entity.Account;
import jp.co.isr.application.account.model.entity.AccountLoginAudit;
import jp.co.isr.application.account.repository.AccountLoginAuditRepository;
import jp.co.isr.application.account.repository.AccountRepository;
import jp.co.isr.application.account.service.AccountLoginAuditService;
import jp.co.isr.application.account.service.AccountModelService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

/**
 * This is the concrete implementation for {@link AccountLoginAuditService}.
 *
 * @author Warren Nocos
 * @since 1.0
 * @version 1.0
 */
@Service
@Validated
@RolesAllowed("ADMIN")
@Transactional(readOnly = true)
public class AccountLoginAuditServiceImpl implements AccountLoginAuditService {

    /**
     * The repository of {@link AccountLoginAudit}
     */
    protected final AccountLoginAuditRepository accountLoginAuditRepository;

    /**
     * The repository of {@link Account}
     */
    protected final AccountRepository accountRepository;

    /**
     * Used for model conversion
     */
    protected final AccountModelService accountModelService;

    /**
     * Constructs an implementation of {@link AccountLoginAuditService}.
     *
     * @param accountLoginAuditRepository the repository of
     * {@link AccountLoginAudit}
     * @param accountRepository the repository of {@link Account}
     * @param accountModelService the {@link AccountModelService} used for model
     * conversion
     */
    @Inject
    public AccountLoginAuditServiceImpl(
            AccountLoginAuditRepository accountLoginAuditRepository,
            AccountRepository accountRepository,
            AccountModelService accountModelService) {
        this.accountLoginAuditRepository = accountLoginAuditRepository;
        this.accountRepository = accountRepository;
        this.accountModelService = accountModelService;
    }

    /**
     * {@inheritDoc }
     *
     */
    @Override
    @Transactional
    @RolesAllowed({"USER", "ADMIN"})
    public void audit(@NotNull AccountDto accountDto) throws AccountNotFoundException {
        Account account = accountRepository.findByEmail(accountDto.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));
        AccountLoginAudit accountLoginAudit = new AccountLoginAudit();
        accountLoginAudit.setAccount(account);
        accountLoginAuditRepository.save(accountLoginAudit);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Collection<LocalDate> findDatesWithLoginActivityAscendingly() {
        return findDatesWithLoginActivityAscendingly(0, Integer.MAX_VALUE);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Collection<LocalDate> findDatesWithLoginActivityAscendingly(
            @NotNull Integer page, @NotNull Integer pageSize) {
        ZoneId defaultZoneId = ZoneId.systemDefault();
        try (Stream<LocalDate> dates = accountLoginAuditRepository.findAllLoginDate(new PageRequest(page, pageSize))
                .parallel()
                .map(Date::getTime)
                .map(Instant::ofEpochMilli)
                .map(instant -> instant.atZone(defaultZoneId).toLocalDate())
                .distinct()) {
            return dates.collect(Collectors.toList());
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Collection<AccountDto> findAccountsWithLoginActivityAscendingly(
            Optional<LocalDate> start, Optional<LocalDate> end) {
        return findAccountsWithLoginActivityAscendingly(start, end, 0, Integer.MAX_VALUE);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Collection<AccountDto> findAccountsWithLoginActivityAscendingly(Optional<LocalDate> start,
            Optional<LocalDate> end, @NotNull Integer page, @NotNull Integer pageSize) {
        if (start.isPresent() && end.isPresent()) {
            ZoneId systemZoneId = ZoneId.systemDefault();
            Date from = Date.from(ZonedDateTime.of(start.get(), LocalTime.MIN, systemZoneId).toInstant());
            Date to = Date.from(ZonedDateTime.of(end.get(), LocalTime.MAX, systemZoneId).toInstant());
            checkDates(from, to);
            return getAccountsDtoFrom(accountLoginAuditRepository.findAccountsBetweenLoginTimeInclusive(from, to, new PageRequest(page, pageSize)));
        } else if (start.isPresent()) {
            Date from = Date.from(ZonedDateTime.of(start.get(), LocalTime.MIN, ZoneId.systemDefault()).toInstant());
            return getAccountsDtoFrom(accountLoginAuditRepository.findAccountsAfterLoginTimeInclusive(from, new PageRequest(page, pageSize)));
        } else if (end.isPresent()) {
            Date to = Date.from(ZonedDateTime.of(end.get(), LocalTime.MAX, ZoneId.systemDefault()).toInstant());
            return getAccountsDtoFrom(accountLoginAuditRepository.findAccountsBeforeLoginTimeInclusive(to, new PageRequest(page, pageSize)));
        } else {
            return getAccountsDtoFrom(accountLoginAuditRepository.findAccountsWithLoginAudit(new PageRequest(page, pageSize)));
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Map<String, Long> findFilteredAccountsWithLoginAudit(Optional<LocalDate> start,
            Optional<LocalDate> end, Collection<String> emails, Collection<String> firstNames,
            Collection<String> middleNames, Collection<String> lastNames) {
        return findFilteredAccountsWithLoginAudit(start, end, emails, firstNames,
                middleNames, lastNames, 0, Integer.MAX_VALUE);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Map<String, Long> findFilteredAccountsWithLoginAudit(Optional<LocalDate> start,
            Optional<LocalDate> end, Collection<String> emails, Collection<String> firstNames,
            Collection<String> middleNames, Collection<String> lastNames,
            @NotNull Integer page, @NotNull Integer pageSize) {
        Date from = null, to = null;
        if (start.isPresent() && end.isPresent()) {
            ZoneId systemZoneId = ZoneId.systemDefault();
            from = Date.from(ZonedDateTime.of(start.get(), LocalTime.MIN, systemZoneId).toInstant());
            to = Date.from(ZonedDateTime.of(end.get(), LocalTime.MAX, systemZoneId).toInstant());
            checkDates(from, to);
        } else if (start.isPresent()) {
            from = Date.from(ZonedDateTime.of(start.get(), LocalTime.MIN, ZoneId.systemDefault()).toInstant());
        } else if (end.isPresent()) {
            to = Date.from(ZonedDateTime.of(end.get(), LocalTime.MAX, ZoneId.systemDefault()).toInstant());
        }
        return accountLoginAuditRepository.findByFilters(from, to,
                emails == null ? Collections.emptyList() : emails,
                firstNames == null ? Collections.emptyList() : firstNames,
                middleNames == null ? Collections.emptyList() : middleNames,
                lastNames == null ? Collections.emptyList() : lastNames,
                new PageRequest(page, pageSize)).stream()
                .map(AccountLoginAudit::getAccount)
                .collect(Collectors.groupingBy(
                        Account::getEmail,
                        TreeMap::new,
                        Collectors.counting()));
    }

    /**
     * Wraps {@link Stream} of {@link Account} into Java 7s try-with-resources
     * block for auto-closing of {@link Stream}, as pointed by Spring Data
     * reference manual.
     *
     * @param accountsStreamSupplier {@link Stream} of {@link Account}
     * @return {@link Collection} of {@link AccountDto}
     * @see
     * <a href="http://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.query-streaming">Streaming
     * query results</a>
     */
    protected Collection<AccountDto> getAccountsDtoFrom(Stream<Account> accountsStreamSupplier) {
        try (Stream<AccountDto> accounts = accountsStreamSupplier.parallel()
                .map(accountModelService::toAccountDto)) {
            return accounts.collect(Collectors.toCollection(HashSet::new));
        }
    }

    /**
     * Validates if dates are valid, by checking start {@link Date} is before
     * end {@link Date}.
     *
     * @param start the start {@link Date}
     * @param end the end {@link Date}
     * @throws IllegalArgumentException if dates are invalid
     */
    protected void checkDates(Date start, Date end) throws IllegalArgumentException {
        if (!start.equals(end) && start.after(end)) {
            throw new IllegalArgumentException("Start date is after end date");
        }
    }

}
