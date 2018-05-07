/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tezui;

import greedy.AircraftGreedy;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import javafx.scene.media.AudioClip;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import model.AircraftExt;
import model.ImportAircraftExt;
import model.Solution;
import sorting.GenerateInitialPopulation;
import sorting.PopulationHeuristic;

/**
 *
 * @author ulakbim
 */
public class GreedyImproved extends javax.swing.JPanel {

    /**
     * Creates new form Panel1
     */
    public GreedyImproved() {
        initComponents();
        List<Integer> waitingQueue = new ArrayList<>();
        int[] ints = {1, 1, 2, 1, 3, 2, 3};
        //int[] ints = {1, 1, 3, 2, 3, 2, 2};
        for (int i : ints) {
            waitingQueue.add(i);
        }
        WAITING_TIMES = createSample(10);
        greedyImproved(createSample(10), waitingQueue, 10);
        
        
    }
    private static int[][] WAITING_TIMES;

    public static int[][] createSample(int typeN) {
//        int[][] sample = new int[typeN][typeN];
//        Random rand = new Random();

//        for (int i = 0; i < typeN; i++) {
//            for (int j = 0; j < typeN; j++) {
//                int randomNum = rand.nextInt(15);
//                sample[i][j] = randomNum;
//            }
//        }
        //int[][] sample = {{2, 1}, {14, 8}};
        //int[][] sample = {{99999,3,15,15,15,15,15,15,15,15},{3,99999,15,15,15,15,15,15,15,15},{15,15,99999,8,8,8,8,8,8,8},{15,15,8,99999,8,8,8,8,8,8},{15,15,8,8,99999,8,8,8,8,8},{15,15,8,8,8,99999,8,8,8,8},{15,15,8,8,8,8,99999,8,8,8},{15,15,8,8,8,8,8,99999,8,8},{15,15,8,8,8,8,8,8,99999,8},{15,15,8,8,8,8,8,8,8,99999}};
//        int[][] sample = {
//            {1, 5, 1},
//            {4, 2, 3},
//            {3, 17, 4}};
        int[][] sample = {
            {1, 5, 4},
            {3, 7, 2},
            {6, 4, 1}};

        return sample;
    }
    static List<AircraftGreedy> bestSol = null;
    private ImportAircraftExt ia;
    private ArrayList<AircraftGreedy> list;
    private Solution best;
    private int populationSize, iterations;

    int planeCount = 0;
    int timeCount = 0;
    int planesInPanel = 0;
    
