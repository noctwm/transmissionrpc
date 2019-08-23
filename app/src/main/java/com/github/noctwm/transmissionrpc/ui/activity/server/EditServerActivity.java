package com.github.noctwm.transmissionrpc.ui.activity.server;

import androidx.appcompat.app.ActionBar;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.github.noctwm.R;
import com.github.noctwm.transmissionrpc.presentation.presenter.server.EditServerPresenter;
import com.github.noctwm.transmissionrpc.app.server.Server;
import com.github.noctwm.transmissionrpc.presentation.view.server.EditServerView;
import com.github.noctwm.transmissionrpc.ui.activity.main.MainActivity;

public class EditServerActivity extends MvpAppCompatActivity implements EditServerView {

    @InjectPresenter
    public EditServerPresenter presenter;

    @ProvidePresenter
    public EditServerPresenter provideEditServerPresenter() {
        return new EditServerPresenter(getIntent().getStringExtra(MainActivity.EXTRA_MESSAGE_SERVER_ID));
    }


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

    }

    /*@Override
    protected void onDestroy() {
        presenter.detachView();
        presenter.destroy();
        super.onDestroy();
    }*/

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
        if (server.isUseAuthentication()) {
            cbAuthorize.setChecked(true);
            etLogin.setEnabled(true);
            etLogin.setText(server.getLogin());
            etPassword.setEnabled(true);
            etPassword.setText(server.getPassword());
        }
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
                presenter.doneClicked(server);
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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



    @Override
    public void finishView() {
        finish();
    }

}
