<%-- 
    Document   : JSPCaddie
    Created on : 30-oct.-2017, 15:12:23
    Author     : Philippe
--%>

<%@page import="java.sql.ResultSet"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.util.Locale"%>
<%@page import="java.util.Locale"%>
<%@page import="database.utilities.Bean_DB_Access"%>
<%@page import="Servlets.ControlDataCenter"%>
<%@page import="java.util.HashMap"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%  
    response.setIntHeader("Refresh", 60);
    Bean_DB_Access BD_airport = new Bean_DB_Access(Bean_DB_Access.DRIVER_MYSQL, "localhost", "3306", "Zeydax", "1234", "bd_airport");
    BD_airport.Connexion();
    ControlDataCenter.ChargerVols(session, BD_airport);
    ControlDataCenter.MAJ_Panier(session, BD_airport);
    BD_airport.Deconnexion();
%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
        <meta name="description" content="">
        <meta name="author" content="">
        <link rel="icon" href="../../favicon.ico">
        <title>Connexion</title>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/css/bootstrap.min.css" integrity="sha384-PsH8R72JQ3SOdhVi3uxftmaW6Vc51MKb0q5P2rRUpPvrszuE4W1povHYgTpBfshb" crossorigin="anonymous">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css"> 
    </head>
    <body>
        <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
            <div class="collapse navbar-collapse" id="navbarCollapse">
                <a class="navbar-brand" href="http://localhost:8084/Web_Applic_Billets/JSPCaddie.jsp"><i class="fa fa-plane"></i><strong> Caddie Virtuel de l'InPrES Airport</strong></a>
                <ul class="navbar-nav mx-auto">
                    <li class="nav-item active">
                        <a class="nav-link"><strong>Client : <% out.println(session.getAttribute("Prenom") + " " + session.getAttribute("Nom")); %></strong></a>
                    </li>
                </ul>
                <form action="ControlDataCenter" method="POST">                                        
                    <button type="submit" class ="btn btn-info btn-"> 
                        <% out.println(session.getAttribute("NbItemsPanier")); %>
                        <i class="fa fa-shopping-cart" aria-hidden="true"></i>
                        Panier
                        <input type="hidden" name="action" value="VoirPanier">
                    </button>
                </form>
                <form class="form-signin" id="Deconnexion" action="ControlDataCenter" method="POST">
                    <input type="hidden" name="action" value="Deconnexion">
                    <button class="btn btn-danger" type="submit" id="submit"><i class="fa fa-sign-out"></i> Déconnexion</button>
                </form>               
            </div>
        </nav>
        <br>
        
        <%  if(session.getAttribute("Error") != null && session.getAttribute("Error").equals("Aucun vol n'est prévu dans les prochaines 24 heures !"))
            {
                out.println("<div class=\"mx-auto col-md-6\">");
                out.println("<div class=\"alert alert-danger\" role=\"alert\">"+ session.getAttribute("Error") +"</div>"); 
                out.println("</div>");
                session.setAttribute("Error", null);
            }
            else
            {
        %>           
        <div class="mx-auto col-md-10">            
            <table class ="table table-hover table-striped">
                <thead>
                    <tr>
                        <th>Numéro du vol</th>
                        <th>Nom compagnie</th>
                        <th>Destination</th>
                        <th>Heure de départ</th>
                        <th>Heure d'arrivée</th>
                        <th>Places restantes</th>
                        <th>Nombre d'accompagnants</th>
                    </tr>
                </thead>
                <tbody>
                    <% 
                        int i = 1;
                        while(session.getAttribute("Vol_" + i) != null)
                        {
                            HashMap<String, Object> Vol = (HashMap<String, Object>) session.getAttribute("Vol_" + i);
                    %>      
                            <tr>
                                <th scope="row"><% out.println(Vol.get("NumeroVol")); %></th>
                                <td><% out.println(Vol.get("NomCompagnie")); %></td>
                                <td><% out.println(Vol.get("Destination")); %></td>
                                <td><% out.println(DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, Locale.FRANCE).format(Vol.get("HeureDepart"))); %></td>
                                <td><% out.println(DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, Locale.FRANCE).format(Vol.get("HeureArrivee"))); %></td>
                                <td><% out.println(Vol.get("PlacesRestantes")); %></td>                                
                                <form action="ControlDataCenter" method="POST">
                                    <td><input class="form-control" type="number" name="NbAccompagnants" min="0" max="<% out.println(Vol.get("PlacesRestantes")); %>" step="1" value="0" style="width: 75px;"></td>
                                    <input type="hidden" name="action" value="AjoutPanier">
                                    <input type="hidden" name="IdVol" value=<% out.println(Vol.get("IdVol").toString()); %> >
                                    <input type="hidden" name="NumeroVol" value=<% out.println(Vol.get("NumeroVol").toString()); %> >
                                    <input type="hidden" name="HeureDepart" value=<% out.println(Vol.get("HeureDepart").toString()); %> >
                                    <td><button class="btn btn-success btn-block" type="submit" id="submit"><i class="fa fa-plus"></i> Ajouter au panier</button></td>
                                </form>
                            </tr>
                    <%      i++;
                        } 
                    %>
                </tbody>
            </table>
        </div>        
        <% } %>
    </body>
</html>
