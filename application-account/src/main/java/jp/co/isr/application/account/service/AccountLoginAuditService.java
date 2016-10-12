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

import jp.co.isr.application.account.service.exception.AccountNotFoundException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import jp.co.isr.application.account.model.dto.AccountDto;
import jp.co.isr.application.account.model.entity.Account;
import jp.co.isr.application.account.model.entity.AccountLoginAudit;
import jp.co.isr.application.account.model.entity.AccountUserDetails;

/**
 * This service is used for auditing {@link Account} logins.
 *
 * @author Warren Nocos
 * @since 1.0
 * @version 1.0
 */
public interface AccountLoginAuditService {

    /**
     * Audits login activity.
     *
     * @param accountDto the {@link AccountDto} that the login activity is
     * associated
     * @throws AccountNotFoundException if no {@link Account} is found basing
     * specified {@link AccountDto}
     */
    void audit(@NotNull AccountDto accountDto) throws AccountNotFoundException;

    /**
     * Retrieves all unique {@link LocalDate} where there were
     * {@link AccountUserDetails} login activities, in pagination. Results are
     * sorted in ascending order.
     *
     * @param page the page
     * @param pageSize the page size
     * @return {@link Collection} of {@link LocalDate} of all the unique dates
     * in pagination
     */
    Collection<LocalDate> findDatesWithLoginActivityAscendingly(
            Optional<Integer> page, Optional<Integer> pageSize);

    /**
     * Retrieves all {@link AccountDto} with login activity. Results may be
     * filtered with specified start and end {@link AccountDto}. Results are
     * also in pagination, and sorted in an ascending order.
     *
     * @param start an {@link Optional} starting {@link LocalDate}, used to
     * filter results
     * @param end an {@link Optional} ending {@link LocalDate}, used to filter
     * results
     * @param page the page
     * @param pageSize the page size
     * @return {@link Collection} of {@link AccountDto}
     */
    Collection<AccountDto> findAccountsWithLoginActivityAscendingly(Optional<LocalDate> start,
            Optional<LocalDate> end, Optional<Integer> page, Optional<Integer> pageSize);

    /**
     * Retrieves accounts with login records. Results can be filtered using
     * specified arguments.
     *
     * @param start used to filter results through
     * {@link AccountLoginAudit#loginTime}
     * @param end used to filter results through
     * {@link AccountLoginAudit#loginTime}
     * @param emails used to filter results through {@link Account#email}
     * @param firstNames used to filter results through
     * {@link Account#firstName}
     * @param middleNames used to filter results through
     * {@link Account#middleName}
     * @param lastNames used to filter results through {@link Account#lastName}
     * @return
     */
    Map<String, Long> findFilteredAccountsWithLoginAudit(Optional<LocalDate> start,
            Optional<LocalDate> end, Collection<String> emails, Collection<String> firstNames,
            Collection<String> middleNames, Collection<String> lastNames);

}
