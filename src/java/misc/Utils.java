/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package misc;

/**
 *
 * @author HP USER
 */
public class Utils {
    public static String generateUID(String preText, String postText) {
        StringBuilder sb = new StringBuilder(String.valueOf(System.nanoTime()));        
        int truncateIndex = (sb.length() > 5) ? 5: 0;
        sb.substring(truncateIndex);
        if (preText != null) sb.insert(0, preText);
        if (postText != null) sb.append(postText);
        
        return sb.toString();                
    }
}
