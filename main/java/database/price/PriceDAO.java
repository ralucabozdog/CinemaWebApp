package database.price;

import model.Price;

import java.util.List;

public interface PriceDAO {
    boolean updatePrice(float amount, String typeOfPlay);

    List<Price> getAllPrices();

    float getPriceForTypeOfPlay(String typeOfPlay);
}
