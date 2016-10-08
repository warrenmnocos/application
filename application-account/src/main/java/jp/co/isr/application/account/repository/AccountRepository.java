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

import java.util.Optional;
import java.util.stream.Stream;
import jp.co.isr.application.account.model.entity.Account;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * The {@link org.springframework.data.repository.Repository} for
 * {@link Account}.
 *
 * @author Warren Nocos
 * @since 1.0
 * @version 1.0
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    /**
     * Deletes an {@link Account} using {@link Account#email}.
     *
     * @param username the email
     */
    void deleteByEmail(@Param("email") String email);

    /**
     * Retrieves an {@link Account} using {@link Account#email}.
     *
     * @param email the email
     * @return the {@link Account}
     */
    Optional<Account> findByEmail(@Param("email") String email);

    /**
     * Retrieves an {@link Account} using {@link Account#id}.
     *
     * @param id the unique identifier
     * @return the {@link Account}
     */
    Optional<Account> findById(@Param("id") long id);

    /**
     * Retrieves all {@link Account}.
     *
     * @return {@link Stream} of {@link Account}
     */
    @Query("SELECT account FROM Account account")
    Stream<Account> findAllAsStream();

    /**
     * Retrieves all {@link Account} in pagination.
     *
     * @param pageable the {@link Pageable}
     * @return {@link Stream} of {@link Account}
     */
    @Query("SELECT account FROM Account account")
    Stream<Account> findAllAsPagedStream(Pageable pageable);

}