    public static int greedyImproved(int[][] waitingTimes, List<Integer> waitingQueue, int N) {
        int latestTimes[] = {5, 10, 7, 12, 18, 15, 20};
        int min = Integer.MAX_VALUE;
        int minI = 0, minJ = 0;
        int counter = 0;
        int l = -1;
        List<AircraftGreedy> planes = new ArrayList<>();
        for (int i = 0; i < waitingQueue.size(); i++) {
            AircraftGreedy plane = new AircraftGreedy();
            plane.setOrder(-1);
            plane.setType(waitingQueue.get(i) - 1);
            plane.setLatestLandingTime(latestTimes[i]);
            plane.setLanded(false);
            planes.add(plane);
        }

        int[] typeCounts = new int[N];//k
        int p = 0;
        int T = 0;
        for (int i = 0; i < typeCounts.length; i++) {
            typeCounts[i] = 0;
        }
        //fill typeCounts matrix
        for (int i = 0; i < waitingQueue.size(); i++) {
            typeCounts[waitingQueue.get(i) - 1] += 1;
        }

        while (true) {
            //find min a(tl)
            for (int i = 0; i < waitingTimes.length; i++) {
                for (int j = 0; j < waitingTimes.length; j++) {
                    if (waitingTimes[i][j] < min) {
                        min = waitingTimes[i][j];
                        minI = i;
                        minJ = j;
                    }
                }
            }
            if (typeCounts[minI] == 0 || typeCounts[minJ] == 0) {
                waitingTimes[minI][minJ] = Integer.MAX_VALUE;
            } else {

                int expiring = isAnyExpiringAircraft(T, planes, waitingTimes, minI, l);
                if (expiring == -1) {

                    int planeIndex = getWaitingAircraftWithType(planes, minI);
                    planes.get(planeIndex).setLanded(true);
                    planes.get(planeIndex).setOrder(counter);
                    counter++;
                    typeCounts[minI] -= 1;
                    System.out.println("down: " + minI);
                    l = minI;
                    expiring = isAnyExpiringAircraft(T, planes, waitingTimes, minJ, l);
                    if (expiring == -1) {
                        planeIndex = getWaitingAircraftWithType(planes, minJ);
                        planes.get(planeIndex).setLanded(true);
                        planes.get(planeIndex).setOrder(counter);
                        counter++;
                        System.out.println("down: " + minJ);
                        T += waitingTimes[l][minJ];
                        planes.get(planeIndex).setActualLandingTime(T);
                        l = minJ;
                        typeCounts[minJ] -= 1;

                        p = 3;
                    } else {
                        planeIndex = expiring;
                        planes.get(planeIndex).setLanded(true);
                        planes.get(planeIndex).setOrder(counter);
                        counter++;
                        T += waitingTimes[minI][planes.get(planeIndex).getType()];
                        planes.get(planeIndex).setActualLandingTime(T);
                        typeCounts[planes.get(planeIndex).getType()] -= 1;
                        System.out.println("down: " + planes.get(planeIndex).getType());
                        l = planes.get(planeIndex).getType();
                    }
                } else {
                    int planeIndex = expiring;
                    planes.get(planeIndex).setLanded(true);
                    planes.get(planeIndex).setOrder(counter);
                    counter++;
                    typeCounts[planes.get(planeIndex).getType()] -= 1;
                    System.out.println("down: " + planes.get(planeIndex).getType());
                    T += waitingTimes[l][planes.get(planeIndex).getType()];
                    planes.get(planeIndex).setActualLandingTime(T);
                    l = planes.get(planeIndex).getType();
                }
                break;
            }
        }

        while (true) {
            //find min in waiting times
            min = Integer.MAX_VALUE;

            for (int j = 0; j < waitingTimes[l].length; j++) {
                if (waitingTimes[l][j] < min) {
                    min = waitingTimes[l][j];
                    minI = l;
                    minJ = j;
                }
            }
            if (min == Integer.MAX_VALUE) {
                break;
            }
            if (typeCounts[minJ] > 0) {
                int expiring = isAnyExpiringAircraft(T, planes, waitingTimes, minJ, l);
                if (expiring == -1) {
                    typeCounts[minJ] -= 1;
                    int planeIndex = getWaitingAircraftWithType(planes, minJ);
                    planes.get(planeIndex).setLanded(true);
                    planes.get(planeIndex).setOrder(counter);
                    counter++;
                    System.out.println("down: " + minJ);
                    p++;
                    T += waitingTimes[l][minJ];
                    planes.get(planeIndex).setActualLandingTime(T);
                    l = minJ;
                } else {
                    int planeIndex = expiring;
                    planes.get(planeIndex).setLanded(true);
                    planes.get(planeIndex).setOrder(counter);
                    counter++;
                    T += waitingTimes[l][planes.get(planeIndex).getType()];
                    typeCounts[planes.get(planeIndex).getType()] -= 1;
                    System.out.println("down: " + planes.get(planeIndex).getType());
                    planes.get(planeIndex).setActualLandingTime(T);
                    l = planes.get(planeIndex).getType();
                }

            } else {
                waitingTimes[l][minJ] = Integer.MAX_VALUE;
            }
            if (p > waitingQueue.size()) {
                break;
            }
        }
        System.out.println("=" + T);
        Collections.sort(planes);
        for (AircraftGreedy a : planes) {
            System.out.println(a.getOrder() + " " + a.getType() + "-" + a.getLatestLandingTime() + " " + a.getActualLandingTime());
        }
        bestSol = planes;
        return 0;
    }
    
