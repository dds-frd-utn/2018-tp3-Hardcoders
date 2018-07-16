package frd.utn.dds_tp3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import frd.utn.dds_tp3.services.RESTService;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_IDCLIENTE = "frd.utn.dds_tp3.EXTRA_IDCLIENTE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button loginButton = findViewById(R.id.loginButton);
        final EditText cuilText = findViewById(R.id.cuilText);
        final EditText passText = findViewById(R.id.passText);

        cuilText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                loginButton.setEnabled(validateLoginFields(cuilText, passText));
            }
        });

        passText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                loginButton.setEnabled(validateLoginFields(cuilText, passText));
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new LoginTask(cuilText.getText().toString(), view.getContext()).execute();
            }
        });
    }

    boolean validateLoginFields(EditText cuilText, EditText passText) {
        return cuilText.getText().toString().trim().length() >= 4 && passText.getText().toString().trim().length() >= 4;
    }

    private class LoginTask extends AsyncTask<String, String, String> {

        private String cuil;
        private Context context;

        private LoginTask(String cuil, Context context) {
            this.cuil = cuil;
            this.context = context;
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = null;
            try {
                result = RESTService.makeGetRequest(
                        getResources().getString(R.string.base_url) + "/cliente/byCuil/" + this.cuil);
            } catch (IOException e) {
//                Toast notificacion = Toast.makeText(
//                        getApplicationContext(), getResources().getString(R.string.error_connection),
//                        Toast.LENGTH_LONG);
//                notificacion.show();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null)
                if (result.length() > 0) {
                    try {
                        JSONObject cliente = new JSONObject(result);
                        Integer idCliente = cliente.getInt("id");
                        Intent intent = new Intent(this.context, MovementsActivity.class);
                        intent.putExtra(MainActivity.EXTRA_IDCLIENTE, idCliente);
                        context.startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    EditText cuilText = findViewById(R.id.cuilText);
                    cuilText.setError(getResources().getString(R.string.error_invalid_cuil));
                }
        }
    }


}
