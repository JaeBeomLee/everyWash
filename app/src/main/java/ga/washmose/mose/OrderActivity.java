package ga.washmose.mose;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class OrderActivity extends AppCompatActivity {
    final int REQ_ADD_ITEMS = 1;
    Button addItems;
    Button dateSelect;
    int year, month, day;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        dateSelect = (Button) findViewById(R.id.order_date_select);
//        addItems = (Button)findViewById(R.id.order_add_items);

        dateSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GregorianCalendar calendar = new GregorianCalendar();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day= calendar.get(Calendar.DAY_OF_MONTH);
                new DatePickerDialog(OrderActivity.this, dateSetListener, year, month, day).show();
            }
        });
//        addItems.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(OrderActivity.this, OrderItemList.class);
//                startActivityForResult(intent, REQ_ADD_ITEMS);
//
//            }
//        });

    }

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            String msg = String.format("%d / %d / %d", year,monthOfYear+1, dayOfMonth);
            Toast.makeText(OrderActivity.this, msg, Toast.LENGTH_SHORT).show();
        }
    };

}
