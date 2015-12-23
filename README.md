# GSheets2A

GSheets2A - An android library to fetch Google Sheets to Android

## Usage

GSheets2A expects the spreadsheet data to be stored in objects, hence requires an object type to store the data.
For example, [this](https://docs.google.com/spreadsheets/d/1tJ64Y8hje0ui4ap9U33h3KWwpxT_-JuVMSZzxD2Er8k) spreadsheet contains data from English Premier League Finals. For this data a class `Team` could be created something like:

```java
public class Team {
    String name;
    Integer totalWins;
    ...
}
```

For this class to store spreadsheet data received from gsheets2a, it should implement `DataObject` interface defined in gsheets2a. This interface just one method which looks like:

```java
public interface DataObject {
    void saveData(ArrayList<String> data);
}
```

Here is an example how class `Team` could implement `DataObject`:
```java
public class Team implements GSheets2A.DataObject {

    String name;
    Integer totalWins;
    ...

    @Override
    public void saveData(ArrayList<String> data) {
        // As per the table https://docs.google.com/spreadsheets/d/1tJ64Y8hje0ui4ap9U33h3KWwpxT_-JuVMSZzxD2Er8k
        name = data.get(1); // 2nd column 
        totalWins = Math.round(Float.parseFloat(data.get(3))); // 3rd column
        ...
    }
}
```

Now, to fetch the data create an instance of `GSheets2A` with key of the google sheet and application context.

```java
GSheets2A gSheets2A = new GSheets2A("1tJ64Y8hje0ui4ap9U33h3KWwpxT_-JuVMSZzxD2Er8k", getApplicationContext());
```

The key of a google sheet can be extracted from the public url of the sheet. Public url could be obtained from File > Share in Google Sheets.

Fetch the data using the above `gSheets2A` object like:

```java
gSheets2A.getData(Team.class, new GSheets2A.DataResult() {
    @Override
    public void onReceiveData(ArrayList data) {
        ArrayList<Team> teams = data; // convert the ArrayList to Team list
        String allNames = "";

        // Iterate through the list and use the data
        for (Team t : teams) {
            allNames += t.name + "\t\t" + t.totalWins + "\n";
        }
        text1.setText(allNames); // use the data
    }
});
```

`getData(Class, callback)` reads the `Class` and creates an ArrayList of objects of the given class type and passes it to the callback. The callback is executed after all the spreadsheet data is fetched and processed for use.

## Gradle

Add to build.gradle depencencies

```groovy
dependencies {
    compile 'space.darkowlzz:gsheets2a:0.1.0'
}
```

## LICENSE

MIT &copy; 2015 Sunny (darkowlzz)
