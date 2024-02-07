package chess;

import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessBoard board;
    private TeamColor turn;
    public ChessGame() {
        this.board = new ChessBoard();
        this.turn = TeamColor.WHITE;

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return turn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        turn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);

        if (piece != null && piece.getTeamColor() == turn) {
            return piece.pieceMoves(board, startPosition);
        }

        return null;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();

        ChessPiece piece = board.getPiece(start);

        if (piece == null || piece.getTeamColor() != turn) {
            throw new InvalidMoveException("Invalid move: No piece at the starting position or not your turn");
        }

        Collection<ChessMove> validMoves = validMoves(start);

        if (validMoves == null || !validMoves.contains(move)) {
            throw new InvalidMoveException("Invalid move: This move isn't valid");
        }

        // Make the move
        board.addPiece(end, piece);
        board.addPiece(start, null);

        // Check for pawn promotion

        // Switch turn
        turn = (turn == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = findKingPosition(teamColor);

        for (int r = 1; r <= ChessBoard.BOARD_SIZE; r++) {
            for (int c = 1; c <= ChessBoard.BOARD_SIZE; c++) {
                ChessPosition currentPosition = new ChessPosition(r,c);
                ChessPiece piece = board.getPiece(currentPosition);

                if (piece != null && piece.getTeamColor() != teamColor) {
                    Collection<ChessMove> movestoCheck = piece.pieceMoves(board, currentPosition);
                    if (movestoCheck != null && movestoCheck.contains(new ChessMove(currentPosition, kingPosition, null))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private ChessPosition findKingPosition(TeamColor color) {
        for (int r = 1; r <= ChessBoard.BOARD_SIZE; r++) {
            for (int c = 1; c <= ChessBoard.BOARD_SIZE; c++) {
                ChessPosition position = new ChessPosition(r,c);
                ChessPiece piece = board.getPiece(position);

                if (piece != null && piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == color) {
                    return position;
                }
            }
        }
        return null;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (!isInCheck(teamColor)) {
            return false;
        }

        for (int r = 1; r <= ChessBoard.BOARD_SIZE; r++) {
            for (int c = 1; c <= ChessBoard.BOARD_SIZE; c++) {
                ChessPosition currentPosition = new ChessPosition(r, c);
                ChessPiece piece = board.getPiece(currentPosition);

                if (piece != null && piece.getTeamColor() == teamColor) {
                    Collection<ChessMove> moves = piece.pieceMoves(board, currentPosition);

                    for (ChessMove move : moves) {
                        // Try each move and see if it gets the team out of check
                        try {
                            ChessGame cloneGame = clone(); // Create a clone of the game
                            cloneGame.makeMove(move); // Make the move in the cloned game

                            // If the team is not in check after making this move, return false (not in checkmate)
                            if (!cloneGame.isInCheck(teamColor)) {
                                return false;
                            }
                        } catch (InvalidMoveException e) {
                            // Invalid move, try the next move
                        }
                    }
                }
            }
        }

        // If no legal move gets the team out of check, it's in checkmate
        return true;
    }

    protected ChessGame clone() {
        // Create a new instance of ChessGame with the same board and turn
        ChessGame clonedGame = new ChessGame();
        clonedGame.board = this.board; // Assuming ChessBoard is immutable or properly handled
        clonedGame.turn = this.turn;
        return clonedGame;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
