package chess;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {

    private int row;
    private int col;

    public ChessPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }
    public ChessPosition(ChessPosition pos){
        this.row = 8 - pos.getRow();
        this.col = pos.getColumn() + 1;
    }
    public String toString(){
        return "Col: " + this.col + " Row: " + this.row;
    }
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ChessPosition that = (ChessPosition) o;
        return this.getRow() == that.getRow() && this.getColumn() == that.getColumn();

    }
    @Override
    public int hashCode(){
        return (31 * this.row) + (21 *  this.col);
    }
    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    // If 1 at bottom, then this would be 7
    // 1-> 7, 2 -> 6, 3 -> 5, 7-> 1, 8 -> 0
    public int getRow() {
       return (8 - this.row);
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    // x-value
    public int getColumn() {
        return this.col - 1;
    }
    public int getBoardRow(){
        return this.row;
    }

    public int getBoardColumn(){
        return this.col;
    }

    public void addColumn(int toAdd){
        this.col += toAdd;
    }

    public void addRow(int toAdd){
        this.row += toAdd;
    }

    public int[] convertToArrayPos(int x, int y){
        return new int[]{8 - x, y - 1};
    }

    public int[] convertToBoardPos(int x, int y){
        return new int[]{8 - x, y + 1};
    }
}
