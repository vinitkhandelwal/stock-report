package processor;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class SbiFundReader implements FundReader {



    @Override
    public Map<String, Double> fundReader(String filePath) throws IOException, InvalidFormatException {
        Workbook workbook = WorkbookFactory.create(new File(filePath));

        // Retrieving the number of sheets in the Workbook
        System.out.println("Workbook has " + workbook.getNumberOfSheets() + " Sheets : ");
        Iterator<Sheet> sheetIterator = workbook.sheetIterator();

        Map<String, Stock> stockMap = new HashMap<>();
        List<Stock> stocks = new ArrayList<>();

        List<String> sbiSheetName = Arrays.asList("SMEEF","SLMF","SMTGS","SMGLF","SEHF","SMIF","SCOF","STOF","SHOF","SCF",
                "SNIF","SMCBF","SOF","SMMDF","SFEF","SDHF","SMUSD",
                "SMIDCAP","SMCMF","SMCOMMA","SMGF","SMMULTI","SMAAF","SBLUECHIP","SAOF","SIF","SMLDF","STAF-II",
                "SETF-SENSEX","SSCF","SBPF","STAF-III","SEOF-I","SLTAF-I","SLTAF-II",
                "SBFS","SDAAF","SETF-NN50","SETF-NBank",
                "SETF-BSE 100","SESF","SETF-Nifty 50","SEOF-IV","SLTAF-III","SETF-10 Yr Gilt","SDFS-B-44");

//        List<String> sbiSheetName = Arrays.asList("HDFCSX","HDFCSXEXTF");
        workbook.forEach(sheet -> {
            if(sbiSheetName.contains(sheet.getSheetName())){
                Iterator<Row> rowIterator = sheet.rowIterator();
                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    try{
                        if(row.getCell(3).getCellTypeEnum().equals(CellType.STRING) && row.getCell(3).getStringCellValue().startsWith("IN")) {
                            String stockName = row.getCell(2).getStringCellValue();
                            Stock stock = new Stock(row.getCell(6).getNumericCellValue(), stockName,
                                    row.getCell(3).getStringCellValue(), row.getCell(7).getNumericCellValue());


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
