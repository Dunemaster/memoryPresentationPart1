package com.pega.learning.memoryopt;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static com.pega.learning.memoryopt.BiasedDateGenerator.dateBetween1950And2025;


public class Main {

    private static final String[] INDUSTRIES = {
            "Technology", "Finance", "Healthcare", "Retail", "Manufacturing", "Education", "Energy", "Transportation",
            "Telecommunications", "Hospitality", "Real Estate", "Pharmaceuticals", "Automotive", "Aerospace", "Agriculture",
            "Construction", "Media", "Entertainment", "Food & Beverage", "Insurance", "Legal", "Consulting", "Logistics",
    };
    public static final int CUSTOMERS_NUMBER = 30_000;

    private static Random random = new Random();

    public static void main(String[] args) throws IOException {

        boolean useOptimizedVersion = false;
        List<CompanyBase> enterpriseCustomers = new ArrayList<>(CUSTOMERS_NUMBER);
        int storageKeysRetrieved = 0;

        // Suppose, we are reading from a file or database
        for (int i = 0; i < CUSTOMERS_NUMBER; i++) {
            // Lots of objects, but probably cannot be easily optimized
            String key = "CST-" + i;
            // 10% companies have a special character in their name
            String specialCharacter = (i % 10 == 0) ? "@" : "";
            String name = "Customer" + specialCharacter + i;

            String industry = readIndustry(i);
            BigDecimal dividendPaid = readDividendPaid(i);

            // half of the companies half foundation date known
            @Nullable LocalDate foundedDate = (i % 2 == 0)
                    ? dateBetween1950And2025()
                    : null;

            CompanyBuilder companyBuilder = new CompanyBuilder(key, name, industry, foundedDate);
            if (i > 10 && i % 10 == 0) {
                // Add subsidiaries for every 10th customer
                for (int j = 1; j <= 3; j++) {
                    companyBuilder.addSubsidiary(enterpriseCustomers.get(i-j));
                }
            }
            if (i % 5 == 0) {

                companyBuilder.addUnicornInfo(random.nextInt(25) +  2000,
                        random.nextInt(10));
            }
            companyBuilder.setDividendPaid(dividendPaid);

            // Creating object
            CompanyBase company = companyBuilder.build(useOptimizedVersion);


            //For 20% of customers, we will retrieve a StorageKey
            if (i % 5 == 0) {
                StorageKey storageKey = company.getStorageKey();
                storageKeysRetrieved += (storageKey != null ? 1 : 0);
            }
            if (i % 100 == 0) {
                company.foundedDate().ifPresent(d ->
                        {
                            System.out.println(company.companyName
                                    + " was founded on "
                                    + d);
                        });
            }
            enterpriseCustomers.add(company);
        }

        System.out.println("Optimized:" + useOptimizedVersion + " Finished reading " + enterpriseCustomers.size() + " customers from " + Main.INDUSTRIES.length + " industries.");
        System.in.read();

    }

    private static BigDecimal readDividendPaid(int i) {
        // reading from file of json payload
        // half of the companies paid dividends
        String dividendString  = (i % 2 == 0)
                ? "0"
                : String.valueOf(new Random().nextInt(1000) + 1);

        return new BigDecimal(dividendString);

    }

    private static String readIndustry(int i) {
        Random random = new Random();
        // emulate reading a database or file
        return new String(Main.INDUSTRIES[random.nextInt(Main.INDUSTRIES.length)]);
    }

}