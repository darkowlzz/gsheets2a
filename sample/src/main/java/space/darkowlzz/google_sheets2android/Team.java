package space.darkowlzz.google_sheets2android;

import java.util.ArrayList;

import space.darkowlzz.gsheets2a.GSheets2A;

public class Team implements GSheets2A.DataObject {

    String name;
    Integer totalWins;

    @Override
    public void saveData(ArrayList<String> data) {
        // As per the table https://docs.google.com/spreadsheets/d/1tJ64Y8hje0ui4ap9U33h3KWwpxT_-JuVMSZzxD2Er8k
        name = data.get(1);
        totalWins = Math.round(Float.parseFloat(data.get(3)));
    }
}
