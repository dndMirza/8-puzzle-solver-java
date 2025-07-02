import java.util.ArrayList;

/*
  The class PuzzleNode represents nodes in the search tree.
  Its essential features are an EightPuzzleState and a reference to the node's parent node.
  The parent reference is used to assemble and output the solution path once the goal state is reached.
 */
public class PuzzleNode implements Comparable<PuzzleNode> {
    EightPuzzleState state;   // The state associated with the node
    PuzzleNode parent;        // The node from which this node was reached
    private int cost;         // The cost of reaching this node from the initial node
    private int heuristic;    // The heuristic value for A* search
    private int priority;     // Combined cost + heuristic for A* priority

    // Constructor used to create new nodes for uniform cost search.
    public PuzzleNode(EightPuzzleState state, PuzzleNode parent, int cost) {
        this.state = state;
        this.parent = parent;
        this.cost = cost;
        this.heuristic = 0;
        this.priority = cost;
    }
    
    // Constructor used to create new nodes for A* search.
    public PuzzleNode(EightPuzzleState state, PuzzleNode parent, int cost, int heuristic) {
        this.state = state;
        this.parent = parent;
        this.cost = cost;
        this.heuristic = heuristic;
        this.priority = cost + heuristic;
    }

    //Constructor used to create initial node.
    public PuzzleNode(EightPuzzleState state) {
        this(state, null, 0);
    }
    
    //Constructor used to create initial node with heuristic for A*.
    public PuzzleNode(EightPuzzleState state, int heuristic) {
        this(state, null, 0, heuristic);
    }

    public int getCost() {
        return cost;
    }
    
    public int getHeuristic() {
        return heuristic;
    }
    
    public int getPriority() {
        return priority;
    }

    public String toString() {
        return "Node:" + state.toString() + " ";
    }

    /*
      Given a list of nodes as first argument, findNodeWithState searches the list for a node
      whose state is that specified as the second argument.
      If such a node is in the list, the first one encountered is returned.
      Otherwise, null is returned.
     */
    public static PuzzleNode findNodeWithState(ArrayList<PuzzleNode> nodeList, EightPuzzleState state) {
        for (PuzzleNode n : nodeList) {
            if (state.sameBoard(n.state)) return n;
        }
        return null;
    }
    
    /*
      Implements the compareTo method for the Comparable interface.
      Nodes are compared based on their priority (cost + heuristic).
      This allows the priority queue to order nodes correctly for both UCS and A*.
     */
    @Override
    public int compareTo(PuzzleNode other) {
        return Integer.compare(this.priority, other.priority);
    }
}
