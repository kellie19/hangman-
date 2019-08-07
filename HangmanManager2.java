// Kellie Gui
// CSE 143 BF with Chrish Thakalath
// Homework 4 bonus Evil Hangman
// This program extends HangmanManager 
// and it improves two flaws in the original HangmanManager
// One improvement is that the program wins immediately
// when the user is down to one guess
// The second improvement is that the words and guesses methods
// cannot be changed by the client

import java.util.*;

public class HangmanManager2 extends HangmanManager {
   private Set<String> currWord;
   private Set<Character> alreadyGu;
   private int left;// these three fields have the same function with the original ones
   private Set<String> oldS; // an unmodifiable string set of words to guess
   private Set<Character> oldC; // an unmodifiable character set of characters already guessed
   
   // This constructor works as the original one and also initializes oldS and oldC 
   // to prevent client to make changes.
   public HangmanManager2(Collection<String> dictionary, int length, int max) {
      super(dictionary, length, max);
      left = max;
      currWord = new TreeSet<String>();
      for(String s : dictionary) {
         if(s.length() == length) {
            currWord.add(s);
         }
      }
      alreadyGu = new TreeSet<Character>();
      oldS = Collections.unmodifiableSet(currWord);
      oldC = Collections.unmodifiableSet(alreadyGu);
   }
   
   // return the times of guessing left and allows modification of left in this class
   public int guessesLeft() {
      return left;
   }
   
   // The improvement of the record method:
   // when there is only one guessing time left,
   // the program goes through the set of words 
   // and finds the word that does not contain the letter.
   // If the word cannot be found or the guessing time left is more than 1,
   // the record method will implement the original one.
   // Otherwise, the current set of words will be cleared and use that word as the answer,
   // and as a result the return record will be 0.
   // In this way, the user will lose no matter what letter input. 
   public int record(char guess) {
      left = super.guessesLeft();
      if(left != 1 || lastWord(guess) == null) {
         return super.record(guess);
      } else {
         currWord = super.words();
         String lastWord = lastWord(guess);
         currWord.clear();
         currWord.add(lastWord);
         left = 0;
         alreadyGu = super.guesses();
         alreadyGu.add(guess);
         return 0;
      }
   }
   
   // a helper method to returns the word in the set that does not contain the letter
   // returns null otherwise
   private String lastWord(char guess){
      for(String s : currWord) {
         if(! s.contains(guess+"")) {
            return s;
         }
      }
      return null;
   }
   
   // The improved words method returns a set of current words that clients cannot modify it 
   public Set<String> words(Set<String> oldS) {
      if(oldS != currWord) {
         oldS = Collections.unmodifiableSet(currWord);
      }
      return oldS;
   }
   
   // The improved guesses method returns a set of characters that are already guessed 
   // and clients cannot change it
   public Set<Character> guesses(Set<Character> oldC) {
      if(oldC != alreadyGu) {
         oldC = Collections.unmodifiableSet(alreadyGu);
      }
      return oldC;
   }
   
}