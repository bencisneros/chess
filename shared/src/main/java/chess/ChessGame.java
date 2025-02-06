package chess;

import java.util.Collection;
import java.util.ArrayList;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    public ChessBoard board;
    public TeamColor team;



    public ChessGame() {
        board = new ChessBoard();
        board.resetBoard();
        team = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return team;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.team = team;
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
        int row = startPosition.row;
        int col = startPosition.col;
        ChessPiece piece = board.board[row][col];
        if(piece == null){
            return null;
        }
        ChessBoard boardCopy = new ChessBoard(board);
        TeamColor color = piece.getTeamColor();

        var opponentMoves = new ArrayList<ChessMove>();
        for(int i = 1; i < 9; i++){
            for(int j = 1; j < 9; j++){
                ChessPiece tempPiece = board.board[i][j];
                if(tempPiece == null){
                    continue;
                }
                if(tempPiece.pieceColor != color){
                    opponentMoves.addAll(tempPiece.pieceMoves(board,startPosition));
                }

            }
        }


        return piece.pieceMoves(board, startPosition);



    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = null;
        for(int i = 1; i < 9; i++){
            for(int j = 1; j < 9; j++){
                ChessPiece tempPiece = board.board[i][j];
                if(tempPiece == null){
                    continue;
                }
                if(tempPiece.pieceColor == teamColor && tempPiece.type == ChessPiece.PieceType.KING){
                    kingPosition = new ChessPosition(i,j);
                }
            }
        }


        for(int i = 1; i < 9; i++){
            for(int j = 1; j < 9; j++){
                ChessPiece tempPiece = board.board[i][j];
                if(tempPiece == null){
                    continue;
                }
                if(tempPiece.pieceColor != teamColor){
                    var opponentMoves = new ArrayList<ChessMove>();
                    ChessPosition tempPosition = new ChessPosition(i,j);
                    opponentMoves.addAll(tempPiece.pieceMoves(board,tempPosition));
                    var chessPositions = new ArrayList<ChessPosition>();
                    for(ChessMove moves : opponentMoves){
                        chessPositions.add(moves.endPosition);
                    }
                    if(chessPositions.contains(kingPosition)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if(!isInCheck(teamColor)){
            return false;
        }
        return false;


    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if(isInCheck(teamColor)){
            return false;
        }
        var allPieces = new ArrayList<ChessPiece>();
        ChessPosition kingPosition = null;
        ChessPiece king = null;
        for(int i = 1; i < 9; i++){
            for(int j = 1; j < 9; j++){
                ChessPiece tempPiece = board.board[i][j];
                if(tempPiece == null){
                    continue;
                }
                if(tempPiece.pieceColor == teamColor){
                    allPieces.add(tempPiece);
                    if(tempPiece.type == ChessPiece.PieceType.KING) {
                        kingPosition = new ChessPosition(i, j);
                        king = board.board[i][j];
                    }
                }
            }
        }

        if(allPieces.size() != 1){
            return false;
        }


        for(int i = 1; i < 9; i++){
            for(int j = 1; j < 9; j++){
                ChessPiece tempPiece = board.board[i][j];
                if(tempPiece == null){
                    continue;
                }
                if(tempPiece.pieceColor != teamColor){
                    var opponentMoves = new ArrayList<ChessMove>();
                    ChessPosition tempPosition = new ChessPosition(i,j);
                    opponentMoves.addAll(tempPiece.pieceMoves(board,tempPosition));
                    var chessPositions = new ArrayList<ChessPosition>();
                    for(ChessMove moves : opponentMoves){
                        chessPositions.add(moves.endPosition);
                    }
                }
            }
        }

        var kingMoves = king.pieceMoves(board, kingPosition);
        for(ChessMove moves : kingMoves){
            if(!kingMoves.contains(moves)){
                break;
            }
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
