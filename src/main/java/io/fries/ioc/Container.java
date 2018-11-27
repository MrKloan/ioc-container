package io.fries.ioc;

import io.fries.ioc.dependencies.Dependencies;
import io.fries.ioc.dependencies.Id;
import io.fries.ioc.instantiator.DefaultInstantiator;
import io.fries.ioc.instantiator.Instantiator;
import io.fries.ioc.registry.Registry;

import java.util.Objects;

public class Container {

    private final Dependencies dependencies;

    private Container(final Dependencies dependencies) {
        this.dependencies = dependencies;
    }

    static Container of(final Dependencies dependencies) {
        return new Container(dependencies);
    }

    @SuppressWarnings("WeakerAccess")
    public static RegistrationContainer using(final Instantiator instantiator) {
        Objects.requireNonNull(instantiator);
        return RegistrationContainer.of(instantiator, Registry.empty());
    }

    @SuppressWarnings("WeakerAccess")
    public static RegistrationContainer empty() {
        return RegistrationContainer.of(new DefaultInstantiator(), Registry.empty());
    }

    @SuppressWarnings("WeakerAccess")
    public <T> T provide(final Id id) {
        Objects.requireNonNull(id);
        return dependencies.getInstance(id);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Container container = (Container) o;
        return Objects.equals(dependencies, container.dependencies);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dependencies);
    }

    @Override
    public String toString() {
        return "Container{" +
                "dependencies=" + dependencies +
                '}';
    }
}
