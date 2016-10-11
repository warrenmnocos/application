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

import jp.co.isr.application.account.service.exception.GrantedAuthorityNotFoundException;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import jp.co.isr.application.account.model.dto.AccountDto;
import jp.co.isr.application.account.model.dto.AccountWithUserDetailsDto;
import jp.co.isr.application.account.model.dto.AddressDto;
import jp.co.isr.application.account.model.entity.Account;
import jp.co.isr.application.account.model.entity.AccountUserDetails;
import jp.co.isr.application.account.model.entity.AccountUserGrantedAuthority;
import jp.co.isr.application.account.model.entity.Address;
import jp.co.isr.application.account.repository.AccountUserGrantedAuthorityRepository;
import jp.co.isr.application.account.service.AccountModelService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * This is a concrete implementation of {@link AccountModelService}.
 *
 * @author Warren Nocos
 * @since 1.0
 * @version 1.0
 */
@Service
@Validated
public class AccountModelServiceImpl implements AccountModelService {

    /**
     * This is used for encrypting password when creating
     * {@link AccountUserDetails}
     */
    protected final PasswordEncoder passwordEncoder;

    /**
     * This is used for looking up {@link AccountUserGrantedAuthority}
     */
    protected final AccountUserGrantedAuthorityRepository accountUserGrantedAuthorityRepository;

    /**
     * Constructs an instance of {@link AccountModelService}.
     *
     * @param passwordEncoder used for encrypting password when creating
     * {@link AccountUserDetails}
     * @param accountUserGrantedAuthorityRepository used for looking up
     * {@link AccountUserGrantedAuthority}
     */
    @Inject
    public AccountModelServiceImpl(@Named("bCryptPasswordEncoder") PasswordEncoder passwordEncoder,
            AccountUserGrantedAuthorityRepository accountUserGrantedAuthorityRepository) {
        this.passwordEncoder = passwordEncoder;
        this.accountUserGrantedAuthorityRepository = accountUserGrantedAuthorityRepository;
    }

    /**
     * {@inheritDoc }
     * {@link Iterable} properties from {@link Account} are appended to the
     * mapped {@link Iterable} properties of the {@link AccountDto}. Other
     * properties are copied by value. Properties used for auditing are not
     * processed.
     */
    @Override
    @Validated
    public void copyProperties(@NotNull Account account, @NotNull AccountDto accountDto) {
        accountDto.setId(account.getId());
        accountDto.setEmail(account.getEmail());
        accountDto.setFirstName(account.getFirstName());
        accountDto.setMiddleName(account.getMiddleName());
        accountDto.setLastName(account.getLastName());
        if (account.getAddresses() != null) {
            HashMap<String, AddressDto> copiedAddresses = account.getAddresses().entrySet()
                    .parallelStream()
                    .reduce(new HashMap<>(),
                            (map, entry) -> {
                                map.put(entry.getKey(), toAddressDto(entry.getValue()));
                                return map;
                            },
                            (previousMap, currentMap) -> {
                                previousMap.putAll(currentMap);
                                return previousMap;
                            });
            accountDto.setAddresses(Optional.ofNullable(accountDto.getAddresses())
                    .map(originalAddress -> {
                        originalAddress.putAll(copiedAddresses);
                        return originalAddress;
                    })
                    .orElseGet(() -> copiedAddresses));
        }
        if (account.getContacts() != null) {
            accountDto.setContacts(Optional.ofNullable(accountDto.getContacts())
                    .map(originalAddress -> {
                        originalAddress.putAll(account.getContacts());
                        return originalAddress;
                    })
                    .orElseGet(() -> account.getContacts()));
        }
    }

    /**
     * {@inheritDoc }
     * {@link Iterable} properties from {@link AccountDto} are appended to the
     * mapped {@link Iterable} properties of the {@link Account} . Other
     * properties are copied by value. Properties used for auditing are not
     * processed.
     */
    @Override
    @Validated
    public void copyProperties(@NotNull AccountDto accountDto, @NotNull Account account) {
        account.setId(accountDto.getId());
        account.setEmail(accountDto.getEmail());
        account.setFirstName(accountDto.getFirstName());
        account.setMiddleName(accountDto.getMiddleName());
        account.setLastName(accountDto.getLastName());
        if (accountDto.getAddresses() != null) {
            HashMap<String, Address> copiedAddresses = accountDto.getAddresses().entrySet()
                    .parallelStream()
                    .reduce(new HashMap<>(),
                            (map, entry) -> {
                                map.put(entry.getKey(), toAddress(entry.getValue()));
                                return map;
                            },
                            (previousMap, currentMap) -> {
                                previousMap.putAll(currentMap);
                                return previousMap;
                            });
            account.setAddresses(Optional.ofNullable(account.getAddresses())
                    .map(originalAddress -> {
                        originalAddress.putAll(copiedAddresses);
                        return originalAddress;
                    })
                    .orElseGet(() -> copiedAddresses));
        }
        if (accountDto.getContacts() != null) {
            account.setContacts(Optional.ofNullable(account.getContacts())
                    .map(originalAddress -> {
                        originalAddress.putAll(accountDto.getContacts());
                        return originalAddress;
                    })
                    .orElseGet(() -> accountDto.getContacts()));
        }
    }

