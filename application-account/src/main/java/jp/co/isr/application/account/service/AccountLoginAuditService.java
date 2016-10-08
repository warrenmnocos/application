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
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import jp.co.isr.application.account.model.dto.AccountDto;
import jp.co.isr.application.account.model.dto.AccountRequestDto;
import jp.co.isr.application.account.model.entity.Account;
import jp.co.isr.application.account.model.entity.AccountUserDetails;

/**
 * This service is used for auditing {@link AccountDto} logins.
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
    void audit(AccountDto accountDto) throws AccountNotFoundException;

    /**
     * Retrieves all unique dates where there were {@link AccountUserDetails}
     * login activities. This is not efficient if plenty of
     * {@link AccountUserDetails} records are stored. Use
     * {@link AccountLoginAuditService#findDatesWithLoginActivity(java.lang.Integer, java.lang.Integer)}
     * instead to paginate results. Results are sorted in ascending order.
     *
     * @return {@link Set} of {@link LocalDate} of all the unique dates
     */
    Set<LocalDate> findDatesWithLoginActivityAscendingly();

    /**
     * Retrieves all unique {@link LocalDate} where there were
     * {@link AccountUserDetails} login activities, in pagination. Results are
     * sorted in ascending order.
     *
     * @param page the page
     * @param pageSize the page size
     * @return {@link Set} of {@link LocalDate} of all the unique dates in
     * pagination
     */
    Set<LocalDate> findDatesWithLoginActivityAscendingly(Integer page, Integer pageSize);

    /**
     * Retrieves all {@link AccountDto} with login activity. Results can be
     * filtered with specified start and end {@link AccountDto}. To paginate
     * results, use
     * {@link AccountLoginAuditService#findAccountsWithLoginActivity(java.util.Optional, java.util.Optional, java.lang.Integer, java.lang.Integer) }.
     * Results are in ascending order.
     *
     * @param start an {@link Optional} starting {@link LocalDate}, used to
     * filter results
     * @param end an {@link Set} ending {@link LocalDate}, used to filter
     * results
     * @return {@link HashSet} of {@link AccountDto}
     */
    Set<AccountDto> findAccountsWithLoginActivityAscendingly(Optional<LocalDate> start, Optional<LocalDate> end);

    /**
     * Retrieves all {@link AccountDto} with login activity. Results can be
     * filtered with specified start and end {@link AccountDto}. Results are
     * also in pagination, and sorted in an ascending order.
     *
     * @param start an {@link Optional} starting {@link LocalDate}, used to
     * filter results
     * @param end an {@link Optional} ending {@link LocalDate}, used to filter
     * results
     * @param page the page
     * @param pageSize the page size
     * @return {@link Set} of {@link AccountDto}
     */
    Set<AccountDto> findAccountsWithLoginActivityAscendingly(Optional<LocalDate> start,
            Optional<LocalDate> end, Integer page, Integer pageSize);

    /**
     * Retrieves accounts with login records. Results can be filtered using
     * {@link AccountRequestDto}. Use {@link AccountLoginAuditService#findFilteredAccountsWIthLoginAudit(jp.co.isr.application.account.model.dto.AccountRequestDto, java.lang.Integer, java.lang.Integer)
     * } to paginate results.
     *
     * @param accountRequestDto the {@link AccountRequestDto}
     * @return {@link Map} of {@link AccountDto} associated to the number of
     * {@link Long}
     */
    Map<AccountDto, Long> findFilteredAccountsWIthLoginAudit(AccountRequestDto accountRequestDto);

    /**
     * Retrieves accounts with login records. Results can be filtered using
     * {@link AccountRequestDto}. Results are also paginated.
     *
     * @param accountRequestDto the {@link AccountRequestDto}
     * @param page the page
     * @param pageSize the page size
     * @return {@link Map} of {@link AccountDto} associated to the number of
     * {@link Long}
     */
    Map<AccountDto, Long> findFilteredAccountsWIthLoginAudit(AccountRequestDto accountRequestDto,
            Integer page, Integer pageSize);

}
