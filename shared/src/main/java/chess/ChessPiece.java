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
        Collection<ChessMove> moves = new ArrayList<>();
        if(type == PieceType.KING){
            moves = kingMoves(board, myPosition);
        }
        else if(type == PieceType.PAWN){
            moves = pawnMoves(board, myPosition);
        }
        else if(type == PieceType.BISHOP){
            moves = bishopMoves(board, myPosition);
        }
        else if(type == PieceType.ROOK){
            moves = rookMoves(board, myPosition);
        }
        else if(type == PieceType.QUEEN){
            Collection<ChessMove> rookMoves = new ArrayList<>();
            Collection<ChessMove> bishopMoves = new ArrayList<>();
            rookMoves = rookMoves(board, myPosition);
            bishopMoves = bishopMoves(board, myPosition);
            moves.addAll(rookMoves);
            moves.addAll(bishopMoves);
        }
        else if(type == PieceType.KNIGHT){
            moves = knightMoves(board, myPosition);
        }
        return moves;
    }


    public Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.row;
        int col = myPosition.col;
        List<ChessMove> moves = new ArrayList<>();

        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }
                if (row + i < 1 || row + i > 8) {
                    continue;
                }
                if (col + j < 1 || col + j > 8) {
                    continue;
                }
                if (board.board[row + i][col + j] == null) {
                    ChessPosition endPosition = new ChessPosition(row + i, col + j);
                    moves.add(new ChessMove(myPosition, endPosition, null));
                    continue;
                }
                if (board.board[row + i][col + j].pieceColor == pieceColor) {
                    continue;
                }
                ChessPosition endPosition = new ChessPosition(row + i, col + j);
                moves.add(new ChessMove(myPosition, endPosition, null));
            }
        }

        return moves;
    }

    public Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.row;
        int col = myPosition.col;
        List<ChessMove> moves = new ArrayList<>();

        if (this.pieceColor == ChessGame.TeamColor.WHITE) {
            if (board.board[row + 1][col] == null && row + 1 < 9) {
                if(row + 1 == 8){
                    ChessPosition endPosition = new ChessPosition(row + 1, col);
                    moves.add(new ChessMove(myPosition, endPosition, PieceType.QUEEN));
                    moves.add(new ChessMove(myPosition, endPosition, PieceType.ROOK));
                    moves.add(new ChessMove(myPosition, endPosition, PieceType.BISHOP));
                    moves.add(new ChessMove(myPosition, endPosition, PieceType.KNIGHT));
                }
                else {
                    ChessPosition endPosition = new ChessPosition(row + 1, col);
                    moves.add(new ChessMove(myPosition, endPosition, null));
                }
            }
            if(board.board[row + 1][col + 1] != null){
                if (board.board[row + 1][col + 1].pieceColor == ChessGame.TeamColor.BLACK) {
                    if(row == 7) {
                        ChessPosition endPosition = new ChessPosition(row + 1, col + 1);
                        moves.add(new ChessMove(myPosition, endPosition, PieceType.QUEEN));
                        moves.add(new ChessMove(myPosition, endPosition, PieceType.ROOK));
                        moves.add(new ChessMove(myPosition, endPosition, PieceType.BISHOP));
                        moves.add(new ChessMove(myPosition, endPosition, PieceType.KNIGHT));
                    }
                    else {
                        ChessPosition endPosition = new ChessPosition(row + 1, col + 1);
                        moves.add(new ChessMove(myPosition, endPosition, null));
                    }
                }
            }
            if (board.board[row + 1][col - 1] != null){
                if (board.board[row + 1][col - 1].pieceColor == ChessGame.TeamColor.BLACK) {
                    if(row == 7) {
                        ChessPosition endPosition = new ChessPosition(row + 1, col - 1);
                        moves.add(new ChessMove(myPosition, endPosition, PieceType.QUEEN));
                        moves.add(new ChessMove(myPosition, endPosition, PieceType.ROOK));
                        moves.add(new ChessMove(myPosition, endPosition, PieceType.BISHOP));
                        moves.add(new ChessMove(myPosition, endPosition, PieceType.KNIGHT));
                    }
                    else {
                        ChessPosition endPosition = new ChessPosition(row + 1, col - 1);
                        moves.add(new ChessMove(myPosition, endPosition, null));
                    }
                }

            }
            if(row == 2 && board.board[row + 1][col] == null && board.board[row + 2][col] == null){
                ChessPosition endPosition = new ChessPosition(row + 2, col);
                moves.add(new ChessMove(myPosition, endPosition, null));
            }
        }


        if (this.pieceColor == ChessGame.TeamColor.BLACK) {
            if (board.board[row - 1][col] == null && row - 1 > 0) {
                if(row - 1 == 1){
                    ChessPosition endPosition = new ChessPosition(row - 1, col);
                    moves.add(new ChessMove(myPosition, endPosition, PieceType.QUEEN));
                    moves.add(new ChessMove(myPosition, endPosition, PieceType.ROOK));
                    moves.add(new ChessMove(myPosition, endPosition, PieceType.BISHOP));
                    moves.add(new ChessMove(myPosition, endPosition, PieceType.KNIGHT));
                }
                else {
                    ChessPosition endPosition = new ChessPosition(row - 1, col);
                    moves.add(new ChessMove(myPosition, endPosition, null));
                }
            }
            if(board.board[row - 1][col - 1] != null) {
                if (board.board[row - 1][col - 1].pieceColor == ChessGame.TeamColor.WHITE) {
                    if(row == 2){
                        ChessPosition endPosition = new ChessPosition(row - 1, col - 1);
                        moves.add(new ChessMove(myPosition, endPosition, PieceType.QUEEN));
                        moves.add(new ChessMove(myPosition, endPosition, PieceType.ROOK));
                        moves.add(new ChessMove(myPosition, endPosition, PieceType.BISHOP));
                        moves.add(new ChessMove(myPosition, endPosition, PieceType.KNIGHT));
                    }
                    else {
                        ChessPosition endPosition = new ChessPosition(row - 1, col - 1);
                        moves.add(new ChessMove(myPosition, endPosition, null));
                    }
                }
            }
            if(board.board[row - 1][col + 1] != null) {
                if (board.board[row - 1][col + 1].pieceColor == ChessGame.TeamColor.WHITE) {
                    if(row == 2){
                        ChessPosition endPosition = new ChessPosition(row - 1, col + 1);
                        moves.add(new ChessMove(myPosition, endPosition, PieceType.QUEEN));
                        moves.add(new ChessMove(myPosition, endPosition, PieceType.ROOK));
                        moves.add(new ChessMove(myPosition, endPosition, PieceType.BISHOP));
                        moves.add(new ChessMove(myPosition, endPosition, PieceType.KNIGHT));
                    }
                    else {
                        ChessPosition endPosition = new ChessPosition(row - 1, col + 1);
                        moves.add(new ChessMove(myPosition, endPosition, null));
                    }
                }
            }
            if(row == 7 && board.board[row - 1][col] == null && board.board[row - 2][col] == null){
                ChessPosition endPosition = new ChessPosition(row - 2, col);
                moves.add(new ChessMove(myPosition, endPosition, null));
                }
        }
        return moves;
    }

    public Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition){
        int row = myPosition.row;
        int col = myPosition.col;
        List<ChessMove> moves = new ArrayList<>();

        for(int i = 1; i + row < 9 && col + i < 9; i ++){
            if(board.board[row + i][col + i] == null){
                ChessPosition endPosition = new ChessPosition(row + i, col + i);
                moves.add(new ChessMove(myPosition, endPosition, null));
            }
            else{
                if(board.board[row + i][col + i].pieceColor != this.pieceColor) {
                    ChessPosition endPosition = new ChessPosition(row + i, col + i);
                    moves.add(new ChessMove(myPosition, endPosition, null));
                }
                break;
            }
        }

        for(int i = 1; row - i > 0 && col - i > 0; i ++){
            if(board.board[row - i][col - i] == null){
                ChessPosition endPosition = new ChessPosition(row - i, col - i);
                moves.add(new ChessMove(myPosition, endPosition, null));
            }
            else{
                if(board.board[row - i][col - i].pieceColor != this.pieceColor) {
                    ChessPosition endPosition = new ChessPosition(row - i, col - i);
                    moves.add(new ChessMove(myPosition, endPosition, null));
                }
                break;
            }
        }

        for(int i = 1; i + row < 9 && col - i > 0; i ++){
            if(board.board[row + i][col - i] == null){
                ChessPosition endPosition = new ChessPosition(row + i, col - i);
                moves.add(new ChessMove(myPosition, endPosition, null));
            }
            else{
                if(board.board[row + i][col - i].pieceColor != this.pieceColor) {
                    ChessPosition endPosition = new ChessPosition(row + i, col - i);
                    moves.add(new ChessMove(myPosition, endPosition, null));
                }
                break;
            }
        }

        for(int i = 1; row - i > 0 && col + i < 9; i ++){
            if(board.board[row - i][col + i] == null){
                ChessPosition endPosition = new ChessPosition(row - i, col + i);
                moves.add(new ChessMove(myPosition, endPosition, null));
            }
            else{
                if(board.board[row - i][col + i].pieceColor != this.pieceColor) {
                    ChessPosition endPosition = new ChessPosition(row - i, col + i);
                    moves.add(new ChessMove(myPosition, endPosition, null));
                }
                break;
            }
        }

        return moves;
    }

    public Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition){
        int row = myPosition.row;
        int col = myPosition.col;
        Collection<ChessMove> moves = new ArrayList<>();

        for(int i = 1; col + i < 9; i ++){
            if(board.board[row][col + i] == null){
                ChessPosition endPosition = new ChessPosition(row, col + i);
                moves.add(new ChessMove(myPosition, endPosition, null));
            }
            else{
                if(board.board[row][col + i].pieceColor != this.pieceColor){
                    ChessPosition endPosition = new ChessPosition(row, col + i);
                    moves.add(new ChessMove(myPosition, endPosition, null));
                }
                break;
            }
        }

        for(int i = 1; col - i > 0; i ++){
            if(board.board[row][col - i] == null){
                ChessPosition endPosition = new ChessPosition(row, col - i);
                moves.add(new ChessMove(myPosition, endPosition, null));
            }
            else{
                if(board.board[row][col - i].pieceColor != this.pieceColor){
                    ChessPosition endPosition = new ChessPosition(row, col - i);
                    moves.add(new ChessMove(myPosition, endPosition, null));
                }
                break;
            }
        }


        for(int i = 1; row + i < 9; i ++){
            if(board.board[row + i][col] == null){
                ChessPosition endPosition = new ChessPosition(row + i, col);
                moves.add(new ChessMove(myPosition, endPosition, null));
            }
            else{
                if(board.board[row + i][col].pieceColor != this.pieceColor){
                    ChessPosition endPosition = new ChessPosition(row + i, col);
                    moves.add(new ChessMove(myPosition, endPosition, null));
                }
                break;
            }
        }

        for(int i = 1; row - i > 0; i ++){
            if(board.board[row - i][col] == null){
                ChessPosition endPosition = new ChessPosition(row - i, col);
                moves.add(new ChessMove(myPosition, endPosition, null));
            }
            else{
                if(board.board[row - i][col].pieceColor != this.pieceColor){
                    ChessPosition endPosition = new ChessPosition(row - i, col);
                    moves.add(new ChessMove(myPosition, endPosition, null));
                }
                break;
            }
        }

        return moves;
    }

    public Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.row;
        int col = myPosition.col;
        Collection<ChessMove> moves = new ArrayList<>();

        if(row + 1 < 9 && col + 2 < 9){
            if(board.board[row + 1][col + 2] == null){
                ChessPosition endPosition = new ChessPosition(row + 1, col + 2);
                moves.add(new ChessMove(myPosition, endPosition, null));
            }
            else if(board.board[row + 1][col + 2].pieceColor != pieceColor){
                ChessPosition endPosition = new ChessPosition(row + 1, col + 2);
                moves.add(new ChessMove(myPosition, endPosition, null));
            }
        }

        if(row - 1 > 0 && col + 2 < 9){
            if(board.board[row - 1][col + 2] == null){
                ChessPosition endPosition = new ChessPosition(row - 1, col + 2);
                moves.add(new ChessMove(myPosition, endPosition, null));
            }
            else if(board.board[row - 1][col + 2].pieceColor != pieceColor){
                ChessPosition endPosition = new ChessPosition(row - 1, col + 2);
                moves.add(new ChessMove(myPosition, endPosition, null));
            }
        }

        if(row - 1 > 0 && col - 2 > 0){
            if(board.board[row - 1][col - 2] == null){
                ChessPosition endPosition = new ChessPosition(row - 1, col - 2);
                moves.add(new ChessMove(myPosition, endPosition, null));
            }
            else if(board.board[row - 1][col - 2].pieceColor != pieceColor){
                ChessPosition endPosition = new ChessPosition(row - 1, col - 2);
                moves.add(new ChessMove(myPosition, endPosition, null));
            }
        }

        if(row + 1 < 9 && col - 2 > 0){
            if(board.board[row + 1][col - 2] == null){
                ChessPosition endPosition = new ChessPosition(row + 1, col - 2);
                moves.add(new ChessMove(myPosition, endPosition, null));
            }
            else if(board.board[row + 1][col - 2].pieceColor != pieceColor){
                ChessPosition endPosition = new ChessPosition(row + 1, col - 2);
                moves.add(new ChessMove(myPosition, endPosition, null));
            }
        }
