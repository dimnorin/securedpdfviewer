package ua.com.znannya.client.util;

import java.awt.Component;
import java.awt.Window;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * A collecton of static helper methods for UI building.
 */
public class GuiUtil
{
  public static final int MAX_TEXTFIELD_HEIGHT = 22;
  public static final Object NONE_CBX_ITEM = " - ";

  public static int computeLabelWidth(JLabel lbl)
  {
    return SwingUtilities.computeStringWidth(lbl.getFontMetrics(lbl.getFont()), lbl.getText());
  }

  public static int getMaxLabelWidth(JLabel... labels)
  {
    List<Integer> widths = new ArrayList<Integer>();
    for (JLabel lbl : labels) {
      widths.add(computeLabelWidth(lbl));
    }
    return Collections.max(widths);
  }

  /** Sets the column withs for given table. Excess width values are ignored. */
  public static void setColumnWidths(JTable tbl, int... widths)
  {
    for (int colIdx = 0; colIdx < widths.length; colIdx++)
    {
      if (colIdx >= tbl.getColumnModel().getColumnCount())  return;   // check for excess width values
      tbl.getColumnModel().getColumn(colIdx).setPreferredWidth(widths[colIdx]);
    }
  }

  /** Sets the column max withs for given table. Excess width values are ignored. */
  public static void setColumnMaximumWidths(JTable tbl, int... widths)
  {
    for (int colIdx = 0; colIdx < widths.length; colIdx++)
    {
      if (colIdx >= tbl.getColumnModel().getColumnCount())  return;   // check for excess width values
      tbl.getColumnModel().getColumn(colIdx).setMaxWidth(widths[colIdx]);
    }
  }
  
  /** 
   * Fills combo box with values of the list. All previously existing items of the box are removed.
   * @param cbx JComboBox instance for filling
   * @param items a list of items to fill the box with
   * @param noneItem if this parameter is not null, then it will be added as first item in combo.
   */
  public static void fillCombo(JComboBox cbx, List<? extends Object> items, Object noneItem)
  {
    if (cbx == null) return ;
    cbx.removeAllItems();
    if (noneItem != null) {
      cbx.addItem(noneItem);
    }
    for (Object d : items) {
      cbx.addItem(d);
    }
  }

  /**
   * Checks text fields for being empty.
   * @return true if one of those fields is empty.
   */
  public static boolean checkEmpty(JTextField... tflds)
  {
    for (JTextField tfld : tflds) {
      if (tfld.getText().isEmpty()) return true;
    }
    return false;
  }

  public static void setSysLAF()
  {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }
    catch (Exception ex) {
      Logger.getLogger(GuiUtil.class.getName()).log(Level.WARNING, "Error setting system LAF.", ex);
    }
  }

  public static void testUiComponent(Component c)
  {
    setSysLAF();
    JFrame f = new JFrame("Test Frame");
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.setSize(800, 600);
    f.setVisible(true);
    if (c instanceof Window) {
      ((Window) c).setLocationRelativeTo(f);
      ((Window) c).setVisible(true);
    }
    else {
      f.add(c);
    }
  }
}
