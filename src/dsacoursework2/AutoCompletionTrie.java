/******************************************************************************

 File        : AutoCompletionTrie.java

 Date        : Wednesday 18th March 2020

 Author      : 100246776

 Description : Solutions for part 3

 Last update : 18th May 2020

 ******************************************************************************/
package dsacoursework2;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

public class AutoCompletionTrie {
    private static final int ASCII_OFFSET = 97;
    private AutoCompletionTrieNode root;

    public AutoCompletionTrie() {
        root = new AutoCompletionTrieNode();
    }

    public AutoCompletionTrie(AutoCompletionTrieNode trieNode) {
        root = trieNode;
    }

    /**
     * Extracts word-frequency pairs for each line of a CSV file and call add method to add to AutoCompletionTrie
     * @param dictionary, a CSV file with each line of the format word,frequency
     * @throws IOException
     */
    public void addDictionaryToTrie(String dictionary) throws IOException {
        BufferedReader csvReader = new BufferedReader(new FileReader(dictionary));
        String line;
        String[] dictionaryEntry;
        while ((line = csvReader.readLine()) != null) {//reads line by line until end of document
            try {
                /*
                E.g. Alive,2 becomes dictionaryEntry[0]="Alive", dictionaryEntry[1]=2
                 */
                dictionaryEntry = line.split(",");
                String word = dictionaryEntry[0];
                int wordFrequency = Integer.parseInt(dictionaryEntry[1]);
                boolean isAdded = this.add(word, wordFrequency);//add word and frequency to trie
                if (isAdded) {
                    System.out.printf(" %s added to trie\n", dictionaryEntry[0]);//test purposes
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        csvReader.close();
    }


    /**
     * If word is a key , update its wordFrequency. Otherwise, add the word and wordFrequency to the trie
     * @param word
     * @param wordFrequency
     * @return True for added and false for not added
     */
    public boolean add(String word, int wordFrequency) {
        boolean added = false;
        if (root == null || word == null) {
            return added;
        }
        AutoCompletionTrieNode p = root; //the root
        for (int i = 0; i < word.length(); i++) //for each character in the key
        {
            char c = word.charAt(i);//converts i to char
            int index = c - ASCII_OFFSET;//b being 98 and a being 97, then the index of b maps to 1(98-97)
            AutoCompletionTrieNode[] offSpring = p.getOffSpring();
            AutoCompletionTrieNode n = offSpring[index];
            if (n == null)//if no reference to any node
            {
                added = true;
                AutoCompletionTrieNode temp = new AutoCompletionTrieNode();//create a new node
                p.getOffSpring()[index] = temp;
                p = temp;//set p to the new node created
            } else {
                p = n;//if node exists at particular index, go to it
            }
        }
        if (p.getIsKey() && !added) {
            //if word is a key, update its frequency of occurrence
            int newFrequency = wordFrequency + p.getWordFrequency();
            p.setWordFrequency(newFrequency);
            System.out.printf("%s is a key in trie. Frequency is now %d\n", word, newFrequency);
            return added;//added is still false. No new key added
        }
        p.setIsKey(true);
        added = true;
        p.setWordFrequency(wordFrequency);//set frequency of word when it is a key
        return added;
    }


    public boolean contains(String key) {
        boolean contains = false;
        if (root == null || key == null || key.equals("")) {
            System.out.println("Some null reference");
            return contains;
        }
        AutoCompletionTrieNode p = root;//starts at the root
        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);//converts i to char
            int index = c - ASCII_OFFSET;//b being 98 and a being 97, then the index of b maps to 1(98-97)
            AutoCompletionTrieNode[] children = p.getOffSpring();
            AutoCompletionTrieNode n = children[index];
            if (n == null) {//if a index that maps to a character is not found, key is not in trie
                /*
                Say, we look for word cat in trie. In the offspring array of root, there is no reference at index 2
                representing character 'c', this means word 'cat' is not present in trie. Return false.
                Same applies for trie nodes further down the tree
                 */
                System.out.println(key + " is not in trie");
                return contains;
            }
            p = n;//if index exists, go to the node
        }
        /*
        Return true only when word is a key
         */
        if (p.getIsKey()) {
            System.out.println(key + " is in trie");
            contains = true;
            return contains;
        }
        /*
        Say,we look for word 'cat'.The characters 'c','a','t' are in the trie. However, node that holds 't' is not a
        key. This means 'cat' in the trie is part of another word that is a key. For e.g catalogue. In this case, return
        false
         */
        System.out.println(key + " is not in trie");
        return contains;//trie does not contain the key
    }


    public String outputBreadthFirstSearch() {
        if (root == null)
            return null;
        StringBuilder sb = new StringBuilder();
        Queue<AutoCompletionTrieNode> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            AutoCompletionTrieNode temp = queue.remove();//remove oldest node added
            AutoCompletionTrieNode[] c = temp.getOffSpring();
            for (int i = 0; i < c.length; i++) {
                if (c[i] != null) {
                    queue.add(c[i]);//add offspring of node to the queue so it goes through them in breadth first order
                    sb.append((char) (i + ASCII_OFFSET));//index of offspring corresponds to character
                }
            }
        }
        System.out.println(sb.toString());//for testing purposes
        return sb.toString();
    }

