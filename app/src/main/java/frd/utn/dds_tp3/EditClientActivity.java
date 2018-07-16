package frd.utn.dds_tp3;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import frd.utn.dds_tp3.services.RESTService;

public class EditClientActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_client);

        Intent intent = getIntent();
        final Integer idCliente = intent.getIntExtra(MainActivity.EXTRA_IDCLIENTE, 0);
        new GetClientData(idCliente).execute();
        Button saveButton = findViewById(R.id.seveButton);
        Button cancelButton = findViewById(R.id.cancelButton);

        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                new SaveClientData(idCliente,  view.getContext()).execute();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                Intent intent = new Intent( view.getContext(), MovementsActivity.class);
                intent.putExtra(MainActivity.EXTRA_IDCLIENTE, idCliente);
                view.getContext().startActivity(intent);
            }
        });


    }

    private class GetClientData extends AsyncTask<String, String, String> {

        private int idCliente;

        public GetClientData(int idCliente){
            this.idCliente = idCliente;
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = null;
            try {
                result = RESTService.makeGetRequest(
                        getResources().getString(R.string.base_url) + "/cliente/" + this.idCliente);
            } catch (IOException e) {
//                Toast notificacion = Toast.makeText(
//                        getApplicationContext(), getResources().getString(R.string.error_connection),
//                        Toast.LENGTH_LONG);
//                notificacion.show();
            }
            return result;
        }

        protected void onPostExecute(String result) {
            if (result !=null)
                if(result.length() > 0 ) {
                    try {
                        JSONObject cliente = new JSONObject(result);
                        EditText editNombre = findViewById(R.id.editNombre);
                        EditText editCuil = findViewById(R.id.editCuil);
                        editNombre.setText(cliente.getString("nombre"));
                        editCuil.setText(String.valueOf(cliente.getInt("cuil")));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

        }
    }

    private class SaveClientData extends AsyncTask<String, String, String> {

        private int idCliente;
        private Context context;

        public SaveClientData(int idCliente, Context context){
            this.idCliente = idCliente;
            this.context = context;
        }
        @Override
        protected String doInBackground(String... strings) {
            String result = null;
            try {

                EditText editNombre = findViewById(R.id.editNombre);
                EditText editCuil = findViewById(R.id.editCuil);
                JSONObject cliente = new JSONObject("{cuil:'" + editCuil.getText().toString() + "',id:" + this.idCliente + ",nombre:'" + editNombre.getText().toString() + "'}");
                result = RESTService.callREST(getResources().getString(R.string.base_url) + "/cliente/" + this.idCliente,
                        "PUT",cliente);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return result;
        }

        protected void onPostExecute(String result) {

            Intent intent = new Intent(this.context, MovementsActivity.class);
            intent.putExtra(MainActivity.EXTRA_IDCLIENTE, idCliente);
            this.context.startActivity(intent);

        }
    }

}

