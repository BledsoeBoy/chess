package chess;

import chess.PieceMovesCalculator.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    public final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {

        return type;
    }

    @Override
    public String toString() {
        return "ChessPiece{}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChessPiece that)) return false;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        Collection<ChessMove> moves = new ArrayList<>();

        if (type == PieceType.KING) {
            KingMovesCalculator.pieceMoves(board, myPosition, moves);
        }
        if (type == PieceType.QUEEN) {
            QueenMovesCalculator.pieceMoves(board, myPosition, moves);
        }
        if (type == PieceType.KNIGHT) {
            KnightMovesCalculator.pieceMoves(board, myPosition, moves);
        }
        if (type == PieceType.PAWN) {
            PawnMovesCalculator.pieceMoves(board, myPosition, moves);
        }
        if (type == PieceType.ROOK) {
            RookMovesCalculator.pieceMoves(board, myPosition, moves);
        }
        if (type == PieceType.BISHOP) {
            BishopMovesCalculator.pieceMoves(board, myPosition, moves);
        }



        return moves;
    }

}

