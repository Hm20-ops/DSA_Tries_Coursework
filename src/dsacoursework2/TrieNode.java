/******************************************************************************

 File        : TrieNode.java

 Date        : Wednesday 18th March 2020

 Author      : 100246776

 Description : Implementation of TrieNode

 Last update : 18th May 2020

 ******************************************************************************/
package dsacoursework2;

public class TrieNode {
    private boolean isKey;//isKey=true if node completes a word
    private final TrieNode[] offspring;

    public TrieNode() {
        this.offspring = new TrieNode[26];
    }//An array to hold the references to offspring

    /**
     *
     * @return an array of reference to children nodes
     */
    public TrieNode[] getOffspring() {
        return offspring;
    }

    /**
     *
     * @return either true of false if node is a key
     */
    public boolean getIsKey() {
        return this.isKey;
    }

    /**
     * Setter method to set isKey to true or false
     * @param bool
     */
    public void setIsKey(boolean bool) {
        this.isKey = bool;
    }//set isKey attribute of the trie node
}
