//import static com.google.common.base.Verify.verify;
//import static org.mockito.Mockito.*;
//import static org.junit.Assert.*;
//
//import android.widget.Toast;
//
//import com.example.template.view.LoginActivity;
//import com.example.template.view.MainActivity;
//import com.google.android.gms.tasks.Task;
//import com.google.errorprone.annotations.DoNotMock;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseAuthException;
//import com.google.firebase.auth.FirebaseUser;
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.*;
//
//public class LoginJUnitTest {
//    @Mock
//    FirebaseAuth mockFirebaseAuth;
//
//    @Mock
//    FirebaseAuth.AuthResult mockAuthResult;
//
//    @Mock
//    FirebaseUser mockUser;
//
//    @Mock
//    Toast mockToast;
//
//    LoginActivity loginActivity;
//
//    @Before
//    public void setUp() {
//        MockitoAnnotations.initMocks(this);
//        loginActivity = new LoginActivity();
//
//        // Set up FirebaseAuth mock
//        loginActivity.firebaseAuth = mockFirebaseAuth;
//        loginActivity.showToast = mockToast;
//    }
//
//
//    @Test
//    public void testLoginWithUnknownEmail() {
//        String unknownEmail = "unknown@example.com";
//        String password = "Password@123";
//
//        // Mocking Task to simulate a failed login with Firebase
//        // still error
//        // How to do UITest with real Firebase credentials
//        Task<AuthResult> failedTask = mock(Task.class);
//        when(failedTask.isSuccessful()).thenReturn(false);
//        when(failedTask.getException()).thenReturn(new FirebaseAuthException("ERROR_INVALID_EMAIL", "The email address is badly formatted."));
//
//        // Mock FirebaseAuth's createUserWithEmailAndPassword to return the failed task
//        when(mockFirebaseAuth.createUserWithEmailAndPassword(unknownEmail, password)).
//                thenReturn(failedTask);
//
//        when(mockFirebaseAuth.createUserWithEmailAndPassword(unknownEmail, password))
//                .thenReturn(Task.forException("Unknown Email"));
//
//        loginActivity.emailEditText.setText(unknownEmail);
//        loginActivity.passwordEditText.setText(password);
//        loginActivity.loginButton.performClick();
//
//        verify(mockToast).show();
//        assertTrue(mockToast.makeText().toString().contains("Login Successful"));
//    }
//
//    private Object when(Task<AuthResult> userWithEmailAndPassword) {
//    }
//}