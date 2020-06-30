package edu.cnm.deepdive.quotes.service;

import android.content.Context;
import edu.cnm.deepdive.quotes.model.dao.SourceDao;
import edu.cnm.deepdive.quotes.model.entity.Source;
import java.util.List;

public class SourcesRepository {

  private final Context context;
  private final QuotesDatabase database;
  private final SourceDao sourceDao;

  public SourcesRepository(Context context) {
    this.context = context;
    database = QuotesDatabase.getInstance();
    sourceDao = database.getSourceDao();
  }

  public LiveData<List<Source>> getAll() {
    return sourceDao.selectAll();
  }
  //TODO add methods for save, delete, get.
}
