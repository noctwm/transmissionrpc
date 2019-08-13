package com.github.noctwm.transmissionrpc.ui.server;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.github.noctwm.R;
import com.github.noctwm.transmissionrpc.server.Server;
import com.github.noctwm.transmissionrpc.ui.main.MainActivity;

public class EditServerActivity extends AppCompatActivity implements EditServerContract.EditServerView {
    private EditServerPresenter presenter;
    private EditText etServerName;
    private EditText etHost;
    private TextView etPort;
    private TextView etRpcPath;
    private EditText etLogin;
    private EditText etPassword;
    private CheckBox cbAuthorize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_server);

        ActionBar toolbar = getSupportActionBar();
        if (toolbar != null) {
            toolbar.setDisplayHomeAsUpEnabled(true);
        }

        etServerName = findViewById(R.id.et_server_name);
        etHost = findViewById(R.id.et_ip);
        etPort = findViewById(R.id.et_port);
        etPort.setFilters(new InputFilter[]{new PortInputFilter()});
        etPort.setText(String.valueOf(Server.DEFAULT_RPC_PORT));


        etRpcPath = findViewById(R.id.et_rpc_path);
        etRpcPath.setText(Server.DEFAULT_RPC_PATH);
        etLogin = findViewById(R.id.et_login);
        etPassword = findViewById(R.id.et_password);


        cbAuthorize = findViewById(R.id.cb_authentication);
        cbAuthorize.setOnCheckedChangeListener((buttonView, isChecked) -> {
            etLogin.setEnabled(isChecked);
            etPassword.setEnabled(isChecked);
        });


        presenter = new EditServerPresenter();
        presenter.attachView(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.viewIsReady();
    }

    @Override
    protected void onPause() {
        presenter.viewIsPaused();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        presenter.detachView();
        presenter.destroy();
        super.onDestroy();
    }

    @Override
    public String getServerId() {
        return getIntent().getStringExtra(MainActivity.EXTRA_MESSAGE_SERVER_ID);
    }

    @Override
    public void setTitle(int title) {
        ActionBar toolbar = getSupportActionBar();
        if (toolbar != null) {
            toolbar.setTitle(title);
        }
    }

    @Override
    public void setServerData(Server server) {
        etServerName.setText(server.getName());
        etHost.setText(server.getHost());
        etPort.setText(String.valueOf(server.getPort()));
        etRpcPath.setText(server.getRpcPath());
        cbAuthorize.setChecked(server.isUseAuthentication());
        etLogin.setEnabled(server.isUseAuthentication());
        etPassword.setEnabled(server.isUseAuthentication());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_activity_edit_server, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_done_activity_edit_server:
                presenter.doneClicked();
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    @Override
    public Server getServer() {
        Server server = new Server();
        server.setName(etServerName.getText().toString().trim());
        server.setHost(etHost.getText().toString().trim());
        if (TextUtils.isEmpty(etPort.getText())) {
            server.setPort(0);
        } else {
            server.setPort(Integer.parseInt(etPort.getText().toString().trim()));
        }
        server.setRpcPath(etRpcPath.getText().toString().trim());
        if (cbAuthorize.isChecked()) {
            server.setUseAuthentication(cbAuthorize.isChecked());
            server.setLogin(etLogin.getText().toString().trim());
            server.setPassword(etPassword.getText().toString().trim());
        }
        return server;
    }

    @Override
    public void showServerNameError() {
        etServerName.setError(getResources().getString(R.string.error_empty_field));
    }

    @Override
    public void showHostError() {
        etHost.setError(getResources().getString(R.string.error_empty_field));
    }

    @Override
    public void showPortError() {
        etPort.setError(getResources().getString(R.string.error_empty_field));
    }

    @Override
    public void showRpcPathError() {
        etRpcPath.setError(getResources().getString(R.string.error_empty_field));
    }

    /*@Override
    public String getServerName() {
        return etServerName.getText().toString().trim();
    }

    @Override
    public String getHost() {
        return etHost.getText().toString().trim();
    }

    @Override
    public int getPort() {
        return Integer.parseInt(etPort.getText().toString().trim());
    }

    @Override
    public String getRpcPath() {
        return etRpcPath.getText().toString().trim();
    }

    @Override
    public boolean isUseAuthentication() {
        return cbAuthorize.isChecked();
    }

    @Override
    public String getLogin() {
        return etLogin.getText().toString().trim();
    }

    @Override
    public String getPassword() {
        return etPassword.getText().toString().trim();
    }*/

    @Override
    public void finishView() {
        finish();
    }

}
