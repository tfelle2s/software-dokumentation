package org.arc42.dokumentation.model.dto.general;

import java.util.logging.Level;
import java.util.logging.Logger;

public class FE_LoginDTO {
    public String username;
    public String password;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public FE_LoginDTO(String username, String password){
        this.username = username;
        this.password = password;
        Logger.getLogger(FE_LoginDTO.class.getName()).log(Level.SEVERE, "username and password were set");
    }

    public String getUsername(){ return username; }
    public String getPassword() { return password; }
}
