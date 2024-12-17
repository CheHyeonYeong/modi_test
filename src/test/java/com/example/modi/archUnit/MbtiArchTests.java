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

@AnalyzeClasses(packages = "com.example.modi")
public class MbtiArchTests {

    // 기본적인 레이어 아키텍처 검증
    @ArchTest
    static final ArchRule layer_dependencies_are_respected = layeredArchitecture()
            .consideringAllDependencies()
            .layer("Controller").definedBy("..controller..")
            .layer("Service").definedBy("..service..")
            .layer("Repository").definedBy("..repository..")
            .layer("DAO").definedBy("..dao..")
            .layer("DTO").definedBy("..dto..")

            .whereLayer("Controller").mayNotBeAccessedByAnyLayer()
            .whereLayer("Service").mayOnlyBeAccessedByLayers("Controller")
            .whereLayer("Repository").mayOnlyBeAccessedByLayers("Service")
            .whereLayer("DAO").mayOnlyBeAccessedByLayers("Service", "Repository");

    // 네이밍 컨벤션 검증
    @ArchTest
    static final ArchRule services_should_be_named_correctly = classes()
            .that().areAnnotatedWith(Service.class)
            .should().haveSimpleNameEndingWith("Service");

    @ArchTest
    static final ArchRule repositories_should_be_named_correctly = classes()
            .that().areAnnotatedWith(Repository.class)
            .should().haveSimpleNameEndingWith("Repository");

    // 패키지 의존성 검증
    @ArchTest
    static final ArchRule services_should_only_be_accessed_by_controllers = classes()
            .that().resideInAPackage("..service..")
            .should().onlyBeAccessed().byAnyPackage("..controller..", "..service..");

    @ArchTest
    static final ArchRule repositories_should_only_be_accessed_by_services = classes()
            .that().resideInAPackage("..repository..")
            .should().onlyBeAccessed().byAnyPackage("..service..");

    // DTO 패키지 규칙
    @ArchTest
    static final ArchRule dtos_should_reside_in_dto_package = classes()
            .that().haveSimpleNameEndingWith("DTO")
            .should().resideInAPackage("..dto..");

    // 순환 참조 방지
    @ArchTest
    static final ArchRule no_cycles_by_method_calls_between_classes = SlicesRuleDefinition.slices()
            .matching("..demo.(*)..")
            .should().beFreeOfCycles();


    // 직접 테스트 메소드로 검증 (ArchRule 직접 사용)
    private final JavaClasses importedClasses = new ClassFileImporter()
            .importPackages("com.example.modi");

    // Service 클래스들이 인터페이스를 구현하는지 검증
    @ArchTest
    static final ArchRule services_should_be_interfaces_or_implementations = classes()
            .that().haveSimpleNameEndingWith("Service")
            .should().beInterfaces()
            .orShould().beAnnotatedWith(Service.class);

}