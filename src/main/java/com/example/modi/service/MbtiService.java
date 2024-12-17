package com.example.modi.service;


import com.example.modi.dto.MbtiDTO;
import com.example.modi.dao.MbtiRepository;
import com.example.modi.dao.MbtiResult;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Transactional
public interface MbtiService {


    String calResult(MbtiDTO mbtiDto);
    void save(MbtiResult mbtiResult);

    // 데이터베이스에 저장된 전체 결과 수를 조회
    long getTotalCount();

    // 특정 MBTI 결과의 개수를 조회
    long getCountByResult(String result);

}
