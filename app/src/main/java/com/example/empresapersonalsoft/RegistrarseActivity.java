package com.example.empresapersonalsoft;

import androidx.appcompat.app.AppCompatActivity;

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
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
            url = "http://172.16.62.245:80/WebServices/consulta.php?usr='" + usuario;
            jrq = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
            rq.add(jrq);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(this, "Error En La Petición", Toast.LENGTH_LONG).show();
        jetUsuario.requestFocus();
    }

    @Override
    public void onResponse(JSONObject response) {
        Toast.makeText(this, "Usuario registrado - encontrado", Toast.LENGTH_SHORT).show();

        JSONArray jsonArray = response.optJSONArray("datos");
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
            e.printStackTrace();
        }
    }
}