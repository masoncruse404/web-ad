import java.awt.BorderLayout;
import java.awt.*;
import java.awt.geom.*;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Random;
import java.math.*;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;


public class Store extends JFrame implements ActionListener {

	JFrame nframe; 
	JButton generateB = new JButton("Add WebAd");
	JButton viewWebAd = new JButton("View Web Ad");
	JButton clearAd = new JButton("Remove Selected Ad");
	JButton showStats = new JButton("Show Stats");
	int numAds;
	BigDecimal minBD;
	BigDecimal maxBD;
	BigDecimal sumBD;

	JLabel totalLabel = new JLabel( "Total ");
	JLabel blank = new JLabel(" ");


	DefaultTableModel model;
	JTable table;

	NumberFormat moneyFormat = NumberFormat.getCurrencyInstance(Locale.US);

	public static void main(String[] args) {
		Store app = new Store();
		app.setSize(800, 600);
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		app.setName("My WebAd Store");
		app.setTitle(app.getName());
		app.setVisible(true);
	}

	Store() {

		Object[] columnNames = {"WebAd Price", "Discount", "Tax", "Total"};
		model = new DefaultTableModel(0,4) {
			public boolean isCellEditable(int row, int column) {
				return false; // makes table cells not-editable
			}
		}; // rows, columns
		model.setColumnIdentifiers(columnNames);

		table = new JTable(model);

		//show the grid lines:
		table.setGridColor(Color.LIGHT_GRAY);

		//align cells right (since these are monetary amounts):
		DefaultTableCellRenderer alignRight = new DefaultTableCellRenderer();
		alignRight.setHorizontalAlignment(SwingConstants.RIGHT);
		for (int c = 0; c < model.getColumnCount(); c++) table.getColumnModel().getColumn(c).setCellRenderer(alignRight);

		//put table on a scroll pane to make it scrollable:
		JScrollPane sp = new JScrollPane(table);
		add(sp, BorderLayout.CENTER);



		//setup our gui:
		JPanel southP = new JPanel();
		southP.setLayout(new BorderLayout());

		JPanel buttons = new JPanel();
		buttons.setLayout(new GridLayout(2,2));
		buttons.add(generateB);
		buttons.add(viewWebAd);
		buttons.add(clearAd);
		buttons.add(showStats);
		buttons.setPreferredSize(new Dimension(600, 76));

		southP.add(buttons, BorderLayout.EAST);

		JPanel choicesP = new JPanel();
		choicesP.setLayout(new GridLayout(3,1));
		Font f = new Font("Helvetica", Font.PLAIN, 40);

		choicesP.add(blank);
		totalLabel.setFont(f);
		choicesP.add(totalLabel);

		southP.add(choicesP, BorderLayout.WEST);

		add(southP, BorderLayout.SOUTH);

		//add listeners last:
		generateB.addActionListener(this);
		clearAd.addActionListener(this);
		showStats.addActionListener(this);
		viewWebAd.addActionListener(this);

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == generateB) {

			WebAd rd = new WebAd();
			model.insertRow(numAds, rd.getRow());
			numAds++;

			// Init Strip of leading $ from new WebAd total string
			String temp = (String) model.getValueAt(0,3);
			String strip = temp.substring(1);

			minBD = new BigDecimal(strip);
			maxBD = new BigDecimal(strip);
			// sums for each data type:
			double sumD = 0.0;
			sumBD = new BigDecimal("0.0");
			
			// sum up all webAd final total
			sumAds();

			// Update main Label
			totalLabel.setText("$" + sumBD.toPlainString());

			// find min webAd total
			findMin();

			// find max webAd total
			findMax();

		

		} else if (e.getSource() == clearAd) {
   			DefaultTableModel model = (DefaultTableModel) this.table.getModel();
   			int row = table.getSelectedRow();

			int col = 0;
			String value = table.getModel().getValueAt(row, col).toString();
			if(value == null)
				numAds++;
			//System.out.println("This is the value " + value);
     			model.removeRow(row);
			numAds--;

			// Init Strip of leading $ from new WebAd total string

			//Recalculate total
			sumBD = new BigDecimal("0.0");
			sumAds();	
			
			//Recalculate Max
			findMax();

			//Recalculate Min
			findMin();

			// Update Label
			totalLabel.setText(sumBD.toPlainString());

		}
		 else if (e.getSource() == showStats) {
			 BigDecimal avgTotal = sumBD.divide(new BigDecimal(numAds), 2, RoundingMode.HALF_UP);
			JOptionPane.showMessageDialog(this, "Showing stats \n Max: " + maxBD + "\n Min: " + minBD + "\n Avg: " + avgTotal, "Summary Statistics", JOptionPane.INFORMATION_MESSAGE);
		}
		else if(e.getSource() == viewWebAd) {
   			DefaultTableModel model = (DefaultTableModel) this.table.getModel();
			// Getting Selected row's total 
			int row = table.getSelectedRow();
			int col = 3;
			String value = table.getModel().getValueAt(row, col).toString();
			// Creating Web Ad pop up
			nframe = new JFrame("Web Ad " + (row + 1));
			nframe.setSize(new Dimension(320, 320));
			mypanel panel = new mypanel();
			panel.price = value;
			nframe.add(panel);
			nframe.setVisible(true);
		
		}
	}

	public void findMax() {
		if(table.getRowCount() == 0)
			return;
		// Init Strip of leading $ from new WebAd total string
		String temp = (String) model.getValueAt(0,3);
		String strip = temp.substring(1);

		maxBD = new BigDecimal(strip);

		for (int j = 0; j < numAds; j++) {
		// Strip leading $ from total
		temp = (String) model.getValueAt(j,3);
		strip = temp.substring(1);

		BigDecimal compBD = new BigDecimal(strip);

		int res;

		res = compBD.compareTo(maxBD);

		if (res == 1)
			maxBD = compBD;
		}
	}

	public void sumAds() {
		for (int j = 0; j < numAds; j++) {
			
		// Strip leading $ from total
		String temp = (String) model.getValueAt(j,3);
		String strip = temp.substring(1);

		BigDecimal addend = new BigDecimal(strip);
		sumBD = sumBD.add(addend);
		}
	}

	public void findMin() {
			// Init Strip of leading $ from new WebAd total string
		if(table.getRowCount() == 0)
			return;
			String temp = (String) model.getValueAt(0,3);
			String strip = temp.substring(1);

			minBD = new BigDecimal(strip);
		for (int j = 0; j < numAds; j++) {

		// Strip leading $ from total
		temp = (String) model.getValueAt(j,3);
		strip = temp.substring(1);

		BigDecimal compBD = new BigDecimal(strip);

		int res;

		res = minBD.compareTo(compBD);

		if (res == 1)
			minBD = compBD;
		}
	}
}



