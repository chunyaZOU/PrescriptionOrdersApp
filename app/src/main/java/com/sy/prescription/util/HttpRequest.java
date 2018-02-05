package com.sy.prescription.util;

import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.sy.prescription.R;
import com.sy.prescription.config.IMApplication;
import com.sy.prescription.model.Basic;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HttpRequest {
    /******
     * 传递的参数： 接口目录，方法名称，参数列表，回调方法
     ********/
    public static RequestQueue requestQueue = null;

    @SuppressWarnings("rawtypes")
    public static Object HttpPost(String url, String version, final String method, int methodType, final Map<String, String> params, final HttpCallback httpCallback, Map<String, JSONArray>... array) {
        if (requestQueue == null)
            requestQueue = Volley.newRequestQueue(IMApplication.getAppContext());
        Request stringRequest = null;
        switch (methodType) {
            case Urls.MethodType.POST:
                try {
                    JSONObject jsonObject = new JSONObject(params);
                    if (array.length != 0) {
                        Map<String, JSONArray> map = array[0];
                        for (Map.Entry<String, JSONArray> entry : map.entrySet()) {
                            jsonObject.put(entry.getKey(), entry.getValue());
                        }
                    }
                    stringRequest = new JsonObjectRequest(Request.Method.POST, url + version + method, jsonObject, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            progressResult(response.toString(), httpCallback);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            ToastUtil.show(R.string.network_error);
                           httpCallback.httpFail(error.getMessage());
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            return params;
                        }

                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            return getHeader();
                        }
                    };
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case Urls.MethodType.GET:
                StringBuilder sb = new StringBuilder(url + version + method + "?");
                Iterator it = params.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry entry = (Map.Entry) it.next();
                    try {
                        if (!it.hasNext())
                            sb.append(entry.getKey() + "=" + URLEncoder.encode(entry.getValue().toString(), "UTF-8"));
                        else
                            sb.append(entry.getKey() + "=" + URLEncoder.encode(entry.getValue().toString(), "UTF-8") + "&");
                    } catch (Exception e) {
                    }
                }
                stringRequest = new StringRequest(Request.Method.GET, sb.toString(), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressResult(response, httpCallback);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        ToastUtil.show(R.string.network_error);
                      //  httpCallback.httpFail(error.getMessage());
                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        return getHeader();
                    }
                };
                break;
        }
        stringRequest.setTag(System.currentTimeMillis());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
        return stringRequest.getTag();
    }

    public static void cancel(Object tag) {
        if (null != requestQueue)
            requestQueue.cancelAll(tag);
    }

    public static Map<String, String> getHeader() {
        Map<String, String> _map = new HashMap<>();
        String authorization = PreferenceUtil.getAuthorization();
        _map.put("sess_id", authorization);
        return _map;
    }

    private static void progressResult(String response, HttpCallback httpCallback) {
        if (!TextUtils.isEmpty(response)) {
            Basic basic = new Gson().fromJson(response, Basic.class);
            if (basic.getStatus() == 1)
                httpCallback.httpSuccess(response);
            else {
                if (!TextUtils.isEmpty(basic.getMsg()))
                    ToastUtil.show(basic.getMsg());
                httpCallback.httpFail(response);
            }
        }
    }

    public interface HttpCallback {
        void httpSuccess(String response);

        void httpFail(String response);
    }
}