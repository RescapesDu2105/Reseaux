package Servlets;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Beans.Client;
import Beans.Vols;
import Classes.Promesse;
import Classes.Vol;
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
public class ControlDataCenter extends HttpServlet 
{
    private final Bean_DB_Access BD_airport = new Bean_DB_Access(Bean_DB_Access.DRIVER_MYSQL, "localhost", "3306", "Zeydax", "1234", "bd_airport");
    private final Bean_DB_Access BD_compta = new Bean_DB_Access(Bean_DB_Access.DRIVER_MYSQL, "localhost", "3306", "Zeydax", "1234", "bd_compta");
    private Client Client = null;
    
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
        
        BD_compta.Connexion();   
        try
        {
            ArrayList<String> Langues = new ArrayList<>();
            ResultSet RS = BD_compta.Select("SELECT Nom FROM Langues");
            while(RS.next())
            {
                Langues.add(RS.getString(1));
            }
            RS.close();
            sc.setAttribute("Langues", Langues);
            
            System.out.println("Langues = " + Langues.size());
        } 
        catch (SQLException ex) 
        {
            ex.printStackTrace();
        }  
        finally 
        {
            BD_compta.Deconnexion();
        }   
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
                boolean Connected = Authentification(request, response, session);

                if(Connected)
                {                    
                    ChargerVols(session);
                    ChargerPanier(session);
                    response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/Web_Applic_Billets/JSPCaddie.jsp");
                }
                else
                    response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/Web_Applic_Billets/JSPInit.jsp");
                break;
            case "Deconnexion":
                session.invalidate();
                session = request.getSession(false);
                response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/Web_Applic_Billets/JSPInit.jsp");
                break;
            case "AjoutPanier":
                AjoutPanier(request, session);
                response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/Web_Applic_Billets/JSPCaddie.jsp");
                break;
            case "VoirPanier":
                response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/Web_Applic_Billets/JSPPay.jsp");
                break;
            case "RetourCaddie":
                ChargerPanier(session);
                response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/Web_Applic_Billets/JSPCaddie.jsp");
                break;
            case "RetirerPanier":
                RetirerPanier(request, session);
                response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/Web_Applic_Billets/JSPPay.jsp");
                break;
            case "RetirerToutPanier":
                RetirerToutPanier(session);
                response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/Web_Applic_Billets/JSPPay.jsp");
                break;
            case "Payer":
            case "ConfirmerPaiement":
                Payer(request, response, session);
                response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/Web_Applic_Billets/JSPPay.jsp");
                break;
            default: System.out.println("DEFAULT");
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
    
    
    private boolean Authentification(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException 
    {
        boolean Connected = false; 

        if(request.getParameterMap().containsKey("inputLogin"))
        {   
            if(request.getParameter("inputPassword").isEmpty())
                session.setAttribute("ErrorLogin", "Le champ du mot de passe ne peut pas être vide !");
            else
            {                                   
                if (request.getParameter("Inscription") != null)
                {
                    if(request.getParameter("inputPassword").length() < 4)
                        session.setAttribute("ErrorLogin", "La longueur du mot de passe doit être supérieur à 4 !");
                    else if (request.getParameter("inputNom").isEmpty())
                        session.setAttribute("ErrorLogin", "Le champ du nom de famille ne peut être vide !");
                    else if (request.getParameter("inputPrenom").isEmpty())
                        session.setAttribute("ErrorLogin", "Le champ du prénom ne peut être vide !");
                    else
                    {
                        HashMap<String, Object> hm = new HashMap<>();
                        hm.put("Login", request.getParameter("inputLogin"));
                        hm.put("Password", request.getParameter("inputPassword"));
                        hm.put("Nom", request.getParameter("inputNom"));
                        hm.put("Prenom", request.getParameter("inputPrenom"));

                        Connected = ConnexionClient(request, session, hm);
                    }                                                
                }
                else             
                    Connected = ConnexionClient(request, session);
            }
        }
        else      
            session.setAttribute("ErrorLogin", "Veuillez remplir les champs de connexion !");  

        return Connected;
    }
    private boolean ConnexionClient(HttpServletRequest request, HttpSession session, HashMap<String, Object> InscriptionClient) throws IOException
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
                Client = new Client(RS.getInt("IdClient"), RS.getString("Nom"), RS.getString("Prenom"));
                session.setAttribute("Client", Client);                
                session.setAttribute("isUserLoggedIn", true);
            }
            else
                session.setAttribute("ErrorLogin", "Le nom d'utilisateur ou le mot de passe est incorrect !");  

