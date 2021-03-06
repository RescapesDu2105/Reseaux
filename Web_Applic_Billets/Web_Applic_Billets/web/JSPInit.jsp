<%-- 
    Document   : JSPInit
    Created on : 30-oct.-2017, 15:12:06
    Author     : Philippe
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/css/bootstrap.min.css" integrity="sha384-PsH8R72JQ3SOdhVi3uxftmaW6Vc51MKb0q5P2rRUpPvrszuE4W1povHYgTpBfshb" crossorigin="anonymous">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css"> 
    </head>
    <body>
        <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
            <div class="collapse navbar-collapse" id="navbarCollapse">
                <a class="navbar-brand" href="http://localhost:8084/Web_Applic_Billets/JSPInit.jsp"><i class="fa fa-plane"></i><strong> Caddie Virtuel de l'InPrES Airport</strong></a>
            </div>
        </nav>
        <br><br><br><br>
        <div class="container" id="main">
            <div class="row justify-content-center">
                <div class="col-4">
                    <form class="form-signin" id="loginform" action="ControlDataCenter" method="POST">
                        <h2 id="HeaderConnexion" class="form-signin-heading"><i class="fa fa-sign-in"></i> Connexion</h2>
                        <% if(session.getAttribute("ErrorLogin") != null)
                        {
                            out.println("<div class=\"alert alert-danger\" role=\"alert\">"+ session.getAttribute("ErrorLogin") +"</div>"); 
                            session.setAttribute("ErrorLogin", null);
                        } %>
                        <div class="form-group">
                            <label for="username"><i class="fa fa-user"></i> Nom d'utilisateur</label>
                            <input type="text" name="inputLogin" id="inputLogin" class="form-control" placeholder="Entrer le nom d'utilisateur" autofocus>
                        </div>
                        <div class="form-group">
                            <label for="password"><i class="fa fa-key"></i> Mot de passe</label>
                            <input type="password" name="inputPassword" id="inputPassword" class="form-control" placeholder="Entrer le mot de passe">  
                        </div>
                        <div class="form-check checkbox">
                            <label><input id="inputCB" type="checkbox" name="Inscription" onclick="InscriptionAddInfos(this);"> Je suis un nouveau client</label>
                        </div>
                        <input id="inputHidden" type="hidden" name="action" value="Authentification">
                        <button class="btn btn-lg btn-success btn-block" type="submit" id="submit"><i class="fa fa-power-off"></i> Connexion</button>
                    </form>
                </div>
            </div>
        </div> <!-- /container -->
        <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.3/umd/popper.min.js" integrity="sha384-vFJXuSJphROIrBnz7yo7oB41mKfc8JzQZiCq4NCceLEaO4IHwicKwpJf9c9IpFgh" crossorigin="anonymous"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/js/bootstrap.min.js" integrity="sha384-alpBpkh1PFOepccYVYDB4do5UnbKysX5WZXm3XxPqe5iKTfUKjNkCk9SaVuEZflJ" crossorigin="anonymous"></script>
        <script type="text/javascript">
            function InscriptionAddInfos(CheckBox)
            {
                if(CheckBox.checked)
                {                                        
                    var inputNom = document.createElement("input");                   
                    inputNom.type = "text";
                    inputNom.id = "inputNom";
                    inputNom.name = "inputNom";
                    inputNom.setAttribute('class', "form-control");
                    inputNom.placeholder = "Entrer le nom de famille";
                    
                    var inputPrenom = document.createElement("input");                   
                    inputPrenom.type = "text";
                    inputPrenom.id = "inputPrenom";
                    inputPrenom.name = "inputPrenom";
                    inputPrenom.setAttribute('class', "form-control");
                    inputPrenom.placeholder = "Entrer le prénom";
                    
                    
                    var div1 = document.createElement("div");
                    div1.setAttribute('class', "form-group");
                    div1.id = "div_inputNom";
                    
                    var div2 = document.createElement("div");
                    div2.setAttribute('class', "form-group");
                    div2.id = "div_inputPrenom";
            
                    var labelNom = document.createElement("label");
                    labelNom.for = "name";
                    labelNom.innerHTML = "Nom de famille";
                    
                    var labelPrenom = document.createElement("label");
                    labelPrenom.for = "surname";
                    labelPrenom.innerHTML = "Prénom";
            
                    div1.appendChild(labelNom);
                    div1.appendChild(inputNom);
            
                    div2.appendChild(labelPrenom);
                    div2.appendChild(inputPrenom);
                                        
                    document.getElementById("loginform").insertBefore(div1, document.getElementById("inputHidden"));
                    document.getElementById("loginform").insertBefore(div2, document.getElementById("inputHidden"));
                }
                else
                {
                    document.getElementById("loginform").removeChild(document.getElementById("div_inputNom"));
                    document.getElementById("loginform").removeChild(document.getElementById("div_inputPrenom"));
                }
            }
        </script>
    </body>
</html>