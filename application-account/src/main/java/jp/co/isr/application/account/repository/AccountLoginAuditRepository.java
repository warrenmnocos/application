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
package jp.co.isr.application.account.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;
import javax.persistence.criteria.Predicate;
import jp.co.isr.application.account.model.entity.Account;
import jp.co.isr.application.account.model.entity.AccountLoginAudit;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * This is the {@link org.springframework.data.repository.Repository} for
 * {@link AccountLoginAudit}.
 *
 * @author Warren Nocos
 * @since 1.0
 * @version 1.0
 */
@Repository
public interface AccountLoginAuditRepository
        extends JpaRepository<AccountLoginAudit, Long>, JpaSpecificationExecutor<AccountLoginAudit> {

    /**
     * This retrieves all unique dates.
     * <br>
     * <br>
     * <b>Compatibility Note:</b>
     * <br>
     * This query is not a valid JPQL query based on standards as of JPA 2.1.
     * This query is tested on Hibernate. This query should be tested if there's
     * an intent to use other JPA providers aside from Hibernate.
     *
     * @param pageable the {@link Pageable}
     * @return the {@link Stream} of {@link Date}
     */
    @Query("SELECT DISTINCT CAST(a.loginTime AS date) FROM AccountLoginAudit a ORDER BY a.loginTime ASC")
    Stream<Date> findAllLoginDate(Pageable pageable);

    /**
     * Retrieves all {@link Account} with {@link AccountLoginAudit}.
     *
     * @param pageable the {@link Pageable}
     * @return the {@link Stream} of {@link Account}
     */
    @Query("SELECT DISTINCT a.account FROM AccountLoginAudit a ORDER BY a.account.email")
    Stream<Account> findAccountsWithLoginAudit(Pageable pageable);

    /**
     * Retrieves all {@link Account} with {@link AccountLoginAudit} from
     * {@code start} and before {@code end}.
     *
     * @param start the start {@link Date}
     * @param end the end {@link Date}
     * @param pageable the {@link Pageable}
     * @return the {@link Stream} of {@link Account}
     */
    @Query("SELECT DISTINCT a.account FROM AccountLoginAudit a WHERE a.loginTime >= :start AND a.loginTime <= :end ORDER BY a.account.email")
    Stream<Account> findAccountsBetweenLoginTimeInclusive(@Param("start") Date start, @Param("end") Date end, Pageable pageable);

    /**
     * Retrieves all {@link Account} with {@link AccountLoginAudit} from
     * {@code start}.
     *
     * @param start the start {@link Date}
     * @param pageable the {@link Pageable}
     * @return the {@link Stream} of {@link Account}
     */
    @Query("SELECT DISTINCT a.account FROM AccountLoginAudit a WHERE a.loginTime >= :start ORDER BY a.account.email")
    Stream<Account> findAccountsAfterLoginTimeInclusive(@Param("start") Date start, Pageable pageable);

    /**
     * Retrieves all {@link Account} with {@link AccountLoginAudit} before
     * {@code end}.
     *
     * @param end the end {@link Date}
     * @param pageable the {@link Pageable}
     * @return the {@link Stream} of {@link Account}
     */
    @Query("SELECT DISTINCT a.account FROM AccountLoginAudit a WHERE a.loginTime <= :end ORDER BY a.account.email")
    Stream<Account> findAccountsBeforeLoginTimeInclusive(@Param("end") Date end, Pageable pageable);

    /**
     * Retrieves all {@link AccountLoginAudit} based on specified filters.
     *
     * @param start the start {@link Date}
     * @param end the end {@link Date}
     * @param emails the {@code emails}, mapped to {@link Account#email}
     * @param firstNames the {@code firstNames}, mapped to
     * {@link Account#firstName}
     * @param middleNames the {@code middleNames}, mapped to
     * {@link Account#middleName}
     * @param lastNames the {@code lastNames}, mapped to
     * {@link Account#lastName}
     * @param pageable the {@link Pageable}
     * @return the {@link List} of {@link Account}
     */
    default List<AccountLoginAudit> findByFilters(Date start, Date end,
            Collection<String> emails, Collection<String> firstNames,
            Collection<String> middleNames, Collection<String> lastNames, Pageable pageable) {
        return findAll((root, criteriaQuery, criteriaBuilder) -> {
            Collection<Predicate> andPredicates = new ArrayList<>(4);

            // Start and End dates
            if (start != null && end != null) {
                andPredicates.add(criteriaBuilder.greaterThanOrEqualTo(root.<Date>get("loginTime"), start));
                andPredicates.add(criteriaBuilder.lessThanOrEqualTo(root.<Date>get("loginTime"), end));
            } else if (start != null) {
                andPredicates.add(criteriaBuilder.greaterThanOrEqualTo(root.<Date>get("loginTime"), start));
            } else if (end != null) {
                andPredicates.add(criteriaBuilder.lessThanOrEqualTo(root.<Date>get("loginTime"), end));
            }

            // Email - either of the emails
            emails.parallelStream()
                    .map(email -> criteriaBuilder.equal(root.get("account").get("email"), email))
                    .reduce(criteriaBuilder::or)
                    .ifPresent(andPredicates::add);

            // Firstname - either of the firstNames
            firstNames.parallelStream()
                    .map(firstName -> criteriaBuilder.equal(root.get("account").get("firstName"), firstName))
                    .reduce(criteriaBuilder::or)
                    .ifPresent(andPredicates::add);

            // Middlename - either of the middleNames
            middleNames.parallelStream()
                    .map(middleName -> criteriaBuilder.equal(root.get("account").get("middleName"), middleName))
                    .reduce(criteriaBuilder::or)
                    .ifPresent(andPredicates::add);

            // Lastname - either of the lastNames
            lastNames.parallelStream()
                    .map(lastName -> criteriaBuilder.equal(root.get("account").get("lastName"), lastName))
                    .reduce(criteriaBuilder::or)
                    .ifPresent(andPredicates::add);

            // Order by Email
            criteriaQuery.orderBy(criteriaBuilder.asc(root.get("account").get("email")));

            // Join predicates - explicitly present to all criteria
            return criteriaBuilder.and(andPredicates.toArray(new Predicate[andPredicates.size()]));
        }, pageable).getContent();
    }

}