    /**
     * {@inheritDoc }
     * Implementation will be provided once needed.
     */
    @Override
    public void copyProperties(@NotNull Account account, @NotNull AccountWithUserDetailsDto accountWithUserDetailsDto) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * {@inheritDoc }
     * {@link Iterable} properties from {@link AccountWithUserDetailsDto} are
     * appended to the mapped {@link Iterable} properties of the {@link Account}
     * . Other properties are copied by value. Properties used for auditing are
     * not processed.
     */
    @Override
    public void copyProperties(@NotNull AccountWithUserDetailsDto accountWithUserDetailsDto, @NotNull Account account) {
        account.setId(accountWithUserDetailsDto.getId());
        account.setEmail(accountWithUserDetailsDto.getEmail());
        account.setFirstName(accountWithUserDetailsDto.getFirstName());
        account.setMiddleName(accountWithUserDetailsDto.getMiddleName());
        account.setLastName(accountWithUserDetailsDto.getLastName());
        account.setAddresses(accountWithUserDetailsDto.getAddresses().entrySet()
                .parallelStream()
                .reduce(new HashMap<>(),
                        (map, entry) -> {
                            map.put(entry.getKey(), toAddress(entry.getValue()));
                            return map;
                        },
                        (previousMap, currentMap) -> {
                            previousMap.putAll(currentMap);
                            return previousMap;
                        }));
        account.setContacts(accountWithUserDetailsDto.getContacts());
    }

    /**
     * {@inheritDoc }
     * Implementation will be provided once needed.
     */
    @Override
    public void copyProperties(@NotNull AccountUserDetails accountUserDetails, @NotNull AccountWithUserDetailsDto accountWithUserDetailsDto) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * {@inheritDoc }
     * {@link Iterable} properties from {@link AccountWithUserDetailsDto} are
     * appended to the mapped {@link Iterable} properties of the
     * {@link AccountUserDetails} . Other properties are copied by value.
     * Properties used for auditing are not processed.
     */
    @Override
    public void copyProperties(@NotNull AccountWithUserDetailsDto accountWithUserDetailsDto, @NotNull AccountUserDetails accountUserDetails) {
        accountUserDetails.setId(accountWithUserDetailsDto.getId());
        accountUserDetails.setAccountNonExpired(accountWithUserDetailsDto.isAccountNonExpired());
        accountUserDetails.setAccountNonLocked(accountWithUserDetailsDto.isAccountNonLocked());
        accountUserDetails.setCredentialsNonExpired(accountWithUserDetailsDto.isCredentialsNonExpired());
        accountUserDetails.setEnabled(accountWithUserDetailsDto.isEnabled());
        accountUserDetails.setPassword(passwordEncoder.encode(accountWithUserDetailsDto.getPassword()));
        accountUserDetails.setUsername(accountWithUserDetailsDto.getEmail());
        accountUserDetails.setGrantedAuthorities(accountWithUserDetailsDto.getGrantedAuthorities().stream()
                .filter(Objects::nonNull)
                .map(accountUserGrantedAuthorityRepository::findByAuthority)
                .map(grantedAuthority -> grantedAuthority.orElseThrow(() -> new GrantedAuthorityNotFoundException("GrantedAuthority not found")))
                .collect(Collectors.toSet()));
    }

    /**
     * {@inheritDoc }
     */
    @Override
    @Validated
    public Account toAccount(@NotNull AccountWithUserDetailsDto accountWithCredentialsDto) {
        Account account = new Account();
        copyProperties(accountWithCredentialsDto, account);
        return account;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    @Validated
    public AccountDto toAccountDto(@NotNull Account account) {
        AccountDto accountDto = new AccountDto();
        copyProperties(account, accountDto);
        return accountDto;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    @Validated
    public Account toAccount(@NotNull AccountDto accountDto) {
        Account account = new Account();
        copyProperties(accountDto, account);
        return account;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    @Validated
    public AccountUserDetails toAccountUserDetails(@NotNull AccountWithUserDetailsDto accountWithCredentialsDto) {
        AccountUserDetails accountUserDetails = new AccountUserDetails();
        copyProperties(accountWithCredentialsDto, accountUserDetails);
        return accountUserDetails;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    @Validated
    public AddressDto toAddressDto(@NotNull Address address) {
        AddressDto addressDto = new AddressDto();
        addressDto.setCity(address.getCity());
        addressDto.setTown(address.getTown());
        addressDto.setCountry(address.getCountry());
        addressDto.setZipCode(address.getZipCode());
        return addressDto;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    @Validated
    public Address toAddress(@NotNull AddressDto addressDto) {
        Address address = new Address();
        address.setCity(addressDto.getCity());
        address.setTown(addressDto.getTown());
        address.setCountry(addressDto.getCountry());
        address.setZipCode(addressDto.getZipCode());
        return address;
    }

}
