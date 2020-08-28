
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import processor.*;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class FundReaderStarter {

    public static final String SECURITY_LIST = "./input/ISINWISE 06.12.19.xlsx";
    public static final Map<String,String> ISIN_SECURTY_MAP = new HashMap<>();

    public static final String AXIS_APR = "./input/MONTHLY_PORTFOLIO_AXISMF_ April 20.xlsx";
    public static final String AXIS_MAY = "./input/MONTHLY_PORTFOLIO_AXISMF May 2020.xlsx";
    public static final String AXIS_JUN = "./input/MONTHLY_PORTFOLIO_AXISMF June 20.xls";
    public static final String AXIS_JUL = "./input/MONTHLY_PORTFOLIO_AXISMF_July 20.xls";

    public static final String MIRAI_APR = "./input/mirae-asset-full-portfolio---april-2020.xlsx";
    public static final String MIRAI_MAY = "./input/mirae-asset-full-portfolio---may-2020-final.xls";
    public static final String MIRAI_JUN = "./input/mirae-asset-full-portfolio---june-2020---final.xlsx";
    public static final String MIRAI_JULY = "./input/mirae-asset-full-portfolio---july-2020---final.xlsx";

    public static final String HDFC_APR= "./input/Monthly Portfolios for April 2020.xls";
    public static final String HDFC_MAY= "./input/Monthly Portfolios for May 2020_1.xls";
    public static final String HDFC_JUN= "./input/HDFC_Monthly Portfolios for June 2020_0.xls";
    public static final String HDFC_JUL= "./input/Monthly Portfolios for July 2020.xls";

    public static final String SBI_MAY= "./input/sbi mf monthly_portfolio_report may-2020.xls";
    public static final String SBI_JUN= "./input/sbi_all schemes monthly portfolio -  as on 30 june 2020.xls";
    public static final String SBI_JUL= "./input/all schemes monthly portfolio -  as on 31 july 2020.xls";


    public static final String HDFC_PREVIOUS= "./input/HDFC_Monthly Portfolios for June 2020_0.xls";
    public static final String HDFC_CURRENT= "./input/Monthly Portfolios for July 2020.xls";
    public static final String SBI_PREVIOUS= "./input/sbi_all schemes monthly portfolio -  as on 30 june 2020.xls";
    public static final String SBI_CURRENT= "./input/all schemes monthly portfolio -  as on 31 july 2020.xls";
    public static final String MIRAI_PREVIOUS = "./input/mirae-asset-full-portfolio---june-2020---final.xlsx";
    public static final String MIRAI_CURRENT = "./input/mirae-asset-full-portfolio---july-2020---final.xlsx";
    public static final String AXIS_PREVIOUS = "./input/MONTHLY_PORTFOLIO_AXISMF June 20.xls";
    public static final String AXIS_CURRENT = "./input/MONTHLY_PORTFOLIO_AXISMF_July 20.xls";







    private static final String output = "./output";

    private static String[] columns = {"Stock Name", "Previous Quantity", "New Quantity", "Variation", "Category"};

    public static void main(String[] args) throws IOException, InvalidFormatException {

        String excelQuantityFileName = output+"/Jun-July-Quantity.xlsx";
        String excelValueFileName = output+"/Jun-July-Value.xlsx";
        Workbook workbook = new XSSFWorkbook();

        List<ReportData> Quantitylist = new ArrayList<>();
        List<ReportData> valuelist = new ArrayList<>();

        populateSecurityList();
        FundReader miraiFundReader = new MiraiFundReader();
        Map<String, Map<Stock,Double>> currentMonthMirraiMap = miraiFundReader.fundReader(MIRAI_CURRENT);
        Map<String, Map<Stock,Double>> previousMonthMirraiMap = miraiFundReader.fundReader(MIRAI_PREVIOUS);

        Map<Stock, Double> miraicurrentMonth = currentMonthMirraiMap.get("quantity");
        Map<Stock, Double> miraipreviousMonth = previousMonthMirraiMap.get("quantity");
        Quantitylist.add(new ReportData(miraipreviousMonth,miraicurrentMonth,"MIRRAI"));


        Map<Stock, Double> mirraicurrentMonthValue = currentMonthMirraiMap.get("value");;
        Map<Stock, Double> mirraipreviousMonthValue = previousMonthMirraiMap.get("value");;
        valuelist.add(new ReportData(mirraipreviousMonthValue, mirraicurrentMonthValue,"MIRRAI"));



        FundReader hdfcReader = new HdfcFundReader();
        Map<String, Map<Stock,Double>> currentMonthHDFCMap = hdfcReader.fundReader(HDFC_CURRENT);
        Map<String, Map<Stock,Double>> previousMonthHDFCMap = hdfcReader.fundReader(HDFC_PREVIOUS);
        Map<Stock, Double> currentMonth = currentMonthHDFCMap.get("quantity");
        Map<Stock, Double> previousMonth = previousMonthHDFCMap.get("quantity");
        Quantitylist.add(new ReportData(previousMonth,currentMonth,"HDFC"));

        Map<Stock, Double> currentMonthValue = currentMonthHDFCMap.get("value");
        Map<Stock, Double> previousMonthValue = previousMonthHDFCMap.get("value");
        valuelist.add(new ReportData(previousMonthValue, currentMonthValue,"HDFC"));


        FundReader sbiReader = new SbiFundReader();
        Map<String, Map<Stock,Double>> currentMonthMap = sbiReader.fundReader(SBI_CURRENT);
        Map<String, Map<Stock,Double>> previousMonthMap = sbiReader.fundReader(SBI_PREVIOUS);
        Map<Stock, Double> sbicurrentMonth = currentMonthMap.get("quantity");
        Map<Stock, Double> sbipreviousMonth = previousMonthMap.get("quantity");
        Quantitylist.add(new ReportData(sbipreviousMonth,sbicurrentMonth,"SBI"));

        Map<Stock, Double> sbicurrentMonthValue = currentMonthMap.get("value");;
        Map<Stock, Double> sbipreviousMonthValue = previousMonthMap.get("value");;
        valuelist.add(new ReportData(sbipreviousMonthValue, sbicurrentMonthValue,"SBI"));


        FundReader axisFundReader = new AxisFundReader();
        Map<String, Map<Stock,Double>> currentMonthAxisMap = axisFundReader.fundReader(AXIS_CURRENT);
        Map<String, Map<Stock,Double>> previousMonthAxisMap = axisFundReader.fundReader(AXIS_PREVIOUS);
        Map<Stock, Double> axiscurrentMonth = currentMonthAxisMap.get("quantity");
        Map<Stock, Double> axispreviousMonth = previousMonthAxisMap.get("quantity");
        Quantitylist.add(new ReportData(axispreviousMonth,axiscurrentMonth,"AXIS"));

        Map<Stock, Double> axiscurrentMonthValue = currentMonthAxisMap.get("value");;
        Map<Stock, Double> axispreviousMonthValue = previousMonthAxisMap.get("value");;
        valuelist.add(new ReportData(axispreviousMonthValue, axiscurrentMonthValue,"AXIS"));


        writeIntoExcel(excelQuantityFileName, Quantitylist);
        writeIntoExcel(excelValueFileName, valuelist);

    }

    private static  void populateSecurityList() throws IOException, InvalidFormatException {
        Workbook workbook = WorkbookFactory.create(new File(SECURITY_LIST));
        workbook.forEach(sheet -> {
                Iterator<Row> rowIterator = sheet.rowIterator();
                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    try{
                            ISIN_SECURTY_MAP.put(row.getCell(0).getStringCellValue(),row.getCell(2).getStringCellValue());
                    }
                    catch(Exception e){
                        System.out.println("inValid Mapping");
                    }
                }
                System.out.println();

        });
    }

    private static void writeIntoExcel(String outputFileName, List<ReportData> reportDataList) throws IOException {
        // new HSSFWorkbook() for generating `.xls` file
        Workbook workbook = new XSSFWorkbook();
        // Create a Sheet
        for(ReportData reportData : reportDataList) {
            Sheet sheet = workbook.createSheet(reportData.getSheetName());
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
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerCellStyle);
            }
            // Create Other rows and cells with employees data
            int rowNum = 1;
            List<Stock> stocks = new ArrayList<>();
            for (Map.Entry<Stock, Double> current : reportData.getCurrentData().entrySet()) {
                Row row = sheet.createRow(rowNum++);
                Stock stock = new Stock();
                if (reportData.getPreviousData().containsKey(current.getKey())) {
                    double variation = (current.getValue() - reportData.getPreviousData().get(current.getKey())) * 100 / reportData.getPreviousData().get(current.getKey());
                    row.createCell(0).setCellValue(ISIN_SECURTY_MAP.get(current.getKey().getISIN()) == null ? current.getKey().getISIN() : ISIN_SECURTY_MAP.get(current.getKey().getISIN()));
                    row.createCell(1).setCellValue(reportData.getPreviousData().get(current.getKey()));
                    row.createCell(2).setCellValue(current.getValue());
                    row.createCell(3).setCellValue(variation);
                    row.createCell(4).setCellValue(current.getKey().getCategory());
                    stock.setStockName(current.getKey().getISIN());
                    stock.setVariation(variation);
                } else {
                    row.createCell(0).setCellValue(ISIN_SECURTY_MAP.get(current.getKey().getISIN()) == null ? current.getKey().getISIN() : ISIN_SECURTY_MAP.get(current.getKey().getISIN()));
                    row.createCell(1).setCellValue(0);
                    row.createCell(2).setCellValue(current.getValue());
                    row.createCell(3).setCellValue(current.getValue());
                    row.createCell(4).setCellValue(current.getKey().getCategory());
                    stock.setStockName(current.getKey().getISIN());
                    stock.setVariation(current.getValue());
                }
                stocks.add(stock);

            }


            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }


            // Resize all columns to fit the content size
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }
        }
        FileOutputStream fileOut = new FileOutputStream(outputFileName);
        workbook.write(fileOut);
        fileOut.close();

        // Closing the workbook
        workbook.close();

    }





}