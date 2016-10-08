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

import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.NotNull;
import jp.co.isr.application.account.model.dto.AccountDto;
import jp.co.isr.application.account.model.dto.AccountWithUserDetailsDto;
import jp.co.isr.application.account.model.entity.Account;
import jp.co.isr.application.account.model.entity.AccountUserDetails;
import jp.co.isr.application.account.repository.AccountRepository;
import jp.co.isr.application.account.repository.AccountUserDetailsRepository;
import jp.co.isr.application.account.service.AccountModelService;
import jp.co.isr.application.account.service.AccountService;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

/**
 * This is the main implementation for {@link AccountService}.
 *
 * @author Warren Nocos
 * @since 1.0
 * @version 1.0
 */
@Service
@RolesAllowed("ADMIN")
@Transactional(readOnly = true)
public class AccountServiceImpl implements AccountService {

    /**
     * This is the repository used for command and lookup of {@link Account}
     * entities.
     */
    protected final AccountRepository accountRepository;

    /**
     * This is the repository used for command and lookup of
     * {@link AccountUserDetails} entities.
     */
    protected final AccountUserDetailsRepository accountUserDetailsRepository;

    /**
     * This is the converter to convert between account entity and DTO.
     */
    protected final AccountModelService accountDtoAccountConverterService;

    /**
     * Constructs concrete implementation for {@link AccountService}.
     *
     * @param accountRepository the {@link Account} repository
     * @param accountUserDetailsRepository the {@link AccountUserDetails}
     * repository
     * @param accountDtoAccountConverterService the converter
     */
    @Inject
    public AccountServiceImpl(
            AccountRepository accountRepository,
            AccountUserDetailsRepository accountUserDetailsRepository,
            AccountModelService accountDtoAccountConverterService) {
        this.accountRepository = accountRepository;
        this.accountUserDetailsRepository = accountUserDetailsRepository;
        this.accountDtoAccountConverterService = accountDtoAccountConverterService;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    @Transactional
    public void deleteAccountById(long id) {
        accountRepository.delete(id);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    @Validated
    @Transactional
    public void deleteAccountByEmail(@NotNull String email) {
        accountRepository.deleteByEmail(email);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Set<AccountDto> findAllAccounts() {
        return accountRepository.findAllAsStream()
                .map(accountDtoAccountConverterService::toAccountDto)
                .collect(Collectors.toSet());
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Set<AccountDto> findAllAccounts(
            @NotNull Integer page, @NotNull Integer pageSize) {
        return accountRepository.findAllAsPagedStream(new PageRequest(page, pageSize))
                .map(accountDtoAccountConverterService::toAccountDto)
                .collect(Collectors.toSet());
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public AccountDto findAccountById(long id) {
        return accountRepository.findById(id)
                .map(accountDtoAccountConverterService::toAccountDto)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public AccountDto findAccountByEmail(@NotNull String email) {
        return accountRepository.findByEmail(email)
                .map(accountDtoAccountConverterService::toAccountDto)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));
    }

    /**
     * {@inheritDoc }
     */
    @Override
    @RolesAllowed({"ADMIN", "USER"})
    public AccountDto getCurrentAccount() throws SecurityException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // Check if principal is present and valid
        if (principal != null && principal.getClass() == AccountUserDetails.class) {
            AccountUserDetails accountUserDetails;
            accountUserDetails = (AccountUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return accountRepository.findByEmail(accountUserDetails.getUsername())
                    .map(accountDtoAccountConverterService::toAccountDto)
                    .orElseThrow(() -> {
                        SecurityContextHolder.clearContext();
                        return new SecurityException("Authentication is required to access this service");
                    });
        }
        throw new SecurityException("Authentication is required to access this service");
    }

    /**
     * {@inheritDoc }
     */
    @Override
    @Validated
    @Transactional
    public void saveAccount(@NotNull AccountWithUserDetailsDto accountWithCredentialsDto) {
        accountRepository.save(accountDtoAccountConverterService.toAccount(accountWithCredentialsDto));
        accountUserDetailsRepository.save(accountDtoAccountConverterService.toAccountUserDetails(accountWithCredentialsDto));
    }

    /**
     * {@inheritDoc }
     */
    @Override
    @Validated
    @Transactional
    public void updateAccount(@NotNull AccountWithUserDetailsDto accountWithCredentialsDto) {
        AccountDto currentAccount = getCurrentAccount();
        accountRepository.findById(accountWithCredentialsDto.getId())
                .ifPresent(account -> {
                    // TODO
                    accountRepository.save(account);
                });
    }

}
