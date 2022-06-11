package com.sep.billardapp.Helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.sep.billardapp.GameDataModel;
import com.sep.billardapp.PlayerDataModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String GAME_DATA_TABLE = "GAME_DATA_TABLE";
    public static final String COLUMN_ID = "COLUMN_ID";
    public static final String COLUMN_SPIELART = "COLUMN_SPIELART";
    public static final String COLUMN_ANZAHL_SPIELER = "COLUMN_ANZAHL_SPIELER";
    public static final String COLUMN_SPIELER_1 = "COLUMN_SPIELER_1";
    public static final String COLUMN_SPIELER_2 = "COLUMN_SPIELER_2";
    public static final String COLUMN_SPIELER_3 = "COLUMN_SPIELER_3";
    public static final String COLUMN_SPIELER_4 = "COLUMN_SPIELER_4";
    public static final String COLUMN_GEWINNER = "COLUMN_GEWINNER";
    public static final String COLUMN_SPIELDAUER = "COLUMN_SPIELDAUER";

    public static final String PLAYER_DATA = "PLAYER_DATA";
    public static final String COLUMN_ID_PLAYER_TABLE = "COLUMN_ID_PLAYER_TABLE";
    public static final String COLUMN_VORNAME = "COLUMN_VORNAME";
    public static final String COLUMN_NACHNAME = "COLUMN_NACHNAME";
    public static final String COLUMN_NICKNAME = "COLUMN_NICKNAME";
    public static final String COLUMN_GEBURTSDATUM = "COLUMN_GEBURTSDATUM";


    public DatabaseHelper(@Nullable Context context) {
        super(context, "gameData.db", null, 1);
    }

    // called the first time a database is accessed
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + GAME_DATA_TABLE + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_SPIELART + " TEXT, " +
                COLUMN_ANZAHL_SPIELER + " INTEGER, " +
                COLUMN_SPIELER_1 + " TEXT, " +
                COLUMN_SPIELER_2 + " TEXT, " +
                COLUMN_SPIELER_3 + " TEXT, " +
                COLUMN_SPIELER_4 + " TEXT, " +
                COLUMN_GEWINNER + " TEXT, " +
                COLUMN_SPIELDAUER + " TEXT)";

        db.execSQL(createTableStatement);

        String createTablePlayerStatement = "CREATE TABLE " + PLAYER_DATA + "(" + COLUMN_ID_PLAYER_TABLE + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_VORNAME + " TEXT, " +
                COLUMN_NACHNAME + " TEXT, " +
                COLUMN_NICKNAME + " TEXT, " +
                COLUMN_GEBURTSDATUM + " TEXT)";

        db.execSQL(createTablePlayerStatement);
    }

    // called if the database version number changes
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addPlayer(PlayerDataModel playerDataModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_VORNAME, playerDataModel.getVorname());
        cv.put(COLUMN_NACHNAME, playerDataModel.getNachname());
        cv.put(COLUMN_NICKNAME, playerDataModel.getNickname());
        cv.put(COLUMN_GEBURTSDATUM, playerDataModel.getGeburtsdatum());

        long insert = db.insert(PLAYER_DATA, null, cv);

        if(insert == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean addOne(GameDataModel gameDataModel) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_SPIELART, gameDataModel.getGamemode());
        cv.put(COLUMN_ANZAHL_SPIELER, gameDataModel.getNumberOfPlayers());
        cv.put(COLUMN_SPIELER_1, gameDataModel.getPlayer1Name());
        cv.put(COLUMN_SPIELER_2, gameDataModel.getPlayer2name());
        cv.put(COLUMN_SPIELER_3, gameDataModel.getPlayer3name());
        cv.put(COLUMN_SPIELER_4, gameDataModel.getPlayer4name());
        cv.put(COLUMN_GEWINNER, gameDataModel.getWinner());
        cv.put(COLUMN_SPIELDAUER, gameDataModel.getGametime());

        long insert = db.insert(GAME_DATA_TABLE, null, cv);

        if(insert == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean deleteOne(GameDataModel gameDataModel) {
        // find gameDataModel in database, if it is found delete it and return true
        // if it is not found return false

        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " + GAME_DATA_TABLE + " WHERE " + COLUMN_ID + " = " + gameDataModel.getId();

        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()) {
            return true;
        } else {
            return false;
        }
    }

    public int fetchWinsCount(EditText et_name_stats) {
        // fetch and count the wins the user with the given username has

        SQLiteDatabase db = this.getReadableDatabase();
        int countWins = (int) DatabaseUtils.queryNumEntries(db, GAME_DATA_TABLE, COLUMN_GEWINNER + " = '" + et_name_stats.getText().toString() + "'");
        db.close();
        return countWins;
    }

    public int fetchLossesCount(EditText et_name_stats) {
        // fetch and count the losses the user with the given username has

        SQLiteDatabase db = this.getReadableDatabase();
        int countLosses = (int) DatabaseUtils.queryNumEntries(db, GAME_DATA_TABLE, COLUMN_GEWINNER + " != '" + et_name_stats.getText().toString() + "' AND (" + COLUMN_SPIELER_1 + " = '" + et_name_stats.getText().toString() + "' OR " + COLUMN_SPIELER_2 + " = '" + et_name_stats.getText().toString() + "' OR " + COLUMN_SPIELER_3 + " = '" + et_name_stats.getText().toString() + "' OR " + COLUMN_SPIELER_4 + " = '" + et_name_stats.getText().toString() + "')");
        db.close();
        return countLosses;
    }

    public ArrayList fetchWinnerList() {
        // fetch a list of all the users who have won at least one game and return them as an array list

        String queryString = "SELECT DISTINCT " + COLUMN_GEWINNER + " FROM " + GAME_DATA_TABLE + " WHERE " + COLUMN_GEWINNER + " NOT LIKE '/' AND " + COLUMN_GEWINNER + " NOT LIKE ''";
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> winnerList = new ArrayList<>();

        Cursor cursor = db.rawQuery(queryString, null);

        cursor.moveToFirst();

        while(!cursor.isAfterLast()) {
            winnerList.add(cursor.getString(0));
            cursor.moveToNext();
        }

        cursor.close();
        return winnerList;
    }

    public int fetchWinsPerName(int name) {
        // count the wins of the wanted user

        SQLiteDatabase db = this.getReadableDatabase();
        int winsPerName = (int) DatabaseUtils.queryNumEntries(db, GAME_DATA_TABLE, COLUMN_GEWINNER + " = '" + fetchWinnerList().get(name).toString() + "'");

        return winsPerName;
    }

    public String calculateAverageTime(EditText et_name_average_time) {
        // return the average game duration of all the games a player has been part of

        String queryString = "SELECT AVG(" + COLUMN_SPIELDAUER + ") FROM " + GAME_DATA_TABLE + " WHERE " + COLUMN_SPIELER_1 + " = '" + et_name_average_time.getText().toString() + "' OR " + COLUMN_SPIELER_2 + " = '" + et_name_average_time.getText().toString() + "' OR " + COLUMN_SPIELER_3 + " = '" + et_name_average_time.getText().toString() + "' OR " + COLUMN_SPIELER_4 + " = '" + et_name_average_time.getText().toString() + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        cursor.moveToFirst();
        float averageTime = cursor.getFloat(0);

        return String.valueOf(averageTime);
    }

    public List<GameDataModel> getEveryGame() {
        List<GameDataModel> returnList = new ArrayList<>();

        String queryString = "SELECT * FROM " + GAME_DATA_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()) {
            // loop through  the cursor and create new gamedata objects, put them into the return list
            do {
                int gameId = cursor.getInt(0);
                String gamemode = cursor.getString(1);
                int numberOfPlayers = cursor.getInt(2);
                String player1name = cursor.getString(3);
                String player2name = cursor.getString(4);
                String player3name = cursor.getString(5);
                String player4name = cursor.getString(6);
                String winner = cursor.getString(7);
                String gametime = cursor.getString(8);

                GameDataModel newGame = new GameDataModel(gameId, gamemode, numberOfPlayers, player1name, player2name, player3name, player4name, winner, gametime);
                returnList.add(newGame);
            } while (cursor.moveToNext());

        } else {
            // failure, do not add anything to the list
        }

        cursor.close();
        db.close();
        return returnList;
    }
}
