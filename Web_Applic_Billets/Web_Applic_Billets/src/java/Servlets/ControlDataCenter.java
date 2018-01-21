package Servlets;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Beans.Client;
import Beans.Langues;
import Beans.Vols;
import Classes.Promesse;
import Classes.Vol;
import Resources._en_EN;
import cryptographie.KeyStoreUtils;
import database.utilities.Bean_DB_Access;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import protocoleTICKMAP.ReponseTICKMAP;
import protocoleTICKMAP.RequeteTICKMAP;

/**
 *
 * @author Philippe
 */
public class ControlDataCenter extends HttpServlet 
{
    private final Bean_DB_Access BD_airport = new Bean_DB_Access(Bean_DB_Access.DRIVER_MYSQL, "localhost", "3306", "Zeydax", "1234", "bd_airport");
    private final Bean_DB_Access BD_compta = new Bean_DB_Access(Bean_DB_Access.DRIVER_MYSQL, "localhost", "3306", "Zeydax", "1234", "bd_compta");
    private final String IP_Serveur = "127.0.0.1";
    private final int Port_Serveur = 30070;
    private Socket socket = null;
    private ObjectOutputStream oos = null;
    private ObjectInputStream ois = null;
    private Client Client = null;
    
    private Properties mailProperties = null;
    
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
        
        /*BD_compta.Connexion();   
        try
        {
            ArrayList<String> Langues = new ArrayList<>();
            ResultSet RS = BD_compta.Select("SELECT Code FROM Langues");
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
        }   */
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
                /*try 
                {
                    Init_Paiement();
                    Envoi_Certificat();
                } 
                catch(IOException | ClassNotFoundException | KeyStoreException | NoSuchAlgorithmException | NoSuchProviderException | UnrecoverableKeyException | CertificateException ex)
                { 
                    ex.printStackTrace(); 
                }*/
                
