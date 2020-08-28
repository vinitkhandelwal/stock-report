package processor;

import java.util.Map;

public class ReportData {

    Map<Stock, Double> previousData;
    Map<Stock, Double> currentData;
    String sheetName;

    public ReportData(Map<Stock, Double> previousData, Map<Stock, Double> currentData, String sheetName) {
        this.previousData = previousData;
        this.currentData = currentData;
        this.sheetName = sheetName;
    }

    public Map<Stock, Double> getPreviousData() {
        return previousData;
    }

    public Map<Stock, Double> getCurrentData() {
        return currentData;
    }

    public String getSheetName() {
        return sheetName;
    }
}
