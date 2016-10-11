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

import javax.validation.constraints.NotNull;
import jp.co.isr.application.account.model.dto.AccountDto;
import jp.co.isr.application.account.model.dto.AccountWithUserDetailsDto;
import jp.co.isr.application.account.model.dto.AddressDto;
import jp.co.isr.application.account.model.entity.Account;
import jp.co.isr.application.account.model.entity.AccountUserDetails;
import jp.co.isr.application.account.model.entity.Address;

/**
 * This is a service for handling {@link Account} models
 *
 * @author Warren Nocos
 * @since 1.0
 * @version 1.0
 */
public interface AccountModelService {

    /**
     * Copies property values of {@link Account} to {@link AccountDto}. Specific
     * considerations like handling of {@link Iterable} properties must be
     * catered and documented on the implementations.
     *
     * @param account the {@link Account}
     * @param accountDto the {@link AccountDto}
     */
    void copyProperties(@NotNull Account account, @NotNull AccountDto accountDto);

    /**
     * Copies property values of {@link AccountDto} to {@link Account}. Specific
     * considerations like handling of {@link Iterable} properties must be
     * catered and documented on the implementations.
     *
     * @param accountDto the {@link AccountDto}
     * @param account the {@link Account}
     */
    void copyProperties(@NotNull AccountDto accountDto, @NotNull Account account);

    /**
     * Copies property values of {@link Account} to
     * {@link AccountWithUserDetailsDto}. Specific considerations like handling
     * of {@link Iterable} properties must be catered and documented on the
     * implementations.
     *
     * @param account the {@link Account}
     * @param accountWithUserDetailsDto the {@link AccountWithUserDetailsDto}
     */
    void copyProperties(@NotNull Account account, @NotNull AccountWithUserDetailsDto accountWithUserDetailsDto);

    /**
     * Copies property values of {@link AccountWithUserDetailsDto} to
     * {@link Account}. Specific considerations like handling of
     * {@link Iterable} properties must be catered and documented on the
     * implementations.
     *
     * @param accountWithUserDetailsDto the {@link AccountWithUserDetailsDto}
     * @param account the {@link Account}
     */
    void copyProperties(@NotNull AccountWithUserDetailsDto accountWithUserDetailsDto, @NotNull Account account);
    
    /**
     * Copies property values of {@link AccountUserDetails} to
     * {@link AccountWithUserDetailsDto}. Specific considerations like handling
     * of {@link Iterable} properties must be catered and documented on the
     * implementations.
     *
     * @param accountUserDetails the {@link AccountUserDetails}
     * @param accountWithUserDetailsDto the {@link AccountWithUserDetailsDto}
     */
    void copyProperties(@NotNull AccountUserDetails accountUserDetails, @NotNull AccountWithUserDetailsDto accountWithUserDetailsDto);

    /**
     * Copies property values of {@link AccountWithUserDetailsDto} to
     * {@link AccountUserDetails}. Specific considerations like handling of
     * {@link Iterable} properties must be catered and documented on the
     * implementations.
     *
     * @param accountWithUserDetailsDto the {@link AccountWithUserDetailsDto}
     * @param accountUserDetails the {@link AccountUserDetails}
     */
    void copyProperties(@NotNull AccountWithUserDetailsDto accountWithUserDetailsDto, @NotNull AccountUserDetails accountUserDetails);

    /**
     * Converts an {@link Account} to {@Link AccountDto}.
     *
     * @param account the {@link Account} to be converted
     * @return the {@Link AccountDto}
     */
    AccountDto toAccountDto(@NotNull Account account);

    /**
     * Converts an {@link AccountWithUserDetailsDto} to {@Link Account}.
     *
     * @param accountWithCredentialsDto the {@link AccountDto} to be converted
     * @return the {@Link Account}
     */
    Account toAccount(@NotNull AccountWithUserDetailsDto accountWithCredentialsDto);

    /**
     * Converts an {@link AccountDto} to {@Link Account}.
     *
     * @param accountDto the {@link AccountDto} to be converted
     * @return the {@Link Account}
     */
    Account toAccount(@NotNull AccountDto accountDto);

    /**
     * Converts an {@link AccountWithUserDetailsDto} to
     * {@Link AccountUserDetails}.
     *
     * @param accountWithCredentialsDto the {@link AccountDto} to be converted
     * @return the {@Link AccountUserDetails}
     */
    AccountUserDetails toAccountUserDetails(@NotNull AccountWithUserDetailsDto accountWithCredentialsDto);

    /**
     * Converts an {@link Address} to {@Link AddressDto}.
     *
     * @param address the {@link Address} to be converted
     * @return the {@Link AddressDto}
     */
    AddressDto toAddressDto(@NotNull Address address);

    /**
     * Converts an {@link AddressDto} to {@Link Address}.
     *
     * @param addressDto the {@link AddressDto} to be converted
     * @return the {@Link Address}
     */
    Address toAddress(@NotNull AddressDto addressDto);

}
