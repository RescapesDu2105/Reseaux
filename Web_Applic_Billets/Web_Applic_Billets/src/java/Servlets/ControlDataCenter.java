package Servlets;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import database.utilities.Bean_DB_Access;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Philippe
 */
public class ControlDataCenter extends HttpServlet {
    private Bean_DB_Access BD_airport = new Bean_DB_Access(Bean_DB_Access.DRIVER_MYSQL, "localhost", "3306", "Zeydax", "1234", "bd_airport");
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param config
     * @throws ServletException if a servlet-specific error occurs
     */
    
    @Override
    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);
        ServletContext sc = getServletContext();
        sc.log("-- démarrage de la servlet ControlDataCenter");
        System.out.println("Démarrage de la Servlet ControlDataCenter");
    }
    
    @Override
    public void destroy() { }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
        HttpSession session = request.getSession(true); 
        String Action = request.getParameter("action");
        
        switch (Action) 
        {
            case "Authentification":
                Authentification(request, response, session);
                break;
            case "Deconnexion":
                session.invalidate();
                response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/Web_Applic_Billets");
                break;
            case "AjoutPanier":
                AjoutPanier(request, response, session);
                break;
            case "VoirPanier":
                response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/Web_Applic_Billets/JSPPay.jsp");
                break;
            case "RetourCaddie":
                response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/Web_Applic_Billets/JSPCaddie.jsp");
                break;
            case "RetirerPanier":
                RetirerPanier(request, response, session);
                break;
            case "RetirerToutPanier":
                RetirerToutPanier(request, response, session);
                break;
            case "Payer":
                Payer(request, response, session);
                break;
            case "AnnulerPaiement":
                response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/Web_Applic_Billets/JSPPay.jsp");
                break;
            case "ConfirmerPaiement":
                break;
            default:
                break;
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
    
    
    private void Authentification(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException 
    {
        if(request.getParameterMap().containsKey("inputLogin"))
        {   
            if(request.getParameter("inputPassword").isEmpty())
            {
                session.setAttribute("ErrorLogin", "Le champ du mot de passe ne peut pas être vide !");
                response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/Web_Applic_Billets");
            }
            else
            {                                    
                if (request.getParameter("Inscription") != null)
                {
                    if(request.getParameter("inputPassword").length() < 4)
                    {                        
                        session.setAttribute("ErrorLogin", "La longueur du mot de passe doit être supérieur à 4 !");
                        response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/Web_Applic_Billets");
                    }
                    else if (request.getParameter("inputNom").isEmpty())
                    {
                        session.setAttribute("ErrorLogin", "Le champ du nom de famille ne peut être vide !");
                        response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/Web_Applic_Billets");
                    }
                    else if (request.getParameter("inputPrenom").isEmpty())
                    {
                        session.setAttribute("ErrorLogin", "Le champ du prénom ne peut être vide !");
                        response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/Web_Applic_Billets");
                    }
                    else
                    {
                        HashMap<String, Object> hm = new HashMap<>();
                        hm.put("Login", request.getParameter("inputLogin"));
                        hm.put("Password", request.getParameter("inputPassword"));
                        hm.put("Nom", request.getParameter("inputNom"));
                        hm.put("Prenom", request.getParameter("inputPrenom"));

                        ConnexionClient(request, response, session, hm);
                    }                                                
                }
                else
                {                        
                    ConnexionClient(request, response, session);
                }
            }
        }
        else
        {            
            session.setAttribute("ErrorLogin", "Veuillez remplir les champs de connexion !");                            
            response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/Web_Applic_Billets");
        }
    }
    private void ConnexionClient(HttpServletRequest request, HttpServletResponse response, HttpSession session, HashMap<String, Object> InscriptionClient) throws IOException
    {
        ResultSet RS; 
        
        BD_airport.Connexion();   
        try
        {
            BD_airport.Insert("Clients", InscriptionClient);
            RS = BD_airport.Select("SELECT IdClient, Nom, Prenom FROM Clients WHERE Login = \"" + request.getParameter("inputLogin") + "\" AND Password = \"" + request.getParameter("inputPassword") + "\"");
            boolean isResults = RS.next();
            if (isResults)                        
            {
                session.setAttribute("IdClient", RS.getInt("IdClient")); 
                session.setAttribute("Nom", RS.getString("Nom"));
                session.setAttribute("Prenom", RS.getString("Prenom"));
                
                RS = BD_airport.Select("SELECT COUNT(*) FROM Promesses WHERE IdClient = " + session.getAttribute("IdClient"));
                if (RS.next())
                    session.setAttribute("NbItemsPanier", RS.getInt(1));
                else
                    session.setAttribute("NbItemsPanier", 0);
                
                session.setAttribute("Connected", true);
                
                response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/Web_Applic_Billets/JSPCaddie.jsp");
            }
            else
            {
                session.setAttribute("ErrorLogin", "Le nom d'utilisateur ou le mot de passe est incorrect !");    
                response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/Web_Applic_Billets");
            }     
            RS.close();
        } 
        catch (SQLException ex) 
        {
            System.out.println("ex = " + ex.getMessage());
            session.setAttribute("ErrorLogin", "Le nom d'utilisateur existe déjà !"); 
            response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/Web_Applic_Billets");
        }          
        BD_airport.Deconnexion();
    }
    private void ConnexionClient(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException
    {
        ResultSet RS; 
        
        BD_airport.Connexion();
        try
        {
            RS = BD_airport.Select("SELECT IdClient, Nom, Prenom FROM Clients WHERE Login = \"" + request.getParameter("inputLogin") + "\" AND Password = \"" + request.getParameter("inputPassword") + "\"");
            boolean isResults = RS.next();
            if (isResults)                        
            {
                session.setAttribute("IdClient", RS.getInt("IdClient")); 
                session.setAttribute("Nom", RS.getString("Nom"));
                session.setAttribute("Prenom", RS.getString("Prenom"));
                
                RS = BD_airport.Select("SELECT COUNT(*) FROM Promesses WHERE IdClient = " + session.getAttribute("IdClient"));
                if (RS.next())
                    session.setAttribute("NbItemsPanier", RS.getInt(1));
                else
                    session.setAttribute("NbItemsPanier", 0);
                                
                session.setAttribute("Connected", true);

                response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/Web_Applic_Billets/JSPCaddie.jsp");
            }
            else
            {
                session.setAttribute("ErrorLogin", "Le nom d'utilisateur ou le mot de passe est incorrect !");    
                response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/Web_Applic_Billets");
            }     
            RS.close();
        } 
        catch (SQLException ex) 
        {
            session.setAttribute("ErrorLogin", "Le nom d'utilisateur ou le mot de passe est incorrect !");
            response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/Web_Applic_Billets");
        }          
        BD_airport.Deconnexion();
    }
    
    public static void ChargerVols(HttpSession session, Bean_DB_Access BD_airport) throws SQLException 
    {
        ResultSet RS;
        int i;
        
        for(i = 1 ; session.getAttribute("Vol_" + i) != null ; i++)
        {
            session.removeAttribute("Vol_" + i);
        }
                
        RS = BD_airport.Select(""
                + "SELECT IdVol, NumeroVol, NomCompagnie, Destination, HeureDepart, HeureArrivee, PlacesRestantes "
                + "FROM bd_airport.vols NATURAL JOIN bd_airport.avions NATURAL JOIN bd_airport.compagnies "
                + "WHERE bd_airport.vols.HeureDepart >= current_time() "
                + "AND bd_airport.vols.PlacesRestantes > 0 "
                + "ORDER BY HeureDepart");
        
        i = 0;
        while(RS.next())
        {
            i++;
            HashMap<String, Object> hm = new HashMap<>();
            
            hm.put("IdVol", RS.getInt("IdVol"));
            hm.put("NumeroVol", RS.getInt("NumeroVol"));
            hm.put("NomCompagnie", RS.getString("NomCompagnie"));
            hm.put("Destination", RS.getString("Destination"));
            hm.put("HeureDepart", RS.getTimestamp("HeureDepart"));
            hm.put("HeureArrivee", RS.getTimestamp("HeureArrivee"));
            hm.put("PlacesRestantes", RS.getInt("PlacesRestantes"));
            
            session.setAttribute("Vol_" + i, hm);
        }               
        RS.close();
        session.setAttribute("NbVols", i);
        
        if (i == 0)            
            session.setAttribute("Error", "Aucun vol n'est prévu dans les prochaines 24 heures !");
    }
    
    public static void MAJ_Panier(HttpSession session, Bean_DB_Access BD_airport) throws SQLException
    {
        ResultSet RS = BD_airport.Select("SELECT COUNT(*) FROM bd_airport.Promesses WHERE IdClient = " + session.getAttribute("IdClient"));
        if (RS != null)
        {        
            RS.next();
            session.removeAttribute("NbItemsPanier");
            session.setAttribute("NbItemsPanier", RS.getInt(1));
        }
    }

    private void AjoutPanier(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException 
    {
        int NbItemsPanier = (int) session.getAttribute("NbItemsPanier");
        ResultSet RS;
        Timestamp DateTimePromesse;
                
        //String IdBilletDebut = request.getParameter("NumeroVol") + "-" + request.getParameter("HeureDepart").substring(8, 10) + request.getParameter("HeureDepart").substring(5, 7) + request.getParameter("HeureDepart").substring(0, 4) + "-";
        //System.out.println("IdBilletDebut = " + IdBilletDebut);
                
        try 
        {   
            BD_airport.Connexion();        
            /*RS = BD_airport.Select(""
                    + "SELECT CONCAT('" + IdBilletDebut + "', LPAD(ids1.id + 1, 4, '0')) AS IdBillet, current_timestamp() AS DateTimePromesse "
                    + "FROM ("
                        + "SELECT CONVERT(SUBSTRING(IdBillet, 14, 18), SIGNED INTEGER) AS id "
                        + "FROM bd_airport.billets "
                        + "WHERE IdBillet LIKE '" + IdBilletDebut + "%') ids1 "
                    + "WHERE NOT EXISTS("
                        +"SELECT * "
                        + "FROM ("
                            + "SELECT CONVERT(SUBSTRING(IdBillet, 14, 18), SIGNED INTEGER) AS id "
                            + "FROM bd_airport.billets "
                            + "WHERE IdBillet LIKE '" + IdBilletDebut + "%') ids2 "
                        + "WHERE ids2.id = ids1.id + 1 "
                        + "ORDER BY ids1.id)"
                    + "LIMIT 1");*/
            
            RS = BD_airport.Select("SELECT CURRENT_TIMESTAMP() FROM DUAL");
            boolean Ok = RS.next();
            if(Ok)
            {
                DateTimePromesse = RS.getTimestamp(1);
                
                HashMap<String, Object> hm = new HashMap<>();
        
                hm.put("NbAccompagnants", Integer.parseInt(request.getParameter("NbAccompagnants")));
                hm.put("DateTimePromesse", DateTimePromesse);
                hm.put("IdClient", session.getAttribute("IdClient"));
                hm.put("IdVol", Integer.parseInt(request.getParameter("IdVol")));

                //System.out.println("hm = " + hm);
                
                BD_airport.Insert("Promesses", hm);
                RS = BD_airport.Select(""
                        + "SELECT IdPromesse "
                        + "FROM bd_airport.Promesses "
                        + "WHERE IdClient = " + session.getAttribute("IdClient") + " "
                        + "AND DateTimePromesse = '" + DateTimePromesse + "'");
                                
                int IdPromesse = -1;
                if(RS != null)
                {
                    RS.next();
                    IdPromesse = RS.getInt("IdPromesse");
                }                
                
                if(IdPromesse != -1)
                {
                    BD_airport.CreateEvent("Event_" + IdPromesse, "AT '" + DateTimePromesse + "' + INTERVAL 2 HOUR", 
                          "DELETE FROM bd_airport.Promesses "
                        + "WHERE IdPromesse = " + IdPromesse);                
                }
                
                session.removeAttribute("NbItemsPanier");
                session.setAttribute("NbItemsPanier", NbItemsPanier + 1);
            }           
            RS.close();       
        } 
        catch (SQLException ex) 
        {
            System.err.println("SQL Error : " + ex.getMessage());
            session.setAttribute("Error", "Une erreur interne s'est produite !");
        }
         
        BD_airport.Deconnexion();
        
        response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/Web_Applic_Billets/JSPCaddie.jsp");
    }

    public static void ChargerPanier(HttpSession session, Bean_DB_Access BD_airport) throws SQLException
    {
        ResultSet RS;
        
        for(int i = 1 ; session.getAttribute("Article_" + i) != null ; i++)
        {
            session.removeAttribute("Article_" + i);
        }
        
        try
        {                
            RS = BD_airport.Select("SELECT IdPromesse, DateTimePromesse, IdVol, NumeroVol, NomCompagnie, Destination, HeureDepart, HeureArrivee, NbAccompagnants "
                    + "FROM bd_airport.Promesses NATURAL JOIN bd_airport.Vols NATURAL JOIN bd_airport.avions NATURAL JOIN bd_airport.compagnies "
                    + "WHERE IdClient = '" + session.getAttribute("IdClient") + "' "
                    + "ORDER BY DateTimePromesse, IdPromesse");

            int i = 0;

            while(RS.next())
            {
                i++;
                
                HashMap<String, Object> hm = new HashMap<>();
                hm.put("IdPromesse", RS.getInt("IdPromesse"));
                hm.put("DateTimePromesse", RS.getTimestamp("DateTimePromesse"));
                hm.put("IdVol", RS.getInt("IdVol"));
                hm.put("NumeroVol", RS.getInt("NumeroVol"));
                hm.put("NomCompagnie", RS.getString("NomCompagnie"));
                hm.put("Destination", RS.getString("Destination"));
                hm.put("HeureDepart", RS.getTimestamp("HeureDepart"));
                hm.put("HeureArrivee", RS.getTimestamp("HeureArrivee"));
                hm.put("NbAccompagnants", RS.getInt("NbAccompagnants"));
                    
                session.setAttribute("Article_" + i, hm);
                System.out.println("Article_" + i + " = " + session.getAttribute("Article_" + i));
            }        
            RS.close();
        } 
        catch (SQLException ex) 
        {            
            session.setAttribute("Error", "Une erreur interne s'est produite !");
        }
    }

    private void RetirerPanier(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException 
    {
        try 
        {
            BD_airport.Connexion();
            int Ok = BD_airport.Update("DELETE FROM bd_airport.Promesses WHERE IdPromesse = '" + request.getParameter("IdPromesse") + "'");   
            if (Ok != 0)
            {
                int NbItemsPanier = (int)session.getAttribute("NbItemsPanier");
                session.removeAttribute("NbItemsPanier");
                session.setAttribute("NbItemsPanier", NbItemsPanier - 1);
            }
            else
            {
                session.setAttribute("Error", "Une erreur interne s'est produite !");
            }            
        } 
        catch (SQLException ex) 
        {
            session.setAttribute("Error", "Une erreur interne s'est produite !");
        }
    
        BD_airport.Deconnexion();        
        response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/Web_Applic_Billets/JSPPay.jsp");
    }    

    private void RetirerToutPanier(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException 
    {
        try 
        {
            BD_airport.Connexion();
            int Ok = BD_airport.Update("DELETE FROM bd_airport.Promesses WHERE IdClient = '" + session.getAttribute("IdClient") + "'");             
                      
            session.removeAttribute("NbItemsPanier");
            session.setAttribute("NbItemsPanier", 0);
                    } 
        catch (SQLException ex) 
        {
            session.setAttribute("Error", "Une erreur interne s'est produite !");
        }
  
        BD_airport.Deconnexion();        
        response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/Web_Applic_Billets/JSPPay.jsp");
    }

    private void Payer(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException 
    {
        ArrayList ArticlesAvant = new ArrayList<>(), ArticlesApres = new ArrayList<>();//, ArticlesPlusDisponibles = new ArrayList<>();

        // On sauvegarde les articles
        for(int i = 1 ; session.getAttribute("Article_" + i) != null ; i++)
        {
            ArticlesAvant.add(i-1, session.getAttribute("Article_" + i));          
        }
        
        try
        {
            // On met à jour
            BD_airport.Connexion();
            ChargerPanier(session, BD_airport);            
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(ControlDataCenter.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            BD_airport.Deconnexion();
        }
        
        for(int i = 1 ; session.getAttribute("Article_" + i) != null ; i++)
        {
            ArticlesApres.add(i-1, session.getAttribute("Article_" + i));          
        }
        
        System.out.println("ArticlesApres 1 = " + ArticlesApres);
        System.out.println("ArticlesAvant 1 = " + ArticlesAvant);
        ArticlesAvant.removeAll(ArticlesApres); // ArticlesAvant = différence s'il y en a ou rien s'il n'y a pas de différence donc si c'est OK
        
        System.out.println("ArticlesAvant 2 = " + ArticlesAvant);
        for(int i = 0 ; i < ArticlesAvant.size() ; i++)
        {    
            System.out.println("Error = " + ArticlesAvant.get(i));
            session.setAttribute("Error_Achat_" + (i+1), ArticlesAvant.get(i));
        }
        
        if (!ArticlesAvant.isEmpty())
            session.setAttribute("ButtonPressed", true);
        else
        {
            session.setAttribute("PaiementEffectue", true);
            // Procedure
            //ChargerPanier()
        }
        
        response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/Web_Applic_Billets/JSPPay.jsp");
    }
}
