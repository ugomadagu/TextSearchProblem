## Instructions for importing
You should be able to import the code in the zip file to any IDE you choose, but I will explain how to do it with Eclipse since that is the IDE I used (and the one I suspect you will use as well).

1. Go to ```File -> Open projects from file system```.
2. Click ```Archive...``` and select the .zip file from this repo.
3. Select only the second folder ```TextSearchProblem.zip_expanded/TextSearchProblem```.
4. Hit ```Finish``` and the you should be good to test.

## Overview of solution
I will give a brief overview of my solution and why I think it is the most efficient solution based on the problem statement.

Since the problem statement specifically states

***You should assume that many searches will be done over the same document.   A single instance of the TextSearcher class will be created once, and then the search method will be called many times. You should build an implementation that is efficient for this usage pattern (the search method should be as fast as possible)***

my implementation was optimized for speed over memory. When the TextSearcher is first initilized, we make 3 passes over the file contents in order to set up our data structures. After this initial set up, searches are executed in O(x) time, where x is the contextWords number. If enough searches are done, or if the same things are searched for repeatedly, we go down to O(1) time.


### Data Structures
- **contentTokens** is a String array that contains all of the words (as a word is defined in the problem statement) inside of the file. All words are ordered according to where they appear in the file.
- **punctuationTokens** is a String array that contains all of the punctiation (all spaces and punctuation marks in-between words) in the file. All punctuation are ordered according to where they appear in the file.
- **wordToIndexMap** is a HashMap that maps a queryWord to a LinkedList that contains all of the indices where that queryWord appears in **contentTokens**.
- **resultsCache** is a HashMap that links a queryWord concatinated with its contextWords number to a String array in order to provide a cached answer if we have searched for that queryWord and contextWords number combination before.

### High Level Algorithm Explanation
- Set up
  * We use regular expressions to populate **contentTokens** and **punctuationTokens**. 
  * We loop through **contentTokens** once and for every word in **contentTokens**, we use **wordToIndexMap** to keep track of every index where that word was found.
  * Initialize **resultsCache** to an empty HashMap.
- Searching
  * Check if we have a mapping for the queryWord in **wordToIndexMap**. If not, return an empty array. If we do, proceed.
  * Check if we have a mapping for the queryWord concatinated with its contextWords number in **resultsCache**. If we do, return that cached result. If we do not, proceed.
  * Create **results** array of strings.
  * For every index occurenceIndex mapped to our queryWord in **wordToIndexMap**, we create a StringBuilder **builder**. 
    * For every index i that is between ```occurenceIndex - contextWords``` and ```occurenceIndex + contextWords``` and inbounds of the **contentTokens** array, builder appends contextWords[i] and punctuationTokens[i + 1]. This makes sense if we think of every word in the file as having an accompanying punctuation. The accompanying punctuation also always occurs after the word. We do not add the punctuation if we are working with the last word to be appened to **builder**.
    * Add **builder**.toString() to our **results** array.
  * Map the queryWord concatinated with its contextWords number to **results** in **resultsCache**.
  * Return **results**.

 
