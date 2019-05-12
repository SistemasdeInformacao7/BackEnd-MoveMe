/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.moveme.webservices;

/**
 *
 * @author Lucas
 */
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("servicoteste")
public class OlaServico {

    @GET
    @Path("servicoteste-html")
    @Produces(MediaType.TEXT_HTML)
    public String olaHtml() {
        return "<html lang=\"pt-br\">"
                + "<head><META  http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">"
                + "<title>Primeiro serviço Move Me</title>"
                + "</head>"
                + "<body><h1>"
                + "Primeiro serviço Move Me<3!"
                + "</h1></body>"
                + "<html>";
    }
}
