/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hangman;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author West
 */
public class HangmanServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private Game game = new Game();
    private String cookie = "0";
    static Random generator = new Random();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String uri = request.getRequestURI().toString();
        if (cookie.equals("0")) {
            //Start New Game
            cookie = generateCookie();
            game.startNewGame();
            try (PrintWriter out = response.getWriter()) {
                out.println("<!DOCTYPE html><html><head><title>MyHttpServer</title></head><body><h2>Hangman</h2>"
                        + "<img src=\"" + "h" + game.getState() + ".gif" + "\">"
                        + "<h2 style=\"font-family:'Lucida Console', monospace\"> " + game.getDisplayWord() + "</h2>"
                        + "<form action=\"HangmanServlet\" method=\"get\"> "
                        + "Guess a character <input type=\"text\" name=\"guess\"><br>"
                        + "<input type=\"submit\" value=\"Submit\">" + "</form></body></html>");
            }
        } else {
            // continue with current game
            // error check input 
            String guess = request.getParameter("guess");
            int result;
            if (guess.length() > 1) {
                //ERROR too long
                result = 4;
            } else {
                if (!guess.matches("[a-zA-Z]+")) {
                    //ERROR enter alpha
                    result = 4;
                } else {
                    //no error
                    result = game.playGame(guess.charAt(0));

                }
            }

            switch (result) {
                case 0: // good guess, continue game
                    try (PrintWriter out = response.getWriter()) {
                        out.println("<!DOCTYPE html><html><head><title>Hangman!</title></head><body><h2>Hangman</h2>"
                                + "<img src=\"" + "h" + game.getState() + ".gif" + "\">"
                                + "<h2 style=\"font-family:'Lucida Console', monospace\"> " + game.getDisplayWord() + "</h2>"
                                + "<form action=\"HangmanServlet\" method=\"get\"> "
                                + "Guess a character <input type=\"text\" name=\"guess\"><br>"
                                + "<input type=\"submit\" value=\"Submit\">" + "</form></body></html>");
                        break;
                    }
                case 1: // good guess, win game
                    try (PrintWriter out = response.getWriter()) {
                        out.println("<!DOCTYPE html><html><head><title>Hangman!</title></head><body><h2>Hangman</h2>"
                                + "<img src=\"" + "h" + game.getState() + ".gif" + "\">"
                                + "<h2 style=\"font-family:'Lucida Console', monospace\"> " + "</h2>"
                                + "<h2>Congratulations you win!</h2>" + "</body></html>");
                        cookie = "0";
                        break;
                    }
                case 2: // bad guess, continue game
                    try (PrintWriter out = response.getWriter()) {
                        out.println("<!DOCTYPE html><html><head><title>Hangman!</title></head><body><h2>Hangman</h2>"
                                + "<img src=\"" + "h" + game.getState() + ".gif" + "\">"
                                + "<h2 style=\"font-family:'Lucida Console', monospace\"> " + game.getDisplayWord() + "</h2>"
                                + "<form action=\"HangmanServlet\" method=\"get\"> "
                                + "Guess a character <input type=\"text\" name=\"guess\"><br>"
                                + "<input type=\"submit\" value=\"Submit\">" + "</form></body></html>");
                        break;
                    }
                case 3: // bad guess, lost game
                    try (PrintWriter out = response.getWriter()) {
                        out.println("<!DOCTYPE html><html><head><title>Hangman!</title></head><body><h2>Hangman</h2>"
                                + "<img src=\"" + "h7.gif" + "\">" + "<h2>You lost!  The word is " + game.getWord() + "</h2>"
                                + "</body></html>");
                        cookie = "0";
                        break;
                    }
                case 4: // invalid guess
                    try (PrintWriter out = response.getWriter()) {
                        out.println("<!DOCTYPE html><html><head><title>Hangman!</title></head><body><h2>Hangman</h2>"
                                + "<img src=\"" + "h" + game.getState() + ".gif" + "\">"
                                + "<h2 style=\"font-family:'Lucida Console', monospace\"> " + game.getDisplayWord() + "</h2>"
                                + "<form action=\"HangmanServlet\" method=\"get\"> "
                                + "Guess a character <input type=\"text\" name=\"guess\"><br>"
                                + "<input type=\"submit\" value=\"Submit\">" + "</form>"
                                + "</body>" + "<div style=\"font-family:'Lucida Console', monospace\"> " + "</div>"
                                + "<div>Error. Enter a single letter only please.</div>" + "</html>");
                        break;
                    }
            }
        }

    }

    private String generateCookie() {
        return Long.toString(generator.nextLong());
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
