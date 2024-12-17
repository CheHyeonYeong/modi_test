package com.example.modi.dao;

import com.example.modi.repository.MbtiRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import lombok.extern.log4j.Log4j2;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.IntStream;


// JUnit Test
@Log4j2
@SpringBootTest
public class MbtiRepositoryTests {

    @Autowired
    private MbtiRepository mbtiRepository;

    private List<String> mbtiExample = Arrays.asList(
            "ISTJ", "ISFJ", "INFJ", "INTJ",
            "ISTP", "ISFP", "INFP", "INTP",
            "ESTP", "ESFP", "ENFP", "ENTP",
            "ESTJ", "ESFJ", "ENFJ", "ENTJ"
    );

    // CUID TEST

    // Create
    @Test
    public void testInsert() {
        IntStream.range(1,10).forEach(i -> {
            MbtiResult mbtiResult = MbtiResult.builder()
                    .result(mbtiExample.get(i%16))
                    .build();
            mbtiRepository.save(mbtiResult);
        });
    }


    // Read2 - find all
    @Test
    public void testFindAll(){
        List<MbtiResult> mbtis = mbtiRepository.findAll();
        mbtis.forEach(mbti -> {
            log.info(mbti.toString());
        } );
    }

    // Read
    @Test
    public void testSelect() {
        Long id = Math.abs(new Random().nextLong()) % 100L;  // 0 ~ 99 범위

        Optional<MbtiResult> result = mbtiRepository.findById(id);  //optional Type으로 받아서 처리해야 함
        MbtiResult mbtiResult = result.orElseThrow();

        log.info(mbtiResult);
    }



    // Read
    public void testSelect(Long id) {

        Optional<MbtiResult> result = mbtiRepository.findById(id);  //optional Type으로 받아서 처리해야 함
        MbtiResult mbtiResult = result.orElseThrow();

        log.info(mbtiResult);
    }

    // Update
    @Test
    public void testUpdate(){
        Long id = Math.abs(new Random().nextLong()) % 100L;  // 0 ~ 99 범위

        log.info("Before Update : "+id );
        testSelect(id);
        Optional<MbtiResult> result = mbtiRepository.findById(id);

        MbtiResult mbtiResult = result.orElseThrow();
        mbtiResult.updateMbti("update mbti");

        mbtiRepository.save(mbtiResult);
        log.info("After Update : " + id);
        testSelect(id);

    }

    // Delete
    @Test
    public void testDelete() {
        Long id = 1L;

        Optional<MbtiResult> result = mbtiRepository.findById(id);
        MbtiResult mbtiResult = result.orElseThrow();

        mbtiRepository.delete(mbtiResult);
    }



}
