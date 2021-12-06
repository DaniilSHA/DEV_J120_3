public interface IFileConverter {
    String toBinary (String inputFileName, String outputFileName, String charSet);
    String toText (String inputFileName, String outputFileName, String charSet);
    double getSum(String fileName) throws ConverterException;
}
