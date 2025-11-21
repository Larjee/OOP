package ru.nsu.munkuev;

import java.util.List;

public interface Graph {
     boolean addVertex(Vertex vertex);
     boolean removeVertex(Vertex vertex);

     boolean addEdge(int from, int to);
     boolean removeEdge(int from, int to);

     List<Vertex> getVertices();

     List<Integer> getParents(int vertex);
     List<Integer> getChildren(int vertex);

     boolean equals(Object o);

}
