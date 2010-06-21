package org.wiztools.util.digesthexutil;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.zip.CRC32;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import org.wiztools.commons.Charsets;
import org.wiztools.commons.DigestAlgorithm;
import org.wiztools.commons.DigestUtil;

/**
 *
 * @author subhash
 */
public class DigestHexFrame extends JFrame implements ClipboardOwner {

    private final DigestHexFrame me;

    private static final String CRC32 = "CRC32";

    private JTextArea jta_in = new JTextArea(10, 35);
    private JTextField jtf_out = new JTextField(20);
    private JComboBox jcb_encoding = new JComboBox(Charset.availableCharsets().keySet().toArray());
    private JComboBox jcb_digest_algo = null;
    {
        final String[] ALGO = new String[DigestAlgorithm.ALL.length + 1];
        ALGO[0] = CRC32;
        System.arraycopy(DigestAlgorithm.ALL, 0, ALGO, 1, DigestAlgorithm.ALL.length);
        jcb_digest_algo = new JComboBox(ALGO);
    }
    private JButton jb_compute = new JButton("        Compute        ");
    private JButton jb_copy = new JButton("<html>&copy;</html>");
    private JButton jb_uppercase = new JButton("<html>&uarr;</html>");
    private JButton jb_lowercase = new JButton("<html>&darr;</html>");

    public DigestHexFrame(final String title) {
        super(title);

        me = this;

        init();
        initUi();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        jta_in.requestFocus();
        setVisible(true);
    }

    private void init(){
        jtf_out.setEditable(false);

        jcb_encoding.setSelectedItem(Charsets.UTF_8.name());
        jcb_encoding.setToolTipText("Text encoding to use");
        jcb_digest_algo.setSelectedItem(DigestAlgorithm.MD5);
        jcb_digest_algo.setToolTipText("Digest algorithm");
        jtf_out.setFont(new Font(Font.DIALOG, Font.PLAIN, 16));

        jb_compute.setMnemonic('o');
        jb_copy.setMnemonic('c');

        jb_compute.setToolTipText("Compute!");
        jb_copy.setToolTipText("Copy digest to clipboard");
        jb_uppercase.setToolTipText("Uppercase");
        jb_lowercase.setToolTipText("Lowercase");

        jb_compute.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final String text = jta_in.getText();
                try{
                    byte[] textBytes = text.getBytes((String)jcb_encoding.getSelectedItem());
                    final String selectedAlgo = (String)jcb_digest_algo.getSelectedItem();
                    if(selectedAlgo.equals(CRC32)){
                        final CRC32 crc = new CRC32();
                        crc.update(textBytes);
                        jtf_out.setText(String.valueOf(crc.getValue()));
                    }
                    else{
                        jtf_out.setText(DigestUtil.digest(textBytes, selectedAlgo));
                    }
                }
                catch(UnsupportedEncodingException ex){
                    assert true: "Will never come here!";
                }
            }
        });

        jb_uppercase.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jtf_out.setText(jtf_out.getText().toUpperCase());
            }
        });

        jb_lowercase.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jtf_out.setText(jtf_out.getText().toLowerCase());
            }
        });

        jb_copy.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final String digest = jtf_out.getText();
                if(digest.trim().equals("")){
                    JOptionPane.showMessageDialog(me, "Nothing to copy!");
                    return;
                }
                Toolkit.getDefaultToolkit().getSystemClipboard()
                        .setContents(new StringSelection(digest), me);
                JOptionPane.showMessageDialog(me, "<html>Copied text:<br>" + digest + "</html>");
            }
        });
    }

    private void initUi(){
        Container c = getContentPane();
        c.setLayout(new BorderLayout());

        {
            JPanel jp = new JPanel();
            jp.setLayout(new BorderLayout());
            JScrollPane jsp = new JScrollPane(jta_in);
            jp.add(jsp, BorderLayout.CENTER);

            JPanel jp_north = new JPanel();
            jp_north.setLayout(new FlowLayout(FlowLayout.LEFT));
            jp_north.add(jcb_encoding);
            jp_north.add(jcb_digest_algo);
            jp.add(jp_north, BorderLayout.NORTH);

            c.add(jp, BorderLayout.CENTER);
        }

        {
            JPanel jp = new JPanel();
            jp.setLayout(new BorderLayout());

            JPanel jp_temp = new JPanel();
            jp_temp.setLayout(new FlowLayout(FlowLayout.CENTER));
            jp_temp.add(jb_compute);
            jp_temp.add(jb_uppercase);
            jp_temp.add(jb_lowercase);
            jp_temp.add(jb_copy);
            jp.add(jp_temp, BorderLayout.NORTH);

            jp.add(jtf_out, BorderLayout.SOUTH);

            c.add(jp, BorderLayout.SOUTH);
        }
    }

    public void lostOwnership(Clipboard clipboard, Transferable contents) {
        // Do nothing!
    }
}
