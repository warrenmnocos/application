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
import jp.co.isr.application.account.model.entity.AccountClientDetails;
import jp.co.isr.application.account.model.entity.AccountClientGrantedAuthority;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * This is the {@link org.springframework.data.repository.Repository} for
 * {@link AccountClientGrantedAuthority}.
 *
 * @author Warren Nocos
 * @since 1.0
 * @version 1.0
 */
@Repository
public interface AccountClientGrantedAuthorityRepository
        extends PagingAndSortingRepository<AccountClientGrantedAuthority, Long> {

    /**
     * This queries for {@link AccountClientGrantedAuthority} using
     * {@link AccountClientGrantedAuthority#authority}
     *
     * @param authority the {@link AccountClientGrantedAuthority#authority} of
     * the {@link AccountClientDetails}
     * @return the {@link AccountClientGrantedAuthority}
     */
    Optional<AccountClientGrantedAuthority> findByAuthority(@Param("authority") String authority);

}
