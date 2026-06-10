package com.tanisha.bfhl.service;

import com.tanisha.bfhl.dto.BfhlRequestDto;
import com.tanisha.bfhl.dto.BfhlResponseDto;

public interface BfhlService {

    /**
     * Processes the input data array and returns categorized results.
     *
     * @param request the incoming request containing the data array
     * @return BfhlResponseDto with categorized numbers, alphabets, special chars, sum, and concat string
     */
    BfhlResponseDto processData(BfhlRequestDto request);
}
