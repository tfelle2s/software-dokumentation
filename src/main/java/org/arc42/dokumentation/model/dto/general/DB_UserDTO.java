package org.arc42.dokumentation.model.dto.general;

public class DB_UserDTO {
    private static DB_UserDTO instance;
    private DB_UserDTO(){}
    public static DB_UserDTO getInstance(){
        if (instance==null){
            instance = new DB_UserDTO();
        }
        return instance;
    }

    public String username = "";

    public String getUsername(){ return username; }
    public void setUsername(String username){
        this.username = username;
    }
}
