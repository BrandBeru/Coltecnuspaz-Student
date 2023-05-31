package org.beru.coltecnuspazStudent.ui.model;

import java.util.List;

public class Datas {
    private static List<String> paths;

    public static int path_id;
    public static String getLastPath(){
        return getPaths().get(getPaths().size()-1);
    }
    public static void addPath(String newPath){
        getPaths().add(getPaths().get(getPaths().size()-1)+"/"+newPath);
    }
    public static List<String> getPaths() {
        return paths;
    }
    public static void setPaths(List<String> paths) {
        Datas.paths = paths;
    }
}
