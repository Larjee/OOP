package ru.nsu.munkuev;

import java.util.List;

public interface TopologicalSort {
    /**
     * Выполняет топологическую сортировку графа.
     *
     * @param graph ориентированный ацикличный граф (DAG)
     * @return список вершин в топологическом порядке
     * @throws IllegalStateException если в графе есть цикл
     */
    List<Vertex> topologicalSort(Graph graph);
}
