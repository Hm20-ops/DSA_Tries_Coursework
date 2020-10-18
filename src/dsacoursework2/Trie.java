/******************************************************************************

 File        : Trie.java

 Date        : Wednesday 25th March 2020

 Author      : 100246776

 Description : Implementation of Trie, Solutions to part 2, Testing

 Last update : 18th May 2020

 ******************************************************************************/
package dsacoursework2;
import java.util.LinkedList;
import java.util.Queue;

public class Trie {
    private static final int ASCII_OFFSET = 97;
    private TrieNode root;

    public Trie() {
        root = new TrieNode();
    }//A trie is constructed with a root node

    public Trie(TrieNode trieNode) {
        root = trieNode;
    }

    /**
     * Adds a word to a trie if word not already a key in the trie.
     * @param word should be valid i.e not null or empty
     * @return True for added. False for not added
     */
    boolean add(String word) {
        boolean added = false;
        if (root == null || word==null || word.equals(""))
        {
            System.out.println("Some null reference");
            return added;
        }
        TrieNode parent = root;
        for (int i = 0; i < word.length(); i++) //for each character in the key
        {
            char character = word.charAt(i);//converts i to ascii equivalent
            int index = character - ASCII_OFFSET;//b being 98 and a being 97, then the index of b maps to 1(98-97)
            TrieNode nodeReference=parent.getOffspring()[index];
            if (nodeReference == null)//if no reference to any node
            {
                added = true;
                TrieNode temp = new TrieNode();//create a new node
                parent.getOffspring()[index] = temp; //add temp to this index position
                parent = temp;//set parent to the new node created
            } else {
                parent = nodeReference;//if node exists at particular index, go to it
            }
        }
        if (parent.getIsKey() && !added) {//if node is a key, return false for not added
            System.out.println(word+" already in trie");
            return added;//if node already in trie, return false for not added
        }
        parent.setIsKey(true);
        added=true;
        System.out.println(word+" added");
        return added;
    }

