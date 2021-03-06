<%-- 
    Document   : JSPPay
    Created on : 30-oct.-2017, 15:12:31
    Author     : Philippe
--%>

<%@page import="java.util.Calendar"%>
<%@page import="java.util.ArrayList"%>
<%@page import="Classes.Vol"%>
<%@page import="Beans.Vols"%>
<%@page import="Classes.Promesse"%>
<%@page import="Beans.Client"%>
<%@page import="Servlets.ControlDataCenter"%>
<%@page import="database.utilities.Bean_DB_Access"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.util.Locale"%>
<%@page import="java.util.Locale"%>
<%@page import="java.util.HashMap"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="Vols" scope="application" class="Beans.Vols"/>
<jsp:useBean id="Client" scope="session" class="Beans.Client"/>
<%  
    response.setIntHeader("Refresh", 60);
    if (session.isNew() || session.getAttribute("isUserLoggedIn") == null) 
    {
        response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/Web_Applic_Billets/JSPInit.jsp"); 
    }
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
                        <a class="nav-link"><strong>Client : <%= Client.getNom() + " " + Client.getPrenom() %></strong></a>
                    </li>
                </ul>
                <form action="ControlDataCenter" method="POST">
                    <input type="hidden" name="action" value="RetourCaddie">
                    <button type="submit" class ="btn btn-info"> 
                        <%= Client.getPanier().size() %>
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
        <%  }  
            else
            {
                if(((Client)session.getAttribute("Client")).getPanier().isEmpty() || session.getAttribute("Error") != null)
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
                                    for(int i = 0 ; i < Client.getPanier().size() ; i++)
                                    {
                                        Promesse Promesse = Client.getPanier().get(i);
                                        Vol Vol = Vols.getVol(Promesse.getIdVol());
                                        
                                        if (Vol.getDateDepart().compareTo(Calendar.getInstance().getTime()) > 0)
                                        {
                                %>
                                            <tr>
                                                <th scope="row"><%= Promesse.getIdPromesse() %></th>
                                                <td><%= Promesse.getDatePromesse(Locale.FRANCE) %></td>
                                                <td><%= Vol.getNumeroVol() %></td>
                                                <td><%= Vol.getNomCompagnie() %></td>
                                                <td><%= Vol.getDestination() %></td>
                                                <td><%= Vol.getDateDepart(Locale.FRANCE) %></td>
                                                <td><%= Vol.getDateArrivee(Locale.FRANCE) %></td>
                                                <td><%= Promesse.getNbAccompagnants() %></td>
                                                <form action="ControlDataCenter" method="POST">
                                                    <input type="hidden" name="action" value="RetirerPanier">
                                                    <input type="hidden" name="IdPromesse" value=<%= Promesse.getIdPromesse() %> >
                                                    <td><button class="btn btn-danger btn-block" type="submit" id="submit"><i class="fa fa-minus" aria-hidden="true"></i> Retirer du panier</button></td>
                                                </form>
                                            </tr>
                                <%      }
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
                                                        ArrayList<Promesse> ArticlesPlusDisponibles = (ArrayList<Promesse>)session.getAttribute("ArticlesPlusDisponibles");
                                                        for(int i = 0 ; i < ArticlesPlusDisponibles.size() ; i++)
                                                        {
                                                            Promesse Promesse = ArticlesPlusDisponibles.get(i);
                                                            Vol Vol = Vols.getVol(Promesse.getIdVol());
                                                        %>
                                                            <tr>
                                                                <th scope="row"><%= Promesse.getIdPromesse() %></th>
                                                                <td><%= Promesse.getDatePromesse(Locale.FRANCE) %></td>
                                                                <td><%= Vol.getNumeroVol() %></td>
                                                                <td><%= Vol.getDestination() %></td>
                                                                <td><%= Vol.getDateDepart(Locale.FRANCE) %></td>
                                                                <td><%= Promesse.getNbAccompagnants() %></td>
                                                                <td>Expiré</td>
                                                            </tr>
                                                    <%  } %>
                                                    </tbody>
                                                </table>
                                            </div>
                                            <div class="modal-footer">
                                                <button type="button" class="btn btn-danger" data-dismiss="modal"><i class="fa fa-undo"></i> Annuler</button>
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
        <%      }
            } %>
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
