package chess;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private final ChessPiece[][] squares = new ChessPiece[8][8];

    private static final int BOARD_SIZE = 8;
    public ChessBoard() {

    }

    public boolean isEmpty(ChessPosition position) {
        return getPiece(position) == null;
    }

    public boolean isOpponent(ChessPosition position, ChessPiece oldPiece) {
        ChessPiece piece = getPiece(position);

        return piece != null && piece.getTeamColor() != oldPiece.getTeamColor();
    }

    public boolean isOwn(ChessPosition position, ChessPiece oldPiece) {
        ChessPiece piece = getPiece(position);

        return piece != null && piece.getTeamColor() == oldPiece.getTeamColor();
    }


    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares [position.getRow() - 1][position.getColumn() - 1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow() - 1][position.getColumn() - 1];
    }


    // Other methods and properties...

    public boolean isInBoard(int row, int col) {
        return row >= 1 && row <= BOARD_SIZE && col >= 1 && col <= BOARD_SIZE;
    }
    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        throw new RuntimeException("Not implemented");
    }
}
