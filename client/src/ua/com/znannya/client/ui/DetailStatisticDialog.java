package ua.com.znannya.client.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import org.jivesoftware.smack.packet.znannya.StatsCollection;
import org.jivesoftware.smack.znannya.dao.FullStatistics;

import ua.com.znannya.client.app.ZnclApplication;
import ua.com.znannya.client.ui.widgets.ObjectListTableModel;
import ua.com.znannya.client.util.GuiUtil;

public class DetailStatisticDialog extends JDialog{
	private final static String REFILL			= "REFILL";
	private final static String COPY			= "COPY";
	private final static String VIEW			= "VIEW";
	private final static String PAGE			= "PAGE";
	private final static String MINUTE			= "MINUTE";
	
	private final static int WIDTH = 1024;
	private final static int HEIGHT = 400;
	
	private JTable tblDetailsStatic;
	private DetailStatsModel tblModel;
	
	private ResourceBundle uiTextResourse = ZnclApplication.getApplication().getUiTextResources();  
	
	public DetailStatisticDialog(JDialog owner){
		super(owner);
		setSize(WIDTH, HEIGHT);
		setIconImage(ZnclApplication.getApplication().getIcon("chart_bar.png").getImage());
		
		initComponent();
		layoutComponent();
		
		final JDialog dialog = this;
		dialog.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e){
				if ( tblDetailsStatic.getSize().getWidth() < dialog.getWidth() )
					  tblDetailsStatic.setAutoResizeMode( JTable.AUTO_RESIZE_ALL_COLUMNS );
				  if ( dialog.getWidth() < WIDTH )
					  tblDetailsStatic.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );
			  }
		  });
		
		setLocationRelativeTo(owner);
	}

	private void initComponent() {
		tblModel = new DetailStatsModel();
		tblDetailsStatic = new JTable(tblModel);
		tblDetailsStatic.setRowHeight(30);
		for (int i = 0; i < tblDetailsStatic.getColumnCount(); i++) {
			tblDetailsStatic.getColumnModel().getColumn(i).setCellRenderer( new TextAreaRenderer() );	
		}
		tblDetailsStatic.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		GuiUtil.setColumnWidths(tblDetailsStatic, 100, 100, 400, 100, 80, 80, 100, 80);
		GuiUtil.setColumnMaximumWidths(tblDetailsStatic, 300, 100, 2000, 100, 80, 80, 100, 80);
		
	}
	
	private void layoutComponent(){
		setLayout( new BorderLayout() );

		JButton btnOk = new JButton("Ok");
		btnOk.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		
		JPanel panelButton = new JPanel();
		panelButton.setLayout( new BoxLayout(panelButton, BoxLayout.X_AXIS) );
		panelButton.add(Box.createHorizontalGlue());
		panelButton.add(btnOk);
		
		JScrollPane scrollPane = new JScrollPane(tblDetailsStatic);
		scrollPane.setSize(WIDTH, HEIGHT);
		add(scrollPane, BorderLayout.CENTER);
		add(panelButton, BorderLayout.SOUTH);
	}
	
	public void setDataInTable(StatsCollection statsCollection){
		tblModel.setObjectList(statsCollection.getFullStatistics());
	}
	
	public JTable getTblDetailStats(){
		return tblDetailsStatic;
	}
	
	public class DetailStatsModel extends ObjectListTableModel<FullStatistics>{
		public DetailStatsModel(){
			super(new String[] {
					uiTextResourse.getString("privateRoomDialog.tblDetailStat.Date") + " (GMT+2)",
					"IP",
					uiTextResourse.getString("privateRoomDialog.tblDetailStat.Descr"),
					uiTextResourse.getString("privateRoomDialog.tblDetailStat.Action"),
					uiTextResourse.getString("privateRoomDialog.tblDetailStat.Quant"),
					uiTextResourse.getString("privateRoomDialog.tblDetailStat.Unit"),
					uiTextResourse.getString("privateRoomDialog.tblDetailStat.CostUnit"),
					uiTextResourse.getString("privateRoomDialog.tblDetailStat.Cost")
			});
		}
		@Override
		public Object getObjectField(int fidx, FullStatistics object) {
			Format formatter = new SimpleDateFormat("dd/MM/yyyy\nHH:mm:ss");
			String action = "";
			if ( object.getAction().equals(REFILL))
				action = uiTextResourse.getString("privateRoomDialog.tblDetailStat.lblRefil");
			else
				if ( object.getAction().equals(VIEW) )
					action = uiTextResourse.getString("privateRoomDialog.tblDetailStat.lblView");
				else
					if ( object.getAction().equals(COPY) )
						action = uiTextResourse.getString("privateRoomDialog.tblDetailStat.lblCopy");
			
			String unit = "";
			String quantity = "";
			if ( object.getUnit().equals(PAGE) ){
				unit = uiTextResourse.getString("privateRoomDialog.tblDetailStat.lblPage");
				quantity = Float.toString( object.getQuantity() );
			} else
				if ( object.getUnit().equals(MINUTE) ){
					unit = uiTextResourse.getString("privateRoomDialog.tblDetailStat.lblMinute");
					float minute = object.getQuantity();
					double secound = (minute - Math.floor(minute)) * 60;
					quantity =  Math.round( minute/60.0 ) + "h:" + Math.round( minute ) + "m:" +  Math.round( secound ) + "s";
				}
			switch (fidx) {
				case 0: return formatter.format( new Date( object.getTime() ) );
				case 1: return object.getIp();
				case 2: return object.getDescription();
				case 3: return action;
				case 4: return quantity;
				case 5: return unit;
				case 6: return object.getCostPerUnit();
				case 7: return object.getAmount();
			}
			return null;
		}
	}
	
	public class TextAreaRenderer extends JEditorPane implements TableCellRenderer {

		public TextAreaRenderer() {
			setEditable(false);
		}

		public Component getTableCellRendererComponent(JTable jTable, Object obj, 
									boolean isSelected, boolean hasFocus, int row, int column) {
			setText(obj.toString());
			return this;
		}
	}
}
