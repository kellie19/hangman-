// Kellie Gui
// CSE 143 BF with Chrish Thakalath
// Homework 4 Evil Hangman 
// HangmanManager is a game adminsitrator to manage a game of hangman.
// In this hangman game, the program did not pick a word at first.
// Instead, it chooses a set of words that has the same pattern every time.
// It picks the set that has most words in it,
// and decides if the input character is in the set. 
// The computer does not pick a word until the set has only one word in it.


import java.util.*;

public class HangmanManager {
   private Set<String> currWord; // a group of words currently for guess
   private Set<Character> alreadyGu; // characters are already guessed
   private String pattern; // the pattern of words in common 
   private int left; // times of guessing left
   
   // pre: the length of the word is greater than 1 
   //      and the max guessing time is greater than 0
   //      throw IllegalArgumentException if is not
   // post: initializes the state of the game
   //       choose the set of words from the dictionary that have the given length
   //       eliminates any duplicates  
   public HangmanManager(Collection<String> dictionary, int length, int max) {
      if(length < 1 || max < 0) {
         throw new IllegalArgumentException();
      }
      left = max;
      currWord = new TreeSet<String>();
      for(String s : dictionary) {
         if(s.length() == length) {
            currWord.add(s);
         }
      }
      alreadyGu = new TreeSet<Character>();
      pattern = "-";
      for (int i = 0; i < length - 1; i++){
         pattern += " -";
      }     
   }
   
   // post: returns the selected set of words for the following 
   public Set<String> words() {
      return currWord;
   }
   
   // post: return how many times the guessing left
   public int guessesLeft() {
      return left;
   }
   
   // post: returns a set of characters that the player has guessed
   public Set<Character> guesses() {
      return alreadyGu;
   }
   
   // pre: the set of words is not empty
   //      throws excpetion if it is not
   // post: returns a string of the current pattern 
   //       to be displayed for the hangman game 
   public String pattern() {
      if (currWord.isEmpty()) {
         throw new IllegalStateException();
      }
      return pattern;
   }
   
   // post: returns a string of new pattern for each word in the set for a given character   
   private String makePattern(String next, char guess) {
      String newPattern = pattern;
      for(int i = 0; i < next.length(); i++) {
         if(next.charAt(i) == guess) {
            newPattern = newPattern.substring(0, 2 * i) + guess 
                         + pattern.substring(2 * i + 1);
         }
      }   
      return newPattern;
   }

   // post: divides the set of words into groups based on their patterns 
   private void makeGroup(char guess, Map<String, Set<String>> group) {
      Iterator<String> itr = currWord.iterator();
      while(itr.hasNext()) {
         String next = itr.next();
         String newPattern = makePattern(next, guess);
         if(! group.containsKey(newPattern)) {
            group.put(newPattern, new TreeSet<String>());
         }
         group.get(newPattern).add(next);
      }   
   }
   
   // post: returns a string of pattern that has most words among the groups      
   private String most(Map<String, Set<String>> group) {
      String result = "";
      int most = 0;
      for(String str : group.keySet() ) {
         if(most < group.get(str).size()) {
            most = group.get(str).size();
            result = str;
         }
      }
      return result;
   }
   
   // pre: The number of guesses left is larger than 1
   //      and the set of words is not empty
   //      throw IllegalStateException if is not
   //      The previous excetpion was thrown 
   //      and the character being guessed cannot be guessed twice,
   //      throw IllegalArgumentException if is not
   // post: decides which set of words to use in the following games
   //       returns a number of occurrences of the guessed letter
   //       in the new picked pattern  
   public int record(char guess) {
      if (left < 1 || currWord.isEmpty()) {
         throw new IllegalStateException();
      }
      if (alreadyGu.contains(guess)) {
         throw new IllegalArgumentException();
      }
      alreadyGu.add(guess);
      Map<String, Set<String>> group = new TreeMap<String, Set<String>>();
      makeGroup(guess, group);
      String most = most(group);
      if (pattern.equals(most)) {
         left--;
      }
      pattern = most;
      currWord = group.get(pattern);    
      int count = 0;
      for (int i = 0; i < pattern.length(); i++){
         if (pattern.charAt(i) == guess) {
            count ++;
         }
      }
      return count;
   }
}