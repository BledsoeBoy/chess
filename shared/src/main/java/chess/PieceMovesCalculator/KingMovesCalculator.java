package chess.PieceMovesCalculator;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;

public class KingMovesCalculator {
    public static void pieceMoves(ChessBoard board, ChessPosition position, Collection<ChessMove> moves) {
        int row = position.getRow();
        int col = position.getColumn();

        // Loop through all possible directions (horizontally, vertically, and diagonally)
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {

                int newRow = row + i;
                int newCol = col + j;

                // Check if the new position is within the board
                if (board.isInBoard(newRow, newCol)) {
                    ChessPosition newPosition = new ChessPosition(newRow, newCol);

                    // Check if the new position is empty or has an opponent's piece
                    if ((board.isEmpty(newPosition) || board.isOpponent(newPosition, board.getPiece(position))) && !board.isOwn(newPosition, board.getPiece(position))) {
                        moves.add(new ChessMove(position, newPosition, null));
                    }
                }
            }
        }
    }

}
