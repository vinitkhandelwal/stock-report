package processor;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;
import java.util.Map;

public interface FundReader {

    public Map<String, Map<Stock,Double>> fundReader(String filePath) throws IOException, InvalidFormatException;
}
