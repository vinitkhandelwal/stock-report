package processor;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class HdfcFundReader implements FundReader {


    @Override
     public Map<String, Map<String,Double>>  fundReader(String filePath) throws IOException, InvalidFormatException {
        Workbook workbook = WorkbookFactory.create(new File(filePath));

        // Retrieving the number of sheets in the Workbook
        System.out.println("Workbook has " + workbook.getNumberOfSheets() + " Sheets : ");
        Iterator<Sheet> sheetIterator = workbook.sheetIterator();



        Map<String, Stock> stockMap = new HashMap<>();
        List<Stock> stocks = new ArrayList<>();

        List<String> hdfcSheetName = Arrays.asList("HDFCSX","HDFCSXEXTF","HDFCNY","HDFCNYEXTF","HCHARITYAP","HDINFG",
                "HDFCEQ","HDFCTA","HDFCTS","HDFCGR","HEOFJUN117","HDFCEOF217","HDFCPM","HDFCCS","HDFCCB" ,"HDFCLARGEF",
                "HDFCRETEQP","HDFCAR","HDFCHOF117","MY2005","HDFCGF","HDFCRETHEP","HDFCMY","MIDCAP","HDFCSMALLF","HMIPLT",
                "HDFCRETHDP","HDFCT2");

//        List<String> hdfcSheetName = Arrays.asList("HDFCSX","HDFCSXEXTF");
        workbook.forEach(sheet -> {
            if(hdfcSheetName.contains(sheet.getSheetName())){
                Iterator<Row> rowIterator = sheet.rowIterator();
                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    try{
                        if(row.getCell(1).getCellTypeEnum().equals(CellType.STRING) && row.getCell(1).getStringCellValue().startsWith("IN")) {
                            String stockName = row.getCell(3).getStringCellValue();
                            Stock stock = new Stock(row.getCell(5).getNumericCellValue(), stockName,
                                    row.getCell(1).getStringCellValue(), row.getCell(6).getNumericCellValue());

                            stocks.add(stock);
                        }
                    }
                    catch(Exception e){
                        System.out.println("inValid Mapping");
                    }

                }
                System.out.println();
            }
        });

        Map<String,Double> quantityTempMap =   stocks.stream().collect(Collectors.toMap(Stock::getStockName, Stock::getQuantity,(oldValue, newValue) -> oldValue+newValue));
        Map<String,Double> quantityMap =  quantityTempMap.entrySet().
                stream().
                sorted(Map.Entry.comparingByValue()).
                collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        Map<String,Double> valuationTempMap =   stocks.stream().collect(Collectors.toMap(Stock::getStockName, Stock::getValue,(oldValue, newValue) -> oldValue+newValue));
        Map<String,Double> valuationMap =  valuationTempMap.entrySet().
                stream().
                sorted(Map.Entry.comparingByValue()).
                collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        System.out.println(valuationMap);

        Map<String, Map<String,Double>> result = new HashMap<>();

        result.put("quantity",quantityMap);
        result.put("value",valuationMap);


        return result;
    }
}
