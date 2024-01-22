package chess.PieceMovesCalculator;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;

public class BishopMovesCalculator {
    public static void pieceMoves(ChessBoard board, ChessPosition position, Collection<ChessMove> moves) {
        int row = position.getRow();
        int col = position.getColumn();

        addBishopMovesInDirection(board, position, row, col, -1, -1, moves);
        addBishopMovesInDirection(board, position, row, col, -1, 1, moves);
        addBishopMovesInDirection(board, position, row, col, 1, -1, moves);
        addBishopMovesInDirection(board, position, row, col, 1, 1, moves);
    }

    private static void addBishopMovesInDirection(ChessBoard board, ChessPosition position, int startRow, int startCol, int rowDirection, int colDirection, Collection<ChessMove> moves) {
        int row = startRow + rowDirection;
        int col = startCol + colDirection;

        while (board.isInBoard(row, col)) {
            ChessPosition newPosition = new ChessPosition(row, col);
            if (board.isEmpty(newPosition)) {
                moves.add(new ChessMove(position, newPosition, null));
            }
            else if (board.isOpponent(newPosition, board.getPiece(position))) {
                moves.add(new ChessMove(position, newPosition, null));
                break;
            }
            else if (board.isOwn(newPosition, board.getPiece(position))) {
                break;
            }

            row += rowDirection;
            col += colDirection;
        }
    }
}


