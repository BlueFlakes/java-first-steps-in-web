package rh.dao;

import java.util.List;
import java.util.function.IntPredicate;

class DaoLayerValidator {
    private final String query;
    private final List<String> values;

    DaoLayerValidator(String query, List<String> values) {
        this.query = query;
        this.values = values;
    }

    boolean isAnyInputNull() {
        return this.query == null || this.values == null;
    }

    boolean isEqualAmountOfInjectionsAndValues() {
        return countQueryInjections() == values.size();
    }

    private long countQueryInjections() {
        IntPredicate isInjection = n -> n == '?';

        return query.chars()
                    .filter(isInjection)
                    .count();
    }
}
