package hr.foi.air1719.webservice;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

/**
 * Created by DrazenVuk on 28.10.2017..
 */

public class WebService extends AsyncTask<String, Void, String> {

    RestMethods _restMethods = null;
    JSONObject  _jsonObject = null;
    String _url;
    private AsyncResponse delegate = null;

    public enum RestMethods {
        GET, PUT, POST, DELETE, UPDATE, PATCH
    }

    public WebService(RestMethods restMethods, JSONObject jsonObject, String url, AsyncResponse  _delegate)
    {
        _restMethods=restMethods;
        _jsonObject=jsonObject;
        _url=url;
        delegate = _delegate;
    }

    @Override
    protected String doInBackground(String... strings) {

        if(_restMethods==null) return "Wrong rest method";

        switch (_restMethods)
        {
            case GET:
                return getDataFromServer(_url);
            case PUT:
                return putDataOnServer(_jsonObject, _url);

        }

        return "Wrong rest method";
    }

    @Override
    protected void onPostExecute(String message) {
        delegate.processFinish(message);
    }

    private String putDataOnServer(JSONObject jsonObject, String url)
    {
        InputStream inputStream = null;
        String result = "";

        try {
            String json = "";
            HttpClient httpclient = new DefaultHttpClient();
            HttpPut httpPut = new HttpPut(url);
            json = jsonObject.toString();
            StringEntity se = new StringEntity(json);
            httpPut.setEntity(se);
            httpPut.addHeader("Accept", "application/json");
            httpPut.addHeader("Content-type", "application/json");
            HttpResponse httpResponse = httpclient.execute(httpPut);
            inputStream = httpResponse.getEntity().getContent();

            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private String getDataFromServer(String url)
    {
        BufferedReader in = null;
        String data = null;

        try{
            HttpClient httpclient = new DefaultHttpClient();

            HttpGet request = new HttpGet();
            URI website = new URI(url);
            request.setURI(website);
            HttpResponse response = httpclient.execute(request);
            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            data = convertBufferedReaderToString(in);
        }catch(Exception e){
            e.printStackTrace();
        }
        return data;
    }

    private String convertBufferedReaderToString(BufferedReader bufferedReader) throws IOException{
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        bufferedReader.close();
        return result;
    }

    private String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }

    public static interface AsyncResponse  {
        void processFinish(String output);
    }
}
