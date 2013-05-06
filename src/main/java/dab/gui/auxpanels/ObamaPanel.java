package dab.gui.auxpanels;

import java.awt.Color;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 * The ObamaPanel contains information of immediate importance to the user, such
 * as component failure and critical temperatures. It contains an image of
 * obama, a speech bubble and a label for text to appear
 *
 * @author Team Haddock
 *
 */
public abstract class ObamaPanel extends JPanel {

    protected JLabel lblObama;
    protected JLabel lblSpeech;
    protected JLabel lblWords;

    public ObamaPanel() {
        setBackground(Color.WHITE);
        setLayout(null);        

        lblWords = new JLabel();
        lblWords.setFont(new Font("Bookman Old Style", Font.BOLD, 14));
        lblWords.setForeground(Color.BLACK);
        lblWords.setVerticalAlignment(SwingConstants.TOP);
        lblWords.setHorizontalAlignment(SwingConstants.LEFT);
        lblWords.setHorizontalTextPosition(SwingConstants.RIGHT);
        lblWords.setBounds(210, 30, 400, 400);
        add(lblWords);

        lblSpeech = new JLabel();
        lblSpeech.setFont(new Font("Bookman Old Style", Font.BOLD, 14));
        lblSpeech.setBounds(0, -80, 1000, 300);
        lblSpeech.setIconTextGap(50);
        lblSpeech.setIcon(new ImageIcon("src/main/resources/dab/gui/auxpanels/speech.png"));
        add(lblSpeech);
    }

    // update the panel with warnings and stuff
    public abstract void update();
}
