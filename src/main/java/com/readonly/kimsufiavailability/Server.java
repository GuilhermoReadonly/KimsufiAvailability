package com.readonly.kimsufiavailability;


import java.io.Serializable;

public class Server implements Serializable {

    private String name;
    private String codeName;
    private int indexJson;

    public Server() {
    }


    public Server(String name, String codeName, int indexJson) {
        this.name = name;
        this.codeName = codeName;
        this.indexJson = indexJson;
    }

    public int getIndexJson() {
        return indexJson;
    }

    public void setIndexJson(int indexJson) {
        this.indexJson = indexJson;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }





}
