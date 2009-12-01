package org.wiztools.util.digesthexutil;

import javax.swing.SwingUtilities;

/**
 *
 * @author subhash
 */
public class Main {
    public static void main(String[] arg){
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new DigestHexFrame("Digest Hex Compute-WizTools.org");
            }
        });
    }
}
