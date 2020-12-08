package com.chaosopher.tigerlang.compiler.regalloc;

import com.chaosopher.tigerlang.compiler.temp.TempList;


public class BitSet {

    private static final int ALL_ONES = 0xFFFFFFFF;
    private static final int WORD_SIZE = 32;
    /**
     * The internal field that stores all the bits.
     */
    private int words[] = null;
    private int capacity;

    public BitSet() {
        //initWords()
    }

    public BitSet(int size) {
        if(size == 0)
        throw new Error("size cannot be zero");
        capacity = size;
        //calculates the number of ints we need to store size bits,
        //where each int is 1 word in length
        //eg size = 10
        //we need 1 int for this
        //new int[1] => X,X,X,X,X,X,X,X,X,X.....0  32 bits
        //eg size = 32
        //we need 1 int for this
        //new int[1] => X,X,X,X,X,X,X,X,X,X.....0  32 bits
        //eg size = 33
        //we need 2 ints for this
        //we could use a ceiling function for this, however I suspect
        //this is quicker, get number of whole divisions and add
        //1 if there is a remainder from the division
        words = new int[size / WORD_SIZE + (size % WORD_SIZE == 0 ? 0 : 1)];
    }

    BitSet(int[] bits) {
    }

    public boolean get(int pos) {
        //get a specific bit using pos
        //first find which int we need to bit shift
        //we can find that from division, returns a word, or 32 bits
        //bitwise and those 32 bits with [0,...1] left shifted by the remainder
        //which is the offset within the int, so shift 0,0,0....1 by the offset
        //this basically moves the 1 in the second int to the position we are
        //interested in. We then & both, which returns 1 if the bit array has a 1
        //or a zero where it doesn't
        return (words[pos / WORD_SIZE] & (1 << (pos % WORD_SIZE))) != 0;
    }

    public void set(int pos, boolean b) {
        //again we find which word we need to bit shift
        int word = words[pos / WORD_SIZE];
        //create a bit mask by shifting the first bit to the
        //bit position we are interested in. [ 0,0,1,0,0]
        int posBit = 1 << (pos % WORD_SIZE);
        //if we want to set our bit to true
        if (b) {
            //or the bit with the word, 
            //word = word | posBit, if position is 0
            //its changed to 1, if its 1 it stays as 1
            word |= posBit;
        } else {
            //we want to change the bit to zero
            //ALL_ONES - posBit flips the bits in our mask
            //This means the item we are interested in is now 0 
            //in the mask
            //word = word & (ALL_ONES - posBit )
            //if position is 0 it stays as 0 ( 0 & 0 = 0)
            //if position is 1 it changes to 0 ( 0 & 1 = 0)
            word &= (ALL_ONES - posBit);
        }
        words[pos / WORD_SIZE] = word;
    }

    public BitSet union(BitSet bitArraySet) {
        int[] union = new int[this.words.length];
        for(var i = 0; i < union.length; i++){
            union[i] = this.words[i] | bitArraySet.words[i];
        }
        //return new BitSet(capacity, union);
        throw new Error();
    }

    public BitSet difference(BitSet bitArraySet) {
        int[] diff = new int[this.words.length];
        for(var i = 0; i < diff.length; i++){
            diff[i] = this.words[i] & ~bitArraySet.words[i];
        }
        throw new Error();
    }

    public BitSet intersection(BitSet bitArraySet) {
        int[] inter = new int[this.words.length];
        for(var i = 0; i < inter.length; i++){
            inter[i] = this.words[i] & bitArraySet.words[i];
        }
        throw new Error();
    }

    public boolean equals(BitSet other) {
        throw new Error();
    }

    public String toString(){
        throw new Error();
    }
}
