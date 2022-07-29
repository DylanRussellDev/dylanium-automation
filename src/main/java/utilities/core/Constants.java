package utilities.core;

public class Constants {

    private static final ReadConfigFile readFile = new ReadConfigFile();

    public static final Long TIMEOUT = Long.parseLong(readFile.properties.getProperty("Timeout"));

}
