package com.pega.learning.memoryopt;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.math.BigDecimal;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

class CompanyV2 extends CompanyBase  {


    // NB: not final!
    private StorageKey storageKey;
    @Nullable
    private final LocalDate foundedDate;

    CompanyV2(String companyId,
              String companyName,
              String sanitizedName,
              String principalIndustry,
              BigDecimal dividendPaid,
              UnicornInfo unicornInfo, @Nullable LocalDate foundedDate, List<CompanyBase> subsidiaries) {
        super(companyId, companyName, sanitizedName, principalIndustry, dividendPaid, unicornInfo);
        this.foundedDate = foundedDate;

        // Return an immutable list!
        this.subsidiaries = List.copyOf(subsidiaries);
    }

    @Override
    List<CompanyBase> getSubsidiaries() {
        return subsidiaries;
    }

    @NonNull
    @Override
    public StorageKey getStorageKey() {
        // Add thread safety if needed
        if (storageKey == null) {
            storageKey = createStorageKeyFromId(companyId);
        }
        return storageKey;
    }

    @Override
    Optional<LocalDate> foundedDate() {
        return Optional.ofNullable(foundedDate);
    }

}
