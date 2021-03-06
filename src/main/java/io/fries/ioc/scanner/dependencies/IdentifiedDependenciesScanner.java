package io.fries.ioc.scanner.dependencies;

import io.fries.ioc.annotations.Identified;
import io.fries.ioc.components.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.List;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

public class IdentifiedDependenciesScanner implements DependenciesScanner {

    @Override
    public List<Id> findByConstructor(final Class<?> type) {
        final Constructor<?> constructor = type.getDeclaredConstructors()[0];
        final Parameter[] parameters = constructor.getParameters();
        return stream(parameters)
                .map(this::extractParameterId)
                .collect(toList());
    }

    Id extractParameterId(final Parameter parameter) {
        if (parameter.isAnnotationPresent(Identified.class))
            return parameterAnnotationToId(parameter);

        return parameterTypeToId(parameter);
    }

    Id parameterAnnotationToId(final Parameter parameter) {
        final Identified identified = parameter.getAnnotation(Identified.class);
        return Id.of(identified.value());
    }

    Id parameterTypeToId(final Parameter parameter) {
        final String parameterType = parameter.getType().getSimpleName();
        return Id.of(parameterType);
    }
}
