package com.tanisha.bfhl.service.impl;

import com.tanisha.bfhl.dto.BfhlRequestDto;
import com.tanisha.bfhl.dto.BfhlResponseDto;
import com.tanisha.bfhl.service.BfhlService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BfhlServiceImpl implements BfhlService {

    // ── Hardcoded user constants ───────────────────────────────────────────────
    private static final String USER_ID     = "Tanisha_Sirohi";
    private static final String EMAIL       = "Tanisha.sirohi.btech2023@sitpune.edu.in";
    private static final String ROLL_NUMBER = "23070122208";

    @Override
    public BfhlResponseDto processData(BfhlRequestDto request) {

        List<String> data = request.getData();

        List<String> oddNumbers       = new ArrayList<>();
        List<String> evenNumbers      = new ArrayList<>();
        List<String> alphabets        = new ArrayList<>();
        List<String> specialChars     = new ArrayList<>();
        long         numericSum       = 0;
        StringBuilder alphabetBuilder = new StringBuilder();

        for (String token : data) {

            // ── Pure numeric token (handles multi-digit like "334") ────────────
            if (isNumeric(token)) {
                long num = Long.parseLong(token);
                numericSum += num;

                if (num % 2 == 0) {
                    evenNumbers.add(token);          // returned as string per spec
                } else {
                    oddNumbers.add(token);
                }

            // ── Pure alphabetic token (handles multi-char like "ABCD") ─────────
            } else if (isAlphabetic(token)) {
                alphabets.add(token.toUpperCase());
                alphabetBuilder.append(token);       // collect original for concat

            // ── Special character token ────────────────────────────────────────
            } else {
                specialChars.add(token);
            }
        }

        String concatString = buildConcatString(alphabetBuilder.toString());

        return BfhlResponseDto.builder()
                .isSuccess(true)
                .userId(USER_ID)
                .email(EMAIL)
                .rollNumber(ROLL_NUMBER)
                .oddNumbers(oddNumbers)
                .evenNumbers(evenNumbers)
                .alphabets(alphabets)
                .specialCharacters(specialChars)
                .sum(String.valueOf(numericSum))
                .concatString(concatString)
                .build();
    }

    // ── Helpers ────────────────────────────────────────────────────────────────

    /**
     * Returns true if every character in the token is a digit.
     * Handles multi-digit strings like "334".
     */
    private boolean isNumeric(String token) {
        if (token == null || token.isEmpty()) return false;
        for (char c : token.toCharArray()) {
            if (!Character.isDigit(c)) return false;
        }
        return true;
    }

    /**
     * Returns true if every character in the token is a letter.
     * Handles multi-char strings like "ABCD".
     */
    private boolean isAlphabetic(String token) {
        if (token == null || token.isEmpty()) return false;
        for (char c : token.toCharArray()) {
            if (!Character.isLetter(c)) return false;
        }
        return true;
    }

    /**
     * Builds the concat_string:
     * 1. Take all alphabetical characters from all alphabet tokens.
     * 2. Reverse the full concatenated string.
     * 3. Apply alternating caps: index 0 → uppercase, index 1 → lowercase, etc.
     *
     * Example C: tokens ["A","ABCD","DOE"]
     *   concat  = "AABCDDOE"
     *   reversed = "EODDBCAA"  → wait, let's trace carefully:
     *   "A" + "ABCD" + "DOE" = "AABCDDOE"
     *   reversed              = "EODDC BAA" → "EODDCBAA"
     *   alternating           = E o D d C b A a  → "EoDdCbAa"  ✓
     */
    private String buildConcatString(String rawAlphabets) {
        if (rawAlphabets == null || rawAlphabets.isEmpty()) return "";

        // Reverse
        String reversed = new StringBuilder(rawAlphabets).reverse().toString();

        // Alternating caps
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < reversed.length(); i++) {
            char c = reversed.charAt(i);
            if (i % 2 == 0) {
                result.append(Character.toUpperCase(c));
            } else {
                result.append(Character.toLowerCase(c));
            }
        }
        return result.toString();
    }
}
