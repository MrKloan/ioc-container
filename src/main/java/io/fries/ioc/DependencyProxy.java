package io.fries.ioc;

import java.lang.reflect.Proxy;
import java.util.List;
import java.util.function.Supplier;

import static java.util.stream.Collectors.toList;

class DependencyProxy implements RegisteredDependency {

    private final Id id;
    private final Class<?> interfaceType;
    private final Class<?> type;
    private final List<Id> dependencies;

    private DependencyProxy(final Id id, final Class<?> interfaceType, final Class<?> type, final List<Id> dependencies) {
        this.id = id;
        this.interfaceType = interfaceType;
        this.type = type;
        this.dependencies = dependencies;
    }

    public static DependencyProxy of(final Id id, final Class<?> interfaceType, final Class<?> type, final List<Id> dependencies) {
        return new DependencyProxy(id, interfaceType, type, dependencies);
    }

    @Override
    public int countDependencies(final Registry registry) {
        return Integer.MAX_VALUE;
    }

    @Override
    public Dependency instantiate(final Instantiator instantiator, final Dependencies dependencies) {
        final Supplier<Object> instanceSupplier = createInstanceSupplier(instantiator, dependencies);
        final Object proxy = createProxy(instanceSupplier);

        return Dependency.of(id, interfaceType, proxy);
    }

    private Supplier<Object> createInstanceSupplier(final Instantiator instantiator, final Dependencies dependencies) {
        return () -> {
            final List<Dependency> requiredDependencies = mapRequiredDependencies(dependencies);
            return instantiator.createInstance(type, requiredDependencies);
        };
    }

    private List<Dependency> mapRequiredDependencies(final Dependencies dependencies) {
        return this.dependencies
                .stream()
                .map(dependencies::get)
                .collect(toList());
    }

    private Object createProxy(final Supplier<Object> instanceSupplier) {
        return Proxy.newProxyInstance(
                interfaceType.getClassLoader(),
                new Class[]{interfaceType},
                new ProxyInvocationHandler(instanceSupplier)
        );
    }
}
