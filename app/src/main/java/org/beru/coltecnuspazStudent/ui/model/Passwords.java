package org.beru.coltecnuspazStudent.ui.model;

public class Passwords {
    public static String getPassword(String user){
        switch(user){
            case "edward":
                return "Aziluth22";
            case "fidel":
                return "_sociales_2023_";
            case "nubia":
                return "_ingles_2023_";
            case "jesus":
                return "_edfisica_2023";
            case "fernando":
                return "_matematicas_2023";
            default:
                    return "140206";
        }
    }
}
