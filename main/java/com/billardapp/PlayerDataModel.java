package com.sep.billardapp;

public class PlayerDataModel {

    private int ID;
    private String vorname;
    private String nachname;
    private String nickname;
    private String geburtsdatum;

    public PlayerDataModel(int ID, String vorname, String nachname, String nickname, String geburtsdatum) {
        this.ID = ID;
        this.vorname = vorname;
        this.nachname = nachname;
        this.nickname = nickname;
        this.geburtsdatum = geburtsdatum;
    }

    @Override
    public String toString() {
        return " User " +
                "ID = " + ID +
                "\n firstname = " + vorname +
                "\n lastname = " + nachname +
                "\n nickname = " + nickname +
                "\n birthday = " + geburtsdatum;
    }

    public int getId() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getGeburtsdatum() {
        return geburtsdatum;
    }

    public void setGeburtsdatum(String geburtsdatum) {
        this.geburtsdatum = geburtsdatum;
    }








}
