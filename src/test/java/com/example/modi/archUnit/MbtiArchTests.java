package com.example.modi.archUnit;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition;
import org.springframework.stereotype.Service;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Controller;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

@AnalyzeClasses(packages = "com.example.modi")  // 검사할 패키지 범위 지정
public class MbtiArchTests {

    // 레이어드 아키텍처 규칙 검증
    // 각 계층간의 의존성 방향을 정의하고 검증
    @ArchTest
    static final ArchRule layer_dependencies_are_respected = layeredArchitecture()
            .consideringAllDependencies()
            // 각 계층 정의
            .layer("Controller").definedBy("..controller..")
            .layer("Service").definedBy("..service..")
            .layer("Repository").definedBy("..repository..")
            .layer("DAO").definedBy("..dao..")
            .layer("DTO").definedBy("..dto..")

            // 계층간 접근 규칙 정의
            .whereLayer("Controller").mayNotBeAccessedByAnyLayer()  // Controller는 어떤 계층에서도 접근 불가
            .whereLayer("Service").mayOnlyBeAccessedByLayers("Controller")  // Service는 Controller만 접근 가능
            .whereLayer("Repository").mayOnlyBeAccessedByLayers("Service")  // Repository는 Service만 접근 가능
            .whereLayer("DAO").mayOnlyBeAccessedByLayers("Service", "Repository");  // DAO는 Service와 Repository만 접근 가능

    // 클래스 명명 규칙 검증
    // @Service 어노테이션이 붙은 클래스는 이름이 "Service"로 끝나야 함
    @ArchTest
    static final ArchRule services_should_be_named_correctly = classes()
            .that().areAnnotatedWith(Service.class)
            .should().haveSimpleNameEndingWith("Service");

    // @Repository 어노테이션이 붙은 클래스는 이름이 "Repository"로 끝나야 함
    @ArchTest
    static final ArchRule repositories_should_be_named_correctly = classes()
            .that().areAnnotatedWith(Repository.class)
            .should().haveSimpleNameEndingWith("Repository");

    // 패키지 의존성 규칙 검증
    // service 패키지의 클래스들은 controller와 service 패키지에서만 접근 가능
    @ArchTest
    static final ArchRule services_should_only_be_accessed_by_controllers = classes()
            .that().resideInAPackage("..service..")
            .should().onlyBeAccessed().byAnyPackage("..controller..", "..service..");

    // repository 패키지의 클래스들은 service 패키지에서만 접근 가능
    @ArchTest
    static final ArchRule repositories_should_only_be_accessed_by_services = classes()
            .that().resideInAPackage("..repository..")
            .should().onlyBeAccessed().byAnyPackage("..service..");

    // DTO 위치 규칙 검증
    // "DTO"로 끝나는 클래스들은 반드시 dto 패키지에 있어야 함
    @ArchTest
    static final ArchRule dtos_should_reside_in_dto_package = classes()
            .that().haveSimpleNameEndingWith("DTO")
            .should().resideInAPackage("..dto..");

    // 순환 참조 방지 규칙
    // 클래스들 간의 순환 참조가 없어야 함
    @ArchTest
    static final ArchRule no_cycles_by_method_calls_between_classes = SlicesRuleDefinition.slices()
            .matching("..demo.(*)..")
            .should().beFreeOfCycles();

    // 클래스 파일 임포트를 위한 설정
    private final JavaClasses importedClasses = new ClassFileImporter()
            .importPackages("com.example.modi");

    // Service 인터페이스 구현 검증
    // Service로 끝나는 클래스는 인터페이스이거나 @Service 어노테이션이 붙어있어야 함
    @ArchTest
    static final ArchRule services_should_be_interfaces_or_implementations = classes()
            .that().haveSimpleNameEndingWith("Service")
            .should().beInterfaces()
            .orShould().beAnnotatedWith(Service.class);
}