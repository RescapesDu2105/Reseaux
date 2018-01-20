<%-- 
    Document   : JSPInit
    Created on : 30-oct.-2017, 15:12:06
    Author     : Philippe
--%>

<%@page import="java.sql.SQLException"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="database.utilities.Bean_DB_Access"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="Langues" scope="session" class="Beans.Langues"/>
<% 
    if(session.getAttribute("isUserLoggedIn") != null)
        response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/Web_Applic_Billets/JSPCaddie.jsp");
%>
<!DOCTYPE html>
<html lang="fr">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
        <meta name="description" content="">
        <meta name="author" content="">
        
        <title>Connexion</title>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.3/css/bootstrap.min.css" integrity="sha384-Zug+QiDoJOrZ5t4lssLdxGhVrurbmBWopoEl+M6BdEfwnCJZtKxi1KgxUyJq13dy" crossorigin="anonymous">
        <script defer src="https://use.fontawesome.com/releases/v5.0.4/js/all.js"></script>
    </head>
    <body>
        <fmt:setLocale value="en_EN" scope="session"/> 
        <fmt:bundle basename = "Resources.">
            
        <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
            <div class="collapse navbar-collapse" id="navbarCollapse">
                <a class="navbar-brand" href="http://localhost:8084/Web_Applic_Billets/JSPInit.jsp"><i class="fa fa-plane"></i><strong> <fmt:message key = "brand"/></strong></a>
            </div>
        </nav>
        <br><br><br><br>
        <div class="container" id="main">
            <div class="row justify-content-center">
                <div class="col-4">
                    <form class="form-signin" id="loginform" action="ControlDataCenter" method="POST">
                        <h2 id="HeaderConnexion" class="form-signin-heading text-center"><i class="fas fa-sign-in-alt"></i> <fmt:message key = "connexion"/></h2>
                        <% if(session.getAttribute("ErrorLogin") != null)
                        {
                            out.println("<div class=\"alert alert-danger\" role=\"alert\">"+ session.getAttribute("ErrorLogin") +"</div>"); 
                            session.setAttribute("ErrorLogin", null);
                        } %>
                        <div class="form-group">
                            <label for="username"><i class="fas fa-language"></i> <fmt:message key = "langue"/></label>
                            <select class="form-control" name="inputLangue">
                                <%  for(String Langue : Langues.getLangues())
                                    { %>
                                        <option><fmt:message key = '<%= Langue %>'/></option>
                                 <% } %>
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="name"><i class="fa fa-user"></i> <fmt:message key = "user"/></label>
                            <input type="text" name="inputLogin" id="inputLogin" class="form-control" placeholder='<fmt:message key = "userI"/>' autofocus>
                        </div>
                        <div class="form-group">
                            <label for="pwd"><i class="fa fa-key"></i> <fmt:message key = "pwd"/></label>
                            <input type="password" name="inputPassword" id="inputPassword" class="form-control" placeholder='<fmt:message key = "pwdI"/>'>  
                        </div>
                        <div class="form-check checkbox">
                            <label><input id="inputCB" type="checkbox" name="Inscription" data-nom='<fmt:message key = "nom"/>' data-nomI='<fmt:message key = "nomI"/>' data-prenom='<fmt:message key = "prenom"/>' data-prenomI='<fmt:message key = "prenomI"/>' data-mail='<fmt:message key = "mail"/>' data-mailI='<fmt:message key = "mailI"/>'> <fmt:message key = "new"/></label>
                        </div>
                        <input id="inputHidden" type="hidden" name="action" value="Authentification">
                        <button class="btn btn-lg btn-success btn-block" type="submit" id="submit"><i class="fa fa-power-off"></i> <fmt:message key = "connexion"/></button>
                    </form>
                </div>
            </div>
        </div> <!-- /container -->
        <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.3/js/bootstrap.min.js" integrity="sha384-a5N7Y/aK3qNeh15eJKGWxsqtnX/wWdSZSKp+81YjTmS15nvnvxKHuzaWwXHDli+4" crossorigin="anonymous"></script>
        <script type="text/javascript">
            $(document).ready(function ()
            {  
                $('#inputCB').click(function(event)            
                {      
                    console.log($(event.currentTarget));

                    if(event.currentTarget.checked)
                    {                                        
                        var inputNom = document.createElement("input");                   
                        inputNom.type = "text";
                        inputNom.id = "inputNom";
                        inputNom.name = "inputNom";
                        inputNom.setAttribute('class', "form-control");
                        inputNom.placeholder = $(event.currentTarget).data('nomi');

                        var inputPrenom = document.createElement("input");                   
                        inputPrenom.type = "text";
                        inputPrenom.id = "inputPrenom";
                        inputPrenom.name = "inputPrenom";
                        inputPrenom.setAttribute('class', "form-control");
                        inputPrenom.placeholder = $(event.currentTarget).data('prenomi');

                        var inputMail = document.createElement("input");                   
                        inputMail.type = "mail";
                        inputMail.id = "inputMail";
                        inputMail.name = "inputMail";
                        inputMail.setAttribute('class', "form-control");
                        inputMail.placeholder = $(event.currentTarget).data('maili');


                        var div1 = document.createElement("div");
                        div1.setAttribute('class', "form-group");
                        div1.id = "div_inputNom";

                        var div2 = document.createElement("div");
                        div2.setAttribute('class', "form-group");
                        div2.id = "div_inputPrenom";                    

                        var div3 = document.createElement("div");
                        div3.setAttribute('class', "form-group");
                        div3.id = "div_inputMail";

                        var labelNom = document.createElement("label");
                        labelNom.for = "name";
                        labelNom.innerHTML = $(event.currentTarget).data('nom');

                        var labelPrenom = document.createElement("label");
                        labelPrenom.for = "surname";
                        labelPrenom.innerHTML = $(event.currentTarget).data('prenom');            

                        var labelMail = document.createElement("label");
                        labelMail.for = "mail";
                        labelMail.innerHTML = $(event.currentTarget).data('mail');

                        div1.appendChild(labelNom);
                        div1.appendChild(inputNom);

                        div2.appendChild(labelPrenom);
                        div2.appendChild(inputPrenom);

                        div3.appendChild(labelMail);
                        div3.appendChild(inputMail);

                        document.getElementById("loginform").insertBefore(div1, document.getElementById("inputHidden"));
                        document.getElementById("loginform").insertBefore(div2, document.getElementById("inputHidden"));
                        document.getElementById("loginform").insertBefore(div3, document.getElementById("inputHidden"));
                    }
                    else
                    {
                        document.getElementById("loginform").removeChild(document.getElementById("div_inputNom"));
                        document.getElementById("loginform").removeChild(document.getElementById("div_inputPrenom"));
                        document.getElementById("loginform").removeChild(document.getElementById("div_inputMail"));
                    }
                });
            });
        </script>
        </fmt:bundle>
    </body>
</html>