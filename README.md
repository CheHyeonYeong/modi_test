# modi_test

## Arch
```
com.example.modi/
├── controller/
│   └── MbtiController         # 사용자의 HTTP 요청을 처리하는 컨트롤러
├── dao/
│   └── MbtiResult            # 데이터 접근 객체, 실제 결과값을 다루는 클래스
├── dto/
│   └── MbtiDTO               # 데이터 전송 객체, 계층 간 데이터 전달용
├── repository/
│   └── MbtiRepository        # 데이터베이스 접근 인터페이스
├── service/
│   ├── MbtiService          # 비즈니스 로직 인터페이스
│   └── MbtiServiceImpl      # 실제 비즈니스 로직 구현체
└── ModiApplication           # 스프링 부트 메인 애플리케이션
```

