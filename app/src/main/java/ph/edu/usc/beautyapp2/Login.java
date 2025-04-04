package ph.edu.usc.beautyapp2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    private EditText usernameInput;
    private Button loginButton;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameInput = findViewById(R.id.username_input);
        loginButton = findViewById(R.id.login_button);

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameInput.getText().toString().trim();
                if (isUsernameValid(username)) {
                    // Redirect to MainActivity
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(Login.this, "Invalid Username", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isUsernameValid(String username) {
        // Retrieve the list of valid usernames from SharedPreferences
        String validUsernames = sharedPreferences.getString("valid_usernames", "");
        String[] usernameArray = validUsernames.split(",");

        for (String validUsername : usernameArray) {
            if (validUsername.equals(username)) {
                return true;
            }
        }
        return false;
    }
}