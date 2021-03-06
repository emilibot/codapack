/**	
 *	Copyright 2011-2016 Marc Comas - Santiago Thió
 *
 *	This file is part of CoDaPack.
 *
 *  CoDaPack is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  CoDaPack is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with CoDaPack.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package coda.gui.output;

import java.io.IOException;
import java.io.Writer;

/**
 *
 * @author marc
 */
public class OutputTableTwoEntries implements OutputElement{
    String title;
    String namesHoritz[], namesVertic[];
    double data[][];
    
    public OutputTableTwoEntries(String title, String nh[], String nv[], double va[][]){
        this.title = title;
        if(nh != null){
            namesHoritz = new String[nh.length];
            System.arraycopy(nh, 0, namesHoritz, 0, nh.length);
        }else{
            namesHoritz = null;
        }
        if(nv != null){
            namesVertic = new String[nv.length];
            System.arraycopy(nv, 0, namesVertic, 0, nv.length);
        }else{
            namesVertic = null;
        }

        data = new double[va.length][va[0].length];
        for(int i=0;i<va.length;i++)
            System.arraycopy(va[i], 0, data[i], 0, va[i].length);
    }

    public String printHTML(String html) {
        html += "<b>" + title + ":</b><br><table>";

        if(namesHoritz != null){
            html += "<tr><td></td>";
            for(int i=0;i<namesHoritz.length;i++){
                html += "<th>" + namesHoritz[i] + "</th>";
            }
            html += "</tr>";
        }
        
        for(int i=0;i<data.length;i++){
            html += "<tr>";
            if(namesVertic != null) html += "<th>" + namesVertic[i] + "</th>";
            else html += "<td></td>";
            for(int j=0;j<data[0].length;j++){
                html += "<td align='right'>" + decimalFormat.format(data[i][j]) + "</td>";
            }
            html += "</tr>";
        }
        html += "</table>";

        return html;
    }

    public void printText(Writer b) throws IOException{

        b.write(title+"\n");
        for(int i=0;i<namesHoritz.length;i++){
            b.write(separator + namesHoritz[i]);
        }
        b.write("\n");
        for(int i=0;i<data.length;i++){
            b.write(namesVertic[i]);
            for(int j=0;j<data[0].length;j++){
                b.write(separator + decimalFormat.format(data[i][j]));
            }
            b.write("\n");
        }
  
    }

}
