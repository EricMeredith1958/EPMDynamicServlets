package com.fedex.epm;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.opencsv.CSVReader;


/**
 * Servlet implementation class UploadFile
 */
@WebServlet(description = "Used to upload a file to populate a database", urlPatterns = { "/UploadFile" })
@MultipartConfig
public class UploadFile extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UploadFile() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
    	// Grab the filename and parts
    	String description = request.getParameter("description"); // Retrieves <input type="text" name="description">
        Part filePart = request.getPart("file"); // Retrieves <input type="file" name="file">
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // MSIE fix.
        
        // Convert the InputStream to a CVSReader - Open Source tool for reading/writing
        // CSV files.
        InputStream fileContent = filePart.getInputStream();
        CSVReader reader = new CSVReader(new InputStreamReader(fileContent));
        String [] nextLine;
        ArrayList data = new ArrayList();
        
        while ((nextLine = reader.readNext()) != null) {
           // nextLine[] is an array of values from the line
           System.out.println(nextLine[0] + "," + nextLine[1] + ", etc...");
           data.add(nextLine);
        }
        reader.close();
        
        // Set the file data into the confirmation JSP
        request.setAttribute("data", data);
        
        // Dispatch a JSP page to confirm the CSV file to upload.
        ServletContext context = getServletContext();
        RequestDispatcher dispatcher = context.getRequestDispatcher("/confirmDBCreate.jsp");
        	dispatcher.forward(request,response);
       
    }
    
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

}
