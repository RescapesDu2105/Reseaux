<%-- 
    Document   : JSPPay
    Created on : 30-oct.-2017, 15:12:31
    Author     : Philippe
--%>

<%@page import="Servlets.ControlDataCenter"%>
<%@page import="database.utilities.Bean_DB_Access"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.util.Locale"%>
<%@page import="java.util.Locale"%>
<%@page import="java.util.HashMap"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%  
    response.setIntHeader("Refresh", 60);
    Bean_DB_Access BD_airport = new Bean_DB_Access(Bean_DB_Access.DRIVER_MYSQL, "localhost", "3306", "Zeydax", "1234", "bd_airport");
    BD_airport.Connexion();
    ControlDataCenter.MAJ_Panier(session, BD_airport);
    ControlDataCenter.ChargerPanier(session, BD_airport);
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
                    <input type="hidden" name="action" value="RetourCaddie">
                    <button type="submit" class ="btn btn-info"> 
                        <% out.println(session.getAttribute("NbItemsPanier")); %>
                        <i class="fa fa-shopping-cart" aria-hidden="true"></i>
                        Retour au caddie                    
                    </button> 
                </form>
                <form id="Deconnexion" action="ControlDataCenter" method="POST">
                    <input type="hidden" name="action" value="Deconnexion">
                    <button class="btn btn-danger" type="submit" id="submit"><i class="fa fa-sign-out"></i> Déconnexion</button>
                </form>               
            </div>
        </nav>
        <br>
        <%  if(session.getAttribute("PaiementEffectue") != null) 
            { 
                session.removeAttribute("PaiementEffectue");
        %>
                <div class="mx-auto col-md-2">
                    <div class="alert alert-success" role="alert"><i class="fa fa-check"></i> Le paiement a bien été effectué!</div>
                </div>
        <%  }   %>
        
        <%  
            if((int)session.getAttribute("NbItemsPanier") == 0 || session.getAttribute("Error") != null)
            { %>
                <div class="mx-auto col-md-2">
                    <%  if(session.getAttribute("Error") != null) 
                        { %>
                            <div class="alert alert-danger" role="alert"><% session.getAttribute("Error"); session.removeAttribute("Error"); %></div>
                    <%  }
                        else 
                        { %>
                            <div class="alert alert-danger" role="alert">Vous n'avez aucun article dans le panier</div>      
                    <%  } %>                    
                </div>
                <div class="mx-auto col-md-1">
                    <form action="ControlDataCenter" method="POST">
                        <input type="hidden" name="action" value="RetourCaddie">
                        <button type="submit" class ="btn btn-info"><i class="fa fa-chevron-left"></i> Retour au caddie</button> 
                    </form>
                </div>
        <%
            }
            else
            {
        %>
        <div class="mx-auto col-md-12">
            <table class ="table table-hover table-striped">
                <thead>
                    <tr>
                        <th>Commande</th>
                        <th>Date de la commande</th>
                        <th>Numéro du vol</th>
                        <th>Nom compagnie</th>
                        <th>Destination</th>
                        <th>Heure de départ</th>
                        <th>Heure d'arrivée</th>
                        <th>Nombre d'accompagnants</th>
<!--                        <th>Prix du billet unitaire</th>
                        <th>Prix total</th>-->
                    </tr>
                </thead>
                <tbody>
                    <% 
                        int i = 1;
                        //while(session.getAttribute("Article_" + i) != null)
                        while(i <= (int)session.getAttribute("NbItemsPanier"))
                        {
                            HashMap<String, Object> Billets = (HashMap<String, Object>) session.getAttribute("Article_" + i);
                    %>
                            <tr>
                                <th scope="row"><% out.println(Billets.get("IdPromesse")); %></th>
                                <td><% out.println(DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, Locale.FRANCE).format(Billets.get("DateTimePromesse"))); %></td>
                                <td><% out.println(Billets.get("NumeroVol")); %></td>
                                <td><% out.println(Billets.get("NomCompagnie")); %></td>
                                <td><% out.println(Billets.get("Destination")); %></td>
                                <td><% out.println(DateFormat.getTimeInstance(DateFormat.SHORT, Locale.FRANCE).format(Billets.get("HeureDepart"))); %></td>
                                <td><% out.println(DateFormat.getTimeInstance(DateFormat.SHORT, Locale.FRANCE).format(Billets.get("HeureArrivee"))); %></td>
                                <td><% out.println(Billets.get("NbAccompagnants")); %></td>
                                <form action="ControlDataCenter" method="POST">
                                    <input type="hidden" name="action" value="RetirerPanier">
                                    <input type="hidden" name="IdPromesse" value=<% out.println(Billets.get("IdPromesse").toString()); %> >
                                    <input type="hidden" name="NumeroArticle" value=<% out.println(i); %> >
                                    <td><button class="btn btn-danger btn-block" type="submit" id="submit"><i class="fa fa-minus" aria-hidden="true"></i> Retirer du panier</button></td>
                                </form>
                            </tr>
                    <%      i++;
                        } 
                    %>
                </tbody>
            </table>
        </div>
        <div class="mx-auto col-md-2">
            <div class="form-inline">
                <div class="form-group">
                    <form action="ControlDataCenter" method="POST">
                        <input type="hidden" name="action" value="Payer">
                        <td><button class="btn btn-success btn-block" type="submit"><i class="fa fa-credit-card" aria-hidden="true"></i> Payer</button></td>
                    </form>
                    <form action="ControlDataCenter" method="POST">
                        <input type="hidden" name="action" value="RetirerToutPanier">
                        <td><button class="btn btn-danger btn-block" type="submit"><i class="fa fa-trash" aria-hidden="true"></i> Retirer tout du panier</button></td>
                    </form>
                </div>
            </div>
            <%  if(session.getAttribute("ButtonPressed") != null && (boolean)session.getAttribute("ButtonPressed"))
                {
                    response.setIntHeader("Refresh", Integer.MAX_VALUE);
            %>
                    <div class="modal fade my-5" id="ModalPayer" role="dialog" data-backdrop="false">
                        <div class="modal-dialog modal-lg" role="document">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h5 class="modal-title" id="exampleModalLabel"><strong>Certains articles ne sont plus disponibles à l'achat !</strong></h5>
                                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                        <i class="fa fa-times"></i>
                                    </button>
                                </div>
                                <div class="modal-body">
                                    <table class ="table table-sm table-striped">
                                        <thead>
                                            <tr>
                                                <th>Commande</th>
                                                <th>Date</th>
                                                <th>Numéro vol</th>
                                                <th>Destination</th>
                                                <th>Heure départ</th>
                                                <th>Nombre d'accompagnants</th>
                                                <th>Raison</th>
                        <!--                        <th>Prix du billet unitaire</th>
                                                <th>Prix total</th>-->
                                            </tr>
                                        </thead>
                                        <tbody>
                                        <%
                                            i = 1;
                                            while(session.getAttribute("Error_Achat_" + i) != null)
                                            {
                                                HashMap<String, Object> Errors = (HashMap<String, Object>) session.getAttribute("Error_Achat_" + i);
                                                System.out.println("Errors = " + Errors);
                                            %>
                                                <tr>
                                                    <th scope="row"><% out.println(Errors.get("IdPromesse")); %></th>
                                                    <td><% out.println(DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.FRANCE).format(Errors.get("DateTimePromesse"))); %></td>
                                                    <td><% out.println(Errors.get("NumeroVol")); %></td>
                                                    <td><% out.println(Errors.get("Destination")); %></td>
                                                    <td><% out.println(DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.FRANCE).format(Errors.get("HeureDepart"))); %></td>
                                                    <td><% out.println(Errors.get("NbAccompagnants")); %></td>
                                                    <td>Expiré</td>
                                                </tr>
                                        <%
                                                session.removeAttribute("Error_Achat_" + i);
                                                i++;
                                            } %>
                                        </tbody>
                                    </table>
                                </div>
                                <div class="modal-footer">
                                    <form action="ControlDataCenter" method="POST">
                                        <input type="hidden" name="action" value="AnnulerPaiement">
                                        <button type="button" class="btn btn-danger" data-dismiss="modal"><i class="fa fa-undo"></i> Annuler</button>
                                    </form>
                                    <form action="ControlDataCenter" method="POST">
                                        <input type="hidden" name="action" value="ConfirmerPaiement">
                                        <button type="button" class="btn btn-success"><i class="fa fa-check"></i> Confirmer le payement</button>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>              
            <%  }   %>
        </div>
        <% } %>
        <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.3/umd/popper.min.js" integrity="sha384-vFJXuSJphROIrBnz7yo7oB41mKfc8JzQZiCq4NCceLEaO4IHwicKwpJf9c9IpFgh" crossorigin="anonymous"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/js/bootstrap.min.js" integrity="sha384-alpBpkh1PFOepccYVYDB4do5UnbKysX5WZXm3XxPqe5iKTfUKjNkCk9SaVuEZflJ" crossorigin="anonymous"></script>
        <%  if(session.getAttribute("ButtonPressed") != null && (boolean)session.getAttribute("ButtonPressed"))
            {
                session.removeAttribute("ButtonPressed");
        %>
                <script type="text/javascript">
                    $.when( $.ready ).then(function()
                    {
                        // Show the Modal on load
                        $('#ModalPayer').modal('show');
                    });
                </script>        
        <% } %>
    </body>
</html>
