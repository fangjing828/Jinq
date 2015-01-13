package org.jinq.jpa;

import java.util.Collection;
import java.util.regex.Pattern;

/**
 * Provides Java implementations of certain JPQL functions.
 *
 */
public class JPQL
{
   /**
    * In-memory implementation of JPQL like.
    * @param str string to search inside
    * @param pattern pattern to look for inside string (uses JPQL/SQL like syntax)
    * @return true iff the pattern in found in the string
    */
   public static boolean like(String str, String pattern)
   {
      return like(str, pattern, "");
   }
   
   /**
    * In-memory implementation of JPQL like.
    * @param str string to search inside
    * @param pattern pattern to look for inside string (uses JPQL/SQL like syntax)
    * @param escapeChar escape character that can be used to escape the special _ and % used in the pattern syntax 
    * @return true iff the pattern in found in the string
    */
   public static boolean like(String str, String pattern, String escapeChar)
   {
      String regex = "";
      String subpattern = "";
      while (!pattern.isEmpty())
      {
         // Ignore Unicode codepoint issues
         String nextChar = pattern.substring(0, 1);
         if (escapeChar.equals(nextChar))
         {
            if (pattern.length() > 1)
            {
               pattern = pattern.substring(1);
               String nextNextChar = pattern.substring(0, 1);
               subpattern += nextNextChar;
            }
         }
         else if ("_".equals(nextChar))
         {
            regex += Pattern.quote(subpattern);
            regex += ".";
            subpattern = "";
         }
         else if ("%".equals(nextChar))
         {
            regex += Pattern.quote(subpattern);
            regex += ".*";
            subpattern = "";
         }
         else
         {
            subpattern += nextChar;
         }
         pattern = pattern.substring(1);
      }
      regex += Pattern.quote(subpattern);
      return str.matches(regex);
   }
   
   /**
    * Checks if an item is in a Collection list. This method is normally 
    * used with the list as a parameter to the query. 
    * @param item object that will be checked to see if it is in list
    * @param list Collection of different items
    * @return true if the item is in the list
    */
   public static <U> boolean isInList(U item, Collection<U> list)
   {
      return list.contains(item);
   }
   
   /**
    * Alternate syntax for the method isInList()
    * @see #isInList()
    */
   public static <U> boolean listContains(Collection<U> list, U item)
   {
      return isInList(item, list);
   }
}
