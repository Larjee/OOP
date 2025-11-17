package ru.nsu.munkuev;

import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void printVertices(List<Vertex> vertices) {
        int n  = vertices.size();
        for (int i = 0; i < n; i++) {
            System.out.printf("%d (label: %s)\n", vertices.get(i).getId(), vertices.get(i).getLabel());
        }
        System.out.println();
    }


    public static void printAdjMatrix(int [][] adjacencyMatrix) {
        int n = adjacencyMatrix.length;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.printf(adjacencyMatrix[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int[][] adjacencyMatrix = new int[][]{{0, 1, 1}, {1, 0, 1}, {1, 1, 0}};


        //====================BEGIN OF GRAPH EXPERIMENTS====================
        AdjacencyMatrixGraph myGraph = new AdjacencyMatrixGraph(adjacencyMatrix);

        //====================Получение всех вершин и их вывод ====================
        System.out.println("====================Getting vertices====================");
        List<Vertex> vertices = myGraph.getVertices();
        System.out.printf("\nVertices: ");
        printVertices(vertices);

        //==================== Добавление вершин ====================
        System.out.println("====================Adding vertices====================");
        myGraph.addVertex(new Vertex("Alpha", 3));//Просто вершина
        myGraph.addVertex(new Vertex("Beta", 3));//Просто вершина

        vertices = myGraph.getVertices();
        adjacencyMatrix = myGraph.getAdjacencyMatrix();

        printVertices(vertices);
        printAdjMatrix(adjacencyMatrix);



        //==================== Добавление ребер ====================
        System.out.println("====================Adding edges====================");
        myGraph.addEdge(0,1);//Такое есть
        myGraph.addEdge(0,0);//Такого нет
        myGraph.addEdge(1,3);//Нет вершины такой

        adjacencyMatrix = myGraph.getAdjacencyMatrix();
        printAdjMatrix(adjacencyMatrix);


        //==================== Получение детей и родителей ====================
        System.out.println("===================Getting children and parents====================");
        List<Integer> children = myGraph.getChildren(0);
        List<Integer> parents = myGraph.getParents(1);

        System.out.printf("Children of 0 vertex: %s\n", children.toString());
        System.out.printf("Parents of 1 vertex: %s\n", parents.toString());

        scanner.close();


        //================= TOPOSORT =================
        int[][] incidence = {
                {-1, -1,  0,  0,  0,  0}, // 0
                { 1,  0, -1,  0,  0,  0}, // 1
                { 0,  1,  0, -1,  0, -1}, // 2
                { 0,  0,  1,  1, -1,  0}, // 3
                { 0,  0,  0,  0,  1,  0}, // 4
                { 0,  0,  0,  0,  0,  1}  // 5
        };


        List<Vertex> vertices_2 = new ArrayList<>();
        for(int i = 0; i < 6; i++){
            // подставь под свой конструктор Vertex, если он другой
            vertices_2.add(new Vertex("v" + i, i));
        }

        Graph g = new IncidenceMatrixGraph(vertices_2, incidence);

        TopologicalSort sorter = new KahnTopologicalSort();
        System.out.println("Topological sort (Kahn):");
        for (Vertex v : sorter.topologicalSort(g)) {
            System.out.print(v.getId() + " ");
        }
        System.out.println();
    }

}
