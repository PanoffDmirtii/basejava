package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for sections ACHIEVEMENT("Достижения"), QUALIFICATIONS("Квалификация"),
 */
public class TextList extends Content {
    private List<String> listOfContent = new ArrayList<>();

    public void addToList(String content){
        listOfContent.add(content);
    }

    @Override
    public String toString() {
        return listOfContent.toString() + "\n";
    }
}