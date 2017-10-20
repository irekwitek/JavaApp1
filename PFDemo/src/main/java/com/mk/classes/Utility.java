package com.mk.classes;


import java.util.*;
import java.math.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public final class Utility
{


    /**
     * This Method return a Vector that contains Strings generated from a given String.
     * The input String is a list of elements delimited by a given delimiter. The original list is
     * broken down into Strings each containing no more elements than a specified number (len). 
     * @param s
     * @param delimiter
     * @param len
     * @return Returns a Vector containing Strings of elements from the original String but each containg no more than a len elements.
     */
    public static Vector breakStringIntoSubstrings( String s, String delimiter, int len )
    {
        Vector ret = new Vector();
        StringTokenizer st = new StringTokenizer( s, delimiter );
        int cnt = st.countTokens();
        if ( cnt <= len )
        {
            ret.add( s );
        }
        else
        {
            int pos = 0;
            StringBuffer elemStr = new StringBuffer();
            while ( st.hasMoreTokens() )
            {
                elemStr.append( ( pos == 0 ? "" : delimiter ) + st.nextToken() );
                pos++;
                if ( pos == len )
                {
                    ret.add( elemStr.toString() );
                    pos = 0;
                    elemStr = new StringBuffer();
                }
            }
            if ( pos != 0 )
            {
                ret.add( elemStr.toString() );
            }
        }
        return ret;
    }


    /**
     * This Method converts a given integer to a String containing a required number of characters.
     * The number is right justified within a String space and any remaining places are filled with a given character.
     * @param number
     * @param numOfChar
     * @param filler
     * @return Returns a String representation of a given integer number.
     */
    public static String convertIntegerToFixedString( long number, int numOfChar, char filler )
    {
        String ret = "" + number;
        numOfChar = Math.abs( numOfChar );
        if ( ret.length() >= numOfChar )
        {
            ret = ret.substring( ret.length() - numOfChar, ret.length() );
        }
        else
        {
            int cnt = ret.length();
            for ( int i = 0; i < numOfChar - cnt; i++)
            {
                ret = filler + ret;
            }
        }
        return ret;
    }


    /**
     * @return Returns a String that is a formatted representation of a given BigDecimal.
     */
    public static String formatBigDecimal( BigDecimal bd )
    {
        String formatNumber = "";
        String rawNumber = "" + bd;
        String numberPart = "";
        String signPart = "";
        String decimalPart = "";
        int minusSignPosition = rawNumber.indexOf( "-" );
        int decimalPosition = rawNumber.indexOf( "." );
        if ( decimalPosition >= 0 )
        {
            decimalPart = rawNumber.substring( decimalPosition, rawNumber.length() );
            numberPart = rawNumber.substring( 0, decimalPosition );
        }
        else
        {
            numberPart = "" + bd;
        }
        if ( minusSignPosition >= 0 )
        {
            signPart = "-";
            numberPart = numberPart.substring( 1, numberPart.length() );
        }
        while ( numberPart.length() > 3 )
        {
            formatNumber = "," + numberPart.substring( numberPart.length() - 3, numberPart.length() ) + formatNumber;
            numberPart = numberPart.substring( 0, numberPart.length() - 3 );
        }
        return signPart + numberPart + formatNumber + decimalPart;
    }



    /**
     * @return Position of a pattern string within a given string but all strings are treated as numbers.
     * A pattern string is converted to a number and is compared to each element of a given string converted to a number
     * A number format of the strings are irrelevant, only the numerical values are compared. 
     */
    public static int getStringPositionCompareAsNumbers( String s, String pattern, String delimiter )
    {
        int ret = -1;
        BigDecimal bdPattern = null, bd = null;
        try
        {
            bdPattern = new BigDecimal( removeCommasFromString( pattern.trim() ) );
        }
        catch ( NumberFormatException nfe )
        {
            return ret;
        }
        StringTokenizer st = new StringTokenizer( s, delimiter );
        int cnt = st.countTokens();
        for ( int i = 0; i < cnt; i++ )
        {
            String str = st.nextToken();
            try
            {
                bd = new BigDecimal( removeCommasFromString( str.trim() ) );
                if ( bdPattern.compareTo( bd ) == 0 )
                {
                    ret = i;
                    break;
                }
            }
            catch ( NumberFormatException nfe )
            {
            }
        }
        return ret;
    }


    public static String removeCommasFromString( String s )
    {
        String ret = "";
        StringTokenizer st = new StringTokenizer( s, "," );
        while ( st.hasMoreTokens() )
        {
            ret = ret + st.nextToken();
        }
        return ret;
    }

    public static String generateRandomNumberAsString(int len )
    {
        long num = (long)(Math.random() * Math.pow(10,len ) );
        return Utility.convertIntegerToFixedString(num, len, '0');
    }


    public static String[] sortHashtable( Hashtable ht)
    {
        Vector v = new Vector();
        Enumeration e = ht.keys();
        while (e.hasMoreElements())
        {
            v.add(e.nextElement());
        }
        Object[] oArr = v.toArray();
        Arrays.sort(oArr);
        String[] ret = new String[oArr.length];
        for (int i = 0; i < oArr.length; i++)
        {
            ret[i] = (String)oArr[i];
        }
    return ret;
    }


    public static boolean checkSpecialCharacters( String str )
    {
        boolean ret = false;
        int len1 = str.length();
        for ( int i = 0; i < len1; i++ )
        {
            int len2 = ApplicationProperties.ILLEGAL_CHARACTERS_LIST.length();
            for ( int j = 0; j < len2; j++)
            {
                if ( str.charAt(i) == ApplicationProperties.ILLEGAL_CHARACTERS_LIST.charAt(j) )
                {
                    return true;
                }
            }
        }

        return ret;
    }

    public static String removeSpecialCharacters( String str )
    {
        StringBuffer ret = new StringBuffer();
        int len1 = str.length();
        int len2 = ApplicationProperties.ILLEGAL_CHARACTERS_LIST.length();
        for ( int i = 0; i < len1; i++ )
        {
            boolean add = true;
            for ( int j = 0; j < len2; j++)
            {
                if ( str.charAt(i) == ApplicationProperties.ILLEGAL_CHARACTERS_LIST.charAt(j) )
                {
                    add = false;
                    break;
                }
            }
            if ( add )
            {
                ret.append(str.charAt(i));
            }
        }
        return ret.toString();
    }

    public static String replaceStringInString( String str, String pattern, String with )
    {
        return str.replaceAll(pattern, with);
    }

    /**
     * Converts a list of delimited strings to a list of delimited strings
     * with single quotes around each. This is to comply with SQL query syntax for text parameters,
     * that required single quotes around each item from the list
     * @return Delimited list of items with single quotes around each.
     */
    public static String convertStringToSqlString(String s, String delimiter)
    {
        String ret = "";
        boolean begin = true;
        StringTokenizer st = new StringTokenizer(s, delimiter);
        while (st.hasMoreTokens())
        {
            ret += (begin ? "" : delimiter) + "'" + st.nextToken().trim() + "'";
            begin = false;
        }
        return ret;
    }
    
    public static boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }
 
    public static int getStringPosition(String s, String pattern, String delimiter) {
        int ret = -1;
        StringTokenizer st = new StringTokenizer(s, delimiter);
        int cnt = st.countTokens();
        for (int i = 0; i < cnt; i++) {
            String str = st.nextToken();
            if (str.equals(pattern)) {
                ret = i;
                break;
            }
        }
        return ret;
    }
}

