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

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Named;
import jp.co.isr.application.account.model.dto.AccountDto;
import jp.co.isr.application.account.service.AccountLoginAuditService;
import jp.co.isr.application.account.service.exception.AccountNotFoundException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * A decorator for {@link AccountLoginAuditService}, used to expose real
 * implementation's operations into a RESTful web operations.
 *
 * @author Warren Nocos
 * @since 1.0
 * @version 1.0
 */
@RestController
@RequestMapping(path = "/api/rest/account",
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class AccountLoginAuditServiceRestEndpoint implements AccountLoginAuditService {

    protected final AccountLoginAuditService accountLoginAuditService;

    @Inject
    public AccountLoginAuditServiceRestEndpoint(
            @Named("accountLoginAuditServiceImpl") AccountLoginAuditService accountLoginAuditService) {
        this.accountLoginAuditService = accountLoginAuditService;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void audit(AccountDto accountDto) throws AccountNotFoundException {
        throw new UnsupportedOperationException("External account login auditing is not supported");
    }

    /**
     * {@inheritDoc }
     */
    @Override
    @GetMapping("/dates")
    public Collection<LocalDate> findDatesWithLoginActivityAscendingly() {
        return accountLoginAuditService.findDatesWithLoginActivityAscendingly();
    }

    /**
     * {@inheritDoc }
     */
    @Override
    @GetMapping(params = {"page", "size"}, path = "/dates")
    public Collection<LocalDate> findDatesWithLoginActivityAscendingly(
            @RequestParam("page") Integer page, @RequestParam("size") Integer pageSize) {
        return accountLoginAuditService.findDatesWithLoginActivityAscendingly(page, pageSize);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    @GetMapping(params = {"start", "end",}, path = "/users")
    public Collection<AccountDto> findAccountsWithLoginActivityAscendingly(
            @DateTimeFormat(pattern = "yyyyMMdd") @RequestParam("start") Optional<LocalDate> start,
            @DateTimeFormat(pattern = "yyyyMMdd") @RequestParam("end") Optional<LocalDate> end) {
        return accountLoginAuditService.findAccountsWithLoginActivityAscendingly(start, end);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    @GetMapping(params = {"start", "end", "page", "size"}, path = "/users")
    public Collection<AccountDto> findAccountsWithLoginActivityAscendingly(
            @DateTimeFormat(pattern = "yyyyMMdd") @RequestParam("start") Optional<LocalDate> start,
            @DateTimeFormat(pattern = "yyyyMMdd") @RequestParam("end") Optional<LocalDate> end,
            @RequestParam("page") Integer page, @RequestParam("size") Integer pageSize) {
        return accountLoginAuditService.findAccountsWithLoginActivityAscendingly(start, end, page, pageSize);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    @GetMapping(params = {"start", "end", "email", "firstName", "middleName", "lastName"}, path = "/logins")
    public Map<String, Long> findFilteredAccountsWithLoginAudit(
            @DateTimeFormat(pattern = "yyyyMMdd") @RequestParam("start") Optional<LocalDate> start,
            @DateTimeFormat(pattern = "yyyyMMdd") @RequestParam("end") Optional<LocalDate> end,
            @RequestParam("email") Collection<String> emails, @RequestParam("firstName") Collection<String> firstNames,
            @RequestParam("middleName") Collection<String> middleNames, @RequestParam("lastName") Collection<String> lastNames) {
        return accountLoginAuditService.findFilteredAccountsWithLoginAudit(start, end, emails, firstNames, middleNames, lastNames);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    @GetMapping(params = {"start", "end", "email", "firstName", "middleName",
        "lastName", "page", "size"}, path = "/logins")
    public Map<String, Long> findFilteredAccountsWithLoginAudit(
            @DateTimeFormat(pattern = "yyyyMMdd") @RequestParam("start") Optional<LocalDate> start,
            @DateTimeFormat(pattern = "yyyyMMdd") @RequestParam("end") Optional<LocalDate> end,
            @RequestParam("email") Collection<String> emails, @RequestParam("firstName") Collection<String> firstNames,
            @RequestParam("middleName") Collection<String> middleNames, @RequestParam("lastName") Collection<String> lastNames,
            @RequestParam("page") Integer page, @RequestParam("size") Integer pageSize) {
        return accountLoginAuditService.findFilteredAccountsWithLoginAudit(start, end, emails, firstNames, middleNames, lastNames, page, pageSize);
    }

}
