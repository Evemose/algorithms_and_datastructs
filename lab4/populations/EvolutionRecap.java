package org.example.lab4.populations;

import org.example.lab4.EvolutionStep;

import java.util.NavigableMap;

public record EvolutionRecap(NavigableMap<Integer, EvolutionStep> evolutionPath) {
    public String toString() {
        var sb = new StringBuilder();
        sb.append("Evolution recap:\n");
        for (var entry : evolutionPath.entrySet()) {
            sb.append("Generation ").append(entry.getKey()).append(":" );
            sb.append("value=").append(entry.getValue().value()).append(", ")
                    .append("weight=").append(entry.getValue().weight()).append("\n");
        }
        return sb.toString();
    }
}
