package ua.com.znannya.client.ui.widgets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

/**
 * Convenience class for building TableModels based on List of objects.
 */
public abstract class ObjectListTableModel<T> extends AbstractTableModel
{
  private List<T> oList = new ArrayList<T>();
  private String[] colNames = null;
  private Vector<Integer> visibleColumns;
  
  private int numerationStart = 1, currentRow;
  
  public ObjectListTableModel() {}
  
  public ObjectListTableModel(String[] acolNames)
  {
    colNames = acolNames;
    visibleColumns = new Vector<Integer>();
    for (int i = 0; i < colNames.length; i++) {
		visibleColumns.add( new Integer(i) );
	}
  }
 
  @Override
  public String getColumnName(int i)
  {
    if (colNames == null || i < 0 || i >= colNames.length)  return "";
    int indx = visibleColumns.get(i);
    return colNames[indx];
//    return colNames[i];
  }
  
  public int getRowCount()
  {
    return oList.size();
  }

  public int getColumnCount()
  {
//    if (colNames == null) return 0;
//    return colNames.length;
	  return visibleColumns.size();
  }

  public Object getValueAt(int rowIndex, int columnIndex)
  {
    T o = oList.get(rowIndex);
    currentRow = rowIndex;
    int indx = visibleColumns.get(columnIndex);
    return getObjectField(indx, o);
  }
  
  public void turnOffColumn(int col){
	  Integer intCol = new Integer(col);
      int i = visibleColumns.indexOf(intCol);
      if(i!=-1){
           visibleColumns.remove(i);
      };
      fireTableStructureChanged();
  }
  
  public void resetColumns(){
      visibleColumns.clear();
      for (int i = 0; i < colNames.length; i++){
    	  visibleColumns.add( new Integer(i) );
      };
      fireTableStructureChanged();
  }
  
  public T getObject(int i)
  {
    if (i < 0 || i >= oList.size()) return null;
    return oList.get(i);
  }
  
  public void setColNames(String[] acolNames)
  {
    colNames = acolNames;
  }
  
  public void setObjectList(List<T> aoList)
  {
    setObjectList(aoList, 1);
  }

  public void setObjectList(List<T> aoList, int numerationStart)
  {
    if (aoList == null) return ;
    this.numerationStart = numerationStart;
    oList = aoList;
    fireTableDataChanged();
  }

  public void setObjectCollection(Collection<T> aoColl)
  {
    setObjectList( new ArrayList<T>(aoColl) );
  }

  public List<T> getObjectList()
  {
    return oList; // TODO: return copy
  }

  public void removeAllObjects()
  {
    oList.clear();
  }

  public void addObject(T o)
  {
    oList.add(o);
    fireTableDataChanged();
  }
  
  public void removeObject(int i)
  {
    if (i < 0 || i >= oList.size()) return;
    oList.remove(i);
    fireTableDataChanged();
  }

  protected int getNumerationStart()
  {
    return numerationStart;
  }

  protected int getCurrentRow()
  {
    return currentRow;
  }

  public abstract Object getObjectField(int fidx, T object);
}
