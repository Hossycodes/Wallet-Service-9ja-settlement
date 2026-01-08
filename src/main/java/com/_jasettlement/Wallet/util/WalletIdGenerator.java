package com._jasettlement.Wallet.util;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class WalletIdGenerator {
    private static final SecureRandom RNG = new SecureRandom();
    private static final DateTimeFormatter MMDD = DateTimeFormatter.ofPattern("MMdd");
    private static final String PREFIX = "WALNG";

    private WalletIdGenerator() {}

    public static String generate() {
        String mmdd = LocalDate.now().format(MMDD); // first 4 digits: month and day
        int randomSix = RNG.nextInt(1_000_000); // 0 .. 999999
        String sixDigits = String.format("%06d", randomSix);
        return PREFIX + mmdd + sixDigits; // total prefix + 10 digits
    }
}
