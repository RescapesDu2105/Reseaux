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
import java.util.HashMap;
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
    private final Bean_DB_Access BD_airport = new Bean_DB_Access(Bean_DB_Access.DRIVER_MYSQL, "localhost", "3306", "Zeydax", "1234", "bd_airport");
    private Client Client;
    
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
                boolean Connected = Authentification(request, response, session);

                if(Connected)
                {                    
                    ChargerVols(session);
                    ChargerPanier(session);
                    response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/Web_Applic_Billets/JSPCaddie.jsp");
                }
                else
                    response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/Web_Applic_Billets");
                break;
            case "Deconnexion":
                session.invalidate();
                response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/Web_Applic_Billets");
                break;
            case "AjoutPanier":
                AjoutPanier(request, session);
                response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/Web_Applic_Billets/JSPCaddie.jsp");
                break;
            case "VoirPanier":
                response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/Web_Applic_Billets/JSPPay.jsp");
                break;
            case "RetourCaddie":
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
                session.setAttribute("Client", Client);
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

        return session.getAttribute("Client") != null;
    }
    
    public void ChargerVols(HttpSession session) 
    {
        ResultSet RS;
        Vols Vols = new Vols();

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
                System.out.println("Vol = " + Vol.getIdVol() + " " + Vol.getDateDepart());
                Vols.getVols().add(Vol);
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

        getServletContext().removeAttribute("Vols");
        getServletContext().setAttribute("Vols", Vols);
        System.out.println("Vols = " + Vols.getVols());
        
        if (session.getAttribute("Error") != null && Vols.getVols().isEmpty())//i == 0)
            session.setAttribute("Error", "Aucun vol n'est prévu dans les prochaines 24 heures !");
    }
    
    private void AjoutPanier(HttpServletRequest request, HttpSession session) throws IOException 
    {
        ResultSet RS;
        Timestamp DateTimePromesse;
                
        System.out.println("Client.getIdClient() = " + Client.getIdClient());
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
                hm.put("IdClient", Client.getIdClient());
                hm.put("IdVol", Integer.parseInt(request.getParameter("IdVol")));

                System.out.println("hm = " + hm);
                
                BD_airport.Insert("Promesses", hm);
                RS = BD_airport.Select(""
                        + "SELECT *"
                        + "FROM bd_airport.Promesses "
                        + "WHERE IdClient = " + Client.getIdClient() + " "
                        + "AND DateTimePromesse = '" + DateTimePromesse + "'");
                                
                if(RS != null)
                {
                    RS.next();
                    
                    Promesse Promesse = new Promesse(RS.getInt("IdPromesse"), DateTimePromesse, RS.getInt("NbAccompagnants"), Integer.parseInt(request.getParameter("IdVol")));
                    
                    BD_airport.CreateEvent("Event_" + Promesse.getIdPromesse(), "AT '" + DateTimePromesse + "' + INTERVAL 2 HOUR", 
                          "DELETE FROM bd_airport.Promesses "
                        + "WHERE IdPromesse = " + Promesse.getIdPromesse()); 
                    
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
            System.out.println("IdClient = " + ((Client)session.getAttribute("Client")).getIdClient());

            RS = BD_airport.Select("SELECT IdPromesse, DateTimePromesse, IdVol, NbAccompagnants "
                    + "FROM bd_airport.Promesses NATURAL JOIN bd_airport.Vols NATURAL JOIN bd_airport.avions NATURAL JOIN bd_airport.compagnies "
                    + "WHERE IdClient = '" + ((Client)session.getAttribute("Client")).getIdClient() + "' "
                    + "ORDER BY DateTimePromesse, IdPromesse");
                        
            while(RS.next())
            {
                Promesse Promesse = new Promesse(RS.getInt("IdPromesse"), RS.getTimestamp("DateTimePromesse"), RS.getInt("IdVol"), RS.getInt("NbAccompagnants"));                
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

    private void RetirerPanier(HttpServletRequest request, HttpSession session) throws IOException 
    {
        BD_airport.Connexion();
        try 
        {
            int Ok = BD_airport.Update("DELETE FROM bd_airport.Promesses WHERE IdPromesse = '" + request.getParameter("IdPromesse") + "'");  
            
            if (Ok != 0)    
                Client.RetirerPromesse(Integer.parseInt(request.getParameter("IdPromesse")));
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
        try 
        {
            BD_airport.Connexion();
            int Ok = BD_airport.Update("DELETE FROM bd_airport.Promesses WHERE IdClient = '" + Client.getIdClient() + "'");             
                      
            if (Ok != 0)
                Client.getPanier().clear();
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
        /*ArrayList ArticlesAvant = new ArrayList<>(), ArticlesApres = new ArrayList<>();//, ArticlesPlusDisponibles = new ArrayList<>();

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
        
        response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/Web_Applic_Billets/JSPPay.jsp");*/
    }
}