///////////////////////////////////////////////////////////////////////////////////
        if(row + 2 < 9 && col + 1 < 9){
            if(board.board[row + 2][col + 1] == null){
                ChessPosition endPosition = new ChessPosition(row + 2, col + 1);
                moves.add(new ChessMove(myPosition, endPosition, null));
            }
            else if(board.board[row + 2][col + 1].pieceColor != pieceColor){
                ChessPosition endPosition = new ChessPosition(row + 2, col + 1);
                moves.add(new ChessMove(myPosition, endPosition, null));
            }
        }

        if(row - 2 > 0 && col + 1 < 9){
            if(board.board[row - 2][col + 1] == null){
                ChessPosition endPosition = new ChessPosition(row - 2, col + 1);
                moves.add(new ChessMove(myPosition, endPosition, null));
            }
            else if(board.board[row - 2][col + 1].pieceColor != pieceColor){
                ChessPosition endPosition = new ChessPosition(row - 2, col + 1);
                moves.add(new ChessMove(myPosition, endPosition, null));
            }
        }

        if(row - 2 > 0 && col - 1 > 0){
            if(board.board[row - 2][col - 1] == null){
                ChessPosition endPosition = new ChessPosition(row - 2, col - 1);
                moves.add(new ChessMove(myPosition, endPosition, null));
            }
            else if(board.board[row - 2][col - 1].pieceColor != pieceColor){
                ChessPosition endPosition = new ChessPosition(row - 2, col - 1);
                moves.add(new ChessMove(myPosition, endPosition, null));
            }
        }
        if(row + 2 < 9 && col - 1 > 0){
            if(board.board[row + 2][col - 1] == null){
                ChessPosition endPosition = new ChessPosition(row + 2, col - 1);
                moves.add(new ChessMove(myPosition, endPosition, null));
            }
            else if(board.board[row + 2][col - 1].pieceColor != pieceColor){
                ChessPosition endPosition = new ChessPosition(row + 2, col - 1);
                moves.add(new ChessMove(myPosition, endPosition, null));
            }
        }

        return moves;
    }
}
