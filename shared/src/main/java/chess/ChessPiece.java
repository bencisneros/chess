package chess;

import java.util.Collection;
import java.util.Objects;
import java.util.List;
import java.util.ArrayList;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */


public class ChessPiece {

    public ChessGame.TeamColor pieceColor;
    public ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return this.pieceColor.equals(that.pieceColor) && this.type.equals(that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
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

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        return kingMoves(board, myPosition);
    }


    public Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition){
        int row = myPosition.row;
        int col = myPosition.col;
        List<ChessMove> moves = new ArrayList<>();

        for(int i = -1; i < 2; i++){
            for(int j = -1; j < 2; j++){
                if(i == 0 && j == 0) {
                    continue;
                }
                if(row + i < 1 || row + i > 8) {
                    continue;
                }
                if(col + j < 1 || col + j > 8) {
                    continue;
                }
                if(board.board[row + i][col + j] == null){
                    ChessPosition endPosition = new ChessPosition(row + i, col + j);
                    moves.add(new ChessMove(myPosition, endPosition, null));
                    continue;
                }
                if(board.board[row + i][col + j].pieceColor == pieceColor) {
                    continue;
                }
                ChessPosition endPosition = new ChessPosition(row + i, col + j);
                moves.add(new ChessMove(myPosition, endPosition, null));
            }
        }

        return moves;
    }
}
