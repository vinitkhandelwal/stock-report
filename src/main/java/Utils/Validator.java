package Utils;

import processor.Stock;

import java.util.*;

public class Validator {

   static List<String> bondCategory = Arrays.asList("Sovereign","SOVEREIGN",
            "CRISIL",
            "CARE",
            "ICRA",
            "IND",
            "[ICRA]",
            "BWR");

    public static boolean isBond(Stock stock){
        if(stock.getCategory() == null || stock.getCategory().isEmpty() ) return false;

        Map m1 = new HashMap<>();
     return bondCategory.stream().anyMatch(s ->  stock.getCategory().startsWith(s));





    }
}
