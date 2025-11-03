package ru.nsu.munkuev;

import java.util.ArrayList;
import java.util.List;

public class AdjacencyMatrixGraph implements Graph {
    private List<Vertex> vertices;
    private int[][] adjacencyMatrix;

    //Пустой констркутор
    public AdjacencyMatrixGraph(){
        this.vertices = new ArrayList<>();
        this.adjacencyMatrix = null;
    }

    //Конструктор по матрице смежности
    public AdjacencyMatrixGraph(int[][] adjacencyMatrix) {
        int n = adjacencyMatrix.length;
        //Проверка на корректность размеров матрицы смежности
        for (int i = 0; i < n; i++) {
            if (adjacencyMatrix[i].length != n) {
                throw new IllegalArgumentException("Matrix must be square");
            }
        }

        this.adjacencyMatrix = adjacencyMatrix;
        vertices = new ArrayList<>();
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            vertices.add(new Vertex(Integer.toString(i), i));
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
                System.out.println("Vertex already exists");
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

        if(!vertices.contains(vertex)) {
            System.out.println("Vertex does not exist");
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
        if(adjacencyMatrix[from][to] == 1) {
            System.out.println("Edge already exists");
            return false;
        }

        adjacencyMatrix[from][to] = 1;

        return true;
    }

    @Override
    public boolean removeEdge(int from, int to) {
        if (adjacencyMatrix[from][to] == 0){
            System.out.println("Edge does not exist");
            return false;
        }

        adjacencyMatrix[from][to] = 0;

        return true;
    }

    @Override
    public List<Vertex> getVertices() {
        return vertices;
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


}
