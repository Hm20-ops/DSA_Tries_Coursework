/******************************************************************************

 File        : AutoCompletionTrieNode.java

 Date        : Monday 30th March 2020

 Author      : 100246776

 Description : Implementation of AutoCompletionTrieNode

 Last update : 18th May 2020

 ******************************************************************************/
package dsacoursework2;

public class AutoCompletionTrieNode {
    private boolean isKey;
    private final AutoCompletionTrieNode[] offspring;
    private int wordFrequency;

    /**
     * wordFrequency is only set when the node is a key
     */
    public AutoCompletionTrieNode()
    {
        this.offspring = new AutoCompletionTrieNode[26];
        this.wordFrequency = 0;
    }

    /**
     *
     * @return an array of offspring
     */
    public AutoCompletionTrieNode[] getOffSpring() {
        return offspring;
    }

    /**
     *
     * @return true if isKey is set to true. False otherwise
     */
    public boolean getIsKey() {
        return this.isKey;
    }

    /**
     *
     * @param bool, true when node needs to be a key. False otherwise.
     */
    public void setIsKey(boolean bool) {
        this.isKey = bool;
    }

    /**
     *
     * @param wordFrequency an integer that specifies the frequency of occurrence of word
     */
    public void setWordFrequency(int wordFrequency) {
        this.wordFrequency = wordFrequency;
    }


    public int getWordFrequency()
    {
        return wordFrequency;
    }
}