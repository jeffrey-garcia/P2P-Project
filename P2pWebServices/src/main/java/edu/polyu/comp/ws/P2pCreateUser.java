package edu.polyu.comp.ws;

import java.io.InputStream;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.polyu.comp.domain.User;
import edu.polyu.comp.service.UserService;
import edu.polyu.comp.util.StringUtil;

@Path("createUser")
public class P2pCreateUser {

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response processInputRequest(InputStream requestBodyStream) {
		String output = "";

		try {
			output = StringUtil.convertInputStreamToString(requestBodyStream);

			// convert the incoming JSON message body into POJO
			ObjectMapper jackson = new ObjectMapper();
			User user = jackson.readValue(output, User.class);

			// add JDBC call here to create user account in DBMS
			boolean isSuccess = new UserService().createUser(user);
			if (isSuccess) {
				return Response.status(Response.Status.OK).entity(jackson.writeValueAsString(user)).build();
			} else {
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
			}
		} catch (Exception e) {
			e.printStackTrace(); // log the error
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}

}
