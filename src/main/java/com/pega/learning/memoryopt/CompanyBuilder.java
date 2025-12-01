package com.pega.learning.memoryopt;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;


class CompanyBuilder {

    // For unlimited caches ConcurrentHashMap can be used
    // The Caffeine cache is used here to limit the size of the cache.
    // The cache here sits in a static field, but it can be moved to some deserialization context, for example
    private static final Cache<String, String> INDUSTRY_CACHE =
            Caffeine.newBuilder().maximumSize(5000).build();

    final String customerId;

    final String customerName;

    final String principalIndustry;
    @Nullable
    private final LocalDate foundedDate;

    private final List<CompanyBase> subsidiaries = new ArrayList<>(4);

    private BigDecimal dividendPaid;
    private int unicornYear;
    private Integer marketCapInBillions;

    public CompanyBuilder(String customerId, String customerName, String principalIndustry,
                          @Nullable LocalDate foundedDate) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.principalIndustry = principalIndustry;
        this.foundedDate = foundedDate;
    }

    public void addSubsidiary(CompanyBase subsidiary) {
        subsidiaries.add(subsidiary);
    }

    public void setDividendPaid(BigDecimal dividendPaid) {
        this.dividendPaid = dividendPaid;
    }

    public void addUnicornInfo(int year, int marketCapInBillions) {
        this.unicornYear = year;
        this.marketCapInBillions = marketCapInBillions;
    }

    public CompanyBase build(boolean useOptimizedVersion) {
        if (useOptimizedVersion) {
            return buildOptimized();
        } else {
            UnicornInfo unicornInfo = new UnicornInfo(unicornYear != 0,
                    unicornYear != 0 ? unicornYear : null,
                    unicornYear != 0 ? marketCapInBillions : null);

            String sanitizedName = sanitizeName(customerName);
            return new CompanyV1_Unoptimized(customerId, customerName, sanitizedName,
                    principalIndustry, dividendPaid,
                    unicornInfo,
                    Optional.ofNullable(foundedDate),
                    subsidiaries);
        }
    }


    private CompanyV2 buildOptimized() {

        // this should happen probably in the reader
        BigDecimal cachedDividendPayed = dividendPaid.compareTo(BigDecimal.ZERO) == 0
                ? BigDecimal.ZERO
                : dividendPaid;

        UnicornInfo unicornInfo = getUnicornInfoOptimized();
        // !
        String cachedIndustry = getIndustryOptimized();

        String sanitizedName = sanitizeNameOptimized(customerName);

        return new CompanyV2(customerId, customerName, sanitizedName, cachedIndustry, cachedDividendPayed,
                unicornInfo,
                foundedDate,
                subsidiaries);
    }

    private String getIndustryOptimized() {
        return INDUSTRY_CACHE.get(principalIndustry, Function.identity());
    }

    private UnicornInfo getUnicornInfoOptimized() {
        return UnicornInfo.createUnicornInfo(
                unicornYear != 0,
                unicornYear != 0 ? unicornYear : null,
                unicornYear != 0 ? marketCapInBillions : null);
    }


    private static String sanitizeName(String name) {
        //NB: this problem hides from YourKit inspection as there are only two copies, but the impact is around 200K!

        // I have seen such pattern not only with strings, but also with array filtering/sanitization

        StringBuilder sb = new StringBuilder(name.length());
        for (int j = 0; j < name.length(); j++) {
            char c = name.charAt(j);
            // Emulate reading from a file
            if (c != '&' && c != '%' && c != '$' && c != '@') {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private static String sanitizeNameOptimized(String name) {
        StringBuilder sb = null;
        for (int j = 0; j < name.length(); j++) {
            char c = name.charAt(j);
            // Emulate reading from a file
            if (sb != null) {
                sb.append(c);
            } else if (c == '&' || c == '%' || c == '$' || c == '@')  {
                sb = new StringBuilder(name.length());
                sb.append(name, 0, j+1);
            }
        }

        if (sb != null) {
            return sb.toString();
        } else {
            return name; // No characters to sanitize, return original name
        }
    }


}