            RS.close();
        } 
        catch (SQLException ex) 
        {
            session.setAttribute("ErrorLogin", "Le nom d'utilisateur existe déjà !"); 
        }  
        finally 
        {
            BD_airport.Deconnexion();
        }        

        return session.getAttribute("Client") != null;
    }
    private boolean ConnexionClient(HttpServletRequest request, HttpSession session) throws IOException
    {
        ResultSet RS; 
        
        BD_airport.Connexion();
        try
        {
            RS = BD_airport.Select("SELECT IdClient, Nom, Prenom FROM Clients WHERE Login = \"" + request.getParameter("inputLogin") + "\" AND Password = \"" + request.getParameter("inputPassword") + "\"");
            boolean isResults = RS.next();
            if (isResults)                        
            {
                Client = new Client(RS.getInt("IdClient"), RS.getString("Nom"), RS.getString("Prenom"));    
            //System.out.println("IdClient = " + Client.getIdClient());         
                session.setAttribute("Client", Client);
                session.setAttribute("isUserLoggedIn", true);
            }
            else
                session.setAttribute("ErrorLogin", "Le nom d'utilisateur ou le mot de passe est incorrect !"); 

            RS.close();
        } 
        catch (SQLException ex) 
        {
            session.setAttribute("ErrorLogin", "Le nom d'utilisateur ou le mot de passe est incorrect !");
        }     
        finally 
        {    
            BD_airport.Deconnexion();
        } 

        return Client != null;
    }
    
    public void ChargerVols(HttpSession session) 
    {
        ResultSet RS;
        Vols Vols = (Vols) getServletContext().getAttribute("Vols");
        if(Vols == null)
        {
            Vols = new Vols();
            getServletContext().setAttribute("Vols", Vols);
        }

        BD_airport.Connexion();
        try
        {        
            RS = BD_airport.Select(""
                + "SELECT IdVol, NumeroVol, NomCompagnie, Destination, HeureDepart, HeureArrivee, PlacesRestantes "
                + "FROM bd_airport.vols NATURAL JOIN bd_airport.avions NATURAL JOIN bd_airport.compagnies "
                + "WHERE bd_airport.vols.HeureDepart >= current_time() "
                + "AND bd_airport.vols.PlacesRestantes > 0 "
                + "ORDER BY HeureDepart");
            
            while(RS.next())
            {
                Vol Vol = new Vol(RS.getInt("IdVol"), RS.getInt("NumeroVol"), RS.getString("NomCompagnie"), RS.getString("Destination"), RS.getTimestamp("HeureDepart"), RS.getTimestamp("HeureArrivee"), RS.getInt("PlacesRestantes"));
                                
                if(Vols.getVol(RS.getInt("IdVol")) == null)
                {
                    Vols.getVols().add(Vol);
                }
            }               
            RS.close();    
        }
        catch (SQLException ex) 
        {
            session.setAttribute("Error", "Problème lors du chargement des vols !");
        }     
        finally 
        {    
            BD_airport.Deconnexion();
        } 

        //System.out.println("Vols = " + Vols.getVols());
        
        if (session.getAttribute("Error") != null && Vols.getVols().isEmpty())//i == 0)
            session.setAttribute("Error", "Aucun vol n'est prévu dans les prochaines 24 heures !");
    }
    
    private void AjoutPanier(HttpServletRequest request, HttpSession session) throws IOException 
    {
        ResultSet RS;
        Timestamp DateTimePromesse;
           
                
        BD_airport.Connexion();       
        try 
        {                
            RS = BD_airport.Select("SELECT CURRENT_TIMESTAMP() FROM dual");
            if(RS.next())
            {
                DateTimePromesse = RS.getTimestamp(1);
                int NbAccompagnants = Integer.parseInt(request.getParameter("NbAccompagnants"));
                int IdVol = Integer.parseInt(request.getParameter("IdVol"));
                
                HashMap<String, Object> hm = new HashMap<>();
        
                hm.put("DateTimePromesse", DateTimePromesse);
                hm.put("NbAccompagnants", NbAccompagnants);
                hm.put("IdClient", Client.getIdClient());
                hm.put("IdVol", IdVol);

                
                BD_airport.Insert("Promesses", hm);     
                
                RS = BD_airport.Select(""
                        + "SELECT * "
                        + "FROM bd_airport.Promesses "
                        + "WHERE IdClient = " + Client.getIdClient() + " "
                        + "AND DateTimePromesse = '" + DateTimePromesse + "' "
                        + "ORDER BY IdPromesse DESC");
                                
                int IdPromesse;
                if(RS != null)
                {
                    RS.next();
                    IdPromesse = RS.getInt("IdPromesse");
                    Promesse Promesse = new Promesse(IdPromesse, DateTimePromesse, NbAccompagnants, IdVol);
                
                    Client.getPanier().add(Promesse);
                }
            }           
            RS.close();       
        } 
        catch (SQLException ex) 
        {
            System.err.println("SQL Error : " + ex.getMessage());
            session.setAttribute("Error", "Une erreur interne s'est produite !");
        }
        finally
        {
            BD_airport.Deconnexion();
        }        
    }

    public void ChargerPanier(HttpSession session)
    {
        ResultSet RS;
                
        BD_airport.Connexion();
        try
        {                
            RS = BD_airport.Select("SELECT IdPromesse, DateTimePromesse, IdVol, NbAccompagnants "
                    + "FROM bd_airport.Promesses NATURAL JOIN bd_airport.Vols NATURAL JOIN bd_airport.avions NATURAL JOIN bd_airport.compagnies "
                    + "WHERE IdClient = '" + ((Client)session.getAttribute("Client")).getIdClient() + "' "
                    + "ORDER BY DateTimePromesse, IdPromesse");
                        
            Client.getPanier().clear();
            while(RS.next())
            {
                Promesse Promesse = new Promesse(RS.getInt("IdPromesse"), RS.getTimestamp("DateTimePromesse"), RS.getInt("NbAccompagnants"), RS.getInt("IdVol")); 
                Client.getPanier().add(Promesse);
            }        
            RS.close();
        } 
        catch (SQLException ex) 
        {            
            session.setAttribute("Error", "Une erreur interne s'est produite !");
        }
        finally
        {
            BD_airport.Deconnexion();
        }
    }

    public void RetirerPanier(HttpServletRequest request, HttpSession session) throws IOException 
    {
        BD_airport.Connexion();
        try 
        {
            int Ok = BD_airport.Update("DELETE FROM bd_airport.Promesses WHERE IdPromesse = '" + request.getParameter("IdPromesse") + "'");  
            
            if (Ok != 0)    
            {
                Client.RetirerPromesse(Integer.parseInt(request.getParameter("IdPromesse")));
            }
            else
                session.setAttribute("Error", "Une erreur interne s'est produite !");
        } 
        catch (SQLException ex) 
        {
            session.setAttribute("Error", "Une erreur interne s'est produite !");
        }
        finally
        {
            BD_airport.Deconnexion();   
        }
    }    

    private void RetirerToutPanier(HttpSession session) throws IOException 
    {
        BD_airport.Connexion();
        try 
        {
            int Ok = BD_airport.Update("DELETE FROM bd_airport.Promesses WHERE IdClient = '" + Client.getIdClient() + "'");             
                      
            if (Ok != 0)
            {
                Client.getPanier().clear();
            }
        } 
        catch (SQLException ex) 
        {
            session.setAttribute("Error", "Une erreur interne s'est produite !");
        }
        finally
        {
            BD_airport.Deconnexion();    
        }
    }

    private void Payer(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException 
    {
        ArrayList<Promesse> ArticlesPlusDisponibles = (ArrayList<Promesse>) Client.getPanier().clone();
        
        ChargerPanier(session);               
        
        ArticlesPlusDisponibles.removeAll(Client.getPanier()); 
        
        
        if (!ArticlesPlusDisponibles.isEmpty())
        {
            session.setAttribute("ButtonPressed", true);            
            session.setAttribute("ArticlesPlusDisponibles", ArticlesPlusDisponibles);
        }
        else
        {
            session.setAttribute("PaiementEffectue", true);
            BD_airport.Connexion();
            try 
            {
                ArrayList<Object> Parameters = new ArrayList<>();
                Parameters.add(Client.getIdClient());
                BD_airport.doProcedure("Payer", Parameters);
                Client.getPanier().clear();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(ControlDataCenter.class.getName()).log(Level.SEVERE, null, ex);
            }
            finally
            {
                BD_airport.Deconnexion(); 
            }            
        }
    }
}
