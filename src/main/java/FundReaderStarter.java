import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import processor.*;


import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class FundReaderStarter {

    public static final String AUGUST = "./input/Monthly Portfolios for Aug 2019.xls";
    public static final String SEPT = "./input/Monthly Portfolios for Sep 2019_3.xls";
    public static final String OCT = "./input/Monthly Portfolios for Oct 2019.xls";
    public static final String AXIS_AUGUST = "./input/MONTHLY_PORTFOLIO_AXISMF Aug 2019.xlsx";
    public static final String AXIS_SEPT = "./input/MONTHLY_PORTFOLIO_AXISMF - SEPT 2019.xls";
    public static final String AXIS_OCT = "./input/MONTHLY_PORTFOLIO_AXISMF-Oct 2019.xlsx";

    public static final String MIRAI_AUGUST = "./input/mirae-asset-monthly-full-portfolio-august-2019.xls";
    public static final String MIRAI_SEPT = "./input/mirae-asset-monthly-full-portfolio-september-2019.xls";
    public static final String MIRAI_OCT = "./input/mirae-asset-full-portfolio---october-2019-(2).xls";

    public static final String HDFC_JUN = "F:\\Programming\\java\\Project\\asss\\src\\main\\resources\\Monthly Portfolios for Jun 2019_0.xls";
    public static final String HDFC_JUL = "F:\\Programming\\java\\Project\\asss\\src\\main\\resources\\Monthly Portfolios for Jul 2019_0.xls";
    public static final String SBI_SEPT = "./input/all schemes monthly portfolio - as on 30 september 2019.xls";
    public static final String SBI_OCT = "./input/all schemes monthly portfolio - as on 31 october 2019.xls";
    public static final String SBI_AUG = "./input/all schemes monthly portfolio - as on 31 august 2019.xls";
    public static final String SBI_JULY = "F:\\Programming\\java\\Project\\asss\\src\\main\\resources\\all schemes monthly portfolio - as on 31 july 2019.xls";
    public static final String SBI_JUN = "F:\\Programming\\java\\Project\\asss\\src\\main\\resources\\all schemes monthly portfolio - as on 30 june 2019.xls";
    public static final String SBI_MAY = "F:\\Programming\\java\\Project\\asss\\src\\main\\resources\\all schemes monthly portfolio - as on 31 may 2019.xls";


    private static final String output = "./output";

    private static String[] columns = {"Stock Name", "Previous Quantity", "New Quantity", "Variation"};

    public static void main(String[] args) throws IOException, InvalidFormatException {

        FundReader miraiFundReader = new MiraiFundReader();
        Map<String, Double> miraicurrentMonth = miraiFundReader.fundReader(MIRAI_OCT).get("quantity");
        Map<String, Double> miraipreviousMonth = miraiFundReader.fundReader(MIRAI_SEPT).get("quantity");
        String miraioutputFile = output+"/mirai_sept_oct.xlsx";
        List<Stock> miraiStocks = writeIntoExcel(miraipreviousMonth,miraicurrentMonth,miraioutputFile);

        Map<String, Double> mirraicurrentMonthValue = miraiFundReader.fundReader(MIRAI_OCT).get("value");;
        Map<String, Double> mirraipreviousMonthValue = miraiFundReader.fundReader(MIRAI_SEPT).get("value");;
        String mirraioutputFileValue = output+"/mirrai_sept_oct_value.xlsx";
        writeIntoExcel(mirraipreviousMonthValue,mirraicurrentMonthValue,mirraioutputFileValue);




       /* FundReader hdfcReader = new HdfcFundReader();

        Map<String, Double> currentMonth = hdfcReader.fundReader(OCT).get("quantity");
        Map<String, Double> previousMonth = hdfcReader.fundReader(SEPT).get("quantity");
        String outputFile = output+"/hdfc_sept_oct_quantity.xlsx";
        List<Stock> hdfcStocks = writeIntoExcel(previousMonth,currentMonth,outputFile);

        Map<String, Double> currentMonthValue = hdfcReader.fundReader(OCT).get("value");
        Map<String, Double> previousMonthValue = hdfcReader.fundReader(SEPT).get("value");
        String outputFileValue = output+"/hdfc_sept_oct_value.xlsx";
        writeIntoExcel(previousMonthValue,currentMonthValue,outputFileValue);*/

       /* FundReader sbiReader = new SbiFundReader();
        Map<String, Double> sbicurrentMonth = sbiReader.fundReader(SBI_OCT).get("quantity");;
        Map<String, Double> sbipreviousMonth = sbiReader.fundReader(SBI_SEPT).get("quantity");;
        String sbioutputFile = output+"/sbi_sept_oct_quantity.xlsx";
        List<Stock> sbiStocks = writeIntoExcel(sbipreviousMonth,sbicurrentMonth,sbioutputFile);

        Map<String, Double> sbicurrentMonthValue = sbiReader.fundReader(SBI_OCT).get("value");;
        Map<String, Double> sbipreviousMonthValue = sbiReader.fundReader(SBI_SEPT).get("value");;
        String sbioutputFileValue = output+"/sbi_sept_oct_value.xlsx";
        writeIntoExcel(sbipreviousMonthValue,sbicurrentMonthValue,sbioutputFileValue);*/
//
        FundReader axisFundReader = new AxisFundReader();
        Map<String, Double> axiscurrentMonth = axisFundReader.fundReader(AXIS_OCT).get("quantity");
        Map<String, Double> axispreviousMonth = axisFundReader.fundReader(AXIS_SEPT).get("quantity");
        String axisoutputFile = output+"/axis_sept_oct.xlsx";
        List<Stock> axisStocks = writeIntoExcel(axispreviousMonth,axiscurrentMonth,axisoutputFile);

        Map<String, Double> axiscurrentMonthValue = axisFundReader.fundReader(AXIS_OCT).get("value");;
        Map<String, Double> axispreviousMonthValue = axisFundReader.fundReader(AXIS_SEPT).get("value");;
        String axisoutputFileValue = output+"/axis_sept_oct_value.xlsx";
        writeIntoExcel(axispreviousMonthValue,axiscurrentMonthValue,axisoutputFileValue);
//
//        System.out.println("Between axis and sbi");
//        axisStocks.stream().filter(axis ->
//                sbiStocks.stream().anyMatch(hdfc -> axis.getStockName().contains(hdfc.getStockName())
//                        && (axis.getVariation() > 0 && hdfc.getVariation() > 0))).
//                forEach(e -> System.out.println(e.getStockName() +" , "+ e.getVariation()));
//
//        System.out.println("--------------------------");
//
//        System.out.println("Between axis and hdfc");
//        axisStocks.stream().filter(axis ->
//                hdfcStocks.stream().anyMatch(hdfc -> axis.getStockName().contains(hdfc.getStockName())
//                        && (axis.getVariation() > 0 && hdfc.getVariation() > 0))).
//                forEach(e -> System.out.println(e.getStockName() +" , "+ e.getVariation()));
//
//
//        System.out.println("--------------------------");
//
//        System.out.println("Between sbi and hdfc");
//
//        sbiStocks.stream().filter(axis ->
//                hdfcStocks.stream().anyMatch(hdfc -> axis.getStockName().contains(hdfc.getStockName())
//                        && (axis.getVariation() > 0 && hdfc.getVariation() > 0))).
//                forEach(e -> {try{System.out.println(e.getStockName() +" , "+ e.getVariation() + " ," +
//                        hdfcStocks.get(hdfcStocks.indexOf(e) ).getVariation());}
//                        catch(Exception ex){
//                            System.out.println(hdfcStocks.indexOf(e) + " " + e);
//                        }
//                });
//
//
//        System.out.println("----------------------------");

    }

    private static List<Stock> writeIntoExcel(Map<String, Double> previousMonth,Map<String, Double> curentMOnth, String outputFileName) throws IOException {
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
        List<Stock> stocks = new ArrayList<>();
        for(Map.Entry<String,Double> current : curentMOnth.entrySet()){
            Row row = sheet.createRow(rowNum++);
            Stock stock = new Stock();
            if(previousMonth.containsKey(current.getKey())){
                double variation = (current.getValue() - previousMonth.get(current.getKey()))*100/previousMonth.get(current.getKey());
                row.createCell(0).setCellValue(current.getKey());
                row.createCell(1).setCellValue(previousMonth.get(current.getKey()));
                row.createCell(2).setCellValue(current.getValue());
                row.createCell(3).setCellValue(variation);

                    stock.setStockName(current.getKey());stock.setVariation(variation);


            }else{
                row.createCell(0).setCellValue(current.getKey());
                row.createCell(1).setCellValue(0);
                row.createCell(2).setCellValue(current.getValue());
                row.createCell(3).setCellValue(current.getValue());
                stock.setStockName(current.getKey());stock.setVariation(current.getValue());
            }
            stocks.add(stock);

        }


        for(int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }


        // Resize all columns to fit the content size
        for(int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Write the output to a file
        FileOutputStream fileOut = new FileOutputStream(outputFileName);
        workbook.write(fileOut);
        fileOut.close();

        // Closing the workbook
        workbook.close();
        return stocks;
    }





}