/******************************************************************************

 File        : DictionaryMaker.java

 Date        : Wednesday 18th March 2020

 Author      : 100246776

 Description : Provide implementation for formDictionary and saveToFile in part 1,

 Last update : 18th May 2020

 ******************************************************************************/

package dsacoursework2;
import java.io.*;
import java.util.*;
public class DictionaryMaker {
    /**
     * Reads all the words in a comma separated text document into an Array
     * Tony's comment
     */
    public static ArrayList<String> readWordsFromCSV(String file, String delimiter) throws FileNotFoundException {
        Scanner sc = new Scanner(new File(file));
        sc.useDelimiter(delimiter);
        ArrayList<String> words = new ArrayList<>();
        String str;
        while (sc.hasNext()) {
            str = sc.next();
            str = str.trim();
            str = str.toLowerCase();
            words.add(str);
        }
        return words;
    }

    /**
     * Forms a dictionary of word,frequency tuples. A word should be unique
     * @param readFile, the file to be read
     * @param writeFile, the file to be written to
     * @throws IOException
     */
    public void formDictionary(String readFile, String writeFile) throws IOException {

        ArrayList<String> listOfWords = readWordsFromCSV(readFile, ",");
        TreeMap<String, Integer> frequencyDictionary = new TreeMap<>();//treeMap maintains sorted order
        for (String s : listOfWords) {
            if (!frequencyDictionary.containsKey(s)) {//if treemap does not contain the key
                frequencyDictionary.put(s, 1);//if key not already in map, set frequency to 1
            } else {
                int frequency=frequencyDictionary.get(s) + 1;
                frequencyDictionary.put(s,frequency);//if key already in map, increase its frequency by 1
            }
        }
        System.out.println(frequencyDictionary);//testing purposes
        saveToFile(frequencyDictionary, writeFile);
        System.out.println("file saved");
    }

    /**
     * Writes a map to a CSV file
     * @param frequencyDictionary, a map of word,frequency pair from formDictionary
     * @param writeFile, an empty CSV file to write to
     * @throws IOException
     */
    public static void saveToFile(TreeMap<String, Integer> frequencyDictionary, String writeFile) throws IOException {
        FileWriter fileWriter = new FileWriter(writeFile);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        for (Map.Entry<String, Integer> entry : frequencyDictionary.entrySet())//loop through TreeMap
        {
            printWriter.println(entry.getKey() + "," + entry.getValue());
        }
        printWriter.close();
    }

    public static void main(String[] args) throws Exception {
        DictionaryMaker df = new DictionaryMaker();
        /*
        Testing done by comparing test.csv to testDictionary, provided in zip file
         */
        df.formDictionary("testDocument.csv", "test.csv");
    }
}
