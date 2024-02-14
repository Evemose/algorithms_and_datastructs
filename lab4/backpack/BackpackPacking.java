package org.example.lab4.backpack;

import lombok.Builder;
import org.example.lab4.EvolutionStep;

import java.util.List;

@Builder
public record BackpackPacking(List<BackpackItem> items, double value, double weight) implements EvolutionStep {
    @Override
    public double value() {
        return items.stream().mapToDouble(BackpackItem::value).sum();
    }

    @Override
    public double weight() {
        return items.stream().mapToDouble(BackpackItem::weight).sum();
    }

    public record BackpackItem(int index, double weight, double value) {
    }
}
