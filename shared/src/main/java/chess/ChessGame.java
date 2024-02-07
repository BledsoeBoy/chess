package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

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

        if (piece == null) {
            return null;
        }

        Collection<ChessMove> allMoves = piece.pieceMoves(board, startPosition);
        Collection<ChessMove> legalMoves = new ArrayList<>();

            for (ChessMove move : allMoves) {
                ChessGame cloneGame = clone();  // Create a clone of the game

                // Make the move in the cloned game
                ChessPosition start = move.getStartPosition();
                ChessPosition end = move.getEndPosition();

                ChessPiece clonePiece = cloneGame.getBoard().getPiece(start);

                cloneGame.getBoard().addPiece(end, clonePiece);
                cloneGame.getBoard().addPiece(start, null);

                // If the team is not in check after making this move, consider it a legal move
                if (!cloneGame.isInCheck(piece.getTeamColor())) {
                    legalMoves.add(move);
                }
            }
            return legalMoves;
    }


    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */

    private boolean isMoveValid(ChessMove move) {
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();

        ChessPiece piece = board.getPiece(start);

        // Check if the piece is not null and it's the correct team's turn
        if (piece != null && piece.getTeamColor() == turn) {
            Collection<ChessMove> validMoves = validMoves(start);
            return validMoves.contains(move);
        } else {
            // Invalid move: No piece at the starting position or not your turn
            return false;
        }
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if (!isMoveValid(move)) {
            throw new InvalidMoveException("Invalid move: This move isn't valid");
        }
        else {

            ChessPosition start = move.getStartPosition();
            ChessPosition end = move.getEndPosition();

            ChessPiece piece = board.getPiece(start);

            board.addPiece(end, piece);
            board.addPiece(start, null);

            // Check for pawn promotion
            if (piece != null && piece.getPieceType() == ChessPiece.PieceType.PAWN &&
                    ((piece.getTeamColor() == TeamColor.WHITE && end.getRow() == 8) ||
                            (piece.getTeamColor() == TeamColor.BLACK && end.getRow() == 1))) {
                promotePawn(move, end);
            }

            turn = (turn == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;
        }
    }

    private void promotePawn(ChessMove move, ChessPosition position) {
        // For simplicity, promoting the pawn to a queen
        ChessPiece promotedPiece = new ChessPiece(turn, move.getPromotionPiece());
        board.addPiece(position, promotedPiece);
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
                    Collection<ChessMove> movesToCheck = piece.pieceMoves(board, currentPosition);
                    for (ChessMove move : movesToCheck) {
                        if (move.getEndPosition().equals(kingPosition)) { //use .equals() here because comparing objects
                            return true;
                        }
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
                        ChessGame cloneGame = clone(); // Create a clone of the game
                        cloneGame.validMoves(move.getStartPosition()); // Make the move in the cloned game

                        //checks to see if there are any valid moves
                        if (!cloneGame.validMoves(move.getStartPosition()).isEmpty()) {
                            return false;
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

        for (int row = 1; row <= ChessBoard.BOARD_SIZE; row++) {
            for (int col = 1; col <= ChessBoard.BOARD_SIZE; col++) {

                ChessPosition position = new ChessPosition(row, col);
                if (this.board.getPiece(position) != null) {
                    clonedGame.board.addPiece(position, this.board.getPiece(position));
                }
            }
        }

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
        // Check if the team's king is not in check
        if (!isInCheck(teamColor)) {
            // Iterate through all pieces of the team
            for (int r = 1; r <= ChessBoard.BOARD_SIZE; r++) {
                for (int c = 1; c <= ChessBoard.BOARD_SIZE; c++) {
                    ChessPosition currentPosition = new ChessPosition(r, c);
                    ChessPiece piece = board.getPiece(currentPosition);

                    // Check if the piece belongs to the specified team
                    if (piece != null && piece.getTeamColor() == teamColor) {
                        // Check if the piece has any legal moves
                        Collection<ChessMove> moves = piece.pieceMoves(board, currentPosition);
                        for (ChessMove move : moves) {
                                ChessGame cloneGame = clone(); // Create a clone of the game
                                cloneGame.validMoves(move.getStartPosition()); // Make the move in the cloned game

                                if (!cloneGame.validMoves(move.getStartPosition()).isEmpty()) {
                                    return false;
                                }
                        }
                    }
                }
            }

            // If no piece of the team has any legal moves, it's in stalemate
            return true;
        }

        return false;
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
