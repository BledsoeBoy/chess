package chess.PieceMovesCalculator;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;

public class KnightMovesCalculator {
    public static void pieceMoves(ChessBoard board, ChessPosition position, Collection<ChessMove> moves) {
        int row = position.getRow();
        int col = position.getColumn();

        addKnightMovesInDirection(board, position, row, col, -2, -1, moves);
        addKnightMovesInDirection(board, position, row, col, -2, 1, moves);
        addKnightMovesInDirection(board, position, row, col, 2, -1, moves);
        addKnightMovesInDirection(board, position, row, col, 2, 1, moves);
        addKnightMovesInDirection(board, position, row, col, -1, -2, moves);
        addKnightMovesInDirection(board, position, row, col, 1, -2, moves);
        addKnightMovesInDirection(board, position, row, col, -1, 2, moves);
        addKnightMovesInDirection(board, position, row, col, 1, 2, moves);
    }
    private static void addKnightMovesInDirection(ChessBoard board, ChessPosition position, int startRow, int startCol, int rowDirection, int colDirection, Collection<ChessMove> moves) {
        int row = startRow + rowDirection;
        int col = startCol + colDirection;

        if (board.isInBoard(row, col)) {
            ChessPosition newPosition = new ChessPosition(row, col);
            if ((board.isEmpty(newPosition) || board.isOpponent(newPosition, board.getPiece(position)))) {
                moves.add(new ChessMove(position, newPosition, null));
            }
        }
    }
}
