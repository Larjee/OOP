package ru.nsu.munkuev;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AdjacencyMatrixGraph implements Graph {
    private List<Vertex> vertices;
    private int[][] adjacencyMatrix;

    //Пустой констркутор
    public AdjacencyMatrixGraph(){
        this.vertices = new ArrayList<>();
        this.adjacencyMatrix = new int[0][0];
    }

    //Конструктор по матрице смежности
    public AdjacencyMatrixGraph(int[][] m) {
        int n = m.length;
        for (int i = 0; i < n; i++) {
            if (m[i].length != n){
                throw new IllegalArgumentException("Matrix must be square");
            }
        }
        this.vertices = new ArrayList<>(n);
        for (int i = 0; i < n; i++){
            vertices.add(new Vertex(Integer.toString(i), i));
        }

        this.adjacencyMatrix = new int[n][n];
        for (int i = 0; i < n; i++){
            System.arraycopy(m[i], 0, this.adjacencyMatrix[i], 0, n);
        }
    }


    //Конструктор по списку вершин
    // не знаю вообще зачем это может понадобиться
    // например, если мы просто создадим пустую матрицу по вершинам
    // и уже сами ее заполним как надо.
    public AdjacencyMatrixGraph(List<Vertex> vertices) {
        if(vertices == null){
            throw new IllegalArgumentException("vertices is null");
        }

        int n = vertices.size();
        this.vertices = new ArrayList<>(vertices);
        for (int i = 0; i < n; i++) {
            this.vertices.get(i).setId(i); // выравниваем id
        }

        this.adjacencyMatrix = new int[n][n];
    }


    @Override
    public boolean addVertex(Vertex vertex) {
        for(Vertex v : vertices) {
            if(v.getId() == vertex.getId()) {
                System.out.printf("Cannot add vertex with id = %d, because it already exists\n",  vertex.getId());
                return false;
            }
        }

        //Добавляем в список вершин новую вершину
        vertices.add(vertex);

        //И расширяем матрицу смежности
        int n = adjacencyMatrix.length;
        int[][] newAdjacencyMatrix = new int[n+1][n+1];
        for(int i = 0; i < n; i++) {
            System.arraycopy(adjacencyMatrix[i], 0, newAdjacencyMatrix[i], 0, n);
        }
        adjacencyMatrix = newAdjacencyMatrix;

        //Сохраняем в id вершины ее индекс в матрице смежности
        vertices.get(n).setId(n);

        return true;
    }

    @Override
    public boolean removeVertex(Vertex vertex) {
        //Индекс удаляемой вершины в матрице смежности
        int vertexIndex = vertex.getId();

        //Выполняем проверки на корректность входных данных
        if (vertexIndex < 0 || vertexIndex >= vertices.size()) {
            return false;
        }
        if(!vertices.contains(vertex)) {
            System.out.printf("Cannot remove vertex with id = %d because it does not exists\n", vertex.getId());
            return false;
        }

        //Удаляем вершину из списка вершин
        vertices.remove(vertexIndex);

        //Уменьшаем матрицу смежности
        int n = adjacencyMatrix.length;
        int[][] newAdjacencyMatrix = new int[n-1][n-1];
        //Копируем в новую матрицу все элементы старой матрицы, вычеркивая
        //строку и столбец, соответствующие удаляемой вершине
        for (int i = 0, ii = 0; i < n; i++) {
            if (i == vertexIndex) continue;// пропускаем строку vertexIndex

            for (int j = 0, jj = 0; j < n; j++) {
                if (j == vertexIndex) continue;// пропускаем столбец vertexIndex
                newAdjacencyMatrix[ii][jj] = adjacencyMatrix[i][j];
                jj++;
            }

            ii++;
        }
        adjacencyMatrix = newAdjacencyMatrix;

        //Перенумеровываем id у вершин вправо от удалённой
        for (int k = vertexIndex; k < vertices.size(); k++) {
            vertices.get(k).setId(k);
        }

        return true;
    }

    @Override
    public boolean addEdge(int from, int to) {
        //Проверка на корректность индексов
        if(from < 0 || from >= vertices.size() || to < 0 || to >= vertices.size()) {
            System.out.printf("Cannot add edge (%d, %d) because index out of range\n", from, to);

            return false;
        }
        //Проверяем нет ли уже такого ребра
        if(adjacencyMatrix[from][to] == 1) {
            System.out.printf("Cannot add edge (%d, %d) because it already exists\n", from, to);
            return false;
        }

        adjacencyMatrix[from][to] = 1;

        return true;
    }

    @Override
    public boolean removeEdge(int from, int to) {
        //Проверка на корректность индексов
        if(from < 0 || from >= vertices.size() || to < 0 || to >= vertices.size()) {
            System.out.printf("Cannot remove edge (%d, %d) because index out of range\n", from, to);
            return false;
        }
        //Проверяем наличие ребра
        if (adjacencyMatrix[from][to] == 0){
            System.out.printf("Cannot remove edge (%d, %d) because it does not exist\n", from, to);
            return false;
        }

        adjacencyMatrix[from][to] = 0;

        return true;
    }

    @Override
    public List<Vertex> getVertices() {
        return vertices;
    }

    public int[][] getAdjacencyMatrix() {
        int size =  adjacencyMatrix.length;
        int[][] adj = new int[size][size];

        for(int i = 0; i < size; i++){
            System.arraycopy(adjacencyMatrix[i], 0, adj[i], 0, size);
        }

        return adjacencyMatrix;
    }

    @Override
    public List<Integer> getParents(int vertex) {
        int verticesSize = vertices.size();
        List<Integer> parents = new ArrayList<>();

        //Пробегаемся по матрице смежности и смотрим из каких вершин можем попасть в vertex
        for (int i = 0; i < verticesSize; i++) {
            if(adjacencyMatrix[i][vertex] == 1) {
                parents.add(i);
            }
        }

        return parents;
    }

    @Override
    public List<Integer> getChildren(int vertex) {
        int verticesSize = vertices.size();
        List<Integer> children = new ArrayList<>();

        //Пробегаемся по матрице смежности и смотрим в какие вершины можем попасть из vertex
        for (int i = 0; i < verticesSize; i++) {
            if(adjacencyMatrix[vertex][i] == 1) {
                children.add(i);
            }
        }

        return children;
    }


    private void checkIndex(int index) {
        if(index < 0 || index >= vertices.size()) {
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

        sb.append("AdjacencyMatrixGraph").append(System.lineSeparator());

        sb.append("Vertices: ");
        for (Vertex v : vertices) {
            sb.append(v.getId()).append(' ');
        }
        sb.append(System.lineSeparator());

        sb.append("Adjacency matrix:").append(System.lineSeparator());
        if (adjacencyMatrix != null) {
            for (int i = 0; i < adjacencyMatrix.length; i++) {
                for (int j = 0; j < adjacencyMatrix[i].length; j++) {
                    sb.append(adjacencyMatrix[i][j]).append(' ');
                }
                sb.append(System.lineSeparator());
            }
        }

        return sb.toString();
    }
}
