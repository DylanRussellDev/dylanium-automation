package io.github.dylanrusselldev.utilities.api;

import io.github.dylanrusselldev.utilities.logging.LoggerClass;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/*
 * Filename: APIRequest.java
 * Author: Dylan Russell
 * Purpose: Provides methods to send GET and POST requests.
 */
public class APIRequest {

    private static final LoggerClass LOGGER = new LoggerClass(APIRequest.class);

    /**
     * Sends a GET request and returns the response.
     *
     * @param url  the URL with the designated API path
     * @param body the request body
     */
    public static JSONObject sendGETRequest(String url, String body) {

        HttpURLConnection httpURLConnection = newConnection(url, "GET");

        httpURLConnection.setRequestProperty("Connection", "keep-alive");
        httpURLConnection.setRequestProperty("Content-Type", "application/json");
        httpURLConnection.setRequestProperty("Accept", "*/*");

        try {
            OutputStream outputStream = httpURLConnection.getOutputStream();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
            outputStreamWriter.write(body);
            outputStreamWriter.flush();
            outputStreamWriter.close();
            outputStream.close();
        } catch (IOException e) {
            LOGGER.fail("Unable to send GET request", e);
            return null;
        }

        return requestResponse(httpURLConnection);

    }

    /**
     * Sends a GET request and returns the response.
     *
     * @param url  the URL with the designated API path
     * @param body the request body
     */
    public static JSONObject sendPOSTRequest(String url, String body) {

        HttpURLConnection httpURLConnection = newConnection(url, "POST");

        httpURLConnection.setRequestProperty("Connection", "keep-alive");
        httpURLConnection.setRequestProperty("Content-Type", "application/json");
        httpURLConnection.setRequestProperty("Accept", "*/*");

        try {
            OutputStream outputStream = httpURLConnection.getOutputStream();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
            outputStreamWriter.write(body);
            outputStreamWriter.flush();
            outputStreamWriter.close();
            outputStream.close();
        } catch (IOException e) {
            LOGGER.fail("Unable to send POST request", e);
            return null;
        }

        return requestResponse(httpURLConnection);

    }

    /**
     * Creates a new HTTP URL Connection to send requests to.
     *
     * @param uri the URL for the connection
     * @param requestType the type of request e.g. GET or POST
     */
    private static HttpURLConnection newConnection(String uri, String requestType) {
        try {
            URL url = new URL(uri);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod(requestType);
            httpURLConnection.setDoOutput(true);
            return httpURLConnection;
        } catch (IOException e) {
            LOGGER.fail("Unable to create a new " + requestType + " connection", e);
            return null;
        }
    }

    /**
     * Return the response as a JSON object.
     *
     * @param httpURLConnection  the url connection object
     */
    private static JSONObject requestResponse(HttpURLConnection httpURLConnection) {

        try {
            BufferedReader bufferedReader;

            if (httpURLConnection.getResponseCode() >= 100 && httpURLConnection.getResponseCode() >= 399) {
                bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            } else {
                bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getErrorStream()));
            }

            StringBuilder stringBuilder = new StringBuilder();
            String response;

            while ((response = bufferedReader.readLine()) != null) {
                stringBuilder.append(response);
            }

            return new JSONObject(stringBuilder.toString());

        } catch (IOException i) {
            LOGGER.fail("Unable to create JSONObject from response body", i);
            return null;
        }

    }

}
