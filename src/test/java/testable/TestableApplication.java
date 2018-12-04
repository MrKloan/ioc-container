package testable;

import io.fries.ioc.annotations.Configuration;
import io.fries.ioc.annotations.Register;

@Configuration
public class TestableApplication {

    @Register("plot.outcome")
    String plotOutcome() {
        return "Outcome";
    }

    String nonRegisteredOutcome() {
        return "Meh.";
    }
}
