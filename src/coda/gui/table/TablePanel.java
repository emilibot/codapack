package coda.gui.table;



import coda.DataFrame;
import coda.DataFrame.DataFrameException;
import coda.DataFrame.DataFrameListener;
import coda.Element;
import coda.Text;
import coda.Variable;
import coda.gui.CoDaPackMain;
import coda.gui.DataList;
import coda.gui.table.ExcelAdapter;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author marc
 */
public final class TablePanel extends JPanel{
    public static final long serialVersionUID = 1L;
    ExcelAdapter ed;
    /**
     *
     */
    DataFrame df = new DataFrame();
    public JTable table;
    private JTable rowTable;
    JScrollPane scrollPane1 = new JScrollPane();
    JPopupMenu pm = new JPopupMenu();
    CoDaPackMain main;
    /**
     *
     */
    
    public TablePanel(){
        
        setLayout(new BorderLayout());
        this.main = main;
        table = new JTable(new DataTableModel(df));

        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // allow columns and rows resizing
        table.setColumnSelectionAllowed(true); // allow selection only by columns
        table.setRowSelectionAllowed(false);
        table.getTableHeader().setReorderingAllowed(false); // avoid column reordering

        // create a row headers with default numbering
        rowTable = new RowNumberTable(table);

        // add scrolling to table panel
        add(scrollPane1, BorderLayout.CENTER);
        scrollPane1.getViewport().add(table, null);
        scrollPane1.setRowHeaderView(rowTable);
        scrollPane1.setCorner(JScrollPane.UPPER_LEFT_CORNER,
            rowTable.getTableHeader());

        table.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent me) {
                showPopup(me);
            }