    public static int isAnyExpiringAircraft(int time, List<AircraftGreedy> planes, int[][] waitingTimes, int candidateType, int lastPlane) {
        int[] waitingTimesTmp = Arrays.copyOf(waitingTimes[candidateType], waitingTimes[candidateType].length);
        int min = Integer.MAX_VALUE;
        int minType = Integer.MAX_VALUE;
        for (int j = 0; j < waitingTimesTmp.length; j++) {
            if (waitingTimesTmp[j] < min) {
                minType = j;
                min = waitingTimesTmp[j];
            }
        }
        int timeBeforeCandidate = 0;
        if(lastPlane !=  -1){
            timeBeforeCandidate = waitingTimes[lastPlane][candidateType];
        }
        

        for (int i = 0; i < planes.size(); i++) {
            int timeAfterCandidate = waitingTimes[candidateType][planes.get(i).getType()];
            if (!planes.get(i).isLanded() && (planes.get(i).getLatestLandingTime() <= time || planes.get(i).getLatestLandingTime() <= time + timeBeforeCandidate + timeAfterCandidate)) {
                return i;
            }
        }
        return -1;
    }

    public static int greedyWithObjects(int[][] waitingTimes, List<Integer> waitingQueue, int N) {

        List<AircraftGreedy> planes = new ArrayList<>();
        for (int i = 0; i < waitingQueue.size(); i++) {
            AircraftGreedy plane = new AircraftGreedy();
            plane.setOrder(-1);
            plane.setType(waitingQueue.get(i) - 1);
            plane.setLanded(false);
            planes.add(plane);
        }
        
        int order = 0;
        int min = Integer.MAX_VALUE;
        int minI = 0, minJ = 0;

        int[] typeCounts = new int[N];//k
        int p = 0;
        int T = 0;
        for (int i = 0; i < typeCounts.length; i++) {
            typeCounts[i] = 0;
        }
        //fill typeCounts matrix
        for (int i = 0; i < waitingQueue.size(); i++) {
            typeCounts[waitingQueue.get(i) - 1] += 1;
        }

        while (true) {
            //find min a(tl)
            for (int i = 0; i < waitingTimes.length; i++) {
                for (int j = 0; j < waitingTimes.length; j++) {
                    if (waitingTimes[i][j] < min) {
                        min = waitingTimes[i][j];
                        minI = i;
                        minJ = j;
                    }
                }
            }
            if (typeCounts[minI] == 0 || typeCounts[minJ] == 0) {
                waitingTimes[minI][minJ] = Integer.MAX_VALUE;
            } else {
                System.out.println("down: " + minI + "-" + minJ);
                int a1 = getWaitingAircraftWithType(planes, minI);

                planes.get(a1).setActualLandingTime(T);
                planes.get(a1).setLanded(true);
                planes.get(a1).setOrder(order++);
                typeCounts[minI] -= 1;
                typeCounts[minJ] -= 1;
                T += waitingTimes[minI][minJ];
                int a2 = getWaitingAircraftWithType(planes, minI);
                planes.get(a2).setActualLandingTime(T);
                planes.get(a2).setLanded(true);
                planes.get(a2).setOrder(order++);
                p = 3;

                break;
            }
        }
        int l = minJ;
        while (true) {
            //find min in waiting times
            min = Integer.MAX_VALUE;

            for (int j = 0; j < waitingTimes[l].length; j++) {
                if (waitingTimes[l][j] < min) {
                    min = waitingTimes[l][j];
                    minI = l;
                    minJ = j;
                }
            }
            if (min == Integer.MAX_VALUE) {
                break;
            }
            if (typeCounts[minJ] > 0) {
                typeCounts[minJ] -= 1;
                System.out.println("down: " + minJ);
                p++;
                T += waitingTimes[l][minJ];
                int a1 = getWaitingAircraftWithType(planes, minJ);
                planes.get(a1).setLanded(true);
                planes.get(a1).setActualLandingTime(T);
                planes.get(a1).setOrder(order++);
                l = minJ;
            } else {
                waitingTimes[l][minJ] = Integer.MAX_VALUE;
            }
            if (p > waitingQueue.size()) {
                break;
            }
        }
        System.out.println("=" + T);
        System.out.println(planes.get(0).getActualLandingTime());
        bestSol = planes;
        return 0;
    }

    public static int getWaitingAircraftWithType(List<AircraftGreedy> planes, int type) {
        for (int i = 0; i < planes.size(); i++) {
            if (planes.get(i).getType() == type && !planes.get(i).isLanded()) {
                return i;
            }
        }
        return -1;
    }

