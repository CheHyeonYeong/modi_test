package com.example.modi.service;

import com.example.modi.dao.MbtiResult;
import com.example.modi.dto.MbtiDTO;
import com.example.modi.repository.MbtiRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class MbtiServiceTests {

    @Mock
    private MbtiRepository mbtiRepository;

    @InjectMocks
    private MbtiServiceImpl mbtiService;

    private List<String> mbtiExample;

    @BeforeEach
    void setUp() {
        mbtiExample = Arrays.asList(
                "ISTJ", "ISFJ", "INFJ", "INTJ",
                "ISTP", "ISFP", "INFP", "INTP",
                "ESTP", "ESFP", "ENFP", "ENTP",
                "ESTJ", "ESFJ", "ENFJ", "ENTJ"
        );
        log.info("MBTI examples initialized: {}", mbtiExample);
    }

    @Test
    void testCalResult() {
        // Given
        MbtiDTO mbtiDto = new MbtiDTO();
        // mbtiDto에 필요한 값들을 설정

        // When
        String result = mbtiService.calResult(mbtiDto);
        log.info("Calculated MBTI result: {}", result);

        // Then
        assertTrue(mbtiExample.contains(result));
    }

    @Test
    void testSave() {
        // Given
        MbtiResult mbtiResult = MbtiResult.builder()
                .result("INTJ")
                .build();

        when(mbtiRepository.save(any(MbtiResult.class))).thenReturn(mbtiResult);

        // When
        mbtiService.save(mbtiResult);
        log.info("Saved MBTI result: {}", mbtiResult);

        // Then
        verify(mbtiRepository, times(1)).save(any(MbtiResult.class));
    }

    @Test
    void testGetTotalCount() {
        // Given
        when(mbtiRepository.count()).thenReturn(100L);

        // When
        long totalCount = mbtiService.getTotalCount();
        log.info("Total MBTI count: {}", totalCount);

        // Then
        assertEquals(100L, totalCount);
        verify(mbtiRepository, times(1)).count();
    }

    @Test
    void testGetCountByResult() {
        // Given
        String mbtiType = "INTJ";
        when(mbtiRepository.countByResult(mbtiType)).thenReturn(10L);

        // When
        long count = mbtiService.getCountByResult(mbtiType);
        log.info("Count for MBTI type {}: {}", mbtiType, count);

        // Then
        assertEquals(10L, count);
        verify(mbtiRepository, times(1)).countByResult(mbtiType);
    }
}