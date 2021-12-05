public class Main {
    public static void main(String[] args) {


        DataConverter converter = new DataConverter();

        System.out.println(converter.toBinary("inputEx.txt", "outputEx.txt", "UTF-8"));

        System.out.println(converter.toText("outputEx.txt", "outputToInput.txt", "UTF-8"));

        try {
            System.out.println(converter.getSum("outputToInput.txt"));
        } catch (ConverterException e) {
            e.printStackTrace();
        }

    }
}