    public void visualizeResult(List<AircraftGreedy> planes) {
        numberOfPlanesLbl.setText(planes.size()+"");
        planeCount = 0;
        timeCount = 0;
        int lastLandingPlaneOrder = -1;
        int lastLandingPlaneType = -1;
        AircraftGreedy lastLandingPlane =null;
        int maxLandingTime = Integer.MIN_VALUE;
        for (AircraftGreedy plane : planes) {
            if (plane.getActualLandingTime() > maxLandingTime) {
                maxLandingTime = plane.getActualLandingTime();
            }
        }
        totalTimeLbl.setText(maxLandingTime+"");
        try {
            for (int i = 0; i <= maxLandingTime; i++) {
                timeLbl.setText(i + "");
                planesOnAirPanel.removeAll();
                planesDownPanel.removeAll();
                planesInPanel = 0;
                for (AircraftGreedy aircraft : planes) {
                    if (aircraft.getActualLandingTime() > i) {
                        JLabel lbl = new JLabel();
                        lbl.setPreferredSize(new Dimension(70, 70));
                        lbl.setBounds(planesOnAirPanel.getComponentCount() * 70, 0, 70, 70);
                        lbl.setText("" + aircraft.getType());
                        if(lastLandingPlane == null || lastLandingPlane.getActualLandingTime() + WAITING_TIMES[lastLandingPlaneType][aircraft.getType()] <= i){//uçak inebilir
                            lbl.setIcon(new ImageIcon(getClass().getResource("plane_green.png")));
                        }else{
                            lbl.setIcon(new ImageIcon(getClass().getResource("plane_red.png")));
                        }

                        planesOnAirPanel.add(lbl);

                        planeCount++;
                    }
                }
                List<AircraftGreedy> orderedPlanes = cloneList(planes);
                Collections.sort(orderedPlanes, new Comparator<AircraftGreedy>() {

                    public int compare(AircraftGreedy o1, AircraftGreedy o2) {
                        return o1.getOrder()-o2.getOrder();
                    }
                });
                for (AircraftGreedy aircraft : orderedPlanes) {
                    if (aircraft.getActualLandingTime() <= i) {
                        JLabel lbl = new JLabel();
                        lbl.setPreferredSize(new Dimension(70, 70));
                        lbl.setBounds(planesDownPanel.getComponentCount() * 70, 0, 70, 70);
                        lbl.setText("" + aircraft.getType());
                        if(aircraft.getActualLandingTime()<=aircraft.getLatestLandingTime()){
                            lbl.setIcon(new ImageIcon(getClass().getResource("plane_black.png")));
                        }else{
                            lbl.setIcon(new ImageIcon(getClass().getResource("plane_red.png")));
                        }
                        planesDownPanel.add(lbl);
                        if (aircraft.getOrder() > lastLandingPlaneOrder) {
                            lastLandingPlaneOrder = aircraft.getOrder();
                            lastLandingPlaneType = aircraft.getType();
                            lastLandingPlane = aircraft;
                        }
                    }
                }

                revalidate();
                repaint();
                Thread.sleep(4000);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public static List<AircraftGreedy> cloneList(List<AircraftGreedy> list) {
        List<AircraftGreedy> clone = new ArrayList<AircraftGreedy>(list.size());
        for (AircraftGreedy item : list) {
            clone.add(item);
        }
        return clone;
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
        totalTimeLbl = new javax.swing.JLabel();
        timeLbl = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        planesOnAirPanel = new javax.swing.JPanel();
        planesDownPanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();

        jLabel1.setText("Uçak Sayısı:");

        numberOfPlanesLbl.setText("0");

        jLabel3.setText("Toplam Geçen Süre");

        totalTimeLbl.setText("0");

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
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(totalTimeLbl))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(numberOfPlanesLbl)))
                        .addContainerGap(675, Short.MAX_VALUE))
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
                    .addComponent(totalTimeLbl))
                .addGap(247, 247, 247))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:

        Thread t = new Thread(new ActuallyDoStuff());
        t.start();
    }//GEN-LAST:event_jButton1ActionPerformed

    public class ActuallyDoStuff implements Runnable {

        public void run() {
            visualizeResult(bestSol);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel numberOfPlanesLbl;
    private javax.swing.JPanel planesDownPanel;
    private javax.swing.JPanel planesOnAirPanel;
    private javax.swing.JLabel timeLbl;
    private javax.swing.JLabel totalTimeLbl;
    // End of variables declaration//GEN-END:variables
}
