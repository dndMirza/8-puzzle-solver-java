import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;
import java.util.Arrays;

/*
  PuzzleSolver is a class that contains the methods used to search for and print solutions
  to the 8-puzzle problem plus the data structures needed for the search.
  It implements both uniform cost search and A* search algorithms.
 */
public class PuzzleSolver {
    // For search statistics
    private ArrayList<PuzzleNode> expanded = new ArrayList<>();
    private PuzzleNode rootNode;

    // Search type constants
    private static final int UNIFORM_COST_SEARCH = 1;
    private static final int A_STAR_SEARCH = 2;

    /*
      Constructor that sets up an instance of the class with a node corresponding
      to the initial state as the root node.
     */
    public PuzzleSolver(int[] initialBoard) {
        EightPuzzleState initialState = new EightPuzzleState(initialBoard);
        rootNode = new PuzzleNode(initialState);
    }

    //Helper class to efficiently track visited states
    private static class StateHashWrapper {
        private final int[] board;

        public StateHashWrapper(EightPuzzleState state) {
            this.board = state.board;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            StateHashWrapper that = (StateHashWrapper) o;
            return Arrays.equals(board, that.board);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(board);
        }
    }

    /*
      Solves the 8-puzzle using uniform cost search.
      UCS is optimal when all step costs are equal (which they are in this puzzle).
     */
    public void solveWithUCS(PrintWriter output) {
        long startTime = System.currentTimeMillis();

        PriorityQueue<PuzzleNode> unexpanded = new PriorityQueue<>();
        Set<StateHashWrapper> visitedStates = new HashSet<>();

        unexpanded.add(rootNode);

        while (!unexpanded.isEmpty()) {
            PuzzleNode current = unexpanded.poll();

            // Skip if we've already visited this state (can happen due to priority queue)
            StateHashWrapper currentWrapper = new StateHashWrapper(current.state);
            if (visitedStates.contains(currentWrapper)) {
                continue;
            }

            expanded.add(current);

            if (current.state.isGoal()) {
                long endTime = System.currentTimeMillis();
                reportSolution(current, output, endTime - startTime, unexpanded.size());
                return;
            }

            // Add current state to visited states
            visitedStates.add(currentWrapper);

            // Generate possible moves
            ArrayList<EightPuzzleState> moves = current.state.possibleMoves();
            for (EightPuzzleState nextState : moves) {
                StateHashWrapper nextWrapper = new StateHashWrapper(nextState);

                // Skip if we've already visited this state
                if (!visitedStates.contains(nextWrapper)) {
                    int newCost = current.getCost() + 1; // +1 for each move
                    PuzzleNode newNode = new PuzzleNode(nextState, current, newCost);
                    unexpanded.add(newNode);
                }
            }
        }

        output.println("No solution found");
    }

    /*
      Solves the 8-puzzle using A* search with Manhattan Distance heuristic.
      A* search is optimal when using an admissible heuristic.
     */
    public void solveWithAStar(PrintWriter output) {
        long startTime = System.currentTimeMillis();

        PriorityQueue<PuzzleNode> unexpanded = new PriorityQueue<>();
        Set<StateHashWrapper> visitedStates = new HashSet<>();

        // Calculate Manhattan Distance heuristic for the root node
        int initialHeuristic = rootNode.state.getManhattanDistance();

        // Create a new root node with the heuristic
        PuzzleNode rootWithHeuristic = new PuzzleNode(rootNode.state, null, 0, initialHeuristic);
        unexpanded.add(rootWithHeuristic);

        while (!unexpanded.isEmpty()) {
            PuzzleNode current = unexpanded.poll();

            // Skip if we've already visited this state (can happen due to priority queue)
            StateHashWrapper currentWrapper = new StateHashWrapper(current.state);
            if (visitedStates.contains(currentWrapper)) {
                continue;
            }

            expanded.add(current);

            if (current.state.isGoal()) {
                long endTime = System.currentTimeMillis();
                reportSolution(current, output, endTime - startTime, unexpanded.size());
                return;
            }

            // Add current state to visited states
            visitedStates.add(currentWrapper);

            // Generate possible moves
            ArrayList<EightPuzzleState> moves = current.state.possibleMoves();
            for (EightPuzzleState nextState : moves) {
                StateHashWrapper nextWrapper = new StateHashWrapper(nextState);

                // Skip if we've already visited this state
                if (!visitedStates.contains(nextWrapper)) {
                    int newCost = current.getCost() + 1; // +1 for each move
                    int newHeuristic = nextState.getManhattanDistance();
                    PuzzleNode newNode = new PuzzleNode(nextState, current, newCost, newHeuristic);
                    unexpanded.add(newNode);
                }
            }
        }

        output.println("No solution found");
    }

    /*
      Prints all the states in a solution recursively.
      It uses the parent links to trace from the goal to the initial state then prints
      each state as the recursion unwinds.
     */
    public void printSolution(PuzzleNode node, PrintWriter output) {
        if (node.parent != null) {
            printSolution(node.parent, output);
        }
        output.println(node.state);
        output.println(); // Blank line between states for readability
    }

    /*
      Reports the solution together with statistics on the number of moves,
      execution time, and the number of expanded and unexpanded nodes.
     */
    public void reportSolution(PuzzleNode node, PrintWriter output, long executionTime, int unexpandedCount) {
        output.println("Solution found!");
        output.println();
        output.println("Initial state:");
        output.println(rootNode.state);
        output.println();
        output.println("Solution path:");
        printSolution(node, output);
        output.println(node.getCost() + " Moves");
        output.println("Execution time: " + executionTime + " ms");
        output.println("Nodes expanded: " + this.expanded.size());
        output.println("Nodes unexpanded: " + unexpandedCount);
    }

    /*
      Main method to run the solver program.
      Allows the user to choose between uniform cost search and A* search with Manhattan Distance.
     */
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        // Get initial and goal states
        System.out.println("Using default initial state: 8 7 6 5 4 3 2 1 0");
        System.out.println("Using default goal state: 1 2 3 4 5 6 7 8 0");
        System.out.println();

        // Create the solver with the default initial state
        PuzzleSolver solver = new PuzzleSolver(EightPuzzleState.INITIAL_BOARD);

        // Choose search algorithm
        System.out.println("Choose search algorithm:");
        System.out.println("1. Uniform Cost Search");
        System.out.println("2. A* Search with Manhattan Distance");
        System.out.print("Enter choice (1 or 2): ");
        int searchChoice = scanner.nextInt();

        // Create appropriate output file
        String outputFileName;
        PrintWriter output;

        if (searchChoice == UNIFORM_COST_SEARCH) {
            outputFileName = "outputUniCost.txt";
            output = new PrintWriter(new File(outputFileName));

            System.out.println("Running Uniform Cost Search...");
            solver.solveWithUCS(output);

        } else if (searchChoice == A_STAR_SEARCH) {
            outputFileName = "outputAstar.txt";
            output = new PrintWriter(new File(outputFileName));

            System.out.println("Running A* Search with Manhattan Distance...");
            solver.solveWithAStar(output);

        } else {
            System.out.println("Invalid choice. Exiting program.");
            scanner.close();
            return;
        }

        output.close();
        System.out.println("Solution saved to " + outputFileName);
        scanner.close();
    }
}