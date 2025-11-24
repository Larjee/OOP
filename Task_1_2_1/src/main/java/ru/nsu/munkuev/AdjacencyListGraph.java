package ru.nsu.munkuev;

import java.util.*;

public class AdjacencyListGraph implements Graph {
    private List<Vertex> vertices;
    private List<List<Integer>> adjacencyList;

    //Пустой конструктор
    public AdjacencyListGraph() {
        this.vertices = new ArrayList<>();
        this.adjacencyList = new ArrayList<>();
    }

    //Конструктор по списку вершин
    public AdjacencyListGraph(List<Vertex> vertices) {
        if(vertices == null){
            throw new IllegalArgumentException("vertices is null");
        }

        int n = vertices.size();

        this.vertices = new ArrayList<>(n);
        this.adjacencyList = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            Vertex v = vertices.get(i);

            v.setId(i);
            this.vertices.add(v);
            this.adjacencyList.add(new ArrayList<>());
        }
    }

    //Конструктор полный
    public AdjacencyListGraph(List<Vertex> vertices, List<List<Integer>> adjacencyList) {
        if (vertices == null || adjacencyList == null) {
            throw new IllegalArgumentException("vertices/adjacencyList is null");
        }

        int n = vertices.size();
        if (adjacencyList.size() != n){
            throw new IllegalArgumentException("adjacencyList.size() != vertices.size()");
        }

        this.vertices = new ArrayList<>(n);
        this.adjacencyList = new ArrayList<>(n);

        //выравниваем id и копируем вершины
        for (int i = 0; i < n; i++) {
            Vertex v = vertices.get(i);
            v.setId(i);
            this.vertices.add(v);
        }

        //копируем списки смежности
        for (int i = 0; i < n; i++) {
            List<Integer> src = adjacencyList.get(i);
            List<Integer> dst = new ArrayList<>(src.size());
            for (Integer to : src) {
                if (to == null || to < 0 || to >= n) {
                    throw new IllegalArgumentException("bad edge " + i + "->" + to);
                }
                dst.add(to);
            }
            this.adjacencyList.add(dst);
        }


    }


    @Override
    public boolean addVertex(Vertex vertex) {

        //Проверки на корректность
        if(checkIndex(vertex.getId())){
            throw new IllegalArgumentException("Index out of bounds");
        }
        if(checkVertex(vertex)){
            System.out.printf("Cannot add vertex with id = %d because it already exists", vertex.getId());
            return false;
        }

        int n = vertices.size();

        //Добавляем в список вершин новую вершину
        vertices.add(vertex);
        //Сохраняем в id вершины ее индекс в матрице смежности
        vertices.get(n).setId(n);

        //Расширяем список смежности
        this.adjacencyList.add(new ArrayList<>());

        return true;
    }


    @Override
    public boolean removeVertex(Vertex vertex) {

        int vertexIndex = vertex.getId();

        //Проверки на корректность
        if(!checkIndex(vertex.getId())){
            throw new IllegalArgumentException("Index out of bounds");
        }
        if(!checkVertex(vertex)){
            System.out.printf("Cannot remove vertex with id = %d because it does not exists", vertex.getId());
            return false;
        }

        //Вычеркиваем из списка вершин и списка смежности
        vertices.remove(vertexIndex);
        adjacencyList.remove(vertexIndex);

        //Перенумеровываем id у вершин вправо от удалённой
        for (List<Integer> nbrs : adjacencyList) {
            //убрать все ссылки на idx
            nbrs.removeIf(to -> to == vertexIndex);
            //сдвинуть индексы, которые были правее
            for (int i = 0; i < nbrs.size(); i++) {
                int to = nbrs.get(i);
                if (to > vertexIndex) nbrs.set(i, to - 1);
            }
        }

        //Перенумеровываем id у вершин вправо от удалённой
        for (int i = vertexIndex; i < vertices.size(); i++) {
            vertices.get(i).setId(i);
        }

        return true;
    }


    @Override
    public boolean addEdge(int from, int to) {
        //Проверка на корректность индексов
        if(!checkIndex(from) || !checkIndex(to)){
            System.out.printf("Cannot add edge (%d, %d)) because index out of range", from, to);
            return false;
        }
        //Проверяем нет ли уже такого ребра
        List<Integer> src = adjacencyList.get(from);
        if(src.contains(to)) {
            System.out.printf("Cannot add edge (%d, %d) because it already exists\n", from, to);
            return false;
        }

        //Добавляем ребро
        src.add(to);

        return true;
    }


    @Override
    public boolean removeEdge(int from, int to) {
        //Проверка на корректность индексов
        if(!checkIndex(from) || !checkIndex(to)){
            System.out.printf("Cannot add edge (%d, %d)) because index out of range", from, to);
            return false;
        }
        //Проверяем наличие ребра
        List<Integer> src = adjacencyList.get(from);
        if(!src.contains(to)) {
            System.out.printf("Cannot remove edge (%d, %d) because it does not exist\n", from, to);
            return false;
        }

        //Ищем его и удаляем
        int srcSize = src.size();
        for(int i = 0; i < srcSize; i++) {
            if(src.get(i) == to) {
                adjacencyList.get(from).remove(i);
            }
        }

        return true;
    }


    @Override
    public List<Vertex> getVertices() {
        return vertices;
    }


    @Override
    public List<Integer> getParents(int vertex) {
        List<Integer> parents = new ArrayList<>();
        int n = vertices.size();

        //Пробегаемся по матрице смежности и смотрим из каких вершин можем попасть в vertex
        for(int i = 0; i < n; i++) {
            List<Integer> src = adjacencyList.get(i);
            if(src.contains(vertex)) {
                parents.add(i);
            }
        }

        return parents;
    }


    @Override
    public List<Integer> getChildren(int vertex) {
        List<Integer> children = new ArrayList<>();
        children.addAll(adjacencyList.get(vertex));

        return children;
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

        //Сравниваем количество вершин
        int n = this.getVertices().size();
        if (n != other.getVertices().size()) {
            return false;
        }

        //Сравниваем детей каждой вершины
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

        sb.append("AdjacencyListGraph").append(System.lineSeparator());

        sb.append("Vertices: ");
        for (Vertex v : vertices) {
            sb.append(v.getId()).append(' ');
        }
        sb.append(System.lineSeparator());

        sb.append("Adjacency list:").append(System.lineSeparator());
        for (int i = 0; i < adjacencyList.size(); i++) {
            sb.append(i).append(": ");
            List<Integer> neighbors = adjacencyList.get(i);
            for (int j = 0; j < neighbors.size(); j++) {
                sb.append(neighbors.get(j));
                if (j + 1 < neighbors.size()) {
                    sb.append(' ');
                }
            }
            sb.append(System.lineSeparator());
        }

        return sb.toString();
    }

    @Override
    public int hashCode() {
        int n = getVertices().size();
        int result = n;

        for (int v = 0; v < n; v++) {
            List<Integer> children = new ArrayList<>(getChildren(v));
            Collections.sort(children);
            result = 31 * result + children.hashCode();
        }

        return result;
    }

    /**
     * Проверяет корректность индекса, при добавлении вершины или вставке ребра.
     * @param index проверяемый индекс
     * @return результат проверки. {@code true} если индекс корректный {@code false} в противном случае.
     */
    private boolean checkIndex(int index) {
        return !(index < 0 || index >= vertices.size());
    }


    /**
     * Проверяет принадлежность вершины к текущему графу.
     * @param vertex вершина, для которой проверяется принадлежность
     * @return результат проверки
     */
    private boolean checkVertex(Vertex vertex) {
        for(Vertex v : vertices) {
            if(v.getId() == vertex.getId()) {
                return true;
            }
        }
        return false;
    }
}
