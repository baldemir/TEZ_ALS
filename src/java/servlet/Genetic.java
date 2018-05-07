/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import com.google.gson.Gson;
import java.awt.Dimension;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableModel;
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
@WebServlet(name = "Genetic", urlPatterns = {"/Genetic"})
public class Genetic extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Gson gs = new Gson();
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            Test3("/Users/ulakbim/Downloads/Aircraftlanding/airland1.txt", 100, 5000);
            out.println(gs.toJson(bestSol));
        }catch (Exception e) {
            out.print(e.getMessage());
            out.close();
        }
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
            bestSol = best.getList();
        }
    }

    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
