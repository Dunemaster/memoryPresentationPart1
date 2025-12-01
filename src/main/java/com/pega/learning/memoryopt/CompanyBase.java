package com.pega.learning.memoryopt;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public abstract class CompanyBase {
    final String companyId;

    final String companyName;
    // Name with some special characters removed
    final String sanitizedName;

    final String principalIndustry;

    final BigDecimal dividendPaid;

    final UnicornInfo unicornInfo;

    protected List<CompanyBase> subsidiaries;

    @NonNull
    abstract StorageKey getStorageKey();

    abstract Optional<LocalDate> foundedDate();

    // Immutable list of subsidiaries
    abstract List<CompanyBase> getSubsidiaries();

    protected CompanyBase(String companyId, String companyName, String sanitizedName, String principalIndustry,
                          BigDecimal dividendPaid,
                          UnicornInfo unicornInfo) {
        this.companyId = companyId;
        this.companyName = companyName;
        this.sanitizedName = sanitizedName;
        this.principalIndustry = principalIndustry;
        this.dividendPaid = dividendPaid;
        this.unicornInfo = unicornInfo;
    }


    // Potentially expensive method to create a StorageKey from the companyId.
    // The resulting object size is 24 bytes, so it is quite small
    static StorageKey createStorageKeyFromId(String companyId) {
        int regionId = companyId.length();
        int shardId = Integer.parseInt(companyId.substring(4));
        return new StorageKey(regionId, shardId);
    }
}
