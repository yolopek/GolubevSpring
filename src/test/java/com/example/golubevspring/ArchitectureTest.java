package com.example.golubevspring;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.apiguardian.api.API;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.library.Architectures.layeredArchitecture;
import static com.tngtech.archunit.library.Architectures.onionArchitecture;

public class ArchitectureTest {

    @Test
    @DisplayName("Соблюдены требования слоеной архитектуры")
    void testLayeredArchitecture() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("com.example.golubevspring");

        ArchRule layeredArchitectureRule = layeredArchitecture()
                .consideringAllDependencies()
                .layer("domain").definedBy("com.example.golubevspring.domain..")
                .layer("app").definedBy("com.example.golubevspring.app..")
                .layer("extern").definedBy("com.example.golubevspring.api..", "com.example.golubevspring.infrastructure..")
                .whereLayer("app").mayOnlyBeAccessedByLayers("app", "extern")
                .whereLayer("extern").mayOnlyBeAccessedByLayers("extern");

        layeredArchitectureRule.check(importedClasses);
    }

    @Test
    @DisplayName("Соблюдены требования архитектуры луковицы")
    void testOnionArchitecture() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages();

        ArchRule onionArchitectureRule = onionArchitecture()
                .domainModels("com.example.golubevspring.domain..")
                .adapter("app", "com.example.golubevspring.app..")
                .adapter("extern", "com.example.golubevspring.api..", "com.example.golubevspring.infrastructure..");

        onionArchitectureRule.check(importedClasses);
    }
}