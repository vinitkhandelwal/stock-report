package processor;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class AxisFundReader implements FundReader {


    @Override
    public Map<String, Double> fundReader(String filePath) throws IOException, InvalidFormatException {


        Workbook workbook = WorkbookFactory.create(new File(filePath));

        // Retrieving the number of sheets in the Workbook
        System.out.println("Workbook has " + workbook.getNumberOfSheets() + " Sheets : ");
        Iterator<Sheet> sheetIterator = workbook.sheetIterator();



        Map<String, Stock> stockMap = new HashMap<>();
        List<Stock> stocks = new ArrayList<>();

        List<String> hdfcSheetName = Arrays.asList("AXISCB1","AXISCB4","AXISCGF","AXISDEF","AXISEA1","AXISEA2","AXISEAF",
                "AXISEHF","AXISEO1","AXISEO2","AXISEQF","AXISESF","AXISF25","AXISGOF","AXISH30",
                "AXISISF","AXISMCF","AXISMLF","AXISNETF","AXISSCF","AXISUSF","AXISTAF","AXISTSF");

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
                                    row.getCell(2).getStringCellValue(), row.getCell(5).getNumericCellValue());

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

        Map<String,Double> map1 =   stocks.stream().collect(Collectors.toMap(Stock::getStockName, Stock::getQuantity,(oldValue, newValue) -> oldValue+newValue));
        Map<String,Double> map2 =  map1.entrySet().
                stream().
                sorted(Map.Entry.comparingByValue()).
                collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        return map2;
    }
}
