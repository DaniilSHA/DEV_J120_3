import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class DataConverter implements IFileConverter {

    @Override
    public String toBinary(String inputFileName, String outputFileName, String charSet) {
        try (BufferedReader input = new BufferedReader(new FileReader(inputFileName));
             FileWriter output = new FileWriter(outputFileName)) {

            ArrayList<String> lines = new ArrayList<>();
            while (input.ready())
                lines.add(input.readLine());

            for (String str : lines) {
                String[] bufferStr = str.split(" ");
                for (int i = 0; i < bufferStr.length; i++) {
                    if (bufferStr[i].matches("[-\\d]\\d*.\\d+")) {
                        double bufferDouble = Double.parseDouble(bufferStr[i]);
                        long doubleBits = Double.doubleToLongBits(bufferDouble);
                        String doubleBitsStr = String.format("%64s", Long.toBinaryString(doubleBits)).replace(" ", "0");
                        output.write(doubleBitsStr + (i == bufferStr.length - 1 ? "\n" :
                                "111" + String.format("%8s", Integer.toBinaryString(' ')).replace(" ", "0") + "111"));
                    } else if (bufferStr[i].matches("[-\\d]+.\\d+[eE][-\\d]\\d+")) {
                        double bufferDouble = Double.parseDouble(bufferStr[i]);
                        long doubleBits = Double.doubleToLongBits(bufferDouble);
                        String doubleBitsStr = String.format("%64s", Long.toBinaryString(doubleBits)).replace(" ", "0");
                        output.write(doubleBitsStr + (i == bufferStr.length - 1 ? "\n" :
                                "111" + String.format("%8s", Integer.toBinaryString(' ')).replace(" ", "0") + "111"));
                    } else {
                        byte[] word = bufferStr[i].getBytes(charSet);
                        String temp = "";
                        for (int j = 0; j < word.length; j++)
                            temp += String.format("%8s", Integer.toBinaryString(word[j])).replace(" ", "0");
                        temp += "1";
                        output.write(temp + (i == bufferStr.length - 1 ? "\n" :
                                "111" + String.format("%8s", Integer.toBinaryString(' ')).replace(" ", "0") + "111"));
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputFileName;
    }

    @Override
    public String toText(String inputFileName, String outputFileName, String charSet) {
        try (BufferedReader input = new BufferedReader(new FileReader(inputFileName));
             FileWriter output = new FileWriter(outputFileName)) {

            ArrayList<String> lines = new ArrayList<>();
            while (input.ready())
                lines.add(input.readLine());

            for (String str : lines) {
                String[] bufferStr = str.split("111" + String.format("%8s", Integer.toBinaryString(' ')).replace(" ", "0") + "111");

                ArrayList<Byte> bufferCharsInBytes = new ArrayList<>();
                for (int i = 0; i < bufferStr.length; i++) {
                    if (bufferStr[i].length() % 2 != 0) {
                        for (int j = 0; j < bufferStr[i].length() - 1; j += 8) {
                            bufferCharsInBytes.add(Byte.parseByte(bufferStr[i].substring(j, j + 8), 2));
                        }
                        byte[] fullWord = new byte[bufferCharsInBytes.size()];
                        for (int j = 0; j < bufferCharsInBytes.size(); j++)
                            fullWord[j] = bufferCharsInBytes.get(j);
                        bufferCharsInBytes.clear();
                        output.write(new String(fullWord, charSet) + (i == bufferStr.length - 1 ? "\n" : " "));
                    } else {
                        long doubleBits = Long.parseLong(bufferStr[i].substring(1), 2);
                        double tempDouble = Double.longBitsToDouble(doubleBits);
                        String outputDouble = bufferStr[i].charAt(0) == '1' ? "-" + tempDouble : "" + tempDouble;
                        output.write(outputDouble + (i == bufferStr.length - 1 ? "\n" : " "));
                    }
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputFileName;
    }

    @Override
    public double getSum(String fileName) throws ConverterException {
        if (fileName == null) throw new ConverterException("имя файла - null");
        double sum = 0;

        try (BufferedReader input = new BufferedReader(new FileReader(fileName))) {
            if (!input.ready()) throw new ConverterException("фаил пуст");

            ArrayList<String> lines = new ArrayList<>();
            while (input.ready())
                lines.add(input.readLine());

            for (String str : lines) {
                String[] bufferStr = str.split(" ");

                for (int i = 0; i < bufferStr.length; i++) {
                    if (bufferStr[i].matches("[-\\d]\\d*.\\d+")) {
                        sum += Double.parseDouble(bufferStr[i]);
                    } else if (bufferStr[i].matches("[-\\d]+.\\d+[eE][-\\d]\\d+")) {
                        sum += Double.parseDouble(bufferStr[i]);
                    }
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sum;
    }
}
