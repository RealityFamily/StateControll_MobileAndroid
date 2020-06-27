package ru.realityfamily.statecontrol;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TaskModel {

    private static String Game;
    private static String Device;

    public void RunAdapter(final Context context, final MyAdapter adapter, List<String> Dataset, final String Status) {

        // Setting a custom method depending on the data received from the server
        View.OnClickListener mListener = null;

        //Getting information of running games from server
        if (Status.equals("None")) {
            RequestQueue queue = Volley.newRequestQueue(context);
            String URL = context.getResources().getString(R.string.BaseUrl);
            StringRequest request = new StringRequest(Request.Method.GET, URL + "api/control/games", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    // Getting information from server
                    try {
                        JSONObject json = new JSONObject(response);
                        // Checking that response has correct fields
                        if (json.has("Data") && json.has("Status")) {
                            List<String> tempList = new ArrayList<>();
                            JSONArray tempArray = json.getJSONArray("Data");
                            for (int i = 0; i < tempArray.length(); i++) {
                                tempList.add(tempArray.getString(i));
                            }
                            // Sending received data to RecyclerView
                            new TaskModel().RunAdapter(context, adapter, tempList, json.getString("Status"));
                        } else {
                            // Error, when response don't have enough information
                            Toast.makeText(context, "No content in response.", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        // Error in parsin JSON response
                        Toast.makeText(context, "JSONException: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // Error, when something went wrong in request
                    Toast.makeText(context, "Volley ERROR", Toast.LENGTH_LONG).show();
                }
            });
            queue.add(request);
        } else if (Status.equals("Games")) {
            mListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Game = ((Button) view).getText().toString();

                    RequestQueue queue = Volley.newRequestQueue(context);
                    String URL = context.getResources().getString(R.string.BaseUrl);
                    StringRequest request = new StringRequest(Request.Method.GET,
                            URL + String.format("api/control/%s/devices", ((Button) view).getText()),
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    // Getting information from server
                                    try {
                                        JSONObject json = new JSONObject(response);
                                        // Checking that response has correct fields
                                        if (json.has("Data") && json.has("Status")) {
                                            List<String> tempList = new ArrayList<>();
                                            JSONArray tempArray = json.getJSONArray("Data");
                                            for (int i = 0; i < tempArray.length(); i++) {
                                                tempList.add(tempArray.getString(i));
                                            }
                                            // Sending received data to RecyclerView
                                            new TaskModel().RunAdapter(context, adapter, tempList, json.getString("Status"));
                                        } else {
                                            // Error, when response don't have enough information
                                            Toast.makeText(context, "No content in response.", Toast.LENGTH_LONG).show();
                                        }
                                    } catch (JSONException e) {
                                        // Error in parsin JSON response
                                        Toast.makeText(context, "JSONException: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Error, when something went wrong in request
                            Toast.makeText(context, "Volley ERROR: " + new String(error.networkResponse.data), Toast.LENGTH_LONG).show();
                        }
                    });
                    queue.add(request);
                }
            };
        } else if (Status.equals("Devices")) {
            mListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Device = ((Button) view).getText().toString();

                    RequestQueue queue = Volley.newRequestQueue(context);
                    String URL = context.getResources().getString(R.string.BaseUrl);
                    StringRequest request = new StringRequest(Request.Method.GET, URL + String.format("api/control/%s/states", Game),
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    // Getting information from server
                                    try {
                                        JSONObject json = new JSONObject(response);
                                        // Checking that response has correct fields
                                        if (json.has("Data") && json.has("Status")) {
                                            List<String> tempList = new ArrayList<>();
                                            JSONArray tempArray = json.getJSONArray("Data");
                                            for (int i = 0; i < tempArray.length(); i++) {
                                                tempList.add(tempArray.getString(i));
                                            }
                                            // Sending received data to RecyclerView
                                            new TaskModel().RunAdapter(context, adapter, tempList, json.getString("Status"));
                                        } else {
                                            // Error, when response don't have enough information
                                            Toast.makeText(context, "No content in response.", Toast.LENGTH_LONG).show();
                                        }
                                    } catch (JSONException e) {
                                        // Error in parsin JSON response
                                        Toast.makeText(context, "JSONException: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Error, when something went wrong in request
                            Toast.makeText(context, "Volley ERROR: " + new String(error.networkResponse.data), Toast.LENGTH_LONG).show();
                        }
                    });
                    queue.add(request);
                }
            };
        } else if (Status.equals("States"))
        {
            mListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RequestQueue queue = Volley.newRequestQueue(context);
                    String URL = context.getResources().getString(R.string.BaseUrl);

                    // Packing choose information to JSON body
                    JSONObject json = new JSONObject();
                    try {
                        json.put("DeviceId", Device);
                        json.put("GameName", Game);
                        json.put("State", ((Button) view).getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    // Creating request
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                            URL + "api/control/state",
                            json,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    // Getting information from server
                                    Toast.makeText(context, "Volley ERROR: " + response, Toast.LENGTH_LONG).show();
                                }
                            },
                            null);

                    queue.add(request);
                }
            };
        } else {
            return;
        }

        // Putting received information to RecyclerView
        adapter.setItems(Dataset, mListener);
    }
}
