import java.math.BigInteger;

public class Stock {

    private double quantity;
    private String stockName;
    private String ISIN;
    private double value;

    public Stock(double quantity, String stockName, String ISIN, double value) {
        this.quantity = quantity;
        this.stockName = stockName;
        this.ISIN = ISIN;
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
}
