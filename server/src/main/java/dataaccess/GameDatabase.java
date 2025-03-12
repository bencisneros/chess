package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Objects;

public class GameDatabase implements GameDAO{

    private static int gameId = 0;

    private final DatabaseManager databaseManager;

    public GameDatabase() throws Exception{
        databaseManager = new DatabaseManager();
        databaseManager.configureDatabase();
    }

    public void clearGameData() throws Exception {
        String statement = "TRUNCATE gameData";
        databaseManager.executeUpdate(statement);
    }

    public GameData createGameData(String gameName) throws Exception {
        gameId++;
        var statement = "INSERT INTO gameData (gameID, whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?, ?)";
        String whiteUsername = "";
        String blackUsername = "";
        ChessGame chessGame = new ChessGame();
        String gsonGame = new Gson().toJson(chessGame);
        databaseManager.executeUpdate(statement, gameId, whiteUsername, blackUsername, gameName, gsonGame);
        return new GameData(gameId, whiteUsername, blackUsername, gameName, chessGame);
    }

    public GameData getGame(int gameID) throws Exception {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM gameData WHERE gameID=?";
            try (var c = conn.prepareStatement(statement)) {
                c.setInt(1, gameID);
                try (var rs = c.executeQuery()) {
                    if (rs.next()) {
                        return readGame(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new Exception(String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    private GameData readGame(ResultSet rs) throws Exception {
        Gson gson = new Gson();
        int gameID = rs.getInt("gameID");
        String whiteUsername = rs.getString("whiteUsername");
        String blackUsername = rs.getString("blackUsername");
        String gameName = rs.getString("gameName");
        ChessGame game = gson.fromJson(rs.getString("game"), ChessGame.class);
        return new GameData(gameID, whiteUsername, blackUsername, gameName, game);
    }

    public void updateGame(String playerColor, String username, GameData gameData) throws Exception {
        if(Objects.equals(playerColor, "WHITE")) {
            if(!Objects.equals(gameData.whiteUsername(), "")){
                throw new AlreadyTaken("403 Error: already taken");
            }
            String statement = "UPDATE gameData SET whiteUsername =? WHERE gameID =?";
            databaseManager.executeUpdate(statement, username, gameData.gameID());
        }
        else if(Objects.equals(playerColor, "BLACK")){
            if(!Objects.equals(gameData.blackUsername(), "")){
                throw new AlreadyTaken("403 Error: already taken");
            }
            String statement = "UPDATE gameData SET blackUsername =? WHERE gameID =?";
            databaseManager.executeUpdate(statement, username, gameData.gameID());
        }
        else{
            throw new DataAccessException("400 Error: bad request");
        }
    }

    public HashMap<Integer, GameData> getGameMap() {
        return null;
    }
}
