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

import jp.co.isr.application.account.service.exception.AccountNotFoundException;
import java.security.Principal;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.security.RolesAllowed;
import javax.cache.annotation.CacheResult;
import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.NotNull;
import jp.co.isr.application.account.model.dto.AccountDto;
import jp.co.isr.application.account.model.dto.AccountRequestDto;
import jp.co.isr.application.account.model.entity.Account;
import jp.co.isr.application.account.model.entity.AccountLoginAudit;
import jp.co.isr.application.account.repository.AccountLoginAuditRepository;
import jp.co.isr.application.account.repository.AccountRepository;
import jp.co.isr.application.account.service.AccountLoginAuditService;
import jp.co.isr.application.account.service.AccountModelService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.context.SecurityContext;
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
    protected final AccountModelService accountDtoAccountConverterService;

    /**
     * Constructs an implementation of {@link AccountLoginAuditService}.
     *
     * @param accountLoginAuditRepository the repository of
     * {@link AccountLoginAudit}
     * @param accountRepository the repository of {@link Account}
     * @param accountDtoAccountConverterService the
     * {@link AccountModelService} used for model conversion
     */
    @Inject
    public AccountLoginAuditServiceImpl(
            AccountLoginAuditRepository accountLoginAuditRepository,
            AccountRepository accountRepository,
            AccountModelService accountDtoAccountConverterService) {
        this.accountLoginAuditRepository = accountLoginAuditRepository;
        this.accountRepository = accountRepository;
        this.accountDtoAccountConverterService = accountDtoAccountConverterService;
    }

    /**
     * {@inheritDoc }
     * Allowable role to invoke this operation is set to 'ANONYMOUS', since
     * auditing will happen after authentication is verified, but before
     * {@link SecurityContext}'s {@link Principal} is created.
     *
     * @see AuthenticationSuccessEvent
     * @see InteractiveAuthenticationSuccessEvent
     */
    @Override
    @Validated
    @RolesAllowed("ANONYMOUS")
    @CacheEvict(allEntries = true,
            cacheNames = {"datesWithLoginActivity"})
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
    @CacheResult(cacheName = "datesWithLoginActivity")
    public Set<LocalDate> findDatesWithLoginActivityAscendingly() {
        return accountLoginAuditRepository.findAllLoginDate()
                .parallel()
                .map(Date::toInstant)
                .map(LocalDate::from)
                .distinct()
                .collect(Collectors.toCollection(HashSet::new));
    }

    /**
     * {@inheritDoc }
     */
    @Override
    @Validated
    public Set<LocalDate> findDatesWithLoginActivityAscendingly(
            @NotNull Integer page, @NotNull Integer pageSize) {
        return accountLoginAuditRepository.findAllLoginDate(new PageRequest(page, pageSize))
                .parallel()
                .map(Date::toInstant)
                .map(LocalDate::from)
                .distinct()
                .collect(Collectors.toCollection(HashSet::new));
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Set<AccountDto> findAccountsWithLoginActivityAscendingly(
            Optional<LocalDate> start, Optional<LocalDate> end) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Set<AccountDto> findAccountsWithLoginActivityAscendingly(Optional<LocalDate> start, Optional<LocalDate> end, Integer page, Integer pageSize) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Map<AccountDto, Long> findFilteredAccountsWIthLoginAudit(AccountRequestDto accountRequestDto) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Map<AccountDto, Long> findFilteredAccountsWIthLoginAudit(AccountRequestDto accountRequestDto, Integer page, Integer pageSize) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
