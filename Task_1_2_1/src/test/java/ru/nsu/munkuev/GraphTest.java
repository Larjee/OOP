package ru.nsu.munkuev;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Набор тестов для всех реализаций {@link Graph} и алгоритма {@link KahnTopologicalSort}.
 *
 * Тесты проверяют:
 * <ul>
 *     <li>добавление / удаление вершин и рёбер;</li>
 *     <li>корректность методов getParents / getChildren;</li>
 *     <li>работу equals между разными реализациями графа;</li>
 *     <li>корректность топологической сортировки и детектирование циклов.</li>
 * </ul>
 */
class GraphTest {

    /**
     * Создаёт список вершин v0..v{n-1} с совпадающими id.
     */
    private List<Vertex> createVertices(int n) {
        List<Vertex> vertices = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            vertices.add(new Vertex("v" + i, i));
        }
        return vertices;
    }

    /**
     * Простой ациклический граф:
     * 5 -> 2, 5 -> 0
     * 4 -> 0, 4 -> 1
     * 2 -> 3
     * 3 -> 1
     */
    private void addSampleDAGEdges(Graph g) {
        assertTrue(g.addEdge(5, 2));
        assertTrue(g.addEdge(5, 0));
        assertTrue(g.addEdge(4, 0));
        assertTrue(g.addEdge(4, 1));
        assertTrue(g.addEdge(2, 3));
        assertTrue(g.addEdge(3, 1));
    }


    // ============ Vertex ============
    @Test
    void vertexCreateSetAndGet(){
        Vertex vertex_both = new Vertex("vertex_both", 1);
        Vertex vertex_label = new Vertex("vertex_label");
        Vertex vertex_id = new Vertex(1);

        //Проверяем имена
        assertEquals("vertex_both", vertex_both.getLabel());
        assertEquals("vertex_label", vertex_label.getLabel());
        assertNull(vertex_id.getLabel());

        //Проверяем айди
        assertEquals(1, vertex_both.getId());
        assertEquals(1, vertex_id.getId());
        assertEquals(0, vertex_label.getId());

        //Проверяем сеттеры
        vertex_both.setId(3);
        vertex_both.setLabel("setNewLabel");
        assertEquals(3, vertex_both.getId());
        assertEquals("setNewLabel", vertex_both.getLabel());

        vertex_label.setLabel("setNewLabel");
        vertex_label.setId(4);
        assertEquals(4, vertex_label.getId());
        assertEquals("setNewLabel", vertex_label.getLabel());

        vertex_id.setLabel("setNewLabel");
        vertex_id.setId(5);
        assertEquals(5, vertex_id.getId());
        assertEquals("setNewLabel", vertex_id.getLabel());
    }


    // ============ AdjacencyMatrixGraph ============

    @Test
    void adjacencyMatrix_addAndRemoveVertexAndEdge() {
        AdjacencyMatrixGraph emptyGraph = new AdjacencyMatrixGraph();
        assertTrue(emptyGraph.getVertices().isEmpty());
        assertEquals(emptyGraph.getAdjacencyMatrix().length, 0);

        Graph g = new AdjacencyMatrixGraph(createVertices(3));

        assertEquals(3, g.getVertices().size());

        // добавляем рёбра 0 -> 1 и 1 -> 2
        assertTrue(g.addEdge(0, 1));
        assertTrue(g.addEdge(1, 2));

        assertEquals(List.of(1), g.getChildren(0));
        assertEquals(List.of(2), g.getChildren(1));
        assertEquals(List.of(0), g.getParents(1));
        assertEquals(List.of(1), g.getParents(2));

        // повторное добавление ребра должно вернуть false
        assertFalse(g.addEdge(0, 1));

        // удаляем ребро 0 -> 1
        assertTrue(g.removeEdge(0, 1));
        assertEquals(List.of(), g.getChildren(0));
        assertEquals(List.of(), g.getParents(1));

        // удаляем вершину с id = 1
        Vertex v1 = g.getVertices().get(1);
        assertTrue(g.removeVertex(v1));
        assertEquals(2, g.getVertices().size());

        // проверяем, что id перенумеровались в 0..size-1
        List<Vertex> vs = g.getVertices();
        assertEquals(0, vs.get(0).getId());
        assertEquals(1, vs.get(1).getId());


        //Также нужно проверить все конструкторы

        //Пустой конструктор
        AdjacencyMatrixGraph emptyConstructorGraph =  new AdjacencyMatrixGraph();
        assertTrue(emptyConstructorGraph.getVertices().isEmpty());
        assertEquals(0, emptyConstructorGraph.getAdjacencyMatrix().length);

        //Конструктор по матрице смежности
        AdjacencyMatrixGraph adjacencyMatrixConstructorGraph = new AdjacencyMatrixGraph(new int[][]{{0,1,1}, {0,0,0}, {1,1,0}});
        assertEquals(3, adjacencyMatrixConstructorGraph.getAdjacencyMatrix().length);
        assertEquals(3, adjacencyMatrixConstructorGraph.getVertices().size());
    }


    // ============ AdjacencyListGraph ============

    @Test
    void adjacencyList_addAndRemoveVertexAndEdge() {
        Graph g = new AdjacencyListGraph(createVertices(3));

        assertEquals(3, g.getVertices().size());

        // 0 -> 1, 1 -> 2
        assertTrue(g.addEdge(0, 1));
        assertTrue(g.addEdge(1, 2));

        assertEquals(List.of(1), g.getChildren(0));
        assertEquals(List.of(2), g.getChildren(1));
        assertEquals(List.of(0), g.getParents(1));
        assertEquals(List.of(1), g.getParents(2));

        // повторное добавление ребра
        assertFalse(g.addEdge(0, 1));

        // удаляем ребро 0 -> 1
        assertTrue(g.removeEdge(0, 1));
        assertEquals(List.of(), g.getChildren(0));
        assertEquals(List.of(), g.getParents(1));

        // удаляем вершину 1
        Vertex v1 = g.getVertices().get(1);
        assertTrue(g.removeVertex(v1));
        assertEquals(2, g.getVertices().size());

        List<Vertex> vs = g.getVertices();
        assertEquals(0, vs.get(0).getId());
        assertEquals(1, vs.get(1).getId());
    }


    // ============ IncidenceMatrixGraph ============

    @Test
    void incidenceMatrix_addAndRemoveVertexAndEdge() {
        Graph g = new IncidenceMatrixGraph(createVertices(3));

        assertEquals(3, g.getVertices().size());

        // 0 -> 1, 1 -> 2
        assertTrue(g.addEdge(0, 1));
        assertTrue(g.addEdge(1, 2));

        assertEquals(List.of(1), g.getChildren(0));
        assertEquals(List.of(2), g.getChildren(1));
        assertEquals(List.of(0), g.getParents(1));
        assertEquals(List.of(1), g.getParents(2));

        // повторное добавление ребра
        assertFalse(g.addEdge(0, 1));

        // удаляем ребро 0 -> 1
        assertTrue(g.removeEdge(0, 1));
        assertEquals(List.of(), g.getChildren(0));
        assertEquals(List.of(), g.getParents(1));

        // удаляем вершину 1
        Vertex v1 = g.getVertices().get(1);
        assertTrue(g.removeVertex(v1));
        assertEquals(2, g.getVertices().size());

        List<Vertex> vs = g.getVertices();
        assertEquals(0, vs.get(0).getId());
        assertEquals(1, vs.get(1).getId());
    }

    // ============ equals между разными реализациями ============

    @Test
    void differentImplementations_withSameStructure_areEqual() {
        List<Vertex> vertices1 = createVertices(6);
        List<Vertex> vertices2 = createVertices(6);
        List<Vertex> vertices3 = createVertices(6);

        Graph matrixGraph = new AdjacencyMatrixGraph(vertices1);
        Graph listGraph   = new AdjacencyListGraph(vertices2);
        Graph incidGraph  = new IncidenceMatrixGraph(vertices3);

        addSampleDAGEdges(matrixGraph);
        addSampleDAGEdges(listGraph);
        addSampleDAGEdges(incidGraph);

        // все графы изоморфны и должны быть равны друг другу
        assertEquals(matrixGraph, listGraph);
        assertEquals(listGraph, matrixGraph);

        assertEquals(matrixGraph, incidGraph);
        assertEquals(incidGraph, matrixGraph);

        assertEquals(listGraph, incidGraph);
        assertEquals(incidGraph, listGraph);

        // граф с другой структурой не равен
        Graph empty = new AdjacencyMatrixGraph();
        assertNotEquals(matrixGraph, empty);
        assertNotEquals(empty, matrixGraph);
    }

    // ============ toString ============

    @Test
    void toString_containsBasicInfo() {
        Graph matrix = new AdjacencyMatrixGraph(createVertices(2));
        matrix.addEdge(0, 1);

        Graph list = new AdjacencyListGraph(createVertices(2));
        list.addEdge(0, 1);

        Graph incid = new IncidenceMatrixGraph(createVertices(2));
        incid.addEdge(0, 1);

        String m = matrix.toString();
        String l = list.toString();
        String i = incid.toString();

        assertTrue(m.contains("AdjacencyMatrixGraph"));
        assertTrue(m.contains("Vertices:"));
        assertTrue(m.contains("Adjacency matrix:"));

        assertTrue(l.contains("AdjacencyListGraph"));
        assertTrue(l.contains("Adjacency list:"));

        assertTrue(i.contains("IncidenceMatrixGraph"));
        assertTrue(i.contains("Incidence matrix"));
    }

    // ============ Топологическая сортировка ============


    private void assertValidTopologicalOrder(Graph g, List<Vertex> order) {
        int n = g.getVertices().size();
        assertEquals(n, order.size(), "В топологическом порядке должно быть столько же вершин, сколько в графе");

        Set<Integer> seen = new HashSet<>();
        int[] position = new int[n];
        for (int idx = 0; idx < order.size(); idx++) {
            Vertex v = order.get(idx);
            int id = v.getId();
            assertTrue(id >= 0 && id < n, "Некорректный id вершины в результате топосортировки");
            assertTrue(seen.add(id), "Вершина встречается дважды в результате топосортировки");
            position[id] = idx;
        }

        // для каждой вершины проверяем, что все её дети идут позже
        for (int u = 0; u < n; u++) {
            for (int v : g.getChildren(u)) {
                assertTrue(position[u] < position[v],
                        "Ребро " + u + " -> " + v + " нарушает порядок топологической сортировки");
            }
        }
    }

    @Test
    void kahnTopologicalSort_worksForAllImplementations() {
        KahnTopologicalSort sorter = new KahnTopologicalSort();

        Graph matrixGraph = new AdjacencyMatrixGraph(createVertices(6));
        Graph listGraph   = new AdjacencyListGraph(createVertices(6));
        Graph incidGraph  = new IncidenceMatrixGraph(createVertices(6));

        addSampleDAGEdges(matrixGraph);
        addSampleDAGEdges(listGraph);
        addSampleDAGEdges(incidGraph);

        List<Vertex> orderMatrix = sorter.topologicalSort(matrixGraph);
        List<Vertex> orderList   = sorter.topologicalSort(listGraph);
        List<Vertex> orderIncid  = sorter.topologicalSort(incidGraph);

        assertValidTopologicalOrder(matrixGraph, orderMatrix);
        assertValidTopologicalOrder(listGraph, orderList);
        assertValidTopologicalOrder(incidGraph, orderIncid);
    }

    @Test
    void kahnTopologicalSort_detectsCycle() {
        List<Vertex> vertices = createVertices(3);
        Graph g = new AdjacencyMatrixGraph(vertices);

        // 0 -> 1 -> 2 -> 0
        assertTrue(g.addEdge(0, 1));
        assertTrue(g.addEdge(1, 2));
        assertTrue(g.addEdge(2, 0));

        KahnTopologicalSort sorter = new KahnTopologicalSort();

        assertThrows(IllegalStateException.class, () -> sorter.topologicalSort(g));
    }
}
