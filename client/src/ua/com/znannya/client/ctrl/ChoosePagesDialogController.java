package ua.com.znannya.client.ctrl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collections;

import ua.com.znannya.client.app.ZnclApplication;
import ua.com.znannya.client.ui.ChoosePagesDialog;
import ua.com.znannya.client.ui.FileDownloadDialog;

public class ChoosePagesDialogController implements ActionListener {
	private ArrayList<Integer> pages;
	private ArrayList<Integer> selPages;	
	private ChoosePagesDialog choosePagesDlg = null;
	
	public ChoosePagesDialogController() {
		init();
	}
	
	private void init(){
		pages = new ArrayList<Integer>();
		selPages = new ArrayList<Integer>();	
	}
	
	private void setupListeners(){
		getDialog().getAddBtn().addActionListener(this);
		getDialog().getDelBtn().addActionListener(this);
		getDialog().getOkBtn().addActionListener(this);
	}
	
	public void loadPages(int pagesCount){
		ZnclApplication.getApplication().getControllerManager()
				.getFileDownloadDialogController().getDialog().resetRbChoosePagesText();
		pages.clear();
		selPages.clear();
		int n = pagesCount;
		for (int i = 0;i<n;i++){
			pages.add(i,i+1);			
		}
		refreshLists();
	}
	
	private void refreshLists(){
		getDialog().getPagesList().setListData(pages.toArray());
		getDialog().getSelectPagesList().setListData(selPages.toArray());
	}
	
	/**
	 * This method adds selected pages to second list and remove them from first list
	 */
	private void transferPages(Integer[] selectPages, ArrayList<Integer> firstList, ArrayList<Integer> secondList){
		
		Integer pageNum;
		for (int i = 0;i<selectPages.length;i++){
			pageNum = selectPages[i];
		    if (firstList.contains(pageNum)){
		    	secondList.add(pageNum);
		    	Collections.sort(secondList);
		    	firstList.remove(pageNum);
		    }
		}
	}

	public ChoosePagesDialog getDialog(){
		if (choosePagesDlg == null){
			choosePagesDlg = new ChoosePagesDialog(ZnclApplication
					.getApplication().getMainFrame());
			setupListeners();
		}
		return choosePagesDlg;
	}

	public void showDialog(){
		refreshLists();
		getDialog().setVisible(true);	
	}
	

	public int getSelectedPagesCount(){
		return selPages.size();
	}
	
	public int[] getSelPgsNumbers(){
		return castToIntArray(selPages.toArray());
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == getDialog().getAddBtn())			
			transferPages(cast(getDialog().getPagesList().getSelectedValues()),pages, selPages);
		if (e.getSource() == getDialog().getDelBtn())
			transferPages(cast(getDialog().getSelectPagesList().getSelectedValues()),selPages, pages);	
		if (e.getSource() == getDialog().getOkBtn()){
			FileDownloadDialog dlg = ZnclApplication.getApplication().getControllerManager().getFileDownloadDialogController().getDialog();
	        dlg.setRbChoosePagesText(getSelectedPagesNumbersText());
	        dlg.setTotalText(getSelectedPagesCount(), ZnclApplication.getApplication().getControllerManager().getFileDownloadDialogController().getFile().getDownloadCost());
	        
	        getDialog().setVisible(false);
		}
		refreshLists();
	}
	
	public String getSelectedPagesNumbersText() {
		int[] nums = getSelPgsNumbers();
		StringBuilder sb = new StringBuilder();
		int num = Integer.MAX_VALUE;
		int pnum = Integer.MAX_VALUE;
		int s = Integer.MAX_VALUE;
		int dif;
		for (int i = 0; i < nums.length; i++) {
			num = nums[i];
			dif = num - pnum;
			if (dif > 1) {
				if ((pnum - s) > 1) {
					sb.append(s + "-" + pnum + ",");
					if (i == nums.length - 1)
						sb.append(num);
				}
				else {
					sb.append(s + ",");
					if (i == nums.length - 1)
						sb.append(num);
				}
				s = num;
			}
			else {
				if (i == nums.length - 1) {
					if ((num - s) >= 1)
						sb.append(s + "-" + num);
					else
						sb.append(num);
				}

				if (dif < 1)
					s = num;
			}

			pnum = num;
		}
		return sb.toString();
	}
	
	private Integer[] cast(Object[] array){	
		Integer[] arr = new Integer[array.length];
		for (int i = 0;i<array.length;i++)
			arr[i]=((Integer)array[i]);
		return arr;
	}
	
	private int[] castToIntArray(Object[] array){	
		int[] arr = new int[array.length];
		for (int i = 0;i<array.length;i++)
			arr[i]=((Integer)array[i]).intValue();
		return arr;
	}
}
