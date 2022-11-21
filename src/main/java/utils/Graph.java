package utils;

import java.util.*;

public class Graph<ID> {
    private final Map<ID, Set<ID>> adjList;
    private final Map<ID, Boolean> visited;

    public Graph() {
        this.adjList = new HashMap<>();
        this.visited = new HashMap<>();
    }

    /**
     * Adds a directed edge to the graph
     * @param nodeL id of left end
     * @param nodeR id of right end
     */
    public void addEdge(ID nodeL, ID nodeR) {
        if (nodeL != nodeR) {
            if (!adjList.containsKey(nodeL)) {
                adjList.put(nodeL, new HashSet<>());
            }
            adjList.get(nodeL).add(nodeR);
        }
    }

    /**
     * Adds an undirected edge to the graph
     * @param nodeL id of left end
     * @param nodeR id of right end
    */
    public void addUndirectedEdge(ID nodeL, ID nodeR) {
        addEdge(nodeL, nodeR);
        addEdge(nodeR, nodeL);
    }

    /**
     * Returns the connected components of the graph
     * @return a list with all the connected components.
     * The connected components are represented as a list with all the
     * IDs of the nodes in that component
     */
    public List<List<ID>> getConnectedComponents() {
        List<List<ID>> connectedComp = new ArrayList<>();

        adjList.keySet().forEach(
                x -> visited.put(x, false)
        );

        adjList.keySet().forEach(
                x -> {
                    if (!visited.get(x)) {
                        List<ID> newCompList = new ArrayList<>();
                        newCompList.add(x);
                        DFSUtil(x, newCompList);
                        connectedComp.add(newCompList);
                    }
                }
        );

        return connectedComp;
    }

    /**
     * Return the number of connected components in the graph
     * @return number of components
     */
    public int getConnectedCompsNum() {
        return getConnectedComponents().size();
    }

    /**
     * Utility function to traverse a connected component using DFS
     * @param x the starting node
     * @param currentComp the connected component that will be traversed
     */
    private void DFSUtil(ID x, List<ID> currentComp) {
        visited.put(x, true);
//        System.out.print(x + " --- ");
        adjList.get(x).forEach(
                adjNode -> {
                    if (!visited.get(adjNode)) {
                        currentComp.add(adjNode);
                        DFSUtil(adjNode, currentComp);
                    }
                }
        );
    }

    /**
     * Determines the connected component of the graph with the longest path
     * @return a list with IDs of the nodes contained in the component
     */
    public List<ID> getComponentWithMaxLen() {
        int maxLen = -1;
        List<ID> biggestComp = new ArrayList<>();

        for (List<ID> currentGraph : getConnectedComponents()) {
            int compMaxLen = getComponentMaxLen(currentGraph);
            if (compMaxLen > maxLen) {
                maxLen = compMaxLen;
                biggestComp = currentGraph;
            }
        }
        return biggestComp;
    }

    /**
     * Computes the length of the longest path in a given graph
     * @param graph the graph for which the length is going to be computed
     * @return the length of the path
     */
    private int getComponentMaxLen(List<ID> graph) {
        ID startNode = graph.get(0);

        ID longPathStartNode = BFSUtil(startNode);

        Set<ID> set = new HashSet<>();
        set.add(longPathStartNode);

        return LEEUtil(longPathStartNode, set);
    }

    /**
     * Lee algorithm utility to compute the longest path in a graph
     * @param x starting node
     * @param set a set to remember visited nodes
     * @return the length of the longest path in the graph
     */
    private int LEEUtil(ID x, Set<ID> set) {
        int max = -1;

        for (ID adjNode : adjList.get(x)) {
            if (!set.contains(adjNode)) {
                set.add(adjNode);
                int l = LEEUtil(adjNode, set);
                if (l > max)
                    max = l;
                set.remove(adjNode);
            }
        }
        return max + 1;
    }

    /**
     * BFS utility function to determine the starting node of the
     * longest path in a graph
     * <p> It determines the node farthest away from the starting node </p>
     * @param start a random node of the graph to start the algorithm
     * @return the ID of the farthest node
     */
    private ID BFSUtil(ID start) {
        Map<ID, Integer> distances = new HashMap<>();
        Queue<ID> queue = new LinkedList<>();

        queue.add(start);
        distances.put(start, 0);

        while (!queue.isEmpty()) {
            ID node = queue.poll();

            adjList.get(node).forEach(
                    adjNode -> {
                        if (!distances.containsKey(adjNode)) {
                            queue.add(adjNode);
                            distances.put(adjNode, distances.get(node) + 1);
                        }
                    }
            );
        }

        Optional<Map.Entry<ID, Integer>> result = distances.entrySet().stream().reduce(
                (x, y) -> {
                    if (x.getValue() > y.getValue())
                        return x;
                    else
                        return y;
                }
        );

        ID nodeIndex = null;
        if (result.isPresent()) {
            nodeIndex = result.get().getKey();
        }
        return nodeIndex;
    }
}