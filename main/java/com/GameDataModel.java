package com.sep.billardapp;

public class GameDataModel {

    private int ID;
    private String gamemode;
    private int numberOfPlayers;
    private String player1name;
    private String player2name;
    private String player3name;
    private String player4name;
    private String winner;
    private String gametime;

    public GameDataModel(int ID, String gamemode, int numberOfPlayers, String player1name, String player2name, String player3name, String player4name, String winner, String gametime) {
        this.ID = ID;
        this.gamemode = gamemode;
        this.numberOfPlayers = numberOfPlayers;
        this.player1name = player1name;
        this.player2name = player2name;
        this.player3name = player3name;
        this.player4name = player4name;
        this.winner = winner;
        this.gametime = gametime;
    }

    @Override
    public String toString() {
        return " Game " +
                "ID = " + ID +
                "\n Gamemode = " + gamemode +
                "\n Numer of players = " + numberOfPlayers +
                "\n Player 1 name = " + player1name +
                "\n Player 2 name = " + player2name +
                "\n Player 3 name = " + player3name +
                "\n Player 4 name = " + player4name +
                "\n Winner = " + winner +
                "\n Game time = " + gametime + " minutes";
    }

    public int getId() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getGamemode() {
        return gamemode;
    }

    public void setGamemode(String gamemode) {
        this.gamemode = gamemode;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public String getPlayer1Name() {
        return player1name;
    }

    public void setPlayer1Name(String player1Name) {
        this.player1name = player1Name;
    }

    public String getPlayer2name() {
        return player2name;
    }

    public void setPlayer2name(String player2name) {
        this.player2name = player2name;
    }

    public String getPlayer3name() {
        return player3name;
    }

    public void setPlayer3name(String player3name) {
        this.player3name = player3name;
    }

    public String getPlayer4name() {
        return player4name;
    }

    public void setPlayer4name(String player4name) {
        this.player4name = player4name;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public String getGametime() {
        return gametime;
    }

    public void setGametime(String gametime) {
        this.gametime = gametime;
    }
}
