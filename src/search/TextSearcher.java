package search;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextSearcher {
	
	private String[] punctuationTokens;
	public String[] contentTokens;
	public HashMap<String, LinkedList<Integer>> wordToIndexMap;
	private HashMap<String, String[]> resultsCache = new HashMap<String, String[]>();

	/**
	 * Initializes the text searcher with the contents of a text file.
	 * The current implementation just reads the contents into a string 
	 * and passes them to #init().  You may modify this implementation if you need to.
	 * 
	 * @param f Input file.
	 * @throws IOException
	 */
	public TextSearcher(File f) throws IOException {
		FileReader r = new FileReader(f);
		StringWriter w = new StringWriter();
		char[] buf = new char[4096];
		int readCount;
		
		while ((readCount = r.read(buf)) > 0) {
			w.write(buf,0,readCount);
		}
		
		init(w.toString());
	}
	
	/**
	 *  Initializes any internal data structures that are needed for
	 *  this class to implement search efficiently.
	 */
	protected void init(String fileContents) {
		contentTokens = fileContents.split("[^'\\w]+"); // Gets string array of all words, as defined by problem statement
		punctuationTokens = fileContents.split("['\\w]+"); // Gets string array of all punctuation (non-words)
		
		wordToIndexMap = new HashMap<String, LinkedList<Integer>>();
		
		for(int i = 0; i < contentTokens.length; i++) {
			String currWord = contentTokens[i].toLowerCase();
			addToWordToIndexMap(currWord, i);		
		}
		
	}
	
	/**
	 * 
	 * @param queryWord The word to search for in the file contents.
	 * @param contextWords The number of words of context to provide on
	 *                     each side of the query word.
	 * @return One context string for each time the query word appears in the file.
	 */
	public String[] search(String queryWord,int contextWords) {
		queryWord = queryWord.toLowerCase();
		
		if(!wordToIndexMap.containsKey(queryWord)) {
			return new String[0];
		}
		
		if(resultsCache.containsKey(queryWord + "-" + contextWords)) {
			return resultsCache.get(queryWord + "-" + contextWords);
		}
		
		LinkedList<Integer> listOfIndexOccurences = wordToIndexMap.get(queryWord);
		String[] result = new String[listOfIndexOccurences.size()];
		
		int resultIndex = 0;
		for(int occurenceIndex : listOfIndexOccurences) {
			StringBuilder builder = new StringBuilder();
			int currIndex = occurenceIndex - contextWords;
			
			for(; currIndex <= occurenceIndex + contextWords; currIndex++) {
				if(currIndex < 0) {
					continue;
				}
				
				if(currIndex >= contentTokens.length) {
					break;
				}
				
				builder.append(contentTokens[currIndex]);
				if(currIndex != occurenceIndex + contextWords) {
					builder.append(punctuationTokens[currIndex + 1]);
				}
			}
			result[resultIndex++] = builder.toString();
		}
		
		resultsCache.put(queryWord + "-" + contextWords, result);
		return result;
	}

	private void addToWordToIndexMap(String word, int index) {
		if(wordToIndexMap.containsKey(word)) {
			LinkedList<Integer> list = wordToIndexMap.get(word);
			list.add(index);
		} else {
			LinkedList<Integer> list = new LinkedList<Integer>();
			list.add(index);
			wordToIndexMap.put(word, list);
		}
	}
}

// Any needed utility classes can just go in this file

