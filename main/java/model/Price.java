package model;

public class Price {
    private float amount;
    private String typeOfPlay;

    public Price(float amount, String typeOfPlay) {
        this.amount = amount;
        this.typeOfPlay = typeOfPlay;
    }

    public float getAmount() {
        return amount;
    }

    public String getTypeOfPlay() {
        return typeOfPlay;
    }
}
