package frd.utn.dds_tp3;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import frd.utn.dds_tp3.entities.Cuenta;
import frd.utn.dds_tp3.services.RESTService;

public class MovementsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movements);

        Intent intent = getIntent();
        final Integer idCliente = intent.getIntExtra(MainActivity.EXTRA_IDCLIENTE, 0);
        Button editButton = findViewById(R.id.editbutton);
        Button logoutButton = findViewById(R.id.logoutButton);

        new AccountsTask(idCliente).execute();

        final Spinner accountsCmb = findViewById(R.id.accountsCmb);
        accountsCmb.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView) view).setTextColor(Color.BLACK);
                Cuenta selectedAccount = (Cuenta) accountsCmb.getSelectedItem();
                new MovementsTask(selectedAccount.getId()).execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        editButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(), EditClientActivity.class);
                intent.putExtra(MainActivity.EXTRA_IDCLIENTE, idCliente );
                view.getContext().startActivity(intent);

            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                view.getContext().startActivity(intent);
            }
        });


    }

    private class AccountsTask extends AsyncTask<String, String, String> {

        Integer idCliente ;

        private  AccountsTask (Integer idCliente){
            this.idCliente = idCliente;
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = null;
            try {
                result = RESTService.makeGetRequest(
                        getResources().getString(R.string.base_url) + "/cuenta/deCliente/" + this.idCliente);
            } catch (IOException e) {
                Toast notificacion = Toast.makeText(
                        getApplicationContext(), getResources().getString(R.string.error_connection),
                        Toast.LENGTH_LONG);
                notificacion.show();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null)
            if (result.length() > 0) {
                try {
                    JSONArray accounts = new JSONArray(result);

                    if (accounts.length() > 0) {


                        List<Cuenta> cuentas = new ArrayList<>();
                        for (int i = 0 ; i< accounts.length(); i++){
                            JSONObject acc = accounts.getJSONObject(i);
                            cuentas.add(new Cuenta(acc.getLong("id"),acc.getLong("numero"),null, acc.getInt("idCliente"),acc.getDouble("saldo")));
                        }

                        if (cuentas.size() > 0){
                            Spinner accountsCmb = findViewById(R.id.accountsCmb);
                            ArrayAdapter<Cuenta> dataAdapter = new ArrayAdapter<Cuenta>(getApplicationContext(),
                                    android.R.layout.simple_spinner_item, cuentas);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            accountsCmb.setAdapter(dataAdapter);
                        }

                    } else {
                        Toast notificacion = Toast.makeText(
                                getApplicationContext(), getResources().getString(R.string.error_accounts),
                                Toast.LENGTH_LONG);
                        notificacion.show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast notificacion = Toast.makeText(
                        getApplicationContext(), "Error",
                        Toast.LENGTH_LONG);
                notificacion.show();
            }
        }


    }

    private class MovementsTask extends AsyncTask<String, String, String> {

        Long idCuenta;

        public MovementsTask (Long idCuenta) {
            this.idCuenta = idCuenta;
        }


        @Override
        protected String doInBackground(String... strings) {
            String result = null;
            try {
                result = RESTService.makeGetRequest(
                        getResources().getString(R.string.base_url) + "/movimiento/deCuenta/" + this.idCuenta);
            } catch (IOException e) {
                Toast notificacion = Toast.makeText(
                        getApplicationContext(), getResources().getString(R.string.error_connection),
                        Toast.LENGTH_LONG);
                notificacion.show();
            }
            return result;
        }

        protected void onPostExecute(String result) {
            if (result != null)
            if (result.length()>0) {
                try {
                    JSONArray movements = new JSONArray(result);

                    TableLayout movsTableLayout = findViewById(R.id.movsTableLayout);

                    movsTableLayout.removeAllViews();

                    TableRow tr_head = new TableRow(getApplicationContext());
                    tr_head.setId(10);
                    tr_head.setBackgroundColor(Color.GRAY);        // part1
                    tr_head.setLayoutParams(new TableRow.LayoutParams(
                            TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.WRAP_CONTENT));

                    TextView label1 = new TextView(getApplicationContext());
                    label1.setId(20);
                    label1.setText("Fecha");
                    label1.setTextColor(Color.WHITE);          // part2
                    label1.setPadding(5, 5, 5, 5);
                    tr_head.addView(label1);// add the column to the table row here

                    TextView label2 = new TextView(getApplicationContext());
                    label2.setId(21);
                    label2.setText("Descripcion");
                    label2.setTextColor(Color.WHITE);          // part2
                    label2.setPadding(5, 5, 5, 5);
                    tr_head.addView(label2);// add the column to the table row here

                    TextView label3 = new TextView(getApplicationContext());
                    label3.setId(22);
                    label3.setText("Importe");
                    label3.setTextColor(Color.WHITE);          // part2
                    label3.setPadding(5, 5, 5, 5);
                    tr_head.addView(label3);// add the column to the table row here

                    movsTableLayout.addView(tr_head, new TableLayout.LayoutParams(
                            TableRow.LayoutParams.WRAP_CONTENT,                    //part4
                            TableRow.LayoutParams.WRAP_CONTENT));

                    TableRow[] tr_headA = new TableRow[movements.length()];
                    TextView[] textArray1 = new TextView[movements.length()];
                    TextView[] textArray2 = new TextView[movements.length()];
                    TextView[] textArray3 = new TextView[movements.length()];

                    for (int i = 0 ; i< movements.length(); i++){

                        JSONObject movJ = movements.getJSONObject(i);

                        //Create the tablerows
                        tr_headA[i] = new TableRow(getApplicationContext());
                        tr_headA[i].setId(i+1);
                        tr_headA[i].setBackgroundColor(Color.GRAY);
                        tr_headA[i].setLayoutParams(new TableRow.LayoutParams(
                                TableRow.LayoutParams.MATCH_PARENT,
                                TableRow.LayoutParams.WRAP_CONTENT));

                        textArray1[i] = new TextView(getApplicationContext());
                        textArray1[i].setId(20 + i);
                        textArray1[i].setText(((String) movJ.get("creado")).substring(0,10));
                        textArray1[i].setTextColor(Color.WHITE);          // part2
                        textArray1[i].setPadding(5, 5, 5, 5);
                        tr_headA[i].addView(textArray1[i]);// add the column to the table row here

                        textArray2[i] = new TextView(getApplicationContext());
                        textArray2[i].setId(21 + i);
                        textArray2[i].setText(movJ.getString("descripcion"));
                        textArray2[i].setTextColor(Color.WHITE);          // part2
                        textArray2[i].setPadding(5, 5, 5, 5);
                        tr_headA[i].addView(textArray2[i]);// add the column to the table row here

                        textArray3[i] = new TextView(getApplicationContext());
                        textArray3[i].setId(22 + i);
                        textArray3[i].setText(String.valueOf( movJ.getDouble("importe") ));
                        textArray3[i].setTextColor(Color.WHITE);          // part2
                        textArray3[i].setPadding(5, 5, 5, 5);
                        tr_headA[i].addView(textArray3[i]);// add the column to the table row here

                        movsTableLayout.addView(tr_headA[i], new TableLayout.LayoutParams(
                                TableRow.LayoutParams.WRAP_CONTENT,                    //part4
                                TableRow.LayoutParams.WRAP_CONTENT));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }


    }


}
