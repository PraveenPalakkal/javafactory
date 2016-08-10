package com.app.framework.delegate;

import com.app.framework.exception.HexApplicationException;
import com.hexaware.framework.logger.LogFactory;
import com.hexaware.framework.logger.Logger;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HJSFBusinessDelegator extends HttpServlet
{
  private static final long serialVersionUID = 1L;
  Logger log = LogFactory.getLogger(HJSFBusinessDelegator.class);

  protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    this.log.info("*****Business Delegator*****");
    try
    {
      BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
      String json = "";
      if (br != null) {
        json = br.readLine();
      }

      String responseString = null;
      String referer = request.getHeader("referer");
      String url = referer + "serviceController/ws/service";
      this.log.debug("Input Param : " + json);
      responseString = sendPost(request, url, json);

      response.getWriter().append(responseString);
    }
    catch (HexApplicationException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    doGet(request, response);
  }

  private String sendPost(HttpServletRequest request, String url, String urlParameters) throws HexApplicationException
  {
    URL obj;
    try {
      String inputLine;
      obj = new URL(url);
      HttpURLConnection con = (HttpURLConnection)obj.openConnection();

      con.setRequestMethod("POST");
      con.setRequestProperty("User-Agent", "Mozilla/5.0");
      con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
      con.setRequestProperty("Content-Type", "application/json");
      Enumeration headerNames = request.getHeaderNames();
      while (headerNames.hasMoreElements()) {
        String key = (String)headerNames.nextElement();
        con.setRequestProperty(key, request.getHeader(key));
      }

      con.setDoOutput(true);
      DataOutputStream wr = new DataOutputStream(con.getOutputStream());
      wr.writeBytes(urlParameters);
      wr.flush();
      wr.close();

      int responseCode = con.getResponseCode();
      this.log.debug("\nSending 'POST' request to URL : " + url);
      this.log.debug("Response Code : " + responseCode);
      this.log.debug("Parameters : " + urlParameters);

      BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

      StringBuffer response = new StringBuffer();

      while ((inputLine = in.readLine()) != null)
        response.append(inputLine);

      in.close();
      this.log.debug("Business Delegate : " + response.toString());
      return response.toString();
    } catch (Exception e) {
      throw new HexApplicationException(e.getMessage());
    }
  }
}