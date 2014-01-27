package ua.com.znannya.client.ui.widgets;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import ua.com.znannya.client.app.ZnclApplication;


/**
 * Custom table header renderer capable of displaying multiline text with word wrapping
 * and showing sorting direction arrow.
 */
public class MultiLineHeaderRenderer implements TableCellRenderer
{
  private JPanel panel = new JPanel();
  private JTextPane textPane = new JTextPane();
  private StyleContext context = new StyleContext();
  private StyledDocument doc = new DefaultStyledDocument(context);
  private Style style;
  private Box boxArrow = new Box(BoxLayout.PAGE_AXIS);
  private JLabel ascend;
  private JLabel descend;

  public MultiLineHeaderRenderer()
  {
    panel.setBorder(UIManager.getBorder("TableHeader.cellBorder"));
    panel.getInsets().set(2, 2, 2, 2);
    panel.setLayout(new BorderLayout());
    panel.add(textPane);
    textPane.setBackground(UIManager.getColor("TableHeader.background"));
    textPane.setStyledDocument(doc);
    style = context.getStyle(StyleContext.DEFAULT_STYLE);
    StyleConstants.setFontSize(style, 10);
    StyleConstants.setAlignment(style, StyleConstants.ALIGN_CENTER);
    boxArrow.add(Box.createVerticalStrut(5));
    panel.add(boxArrow, BorderLayout.SOUTH);
    ascend = new JLabel( ZnclApplication.getApplication().getIcon("Ascend.png") );
    ascend.setAlignmentX(Component.CENTER_ALIGNMENT);
    descend = new JLabel( ZnclApplication.getApplication().getIcon("Descend.png") );
    descend.setAlignmentX(Component.CENTER_ALIGNMENT);
  }

  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
  {
    String str = value == null ? "" : value.toString();
    try {
      doc.remove(0, doc.getLength());
      doc.insertString(0, str, style);
      doc.setParagraphAttributes(0, doc.getLength(), style, true);
    }
    catch (BadLocationException ex) {
      Logger.getLogger(MultiLineHeaderRenderer.class.getName()).log(Level.WARNING, null, ex);
    }
    return panel;
  }

  /** Draws a small arrow for showing sorting direction. */
  public void showArrow(boolean asc)
  {
    boxArrow.removeAll();
    if (asc) {
      boxArrow.add(ascend);
    }
    else {
      boxArrow.add(descend);
    }
  }

  /** Removes sorting arrow if it is shown. */
  public void hideArrow()
  {
    boxArrow.removeAll();
    boxArrow.add(Box.createVerticalStrut(5));
  }
}

