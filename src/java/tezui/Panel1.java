/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tezui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Iterator;
import javafx.scene.media.AudioClip;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import model.Aircraft;
import model.AircraftExt;
import model.ImportAircraftExt;
import model.Solution;
import sorting.GenerateInitialPopulation;
import sorting.PopulationHeuristic;

/**
 *
 * @author ulakbim
 */
public class Panel1 extends javax.swing.JPanel {

    /**
     * Creates new form Panel1
     */
    public Panel1() {
        initComponents();
        Test3("/Users/ulakbim/Downloads/Aircraftlanding/airland1.txt", 100, 5000);
    }
    ArrayList<Aircraft> bestSol = null;
    private ImportAircraftExt ia;
    private ArrayList<Aircraft> list;
    private Solution best;
    private int populationSize, iterations;

    public void Test3(String filename, int populationSize, int iterations) {
        this.populationSize = populationSize;
        this.iterations = iterations;
        best = new Solution();
        ia = new ImportAircraftExt(filename);
        list = ia.getAircraftList();
        firstIteration();
    }

    public void firstIteration() {
        Solution bestGip = new Solution();
        Solution temp = new Solution();

        int k = 0;
        while (k++ < 10) {
            GenerateInitialPopulation gip = new GenerateInitialPopulation(populationSize, list);
            PopulationHeuristic ph = new PopulationHeuristic(gip.getPopulation(), "scheduled");

            int j = 0;

            Solution sol;
            bestGip = gip.getBestSolution();

            if (bestGip.getFitness() < best.getFitness()) {
                best = bestGip;
            }

            while (j < iterations) {
                sol = ph.createChild();
                ph.populationReplacementExt(sol);
                if (sol.getUnfitness() == 0) {
                    temp = sol;
                    if (sol.getFitness() < best.getFitness()) {
                        best = sol;
                        j = 0;
                    }
                }
                j++;
            }
        }
        if (best.getUnfitness() == 0) {
            tightening(best);
        } else {
            tightening(temp);
        }
    }

    public void tightening(Solution sol) {
        Iterator<Aircraft> it = sol.getList().iterator();

        while (it.hasNext()) {
            AircraftExt air = (AircraftExt) it.next();
            if (air.getScheduledLandingTime() > air.getTargetLandingTime()) {
                air.setLatestLandingTime(air.getScheduledLandingTime());
            } else if (air.getScheduledLandingTime() < air.getTargetLandingTime()) {
                air.setEarliestLandindgTime(air.getScheduledLandingTime());
            }
        }
        iterate(sol);
    }

