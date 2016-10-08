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

import java.security.Principal;
import java.util.Set;
import jp.co.isr.application.account.model.dto.AccountDto;
import jp.co.isr.application.account.model.dto.AccountWithUserDetailsDto;
import jp.co.isr.application.account.model.entity.Account;
import jp.co.isr.application.account.model.entity.AccountUserDetails;

/**
 * This is the service used for account-related operations.
 *
 * @author Warren Nocos
 * @since 1.0
 * @version 1.0
 */
public interface AccountService {

    /**
     * Deletes an {@link Account} using the unique identifier specified.
     *
     * @param id the unique identifier of {@link Account}
     */
    void deleteAccountById(long id);

    /**
     * Deletes an {@link Account} using the email specified.
     *
     * @param email the email of an {@link Account}, must not be {@code null}
     */
    void deleteAccountByEmail(String email);

    /**
     * Retrieves all accounts. This is not efficient if plenty of
     * {@link Account} records are stored. Use {@link AccountService#findAllAccounts(java.lang.Integer, java.lang.Integer)
     * } instead to paginate results.
     *
     * @return {@link Set} of accounts
     */
    Set<AccountDto> findAllAccounts();

    /**
     * Retrieves all accounts in pagination.
     *
     * @param page the page, from {@link 0}, must not be {@code null}
     * @param pageSize the page size, must not be {@code null}
     * @return {@link Set} of accounts
     */
    Set<AccountDto> findAllAccounts(Integer page, Integer pageSize);

    /**
     * Retrieves an {@link Account} with the specified unique identifier.
     *
     * @param id the unique identifier of {@link Account}
     * @return the {@link Account}
     */
    AccountDto findAccountById(long id);

    /**
     * Retrieves an {@link Account} with the specified email
     *
     * @param email the email of {@link Account}
     * @return the {@link Account}, must not be {@code null}
     */
    AccountDto findAccountByEmail(String email);

    /**
     * Retrieves the currently authenticated {@link Account}.
     *
     * @return the currently authenticated {@link Account}
     * @throws SecurityException if {@link Principal} is not found or invalid
     */
    AccountDto getCurrentAccount() throws SecurityException;

    /**
     * Persists an {@link Account} to the repository.
     *
     * @param accountWithCredentialsDto the {@link AccountWithUserDetailsDto},
     * which is converted to {@link Account} and {@link AccountUserDetails}, to
     * be persisted, and must not be {@code null}
     */
    void saveAccount(AccountWithUserDetailsDto accountWithCredentialsDto);

    /**
     * Updates an {@link Account} to the repository.
     *
     * @param accountWithCredentialsDto the {@link AccountWithUserDetailsDto},
     * which is converted to {@link Account} and {@link AccountUserDetails}, to
     * be updated, and must not be {@code null}
     */
    void updateAccount(AccountWithUserDetailsDto accountWithCredentialsDto);

}