    public String outputDepthFirstSearch()//interface function
    {
        StringBuilder sb = new StringBuilder();
        String result = outputDepthFirstSearch(sb, root);
        return result;
    }


    private String outputDepthFirstSearch(StringBuilder sb, AutoCompletionTrieNode trieNode) {
        if (trieNode == null) {//base case for non=existing trie
            return null;
        }
        AutoCompletionTrieNode[] children = trieNode.getOffSpring();
        for (int i = 0; i < children.length; i++) {
            if (children[i] != null)//ignore nulls
            {
                sb.append((char) (i + ASCII_OFFSET));//convert index to char(0 maps to 'a')
                outputDepthFirstSearch(sb, children[i]);//recursive call on node's children- depth first
            }
        }
        return sb.toString();
    }


    public AutoCompletionTrie getSubTrie(String prefix) {
        int index;
        if (root == null || prefix == null || prefix.equals("")) {
            return null;
        }
        AutoCompletionTrieNode p = root;
        for (int i = 0; i < prefix.length(); i++) {
            index = prefix.charAt(i) - ASCII_OFFSET;
            if (p.getOffSpring()[index] == null) {//prefix does not exist
                return null;
            }
            p = p.getOffSpring()[index];
        }
        return new AutoCompletionTrie(p);
    }


    private void getAllWords(LinkedList listOfWords, StringBuilder sb, AutoCompletionTrieNode root) {
        if (root.getIsKey()) {
            System.out.println(sb.toString());
            listOfWords.add(sb.toString());
        }
        AutoCompletionTrieNode[] children = root.getOffSpring();
        for (int i = 0; i < children.length; i++) {
            if (children[i] != null) {
                getAllWords(listOfWords, sb.append((char) (ASCII_OFFSET + i)), children[i]);
                /*
                Say, the key 'bat'is added to stringBuilder, there might still be other keys to find such as bass
                Once function returns, we continue adding to the string "ba" if there any other keys.
                 */
                sb.setLength(sb.length() - 1);//backtracks one level
            }
        }
    }

    public LinkedList<String> getAllWords()
    {
        LinkedList<String> listOfWords = new LinkedList<>();
        getAllWords(listOfWords, new StringBuilder(), root);
        return listOfWords;
    }


    public static class WordAndFrequency {
        /*
        Objects of WordAndFrequency hold a word string and frequency of that key
         */
        private String key;
        private int freq;

        WordAndFrequency(String key, int freq)
        {
            this.key = key;
            this.freq = freq;
        }
        WordAndFrequency(String key, String freq) {
            this.key = key;
            this.freq = Integer.parseInt(freq);
        }

        int getFreq() {
            return freq;
        }

        String getKey() {
            return key;
        }
    }

    /*
    Design decision for priority queue:
    1. Ability to specify priority of an element. As such, no need to sort
    2. Adding to priority queue is log(n) which is fairly fast
    3. Poll() is O(1) time complexity
    4. Better space complexity than using an arraylist that doubles in size to accommodate more elements
    5. Priority queue guarantees maintaining order for highest priority value. It does not necessarily maintain sorted order.
       As we only ever use it to find highest priorities, it is better to use it instead of a treeset,
        which maintains sorted order throughout
       It is also more computationally expensive to maintain trees
     */
    /**
     * An interface function
     * @return A queue, q, containing WordAndFrequency objects
     */
    private PriorityQueue<WordAndFrequency> wordFrequencyInfo() {
        //Each WordAndFrequency object has a key and its frequency
        PriorityQueue<WordAndFrequency> q = new PriorityQueue<>(new CompareByFreqAndAlphabet());
        wordFrequencyInfo(q, new StringBuilder(), root);//get all words in the trie and their associated frequency
        return q;
    }

