package org.wiztools.util.digesthexutil;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import org.wiztools.commons.CommonCharset;
import org.wiztools.commons.DigestAlgorithm;
import org.wiztools.commons.DigestUtil;

/**
 *
 * @author subhash
 */
public class DigestHexFrame extends JFrame implements ClipboardOwner {

    private final DigestHexFrame me;

    private JTextArea jta_in = new JTextArea(10, 20);
    private JTextField jtf_out = new JTextField(20);
    private JComboBox jcb_encoding = new JComboBox(Charset.availableCharsets().keySet().toArray());
    private JComboBox jcb_digest_algo = new JComboBox(DigestAlgorithm.ALL);
    private JButton jb_generate = new JButton("        Generate        ");
    private JButton jb_copy = new JButton("Copy-2-Clipboard");

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

        jcb_encoding.setSelectedItem(CommonCharset.UTF_8.name());
        jcb_encoding.setToolTipText("Text encoding to use");
        jcb_digest_algo.setSelectedItem(DigestAlgorithm.MD5);
        jcb_digest_algo.setToolTipText("Digest algorithm");

        jb_generate.setMnemonic('G');
        jb_copy.setMnemonic('C');

        jb_generate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final String text = jta_in.getText();
                try{
                    byte[] textBytes = text.getBytes((String)jcb_encoding.getSelectedItem());
                    jtf_out.setText(DigestUtil.digest(textBytes, (String)jcb_digest_algo.getSelectedItem()));
                }
                catch(UnsupportedEncodingException ex){
                    assert true: "Will never come here!";
                }
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
            jp_temp.add(jb_generate);
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