                //Envoyer_Mail_Confirmation();
                
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
                Payer(session);
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
                    else if (request.getParameter("inputMail").isEmpty())
                        session.setAttribute("ErrorLogin", "Le champ de l'adresse mail ne peut être vide !");
                    else
                    {
                        HashMap<String, Object> hm = new HashMap<>();
                        hm.put("Login", request.getParameter("inputLogin"));
                        hm.put("Password", request.getParameter("inputPassword"));
                        hm.put("Nom", request.getParameter("inputNom"));
                        hm.put("Prenom", request.getParameter("inputPrenom"));
                        hm.put("Mail", request.getParameter("inputMail"));                        

                        Connected = ConnexionClient(request, session, hm);
                    }                                                
                }
                else             
                    Connected = ConnexionClient(request, session);
                
                
                _en_EN bundle = new _en_EN();
                Langues langues = new Langues();
                for(String langue : langues.getLangues())
                {
                    //System.out.println("langue = " + new String(request.getParameter("inputLangue").getBytes("ISO-8859-1"),"UTF-8"));
                    //System.out.println("bundle = " + bundle.getString(langue));
                    if(bundle.getString(langue).equals(new String(request.getParameter("inputLangue").getBytes("ISO-8859-1"),"UTF-8")))
                    {
                        session.setAttribute("langue", langue);
                        break;
                    }
                }   
                //System.out.println("Langue = " + session.getAttribute("langue"));
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
            RS = BD_airport.Select("SELECT IdClient, Nom, Prenom, Mail FROM Clients WHERE Login = \"" + request.getParameter("inputLogin") + "\" AND Password = \"" + request.getParameter("inputPassword") + "\"");
            boolean isResults = RS.next();
            if (isResults)                        
            {
                Client = new Client(RS.getInt("IdClient"), RS.getString("Nom"), RS.getString("Prenom"), RS.getString("Mail"));
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
            RS = BD_airport.Select("SELECT IdClient, Nom, Prenom, Mail FROM Clients WHERE Login = \"" + request.getParameter("inputLogin") + "\" AND Password = \"" + request.getParameter("inputPassword") + "\"");
            boolean isResults = RS.next();
            if (isResults)                        
            {
                Client = new Client(RS.getInt("IdClient"), RS.getString("Nom"), RS.getString("Prenom"), RS.getString("Mail"));    
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

    private void Payer(HttpSession session) throws IOException 
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
            BD_airport.Connexion();
            try 
            {
                // Serveur_Billet
                //Init_Paiement();
                //Envoi_Certificat();
                
                ArrayList<Object> Parameters = new ArrayList<>();
                Parameters.add(Client.getIdClient());
                BD_airport.doProcedure("Payer", Parameters);
                Envoyer_Mail_Confirmation();
                Client.getPanier().clear();
                session.setAttribute("PaiementEffectue", true);
                
                // Envoi d'un email de confirmation
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
    
    private void Envoyer_Mail_Confirmation()
    {
        Store st;
                
        if(mailProperties == null)
            LireProperties();
        
        try
        {
            Session session = Session.getDefaultInstance(mailProperties);
            st = session.getStore("pop3");
            
            st.connect(mailProperties.getProperty("mail.pop3.host"), "InpresAirport-pay@u2.tech.hepl.local", "123Soleil");
            //st.connect(user.getMailProperties().getProperty("mail.pop3.host"), User  + "@u2.tech.hepl.local", Pwd);
            
            //Mail_Utilities.Envoyer_Mail(session, "InpresAirport-pay@u2.tech.hepl.local", "dimartino@u2.tech.hepl.local", "Confirmation de paiement", "Reçu de paiement pour l'achat de " + Client.getPanier().size() + " articles");
            
            try
            {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("InpresAirport-pay@u2.tech.hepl.local"));
                message.setRecipient(Message.RecipientType.TO, new InternetAddress("dimartino@u2.tech.hepl.local"));
                message.setSubject("Confirmation de paiement");
                Multipart MP = new MimeMultipart();
                MimeBodyPart BP = new MimeBodyPart();
                BP.setText("Reçu de paiement pour l'achat de " + Client.getPanier().size() + " articles");
                MP.addBodyPart(BP);

                message.setContent(MP);
                message.saveChanges();

                Transport.send(message);
            }
            catch (AddressException ex)
            {

            }
            catch (MessagingException ex)
            {

            }
        }
        catch(MessagingException ex) 
        {
            ex.printStackTrace();            
        }
    }
    
    private void LireProperties()
    {
        String pathProperties = "E:\\Dropbox\\B3\\Reseaux\\2017-2018\\Reseaux\\Web_Applic_Billets\\Web_Applic_Billets\\src\\java\\Resources\\config.properties";

        try
        {
            FileInputStream fis = new FileInputStream(pathProperties);
            mailProperties = new Properties();
            mailProperties.load(fis);
        }
        catch(FileNotFoundException ex)
        {
            try 
            {
                FileOutputStream fos = new FileOutputStream(pathProperties);
                
                mailProperties.put("mail.pop3.host", "10.59.26.134");
                mailProperties.put("mail.pop3.port", "110");
                mailProperties.put("mail.smtp.host", "10.59.26.134");
                mailProperties.put("mail.smtp.port", "25");
                mailProperties.put("mail.disable.top", "true");
                mailProperties.put("store", "pop3");
                        
                try 
                {
                    mailProperties.store(fos, null);
                }
                catch (IOException ex1) 
                {
                    System.exit(0);
                }
            } 
            catch (FileNotFoundException ex1) 
            {
                System.exit(0);
            }
        }
        catch(IOException ex)
        {
            System.exit(0);
        }
    }
    
    private void Init_Paiement() throws IOException
    {
        socket = new Socket(IP_Serveur, Port_Serveur);
        
        if (socket.isConnected()) 
        {
            System.out.println("Connexion OK");

            try 
            {        
                System.out.println("Création des flux");
                oos = new ObjectOutputStream(socket.getOutputStream());
                oos.flush();
                ois = new ObjectInputStream(socket.getInputStream());
                System.out.println("Fin de la création des flux");
            }
            catch(IOException ex) 
            {
                System.out.println(ex.getMessage());
            }            
            System.out.println("Client prêt");
            System.out.println("Connected = " + socket.isConnected());
        }
        else 
        {            
            System.out.println("Client pas prêt !");
        }
    }
    private void Envoi_Certificat() throws KeyStoreException, IOException, FileNotFoundException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException, NoSuchProviderException, ClassNotFoundException
    {
        RequeteTICKMAP Req = new RequeteTICKMAP(RequeteTICKMAP.REQUEST_SEND_CERTIFICATE);
        ReponseTICKMAP Rep = null;
        
        Security.addProvider(new BouncyCastleProvider());
        System.out.println("Path = " + "E:\\Dropbox\\B3\\Reseaux\\2017-2018\\Reseaux\\Web_Applic_Billets\\keystore"+ System.getProperty("file.separator")+"WebAppKeyStore.jks");
        KeyStoreUtils ks = new KeyStoreUtils("E:\\Dropbox\\B3\\Reseaux\\2017-2018\\Reseaux\\Web_Applic_Billets\\keystore"+ System.getProperty("file.separator")+"WebAppKeyStore.jks", "123Soleil", "webappcle");

        Req.getChargeUtile().put("Certificate" , ks.getCertif());
        oos.writeObject(Req);
        oos.flush();
        
        Rep = (ReponseTICKMAP) ois.readObject();

        if(Rep.getCode() == ReponseTICKMAP.SEND_CERTIFICATE_OK)
        {
            X509Certificate certifServeur = (X509Certificate) Rep.getChargeUtile().get("Certificate");

            ks.saveCertificate("PublicClientWebKey", certifServeur);
            ks.SaveKeyStore(System.getProperty("user.dir")+ System.getProperty("file.separator")+"keystore"+System.getProperty("file.separator")+"ServeurBilletKeyStore.jks", "123Soleil");
            System.out.println("OK");
        }
    }
}
