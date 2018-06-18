package br.com.developers.perloti.bookmanager.model;

import android.util.Log;

import java.util.List;

import br.com.developers.perloti.bookmanager.util.BMUtil;
import io.realm.Realm;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;

/**
 * Created by perloti on 18/06/18.
 */

public class BookRepository {

    public static void save(Book book) {
        try {
            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            realm.copyToRealm(book);
            realm.commitTransaction();
            realm.close();
        } catch (RealmPrimaryKeyConstraintException e) {
            Log.e(BMUtil.TAG_PERSISTENCE, "Enter in catch - ID already exists");
            Log.e(BMUtil.TAG_PERSISTENCE, e.getMessage());
            update(book);
        }
    }

    public static void removeById(String id) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        Book feedHash = realm.where(Book.class).equalTo("id", id).findFirst();
        feedHash.deleteFromRealm();
        realm.commitTransaction();
        realm.close();
    }

    public static void update(Book bookNew) {
        Realm realm = Realm.getDefaultInstance();
        realm.copyToRealmOrUpdate(bookNew);
        realm.commitTransaction();
        realm.close();
    }

    public static Book getBookById(String id) {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(Book.class).equalTo("id", id).findFirst();
    }

    public static List<Book> getBooksAll() {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(Book.class).findAll();
    }

    public static List<Book> getBookByStatus(String status) {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(Book.class).equalTo("status", status).findAll();
    }

}
