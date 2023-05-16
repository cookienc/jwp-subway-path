package subway.application.strategy.insert;

import org.springframework.stereotype.Component;
import subway.domain.Sections;

import java.util.List;

@Component
public class SectionInserter {

    private final List<InsertStrategyInterface> strategies;

    public SectionInserter(List<InsertStrategyInterface> strategies) {
        this.strategies = strategies;
    }

    public Long insert(Sections sections, InsertSection insertSection) {
        Long newSectionId = null;

        for (InsertStrategyInterface strategy : strategies) {
            if (strategy.support(sections, insertSection)) {
                newSectionId = strategy.insert(sections, insertSection);
                break;
            }
        }

        return newSectionId;
    }
}