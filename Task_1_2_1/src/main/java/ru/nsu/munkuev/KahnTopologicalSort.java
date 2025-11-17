package ru.nsu.munkuev;

import java.util.*;

/**
 * Топологическая сортировка по алгоритму Кана.
 * Использует степени захода (in-degree).
 */
public class KahnTopologicalSort implements TopologicalSort {

    @Override
    public List<Vertex> topologicalSort(Graph graph) {
        List<Vertex> vertices = graph.getVertices();
        int n = vertices.size();

        //1. Считаем степени захода для каждой вершины
        int[] indegree = new int[n];
        for (int v = 0; v < n; v++) {
            indegree[v] = graph.getParents(v).size();
        }

        //2. Кладем в очередь все вершины с indegree == 0
        Deque<Integer> queue = new ArrayDeque<>();
        for (int v = 0; v < n; v++) {
            if (indegree[v] == 0) {
                queue.add(v);
            }
        }

        List<Vertex> result = new ArrayList<>(n);
        int visitedCount = 0;

        //3. Пока есть вершины с нулевой степенью захода
        while (!queue.isEmpty()) {
            int v = queue.removeFirst();
            result.add(vertices.get(v));
            visitedCount++;

            //Удаляем вершину: уменьшаем indegree у её детей
            for (int child : graph.getChildren(v)) {
                indegree[child]--;
                if (indegree[child] == 0) {
                    queue.addLast(child);
                }
            }
        }

        //4. Если обошли не все вершины значит в графе есть цикл
        if (visitedCount != n) {
            throw new IllegalStateException("Graph contains a cycle, topological sort is impossible");
        }

        return result;
    }
}
