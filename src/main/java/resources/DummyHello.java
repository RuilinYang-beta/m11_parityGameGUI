package resources;


import dao.DummyDao;
import model.DummyModel;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * restful Test class
 *
 * @author marscheng
 * @create 2017-07-26 3:19 p.m.
 */
@Path("/hello")
public class DummyHello {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String sayHello(){
        return "Hello,I am text!";
    }


    @GET
    @Produces(MediaType.TEXT_XML)
    public String sayXMLHello() {
        return "<?xml version=\"1.0\"?>" + "<hello> Hello,I am xml!" + "</hello>";
    }


    @GET
    @Produces(MediaType.TEXT_HTML)
    public String sayHtmlHello() {
        return "<html> " + "<title>" + "Hello Jersey" + "</title>"
                + "<body><h1>" + "Hello,I am html!" + "</body></h1>" + "</html> ";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String sayJSONHello() {
        return "{ 'test':'jdk'}";
    }

    @Path("/all")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<DummyModel> getAllDummies(){
        List<DummyModel> dummies = new ArrayList<>();
        for (Map.Entry<String, DummyModel> entry : DummyDao.instance.getModel().entrySet()){
            dummies.add(entry.getValue());
        }
        return dummies;
    }


}