    /**
     * If word is a key in trie, return true. Otherwise return false.
     * @param word should be valid i.e not null and not empty
     * @return true for word in trie. False for word not in trie
     */
    public boolean contains(String word) {
        boolean contains=false;
        if (root == null  || word ==null || word.equals("")) {
            System.out.println("Some null reference");
            return contains;
        }
        TrieNode p = root;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            int index = c - ASCII_OFFSET;
            TrieNode[] children=p.getOffspring();
            TrieNode n=children[index];
            if (n== null) {
                /*
                Say, we look for word cat in trie. In the offspring array of root, there is no reference at index 2
                representing character 'c', this means word 'cat' is not present in trie. Return false.
                Same applies for trie nodes further down the tree
                 */
                System.out.println(word+ " is not in trie");
                return contains;
            }
            p = n;//if a reference corresponding to the index is found, go to it.
        }
        /*
        Return true only when word is a key
         */
        if (p.getIsKey()) {
            System.out.println(word+ " is in trie");
            contains= true;
            return contains;
        }
        /*
        Say,we look for word 'cat'.The characters 'c','a','t' are in the trie. However, node that holds 't' is not a
        key. This means 'cat' in the trie is part of another word that is a key. For e.g catalogue. In this case, return
        false
         */
        System.out.println(word+ " is not in trie");
        return contains;//trie does not contain the key
    }

    /**
     * Goes through trie and outputs each character in breadth first search order
     * @return A string containing characters in that order
     * Design decision:
     * Use a Queue because it respects first-in first-out rule. This is useful for breadth first search.
     * Underlying implementation of queue is a linkedlist. This ensures ensure O(1) for enqueue and dequeue.
     */
    public String outputBreadthFirstSearch() {
        if (root == null)
            return null;
        StringBuilder sb = new StringBuilder();
        Queue<TrieNode> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            TrieNode temp = queue.remove();//remove oldest node added
            TrieNode[] c=temp.getOffspring();
            for (int i = 0; i < c.length; i++) {
                if (c[i] != null) {
                    queue.add(c[i]);//add offspring of node to the queue so it goes through them in breadth first order
                    sb.append((char) (i + ASCII_OFFSET));//index of offspring reference corresponds to character
                }
            }
        }
        System.out.println(sb.toString());//testing purposes
        return sb.toString();//toString method returns a string object
    }

    /**
     * Interface method to outputDepthFirstSearch(StringBuilder sb, TrieNode trieNode)
     * @return A string of characters in depth first search order
     */
    public String outputDepthFirstSearch()
    {
        StringBuilder sb=new StringBuilder();
        String result=outputDepthFirstSearch(sb,root);
        return result;
    }

    /**
     * Goes through trie and outputs character in depth first search order
     * @param sb An empty stringbuilder to store characters
     * @param trieNode Start point of depth first Search. Can specify any node as returned by getSubTrie(String prefix)
     * @return A string of characters in depth first search order
     * Design decision:
     * Recursion as opposed to a iterative version provides a more elegant and shorter solution
     * No need to handle stack data structure
     */
    private String outputDepthFirstSearch(StringBuilder sb, TrieNode trieNode) {
        if (trieNode == null) {//base case for non=existing trie
            return null;
        }
        TrieNode[] children = trieNode.getOffspring();
        for (int i = 0; i < children.length; i++) {
            if (children[i] != null)//ignore nulls
            {
                sb.append((char) (i + ASCII_OFFSET));//convert index to char
                outputDepthFirstSearch(sb, children[i]);//recursive call on node's children- depth first
            }
        }
        return sb.toString();
    }

    /**
     * @param prefix, an incomplete word
     * @return A Trie rooted at prefix
     */
    public Trie getSubTrie(String prefix) {
        int index;
        if (root == null || prefix==null || prefix.equals("")) {
            return null;
        }
        TrieNode parent = root;
        for (int i = 0; i < prefix.length(); i++) {
            index = prefix.charAt(i) - ASCII_OFFSET;
            if (parent.getOffspring()[index] == null) {//prefix does not exist
                return null;
            }
            parent = parent.getOffspring()[index];//if node is not null, go to it
        }
        return new Trie(parent);//return a trie rooted at the last node assigned to parent
    }

    /**
     * Interface method to getAllWords(LinkedList listOfWords, StringBuilder sb, TrieNode root)
     * @return A LinkedList containing all keys in the trie
     * Design decision:
     */
    public LinkedList<String> getAllWords()//interface method for private getAllWords method
    {
        if(root==null){
            return null;
        }
        LinkedList<String> listOfWords = new LinkedList<>();
        getAllWords(listOfWords, new StringBuilder(), root);
        if(listOfWords.size()==0){
            return null;
        }
        return listOfWords;
    }

    /**
     * Populates a LinkedList with keys from the trie.
     * @param listOfWords A linkedlist
     * @param sb An empty StringBuilder
     * @param root Root node of Trie
     * Design decision:
     *             A LinkedList maintains O(1) for insertion. It is only added to.
     *             A LinkedList has better space-time complexity than using an ArrayList
     *             For e.g For a large trie, using an arraylist that doubles in size when it runs out of space
     *             is wasteful
     */
    private void getAllWords(LinkedList listOfWords, StringBuilder sb, TrieNode root) {
        if (root.getIsKey()) {
            System.out.println(sb.toString());//for test purposes
            listOfWords.add(sb.toString());//if key is found, add to listOfWords
        }
        TrieNode[] offspring = root.getOffspring();
        for (int i = 0; i < offspring.length; i++) {
            TrieNode o=offspring[i];
            if (o != null) {
                getAllWords(listOfWords, sb.append((char) (ASCII_OFFSET + i)), o);
                /*
                Say, the key 'bat'is added to stringBuilder, there might still be other keys to find such as bass
                Once function returns, we continue adding to the string "ba" if there any other keys.
                 */
                sb.setLength(sb.length() - 1);//backtracks one level
            }
        }
    }


    public static void main(String[] args) {
        Trie trie = new Trie();

        System.out.println("Testing add(key)...");
        if (!trie.add("bat")) throw new AssertionError();
        if (!trie.add("cat")) throw new AssertionError();
        if (!trie.add("chat")) throw new AssertionError();
        if (!trie.add("cheese")) throw new AssertionError();
        if (!trie.add("cheers")) throw new AssertionError();
        if (!trie.add("cheer")) throw new AssertionError();
        if (trie.add("bat")) throw new AssertionError();
        if (trie.add("cat")) throw new AssertionError();
        if (trie.add("chat")) throw new AssertionError();
        if (trie.add("cheese")) throw new AssertionError();
        if (trie.add("cheers")) throw new AssertionError();
        if (trie.add(null)) throw new AssertionError();
        if (trie.add("")) throw new AssertionError();

        System.out.println("\nTesting contains(key)...");
        if (!trie.contains("bat")) throw new AssertionError();
        if (!trie.contains("cat")) throw new AssertionError();
        if (!trie.contains("cat")) throw new AssertionError();
        if (!trie.contains("cheer")) throw new AssertionError();
        if (!trie.contains("cheers")) throw new AssertionError();
        if (trie.contains("coronavirus")) throw new AssertionError();
        if (trie.contains("china")) throw new AssertionError();
        if (trie.contains("ch")) throw new AssertionError();
        if (trie.contains("")) throw new AssertionError();
        if (trie.contains(null)) throw new AssertionError();

        System.out.println("\nTesting outputBreadthFirstSearch()");
        if(!trie.outputBreadthFirstSearch().equals("bcaahttaetersse")) throw new AssertionError();

        System.out.println("\nTesting outputDepthFirstSearch()");
        System.out.println(trie.outputDepthFirstSearch());
        if(!trie.outputDepthFirstSearch().equals("batcathateersse")) throw new AssertionError();

        System.out.println("\nTesting getAllWords()");
        trie.getAllWords();
        System.out.println("Test passed. If adding more keys, check if getAllWords() finds all keys");

        System.out.println("\nTesting getSubTrie(String prefix)");
        Trie subtrie=trie.getSubTrie("ch");
        subtrie.getAllWords();
        System.out.println("Test passed. If adding more keys, check if getAllWords() finds all keys");

        System.out.println("\nTesting getSubTrie(String prefix)");
        Trie subtrie1=trie.getSubTrie("coron");
        if(subtrie1==null){
            System.out.println("Invalid prefix");
        }

    }
}
