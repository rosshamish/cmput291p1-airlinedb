package com.cmput291.rhanders_abradsha_dshin;

/**
 * Created by ross on 15-10-23.
 */
public enum SQLQueries {
    // -------
    // HEY YOU: Use these like SQLQueries.Search.toString()
    // -------

    Search("select * from table" +
           "where a=b"),
    List("select * from table" +
         "where b=c")
    ;

    private final String text;

    private SQLQueries(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return this.text;
    }
}
