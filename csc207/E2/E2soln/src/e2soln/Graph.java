package e2soln;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * An undirected graph of Node<T>s.
 * @param <T> the type of values in this Graph's Nodes.
 */
public class Graph<T> {

    // instance variable
	private Map<Node<T>, Set<Node<T>>> graphMap;

    /**
     * Creates a new empty Graph.
     */
	public Graph() {
		graphMap = new HashMap<Node<T>, Set<Node<T>>>();
	}

    /**
     * Returns a Set of Nodes in this Graph.
     * @return a Set of Nodes in this Graph
     */
	public Set<Node<T>> getNodes() {
		return graphMap.keySet();
	}

    /**
     * Returns the Node from this Graph with the given ID.
     * @param id the ID of the Node to return
     * @return the Node from this Graph with the given ID
     * @throws NoSuchNodeException if there is no Node with ID
     *    id in this Graphs
     */
	public Node<T> getNode(int id) throws NoSuchNodeException {
		for(Node<T> node: graphMap.keySet()) {
			if (node.getId() == id) {
				return node;
			}
		}
		throw new NoSuchNodeException(Integer.toString(id));
	}

    /**
     * Returns a Set of neighbours of the given Node.
     * @param node the Node whose neighbours are returned
     * @return a Set of neighbours of node
     */
	public Set<Node<T>> getNeighbours(Node<T> node) {
		return graphMap.get(node);
	}

    /**
     * Returns whether Nodes with the given IDs are adjacent in this Graph.
     * @param id1 ID of the node to test for adjacency
     * @param id2 ID of the node to test for adjacency
     * @return true, if Nodes with IDs id1 and id2 are adjacent in this Graph,
     *    and false otherwise
     * @throws NoSuchNodeException if node with ID id1 or id2 is not in this Graph
     */
    public boolean areAdjacent(int id1, int id2) throws NoSuchNodeException {
    	if (graphMap.get(getNode(id1)).contains(getNode(id2)) ||
    			graphMap.get(getNode(id2)).contains(getNode(id1))) {
    		return true;
    	}
    	return false;
    }

    /**
     * Returns whether the given Nodes are adjacent in this Graph.
     * @param node1 the Node to test for adjacency with node2
     * @param node2 the Node to test for adjacency with node1
     * @return true, if node1 and node2 are adjacent in this Graph,
     *    and false otherwise
     * @throws NoSuchNodeException if node1 or node2 are not in this Graph
     */
    public boolean areAdjacent(Node<T> node1, Node<T> node2) throws NoSuchNodeException {
    	if (graphMap.containsKey(node1) && graphMap.containsKey(node2)) {
    		
    		if (graphMap.get(node1).contains(node2) ||
    				graphMap.get(node2).contains(node1)) {
    			return true;
    		}
    		return false;
    	
    	}
    	else { throw new NoSuchNodeException(); }
    }

    /**
     * Returns the number of nodes in this Graph.
     * @return The number of nodes in this Graph.
     */
    public int getNumNodes() {
        return getNodes().size();	
    }

    /**
     * Returns the number of edges in this Graph.
     * @return The number of edges in this Graph.
     */
    public int getNumEdges() {	
        int total = 0;

        for (Node<T> node : getNodes()) {
            total += getNeighbours(node).size();
        }

        return total / 2;
    }

    /**
     * Adds a new Node with the given value to this Graph. 
     * @param id the ID of the new Node
     * @param value the value of the new Node
     */
    public void addNode(int id, T value) {
    	graphMap.put(new Node<T>(id, value), new TreeSet<Node<T>>());
    }

    /**
     * Adds an edge between the given nodes in this Graph. If there 
     * is already an edge between node1 and node2, does nothing.
     * @param node1 the node to add an edge to and from node2
     * @param node2 the node to add an edge to and from node1
     * @throws NoSuchNodeException if node1 or node2 is not in
     *    this Graph
     */
    public void addEdge(Node<T> node1, Node<T> node2) throws NoSuchNodeException {
    	if (!areAdjacent(node1, node2) && node1 != node2) {
    		graphMap.get(node1).add(node2);
    		graphMap.get(node2).add(node1);
   		}
    }

    /**
     * Adds an edge between the nodes with the given IDs in this Graph. 
     * @param id1 ID of the node to add an edge to and from
     * @param id2 ID of the node to add an edge to and from
     * @throws NoSuchNodeExceptionf there is no Node with ID 
     *    id1 or ID id2 in this Graph.
     */
    public void addEdge(int id1, int id2) throws NoSuchNodeException {
    	if (!areAdjacent(id1, id2) && id1 != id2) {
    		graphMap.get(getNode(id1)).add(getNode(id2));
    		graphMap.get(getNode(id2)).add(getNode(id1));
    	}
    }

    @Override
    public String toString() {

        String result = "";
        result += "Number of nodes: " + getNumNodes() + "\n";
        result += "Number of edges: " + getNumEdges() + "\n";

        for (Node<T> node: getNodes()) {
            result += node + " is adjacent to: ";
            for    (Node<T> neighbour: getNeighbours(node)) {
                result += neighbour + " ";
            }
            result += "\n";
        }
        return result;
    }
}