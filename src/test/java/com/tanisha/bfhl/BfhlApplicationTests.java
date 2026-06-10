package com.tanisha.bfhl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tanisha.bfhl.dto.BfhlRequestDto;
import com.tanisha.bfhl.dto.BfhlResponseDto;
import com.tanisha.bfhl.service.BfhlService;
import com.tanisha.bfhl.service.impl.BfhlServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class BfhlApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private BfhlService bfhlService;

    @BeforeEach
    void setUp() {
        bfhlService = new BfhlServiceImpl();
    }

    // ── Service Unit Tests ─────────────────────────────────────────────────────

    @Test
    @DisplayName("Example A: mixed array with single-char elements")
    void testExampleA() {
        BfhlRequestDto request = new BfhlRequestDto();
        request.setData(Arrays.asList("a", "1", "334", "4", "R", "$"));

        BfhlResponseDto response = bfhlService.processData(request);

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getUserId()).isEqualTo("sameer_shekhar_14042004");
        assertThat(response.getEmail()).isEqualTo("sameer.shekhar.btech2023@sitpune.edu.in");
        assertThat(response.getRollNumber()).isEqualTo("23070122191");

        assertThat(response.getOddNumbers()).containsExactly("1");
        assertThat(response.getEvenNumbers()).containsExactlyInAnyOrder("334", "4");
        assertThat(response.getAlphabets()).containsExactlyInAnyOrder("A", "R");
        assertThat(response.getSpecialCharacters()).containsExactly("$");
        assertThat(response.getSum()).isEqualTo("339");
        // concat: a + R → "aR" reversed → "Ra" → alternating caps → "Ra"
        assertThat(response.getConcatString()).isEqualTo("Ra");
    }

    @Test
    @DisplayName("Example B: mixed array with multiple special characters")
    void testExampleB() {
        BfhlRequestDto request = new BfhlRequestDto();
        request.setData(Arrays.asList("2", "a", "y", "4", "&", "-", "*", "5", "92", "b"));

        BfhlResponseDto response = bfhlService.processData(request);

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getOddNumbers()).containsExactly("5");
        assertThat(response.getEvenNumbers()).containsExactlyInAnyOrder("2", "4", "92");
        assertThat(response.getAlphabets()).containsExactlyInAnyOrder("A", "Y", "B");
        assertThat(response.getSpecialCharacters()).containsExactlyInAnyOrder("&", "-", "*");
        assertThat(response.getSum()).isEqualTo("103");
        // concat: a+y+b = "ayb" reversed = "bya" alternating = "ByA"
        assertThat(response.getConcatString()).isEqualTo("ByA");
    }

    @Test
    @DisplayName("Example C: multi-char alphabet tokens only")
    void testExampleC() {
        BfhlRequestDto request = new BfhlRequestDto();
        request.setData(Arrays.asList("A", "ABCD", "DOE"));

        BfhlResponseDto response = bfhlService.processData(request);

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getOddNumbers()).isEmpty();
        assertThat(response.getEvenNumbers()).isEmpty();
        assertThat(response.getAlphabets()).containsExactly("A", "ABCD", "DOE");
        assertThat(response.getSpecialCharacters()).isEmpty();
        assertThat(response.getSum()).isEqualTo("0");
        // concat: A+ABCD+DOE = "AABCDDOE" reversed = "EODDCBAA"
        // alternating: E o D d C b A a → "EoDdCbAa"
        assertThat(response.getConcatString()).isEqualTo("EoDdCbAa");
    }

    @Test
    @DisplayName("Empty data array returns zeros and empty lists")
    void testEmptyArray() {
        BfhlRequestDto request = new BfhlRequestDto();
        request.setData(Collections.emptyList());

        BfhlResponseDto response = bfhlService.processData(request);

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getOddNumbers()).isEmpty();
        assertThat(response.getEvenNumbers()).isEmpty();
        assertThat(response.getAlphabets()).isEmpty();
        assertThat(response.getSpecialCharacters()).isEmpty();
        assertThat(response.getSum()).isEqualTo("0");
        assertThat(response.getConcatString()).isEmpty();
    }

    @Test
    @DisplayName("Numbers only: no alphabets or special chars")
    void testNumbersOnly() {
        BfhlRequestDto request = new BfhlRequestDto();
        request.setData(Arrays.asList("1", "2", "3", "10"));

        BfhlResponseDto response = bfhlService.processData(request);

        assertThat(response.getOddNumbers()).containsExactlyInAnyOrder("1", "3");
        assertThat(response.getEvenNumbers()).containsExactlyInAnyOrder("2", "10");
        assertThat(response.getAlphabets()).isEmpty();
        assertThat(response.getSpecialCharacters()).isEmpty();
        assertThat(response.getSum()).isEqualTo("16");
        assertThat(response.getConcatString()).isEmpty();
    }

    @Test
    @DisplayName("Zero is treated as even number")
    void testZeroIsEven() {
        BfhlRequestDto request = new BfhlRequestDto();
        request.setData(List.of("0"));

        BfhlResponseDto response = bfhlService.processData(request);

        assertThat(response.getEvenNumbers()).containsExactly("0");
        assertThat(response.getOddNumbers()).isEmpty();
        assertThat(response.getSum()).isEqualTo("0");
    }

    // ── Controller Integration Tests ───────────────────────────────────────────

    @Test
    @DisplayName("POST /bfhl returns 200 with valid payload")
    void testPostBfhlEndpointSuccess() throws Exception {
        String requestJson = """
                {
                    "data": ["a", "1", "334", "4", "R", "$"]
                }
                """;

        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.is_success").value(true))
                .andExpect(jsonPath("$.user_id").value("sameer_shekhar_14042004"))
                .andExpect(jsonPath("$.email").value("sameer.shekhar.btech2023@sitpune.edu.in"))
                .andExpect(jsonPath("$.roll_number").value("23070122191"))
                .andExpect(jsonPath("$.sum").value("339"))
                .andExpect(jsonPath("$.concat_string").value("Ra"));
    }

    @Test
    @DisplayName("POST /bfhl returns 400 when data field is null")
    void testPostBfhlEndpointNullData() throws Exception {
        String requestJson = """
                {
                    "data": null
                }
                """;

        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.is_success").value(false));
    }

    @Test
    @DisplayName("POST /bfhl returns 400 for malformed JSON")
    void testPostBfhlEndpointMalformedJson() throws Exception {
        String badJson = "{ bad json }";

        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.is_success").value(false));
    }

    @Test
    @DisplayName("POST /bfhl Example B integration test")
    void testPostBfhlExampleB() throws Exception {
        String requestJson = """
                {
                    "data": ["2", "a", "y", "4", "&", "-", "*", "5", "92", "b"]
                }
                """;

        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sum").value("103"))
                .andExpect(jsonPath("$.concat_string").value("ByA"));
    }

    @Test
    @DisplayName("POST /bfhl Example C integration test")
    void testPostBfhlExampleC() throws Exception {
        String requestJson = """
                {
                    "data": ["A", "ABCD", "DOE"]
                }
                """;

        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sum").value("0"))
                .andExpect(jsonPath("$.concat_string").value("EoDdCbAa"));
    }
}
