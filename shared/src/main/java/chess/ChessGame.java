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
        ChessPiece tempPiece = board.board[row][col];

        var possibleMoves = new ArrayList<ChessMove>();
        possibleMoves.addAll(tempPiece.pieceMoves(board, startPosition));
        return possibleMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {

        int startRow = move.startPosition.row;
        int startCol = move.startPosition.col;
        int endCol = move.endPosition.col;
        int endRow = move.endPosition.row;

        ChessPiece tempPiece = board.board[startRow][startCol];
        if(tempPiece == null){
            throw new InvalidMoveException("no piece at start point");
        }
        if(tempPiece.pieceColor != team){
            throw new InvalidMoveException("not your turn");
        }

        var possibleMoves = validMoves(move.startPosition);
        if(possibleMoves.contains(move)){
            board.board[startRow][startCol] = null;
            if(tempPiece.type == ChessPiece.PieceType.PAWN && (endRow == 1 || endRow == 8)){
                ChessPiece promotion = new ChessPiece(team, move.promotionPiece);
                board.board[endRow][endCol] = promotion;
            }
            else {
                board.board[endRow][endCol] = tempPiece;
            }
        }
        else{
            throw new InvalidMoveException("invalid move");
        }


        if(team == TeamColor.BLACK){
            setTeamTurn(TeamColor.WHITE);
        }
        else{
            setTeamTurn(TeamColor.BLACK);
        }
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

        var kingEndPositions = new ArrayList<ChessPosition>();
        var opponentEndPositions = new ArrayList<ChessPosition>();
        ChessPosition kingPosition = null;
        var opponentMoves = new ArrayList<ChessMove>();
        for(int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPiece tempPiece = board.board[i][j];
                if (tempPiece == null) {
                    continue;
                }
                if (tempPiece.pieceColor == teamColor && tempPiece.type == ChessPiece.PieceType.KING) {
                    kingPosition = new ChessPosition(i, j);
                }
                else if(tempPiece.pieceColor != teamColor){
                    if(tempPiece.type != ChessPiece.PieceType.PAWN) {
                        opponentMoves.addAll(tempPiece.pieceMoves(board, new ChessPosition(i, j)));
                    }
                    else{
                        if(tempPiece.pieceColor == TeamColor.WHITE){
                            opponentEndPositions.add(new ChessPosition(i + 1, j + 1));
                            opponentEndPositions.add(new ChessPosition(i + 1, j - 1));
                        }
                        else{
                            opponentEndPositions.add(new ChessPosition(i - 1, j + 1));
                            opponentEndPositions.add(new ChessPosition(i - 1, j - 1));
                        }
                    }
                }
            }
        }
        var kingMoves = new ArrayList<ChessMove>();
        kingMoves.addAll(board.board[kingPosition.row][kingPosition.col].pieceMoves(board, kingPosition));

        for(ChessMove moves : kingMoves){
            kingEndPositions.add(moves.endPosition);
        }
        for(ChessMove moves : opponentMoves){
            opponentEndPositions.add(moves.endPosition);
        }

        for(ChessPosition position : kingEndPositions){
            if(!opponentEndPositions.contains(position)){
                return false;
            }
        }
        return true;


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