    /**
     * Comparator logic for priority queue
     */
    static class CompareByFreqAndAlphabet implements Comparator<WordAndFrequency> {
        public int compare(WordAndFrequency m1, WordAndFrequency m2) {
            if (m1.getFreq() < m2.getFreq()) {
                return 1;
            } else if (m1.getFreq() > m2.getFreq()) {
                return -1;
            } else {
                return m1.getKey().compareTo(m2.getKey());//if same frequency, sort by key length i.e choose shorter word
            }
        }
    }


    /**
     * The algorithm goes through the AutoCompletionTrie recursively.
     * When it encounters a node that is a key, it means that the stringbuilder contains a complete word.
     * A WordAndFrequency entity is then made and added to the priority queue.
     * @param queue, an empty priority queue
     * @param sb, an empty stringbuilder
     * @param root, starting node in an AutoCompletionTrie
     */
    private void wordFrequencyInfo(PriorityQueue queue, StringBuilder sb, AutoCompletionTrieNode root) {
        if (root.getIsKey()) {
            queue.add(new WordAndFrequency(sb.toString(), root.getWordFrequency()));
        }
        AutoCompletionTrieNode[] offspring = root.getOffSpring();
        for (int i = 0; i < offspring.length; i++) {
            if (offspring[i] != null) {
                wordFrequencyInfo(queue, sb.append((char) (97 + i)), offspring[i]);
                sb.setLength(sb.length() - 1);//backtracks one level
            }
        }
    }

    /**
     * This function finds the most probable matches for each prefix in the list
     * Writes matches to a CSV file.
     * @param prefixes, an ArrayList of prefixes
     * @throws IOException
     */
    public void prefixMatches(ArrayList<String> prefixes) throws IOException {
        final DecimalFormat df = new DecimalFormat("#.######");
        ArrayList<String> toWrite=new ArrayList<>();
        for (String prefix : prefixes) {
            toWrite.add(prefix);
            AutoCompletionTrie currentSubTrie = this.getSubTrie(prefix);//get subtrie rooted at prefix
            /*
            temp contains a WordAndFrequency objects. The object contains a unique word and frequency of occurrence
             */
            PriorityQueue<WordAndFrequency> temp = currentSubTrie.wordFrequencyInfo();
            int totalFreq = 0;
            for (WordAndFrequency wordAndFrequency : temp) {
                /*
                adding frequency attribute of each object to find totalFrequency
                 */
                totalFreq += wordAndFrequency.getFreq();
            }

            int limit = Math.min(temp.size(), 3);

            for (int i = 0; i < limit; i++)
            {
                WordAndFrequency wf=temp.poll();//removes and return top most object in priority queue
                if(wf !=null){
                    toWrite.add(prefix+wf.getKey());
                    toWrite.add(df.format((double)wf.getFreq()/totalFreq));
                }
            }
            saveToFile("lotrMatches.csv", toWrite);
            toWrite.clear();
        }
    }

    /**
     * Writes ArrayList to a file in a specific format
     * @param writeFile, name of file to write to
     * @param toWrite, an Arraylist to write to file
     * @throws IOException
     */
    private static void saveToFile(String writeFile, ArrayList<String> toWrite) throws IOException
    {
        BufferedWriter writer = new BufferedWriter(
                new FileWriter(writeFile, true));  //with append set to true, file data is not overwritten
        for(int i=0;i<toWrite.size();i++)
        {
            if(i != toWrite.size()-1)
            {
                writer.write(toWrite.get(i)+",");
            }
            else {
                writer.write(toWrite.get(i)+System.lineSeparator());
            }
        }
        writer.close();
    }


    public static void main(String[] args) throws IOException {

        AutoCompletionTrie trie = new AutoCompletionTrie();
        DictionaryMaker df = new DictionaryMaker();
        /*
        Writes lotr as a dictionary in csv format
         */
        df.formDictionary("lotr.csv", "dictionaryOfWords.csv");

        trie.addDictionaryToTrie("dictionaryOfWords.csv");
        /*
        Load queries into an ArrayList
         */
        ArrayList<String> lotrQueries = DictionaryMaker.readWordsFromCSV("lotrQueries.csv", "\n");

        trie.prefixMatches(lotrQueries);

    }
}
