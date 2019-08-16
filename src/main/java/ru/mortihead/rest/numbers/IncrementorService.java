package ru.mortihead.rest.numbers;


import ru.mortihead.numbers.Incrementor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

//getNumber
//incrementNumber
//setMaximumValue
//getMaximumValue

@Path("/service")
public class IncrementorService {
    private static final String INCREMENTOR_SESSION_ATTR = "incrementor";

    private Incrementor getIncrementorFromSession(HttpServletRequest req) {
        HttpSession session = req.getSession(true);
        Object incrementor = session.getAttribute(INCREMENTOR_SESSION_ATTR);
        if (incrementor == null) {
            incrementor = new Incrementor();
            session.setAttribute(INCREMENTOR_SESSION_ATTR, incrementor);
        }
        return ((Incrementor) incrementor);
    }

    @GET
    @Path("/initnumber")
    @Produces(MediaType.TEXT_PLAIN)
    public String initNumber(@Context HttpServletRequest req) {
        Incrementor incrementor = getIncrementorFromSession(req);
        return String.valueOf(incrementor.getNumber());
    }

    @GET
    @Path("/getnumber")
    @Produces(MediaType.TEXT_PLAIN)
    public String getNumber(@Context HttpServletRequest req) {
        Incrementor incrementor = getIncrementorFromSession(req);
        return String.valueOf(incrementor.getNumber());
    }

    @GET
    @Path("/incrementnumber")
    @Produces(MediaType.TEXT_PLAIN)
    public String incrementNumber(@Context HttpServletRequest req) {
        Incrementor incrementor = getIncrementorFromSession(req);
        incrementor.incrementNumber();
        return String.valueOf(incrementor.getNumber());
    }

    @GET
    @Path("/setmaximumvalue")
    @Produces(MediaType.TEXT_PLAIN)
    public String setMaximumValue(@Context HttpServletRequest req, @QueryParam("value") final int value) {
        Incrementor incrementor = getIncrementorFromSession(req);
        incrementor.setMaximumValue(value);
        return String.valueOf(incrementor.getMaximumValue());
    }

    @GET
    @Path("/getmaximumvalue")
    @Produces(MediaType.TEXT_PLAIN)
    public String getMaximumValue(@Context HttpServletRequest req) {
        Incrementor incrementor = getIncrementorFromSession(req);
        return String.valueOf(incrementor.getMaximumValue());
    }
}