package com.example.librascanapp;

import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LogsActivity extends AppCompatActivity {

    private TableLayout logsTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_logs);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        logsTable = findViewById(R.id.table);

        FirebaseDatabase.getInstance().getReference("logs")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            for (DataSnapshot logSnapshot : snapshot.getChildren()){
                                String studentId = logSnapshot.child("logID").getValue(String.class);
                                String timestamp = logSnapshot.child("timestamp").getValue(String.class);

                                String[] dateTime = timestamp.split(" ");
                                String date = dateTime[0];
                                String time = dateTime[1];

                                fetchLogsData(studentId,date, time);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(LogsActivity.this, "Error: "+ error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void fetchLogsData(String studentID, String date, String time){
        FirebaseDatabase.getInstance().getReference("Students").child(studentID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            String id = snapshot.child("id").getValue(String.class);
                            String name = snapshot.child("name").getValue(String.class);
                            String courseYr = snapshot.child("courseYr").getValue(String.class);
                            String department = snapshot.child("department").getValue(String.class);

                            addTableRow(id, name, courseYr, department,date, time);
                        }else {
                            Toast.makeText(LogsActivity.this, "Student not found!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(LogsActivity.this, "Error: "+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addTableRow(String id, String name, String courseYr, String department, String date, String time){
        TableRow tableRow = new TableRow(this);
        tableRow.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        tableRow.addView(createTextView(id));
        tableRow.addView(createTextView(name));
        tableRow.addView(createTextView(courseYr));
        tableRow.addView(createTextView(department));
        tableRow.addView(createTextView(date));
        tableRow.addView(createTextView(time));

        logsTable.addView(tableRow);
    }

    private TextView createTextView(String Text){
        TextView textView = new TextView(this);
        textView.setText(Text);
        textView.setPadding(8,8,8,8);
        textView.setGravity(Gravity.CENTER);

        return textView;
    }
}