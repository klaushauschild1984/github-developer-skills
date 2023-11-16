package de.codecentric.github.developer.skills.service;

import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.metamodel.Type;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TruncateService {

    private final EntityManager entityManager;

    private List<String> tables;

    @PostConstruct
    void postConstruct() {
        tables =
            entityManager
                .getMetamodel()
                .getEntities()
                .stream()
                .map(Type::getJavaType)
                .map(Class::getSimpleName)
                .map(name -> name.replaceAll("([a-z])([A-Z])", "$1_$2"))
                .map(String::toLowerCase)
                .collect(Collectors.toList());
    }

    @Transactional
    public void perform() {
        entityManager.createNativeQuery("SET @@foreign_key_checks = 0;").executeUpdate();
        tables.forEach(table ->
            entityManager.createNativeQuery(String.format("TRUNCATE TABLE %s;", table)).executeUpdate()
        );
        entityManager.createNativeQuery("SET @@foreign_key_checks = 1;").executeUpdate();
    }
}
