package th.ac.tu.siit.its333.lab4exercise1;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;


public class ListCourseActivity extends ActionBarActivity implements AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener {

    CourseDBHelper helper;
    SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_course);

        helper = new CourseDBHelper(this);


        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT _id, code, (grade || ' (' || credit || ' Credits)') g FROM course ORDER BY _id ASC;",
                null);

        adapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_2,
                cursor,
                new String[] {"code", "g"},
                new int[] {android.R.id.text1, android.R.id.text2},
                0);

        ListView lv = (ListView)findViewById(R.id.listView);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);
        lv.setOnItemLongClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view,
                            int position, long id) {
        Toast t = Toast.makeText(this.getApplicationContext(),
                "course id=" + id + " is clicked",
                Toast.LENGTH_SHORT);
        t.show();

        Log.d("course", id + " is clicked");
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view,
                                   int position, long id) {
        SQLiteDatabase db = helper.getWritableDatabase();

        int n = db.delete("course",
                "_id = ?",
                new String[]{Long.toString(id)});

        if (n == 1) {
            Toast t = Toast.makeText(this.getApplicationContext(),
                    "Successfully deleted the selected item.",
                    Toast.LENGTH_SHORT);
            t.show();

            // retrieve a new collection of records
            Cursor cursor = db.rawQuery(
                    "SELECT _id, code, (grade || ' ' || credit) g FROM course ORDER BY _id ASC;",
                    null);

            // update the adapter
            adapter.changeCursor(cursor);
        }
        db.close();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_course, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
