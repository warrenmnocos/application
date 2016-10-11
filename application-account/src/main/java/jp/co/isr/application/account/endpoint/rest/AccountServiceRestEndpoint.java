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
package jp.co.isr.application.account.endpoint.rest;

import java.util.Set;
import javax.inject.Inject;
import javax.inject.Named;
import jp.co.isr.application.account.model.dto.AccountDto;
import jp.co.isr.application.account.model.dto.AccountWithUserDetailsDto;
import jp.co.isr.application.account.service.AccountService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * A decorator for {@link AccountService}, used to expose real implementation's
 * operations into a RESTful web operations.
 *
 * @author Warren Nocos
 * @since 1.0
 * @version 1.0
 */
@RestController
@RequestMapping(path = "/api/rest/account",
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class AccountServiceRestEndpoint implements AccountService {

    /**
     * This is where operations are delegated.
     */
    protected final AccountService accountService;

    /**
     * Constructs an implementation of {@link AccountService} that exposes
     * specified implementation into a RESTful web service.
     *
     * @param accountService
     */
    @Inject
    public AccountServiceRestEndpoint(
            @Named("accountServiceImpl") AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    @GetMapping
    public Set<AccountDto> findAllAccounts() {
        return accountService.findAllAccounts();
    }

    /**
     * {@inheritDoc }
     */
    @Override
    @GetMapping(params = {"page", "size"})
    public Set<AccountDto> findAllAccounts(
            @RequestParam("page") Integer page,
            @RequestParam("size") Integer pageSize) {
        return accountService.findAllAccounts(page, pageSize);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    @GetMapping(params = "id")
    public AccountDto findAccountById(long id) {
        return accountService.findAccountById(id);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    @GetMapping(params = "email")
    public AccountDto findAccountByEmail(String email) {
        return accountService.findAccountByEmail(email);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    @GetMapping("/me")
    public AccountDto getCurrentAccount() {
        return accountService.getCurrentAccount();
    }

    /**
     * {@inheritDoc }
     */
    @Override
    @PostMapping
    public void saveAccount(@RequestBody AccountWithUserDetailsDto accountWithUserDetailsDto) {
        accountService.saveAccount(accountWithUserDetailsDto);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    @PutMapping
    public void updateAccount(@RequestBody AccountWithUserDetailsDto accountWithUserDetailsDto) {
        accountService.updateAccount(accountWithUserDetailsDto);
    }

}
