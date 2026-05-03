package by.may.department.servlet;

import by.may.department.command.Command;
import by.may.department.factory.CommandFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class DispatcherServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String commandName = request.getParameter("command");

        Command command = CommandFactory.getCommand(commandName);

        try {
            command.execute(request, response);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

}
