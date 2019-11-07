package ru.itpark.servlet;

import ru.itpark.service.AutoService;
import ru.itpark.service.FileService;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.sql.SQLException;

public class CatalogServlet extends HttpServlet {
    private AutoService autoService;
    private FileService fileService;

    @Override
    public void init() throws ServletException {
        final Context context;
        try {
            context = new InitialContext();
            autoService = (AutoService) context.lookup("java:/comp/env/bean/auto-service");
            fileService = (FileService) context.lookup("java:/comp/env/bean/file-service");
        } catch (NamingException e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setAttribute("items", autoService.getAll());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
        req.getRequestDispatcher("/WEB-INF/catalog.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            final String name = req.getParameter("name");
            final String description = req.getParameter("description");
            final Part part = req.getPart("image");

            final String image = fileService.writeFile(part);

            autoService.create(name, description, image);
            resp.sendRedirect(String.join("/", req.getContextPath(), req.getServletPath()));
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }
}
