package processor;

import java.util.Objects;

public class Stock {

    private double quantity;
    private String stockName;
    private String ISIN;
    private double value;
    private double variation;
    private String category;


    public Stock() {
    }

    public Stock(double quantity, String stockName, String ISIN, double value) {
        this.quantity = quantity;
        this.stockName = stockName;
        this.ISIN = ISIN;
        this.value = value;
    }

    public Stock(double quantity, String stockName, String ISIN, String category, double value) {
        this.quantity = quantity;
        this.stockName = stockName;
        this.ISIN = ISIN;
        this.category = category;
        this.value = value;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "quantity=" + quantity +
                ", stockName='" + stockName + '\'' +
                ", ISIN='" + ISIN + '\'' +
                ", value=" + value +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stock stock = (Stock) o;
        return ISIN.equals(stock.ISIN);
    }

    //    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        Stock stock = (Stock) o;
//
//        return stockName.contains(stock.stockName);
//    }


    @Override
    public int hashCode() {
        return Objects.hash(ISIN);
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public String getISIN() {
        return ISIN;
    }

    public void setISIN(String ISIN) {
        this.ISIN = ISIN;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getVariation() {
        return variation;
    }

    public void setVariation(double variation) {
        this.variation = variation;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