            @Override
            public void mouseReleased(MouseEvent me) {
                showPopup(me);
            }
        });
        /*
         * MENU
         */
        JMenuBar actions = new JMenuBar();

 

        // MENU ACCIONS OUTPUTS!!!!!!!
        this.add(actions, java.awt.BorderLayout.NORTH);
        
    }

    public void clearData(){
        table.setModel(new DataTableModel(new DataFrame()));
    }
    JMenuItem menuRenameVariable(DataFrame df, Variable var){
        JMenuItem item = new JMenuItem();
        item.setText("Rename variable " + var.getName() + ".");
        item.addActionListener(new VariableRename(this, df, var.getName()));
                
        return item;
    }
    JMenuItem menuFactorizeVariable(final DataFrame df, final Variable var){
        JMenuItem item = new JMenuItem();
        if(var.isText()){
            item.setText("Unfactorize " + var.getName() + ".");
            item.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                     var.toNumeric();
                     df.alertModification(var);
                }
            });
        }else{
            item.setText("Factorize " + var.getName() + ".");
            item.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                     var.toText();
                     df.alertModification(var);
                }

            });
        }
        if(!var.isNumeric() && !var.isText())
            item.setEnabled(false);
        
        return item;
    }
    class VariableRename implements ActionListener{
        String vname;
        JPanel panel;
        DataFrame df;
        public VariableRename(JPanel panel, DataFrame df, String vname){
            this.vname = vname;
            this.panel = panel;
            this.df = df;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            boolean exit = false;
            String name = null;
            while(!exit){
                name = JOptionPane.showInputDialog(panel, "Variable name", vname);
                if(name != null){
                    try {
                        df.rename(vname, name);
                        CoDaPackMain.dataList.setData(df);
                        exit = true;
                    } catch (DataFrameException ex) {
                        JOptionPane.showMessageDialog(panel, "Name already in use!");
                        System.out.println("name not found");
                    }
                }else{
                    exit = true;
                }
            }
            System.out.println(name);
        }
    }
    private void showPopup(MouseEvent me) {
        // is this event a popup trigger?
        if (pm.isPopupTrigger(me)) {
            Point p = me.getPoint();
            int row = table.rowAtPoint(p);
            int col = table.columnAtPoint(p);
            // if we've clicked on a row in the second col
            if (col >= 0) {
                DataFrame dfloc = ((DataTableModel)table.getModel()).dataFrame;
                pm = new JPopupMenu();
                pm.add(menuRenameVariable(dfloc, dfloc.get(col)));
                pm.add(menuFactorizeVariable(dfloc, dfloc.get(col)));//new JMenuItem("factorize " + dfloc.get(col).getName()));
                //one.setText("Rename variable " + df.get(col).getName() + ".");
                //two.setText("Eliminate variable " + df.get(col).getName() + ".");
                pm.show(table, p.x, p.y);
            }
        }
    }
    /**
    *
    * @param df
    */
    public void setDataFrame(DataFrame df){
        if(df == null)
            return;
        ed = new ExcelAdapter(table, df);
        table.setDefaultRenderer(Object.class, new DataRenderer(df) );
        df.removeDataFrameListener();
        df.addDataFrameListener(new DataFrameListener(){
            @Override
            public void dataFrameModified(DataFrame df) {
                table.setModel(new DataTableModel(df));
            }
        });
        table.setModel(new DataTableModel(df));
        
    }
    private static Color outputColor = new Color(162,193,215);
    private class DataRenderer extends DefaultTableCellRenderer{
        public static final long serialVersionUID = 1L;
        
        DataFrame dataFrame;
        
        public DataRenderer(DataFrame df){ 
            dataFrame = df;
        }
        @Override
	public Component getTableCellRendererComponent(JTable table, 
                Object value, boolean isSelected, boolean hasFocus, 
                int row, int column){
            
            setForeground(Color.black);
            setBackground(Color.white);
            setHorizontalAlignment(SwingConstants.RIGHT);
            setFont(new Font ("Monospace", Font.PLAIN, 12));
            
            Variable var = dataFrame.get(column);
            if(row < var.size()){
                Element el = var.get(row);
                //setBackground(isSelected ? outputColor :  Color.white);
                if(el instanceof Text){
                    setHorizontalAlignment(SwingConstants.LEFT);
                    setBackground(isSelected ? new Color(162,193,215):  Color.orange);
                    setBackground(Color.orange);
                }
//                if(el instanceof NonAvailable){
//                    setHorizontalAlignment(SwingConstants.CENTER);
//                    //setBackground(isSelected ? outputColor :  new Color(162,193,215));
//                    setBackground(new Color(162,193,215));
//                }
            }
            if (isSelected){
                setFont( getFont().deriveFont(Font.BOLD) );
            }
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    /**
    *
    */
    public class DataTableModel extends AbstractTableModel{
        /**
        *
        */
        
        DataFrame dataFrame;
        public static final long serialVersionUID = 1L;
        int COLSIZE, ROWSIZE;
        boolean is_editable = true;
        
        /**
         *
         * @param df
         */
        public DataTableModel(DataFrame df){
            dataFrame = df;
            COLSIZE = dataFrame.size();
            ROWSIZE = 0;
            for(int i=0;i<dataFrame.size();i++){
                ROWSIZE = Math.max(ROWSIZE, dataFrame.get(i).size());
            }
        }
        /**
         *
         * @return
         */
        @Override
        public int getRowCount() {
            return ROWSIZE;
        }
        /**
         *
         * @return
         */
        @Override
        public int getColumnCount() {
            return COLSIZE;
        }
        /*
         *
         * @param i
         * @return
         */
        @Override
        public String getColumnName(int i){
            
            Variable var = dataFrame.get(i);
            return dataFrame.get(i).getName();
            
        }
        @Override
        public boolean isCellEditable(int arg0, int arg1){
            return is_editable;
        }
        /**
         *
         * @param arg0
         * @param arg1
         * @return
         */
        @Override
        public Object getValueAt(int row, int col) {
            Variable var = dataFrame.get(col);
            if( row < var.size()){
                return var.get(row).toString();
            }else{
                return "";
            }
        }
        @Override
        public void setValueAt(Object arg, int row, int col) {
            Variable var = dataFrame.get(col);
            Element el = dataFrame.get(col).get(row);
            Element new_el = var.setElementFromString(((String)arg).trim());
            if(new_el != null)
                var.set(row, new_el);
        }
    }
}