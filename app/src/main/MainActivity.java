package android.assignment;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText title;
    private EditText description;
    private ListView listView;
    private Button button;

    private List<Content> datas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        title = (EditText) findViewById(R.id.title);
        description = (EditText) findViewById(R.id.description);
        listView = (ListView) findViewById(R.id.listView);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);
        listView.setAdapter(adapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                datas.remove(i);
                adapter.notifyDataSetChanged();
                return false;
            }
        });
    }

    private BaseAdapter adapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int i) {
            return datas.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item, null);
            Content content = datas.get(i);
            TextView title = (TextView) view.findViewById(R.id.title);
            TextView description = (TextView) view.findViewById(R.id.description);
            title.setText(content.getTitle());
            description.setText(content.getDescription());

            return view;
        }
    };

    @Override
    public void onClick(View view) {
        String mTitle = title.getText().toString();
        String mDescription = description.getText().toString();
        if ("".equals(mTitle) || "".equals(description)) {
            return;
        }

        File file = new File(Environment.getExternalStorageDirectory(),"datas.txt");
        try {
            FileOutputStream out = new FileOutputStream(file,true);
            out.write((mTitle+"###"+mDescription+"\n").getBytes());
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        datas.add(new Content(mTitle, mDescription));
        adapter.notifyDataSetChanged();
        listView.setSelection(datas.size() - 1);
        title.setText("");
        description.setText("");
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        File file = new File(Environment.getExternalStorageDirectory(),"datas.txt");
//        try {
//            Scanner scanner = new Scanner(file);
//            datas.clear();
//            while (scanner.hasNext()) {
//                String[] str = scanner.nextLine().split("###");
//                if (str.length == 2)
//                    datas.add(new Content(str[0],str[1]));
//            }
//            adapter.notifyDataSetChanged();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
}
