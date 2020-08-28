package Utils;

import processor.Stock;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Converter {

    public static Map<Stock,Double> convertStockListToMapBasedOnQuantity(List<Stock> stocks){
        Map<Stock,Double> quantityTempMap =   stocks.stream().filter(stock -> !Validator.isBond(stock)).
                collect(Collectors.toMap(x -> x, Stock::getQuantity,(oldValue, newValue) -> oldValue+newValue));
        Map<Stock,Double> quantityMap =  quantityTempMap.entrySet().
                stream().
                sorted(Map.Entry.comparingByValue()).
                collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        return quantityMap;

    }

    public static Map<Stock,Double> convertStockListToMapBasedOnValuation(List<Stock> stocks){

        Map<Stock,Double> valuationTempMap =   stocks.stream()
                .filter(stock -> !Validator.isBond(stock)).collect(Collectors.toMap(x -> x, Stock::getValue,(oldValue, newValue) -> oldValue+newValue));
        Map<Stock,Double> valuationMap =  valuationTempMap.entrySet().
                stream().
                sorted(Map.Entry.comparingByValue()).
                collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        return valuationMap;
    }
}
