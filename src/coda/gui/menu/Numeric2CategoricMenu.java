/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package coda.gui.menu;

import coda.gui.utils.BoxDataSelector;
import coda.DataFrame;
import coda.gui.CoDaPackMain;
import java.awt.BorderLayout;
import java.awt.Point;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 *
 * @author mcomas
 */
public class Numeric2CategoricMenu extends JDialog{
    public static final long serialVersionUID = 1L;
    BoxDataSelector ds;
    DataFrame df;
    public Numeric2CategoricMenu(final CoDaPackMain mainApp){

        Point p = mainApp.getLocation();
        p.x = p.x + (mainApp.getWidth()-520)/2;
        p.y = p.y + (mainApp.getHeight()-430)/2;
        setLocation(p);


        setSize(190,370);
        df = mainApp.getActiveDataFrame();
        ds = new BoxDataSelector(df);
        getContentPane().setLayout(new BorderLayout());

        getContentPane().add(ds, BorderLayout.CENTER);

        JButton accept = new JButton("Accept");
        getContentPane().add(accept, BorderLayout.SOUTH);
        accept.addActionListener(new java.awt.event.ActionListener() {
            
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                String selected[] = ds.getSelectedData();
                int n = selected.length;
                for(int i=0;i<n;i++){
                    df.get(selected[i]).categorize();
                }
                mainApp.updateDataFrame(df);
                setVisible(false);
            }
            
        });
    }
    @Override
    public void setVisible(boolean v){
        if(df == null){
            JOptionPane.showMessageDialog(null, "No data available");
            this.dispose();
        }else{
            super.setVisible(v);
        }
    }
}