    public void iterate(Solution solution) {
        Solution bestGip = new Solution();
        Solution temp = new Solution();
        int k = 0;

        while (k++ < 10) {
            GenerateInitialPopulation gip = new GenerateInitialPopulation(populationSize, solution.getList());
            PopulationHeuristic ph = new PopulationHeuristic(gip.getPopulation(), "scheduled");

            int j = 0;

            Solution sol;
            bestGip = gip.getBestSolution();

            if (bestGip.getFitness() < best.getFitness() && bestGip.getUnfitness() == 0) {
                best = bestGip;
            }

            while (j < iterations) {
                sol = ph.createChild();
                ph.populationReplacementExt(sol);
                if (sol.getUnfitness() == 0) {
                    temp = sol;
                    if (sol.getFitness() < best.getFitness()) {
                        best = sol;
                        j = 0;
                    }
                }
                j++;
            }
        }
        if (best.getFitness() < solution.getFitness() && best.getUnfitness() == 0) {
            tightening(best);
        } else if (temp.getFitness() < solution.getFitness()) {
            tightening(temp);
        } else {
            System.out.println("Best solution: " + best.getFitness());
            System.out.println(best.toString());
            numberOfPlanesLbl.setText(best.getList().size()+ "");
            fitnessLbl.setText(best.getFitness()+"");
            bestSol = best.getList();
            DefaultTableModel model = (DefaultTableModel) resultTable.getModel();
            for (Aircraft aircraft : best.getList()) {
                model.addRow(new Object[]{aircraft.getApperanceTime(), aircraft.getEarliestLandindgTime(), aircraft.getLatestLandingTime(), aircraft.getScheduledLandingTime(), aircraft.getNumber()});
            }

        }
    }
    
    
    int planeCount = 0;
    int timeCount = 0;
    int planesInPanel = 0;
    public void visualizeResult() {
        planeCount = 0;
        timeCount = 0;
        try {
            for (int i = 0; i <= bestSol.get(bestSol.size() - 1).getScheduledLandingTime(); i++) {

                timeLbl.setText(i + "");
                planesOnAirPanel.removeAll();
                planesDownPanel.removeAll();
                planesInPanel = 0;
                for (Aircraft aircraft : bestSol) {
                    if (aircraft.getApperanceTime() <= i && aircraft.getScheduledLandingTime() > i) {
                        JLabel lbl = new JLabel();
                        lbl.setPreferredSize(new Dimension(70, 70));
                        lbl.setBounds(planesOnAirPanel.getComponentCount() * 70, 0, 70, 70);
                        lbl.setText(""+aircraft.getNumber());
                        if(aircraft.getEarliestLandindgTime() <= i){//uçak inebilir
                            lbl.setIcon(new ImageIcon(getClass().getResource("plane_green.png")));
                        }else{
                            lbl.setIcon(new ImageIcon(getClass().getResource("plane_red.png")));
                        }
                        
                        planesOnAirPanel.add(lbl);
                        
                        planeCount++;
                    }
                    if(aircraft.getScheduledLandingTime()<=i){
                        JLabel lbl = new JLabel();
                        lbl.setPreferredSize(new Dimension(70, 70));
                        lbl.setBounds(planesDownPanel.getComponentCount() * 70, 0, 70, 70);
                        lbl.setText(""+aircraft.getNumber());
                        lbl.setIcon(new ImageIcon(getClass().getResource("plane_black.png")));
                        planesDownPanel.add(lbl);
                    }
                    
                }
                revalidate();
                repaint();
                Thread.sleep(1000);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        numberOfPlanesLbl = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        fitnessLbl = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        resultTable = new javax.swing.JTable(){

            public Component prepareRenderer(TableCellRenderer renderer,
                int row, int column) {
                JLabel label = (JLabel) super.prepareRenderer(renderer, row, column);

                if(bestSol == null){
                    label.setBackground(Color.YELLOW);
                }else{
                    if(column == 3){
                        System.out.println(bestSol.get(row).getApperanceTime() + "," + bestSol.get(row).getLatestLandingTime() +","+ bestSol.get(row).getApperanceTime()+","+bestSol.get(row).getEarliestLandindgTime());
                        if(bestSol.get(row).getScheduledLandingTime()<=bestSol.get(row).getLatestLandingTime() && bestSol.get(row).getScheduledLandingTime()>=bestSol.get(row).getEarliestLandindgTime()){

                            label.setBackground(Color.GREEN);
                        }else{
                            label.setBackground(Color.RED);
                        }
                    }else{
                        label.setBackground(Color.GRAY);
                    }

                }
                return label;
            }
        };
        timeLbl = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        planesOnAirPanel = new javax.swing.JPanel();
        planesDownPanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();

        jLabel1.setText("Uçak Sayısı:");

        numberOfPlanesLbl.setText("0");

        jLabel3.setText("Fitness:");

        fitnessLbl.setText("0");

        resultTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Appearance Time", "Earliest Landing Time", "Latest Landing Time", "Landing Time", "Number"
            }
        ));
        jScrollPane1.setViewportView(resultTable);

        timeLbl.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        timeLbl.setText("0");

        jButton1.setText("Simülasyonu Başlat");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        planesOnAirPanel.setBackground(new java.awt.Color(102, 204, 255));

        javax.swing.GroupLayout planesOnAirPanelLayout = new javax.swing.GroupLayout(planesOnAirPanel);
        planesOnAirPanel.setLayout(planesOnAirPanelLayout);
        planesOnAirPanelLayout.setHorizontalGroup(
            planesOnAirPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        planesOnAirPanelLayout.setVerticalGroup(
            planesOnAirPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 94, Short.MAX_VALUE)
        );

        planesDownPanel.setBackground(new java.awt.Color(153, 153, 153));

        javax.swing.GroupLayout planesDownPanelLayout = new javax.swing.GroupLayout(planesDownPanel);
        planesDownPanel.setLayout(planesDownPanelLayout);
        planesDownPanelLayout.setHorizontalGroup(
            planesDownPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        planesDownPanelLayout.setVerticalGroup(
            planesDownPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 80, Short.MAX_VALUE)
        );

        jLabel2.setText("Havada Bekleyen Uçaklar");

        jLabel4.setText("İnmiş Uçaklar");

        jLabel7.setText("Zaman:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 845, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel3)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(fitnessLbl))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(numberOfPlanesLbl)))
                                .addGap(0, 750, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGap(262, 262, 262)
                        .addComponent(jLabel7)
                        .addGap(18, 18, 18)
                        .addComponent(timeLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1))
                    .addComponent(planesOnAirPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(planesDownPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel4))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton1)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(timeLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel7)))
                .addGap(38, 38, 38)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(planesOnAirPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 165, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(planesDownPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(numberOfPlanesLbl))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(fitnessLbl))
                .addGap(34, 34, 34)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        

        Thread t = new Thread(new ActuallyDoStuff());
        t.start();
    }//GEN-LAST:event_jButton1ActionPerformed

    public class ActuallyDoStuff implements Runnable {

        public void run() {
            visualizeResult();
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel fitnessLbl;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel numberOfPlanesLbl;
    private javax.swing.JPanel planesDownPanel;
    private javax.swing.JPanel planesOnAirPanel;
    private javax.swing.JTable resultTable;
    private javax.swing.JLabel timeLbl;
    // End of variables declaration//GEN-END:variables
}
