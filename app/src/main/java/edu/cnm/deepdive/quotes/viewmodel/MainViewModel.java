package edu.cnm.deepdive.quotes.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import edu.cnm.deepdive.quotes.model.entity.Quote;
import edu.cnm.deepdive.quotes.service.QuotesRepository;
import io.reactivex.disposables.CompositeDisposable;
import java.util.List;

public class MainViewModel extends AndroidViewModel {


  private final QuotesRepository repository;
  private final MutableLiveData<List<Quote>> quotes;
  private final MutableLiveData<Throwable> throwable;
  private final CompositeDisposable pending;




  public MainViewModel(@NonNull Application application) {
    super(application);
    repository = new QuotesRepository(application);
    quotes = new MutableLiveData<>();
    throwable = new MutableLiveData<>();
    pending = new CompositeDisposable();
    loadQuotes();

  }
  public MutableLiveData<List<Quote>> getQuotes() {
    return quotes;
  }

  public MutableLiveData<Throwable> getThrowable() {
    return throwable;
  }
  private void loadQuotes() {
    pending.add(
    repository.getQuotes()
        .subscribe(
            (quotes) -> this.quotes.postValue(quotes),
            (throwable) -> this.throwable.postValue(throwable)
        )
    );
  }
}