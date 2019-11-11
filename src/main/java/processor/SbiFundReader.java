package processor;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class SbiFundReader implements FundReader {



    @Override
    public Map<String, Map<String,Double>> fundReader(String filePath) throws IOException, InvalidFormatException {
        Workbook workbook = WorkbookFactory.create(new File(filePath));

        // Retrieving the number of sheets in the Workbook
        System.out.println("Workbook has " + workbook.getNumberOfSheets() + " Sheets : ");
        Iterator<Sheet> sheetIterator = workbook.sheetIterator();

        Map<String, Stock> stockMap = new HashMap<>();
        List<Stock> stocks = new ArrayList<>();

//        List<String> sbiSheetName = Arrays.asList("SMEEF","SLMF","SMTGS","SMGLF","SEHF","SMIF","SCOF","STOF","SHOF","SCF",
//                "SNIF","SMCBF","SOF","SMMDF","SFEF","SDHF","SMUSD",
//                "SMIDCAP","SMCMF","SMCOMMA","SMGF","SMMULTI","SMAAF","SBLUECHIP","SAOF","SIF","SMLDF","STAF-II",
//                "SETF-SENSEX","SSCF","SBPF","STAF-III","SEOF-I","SLTAF-I","SLTAF-II",
//                "SBFS","SDAAF","SETF-NN50","SETF-NBank",
//                "SETF-BSE 100","SESF","SETF-Nifty 50","SEOF-IV","SLTAF-III","SETF-10 Yr Gilt","SDFS-B-44");
//

        List<String> sbiSheetName = Arrays.asList("SMEEF","SLMF","SMTGS","SMGLF","SEHF","SMIF","SCOF","STOF","SHOF","SCF","SNIF","SMCBF","SOF","SMMDF","SLF","SDBF","SSF","SCRF","SFEF","SDHF","SMUSD","SMIDCAP","SMCMF","SMCOMMA","SMGF","SMMULTI","SMAAF","SBLUECHIP","SAOF","SIF","SMLDF","SSTDF","SETF-Gold","SPSU","SGF","STAF-II","SETF-SENSEX","SSCF","SBPF","STAF-III","SEOF-I","SLTAF-I","SLTAF-II","SBFS","SDAAF","SETF-NN50","SETF-NBank","SETF-BSE 100","SESF","SETF-Nifty 50","SEOF-IV","SLTAF-III","SETF-10 Yr Gilt","SDAFS-XVIII","SLTAF-IV","SDAFS-XIX","SDFS-B-46","SDFS-B-49","SDAFS-XXII","SDFS-C-1","SDAFS-XXIII","SDFS-C-2","SDAFS-XXIV","SDAFS-XXV","SLTAF-V","SDFS-C-7","SDAFS-XXVI","SDFS-C-8","SDFS-C-9","SDFS-C-10","SDAFS-XXVII","SDFS-C-12","SDFS-C-14","SLTAF-VI","SDAFS-XXVIII","SDFS-C-16","SDFS-C-18","SDFS-C-19","SDAFS-XXIX","SDFS-C-20","SDFS-C-21","SDFS-C-22","SDFS-C-23","SETF-SN50","SDFS-C-24","SDFS-C-25","SDAFS-XXX","SDFS-C-26","SDFS-C-27","SDFS-C-28","SDFS-C-29","SDFS-C-30","SETF-Quality","SDFS-C-31","SDFS-C-32","SDFS-C-33","SDFS-C-34","SDFS-C-35","SDFS-C-36","SDFS-C-37","SDFS-C-38","SCBF","SDFS-C-40","SDFS-C-41","SDFS-C-42","SDFS-C-43","SDFS-C-44","SCPOF-A1","SDFS-C-46","SEMVF","SDFS-C-47","SDFS-C-48","SDFS-C-49","SCPOF-Series A (Plan 2)","SDFS-C-50","SFMP- Series 1","SFMP- Series 2","SFMP- Series 3","SFMP- Series 4","SCPOF-Series A (Plan 3)","SFMP- Series 6","SFMP- Series 7","SFMP- Series 8","SCPOF-Series A (Plan 4)","SFMP- Series 9","SFMP- Series 10","SFMP- Series 11","SFMP- Series 12","SFMP- Series 13","SFMP- Series 14","SFMP- Series 15","SFMP- Series 16","SFMP- Series 17","SCPOF-Series A (Plan 5)","SFMP- Series 18","SCPOF-Series A (Plan 6)","SFMP- Series 19","SFMP- Series 20","SFMP- Series 21","SFMP- Series 22","SBIRIOS");

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
