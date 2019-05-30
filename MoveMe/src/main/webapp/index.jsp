<%-- 
    Document   : index
    Created on : 29/05/2019, 11:03:31
    Author     : omupa
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Projeto WebService</title>
    </head>
    <body>
        <div style="text-align: center;">
            <h1>MoveMe web services!</h1>
            <p>WebService iniciado com sucesso</p>
        </div>
        <br>
        <div>
            <hr>
            <h2>Manual</h2>
            <hr>
            <h3>Caminho padrão</h3>
            <p>.../MoveMe/rest/{opções}/{métodos}</p>
            <h3>{Opções}</h3>
            <ul>
                <li>passageiro</li>
                <li>administrador</li>
                <li>motorista</li>
                <li>restaurante</li>
                <li>veiculo</li>
                <li>viagem</li>
                <li>avaliarestaurante</li>
                <li>usuarioviagem</li>
            </ul>

            <h3>Métodos</h3>
            <ul>
                <li>POST: /inserir</li>
                <li>GET: /getall</li>
                <li>GET: /{id} - Exemplo: /passageiro/1</li>
                <li>PUT: /editar</li>
                <li>DELETE: /{id}</li>
            </ul>
            <h3>Observações</h3>
            <p>Exceto os campos /{id}, deve ser enviado um objeto JSON</p>
            <p>Fazer JSON: </p>
            <code>
                Usuario usuario = new Usuario();
                String saida = new Gson().toJson(usuario);
            </code>

            <p>Desfazer JSON: </p>
            <code>
                public void recebeJSON(String dadosUsuario){<br>
                &nbsp;&nbsp;&nbsp;&nbsp;Usuario usuario = new Gson().fromJson(dadosUsuario, Usuario.class);<br>
                }
            </code>
        </div>
        <br><br><br><br><br><br><br>
    </body>
</html>
