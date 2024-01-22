package chess.PieceMovesCalculator;

import chess.*;

import java.util.Collection;

public class PawnMovesCalculator {
    public static void pieceMoves(ChessBoard board, ChessPosition position, Collection<ChessMove> moves) {
        ChessPiece pawn = board.getPiece(position);
        int row = position.getRow();
        int col = position.getColumn();
        int direction = 0;

        //get direction or teamcolor
        if (pawn.getTeamColor() == ChessGame.TeamColor.WHITE) {
            direction = 1;
        } else {
            direction = -1;
        }

        if ((direction == -1 && row == 7) || (direction == 1 && row == 2)) {
            addPawnMoveInitialInDirection(board, position, row, col, direction, moves);
        }

        addPawnMoveOnceInDirection(board, position, row + direction, col, direction, moves);

        if (direction == 1) {
            addPawnAttack(board, position, row, col, 1, 1, direction, moves);
            addPawnAttack(board, position, row, col, 1, -1, direction, moves);
        }
        else if (direction == -1) {
            addPawnAttack(board, position, row, col, -1, 1, direction, moves);
            addPawnAttack(board, position, row, col, -1, -1, direction, moves);
        }
    }

    private static void addPawnMoveOnceInDirection(ChessBoard board, ChessPosition position, int row, int col, int direction, Collection<ChessMove> moves) {
        if (board.isInBoard(row, col)) {
            ChessPosition newPosition = new ChessPosition(row, col);
            if (board.isEmpty(newPosition)) {
                if ((direction == -1 && row == 1) || (direction == 1 && row == 8)) {
                    moves.add(new ChessMove(position, newPosition, ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(position, newPosition, ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(position, newPosition, ChessPiece.PieceType.KNIGHT));
                    moves.add(new ChessMove(position, newPosition, ChessPiece.PieceType.BISHOP));
                }
                else {
                    moves.add(new ChessMove(position, newPosition, null));
                }
            }
        }
    }

    private static void addPawnMoveInitialInDirection(ChessBoard board, ChessPosition position, int row, int col, int direction, Collection<ChessMove> moves) {
        int newRow = row + (1 * direction);

        if (board.isInBoard(newRow, col)) {
            ChessPosition newPosition = new ChessPosition(newRow, col);
            if (board.isEmpty(newPosition)) {
                moves.add(new ChessMove(position, newPosition, null));
            }
        }

        newRow = row + (2 * direction);

        if (board.isInBoard(newRow, col)) {
            ChessPosition onePosition = new ChessPosition(row + 1 * direction, col);
            ChessPosition newPosition = new ChessPosition(newRow, col);
            if (board.isEmpty(newPosition) && board.isEmpty(onePosition)) {
                moves.add(new ChessMove(position, newPosition, null));
            }
        }
    }

    private static void addPawnAttack(ChessBoard board, ChessPosition position, int startRow, int startCol, int rowDirection, int colDirection, int direction, Collection<ChessMove> moves) {
        int row = startRow + rowDirection;
        int col = startCol + colDirection;

        if (board.isInBoard(row, col)) {
            ChessPosition newPosition = new ChessPosition(row, col);
            if (board.isOpponent(newPosition, board.getPiece(position))) {
                if ((direction == -1 && row == 1) || (direction == 1 && row == 8)) {
                    moves.add(new ChessMove(position, newPosition, ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(position, newPosition, ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(position, newPosition, ChessPiece.PieceType.KNIGHT));
                    moves.add(new ChessMove(position, newPosition, ChessPiece.PieceType.BISHOP));
                }
                else {
                    moves.add(new ChessMove(position, newPosition, null));
                }
            }
        }
    }
}
