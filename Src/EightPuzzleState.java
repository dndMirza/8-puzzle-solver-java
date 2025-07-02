import java.util.ArrayList;

/*
  Instances of the class EightPuzzleState represent states that can arise in the 8-puzzle game.
  The int array board represents the board configuration; that is, the location of each of the
  eight tiles and the empty space. Empty space is represented by 0.
  The board is represented as a 1D array of length 9, where positions correspond to:
  0 1 2
  3 4 5
  6 7 8
 */
public class EightPuzzleState {
    final int[] board;
    private int emptyPos; // Position of the empty space

    // Example initial and goal states
    static final int[] INITIAL_BOARD = {8, 7, 6, 5, 4, 3, 2, 1, 0}; // This can be changed as needed
    static final int[] GOAL_BOARD = {1, 2, 3, 4, 5, 6, 7, 8, 0};    // This can be changed as needed

    //Constructor that takes an int array holding a board configuration as argument.
    public EightPuzzleState(int[] board) {
        this.board = board;
        for (int i = 0; i < 9; i++) {
            if (board[i] == 0) {
                this.emptyPos = i;
                break;
            }
        }
    }

    //Returns a new EightPuzzleState with the same board configuration as the current EightPuzzleState.
    public EightPuzzleState clone() {
        int[] clonedBoard = new int[9];
        System.arraycopy(this.board, 0, clonedBoard, 0, 9);
        return new EightPuzzleState(clonedBoard);
    }

    //Returns the position of the empty space.
    public int getEmptyPos() {
        return emptyPos;
    }

    //Returns the board configuration of the current EightPuzzleState as a printable string.
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 9; i++) {
            sb.append(board[i]).append(" ");
            if (i % 3 == 2 && i < 8) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    //Returns true if and only if the board configuration of the current EightPuzzleState is the goal configuration.
    public boolean isGoal() {
        for (int i = 0; i < 9; i++) {
            if (this.board[i] != GOAL_BOARD[i]) return false;
        }
        return true;
    }

    /*
      Returns true if and only if the EightPuzzleState supplied as argument has the same board
      configuration as the current EightPuzzleState.
     */
    public boolean sameBoard(EightPuzzleState state) {
        for (int i = 0; i < 9; i++) {
            if (this.board[i] != state.board[i]) return false;
        }
        return true;
    }

    /*
      Returns a list of all EightPuzzleStates that can be reached in a single move from the current EightPuzzleState.
      A tile can only move into an adjacent empty space (no jumping in the 8-puzzle).
     */
    public ArrayList<EightPuzzleState> possibleMoves() {
        ArrayList<EightPuzzleState> moves = new ArrayList<>();

        // Calculate row and column of empty space
        int row = emptyPos / 3;
        int col = emptyPos % 3;

        // Check all four possible moves: up, down, left, right

        // Move up (tile below the empty space moves up)
        if (row < 2) {
            int newEmptyPos = emptyPos + 3;
            EightPuzzleState newState = this.clone();
            newState.board[emptyPos] = this.board[newEmptyPos];
            newState.board[newEmptyPos] = 0;
            newState.emptyPos = newEmptyPos;
            moves.add(newState);
        }

        // Move down (tile above the empty space moves down)
        if (row > 0) {
            int newEmptyPos = emptyPos - 3;
            EightPuzzleState newState = this.clone();
            newState.board[emptyPos] = this.board[newEmptyPos];
            newState.board[newEmptyPos] = 0;
            newState.emptyPos = newEmptyPos;
            moves.add(newState);
        }

        // Move right (tile to the left of the empty space moves right)
        if (col > 0) {
            int newEmptyPos = emptyPos - 1;
            EightPuzzleState newState = this.clone();
            newState.board[emptyPos] = this.board[newEmptyPos];
            newState.board[newEmptyPos] = 0;
            newState.emptyPos = newEmptyPos;
            moves.add(newState);
        }

        // Move left (tile to the right of the empty space moves left)
        if (col < 2) {
            int newEmptyPos = emptyPos + 1;
            EightPuzzleState newState = this.clone();
            newState.board[emptyPos] = this.board[newEmptyPos];
            newState.board[newEmptyPos] = 0;
            newState.emptyPos = newEmptyPos;
            moves.add(newState);
        }

        return moves;
    }

    /*
      Calculates the Manhattan distance heuristic for A* search.
      Manhattan distance is the sum of the distances of the tiles from their goal positions,
      measured along the grid (not diagonally).
     */
    public int getManhattanDistance() {
        int distance = 0;
        for (int i = 0; i < 9; i++) {
            if (board[i] != 0) { // Skip the empty tile
                int currentRow = i / 3;
                int currentCol = i % 3;

                // Find where this tile should be in the goal state
                int targetPosition = -1;
                for (int j = 0; j < 9; j++) {
                    if (GOAL_BOARD[j] == board[i]) {
                        targetPosition = j;
                        break;
                    }
                }

                int targetRow = targetPosition / 3;
                int targetCol = targetPosition % 3;

                // Add Manhattan distance for this tile
                distance += Math.abs(currentRow - targetRow) + Math.abs(currentCol - targetCol);
            }
        }
        return distance;
    }
}