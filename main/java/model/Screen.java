package model;

public class Screen {
    private String screenName;
    private int theatre;
    private int nbRows;
    private int nbChairsRow;

    public Screen(String screenName, int theatre, int nbRows, int nbChairsRow) {
        this.screenName = screenName;
        this.theatre = theatre;
        this.nbRows = nbRows;
        this.nbChairsRow = nbChairsRow;
    }

    public String getScreenName() {
        return screenName;
    }

    public int getTheatre() {
        return theatre;
    }

    public int getNbRows() {
        return nbRows;
    }

    public int getNbChairsRow() {
        return nbChairsRow;
    }
}
