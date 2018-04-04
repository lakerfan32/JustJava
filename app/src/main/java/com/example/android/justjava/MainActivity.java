package com.example.android.justjava;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    int quantity = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method is called when the plus button is clicked.
     */
    public void increment(View view) {
        if (quantity == 100) {
            // Show an error message as a toast
            Toast.makeText(this, "You cannot have more than 100 coffees", Toast.LENGTH_SHORT).show();

            // Exit this method early if nothing left to do
            return;
        }
        quantity = quantity + 1;
        displayQuantity(quantity);
    }

    /**
     * This method is called when the minus button is clicked.
     */
    public void decrement(View view) {
        if (quantity == 1) {
            // Show an error message as a toast
            Toast.makeText(this, "You cannot have less than 1 cup of coffee", Toast.LENGTH_SHORT).show();

            // Exit this method early if nothing left to do
            return;
        }
        quantity = quantity - 1;
        displayQuantity(quantity);
    }

    /**
     * This method is called when the order button is clicked.
     * Also, verifies user name and which Checkboxes are selected.
     */
    public void submitOrder(View view) {
        // Find user's name
        EditText nameField = (EditText) findViewById(R.id.name_field);
        String name = nameField.getText().toString();
        Log.v("Main Activity", "Name: " + name);

        // Determine if user wants whipped cream topping
        CheckBox whippedCreamCheckBox = (CheckBox) findViewById(R.id.whipped_cream_checkbox);
        boolean hasWhippedCream = whippedCreamCheckBox.isChecked();
        Log.v("Main Activity", "Has whipped cream: " + hasWhippedCream);

        //Determine if user wants chocolate topping
        CheckBox chocolateCheckBox = (CheckBox) findViewById(R.id.chocolate_checkbox);
        boolean hasChocolate = chocolateCheckBox.isChecked();
        Log.v("Main Activity", "Has chocolate: " + hasChocolate);

        int price = calculatePrice(hasWhippedCream, hasChocolate);
        String priceMessage = createOrderSummary(name, price, hasWhippedCream, hasChocolate);

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, "Just Java order for " + name);
        intent.putExtra(Intent.EXTRA_TEXT, priceMessage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }

        displayMessage(priceMessage);

    }

    /**
     * Calculates the price of the order.
     *
     * @param addWhippedCream means user has added whipped cream topping
     * @param addChocolate means user has added chocolate topping
     * @return total price
     */
    private int calculatePrice(boolean addWhippedCream, boolean addChocolate) {
        // Price of 1 cup of coffee is $5
        int basePrice = 5;

        // Add $1 if user wants whipped topping
        if (addWhippedCream) {
            basePrice = basePrice + 1;
        }

        // Add $2 if user wants chocolate topping
        if (addChocolate) {
            basePrice = basePrice + 2;
        }

        // Calculate total order price by multiplying by quantity
        return quantity * basePrice;
    }

    /**
     * Create Summary of the order.
     *
     * @param name of the customer
     * @param price of the order
     * @param addWhippedCream determines if user wants whipped cream topping
     * @param addChocolate determines if user wants chocolate topping
     * @return text of the order
     */
    private String createOrderSummary(String name, int price, boolean addWhippedCream, boolean addChocolate) {
        String priceMessage = "Name: " + name;
        priceMessage = priceMessage + "\n" + getString(R.string.summary_whipped_cream) + addWhippedCream;
        priceMessage = priceMessage + "\n" + getString(R.string.summary_chocolate) + addChocolate;
        priceMessage = priceMessage + "\n" + getString(R.string.summary_quantity) + quantity;
        priceMessage = priceMessage + "\n" + getString(R.string.summary_price,
                NumberFormat.getCurrencyInstance().format(price));
        priceMessage = priceMessage + "\n" + getString(R.string.thank_you);
        return priceMessage;
    }

    /**
     * This method displays the given text on the screen.
     */
    private void displayMessage(String message) {
        TextView orderSummaryTextView = (TextView) findViewById(R.id.order_summary_text_view);
        orderSummaryTextView.setText(message);
    }

    /**
     * This method displays the given quantity value on the screen.
     */
        private void displayQuantity(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }

}