package ru.realityfamily.statecontrol;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
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
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TaskModel {

    public String Game = "";
    public String Device = "";
    public String Status = "";

    public void GetGames(final Context context, final MyAdapter adapter, final Handler gameHandler, final Handler deviceHandler) {
        Game = "";
        Message gMsg = new Message().obtain();
        gMsg.obj = Game;
        gameHandler.sendMessage(gMsg);

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

                        Status = json.getString("Status");
                        // Sending received data to RecyclerView
                        adapter.setItems(tempList, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                GetDevices(context, adapter, gameHandler, deviceHandler, ((Button) view).getText().toString());
                            }
                        });
                    } else {
                        // Error, when response don't have enough information
                        Toast.makeText(context, "No content in response.", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // Error in parsing JSON response
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
    }

    public void GetDevices(final Context context, final MyAdapter adapter, Handler gameHandler, final Handler deviceHandler, final String game) {
        Game = game;
        Message gMsg = new Message().obtain();
        gMsg.obj = game;
        gameHandler.sendMessage(gMsg);

        Device = "";
        Message dMsg = new Message().obtain();
        dMsg.obj = Device;
        deviceHandler.sendMessage(dMsg);

        RequestQueue queue = Volley.newRequestQueue(context);
        String URL = context.getResources().getString(R.string.BaseUrl);
        StringRequest request = new StringRequest(Request.Method.GET,
                URL + String.format("api/control/%s/devices", game),
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
                                Status = json.getString("Status");
                                adapter.setItems(tempList, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        GetStates(context, adapter, deviceHandler, null, ((Button) view).getText().toString());
                                    }
                                });
                            } else {
                                // Error, when response don't have enough information
                                Toast.makeText(context, "No content in response.", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            // Error in parsing JSON response
                            Toast.makeText(context, "JSONException: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Error, when something went wrong in request
                        Toast.makeText(context, "Volley ERROR: " + new String(error.networkResponse.data), Toast.LENGTH_LONG).show();
                    }
                }
        );
        queue.add(request);
    }

    public void GetStates(final Context context, final MyAdapter adapter, Handler deviceHandler,
                          final Handler disconnectHandler, final String device) {
        if (disconnectHandler != null) {
            disconnectHandler.sendEmptyMessage(View.VISIBLE);
        }

        Device = device;
        Message msg = new Message().obtain();
        msg.obj = device;
        deviceHandler.sendMessage(msg);

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
                                Status = json.getString("Status");
                                adapter.setItems(tempList, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        sendState(context, ((Button) view).getText().toString());
                                    }
                                });
                            } else {
                                // Error, when response don't have enough information
                                Toast.makeText(context, "No content in response.", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            // Error in parsing JSON response
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

    public void sendState(final Context context, final String state) {
        RequestQueue queue = Volley.newRequestQueue(context);
        String URL = context.getResources().getString(R.string.BaseUrl);

        // Packing choose information to JSON body
        JSONObject json = new JSONObject();
        try {
            json.put("DeviceId", Device);
            json.put("GameName", Game);
            json.put("State", state);
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

    public void GetLaunchers(final Context context, final MyAdapter adapter,
                             final Handler gameHandler, final Handler disconnectHandler,
                             final Handler deviceHandler) {
        Device = "";
        Message gMsg = new Message().obtain();
        gMsg.obj = Device;
        deviceHandler.sendMessage(gMsg);

        RequestQueue queue = Volley.newRequestQueue(context);
        String URL = context.getResources().getString(R.string.BaseUrl);
        StringRequest request = new StringRequest(Request.Method.GET, URL + "api/control/launchers", new Response.Listener<String>() {
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

                        Status = json.getString("Status");
                        // Sending received data to RecyclerView
                        adapter.setItems(tempList, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                GetLaunchersGames(context, adapter, gameHandler, deviceHandler, disconnectHandler, ((Button) view).getText().toString());
                            }
                        });
                    } else {
                        // Error, when response don't have enough information
                        Toast.makeText(context, "No content in response.", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // Error in parsing JSON response
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
    }

    public void GetLaunchersGames(final Context context, final MyAdapter adapter,
                                  Handler gameHandler, final Handler deviceHandler,
                                  final Handler disconnectHandler, final String device) {
        if (disconnectHandler != null) {
            disconnectHandler.sendEmptyMessage(View.GONE);
        }

        Device = device;
        Message dMsg = new Message().obtain();
        dMsg.obj = device;
        deviceHandler.sendMessage(dMsg);

        Game = "";
        Message gMsg = new Message().obtain();
        gMsg.obj = Game;
        gameHandler.sendMessage(gMsg);

        RequestQueue queue = Volley.newRequestQueue(context);
        String URL = context.getResources().getString(R.string.BaseUrl);
        StringRequest request = new StringRequest(Request.Method.GET,
                URL + String.format("api/control/%s/games", device),
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
                                Status = json.getString("Status");
                                adapter.setItems(tempList, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        GetStates(context, adapter, deviceHandler, disconnectHandler, ((Button) view).getText().toString());
                                    }
                                });
                            } else {
                                // Error, when response don't have enough information
                                Toast.makeText(context, "No content in response.", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            // Error in parsing JSON response
                            Toast.makeText(context, "JSONException: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Error, when something went wrong in request
                Toast.makeText(context, "Volley ERROR: " + new String(error.networkResponse.data), Toast.LENGTH_LONG).show();
            }
        }
        );
        queue.add(request);
    }

    public void CloseApp(final Context context) {
        RequestQueue queue = Volley.newRequestQueue(context);
        String URL = context.getResources().getString(R.string.BaseUrl);
        // Creating request
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                URL + String.format("api/control/%s/%s/disconnect", Device, Game),
                new JSONObject(),
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
}
