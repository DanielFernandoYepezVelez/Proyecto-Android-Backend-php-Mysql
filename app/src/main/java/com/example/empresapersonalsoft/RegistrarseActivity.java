package com.example.empresapersonalsoft;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegistrarseActivity extends AppCompatActivity implements Response.Listener<JSONObject>,Response.ErrorListener {
    EditText jetUsuario, jetNombre, jetCorreo, jetClave;
    CheckBox jcbActivo;
    RequestQueue rq;
    JsonRequest jrq;

    String usuario, nombre, correo, clave, url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);
        getSupportActionBar().hide();

        jetUsuario = findViewById(R.id.etusuario);
        jetNombre = findViewById(R.id.etnombre);
        jetCorreo = findViewById(R.id.etcorreo);
        jetClave = findViewById(R.id.etclave);

        jcbActivo = findViewById(R.id.cbactivo);
        rq = Volley.newRequestQueue(this); //conexion a internet
    }

    public void consultar(View view) {
        usuario = jetUsuario.getText().toString();

        if (usuario.isEmpty()) {
            Toast.makeText(this, "Usuario Requerido", Toast.LENGTH_LONG).show();
            jetUsuario.requestFocus();
        } else {
            url = "http://172.16.60.56:80/Proyecto-Backend-Php-Android/consulta.php?usr=" + usuario;
            jrq = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
            rq.add(jrq);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        System.out.println("Aqui tenemos el error: " + error);
        Toast.makeText(this, "Error En La Petición", Toast.LENGTH_LONG).show();
        jetUsuario.requestFocus();
    }

    @Override
    public void onResponse(JSONObject response) {
        Toast.makeText(this, "Usuario registrado - encontrado", Toast.LENGTH_SHORT).show();

        JSONArray jsonArray = response.optJSONArray("datos");

        System.out.println("segundo error" + jsonArray);
        JSONObject jsonObject = null;

        try {
            jsonObject = jsonArray.getJSONObject(0);//posicion 0 del arreglo....
            jetNombre.setText(jsonObject.optString("nombre"));
            jetCorreo.setText(jsonObject.optString("correo"));
            jetClave.setText(jsonObject.optString("clave"));

            if (jsonObject.optString("activo").equals("si")) {
                jcbActivo.setChecked(true);
            } else {
                jcbActivo.setChecked(false);
            }
        } catch (JSONException e) {
            System.out.println("aqui se estalla");
            e.printStackTrace();
        }
    }

    public void adicionar(View view){
        usuario=jetUsuario.getText().toString();
        nombre=jetNombre.getText().toString();
        correo=jetCorreo.getText().toString();
        clave=jetClave.getText().toString();

        if (usuario.isEmpty() || nombre.isEmpty() || correo.isEmpty() || clave.isEmpty()){
            Toast.makeText(this, "Todos los datos son requeridos", Toast.LENGTH_SHORT).show();
            jetUsuario.requestFocus();
        } else {
            url = "http://172.16.60.56:80/Proyecto-Backend-Php-Android/registrocorreo.php";

            StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            limpiar_campos();
                            Toast.makeText(getApplicationContext(), "Registro de usuario realizado!", Toast.LENGTH_LONG).show();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Registro de usuario incorrecto!", Toast.LENGTH_LONG).show();
                        }
                    })
            {
                @Override
                protected Map<String, String> getParams()
                {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("usr",jetUsuario.getText().toString().trim());
                    params.put("nombre", jetNombre.getText().toString().trim());
                    params.put("correo",jetCorreo.getText().toString().trim());
                    params.put("clave",jetClave.getText().toString().trim());
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(postRequest);
        }
    }

    public void actualizar(View view){
        usuario=jetUsuario.getText().toString();
        nombre=jetNombre.getText().toString();
        correo=jetCorreo.getText().toString();
        clave=jetClave.getText().toString();

        if (usuario.isEmpty() || nombre.isEmpty() || correo.isEmpty() || clave.isEmpty()){
            Toast.makeText(this, "Todos los datos son requeridos", Toast.LENGTH_SHORT).show();
            jetUsuario.requestFocus();
        } else {
            url = "http://172.16.60.56:80/Proyecto-Backend-Php-Android/actualiza.php";

            StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    limpiar_campos();
                    Toast.makeText(getApplicationContext(), "Registro de usuario realizado!", Toast.LENGTH_LONG).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Registro de usuario incorrecto!", Toast.LENGTH_LONG).show();
                }
            })
            {
                @Override
                protected Map<String, String> getParams()
                {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("usr",jetUsuario.getText().toString().trim());
                    params.put("nombre", jetNombre.getText().toString().trim());
                    params.put("correo",jetCorreo.getText().toString().trim());
                    params.put("clave",jetClave.getText().toString().trim());
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(postRequest);
        }
    }

    public void anular(View view){
        usuario=jetUsuario.getText().toString();

        if (usuario.isEmpty()){
            Toast.makeText(this, "El Usuario Es Requerido", Toast.LENGTH_SHORT).show();
            jetUsuario.requestFocus();
        } else {
            url = "http://172.16.60.56:80/Proyecto-Backend-Php-Android/anula.php";

            StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    limpiar_campos();
                    Toast.makeText(getApplicationContext(), "Registro Anulado!", Toast.LENGTH_LONG).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Error Anulando!", Toast.LENGTH_LONG).show();
                }
            })
            {
                @Override
                protected Map<String, String> getParams()
                {
                    Map<String, String> params = new HashMap<String, String>();

                    params.put("usr",jetUsuario.getText().toString().trim());
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(postRequest);
        }
    }

    public void limpiar(View view) {
        limpiar_campos();
    }

    public void regresar(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void limpiar_campos(){
        jetUsuario.setText("");
        jetNombre.setText("");
        jetCorreo.setText("");
        jetClave.setText("");
        jetUsuario.requestFocus();
    }

}