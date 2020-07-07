package edu.cnm.deepdive.quotes.service;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import edu.cnm.deepdive.quotes.BuildConfig;

public class GoogleSignInService {

  private static Application context;

  private final GoogleSignInClient client;
  private final MutableLiveData<GoogleSignInAccount> account;
  private final MutableLiveData<Throwable> throwable;

  private GoogleSignInService() {   //the constructor
    account = new MutableLiveData<>();
    throwable = new MutableLiveData<>();
    GoogleSignInOptions options = new GoogleSignInOptions.Builder()  //create a builder object and return it, don't assign
        .requestEmail()  //invoke this request that return an builder object whit which invoke the next.
        .requestId()
        .requestProfile()
        // .requestIdToken(BuildConfig.CLIENT_ID)
        .build(); // this return an option object
    client = GoogleSignIn.getClient(context, options);
  }

  public static void setContext(Application context) {
    GoogleSignInService.context = context;
  }

  public static GoogleSignInService getInstance() {
    return InstanceHolder.INSTANCE;  // way to implement Singleton pattern
  }

  //  Adding methods for getting LiveData, refreshing login, starting a new login, and logging out.

  public LiveData<GoogleSignInAccount> getAccount() {
    return account;
  }

  public LiveData<Throwable> getThrowable() {
    return throwable;
  }

  public Task<GoogleSignInAccount> refresh() {
    return client.silentSignIn()
        .addOnSuccessListener(this::update)  //lambda method reference here
        .addOnFailureListener(this::update);
  }

  public void startSignIn(Activity activity, int requestCode) {
    update((GoogleSignInAccount) null); //casting to show specific what GSIA
    Intent intent = client.getSignInIntent();
    activity.startActivityForResult(intent, requestCode);
  }

  public Task<GoogleSignInAccount> completeSignIn(Intent data) {
    Task<GoogleSignInAccount> task = null;
    try {
      task = GoogleSignIn.getSignedInAccountFromIntent(data);
      update(task.getResult(ApiException.class));
    } catch (ApiException e) {
      update(e);
    }
    return task;
  }

  public Task<Void> signOut() {
    return client.signOut()
        .addOnCompleteListener((ignored) -> update((GoogleSignInAccount) null));
  }

  private void update(GoogleSignInAccount account) {
    this.account.setValue(account);
    this.throwable.setValue(null);
  }

  private void update(Throwable throwable) {
    this.account.setValue(null);
    this.throwable.setValue(throwable);

  }

  private static class InstanceHolder {

    private static final GoogleSignInService INSTANCE = new GoogleSignInService(); //the class is load in the memory when is a request for getInstance and keep the result for next requests. Only happens once.
  }
}
