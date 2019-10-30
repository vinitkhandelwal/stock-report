import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import javax.xml.bind.SchemaOutputResolver;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class FundReader {

    public static final String AUGUST = "F:\\Programming\\java\\Project\\asss\\src\\main\\resources\\Monthly Portfolios for Aug 2019.xls";
    public static final String SEPT = "F:\\Programming\\java\\Project\\asss\\src\\main\\resources\\Monthly Portfolios for Sep 2019_3.xls";
    public static final String HDFC_JUN = "F:\\Programming\\java\\Project\\asss\\src\\main\\resources\\Monthly Portfolios for Jun 2019_0.xls";
    public static final String HDFC_JUL = "F:\\Programming\\java\\Project\\asss\\src\\main\\resources\\Monthly Portfolios for Jul 2019_0.xls";
    public static final String SBI_SEPT = "F:\\Programming\\java\\Project\\asss\\src\\main\\resources\\all schemes monthly portfolio - as on 30 september 2019.xls";
    public static final String SBI_AUG = "F:\\Programming\\java\\Project\\asss\\src\\main\\resources\\all schemes monthly portfolio - as on 31 august 2019.xls";
    public static final String SBI_JULY = "F:\\Programming\\java\\Project\\asss\\src\\main\\resources\\all schemes monthly portfolio - as on 31 july 2019.xls";
    public static final String SBI_JUN = "F:\\Programming\\java\\Project\\asss\\src\\main\\resources\\all schemes monthly portfolio - as on 30 june 2019.xls";
    public static final String SBI_MAY = "F:\\Programming\\java\\Project\\asss\\src\\main\\resources\\all schemes monthly portfolio - as on 31 may 2019.xls";


    private static final String output = "F:\\Programming\\java\\Project\\asss\\src\\main\\output";

    private static String[] columns = {"Stock Name", "Previous Quantity", "New Quantity", "Variation"};

    public static void main(String[] args) throws IOException, InvalidFormatException {

        Map<String, Double> currentMonth = hdfcFundReport(SEPT);
        Map<String, Double> previousMonth = hdfcFundReport(AUGUST);
        String outputFile = "hdfc_aug_sept.xlsx";

        /*Map<String, Double> currentMonth = sbiFundReport(SBI_JUN);
        Map<String, Double> previousMonth = sbiFundReport(SBI_MAY);
        String outputFile = "sbi_may_jun.xlsx";*/
        writeIntoExcel(previousMonth,currentMonth,outputFile);


    }

    private static void writeIntoExcel(Map<String, Double> previousMonth,Map<String, Double> curentMOnth, String outputFileName) throws IOException {
        Workbook workbook = new XSSFWorkbook(); // new HSSFWorkbook() for generating `.xls` file

        /* CreationHelper helps us create instances of various things like DataFormat,
           Hyperlink, RichTextString etc, in a format (HSSF, XSSF) independent way */
        CreationHelper createHelper = workbook.getCreationHelper();

        // Create a Sheet
        Sheet sheet = workbook.createSheet("HDFC");

        // Create a Font for styling header cells
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setColor(IndexedColors.RED.getIndex());

        // Create a CellStyle with the font
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);

        // Create a Row
        Row headerRow = sheet.createRow(0);

        // Create cells
        for(int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerCellStyle);
        }


        // Create Other rows and cells with employees data
        int rowNum = 1;
        for(Map.Entry<String,Double> current : curentMOnth.entrySet()){
            Row row = sheet.createRow(rowNum++);
            if(previousMonth.containsKey(current.getKey())){
                double variation = (current.getValue() - previousMonth.get(current.getKey()))*100/previousMonth.get(current.getKey());
                row.createCell(0).setCellValue(current.getKey());
                row.createCell(1).setCellValue(previousMonth.get(current.getKey()));
                row.createCell(2).setCellValue(current.getValue());
                row.createCell(3).setCellValue(variation);

            }else{
                row.createCell(0).setCellValue(current.getKey());
                row.createCell(1).setCellValue(0);
                row.createCell(2).setCellValue(current.getValue());
                row.createCell(3).setCellValue(100);
            }

        }


        for(int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }


        // Resize all columns to fit the content size
        for(int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Write the output to a file
        FileOutputStream fileOut = new FileOutputStream(output+"\\"+outputFileName);
        workbook.write(fileOut);
        fileOut.close();

        // Closing the workbook
        workbook.close();
    }


    private static Map<String,Double> sbiFundReport(String filePath) throws IOException, InvalidFormatException {
        Workbook workbook = WorkbookFactory.create(new File(filePath));

        // Retrieving the number of sheets in the Workbook
        System.out.println("Workbook has " + workbook.getNumberOfSheets() + " Sheets : ");
        Iterator<Sheet> sheetIterator = workbook.sheetIterator();

        Map<String,Stock> stockMap = new HashMap<>();
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

        Map<String,Double> map1 =   stocks.stream().collect(Collectors.toMap(Stock::getStockName,Stock::getQuantity,(oldValue,newValue) -> oldValue+newValue));
        Map<String,Double> map2 =  map1.entrySet().
                stream().
                sorted(Map.Entry.comparingByValue()).
                collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        return map2;

    }



    private static Map<String,Double> hdfcFundReport(String filePath) throws IOException, InvalidFormatException {
        Workbook workbook = WorkbookFactory.create(new File(filePath));

        // Retrieving the number of sheets in the Workbook
        System.out.println("Workbook has " + workbook.getNumberOfSheets() + " Sheets : ");
        Iterator<Sheet> sheetIterator = workbook.sheetIterator();



        Map<String,Stock> stockMap = new HashMap<>();
        List<Stock> stocks = new ArrayList<>();

        List<String> hdfcSheetName = Arrays.asList("HDFCSX","HDFCSXEXTF","HDFCNY","HDFCNYEXTF","HCHARITYAP","HDINFG",
                "HDFCEQ","HDFCTA","HDFCTS","HDFCGR","HEOFJUN117","HDFCEOF217","HDFCPM","HDFCCS","HDFCCB" ,"HDFCLARGEF",
                "HDFCRETEQP","HDFCAR","HDFCHOF117","MY2005","HDFCGF","HDFCRETHEP","HDFCMY","MIDCAP","HDFCSMALLF","HMIPLT",
                "HDFCRETHDP");

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

        Map<String,Double> map1 =   stocks.stream().collect(Collectors.toMap(Stock::getStockName,Stock::getQuantity,(oldValue,newValue) -> oldValue+newValue));
        Map<String,Double> map2 =  map1.entrySet().
                stream().
                sorted(Map.Entry.comparingByValue()).
                collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        return map2;



    }
}