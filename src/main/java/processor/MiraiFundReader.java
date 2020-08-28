package processor;

import Utils.Converter;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class MiraiFundReader implements FundReader {


    @Override
    public Map<String, Map<Stock,Double>>  fundReader(String filePath) throws IOException, InvalidFormatException {
        Workbook workbook = WorkbookFactory.create(new File(filePath));

        // Retrieving the number of sheets in the Workbook
        System.out.println("Workbook has " + workbook.getNumberOfSheets() + " Sheets : ");
        Iterator<Sheet> sheetIterator = workbook.sheetIterator();



        Map<String, Stock> stockMap = new HashMap<>();
        List<Stock> stocks = new ArrayList<>();

        List<String> hdfcSheetName = Arrays.asList("MALCF","MAEBF","MAGCF","MAHEF","MATSF","MAHCF","MAESF","MAFF","MAN50ETF","MAMCF");

//        List<String> hdfcSheetName = Arrays.asList("HDFCSX","HDFCSXEXTF");
        workbook.forEach(sheet -> {
            if(hdfcSheetName.contains(sheet.getSheetName())){
                Iterator<Row> rowIterator = sheet.rowIterator();
                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    try{
                        if(row.getCell(2).getCellTypeEnum().equals(CellType.STRING) && row.getCell(2).getStringCellValue().startsWith("IN")) {
                            String stockName = row.getCell(1).getStringCellValue();
                            Stock stock = new Stock(row.getCell(4).getNumericCellValue(), stockName,
                                    row.getCell(2).getStringCellValue(),row.getCell(3).getStringCellValue(), row.getCell(5).getNumericCellValue());

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

        Map<String, Map<Stock,Double>> result = new HashMap<>();

        result.put("quantity", Converter.convertStockListToMapBasedOnQuantity(stocks));
        result.put("value",Converter.convertStockListToMapBasedOnValuation(stocks));
        return  result;
    }
}
