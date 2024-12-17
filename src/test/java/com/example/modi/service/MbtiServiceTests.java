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
@ExtendWith(MockitoExtension.class)  // JUnit5에서 Mockito를 사용하기 위한 확장 설정
public class MbtiServiceTests {

    @Mock  // MbtiRepository의 가짜(mock) 객체 생성
    private MbtiRepository mbtiRepository;

    @InjectMocks  // Mock 객체를 자동으로 주입받는 실제 서비스 객체
    private MbtiServiceImpl mbtiService;

    private List<String> mbtiExample;

    @BeforeEach  // 각 테스트 메소드 실행 전에 실행되는 초기화 메소드
    void setUp() {
        // 테스트에 사용할 MBTI 유형들을 리스트로 초기화
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
        // Given (준비): 테스트 데이터 설정
        MbtiDTO mbtiDto = new MbtiDTO();
        // mbtiDto에 필요한 값들을 설정해야 함

        // When (실행): 테스트할 메소드 실행
        String result = mbtiService.calResult(mbtiDto);
        log.info("Calculated MBTI result: {}", result);

        // Then (검증): 결과 검증
        // 계산된 결과가 유효한 MBTI 유형 중 하나인지 확인
        assertTrue(mbtiExample.contains(result));
    }

    @Test
    void testSave() {
        // Given: 테스트용 MbtiResult 객체 생성
        MbtiResult mbtiResult = MbtiResult.builder()
                .result("INTJ")
                .build();

        // Mock 객체의 동작 정의: save 메소드 호출 시 mbtiResult를 반환하도록 설정
        when(mbtiRepository.save(any(MbtiResult.class))).thenReturn(mbtiResult);

        // When: 저장 메소드 실행
        mbtiService.save(mbtiResult);
        log.info("Saved MBTI result: {}", mbtiResult);

        // Then: repository의 save 메소드가 정확히 1번 호출되었는지 검증
        verify(mbtiRepository, times(1)).save(any(MbtiResult.class));
    }

    @Test
    void testGetTotalCount() {
        // Given: Mock 객체가 count 메소드 호출 시 100을 반환하도록 설정
        when(mbtiRepository.count()).thenReturn(100L);

        // When: 전체 개수를 조회
        long totalCount = mbtiService.getTotalCount();
        log.info("Total MBTI count: {}", totalCount);

        // Then:
        // 1. 반환된 값이 100인지 확인
        assertEquals(100L, totalCount);
        // 2. count 메소드가 정확히 1번 호출되었는지 검증
        verify(mbtiRepository, times(1)).count();
    }

    @Test
    void testGetCountByResult() {
        // Given: 특정 MBTI 유형의 개수를 10으로 설정
        String mbtiType = "INTJ";
        when(mbtiRepository.countByResult(mbtiType)).thenReturn(10L);

        // When: 특정 MBTI 유형의 개수를 조회
        long count = mbtiService.getCountByResult(mbtiType);
        log.info("Count for MBTI type {}: {}", mbtiType, count);

        // Then:
        // 1. 반환된 값이 10인지 확인
        assertEquals(10L, count);
        // 2. countByResult 메소드가 정확히 1번 호출되었는지 검증
        verify(mbtiRepository, times(1)).countByResult(mbtiType);
    }
}