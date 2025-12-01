package com.pega.learning.memoryopt;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

class CompanyV1_Unoptimized extends CompanyBase {

    // this a final field, nice!
    @NonNull
    private final StorageKey storageKey;
    private final Optional<LocalDate> foundedDate;

    CompanyV1_Unoptimized(String companyId,
                          String companyName,
                          String sanitizedName,
                          String principalIndustry,
                          BigDecimal dividendPaid,
                          UnicornInfo unicornInfo, Optional<LocalDate> foundedDate, List<CompanyBase> subsidiaries) {
        super(companyId,companyName,sanitizedName, principalIndustry, dividendPaid, unicornInfo);
        this.foundedDate = foundedDate;

        this.subsidiaries = Collections.unmodifiableList(subsidiaries);
        this.storageKey = createStorageKeyFromId(companyId);

    }

    @Override
    List<CompanyBase> getSubsidiaries() {
        return subsidiaries;
    }

    @Override
    @NonNull
    public StorageKey getStorageKey() {
        return storageKey;
    }

    @Override
    Optional<LocalDate> foundedDate() {
        return foundedDate;
    }
}
