package ru.nsu.munkuev;

import java.util.ArrayList;
import java.util.List;

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
        int n = vertices.size();

        for(Vertex v : vertices) {
            if(v.getId() == vertex.getId()) {
                System.out.println("Vertex already exists");
                return false;
            }
        }

        vertices.add(vertex);
        vertices.get(n).setId(n);

        this.adjacencyList.add(new ArrayList<>());

        return true;
    }

    @Override
    public boolean removeVertex(Vertex vertex) {
        int vertexIndex = vertex.getId();

        if(!vertices.contains(vertex)) {
            System.out.println("Vertex does not exist");
            return false;
        }

        //Вычеркиваем из списка вершин и списка смежности
        vertices.remove(vertexIndex);
        adjacencyList.remove(vertexIndex);

        //Перенумеровываем id у вершин вправо от удалённой
        for (int k = vertexIndex; k < vertices.size(); k++) {
            vertices.get(k).setId(k);
        }

        return true;
    }

    @Override
    public boolean addEdge(int from, int to) {
        //Проверяем нет ли уже такого ребра
        List<Integer> src = adjacencyList.get(from);
        if(src.contains(to)) {
            System.out.println("Edge already exists");
            return false;
        }
        src.add(to);

        return true;
    }

    @Override
    public boolean removeEdge(int from, int to) {
        //Проверяем есть ли такое ребро
        List<Integer> src = adjacencyList.get(from);
        if(src.contains(to)) {
            System.out.println("Edge does not exists");
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
        return adjacencyList.get(vertex);
    }


}
