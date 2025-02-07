package chess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class ChessGameHelper {

    public ChessGameHelper() {
    }

    public boolean isInCheck(ChessBoard board, ChessGame.TeamColor teamColor){
        ChessPosition kingPosition = null;
        var oppMoves = new ArrayList<ChessMove>();
        var oppEnds = new HashSet<ChessPosition>();
        for(int i = 1; i < 9; i++){
            for(int j = 1; j < 9; j++){
                ChessPiece tempPiece = board.board[i][j];
                if(tempPiece == null){
                    continue;
                }
                if(tempPiece.pieceColor == teamColor && tempPiece.type == ChessPiece.PieceType.KING){
                    kingPosition = new ChessPosition(i,j);
                }
                else if(tempPiece.pieceColor != teamColor){
                    oppMoves.addAll(tempPiece.pieceMoves(board,new ChessPosition(i,j)));
                }
            }
        }

        for(ChessMove moves : oppMoves){
            oppEnds.add(moves.endPosition);
        }

        return oppEnds.contains(kingPosition);
    }
}
