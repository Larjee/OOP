package ru.nsu.munkuev;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class IncidenceMatrixGraph implements Graph {
    private List<Vertex> vertices;
    private int[][] incidenceMatrix;

    //Пустой конструктор — пустой граф
    public IncidenceMatrixGraph() {
        this.vertices = new ArrayList<>();
        this.incidenceMatrix = new int[0][0];
    }

    //Конструктор по матрице инцидентности
    public IncidenceMatrixGraph(int[][] m) {
        if (m == null) {
            throw new IllegalArgumentException("Matrix is null");
        }

        int n = m.length;
        if (n == 0) {
            this.vertices = new ArrayList<>();
            this.incidenceMatrix = new int[0][0];
            return;
        }

        int edges = m[0].length;
        for (int i = 1; i < n; i++) {
            if (m[i].length != edges) {
                throw new IllegalArgumentException("Matrix must be rectangular");
            }
        }

        this.vertices = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            this.vertices.add(new Vertex(Integer.toString(i), i));
        }

        this.incidenceMatrix = new int[n][edges];
        for (int i = 0; i < n; i++) {
            System.arraycopy(m[i], 0, this.incidenceMatrix[i], 0, edges);
        }
    }

    //Конструктор по списку вершин — граф без рёбер
    public IncidenceMatrixGraph(List<Vertex> vertices) {
        if (vertices == null) {
            throw new IllegalArgumentException("vertices is null");
        }

        int n = vertices.size();
        this.vertices = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            Vertex v = vertices.get(i);
            v.setId(i);
            this.vertices.add(v);
        }

        this.incidenceMatrix = new int[n][0];
    }

    //Конструктор по вершинам и готовой матрице инцидентности
    public IncidenceMatrixGraph(List<Vertex> vertices, int[][] incidenceMatrix) {
        if (vertices == null) {
            throw new IllegalArgumentException("vertices is null");
        }
        if (incidenceMatrix == null) {
            throw new IllegalArgumentException("incidenceMatrix is null");
        }

        int n = vertices.size();
        if (n != incidenceMatrix.length) {
            throw new IllegalArgumentException("vertices size and matrix rows differ");
        }

        int edges = (n == 0) ? 0 : incidenceMatrix[0].length;
        for (int i = 1; i < n; i++) {
            if (incidenceMatrix[i].length != edges) {
                throw new IllegalArgumentException("Matrix must be rectangular");
            }
        }

        this.vertices = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            Vertex v = vertices.get(i);
            v.setId(i);
            this.vertices.add(v);
        }

        this.incidenceMatrix = new int[n][edges];
        for (int i = 0; i < n; i++) {
            System.arraycopy(incidenceMatrix[i], 0, this.incidenceMatrix[i], 0, edges);
        }
    }


    @Override
    public boolean addVertex(Vertex vertex) {
        //проверяем, нет ли вершины с таким id
        for (Vertex v : vertices) {
            if (v.getId() == vertex.getId()) {
                System.out.printf("Cannot add vertex with id = %d, because it already exists\n", vertex.getId());
                return false;
            }
        }

        int oldV = incidenceMatrix.length;
        int edges = (oldV == 0) ? 0 : incidenceMatrix[0].length;

        //Добавляем вершину в список
        vertices.add(vertex);
        vertex.setId(oldV); // индекс строки в матрице

        //Расширяем матрицу инцидентности: добавляем одну строку
        int[][] newMatrix = new int[oldV + 1][edges];
        for (int i = 0; i < oldV; i++) {
            System.arraycopy(incidenceMatrix[i], 0, newMatrix[i], 0, edges);
        }
        //последняя строка автоматически заполнена нулями
        incidenceMatrix = newMatrix;

        return true;
    }


    @Override
    public boolean removeVertex(Vertex vertex) {
        int vertexIndex = vertex.getId();

        // проверки на корректность
        checkIndex(vertexIndex);
        if (!vertices.contains(vertex)) {
            System.out.printf("Cannot remove vertex with id = %d because it does not exists\n", vertex.getId());
            return false;
        }

        int oldV = incidenceMatrix.length;
        int edges = (oldV == 0) ? 0 : incidenceMatrix[0].length;

        //Те ребра, где вершина инцидентна
        boolean[] removeEdge = new boolean[edges];
        int newEdges = 0;
        for (int e = 0; e < edges; e++) {
            if (incidenceMatrix[vertexIndex][e] != 0) {
                removeEdge[e] = true;
            } else {
                newEdges++;
            }
        }

        //Новая матрица
        int[][] newMatrix = new int[oldV - 1][newEdges];
        for (int v = 0, nv = 0; v < oldV; v++) {
            if (v == vertexIndex) {
                continue; // пропускаем строку удаляемой вершины
            }
            int ne = 0;
            for (int e = 0; e < edges; e++) {
                if (!removeEdge[e]) {
                    newMatrix[nv][ne] = incidenceMatrix[v][e];
                    ne++;
                }
            }
            nv++;
        }

        incidenceMatrix = newMatrix;

        //удаляем вершину из списка
        vertices.remove(vertexIndex);

        //перенумеровываем id оставшихся вершин справа
        for (int i = vertexIndex; i < vertices.size(); i++) {
            vertices.get(i).setId(i);
        }

        return true;
    }


    @Override
    public boolean addEdge(int from, int to) {
        //проверка индексов
        if (from < 0 || from >= vertices.size() || to < 0 || to >= vertices.size()) {
            System.out.printf("Cannot add edge (%d, %d) because index out of range\n", from, to);
            return false;
        }

        int v = incidenceMatrix.length;
        int edges = (v == 0) ? 0 : incidenceMatrix[0].length;

        //проверяем, нет ли уже такого ребра
        for (int e = 0; e < edges; e++) {
            if (incidenceMatrix[from][e] == -1 && incidenceMatrix[to][e] == 1) {
                System.out.printf("Cannot add edge (%d, %d) because it already exists\n", from, to);
                return false;
            }
        }

        //Расширяем матрицу на один столбец
        int[][] newMatrix = new int[v][edges + 1];
        for (int i = 0; i < v; i++) {
            if (edges > 0) {
                System.arraycopy(incidenceMatrix[i], 0, newMatrix[i], 0, edges);
            }
        }

        //Записываем новое ребро from -> to
        newMatrix[from][edges] = -1;
        newMatrix[to][edges] = 1;

        incidenceMatrix = newMatrix;

        return true;
    }


    @Override
    public boolean removeEdge(int from, int to) {
        //проверка индексов
        if (from < 0 || from >= vertices.size() || to < 0 || to >= vertices.size()) {
            System.out.printf("Cannot remove edge (%d, %d) because index out of range\n", from, to);
            return false;
        }

        int v = incidenceMatrix.length;
        if (v == 0) {
            System.out.printf("Cannot remove edge (%d, %d) because it does not exist\n", from, to);
            return false;
        }
        int edges = incidenceMatrix[0].length;

        //ищем столбец, соответствующий ребру from -> to
        int edgeIndex = -1;
        for (int e = 0; e < edges; e++) {
            if (incidenceMatrix[from][e] == -1 && incidenceMatrix[to][e] == 1) {
                edgeIndex = e;
                break;
            }
        }

        if (edgeIndex == -1) {
            System.out.printf("Cannot remove edge (%d, %d) because it does not exist\n", from, to);
            return false;
        }

        //Новая матрица без этого столбца
        int[][] newMatrix = new int[v][edges - 1];
        for (int i = 0; i < v; i++) {
            int ne = 0;
            for (int e = 0; e < edges; e++) {
                if (e == edgeIndex) {
                    continue;
                }
                newMatrix[i][ne] = incidenceMatrix[i][e];
                ne++;
            }
        }

        incidenceMatrix = newMatrix;

        return true;
    }


    @Override
    public List<Vertex> getVertices() {
        return vertices;
    }

    public int[][] getIncidenceMatrix() {
        int v = incidenceMatrix.length;
        if (v == 0) {
            return new int[0][0];
        }
        int edges = incidenceMatrix[0].length;
        int[][] m = new int[v][edges];
        for (int i = 0; i < v; i++) {
            System.arraycopy(incidenceMatrix[i], 0, m[i], 0, edges);
        }
        return m;
    }


    @Override
    public List<Integer> getParents(int vertex) {
        checkIndex(vertex);

        List<Integer> parents = new ArrayList<>();
        int v = incidenceMatrix.length;
        if (v == 0) {
            return parents;
        }
        int edges = incidenceMatrix[0].length;

        // u -> vertex: в столбце e стоит -1 в строке u и 1 в строке vertex
        for (int u = 0; u < v; u++) {
            if (u == vertex) {
                continue;
            }
            for (int e = 0; e < edges; e++) {
                if (incidenceMatrix[u][e] == -1 && incidenceMatrix[vertex][e] == 1) {
                    parents.add(u);
                    break; // одно ребро уже нашлось, дальше можно не смотреть
                }
            }
        }

        return parents;
    }


    @Override
    public List<Integer> getChildren(int vertex) {
        checkIndex(vertex);

        List<Integer> children = new ArrayList<>();
        int v = incidenceMatrix.length;
        if (v == 0) {
            return children;
        }
        int edges = incidenceMatrix[0].length;

        // vertex -> u: в столбце e стоит -1 в строке vertex и 1 в строке u
        for (int u = 0; u < v; u++) {
            if (u == vertex) {
                continue;
            }
            for (int e = 0; e < edges; e++) {
                if (incidenceMatrix[vertex][e] == -1 && incidenceMatrix[u][e] == 1) {
                    children.add(u);
                    break;
                }
            }
        }

        return children;
    }


    private void checkIndex(int index) {
        if (index < 0 || index >= vertices.size()) {
            throw new IllegalArgumentException("Index out of bounds");
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Graph)) {
            return false;
        }

        Graph other = (Graph) o;

        // сравниваем количество вершин
        int n = this.getVertices().size();
        if (n != other.getVertices().size()) {
            return false;
        }

        // сравниваем детей каждой вершины
        for (int i = 0; i < n; i++) {
            List<Integer> childrenThis = new ArrayList<>(this.getChildren(i));
            List<Integer> childrenOther = new ArrayList<>(other.getChildren(i));

            Collections.sort(childrenThis);
            Collections.sort(childrenOther);

            if (!childrenThis.equals(childrenOther)) {
                return false;
            }
        }

        return true;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("IncidenceMatrixGraph").append(System.lineSeparator());

        sb.append("Vertices: ");
        for (Vertex v : vertices) {
            sb.append(v.getId()).append(' ');
        }
        sb.append(System.lineSeparator());

        sb.append("Incidence matrix (vertices x edges):").append(System.lineSeparator());
        if (incidenceMatrix != null) {
            for (int i = 0; i < incidenceMatrix.length; i++) {
                for (int j = 0; j < incidenceMatrix[i].length; j++) {
                    sb.append(incidenceMatrix[i][j]).append(' ');
                }
                sb.append(System.lineSeparator());
            }
        }

        return sb.toString();
    }
}
