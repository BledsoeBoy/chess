package chess.PieceMovesCalculator;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;

public class QueenMovesCalculator {
    public static void pieceMoves(ChessBoard board, ChessPosition position, Collection<ChessMove> moves) {
        int row = position.getRow();
        int col = position.getColumn();

        addDiagonalMovesInDirection(board, position, row, col, -1, -1, moves);
        addDiagonalMovesInDirection(board, position, row, col, -1, 1, moves);
        addDiagonalMovesInDirection(board, position, row, col, 1, -1, moves);
        addDiagonalMovesInDirection(board, position, row, col, 1, 1, moves);

        addQueenMovesInRowDirection(board, position, row, col, -1, moves);
        addQueenMovesInRowDirection(board, position, row, col, 1, moves);
        addQueenMovesInColDirection(board, position, row, col, -1, moves);
        addQueenMovesInColDirection(board, position, row, col, 1, moves);
    }

    private static void addDiagonalMovesInDirection(ChessBoard board, ChessPosition position, int startRow, int startCol, int rowDirection, int colDirection, Collection<ChessMove> moves) {
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
    public static void addQueenMovesInRowDirection(ChessBoard board, ChessPosition position, int startRow, int startCol, int rowDirection, Collection<ChessMove> moves) {
        int row = startRow + rowDirection;
        int col = startCol;

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
        }
    }

    public static void addQueenMovesInColDirection(ChessBoard board, ChessPosition position, int startRow, int startCol, int colDirection, Collection<ChessMove> moves) {
        int row = startRow;
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

            col += colDirection;
        }
    }
}
